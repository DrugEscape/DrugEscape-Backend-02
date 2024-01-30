package skhu.gdsc.drugescape.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import skhu.gdsc.drugescape.domain.Board;
import skhu.gdsc.drugescape.domain.UploadImage;

import java.time.LocalDateTime;

@Data
@Builder
public class BoardResponseDto {
    private Long id;
    private String userLoginId;
    private String userNickname;
    private String title;
    private String content;
    private Integer likeCnt;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private MultipartFile newImage;
    private UploadImage uploadImage;

    public static BoardResponseDto of(Board board) {
        return BoardResponseDto.builder()
                .id(board.getId())
                .userLoginId(board.getUser().getLoginId())
                .userNickname(board.getUser().getNickname())
                .title(board.getTitle())
                .content(board.getContent())
                .createdAt(board.getCreatedAt())
                .lastModifiedAt(board.getLastModifiedAt())
                .likeCnt(board.getLikes().size())
                .uploadImage(board.getUploadImage())
                .build();
    }

}
