package com.fsoft.team.serviceImp;

import com.fsoft.team.entity.Question;
import com.fsoft.team.repository.QuestionRepository;
import com.fsoft.team.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImp implements QuestionService{
    
    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public void saveQuestion(Question quetsion) {
        questionRepository.save(quetsion);
    }

    @Override
    public Question findById(long questionID) {
        return questionRepository.getById(questionID);
    }

    @Override
    public void deleteQuestionByID(long questionID) {
        questionRepository.deleteById(questionID);
    }
}
