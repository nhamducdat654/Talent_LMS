package com.fsoft.team.serviceImp;

import com.fsoft.team.entity.Category;
import com.fsoft.team.entity.Enrollement;
import com.fsoft.team.repository.EnrollementRepository;
import com.fsoft.team.service.EnrollementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollementServiceImp implements EnrollementService {

    @Autowired
    EnrollementRepository enrollementRepository;

    @Override
    public void addNewEnroll(Enrollement enroll) {
        enrollementRepository.save(enroll);
    }

    @Override
    public List<Enrollement> getEnrollWithCourseID(Long id) {
        return enrollementRepository.getEnrollWithCourseID(id);
    }

    @Override
    public Enrollement deleteEnroll(Long id) {
        Enrollement enrollement = enrollementRepository.getById(id);
        enrollementRepository.delete(enrollement);
        return enrollement;
    }

    @Override
    public List<Long> getCourseIDByUserName(String username) {
        return enrollementRepository.getCourseIDByUserName(username);
    }

    @Override
    public Enrollement getEnrollementByUserNameAndCourseID(String username, Long courseID) {
        return enrollementRepository.getEnrollementByUserNameAndCourseID(username,courseID);
    }

    @Override
    public List<Enrollement> findAll() {
        return enrollementRepository.findAll();
    }

    @Override
    public List<Enrollement> getListEnrollementByUsername(String username) {
        List<Enrollement> listE = enrollementRepository.getEnrollementByUsername(username);
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        for (Enrollement enrollement : listE) {
            try {
                enrollement.getCourse().getCategory().getCategoryID();
            } catch(Exception e) {
                 enrollement.getCourse().setCategory(new Category(null, "General"));
            }
        }
        System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
        
        return enrollementRepository.getEnrollementByUsername(username);
    }

    @Override
    public void save(Enrollement e) {
        enrollementRepository.save(e);
    }

    @Override
    public int delete(Long id) {
        return enrollementRepository.delete(id);
    }

    @Override
    public void updateDateComplete(Long id) {
        enrollementRepository.updateDisable(id);
    }

    @Override
    public void updateIsComplete(Long id) {
        enrollementRepository.updateIsComplete(id);
    }

}
