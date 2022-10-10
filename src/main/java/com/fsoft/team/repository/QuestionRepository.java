package com.fsoft.team.repository;

import com.fsoft.team.entity.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT qe FROM Question qe WHERE qe.quiz.quizID = ?1")
    public List<Question> getListQuestionOfQuiz(Long quizID);

    @Query("SELECT qe FROM Question qe WHERE qe.course.courseID = ?1 AND qe.quiz.quizID = null")
    public List<Question> getListQuestionOfCourse(Long courseID);

    @Query("SELECT qe FROM Question qe WHERE qe.quiz.quizID = null")
    public List<Question> getListAllQuestion();
}
