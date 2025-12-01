package securechat.application.dtos;

import lombok.Data;

@Data
public class SendMessageRequest
{
    private String plaintext;
    private String honeyKey;

}