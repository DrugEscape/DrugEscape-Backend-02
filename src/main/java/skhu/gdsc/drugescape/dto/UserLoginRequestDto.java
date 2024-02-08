package skhu.gdsc.drugescape.dto;

import lombok.Data;

@Data
public class UserLoginRequestDto {

    private String loginId;
    private String password;
}
