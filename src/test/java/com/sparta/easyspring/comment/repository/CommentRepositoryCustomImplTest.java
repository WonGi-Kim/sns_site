package com.sparta.easyspring.comment.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.easyspring.auth.entity.User;
import com.sparta.easyspring.comment.entity.Comment;
import com.sparta.easyspring.comment.entity.QComment;
import com.sparta.easyspring.commentlike.entity.QCommentLike;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QPageRequest;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentRepositoryCustomImplTest {

    @Mock
    private JPAQueryFactory queryFactory;

    @InjectMocks
    private CommentRepositoryCustomImpl commentRepositoryCustom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentRepositoryCustom = new CommentRepositoryCustomImpl(queryFactory);
    }

    @Test
    public void findAllLikeComment() {
        User user = new User();
        Comment comment = new Comment();

        QPageRequest pageRequest = QPageRequest.of(0, 5);
        QCommentLike qCommentLike = QCommentLike.commentLike;
        QComment qComment = QComment.comment;

        JPAQuery<Comment> mockQuery = Mockito.mock(JPAQuery.class);
        when(queryFactory.select(qComment)).thenReturn(mockQuery);
        when(mockQuery.from(qCommentLike)).thenReturn(mockQuery);
        when(mockQuery.join(qCommentLike.comment, qComment)).thenReturn(mockQuery);
        when(mockQuery.where(qCommentLike.user.eq(user))).thenReturn(mockQuery);
        when(mockQuery.orderBy(qComment.createdAt.desc())).thenReturn(mockQuery);
        when(mockQuery.offset(pageRequest.getOffset())).thenReturn(mockQuery);
        when(mockQuery.limit(pageRequest.getPageSize())).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(Collections.singletonList(comment));

        JPAQuery<Long> mockCountQuery = Mockito.mock(JPAQuery.class);
        when(queryFactory.select(qComment.count())).thenReturn(mockCountQuery);
        when(mockCountQuery.from(qCommentLike)).thenReturn(mockCountQuery);
        when(mockCountQuery.where(qCommentLike.user.eq(user))).thenReturn(mockCountQuery);
        when(mockCountQuery.fetchOne()).thenReturn(1L);

        Page<Comment> result = commentRepositoryCustom.findAllLikeComment(user, pageRequest);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1L);
    }
}

