package com.project.questapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.questapp.entities.Comment;
import com.project.questapp.entities.Like;
import com.project.questapp.entities.Post;
import com.project.questapp.entities.User;
import com.project.questapp.repos.LikeRepository;
import com.project.questapp.requests.CommentCreateRequest;
import com.project.questapp.requests.LikeCreateRequest;

@Service
public class LikeService {

    private LikeRepository likeRepository;
    private UserService userService;
    private PostService postService;

    
    @Autowired
    public LikeService(LikeRepository likeRepository, UserService userService, PostService postService) {
        this.likeRepository = likeRepository;
        this.postService = postService;
        this.userService = userService;
    }
    
    public List<Like> getAllLikesWithParam(Optional<Long> userId, Optional<Long> postId) {
        if(userId.isPresent() && postId.isPresent()) {
            return likeRepository.findByUserIdAndPostId(userId.get(),postId.get());
        }else if(userId.isPresent()) {
            return likeRepository.findByUserId(userId.get());
        }else if(postId.isPresent()) {
            return likeRepository.findByPostId(postId.get());
        }else
            return likeRepository.findAll();
            
    }

    public Like createLikeById(LikeCreateRequest likeCreateRequest) {

        User user = userService.getOneUserById(likeCreateRequest.getUserId());
        Post post = postService.getOnePostById(likeCreateRequest.getPostId());

        if(user != null && post != null){
            Like likeToSave=  new Like();
            likeToSave.setPost(post);
            likeToSave.setUser(user);
            likeToSave.setId(likeCreateRequest.getId());
            return likeRepository.save(likeToSave);
        }else
            return null;
        
    }

    
    public void deleteLikeById(Long likeId) {
        likeRepository.deleteById(likeId);
    }


    
}
