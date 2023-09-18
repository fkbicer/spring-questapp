package com.project.questapp.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.questapp.entities.Comment;
import com.project.questapp.entities.Post;
import com.project.questapp.entities.User;
import com.project.questapp.repos.CommentRepository;
import com.project.questapp.requests.CommentCreateRequest;
import com.project.questapp.requests.CommentUpdateRequest;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private UserService userService;
    private PostService postService;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService, PostService postService) {
        this.postService = postService;
        this.userService = userService;
        this.commentRepository = commentRepository;
    }

    public List<Comment> getAllCommentsWithParam(Optional<Long> userId, Optional<Long> postId) {
        if(userId.isPresent() && postId.isPresent()) {
            return commentRepository.findByUserIdAndPostId(userId.get(),postId.get());
        }else if(userId.isPresent()) {
            return commentRepository.findByUserId(userId.get());
        }else if(postId.isPresent()) {
            return commentRepository.findByPostId(postId.get());
        }else
            return commentRepository.findAll();
            
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    public Comment createCommentById(CommentCreateRequest commentCreateRequest) {

        User user = userService.getOneUserById(commentCreateRequest.getUserId());
        Post post = postService.getOnePostById(commentCreateRequest.getPostId());

        if(user != null && post != null){
            Comment commentToSave=  new Comment();
            commentToSave.setPost(post);
            commentToSave.setUser(user);
            commentToSave.setId(commentCreateRequest.getId());
            commentToSave.setText(commentCreateRequest.getText());
            commentToSave.setCreateDate(new Date());
            return commentRepository.save(commentToSave);
        }else
            return null;
        
    }

    public Comment updateCommentById(Long commentId, CommentUpdateRequest request) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isPresent()){
            Comment commentToUpdate = comment.get();
            commentToUpdate.setText(request.getText());
            return commentRepository.save(commentToUpdate);
        }else
            return null;
    }

    public void deleteCommentById(Long commentId) {
        commentRepository.deleteById(commentId);
    }
    
}
