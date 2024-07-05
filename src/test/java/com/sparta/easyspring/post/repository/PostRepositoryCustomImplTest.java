package com.sparta.easyspring.post.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.easyspring.auth.entity.User;
import com.sparta.easyspring.post.entity.Post;
import com.sparta.easyspring.post.entity.QPost;
import com.sparta.easyspring.postlike.entity.QPostLike;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
public class PostRepositoryCustomImplTest {
    @Mock
    private JPAQueryFactory queryFactory;

    @InjectMocks
    private PostRepositoryCustomImpl postRepositoryCustom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postRepositoryCustom = new PostRepositoryCustomImpl(queryFactory);
    }

    @Test
    public void findAllLikePost() {
        User user = new User();
        Post post = new Post();
        QPost qPost = QPost.post;
        QPostLike qPostLike = QPostLike.postLike;
        QPageRequest qPageRequest = QPageRequest.of(0, 5);

        JPAQuery<Post> mockQuery = Mockito.mock(JPAQuery.class);
        when(queryFactory.select(qPost)).thenReturn(mockQuery);
        when(mockQuery.from(qPostLike)).thenReturn(mockQuery);
        when(mockQuery.join(qPostLike.post, qPost)).thenReturn(mockQuery);
        when(mockQuery.where(qPostLike.user.eq(user))).thenReturn(mockQuery);
        when(mockQuery.orderBy(qPost.createdAt.desc())).thenReturn(mockQuery);
        when(mockQuery.offset(qPageRequest.getOffset())).thenReturn(mockQuery);
        when(mockQuery.limit(qPageRequest.getPageSize())).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(Collections.singletonList(post));

        JPAQuery<Long> mockCountQuery = Mockito.mock(JPAQuery.class);
        when(queryFactory.select(qPost.count())).thenReturn(mockCountQuery);
        when(mockCountQuery.from(qPostLike)).thenReturn(mockCountQuery);
        when(mockCountQuery.where(qPostLike.user.eq(user))).thenReturn(mockCountQuery);
        when(mockCountQuery.fetchOne()).thenReturn(1L);

        Page<Post> result = postRepositoryCustom.findAllLikePost(user, qPageRequest);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1L);
    }

    @Test
    public void findAllPostByFollowings() {
        User user = new User();
        Long followingId = 1L;
        List<Long> followingIds = Collections.singletonList(followingId);
        Post post = new Post();
        QPost qPost = QPost.post;
        QPageRequest qPageRequest = QPageRequest.of(0, 5);

        JPAQuery<Post> mockQuery = Mockito.mock(JPAQuery.class);
        when(queryFactory.selectFrom(qPost)).thenReturn(mockQuery);
        when(mockQuery.where(qPost.user.id.eq(followingId))).thenReturn(mockQuery);
        when(mockQuery.orderBy(qPost.createdAt.desc())).thenReturn(mockQuery);
        when(mockQuery.offset(qPageRequest.getOffset())).thenReturn(mockQuery);
        when(mockQuery.limit(qPageRequest.getPageSize())).thenReturn(mockQuery);
        when(mockQuery.fetch()).thenReturn(Collections.singletonList(post));

        JPAQuery<Long> mockCountQuery = Mockito.mock(JPAQuery.class);
        when(queryFactory.select(qPost.count())).thenReturn(mockCountQuery);
        when(mockCountQuery.from(qPost)).thenReturn(mockCountQuery);
        when(mockCountQuery.where(qPost.user.id.eq(followingId))).thenReturn(mockCountQuery);
        when(mockCountQuery.fetchOne()).thenReturn(1L);

        Page<Post> result = postRepositoryCustom.findAllPostByFollowings(followingIds, qPageRequest);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1L);
    }
}
