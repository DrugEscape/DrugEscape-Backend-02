package skhu.gdsc.drugescape.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import skhu.gdsc.drugescape.domain.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoardId(Long boardId);
    List<Comment> findAllByUserLoginId(String loginId);
}
