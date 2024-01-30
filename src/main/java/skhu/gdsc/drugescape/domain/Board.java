package skhu.gdsc.drugescape.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import skhu.gdsc.drugescape.dto.BoardResponseDto;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Board extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;   // 제목

    private String content;    // 본문

    @ManyToOne(fetch = FetchType.LAZY)
    private User user; // 작성자

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Like> likes;       // 좋아요
    private Integer likeCnt;        // 좋아요 수

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Comment> comments; // 댓글
    private Integer commentCnt;     // 댓글 수

    @OneToOne(fetch = FetchType.LAZY)
    private UploadImage uploadImage;

    public void update(BoardResponseDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
    }

    public void likeChange(Integer likeCnt) {
        this.likeCnt = likeCnt;
    }

    public void commentChange(Integer commentCnt) {
        this.commentCnt = commentCnt;
    }

    public void setUploadImage(UploadImage uploadImage) {
        this.uploadImage = uploadImage;
    }

}