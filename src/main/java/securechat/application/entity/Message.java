package securechat.application.entity;

import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message
{
    private String honeyCipher;

    private Instant timestamp;
}
