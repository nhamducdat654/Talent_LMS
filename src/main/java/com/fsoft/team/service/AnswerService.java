package com.fsoft.team.service;

import com.fsoft.team.entity.Answer;
import java.util.List;

public interface AnswerService {
    
    void saveAnswer(Answer answer);
    
    List<Answer> findByQuestionId(long questionID);
}
