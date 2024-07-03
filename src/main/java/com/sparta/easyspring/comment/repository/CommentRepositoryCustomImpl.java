package com.sparta.easyspring.comment.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.easyspring.auth.entity.User;
import com.sparta.easyspring.comment.entity.Comment;
import com.sparta.easyspring.comment.entity.QComment;
import com.sparta.easyspring.commentlike.entity.QCommentLike;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Override
    public Page<Comment> findAllLikeComment(User user, Pageable pageable) {

        QCommentLike commentLike = QCommentLike.commentLike;
        QComment comment = QComment.comment;

        List<Comment> commentList = queryFactory
                .select(comment)
                .from(commentLike)
                .join(commentLike.comment, comment)
                .where(commentLike.user.eq(user))
                .orderBy(comment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(comment.count())
                .from(commentLike)
                .where(commentLike.user.eq(user));

        return new PageImpl<>(commentList, pageable, countQuery.fetchOne());
    }
}
