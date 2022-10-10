package com.fsoft.team.repository;

import com.fsoft.team.entity.Progress;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long>{
    
    @Query("SELECT p FROM Progress p WHERE p.user.username = ?1")
    List<Progress> getListProgressByUser(String username);

    @Query("select count(p) from Progress p where p.user.username = ?1 and p.quizuser.quizUserID = ?2")
    public Long countProgressByUserName(String username,Long contentUserID);
}
