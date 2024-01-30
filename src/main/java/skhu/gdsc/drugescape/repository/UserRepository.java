package skhu.gdsc.drugescape.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import skhu.gdsc.drugescape.domain.User;
import skhu.gdsc.drugescape.domain.UserRole;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);

    // ADMIN이 User 검색 시
    Page<User> findAllByNicknameContains(String nickname, PageRequest pageRequest);

    // 회원 가입 시 중복 체크용
    Boolean existsByLoginId(String loginId);
    Boolean existsByNickname(String nickname);
}
