package com.sparta.easyspring.post.repository;

import com.sparta.easyspring.auth.entity.User;
import com.sparta.easyspring.post.entity.Post;
import com.sparta.easyspring.postlike.entity.PostLike;
import com.sparta.easyspring.postlike.repository.PostLikeRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>, PostRepositoryCustom {
    Page<Post> findAllByUser(User user, Pageable pageable);

    @Query("SELECT p FROM Post p ORDER BY p.isPinned DESC, p.isNotice DESC, p.createdAt DESC")
    Page<Post> findAllSorted(Pageable pageable);
}
