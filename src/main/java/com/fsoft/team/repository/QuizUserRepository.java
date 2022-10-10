package com.fsoft.team.repository;

import com.fsoft.team.entity.QuizUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizUserRepository extends JpaRepository<QuizUser, Long>{
    
    @Query("SELECT qu FROM QuizUser qu WHERE qu.quiz.quizID = ?1 AND qu.user.username = ?2 AND qu.isPass = 1")
    QuizUser checkExistQuizUser(long quizID, String username);

    @Query("Select count(q) from QuizUser q where q.user.username = ?1 and q.quiz.quizID = ?2 and q.isPass = TRUE")
    Long countQuizUserIDByContentIDAndUserName(String username,Long contentID);
}
