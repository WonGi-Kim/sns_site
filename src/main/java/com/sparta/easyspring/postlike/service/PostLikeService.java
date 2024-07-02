package com.sparta.easyspring.postlike.service;

import com.sparta.easyspring.auth.entity.User;
import com.sparta.easyspring.auth.service.UserService;
import com.sparta.easyspring.exception.CustomException;
import com.sparta.easyspring.post.entity.Post;
import com.sparta.easyspring.post.service.PostService;
import com.sparta.easyspring.postlike.entity.PostLike;
import com.sparta.easyspring.postlike.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.easyspring.exception.ErrorEnum.*;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostService postService;
    private final UserService userService;

    @Transactional
    public String likePost(long userId, long postId) {
        User user = userService.findUserById(userId);
        Post post = postService.findPostbyId(postId);

        if (postLikeRepository.findPostLikeByUserAndPost(user, post).isPresent()) {
            throw new CustomException(DUPLICATE_LIKE);
        }
        if (post.getUser().getId().equals(userId)) {
            throw new CustomException(CANNOT_LIKE_OWN_CONTENT);
        }

        PostLike postLike = new PostLike(user, post);
        postLikeRepository.save(postLike);

        post.updatePostLikes(postLikeRepository.countPostLike(postId));

        return "게시글 좋아요 완료";
    }

    @Transactional
    public String unlikePost(long userId, long postId) {
        User user = userService.findUserById(userId);
        Post post = postService.findPostbyId(postId);

        if (!postLikeRepository.findPostLikeByUserAndPost(user, post).isPresent()) {
            throw new CustomException(LIKE_NOT_FOUND);
        }

        postLikeRepository.deletePostLike(user, post);

        post.updatePostLikes(postLikeRepository.countPostLike(postId));

        return "게시글 좋아요 해제 완료";
    }
}
