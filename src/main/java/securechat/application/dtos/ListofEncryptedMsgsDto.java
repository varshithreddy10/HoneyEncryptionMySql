package securechat.application.dtos;

import lombok.Data;
import securechat.application.entity.MessageEntity;

import java.util.ArrayList;
import java.util.List;

@Data
public class ListofEncryptedMsgsDto
{
    private List<MessageEntity> allencryptedmessages = new ArrayList<>();
    private String honeykey;
}
