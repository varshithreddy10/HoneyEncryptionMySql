package securechat.application.dtos;

import lombok.Data;

import java.time.Instant;

@Data
public class SendMessageResponse
{
    private String honeyCipher;
    private Instant timestamp;

}
