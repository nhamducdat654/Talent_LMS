package com.fsoft.team.controllers;

import com.fsoft.team.entity.User;
import com.fsoft.team.repository.CreditRepository;
import com.fsoft.team.repository.EnrollementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
@SessionAttributes({"USER"})
public class EnrollController {

    @Autowired
    private EnrollementRepository enrollementRepository;
    @Autowired
    private CreditRepository creditRepository;

    @PostMapping("/course_order")
    public String enroll(Model model, @RequestParam(value = "courseID") String courseID,
            @RequestParam(value = "cardNumber") String cardNumber, @RequestParam(value = "cvc") String cvc,
            @RequestParam(value = "expirationDate") String expirationDate, HttpSession session) {

        LocalDateTime enrollDate = LocalDateTime.now();
        boolean isComplete = false;
        User user = (User) session.getAttribute("USER");
        String username = user.getUsername();
        if (cardNumber.equalsIgnoreCase("free") && cvc.equalsIgnoreCase("free") && expirationDate.equalsIgnoreCase("free")) {
            cardNumber = "";
            cvc = "";
            expirationDate = "";
        }
        if (creditRepository.existsCreditByCardNumber(cardNumber)) {
            enrollementRepository.insertEnrollment(enrollDate, isComplete, username, Long.parseLong(courseID), cardNumber, user.getRole().getRoleName());
        } else {
            creditRepository.insertCredit(cardNumber, cvc, expirationDate, username);
            enrollementRepository.insertEnrollment(enrollDate, isComplete, username, Long.parseLong(courseID), cardNumber, user.getRole().getRoleName());
        }

        return "redirect:/course-detail?txtCourseID=" + courseID + "&txtURL=catalog";
    }

}
