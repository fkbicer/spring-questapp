package com.project.questapp.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.questapp.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUserId(Long userId);

    @Query(value = "SELECT id FROM post WHERE user_id = :userId ORDER BY create_date DESC limit 5", nativeQuery =  true)
    
    List<Long> findTop5ByUserId(@Param("userId") Long userId);
    
}
