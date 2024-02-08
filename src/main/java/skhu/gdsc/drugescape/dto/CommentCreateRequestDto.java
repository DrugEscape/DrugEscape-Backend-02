package skhu.gdsc.drugescape.dto;

import lombok.Data;
import skhu.gdsc.drugescape.domain.Board;
import skhu.gdsc.drugescape.domain.Comment;
import skhu.gdsc.drugescape.domain.User;

@Data
public class CommentCreateRequestDto {

    private String content;

    public Comment toEntity(Board board, User user) {
        return Comment.builder()
                .user(user)
                .board(board)
                .content(content)
                .build();
    }
}
