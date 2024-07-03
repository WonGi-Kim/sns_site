package com.sparta.easyspring.post.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.easyspring.auth.entity.User;
import com.sparta.easyspring.post.entity.Post;
import com.sparta.easyspring.post.entity.QPost;
import com.sparta.easyspring.postlike.entity.QPostLike;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> findAllLikePost(User user, Pageable pageable) {
        QPostLike postLike = QPostLike.postLike;
        QPost post = QPost.post;

        List<Post> postList = queryFactory
                .select(post)
                .from(postLike)
                .join(postLike.post, post)
                .where(postLike.user.eq(user))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(postLike)
                .where(postLike.user.eq(user));

        return new PageImpl<>(postList, pageable, countQuery.fetchOne());
    }
}
