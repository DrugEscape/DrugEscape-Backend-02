package skhu.gdsc.drugescape.dto;

import lombok.Data;
import skhu.gdsc.drugescape.domain.User;
import skhu.gdsc.drugescape.domain.UserRole;

import java.time.LocalDateTime;

@Data
public class UserJoinRequestDto {

    private String loginId;
    private String password;
    private String passwordCheck;
    private String nickname;

    public User toEntity(String encodedPassword) {
        return User.builder()
                .loginId(loginId)
                .password(encodedPassword)
                .nickname(nickname)
                .userRole(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .receivedLikeCnt(0)
                .build();
    }
}
