package com.fsoft.team.repository;

import com.fsoft.team.entity.Answer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query("SELECT a FROM Answer a WHERE a.question.questionID = ?1")
    public List<Answer> getListAnswerOfQuestion(Long questionID);

    @Query("SELECT a FROM Answer a WHERE a.question.questionID=?1")
    List<Answer> findByQuestionId(long questionID);
}
