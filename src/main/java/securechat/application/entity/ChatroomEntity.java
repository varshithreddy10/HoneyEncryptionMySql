package securechat.application.entity;


import lombok.Data;

@Data
public class ChatroomEntity
{
    private Long id;

    private Long user1Id;

    private Long user2Id;
}
