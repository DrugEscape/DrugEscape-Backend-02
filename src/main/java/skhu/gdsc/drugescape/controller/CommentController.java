package skhu.gdsc.drugescape.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import skhu.gdsc.drugescape.dto.CommentCreateRequestDto;
import skhu.gdsc.drugescape.service.BoardService;
import skhu.gdsc.drugescape.service.CommentService;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final BoardService boardService;

    @PostMapping("/{boardId}")
    public String addComments(@PathVariable Long boardId, @ModelAttribute CommentCreateRequestDto req,
                              Authentication auth, Model model) {
        commentService.writeComment(boardId, req, auth.getName());

        model.addAttribute("message", "댓글이 추가되었습니다."); // 메시지
        model.addAttribute("nextUrl", "/boards/" + boardId); // 게시글이 있는 페이지로 이동
        return "printMessage"; // model 객체에 담긴 데이터 view 반환
    }

    @PostMapping("/{commentId}/edit")
    public String editComment(@PathVariable Long commentId, @ModelAttribute CommentCreateRequestDto req,
                              Authentication auth, Model model) {
        Long boardId = commentService.editComment(commentId, req.getContent(), auth.getName());
        model.addAttribute("message", boardId == null? "잘못된 요청입니다." : "댓글이 수정 되었습니다.");
        model.addAttribute("nextUrl", "/boards/" + boardId);
        return "printMessage";
    }

    @GetMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId, Authentication auth, Model model) {
        Long boardId = commentService.deleteComment(commentId, auth.getName());
        model.addAttribute("message", boardId == null? "작성자만 삭제 가능합니다." : "댓글이 삭제 되었습니다.");
        model.addAttribute("nextUrl", "/boards/" + boardId);
        return "printMessage";
    }
}
