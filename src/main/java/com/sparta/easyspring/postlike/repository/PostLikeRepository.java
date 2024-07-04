package com.sparta.easyspring.postlike.repository;

import com.sparta.easyspring.auth.entity.User;
import com.sparta.easyspring.commentlike.entity.CommentLike;
import com.sparta.easyspring.commentlike.repository.CommentLikeRepositoryCustom;
import com.sparta.easyspring.post.entity.Post;
import com.sparta.easyspring.postlike.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PostLikeRepository extends JpaRepository<PostLike, Long>, QuerydslPredicateExecutor<PostLike>, PostLikeRepositoryCustom {
    PostLike findByUserAndPost(User user, Post post);
}
