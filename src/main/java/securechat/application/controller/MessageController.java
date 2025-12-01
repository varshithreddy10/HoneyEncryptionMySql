package securechat.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import securechat.application.dtos.DecryptRequest;
import securechat.application.dtos.SendMessageRequest;
import securechat.application.dtos.SendMessageResponse;
import securechat.application.service.MessageService;

@RestController
@RequestMapping("/api/messages")
public class MessageController
{
    @Autowired
    private MessageService messageservice;


    @PostMapping("/send")
    public ResponseEntity<SendMessageResponse> send(@RequestBody SendMessageRequest sendmsgreq)
    {
        SendMessageResponse sendmessageresponse = messageservice.send(sendmsgreq);

        return new ResponseEntity<>(sendmessageresponse , HttpStatus.FOUND);
    }

    @PostMapping("/decrypt/{id}")
    public ResponseEntity<String> decrypt(@PathVariable Long id, @RequestBody DecryptRequest decryptreq)
    {
        System.out.println(decryptreq);

        String decryptedmessage = messageservice.decrypt(id, decryptreq.getHoneyKey());

        return new ResponseEntity<>(decryptedmessage , HttpStatus.FOUND);
    }

    @PostMapping("/decrypt")
    public ResponseEntity<String> decryptTest(@RequestBody DecryptRequest decryptreq)
    {
        System.out.println(decryptreq);

        String decryptedmessage = messageservice.decrypttestwithoutMessageId( decryptreq );

        return new ResponseEntity<>(decryptedmessage , HttpStatus.FOUND);
    }
}
