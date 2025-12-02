package securechat.application.dtos;

import lombok.Data;

@Data
public class EncryptDto
{
    private String normaltext;
    private String honeykey;
}
