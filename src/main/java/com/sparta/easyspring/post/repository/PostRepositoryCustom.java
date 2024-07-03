package com.sparta.easyspring.post.repository;

import com.sparta.easyspring.auth.entity.User;
import com.sparta.easyspring.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<Post> findAllLikePost(User user, Pageable pageable);
}
