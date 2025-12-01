package securechat.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "messages")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   // private String senderId;
   // private String receiverId;

    @Lob
    private String honeyCipher;

    private Instant timestamp;
}
