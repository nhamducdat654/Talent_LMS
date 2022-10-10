package com.fsoft.team.serviceImp;

import com.fsoft.team.entity.Content;
import com.fsoft.team.entity.ContentUser;
import com.fsoft.team.entity.Progress;
import com.fsoft.team.entity.Quiz;
import com.fsoft.team.entity.QuizUser;
import com.fsoft.team.repository.ProgressRepository;
import com.fsoft.team.service.ProgressService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProgressServiceImp implements ProgressService {
    
    @Autowired
    private ProgressRepository progressRepository;

    @Override
    public Progress saveProgress(Progress progress) {
        return progressRepository.save(progress);
    }

    @Override
    public List<Progress> getListProgressByUser(String username) {
        List<Progress> listProgress = progressRepository.getListProgressByUser(username);
        
        for (Progress progress : listProgress) {
            try {
                progress.getQuizuser().getQuizUserID();
            } catch(Exception ex) {
                progress.setQuizuser(new QuizUser());
            }
            try {
                progress.getContentuser().getContentUserID();
            } catch(Exception e) {
                progress.setContentuser(new ContentUser());
            }
            try {
                progress.getQuizuser().getQuiz().getQuizID();
            } catch(Exception e) {
                progress.getQuizuser().setQuiz(new Quiz());
            }
               
            try { 
                progress.getContentuser().getContent().getContentID();
            } catch(Exception e) {
                progress.getContentuser().setContent(new Content());
            }
        }
        
        return listProgress;
    }

    @Override
    public void resetProgress() {
        progressRepository.deleteAll();
    }

    @Override
    public Long countProgressByUserName(String username,Long contentUserID) {
        return progressRepository.countProgressByUserName(username,contentUserID);
    }
}
