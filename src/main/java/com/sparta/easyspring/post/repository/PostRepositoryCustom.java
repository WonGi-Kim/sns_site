package com.sparta.easyspring.post.repository;

import com.sparta.easyspring.auth.entity.User;
import com.sparta.easyspring.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {

    Page<Post> findAllLikePost(User user, Pageable pageable);

    Page<Post> findAllPostByFollowings(List<Long> followings, Pageable pageable);
}
