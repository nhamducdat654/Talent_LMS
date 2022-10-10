package com.fsoft.team.serviceImp;

import com.fsoft.team.entity.Answer;
import com.fsoft.team.repository.AnswerRepository;
import com.fsoft.team.service.AnswerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerServiceImp implements AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Override
    public void saveAnswer(Answer answer) {
        answerRepository.save(answer);
    }

    @Override
    public List<Answer> findByQuestionId(long questionID) {
        return  answerRepository.findByQuestionId(questionID);
    }

}
