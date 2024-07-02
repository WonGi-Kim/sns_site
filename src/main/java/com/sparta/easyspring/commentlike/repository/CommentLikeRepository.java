
package com.sparta.easyspring.commentlike.repository;


import com.sparta.easyspring.commentlike.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CommentLikeRepository extends JpaRepository<CommentLike,Long>, QuerydslPredicateExecutor<CommentLike>, CommentLikeRepositoryCustom {
    //Optional<CommentLike> findByUserAndComment(User user, Comment comment);
}
