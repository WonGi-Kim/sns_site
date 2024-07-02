package com.sparta.easyspring.commentlike.repository;

import com.sparta.easyspring.auth.entity.User;
import com.sparta.easyspring.comment.entity.Comment;
import com.sparta.easyspring.commentlike.entity.CommentLike;

import java.util.Optional;

public interface CommentLikeRepositoryCustom {

    Optional<CommentLike> findByUserAndComment(User user, Comment comment);

    Long countByCommentId(Long commentId);

    // 댓글 좋아요 삭제
    void deleteCommentLike(User user, Comment comment);

}
