package com.sparta.easyspring.commentlike.repository;

import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.easyspring.auth.entity.User;
import com.sparta.easyspring.comment.entity.Comment;
import com.sparta.easyspring.commentlike.entity.CommentLike;
import com.sparta.easyspring.commentlike.entity.QCommentLike;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentLikeRepositoryCustomImplTest {
    @Mock
    private JPAQueryFactory queryFactory;

    @InjectMocks
    private CommentLikeRepositoryCustomImpl commentLikeRepositoryCustom;

    @BeforeEach
    void setUP() {
        commentLikeRepositoryCustom = new CommentLikeRepositoryCustomImpl(queryFactory);
    }

    @Test
    public void findByUserAndComment() {
        User user = new User();
        Comment comment = new Comment();
        CommentLike commentLike = new CommentLike();
        QCommentLike qCommentLike = QCommentLike.commentLike;

        JPAQuery<CommentLike> query = Mockito.mock(JPAQuery.class);
        when(queryFactory.selectFrom(qCommentLike)).thenReturn(query);
        when(query.where(qCommentLike.user.eq(user).and(qCommentLike.comment.eq(comment)))).thenReturn(query);
        when(query.fetchOne()).thenReturn(commentLike);

        Optional<CommentLike> result = commentLikeRepositoryCustom.findByUserAndComment(user, comment);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(commentLike);
    }

    @Test
    public void countByCommentId() {
        Long commentLikeId = 1L;
        long expectedCount = 2L;
        QCommentLike qCommentLike = QCommentLike.commentLike;

        JPAQuery<Long> query = Mockito.mock(JPAQuery.class);
        when(queryFactory.select(qCommentLike.count())).thenReturn(query);
        when(query.from(qCommentLike)).thenReturn(query);
        when(query.where(qCommentLike.comment.id.eq(commentLikeId))).thenReturn(query);
        when(query.fetchOne()).thenReturn(expectedCount);

        Long result = commentLikeRepositoryCustom.countByCommentId(commentLikeId);

        assertThat(result).isEqualTo(expectedCount);
    }

    @Test
    public void deletePostLike() {
        User user = new User();
        Comment comment = new Comment();
        QCommentLike qCommentLike = QCommentLike.commentLike;

        JPADeleteClause deleteClause = Mockito.mock(JPADeleteClause.class);
        lenient().when(queryFactory.delete(qCommentLike)).thenReturn(deleteClause);
        lenient().when(deleteClause.where(qCommentLike.user.eq(user).and(qCommentLike.comment.eq(comment)))).thenReturn(deleteClause);
        lenient().when(deleteClause.execute()).thenReturn(1L);
    }

}
