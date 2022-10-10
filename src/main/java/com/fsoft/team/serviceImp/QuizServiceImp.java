package com.fsoft.team.serviceImp;

import com.fsoft.team.entity.Answer;
import com.fsoft.team.entity.Question;
import com.fsoft.team.repository.QuizRepository;
import com.fsoft.team.service.QuizService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizServiceImp implements QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Override
    public Long countByQuizID(Long quizID) {
        return quizRepository.countByQuizID(quizID);
    }

        @Override
    public List<Answer> showAnswer() {
        return quizRepository.showAnswer();
    }

    @Override
    public List<Question> showQuestion(Long quizID) {
        return quizRepository.showQuestion(quizID);
    }
    
    @Override
    public List<Answer> showAnswerByQuizID(Long quizID) {
        return quizRepository.showAnswerByQuizID(quizID);
    }

    @Override
    public Long countQuizByCourseID(Long courseID) {
        return quizRepository.countQuizByCourseID(courseID);
    }

    @Override
    public List<Long> getQuizIDByCourseID(Long courseID) {
        return quizRepository.getQuizIDByCourseID(courseID);
    }


}
