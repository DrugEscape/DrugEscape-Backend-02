package skhu.gdsc.drugescape.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardSearchRequestDto {
    private String searchType; // 검색 타입
    private String keyword; // 검색 내용
}
