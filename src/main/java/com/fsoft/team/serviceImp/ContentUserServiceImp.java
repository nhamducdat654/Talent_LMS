package com.fsoft.team.serviceImp;

import com.fsoft.team.entity.ContentUser;
import com.fsoft.team.repository.ContentUserRepository;
import com.fsoft.team.service.ContentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentUserServiceImp implements ContentUserService {

    @Autowired
    private ContentUserRepository contentUserRepository;

    @Override
    public ContentUser saveContentUser(ContentUser contentUser) {
        return contentUserRepository.save(contentUser);
    }

    @Override
    public boolean checkExistContentUser(long contentID, String username) {
        ContentUser cu = contentUserRepository.checkExistContentUser(contentID, username);
        if (cu == null) {
            return true;
        }
        return false;
    }

    @Override
    public Long countContentUserIDByContentIDAndUserName(String username, Long contentID) {
        return contentUserRepository.countContentUserIDByContentIDAndUserName(username, contentID);
    }

    @Override
    public void deleteContentUserByContentIDAndUsername(Long contentID, String username) {
        ContentUser cu = contentUserRepository.checkExistContentUser(contentID, username);
        
        if (cu != null) {
            contentUserRepository.delete(cu);
        }
    }

}
