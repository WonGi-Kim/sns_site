package com.sparta.easyspring.post.service;

import com.sparta.easyspring.auth.entity.User;
import com.sparta.easyspring.auth.service.UserService;
import com.sparta.easyspring.exception.CustomException;
import com.sparta.easyspring.exception.GlobalExceptionHandler;
import com.sparta.easyspring.follow.entity.Follow;
import com.sparta.easyspring.follow.repository.FollowRepository;
import com.sparta.easyspring.follow.service.FollowService;
import com.sparta.easyspring.post.dto.PostRequestDto;
import com.sparta.easyspring.post.dto.PostResponseDto;
import com.sparta.easyspring.post.entity.Post;
import com.sparta.easyspring.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.easyspring.exception.ErrorEnum.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final FollowService followService;
    private final FollowRepository followRepository;

    public PostResponseDto addPost(PostRequestDto requestDto, User user) {
        Post post = new Post(requestDto,user);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    public List<PostResponseDto> getAllPost(int page, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC,sortBy);
        Pageable pageable = PageRequest.of(page,5,sort);
        Page<PostResponseDto> postPage = postRepository.findAll(pageable).map(PostResponseDto::new);
        List<PostResponseDto> postList = postPage.getContent();
        return postList;
    }

    public PostResponseDto getPost(Long postId) {
        Post post = findPostbyId(postId);
        PostResponseDto postResponseDto = new PostResponseDto(post);
        return postResponseDto;
    }

    public PostResponseDto editPost(Long postId, PostRequestDto requestDto, User user) {
        Post post = findPostbyId(postId);
        if(!post.getUser().getId().equals(user.getId())){
            throw new CustomException(INCORRECT_USER);
        }
        post.update(requestDto);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    public void deletePost(Long postId, User user) {
        Post post = findPostbyId(postId);
        if(!post.getUser().getId().equals(user.getId())){
            throw new CustomException(INCORRECT_USER);
        }
        postRepository.delete(post);
    }
    public List<PostResponseDto> getAllFollowPost(Long followingId, User user,int page, String sortBy){
        User checkUser = userService.findUserById(followingId);
        Follow checkFollow = followService.findFollowById(checkUser.getId(),user);
        if(checkFollow==null){
            throw new CustomException(NOT_FOLLOW_USER);
        }
        Sort sort = Sort.by(Sort.Direction.DESC,sortBy);
        Pageable pageable = PageRequest.of(page,5,sort);

        Page<Post> followPostPage = postRepository.findAllByUser(checkUser,pageable);
        List<PostResponseDto> followPostList = followPostPage
                .stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        return followPostList;
    }

    public Post findPostbyId(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(
                ()->new CustomException(POST_NOT_FOUND)
        );
        return post;
    }

    @Transactional
    public List<PostResponseDto> getAllLikePost(Long userId, int page, int size) {
        User user = userService.findUserById(userId);

        Pageable pageable = PageRequest.of(page, size);

        Page<Post> likePostPage = postRepository.findAllLikePost(user, pageable);

        return likePostPage.stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    //  팔로잉 게시글 목록 조회 기능
    public List<PostResponseDto> getFollowingPost(Long userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        User user = userService.findUserById(userId);

        List<Long> followings = followRepository.findAllFollowingByUser(user);

        if(followings.isEmpty()) {
            throw new CustomException(ALREADY_FOLLOW);
        }

        Page<Post> followingsPost = postRepository.findAllPostByFollowings(followings, pageable);

        return followingsPost.stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }
}
