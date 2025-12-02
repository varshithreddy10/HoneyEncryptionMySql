package securechat.application.service;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import securechat.application.dtos.*;
import securechat.application.entity.Message;
import securechat.application.entity.MessageEntity;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService
{


    @Autowired
    private  HoneyService honeyservice;

    @Autowired
    private ModelMapper modelmapper;

    public String encryptSingleMessage(EncryptDto encryptdto)
    {
        String honeycipher = honeyservice.encode(encryptdto.getNormaltext(), encryptdto.getHoneykey());

        // creating new message object and filling with the details present in the SendMessageRequest details
        Message message = new Message();
        message.setHoneyCipher(honeycipher);
        message.setTimestamp(Instant.now());

        return honeycipher;
    }

    public String decryptSingleEncryptedMessage(DecryptRequest decryptreq)
    {
        String honeykey = decryptreq.getHoneyKey();
        String honeycipher = decryptreq.getHoneyCipher();

        String decryptedmessage = honeyservice.decode(honeycipher , honeykey);

        return decryptedmessage;
    }

    public String decryptSingleMsg(String honeycipher , String honeykey)
    {

        String decryptedmessage = honeyservice.decode(honeycipher , honeykey);

        return decryptedmessage;
    }


    /*public List<String> decryptAllMessages(ListofEncryptedMsgsDto decryptallmsgs)
    {
        List<DecryptRequest> allencryptedmessages = decryptallmsgs.getAllencryptedmessages();

        List<String> alldecryptedlistmsgs = new ArrayList<>();

        for(DecryptRequest decryptrequest : allencryptedmessages)
        {
            String msgafterdecryption = decryptSingleEncryptedMessage(decryptrequest);
            alldecryptedlistmsgs.add(msgafterdecryption);
        }

        return alldecryptedlistmsgs;
    }*/

    public List<MessageEntity>  ListOfEncryptedMessages(ListofEncryptedMsgsDto decryptallmsgs)
    {
        List<MessageEntity> allencryptedmessages = decryptallmsgs.getAllencryptedmessages();

        String honeykey = decryptallmsgs.getHoneykey();

        //List<String> alldecryptedlistmsgs = new ArrayList<>();

        List<MessageEntity> alldecryptedMsgs = new ArrayList<>();

        for(MessageEntity messageentity : allencryptedmessages)
        {
            String honeycipher  = messageentity.getContent();
            String msgafterdecryption = decryptSingleMsg(honeycipher , honeykey);
           // alldecryptedlistmsgs.add(msgafterdecryption);

            messageentity.setContent(msgafterdecryption);
            alldecryptedMsgs.add(messageentity);
        }

        return alldecryptedMsgs;
    }
}
