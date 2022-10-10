package com.fsoft.team.service;

import com.fsoft.team.entity.Progress;
import java.util.List;

public interface ProgressService {

    Progress saveProgress(Progress progress);
    
    List<Progress> getListProgressByUser(String username);
    
    void resetProgress();

    public Long countProgressByUserName(String username,Long contentUserID);
    
}
