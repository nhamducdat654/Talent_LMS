package com.fsoft.team.serviceImp;

import com.fsoft.team.entity.QuizUser;
import com.fsoft.team.repository.QuizUserRepository;
import com.fsoft.team.service.QuizUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizUserServiceImp implements QuizUserService {

    @Autowired
    private QuizUserRepository quizUserRepository;
    
    @Override
    public QuizUser save(QuizUser quizUser) {
        return quizUserRepository.save(quizUser);
    }

    @Override
    public boolean checkExistQuizUser(long quizID, String username) {
        QuizUser qu = quizUserRepository.checkExistQuizUser(quizID, username);
        if(qu == null) {
            return true;
        }
        return false;
    }

    @Override
    public Long countQuizUserIDByContentIDAndUserName(String username, Long contentID) {
        return quizUserRepository.countQuizUserIDByContentIDAndUserName(username,contentID);
    }

    @Override
    public void deleteQuizUserByContentIDAndUsername(Long quizID, String username) {
        QuizUser qu = quizUserRepository.checkExistQuizUser(quizID, username);
        if(qu != null) {
            quizUserRepository.delete(qu);
        }
    }

}
