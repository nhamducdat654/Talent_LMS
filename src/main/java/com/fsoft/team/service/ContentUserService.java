package com.fsoft.team.service;

import com.fsoft.team.entity.ContentUser;

import java.util.List;

public interface ContentUserService {

    ContentUser saveContentUser(ContentUser contentUser);
    
    boolean checkExistContentUser(long contentID, String username);

    Long countContentUserIDByContentIDAndUserName(String username, Long contentID);
    
    void deleteContentUserByContentIDAndUsername(Long contentID, String username);
}
