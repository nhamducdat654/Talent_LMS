package com.fsoft.team.serviceImp;

import com.fsoft.team.entity.Content;
import com.fsoft.team.repository.ContentRepository;
import com.fsoft.team.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentServiceImp implements ContentService {

    @Autowired
    private ContentRepository contentRepository;

    @Override
    public Content showContent(Long contentID) {
        return contentRepository.showContent(contentID);
    }

    @Override
    public Long countContentByCourseID(Long id) {
        return contentRepository.countContentByCourseID(id);
    }

    @Override
    public List<Long> getContentIDByCourseID(Long courseID) {
        return contentRepository.getContentIDByCourseID(courseID);
    }

}
