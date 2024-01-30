package skhu.gdsc.drugescape.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import skhu.gdsc.drugescape.domain.User;
import skhu.gdsc.drugescape.dto.UserDto;
import skhu.gdsc.drugescape.dto.UserJoinRequestDto;
import skhu.gdsc.drugescape.dto.UserLoginRequestDto;
import skhu.gdsc.drugescape.service.BoardService;
import skhu.gdsc.drugescape.service.UserService;


@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final BoardService boardService;

    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("userJoinRequest", new UserJoinRequestDto());
        return "users/join";
    }

    @PostMapping("/join")
    public String join(@Valid @ModelAttribute UserJoinRequestDto req, BindingResult bindingResult, Model model) {

        // Validation
        if (userService.joinValid(req, bindingResult).hasErrors()) {
            return "users/join";
        }

        userService.join(req);
        model.addAttribute("message", "회원가입에 성공했습니다!\n로그인 후 사용 가능합니다!");
        model.addAttribute("nextUrl", "/users/login");
        return "printMessage";
    }

    @GetMapping("/login")
    public String loginPage(Model model, HttpServletRequest request) {

        // 로그인 성공 시 이전 페이지로 redirect 되게 하기 위해 세션에 저장
        String uri = request.getHeader("Referer");
        if (uri != null && !uri.contains("/login") && !uri.contains("/join")) {
            request.getSession().setAttribute("prevPage", uri);
        }

        model.addAttribute("userLoginRequest", new UserLoginRequestDto());
        return "users/login";
    }

    @GetMapping("/edit")
    public String userEditPage(Authentication auth, Model model) {
        User user = userService.myInfo(auth.getName());
        model.addAttribute("userDto", UserDto.of(user));
        return "users/edit";
    }

    @PostMapping("/edit")
    public String userEdit(@Valid @ModelAttribute UserDto dto, BindingResult bindingResult,
                           Authentication auth, Model model) {

        // Validation
        if (userService.editValid(dto, bindingResult, auth.getName()).hasErrors()) {
            return "users/edit";
        }

        userService.edit(dto, auth.getName());

        model.addAttribute("message", "정보가 수정되었습니다.");
        model.addAttribute("nextUrl", "/users/myPage/board");
        return "printMessage";
    }

    @GetMapping("/delete")
    public String userDeletePage(Authentication auth, Model model) {
        User user = userService.myInfo(auth.getName());
        model.addAttribute("userDto", UserDto.of(user));
        return "users/delete";
    }

    @PostMapping("/delete")
    public String userDelete(@ModelAttribute UserDto dto, Authentication auth, Model model) {
        Boolean deleteSuccess = userService.delete(auth.getName(), dto.getNowPassword());
        if (deleteSuccess) {
            model.addAttribute("message", "탈퇴 되었습니다.");
            model.addAttribute("nextUrl", "/users/logout");
            return "printMessage";
        } else {
            model.addAttribute("message", "현재 비밀번호가 틀려 탈퇴에 실패하였습니다.");
            model.addAttribute("nextUrl", "/users/delete");
            return "printMessage";
        }
    }

    // 관리자 페이지. 모든 유저 정보 확인
    @GetMapping("/admin")
    public String adminPage(@RequestParam(required = false, defaultValue = "1") int page,
                            @RequestParam(required = false, defaultValue = "") String keyword,
                            Model model) {

        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("id").descending());
        Page<User> users = userService.findAllByNickname(keyword, pageRequest);

        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);
        return "users/admin";
    }

}
