package com.fsoft.team.controllers;

import com.fsoft.team.dtos.SectionDTO;
import com.fsoft.team.entity.Content;
import com.fsoft.team.entity.ContentUser;
import com.fsoft.team.entity.Quiz;
import com.fsoft.team.entity.User;
import com.fsoft.team.service.ContentService;
import com.fsoft.team.service.ContentUserService;
import com.fsoft.team.service.CourseService;
import com.fsoft.team.service.ProgressService;
import com.fsoft.team.service.QuizService;
import com.fsoft.team.service.QuizUserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"USER"})
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ContentUserService contentUserService;

    @Autowired
    private QuizUserService quizUserService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private QuizService quizService;

    @GetMapping("/reset-progress")
    public String resetProgress(Model model, @RequestParam("txtCourseID") long courseID) {
        User user = (User) model.getAttribute("USER");

        List<Long> listContentID = contentService.getContentIDByCourseID(courseID);
        for (Long long1 : listContentID) {
            contentUserService.deleteContentUserByContentIDAndUsername(long1, user.getUsername());
        }

        List<Long> listQuizID = quizService.getQuizIDByCourseID(courseID);
        for (Long long1 : listQuizID) {
            quizUserService.deleteQuizUserByContentIDAndUsername(long1, user.getUsername());
        }

        return "redirect:/start-this-course?txtCourseID=" + courseID;
    }

    @GetMapping("start-this-course")
    public String startThisCourse(@RequestParam("txtCourseID") long courseID
    ) {
        List<SectionDTO> count = courseService.getCountSection(courseID);

        List<Content> listContent = courseService.getContent(courseID);

        for (Content content : listContent) {
            if (content.getSection().getSectionID() == count.get(0).getSectionID() && content.getContentIndex() == 1) {
                return "redirect:/doContent?contentid=" + content.getContentID() + "&txtMaxIndex=" + count.get(0).getCountSection();
            }
        }

        List<Quiz> listQuiz = courseService.getListQuiz(courseID);
        for (Quiz quiz : listQuiz) {
            if (quiz.getSection().getSectionID() == count.get(0).getSectionID() && quiz.getQuizIndex() == 1) {
                return "redirect:/doQuiz?quizid=" + quiz.getQuizID() + "&txtMaxIndex=" + count.get(0).getCountSection();
            }
        }

        return "null";
    }
}
