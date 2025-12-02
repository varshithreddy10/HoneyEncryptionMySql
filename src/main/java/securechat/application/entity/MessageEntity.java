package securechat.application.entity;

import lombok.Data;

import java.time.Instant;

@Data
public class MessageEntity
{
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;

    private ChatroomEntity chatRoom;

    private Instant timestamp;
}
