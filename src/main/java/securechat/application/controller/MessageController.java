package securechat.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import securechat.application.dtos.*;
import securechat.application.entity.MessageEntity;
import securechat.application.service.MessageService;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController
{
    @Autowired
    private MessageService messageservice;

    @PostMapping("/encrypt/single/message")
    public ResponseEntity<String> encryptSingleMsg(@RequestBody EncryptDto encryptdto)
    {
        System.out.println("honeycipher = "+encryptdto.getNormaltext());
        System.out.println("honeykey = "+encryptdto.getHoneykey());

        String honeycipher = messageservice.encryptSingleMessage(encryptdto);

        return new ResponseEntity<>(honeycipher , HttpStatus.FOUND);
    }

   /* @PostMapping("/decrypt")
    public ResponseEntity<String> decryptSingleMsg(@RequestBody DecryptRequest decryptreq)
    {
        System.out.println(decryptreq);

        String decryptedmessage = messageservice.decryptSingleEncryptedMessage( decryptreq );

        return new ResponseEntity<>(decryptedmessage , HttpStatus.FOUND);
    }*/

    @PostMapping("/decrypt/single/message")
    public ResponseEntity<String> decryptSingleMsg(@RequestBody DecryptRequest decryptreq)
    {
        String decryptedmessage = messageservice.decryptSingleEncryptedMessage( decryptreq );

        return new ResponseEntity<>(decryptedmessage , HttpStatus.FOUND);
    }


   /* @PostMapping("/decrypt/list/of/messages")
    public ResponseEntity<List<String>> decryptLIstTest(@RequestBody ListofEncryptedMsgsDto decryptallmsgs)
    {
        System.out.println(decryptallmsgs);

        List<String> decryptedmessage = messageservice.decryptListOfEncryptedMessages( decryptallmsgs );

        return new ResponseEntity<>(decryptedmessage , HttpStatus.FOUND);
    }*/

    @PostMapping("/decrypt/bulk/messages")
    public ResponseEntity<List<MessageEntity>> decryptAllMessages(@RequestBody ListofEncryptedMsgsDto decryptallmsgs)
    {
        List<MessageEntity> allencryptedmessages =decryptallmsgs.getAllencryptedmessages();

        List<MessageEntity> decryptedmessage = messageservice.ListOfEncryptedMessages( decryptallmsgs );

        return new ResponseEntity<>(decryptedmessage , HttpStatus.FOUND);
    }


}
