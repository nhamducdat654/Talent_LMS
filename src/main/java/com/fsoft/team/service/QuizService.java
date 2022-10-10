package com.fsoft.team.service;

import com.fsoft.team.entity.Answer;
import com.fsoft.team.entity.Question;
import java.util.List;

public interface QuizService {

    Long countByQuizID(Long quizID);

    List<Answer> showAnswer();

    List<Question> showQuestion(Long quizID);

    List<Answer> showAnswerByQuizID(Long quizID);

    Long countQuizByCourseID(Long courseID);

    List<Long> getQuizIDByCourseID(Long courseID);
}
