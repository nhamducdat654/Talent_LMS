package com.fsoft.team.controllers;

import com.fsoft.team.entity.Course;
import com.fsoft.team.entity.Enrollement;
import com.fsoft.team.entity.User;
import com.fsoft.team.service.CourseService;
import com.fsoft.team.service.EnrollementService;
import com.fsoft.team.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class CertificateController {

    @Autowired
    CourseService courseService;
    @Autowired
    UserService userService;
    @Autowired
    EnrollementService enrollementService;

    @GetMapping("/GenerateCerti")
    public String generateCertificate(Model model, @RequestParam(name = "txtUserName") String username,
            @RequestParam(name = "txtCourseID") Long courseID) {
        Course course = courseService.getCourseById(courseID);
        Enrollement enrollement = enrollementService.getEnrollementByUserNameAndCourseID(username, courseID);
        model.addAttribute("COURSE", course);

        User user = userService.getUserByUserName(username);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" dd/MM/yyyy, HH:mm");
        String date = enrollement.getDateComplete().format(formatter);
        
        model.addAttribute("USER", user);
        model.addAttribute("DATE", date);
        return "ViewCertificate";
    }
}
