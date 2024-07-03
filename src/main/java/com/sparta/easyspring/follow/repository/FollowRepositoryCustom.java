package com.sparta.easyspring.follow.repository;

import com.sparta.easyspring.auth.entity.User;
import com.sparta.easyspring.follow.entity.Follow;

import java.util.List;

public interface FollowRepositoryCustom {
    Follow findByFollowingIdAndUser(Long followingId, User user);

    List<Long> findAllFollowingByUser(User user);
}
