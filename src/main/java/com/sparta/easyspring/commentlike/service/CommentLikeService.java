
package com.sparta.easyspring.commentlike.service;


import com.sparta.easyspring.auth.entity.User;
import com.sparta.easyspring.auth.service.UserService;
import com.sparta.easyspring.comment.entity.Comment;
import com.sparta.easyspring.comment.service.CommentService;
import com.sparta.easyspring.commentlike.entity.CommentLike;
import com.sparta.easyspring.commentlike.repository.CommentLikeRepository;
import com.sparta.easyspring.exception.CustomException;
import com.sparta.easyspring.exception.ErrorEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentLikeService {

    private final CommentService commentService;
    private final UserService userService;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public ResponseEntity<String> likeComment(Long userId, Long commentId, User loginUser) {
        // 로그인 유저와 userId 비교
        checkUser(userId,loginUser);

        // 유저, 댓글 있는지 확인
        User user = userService.findUserById(userId);
        Comment comment = commentService.findCommentbyId(commentId);

        // 좋아요 유무 확인
        if (commentLikeRepository.findByUserAndComment(user, comment).isPresent()) {
            throw new CustomException(ErrorEnum.DUPLICATE_LIKE);
        }

        // 본인이 작성한 게시물에 좋아요를 남길 수 없음 ( commentService에서 연산 후 비교하기 때문에 CommentRepository에서 QueryDSL을 작성할 필요가 있는가? )
        if (comment.getUser().getId().equals(loginUser.getId())) {
            throw new CustomException(ErrorEnum.CANNOT_LIKE_OWN_CONTENT);
        }

        // 위에 조건 다 통과하면 댓글 좋아요 추가
        CommentLike commentLike = new CommentLike(user,comment);
        commentLikeRepository.save(commentLike);
        // 숫자 증가
        comment.updateCommentLike(commentLikeRepository.countByCommentId(commentId));

        return ResponseEntity.ok("댓글 좋아요 완료");
    }

    @Transactional
    public ResponseEntity<String> unlikeComment(Long userId, Long commentId, User loginUser) {
        // 로그인 유저와 userId 비교
        checkUser(userId,loginUser);

        // 유저, 댓글 있는지 확인
        User user = userService.findUserById(userId);
        Comment comment = commentService.findCommentbyId(commentId);

        // 좋아요가 이전에 존재하는지 확인
        if (!commentLikeRepository.findByUserAndComment(user, comment).isPresent()) {
            throw new CustomException(ErrorEnum.LIKE_NOT_FOUND);
        }

        // 좋아요 해제
        commentLikeRepository.deleteCommentLike(user, comment);

        comment.updateCommentLike(commentLikeRepository.countByCommentId(commentId));

        return ResponseEntity.ok("댓글 좋아요 해제 완료");
    }

    private void checkUser(Long userId, User loginUser) {
        if(!userId.equals(loginUser.getId())){
            throw new CustomException(ErrorEnum.USER_NOT_AUTHENTICATED);
        }
    }
}
