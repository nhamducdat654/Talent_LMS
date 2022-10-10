package com.fsoft.team.service;

import com.fsoft.team.entity.Content;

import java.util.List;

public interface ContentService {
    
    Content showContent(Long contentID);

    public Long countContentByCourseID(Long id);

    List<Long> getContentIDByCourseID(Long courseID);
}
