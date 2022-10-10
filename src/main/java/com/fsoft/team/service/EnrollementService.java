package com.fsoft.team.service;

import com.fsoft.team.entity.Enrollement;

import java.util.List;

public interface EnrollementService {
    public void addNewEnroll(Enrollement enroll);

    public List<Enrollement> getEnrollWithCourseID(Long id);

    public Enrollement deleteEnroll(Long id);

    public List<Long> getCourseIDByUserName(String username);

    public Enrollement getEnrollementByUserNameAndCourseID(String username,Long courseID);

    List<Enrollement> findAll();
    List<Enrollement> getListEnrollementByUsername(String username);
    void save(Enrollement e);
    int delete(Long id);
    void updateDateComplete(Long id);
    void updateIsComplete(Long id);
}
