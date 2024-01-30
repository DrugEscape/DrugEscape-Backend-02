package skhu.gdsc.drugescape.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import skhu.gdsc.drugescape.domain.Board;
import skhu.gdsc.drugescape.domain.User;

@Data
@Builder
public class BoardRequestDto {

    private String title;
    private String content;
    private MultipartFile uploadImage;

    public Board toEntity(User user) {
        return Board.builder()
                .user(user)
                .title(title)
                .content(content)
                .likeCnt(0)
                .commentCnt(0)
                .build();
    }
}
