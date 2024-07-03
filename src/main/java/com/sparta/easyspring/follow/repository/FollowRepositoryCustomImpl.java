package com.sparta.easyspring.follow.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.easyspring.auth.entity.QUser;
import com.sparta.easyspring.auth.entity.User;
import com.sparta.easyspring.follow.entity.Follow;
import com.sparta.easyspring.follow.entity.QFollow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryCustomImpl implements FollowRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Follow findByFollowingIdAndUser(Long followingId, User user) {
        QFollow follow = QFollow.follow;

        Follow foundFollow = queryFactory
                .selectFrom(follow)
                .where(follow.followingId.eq(followingId)
                        .and(follow.user.eq(user)))
                .fetchOne();

        return foundFollow;
    }

    @Override
    public List<Long> findAllFollowingByUser(User user) {
        QFollow follow = QFollow.follow;

        return queryFactory
                .select(follow.followingId)
                .from(follow)
                .where(follow.user.eq(user))
                .fetch();
    }

}
