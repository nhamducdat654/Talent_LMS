package com.fsoft.team.repository;

import com.fsoft.team.entity.Answer;
import com.fsoft.team.entity.Question;
import com.fsoft.team.entity.Quiz;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @Query("SELECT q FROM Quiz q WHERE q.quizIndex > ?1 AND q.section.sectionID = ?2 AND q.course.courseID = ?3")
    public List<Quiz> getListQuizDelete(int contentIndex, Long sectionID, Long courseID);

    @Query("SELECT q FROM Quiz q WHERE q.quizIndex > ?1 AND q.section.sectionID = null AND q.course.courseID = ?2")
    public List<Quiz> getListQuizDeleteNull(int contentIndex, Long courseID);

    @Query("SELECT q FROM Quiz q WHERE q.quizIndex > ?1 AND q.quizIndex <= ?2 AND q.section.sectionID = ?3 AND q.course.courseID = ?4")
    public List<Quiz> getListQuizUpdateInc(int contentIndex, int newIndex, Long sectionID, Long courseID);

    @Query("SELECT q FROM Quiz q WHERE q.quizIndex > ?1 AND q.quizIndex <= ?2 AND q.section.sectionID = null AND q.course.courseID = ?3")
    public List<Quiz> getListQuizUpdateIncNull(int contentIndex, int newIndex, Long courseID);

    @Query("SELECT q FROM Quiz q WHERE q.quizIndex < ?1 AND q.quizIndex >= ?2 AND q.section.sectionID = ?3 AND q.course.courseID = ?4")
    public List<Quiz> getListQuizUpdateDes(int contentIndex, int newIndex, Long sectionID, Long courseID);

    @Query("SELECT q FROM Quiz q WHERE q.quizIndex < ?1 AND q.quizIndex >= ?2 AND q.section.sectionID = null AND q.course.courseID = ?3")
    public List<Quiz> getListQuizUpdateDesNull(int contentIndex, int newIndex, Long courseID);

    @Query("SELECT q FROM Quiz q WHERE q.quizIndex >= ?1 AND q.section.sectionID = ?2 AND q.course.courseID = ?3")
    public List<Quiz> getListQuizUpdateIncOrther(int newIndex, Long sectionID, Long courseID);

    @Query("SELECT q FROM Quiz q WHERE q.quizIndex >= ?1 AND q.section.sectionID = null AND q.course.courseID = ?2")
    public List<Quiz> getListQuizUpdateIncOrtherNull(int newIndex, Long courseID);

    @Query("SELECT count(q) FROM Question q WHERE q.quiz.quizID=?1")
    Long countByQuizID(Long quizID);

    @Query("SELECT a FROM Answer a")
    List<Answer> showAnswer();

    @Query("SELECT q FROM Question q WHERE q.quiz.quizID=?1")
    List<Question> showQuestion(Long quizID);

    @Query("SELECT a FROM Answer a JOIN Question q ON a.question.questionID=q.questionID WHERE q.quiz.quizID=?1")
    List<Answer> showAnswerByQuizID(Long quizID);

    @Query("SELECT count(q) FROM Quiz q WHERE q.course.courseID=?1")
    Long countQuizByCourseID(Long courseID);

    @Query("SELECT q.quizID FROM Quiz q WHERE q.course.courseID=?1")
    List<Long> getQuizIDByCourseID(Long courseID);
}
