package com.sparta.easyspring.postlike.repository;

import com.sparta.easyspring.auth.entity.User;
import com.sparta.easyspring.post.entity.Post;
import com.sparta.easyspring.postlike.entity.PostLike;

import java.util.Optional;

public interface PostLikeRepositoryCustom {
    Optional<PostLike> findPostLikeByUserAndPost(User user, Post post);

    void deletePostLike(User user, Post post);

    Long countPostLike(Long postlikeId);
}
