package securechat.application.dtos;

import lombok.Data;

@Data
public class DecryptRequest
{
    private String honeyCipher;
    private String honeyKey;
}