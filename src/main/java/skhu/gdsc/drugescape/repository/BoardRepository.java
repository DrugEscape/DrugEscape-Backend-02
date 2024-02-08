package skhu.gdsc.drugescape.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import skhu.gdsc.drugescape.domain.Board;
import skhu.gdsc.drugescape.domain.UserRole;
import skhu.gdsc.drugescape.service.BoardService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByTitleContainsAndUserUserRoleNot(String keyword, UserRole userRole, PageRequest pageRequest);

    Page<Board> findAllByUserNicknameContainsAndUserUserRoleNot(String keyword, UserRole userRole, PageRequest pageRequest);

    Page<Board> findAllByUserUserRoleNot(UserRole userRole, PageRequest pageRequest);

    Collection<? extends Board> findAllByUserLoginId(String loginId);


}
