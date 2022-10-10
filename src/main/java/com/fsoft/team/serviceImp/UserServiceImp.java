package com.fsoft.team.serviceImp;

//import com.fsoft.team.config.CustomUserDetails;
import com.fsoft.team.config.CustomUserDetails;
import com.fsoft.team.entity.User;
import com.fsoft.team.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fsoft.team.repository.UserRepository;

@Service
public class UserServiceImp implements UserService, UserDetailsService   {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> getAllUser(int page, int size) {
        return userRepository.findUserAndPage(PageRequest.of(page, size));
    }

    @Override
    public User getUserByUserName(String username) {
        return userRepository.getById(username);
    }

    @Override
    public Long countAllUser() {
        return userRepository.count();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void disableUser(String username) {
        userRepository.updateStatusUser(username);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findAllByEmail(String email) {
        return userRepository.findAllByEmail(email);
    }


    @Override
    public Optional<User> findById(String s) {
        return userRepository.findById(s);
    }

    @Override
    public User save(User s) {
        return userRepository.save(s);
    }

    @Override
    public boolean checkLogin(String username, String password) {
        Optional<User> optionalUser=findById(username);
        if(optionalUser.isPresent() && optionalUser.get().getPassword().equals(password)){
            return true;
        }
        return false;
    }

    @Override
    public boolean checkLoginByEmail(String username, String password) {
        Optional<User> optionalUser=findAllByEmail(username);
        if(optionalUser.isPresent() && optionalUser.get().getPassword().equals(password)){
            return true;
        }
        return false;
    }

    @Override
    public boolean checkEmailExist(String email) {
        Optional<User> optionalUser=findAllByEmail(email);
        if(optionalUser.isPresent()){
            return true;
        }
        return false;
    }

    @Override
    public boolean checkUsernameExist(String username) {
        Optional<User> optionalUser=findById(username);
        if(optionalUser.isPresent()){
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        return new CustomUserDetails(user);
    }
}
