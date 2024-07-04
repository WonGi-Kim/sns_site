package com.sparta.easyspring.commentlike.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.easyspring.auth.entity.User;
import com.sparta.easyspring.comment.entity.Comment;
import com.sparta.easyspring.commentlike.entity.CommentLike;
import com.sparta.easyspring.commentlike.entity.QCommentLike;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentLikeRepositoryCustomImpl implements CommentLikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<CommentLike> findByUserAndComment(User user, Comment comment) {
        QCommentLike commentLike = QCommentLike.commentLike;

        CommentLike foundCommentLike = queryFactory
                .selectFrom(commentLike)
                .where(commentLike.user.eq(user)
                        .and(commentLike.comment.eq(comment)))
                .fetchOne();

        return Optional.ofNullable(foundCommentLike);
    }

    @Override
    // 댓글 좋아요 갯수
    public Long countByCommentId(Long commentId) {
        QCommentLike commentLike = QCommentLike.commentLike;

        return queryFactory
                .select(commentLike.count())
                .from(commentLike)
                .where(commentLike.comment.id.eq(commentId))
                .fetchOne();
    }

    @Override
    @Transactional
    // 댓글 좋아요 삭제
    public void deleteCommentLike(User user, Comment comment) {
        QCommentLike commentLike = QCommentLike.commentLike;
        queryFactory.delete(commentLike)
                .where(commentLike.user.eq(user)
                        .and(commentLike.comment.eq(comment)))
                .execute();
    }
}
