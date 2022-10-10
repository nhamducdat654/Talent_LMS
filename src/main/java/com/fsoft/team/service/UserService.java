package com.fsoft.team.service;

import com.fsoft.team.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService  {
    public List<User> getAllUser(int page,int size);
    public User getUserByUserName(String username);
    public Long countAllUser();

    List<User> findAll();
    void disableUser(String username);
    User findByUsername(String username);
    Optional<User> findAllByEmail(String email);

    Optional<User> findById(String s);

    User save(User s);



    boolean checkLogin(String username, String password);

    boolean checkLoginByEmail(String username, String password);

    boolean checkEmailExist(String email);

    boolean checkUsernameExist(String username);
}
