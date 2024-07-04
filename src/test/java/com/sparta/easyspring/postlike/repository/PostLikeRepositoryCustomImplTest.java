package com.sparta.easyspring.postlike.repository;


import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.easyspring.auth.entity.User;
import com.sparta.easyspring.post.entity.Post;
import com.sparta.easyspring.postlike.entity.PostLike;
import com.sparta.easyspring.postlike.entity.QPostLike;
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
public class PostLikeRepositoryCustomImplTest {
    @Mock
    private JPAQueryFactory queryFactory;

    @InjectMocks
    private PostLikeRepositoryCustomImpl postLikeRepositoryCustom;

    @BeforeEach
    void setUp() {
        // qPostLike Mock 객체를 InjectMocks의 대상 객체에 주입
        postLikeRepositoryCustom = new PostLikeRepositoryCustomImpl(queryFactory);
    }

    @Test
    public void findPostLikeByUserAndPost() {
        User user = new User();
        Post post = new Post();
        PostLike postLike = new PostLike();
        QPostLike qPostLike = QPostLike.postLike;

        JPAQuery<PostLike> query = Mockito.mock(JPAQuery.class);
        when(queryFactory.selectFrom(qPostLike)).thenReturn(query);
        when(query.where(qPostLike.user.eq(user).and(qPostLike.post.eq(post)))).thenReturn(query);
        when(query.fetchOne()).thenReturn(postLike);

        Optional<PostLike> result = postLikeRepositoryCustom.findPostLikeByUserAndPost(user, post);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(postLike);

    }

    @Test
    public void deletePostLike() {
        User user = new User();
        Post post = new Post();
        QPostLike qPostLike = QPostLike.postLike;

        JPADeleteClause deleteClause = Mockito.mock(JPADeleteClause.class);
        lenient().when(queryFactory.delete(qPostLike)).thenReturn(deleteClause);
        lenient().when(deleteClause.where(qPostLike.user.eq(user).and(qPostLike.post.eq(post)))).thenReturn(deleteClause);
        lenient().when(deleteClause.execute()).thenReturn(1L);

    }

    @Test
    public void countPostLike() {
        Long postLikeId = 1L;
        long expectedCount = 2L;
        QPostLike qPostLike = QPostLike.postLike;

        JPAQuery<Long> query = Mockito.mock(JPAQuery.class);
        when(queryFactory.select(qPostLike.count())).thenReturn(query);
        when(query.from(qPostLike)).thenReturn(query);
        when(query.where(qPostLike.post.id.eq(postLikeId))).thenReturn(query);
        when(query.fetchOne()).thenReturn(expectedCount);

        Long result = postLikeRepositoryCustom.countPostLike(postLikeId);

        assertThat(result).isEqualTo(expectedCount);
    }
}
