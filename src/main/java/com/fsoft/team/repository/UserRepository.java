package com.fsoft.team.repository;

import com.fsoft.team.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    @Query("select u from User  u ")
    public List<User> findUserAndPage(Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE User u set u.status = 0 where u.username = :username")
    void updateStatusUser(@Param("username") String username);

    @Transactional
    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByUsername(@Param("username") String username);

    Optional<User> findAllByEmail(String email);
}
