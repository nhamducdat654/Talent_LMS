package com.fsoft.team.service;


import com.fsoft.team.entity.QuizUser;

public interface QuizUserService {
    
    QuizUser save(QuizUser quizUser);
    
    boolean checkExistQuizUser(long quizID, String username);

    Long countQuizUserIDByContentIDAndUserName(String username,Long contentID);
    
    void deleteQuizUserByContentIDAndUsername(Long quizID, String username);
}
