package com.project.questapp.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.project.questapp.entities.Post;
import com.project.questapp.entities.User;
import com.project.questapp.repos.PostRepository;
import com.project.questapp.requests.PostCreateRequest;
import com.project.questapp.requests.PostUpdateRequest;
import com.project.questapp.responses.PostResponse;

@Service
public class PostService {

        private PostRepository postRepository;
        private UserService userService;
        private LikeService likeService;

        public PostService(PostRepository postRepository, UserService userService, LikeService likeService) {
            this.likeService = likeService;
            this.postRepository = postRepository;
            this.userService = userService;
        }

        public List<PostResponse> getAllPosts(Optional<Long> userId) {
            List<Post> postList;
            if(userId.isPresent()) {
                postList = postRepository.findByUserId(userId.get()); 
            }else{
                postList = postRepository.findAll();
            }
            return postList.stream().map(p -> new PostResponse(p)).collect(Collectors.toList());
            
        }

        public Post getOnePostById(Long postId) {
            return postRepository.findById(postId).orElse(null);
        }

        public Post createOnePost(PostCreateRequest postCreateRequest) {
            User user = userService.getOneUserById(postCreateRequest.getUserId());
            if(user == null) {
                return null;
            }
            Post toSave = new Post();
            toSave.setId(postCreateRequest.getId());
            toSave.setText(postCreateRequest.getText());
            toSave.setTitle(postCreateRequest.getTitle());
            toSave.setUser(user);
            return postRepository.save(toSave);
        }

        public Post updateOnePostById(Long postId, PostUpdateRequest postUpdateRequest) {
            Optional<Post> post = postRepository.findById(postId);
            if(post.isPresent()) {
                Post toUpdate = post.get();
                toUpdate.setText(postUpdateRequest.getText());
                toUpdate.setTitle(postUpdateRequest.getTitle());
                postRepository.save(toUpdate);
                return toUpdate;
            }
            return null;
        }

        public void deleteOnePostById(Long postId) {
            postRepository.deleteById(postId);
        }


        
}
