package skhu.gdsc.drugescape.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import skhu.gdsc.drugescape.domain.Board;
import skhu.gdsc.drugescape.dto.*;
import skhu.gdsc.drugescape.service.BoardService;
import skhu.gdsc.drugescape.service.UploadImageService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:1573"})
@RestController
@RequestMapping("/share")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final UploadImageService uploadImageService;


    @GetMapping
    public List<Board> getAllBoards() {
        return boardService.getAllBoard();
    }

    // 검색 조건에 따라 필터링된 결과 반환
    @GetMapping("/list")
    public Page<Board> getBoardList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String keyword
    ) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        return boardService.getBoardList(pageRequest, searchType, keyword);
    }

    // 특정 상세정보 반환
    @GetMapping("/{boardId}")
    public BoardResponseDto getBoard(@PathVariable Long boardId) {
        return boardService.getBoard(boardId);
    }

    @PostMapping("/post")
    public Long writeBoard(@RequestBody BoardRequestDto req, Authentication auth) throws IOException {
        String loginId = auth.getName();
        return boardService.writeBoard(req, loginId, auth);
    }

    @PutMapping("/post/edit/{boardId}")
    public Long editBoard(@PathVariable Long boardId, @RequestBody BoardResponseDto dto) throws IOException {
        return boardService.editBoard(boardId, dto);
    }

    @DeleteMapping("post/delete/{boardId}")
    public Long deleteBoard(@PathVariable Long boardId) throws IOException {
        return boardService.deleteBoard(boardId);
    }

    // 특정 카테고리에 속한 사용자의 게시글을 조회하여 리스트로 반환
    @GetMapping("/my-boards")
    public List<Board> findMyBoard(@RequestParam String category, Authentication auth) {
        String loginId = auth.getName();
        return boardService.findMyBoard(category, loginId);
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource showImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + uploadImageService.getFullPath(filename));
    }

    @GetMapping("/images/download/{boardId}")
    public ResponseEntity<UrlResource> downloadImage(@PathVariable Long boardId) throws MalformedURLException {
        return uploadImageService.downloadImage(boardId);
    }
}