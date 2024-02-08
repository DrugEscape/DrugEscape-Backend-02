package skhu.gdsc.drugescape.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;     // 로그인할 때 사용하는 아이디
    private String password;    // 비밀번호
    private String nickname;    // 닉네임
    private LocalDateTime createdAt;    // 가입 시간
    private Integer receivedLikeCnt; // 유저가 받은 좋아요 개수

    @Enumerated(EnumType.STRING)
    private UserRole userRole;      // 권한

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Board> boards;     // 작성글

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Like> likes;       // 유저가 누른 좋아요

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Comment> comments; // 댓글

    public void likeChange(Integer receivedLikeCnt) {
        this.receivedLikeCnt = receivedLikeCnt;
    }

    public void edit(String newPassword, String newNickname) {
        this.password = newPassword;
        this.nickname = newNickname;
    }

}