package com.fsoft.team.service;

import com.fsoft.team.entity.Question;

public interface QuestionService {
    
    void saveQuestion(Question quetsion);
    
    Question findById(long questionID);
    
    void deleteQuestionByID(long questionID);
}
