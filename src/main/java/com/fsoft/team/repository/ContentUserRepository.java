package com.fsoft.team.repository;

import com.fsoft.team.entity.ContentUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentUserRepository extends JpaRepository<ContentUser, Long> {

    @Query("SELECT cu FROM ContentUser cu WHERE cu.content.contentID = ?1 AND cu.user.username = ?2")
    ContentUser checkExistContentUser(long contentID, String username);

    @Query("Select count(c) from ContentUser c where c.user.username = ?1 and c.content.contentID = ?2 and c.isComplete = true")
    Long countContentUserIDByContentIDAndUserName(String username, Long contentID);
}
