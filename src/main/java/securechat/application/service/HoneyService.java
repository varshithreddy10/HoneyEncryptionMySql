package securechat.application.service;

import org.springframework.stereotype.Service;
import securechat.application.util.CryptoUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Locale;
import java.util.Random;

@Service
public class HoneyService
{

    private static final String MAGIC_TAG = "##REAL##";

    // =============================
    //   CREATING REALISTIC FAKE MESSAGE ENGINE
    // =============================

    private static final String[] TEMPLATES = {
            "{SUBJECT} {ACTION} {TIME}",
            "{PERSON} said {SUBJECT} was {ACTION} {TIME}",
            "{SUBJECT} just {ACTION} {TIME}",
            "Hey, {SUBJECT} {ACTION} {TIME}",
            "FYI, {SUBJECT} {ACTION} {TIME}",
            "{SUBJECT} {ACTION} {TIME}, check {LOCATION}",
            "{SUBJECT} {ACTION}. Will update {TIME}"
    };

    private static final String[] SUBJECTS = {
            "The meeting", "Your request", "The project", "The report", "The manager",
            "The package", "The file", "The invoice", "The contract", "The delivery",
            "The update", "Your order", "The build", "The release", "The ticket",
            "The application", "The form", "The memo", "The batch", "The message"
    };

    private static final String[] ACTIONS = {
            "was approved", "is delayed", "was cancelled", "is ready",
            "was updated", "needs review", "was delivered", "left early",
            "is pending", "is scheduled", "is postponed", "is completed",
            "was rejected", "is under review", "was escalated", "is being processed",
            "was verified", "is confirmed", "was uploaded", "is being checked"
    };

    private static final String[] TIMES = {
            "by tonight", "after lunch", "tomorrow morning", "this afternoon",
            "next week", "at 5 PM", "on Monday", "before EOD", "right now",
            "shortly", "in 2 hours", "this evening", "by Friday", "on Tuesday",
            "first thing tomorrow", "within 24 hours", "ASAP", "by noon",
            "after the call", "later today"
    };

    private static final String[] LOCATIONS = {
            "in the office", "at reception", "near the lobby", "on Slack",
            "in the portal", "on the desk", "in the inbox", "on the tracker",
            "in the shared drive", "in the repository", "on the board", "in the channel"
    };

    private static final String[] PERSONS = {
            "Alice", "Bob", "Charlie", "Dana", "Eve", "Frank",
            "Grace", "Heidi", "Ivan", "Judy"
    };

    // =============================
    //             ENCODE
    // =============================

    public String encode(String plaintext, String honeyKey)
    {

        String taggedMessage = MAGIC_TAG + plaintext;

        byte[] messageBytes = taggedMessage.getBytes(StandardCharsets.UTF_8);

        byte[] nonce = CryptoUtil.random(16);
        byte[] keyStream = generateKeystream(honeyKey.getBytes(), nonce, messageBytes.length);

        byte[] cipherBytes = xor(messageBytes, keyStream);

        byte[] finalPayload = new byte[nonce.length + cipherBytes.length];
        System.arraycopy(nonce, 0, finalPayload, 0, nonce.length);
        System.arraycopy(cipherBytes, 0, finalPayload, nonce.length, cipherBytes.length);

        return Base64.getEncoder().encodeToString(finalPayload);
    }

    // =============================
    //             DECODE
    // =============================

    public String decode(String honeyCipher, String honeyKey)
    {
        try
        {
            byte[] decodedRaw = Base64.getDecoder().decode(honeyCipher);

            if (decodedRaw.length < 16)
            {
                return generateFakeMessage(honeyCipher, honeyKey);
            }

            byte[] nonce = new byte[16];
            System.arraycopy(decodedRaw, 0, nonce, 0, 16);

            int cipherLength = decodedRaw.length - 16;
            byte[] cipherBytes = new byte[cipherLength];
            System.arraycopy(decodedRaw, 16, cipherBytes, 0, cipherLength);

            byte[] keyStream = generateKeystream(honeyKey.getBytes(), nonce, cipherLength);

            byte[] recoveredBytes = xor(cipherBytes, keyStream);
            String recoveredString = new String(recoveredBytes, StandardCharsets.UTF_8);

            if (recoveredString.startsWith(MAGIC_TAG))
            {
                return recoveredString.substring(MAGIC_TAG.length());
            }

            return generateFakeMessage(honeyCipher, honeyKey);

        }
        catch (Exception e)
        {
            //testing
            e.printStackTrace();
            return generateFakeMessage(honeyCipher, honeyKey);
        }
    }

    // =============================
    //     REALISTIC FAKE MESSAGE
    // =============================

    private String generateFakeMessage(String cipher, String key)
    {

        byte[] hash = sha256(cipher + "|" + key);

        long seed = deriveLongSeed(hash);

        Random rand = new Random(seed);

        String template = TEMPLATES[rand.nextInt(TEMPLATES.length)];

        String subject = SUBJECTS[rand.nextInt(SUBJECTS.length)];
        String action = ACTIONS[rand.nextInt(ACTIONS.length)];
        String time = TIMES[rand.nextInt(TIMES.length)];
        String location = LOCATIONS[rand.nextInt(LOCATIONS.length)];
        String person = PERSONS[rand.nextInt(PERSONS.length)];

        String fake = template
                .replace("{SUBJECT}", subject)
                .replace("{ACTION}", action)
                .replace("{TIME}", time)
                .replace("{LOCATION}", location)
                .replace("{PERSON}", person);

        // Optional: 5% chance to add a small code (natural, occasional)
        if (rand.nextInt(100) < 5)
        {
            String code = String.format(Locale.ROOT, "%05d", rand.nextInt(100000));
            fake += " (" + code + ")";
        }

        return fake;
    }

    private byte[] sha256(String input)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(input.getBytes(StandardCharsets.UTF_8));
        }
        catch (Exception e)
        {
            return input.getBytes(StandardCharsets.UTF_8);
        }
    }

    private long deriveLongSeed(byte[] hash)
    {

        if (hash.length < 16)
        {
            long fallback = 0;
            for (byte b : hash)
                fallback = (fallback << 8) | (b & 0xff);
            return fallback;
        }

        long a = 0, b = 0;

        for (int i = 0; i < 8; i++)
            a = (a << 8) | (hash[i] & 0xff);

        for (int i = 8; i < 16; i++)
            b = (b << 8) | (hash[i] & 0xff);

        return a ^ b;
    }

    // =============================
    //            HELPERS
    // =============================

    private byte[] xor(byte[] data, byte[] keyStream)
    {
        byte[] out = new byte[data.length];
        for (int i = 0; i < data.length; i++)
        {
            out[i] = (byte) (data[i] ^ keyStream[i]);
        }
        return out;
    }

    private byte[] generateKeystream(byte[] key, byte[] nonce, int length)
    {
        byte[] keyStream = new byte[length];
        byte[] currentHash = nonce;
        int bytesGenerated = 0;

        while (bytesGenerated < length)
        {
            currentHash = CryptoUtil.hmac(key, currentHash);
            int copyLen = Math.min(currentHash.length, length - bytesGenerated);
            System.arraycopy(currentHash, 0, keyStream, bytesGenerated, copyLen);
            bytesGenerated += copyLen;
        }
        return keyStream;
    }
}
