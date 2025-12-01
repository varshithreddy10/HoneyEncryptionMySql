package securechat.application.dtos;

import lombok.Data;

import java.time.Instant;

@Data
public class SendMessageResponse
{
    private Long messageId;
    private String honeyCipher;
    private Instant timestamp;

}
