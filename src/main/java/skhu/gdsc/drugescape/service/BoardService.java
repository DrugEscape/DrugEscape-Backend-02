package skhu.gdsc.drugescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import skhu.gdsc.drugescape.domain.*;
import skhu.gdsc.drugescape.dto.BoardRequestDto;
import skhu.gdsc.drugescape.dto.BoardResponseDto;
import skhu.gdsc.drugescape.repository.BoardRepository;
import skhu.gdsc.drugescape.repository.CommentRepository;
import skhu.gdsc.drugescape.repository.LikeRepository;
import skhu.gdsc.drugescape.repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final UploadImageService uploadImageService;

    // get boards data 모든 게시물 조회
    public List<Board> getAllBoard() {
        return boardRepository.findAll();
    }

    // 페이징 처리된 게시글 목록 조회
    public Page<Board> getBoardList(PageRequest pageRequest, String searchType, String keyword) {
        if (searchType != null && keyword != null) {
            if (searchType.equals("title")) {
                return boardRepository.findAllByTitleContainsAndUserUserRoleNot(keyword, UserRole.ADMIN, pageRequest);
            } else {
                return boardRepository.findAllByUserNicknameContainsAndUserUserRoleNot(keyword, UserRole.ADMIN, pageRequest);
            }
        }
        return boardRepository.findAllByUserUserRoleNot(UserRole.ADMIN, pageRequest);
    }

    // boardId를 통해 특정 게시글 조회
    public BoardResponseDto getBoard(Long boardId) {
        Optional<Board> optBoard = boardRepository.findById(boardId);

        // id에 해당하는 게시글이 없으면 null return
        if (optBoard.isEmpty()) {
            return null;
        }

        return BoardResponseDto.of(optBoard.get());
    }

    // 게시물 작성
    @Transactional
    public Long writeBoard(BoardRequestDto req, String loginId, Authentication auth) throws IOException {
        User loginUser = userRepository.findByLoginId(loginId).get();

        Board savedBoard = boardRepository.save(req.toEntity(loginUser));

        UploadImage uploadImage = uploadImageService.saveImage(req.getUploadImage(), savedBoard);
        if (uploadImage != null) {
            savedBoard.setUploadImage(uploadImage);
        }
        return savedBoard.getId();
    }

    // 게시글 수정
    @Transactional
    public Long editBoard(Long boardId, BoardResponseDto dto) throws IOException {
        Optional<Board> optBoard = boardRepository.findById(boardId);

        // id에 해당하는 게시글이 없으면 null return
        if (optBoard.isEmpty()) {
            return null;
        }

        Board board = optBoard.get();
        // 게시글에 이미지가 있었으면 삭제
        if (board.getUploadImage() != null) {
            uploadImageService.deleteImage(board.getUploadImage());
            board.setUploadImage(null);
        }

        UploadImage uploadImage = uploadImageService.saveImage(dto.getNewImage(), board);
        if (uploadImage != null) {
            board.setUploadImage(uploadImage);
        }
        // 게시물 업데이트
        board.update(dto);

        return board.getId();
    }

    // 개시글 삭제
    public Long deleteBoard(Long boardId) throws IOException {
        Optional<Board> optBoard = boardRepository.findById(boardId);

        // id에 해당하는 게시글이 없으면 null return
        if (optBoard.isEmpty()) {
            return null;
        }

        User boardUser = optBoard.get().getUser();
        boardUser.likeChange(boardUser.getReceivedLikeCnt() - optBoard.get().getLikeCnt());
        if (optBoard.get().getUser() != null) {
            uploadImageService.deleteImage(optBoard.get().getUploadImage());
        }
        boardRepository.deleteById(boardId);
        return boardId;
    }

    // 내가 작성한 게시글, 좋아요, 댓글 조회

    public String getCategory(Long boardId) {
        Board board = boardRepository.findById(boardId).get();
        return board.getCategory().toString().toLowerCase();
    }

    public List<Board> findMyBoard(String category, String loginId) {
        List<Board> boards = new ArrayList<>();

        if (category.equals("board")) {
            boards.addAll(boardRepository.findAllByUserLoginId(loginId));
        } else if (category.equals("like")) {
            List<Like> likes = likeRepository.findAllByUserLoginId(loginId);
            for (Like like : likes) {
                boards.add(like.getBoard());
            }
        } else if (category.equals("comment")) {
            List<Comment> comments = commentRepository.findAllByUserLoginId(loginId);
            HashSet<Long> commentIds = new HashSet<>();

            for (Comment comment : comments) {
                if (!commentIds.contains(comment.getBoard().getId())) {
                    boards.add(comment.getBoard());
                    commentIds.add(comment.getBoard().getId());
                }
            }
        }

        return boards;
    }
}
