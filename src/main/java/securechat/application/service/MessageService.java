package securechat.application.service;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import securechat.application.dtos.DecryptRequest;
import securechat.application.dtos.SendMessageRequest;
import securechat.application.dtos.SendMessageResponse;
import securechat.application.entity.Message;
import securechat.application.repo.MessageRepository;

import java.time.Instant;

@Service
public class MessageService
{

    @Autowired
    private  MessageRepository repo;

    @Autowired
    private  HoneyService honeyservice;

    @Autowired
    private ModelMapper modelmapper;

    public SendMessageResponse send(SendMessageRequest sendmsgreq)
    {
        String honeycipher = honeyservice.encode(sendmsgreq.getPlaintext(), sendmsgreq.getHoneyKey());

        /*Message msg = Message.builder()
                .senderId(req.getSenderId())
                .receiverId(req.getReceiverId())
                .honeyCipher(cipher)
                .timestamp(Instant.now().getEpochSecond())
                .build();

         */

        // creating new message object and filling with the details present in the SendMessageRequest details
        Message message = new Message();
        //message.setSenderId(sendmsgreq.getSenderId());
        //message.setReceiverId(sendmsgreq.getReceiverId());
        message.setHoneyCipher(honeycipher);
        message.setTimestamp(Instant.now());

        //test
        System.out.println(message);

        Message savedmessage = repo.save(message);

        //test
        System.out.println("message object after saving"+savedmessage);

        SendMessageResponse resp = new SendMessageResponse();
        resp.setMessageId(message.getId());
        resp.setHoneyCipher(honeycipher);
        resp.setTimestamp(message.getTimestamp());


       /* try
        {
            Message message = modelmapper.map(sendmsgreq , Message.class);
            message.setHoneyCipher(honeycipher);
            repo.save(message);


            resp.setMessageId(message.getId());
            resp.setHoneyCipher(honeycipher);
            resp.setTimestamp(message.getTimestamp());
        }
        catch (Exception e)
        {
            System.out.println("modelmapper is not working and there is problem in the modelmapper logic change the values in the entity and the dto classes");
        }

    */

        return resp;
    }

    public String decrypt(Long messageId, String honeyKey)
    {
        Message msg = repo.findById(messageId).orElseThrow();

        System.out.println("decrypted message from the database fetched from the message id "+messageId+"="+msg);

        String decryptedmessage = honeyservice.decode(msg.getHoneyCipher(), honeyKey);

        return decryptedmessage;
    }

    public String decrypttestwithoutMessageId(DecryptRequest decryptreq)
    {
        String honeykey = decryptreq.getHoneyKey();
        String honeycipher = decryptreq.getHoneyCipher();

        String decryptedmessage = honeyservice.decode(honeycipher , honeykey);

        return decryptedmessage;
    }
}
