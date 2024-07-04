package com.sparta.easyspring.postlike.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.easyspring.auth.entity.User;
import com.sparta.easyspring.post.entity.Post;
import com.sparta.easyspring.postlike.entity.PostLike;
import com.sparta.easyspring.postlike.entity.QPostLike;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostLikeRepositoryCustomImpl implements PostLikeRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<PostLike> findPostLikeByUserAndPost(User user, Post post) {
        QPostLike postLike = QPostLike.postLike;

        PostLike foundPostLike = queryFactory
                .selectFrom(postLike)
                .where(postLike.user.eq(user)
                        .and(postLike.post.eq(post)))
                .fetchOne();

        return Optional.ofNullable(foundPostLike);
    }

    @Override
    @Transactional
    // 댓글 좋아요 삭제
    public void deletePostLike(User user, Post post) {
        QPostLike postLike = QPostLike.postLike;

        queryFactory.delete(postLike)
                .where(postLike.user.eq(user)
                        .and(postLike.post.eq(post)))
                .execute();
    }

    @Override
    public Long countPostLike(Long postlikeId){
        QPostLike postLike = QPostLike.postLike;

        return queryFactory
                .select(postLike.count())
                .from(postLike)
                .where(postLike.post.id.eq(postlikeId))
                .fetchOne();

    }
}
