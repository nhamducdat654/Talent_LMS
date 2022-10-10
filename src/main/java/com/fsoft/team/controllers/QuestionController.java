package com.fsoft.team.controllers;

import com.fsoft.team.entity.Answer;
import com.fsoft.team.entity.Course;
import com.fsoft.team.entity.Question;
import com.fsoft.team.entity.Quiz;
import com.fsoft.team.service.AnswerService;
import com.fsoft.team.service.CourseService;
import com.fsoft.team.service.QuestionService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes({"USER"})
public class QuestionController {

    @Autowired
    private CourseService courseService;

    @Autowired
    AnswerService answerService;

    @Autowired
    QuestionService questionService;

    @GetMapping("delete-question")
    public String deleteQuestion(@RequestParam("txtCourseID") long courseID, @RequestParam("txtContentID") long quizID, @RequestParam("txtMaxIndex") int max, @RequestParam("txtQuestionID") long questionID) {

        questionService.deleteQuestionByID(questionID);

        return "redirect:/add-quiz-form?txtCourseID=" + courseID + "&txtContentID=" + quizID + "&txtMaxIndex=" + max + "&txtShowQuestion=Show questions from this course";
    }

    @GetMapping("editQuestion")
    public String editQuestion(ModelMap model, @RequestParam("txtCourseID") long courseID, @RequestParam("txtContentID") long quizID, @RequestParam("txtQuestionID") long questionID, @RequestParam("txtMaxIndex") int max) {
        Question question = questionService.findById(questionID);
        List<Answer> listAnswer = answerService.findByQuestionId(questionID);
        model.addAttribute("question", question);
        model.addAttribute("answer", listAnswer);
        Course course = courseService.getCourseDetail(courseID);
        model.addAttribute("COURSE", course);
        if (quizID > 0) {
            Quiz quiz = courseService.getQuiz(quizID);
            model.addAttribute("QUIZ", quiz);
            model.addAttribute("MAX_INDEX", max);
        }
        return "editQuestion";
    }

    @GetMapping("addQuestion")
    public String addQuestion(ModelMap model, @RequestParam("txtCourseID") long courseID, @RequestParam("txtContentID") long quizID, @RequestParam("txtMaxIndex") int max) {
        Question question = new Question();
        Answer answer = new Answer();
        Course course = courseService.getCourseDetail(courseID);
        model.addAttribute("COURSE", course);
        if (quizID > 0) {
            Quiz quiz = courseService.getQuiz(quizID);
            model.addAttribute("QUIZ", quiz);
            model.addAttribute("MAX_INDEX", max);
        }
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "editQuestion";
    }

    @PostMapping("saveOrUpdate")
    public String saveOrUpadte(ModelMap model, Question question, @RequestParam("txtAnswerContent") String[] answer, @RequestParam("txtQuestionID") String questionId,
            @RequestParam(value = "txtCheck", defaultValue = "false") String[] check, RedirectAttributes redirect, @RequestParam("continue") String saveAndAdd,
            @RequestParam("txtCourseID") long courseID, @RequestParam("txtContentID") long quizID, @RequestParam("txtMaxIndex") int max) {

        List<Integer> trueAnswer = new ArrayList<>();
        boolean answerTrue = false;
        boolean checkDub = false;
        if (questionId == "") {

            if (check.length != 1) {
                return "redirect:/addQuestion?txtCourseID=" + courseID + "&txtContentID=" + quizID + "&txtMaxIndex=" + max;
            } else if (question.getQuestionName() == "") {
                return "redirect:/addQuestion?txtCourseID=" + courseID + "&txtContentID=" + quizID + "&txtMaxIndex=" + max;
            }

            for (int i = 0; i < answer.length; ++i) {
                for (int j = i + 1; j < answer.length; j++) {
                    if (answer[i].trim().equalsIgnoreCase(answer[j].trim())) {
                        checkDub = true;
                        break;

                    }
                }

            }
            if (checkDub) {
                return "redirect:/addQuestion?txtCourseID=" + courseID + "&txtContentID=" + quizID + "&txtMaxIndex=" + max;
            }

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formatDateTime = now.format(formatter);
            LocalDateTime dateCreate = LocalDateTime.parse(formatDateTime, formatter);
            question.setDateOfCreate(dateCreate);
            question.setCourse(new Course(courseID));

            for (String string : check) {
                String[] arrSplit = string.split("-");
                try {
                    trueAnswer.add(Integer.parseInt(arrSplit[1]));
                } catch (Exception e) {
                    return "redirect:/addQuestion?txtCourseID=" + courseID + "&txtContentID=" + quizID + "&txtMaxIndex=" + max;
                }
            }
            questionService.saveQuestion(question);
            for (int i = 0; i < answer.length; ++i) {
                for (int j = 0; j < trueAnswer.size(); j++) {
                    if (i == trueAnswer.get(j)) {
                        answerTrue = true;
                        break;
                    } else {
                        answerTrue = false;
                    }
                }
                if (answerTrue) {
                    Answer ans = new Answer(null, answer[i], true, question);
                    answerService.saveAnswer(ans);
                } else {
                    Answer ans = new Answer(null, answer[i], false, question);
                    answerService.saveAnswer(ans);
                }

            }

        } else {

            if (check.length != 1) {
                redirect.addAttribute("id", questionId);

                return "redirect:/addQuestion?txtCourseID=" + courseID + "&txtContentID=" + quizID + "&txtMaxIndex=" + max;
            } else if (question.getQuestionName() == "") {
                redirect.addAttribute("id", questionId);

                return "redirect:/addQuestion?txtCourseID=" + courseID + "&txtContentID=" + quizID + "&txtMaxIndex=" + max;
            }
            
            for (int i = 0; i < answer.length; ++i) {
                for (int j = i + 1; j < answer.length; j++) {
                    if (answer[i].trim().equalsIgnoreCase(answer[j].trim())) {
                        checkDub = true;
                        break;

                    }
                }

            }
            if (checkDub) {
                return "redirect:/addQuestion?txtCourseID=" + courseID + "&txtContentID=" + quizID + "&txtMaxIndex=" + max;
            }

            Long queId = Long.parseLong(questionId);
            Question que = questionService.findById(queId);
            question.setQuestionID(queId);
            question.setDateOfCreate(que.getDateOfCreate());
            question.setCourse(new Course(courseID));

            questionService.saveQuestion(question);
            List<Answer> listAnswer = answerService.findByQuestionId(queId);
            for (String string : check) {
                String[] arrSplit = string.split("-");
                try {
                    trueAnswer.add(Integer.parseInt(arrSplit[1]));
                } catch (Exception e) {
                    return "redirect:/addQuestion?txtCourseID=" + courseID + "&txtContentID=" + quizID + "&txtMaxIndex=" + max;
                }
            }

            for (int i = 0; i < answer.length; ++i) {
                for (int j = 0; j < trueAnswer.size(); j++) {
                    if (i == trueAnswer.get(j)) {
                        answerTrue = true;
                        break;
                    } else {
                        answerTrue = false;
                    }
                }
                if (answerTrue) {
                    Answer ans = new Answer(listAnswer.get(i).getAnswerID(), answer[i], true, question);
                    answerService.saveAnswer(ans);
                } else {
                    Answer ans = new Answer(listAnswer.get(i).getAnswerID(), answer[i], false, question);
                    answerService.saveAnswer(ans);
                }

            }

        }

        if (Boolean.parseBoolean(saveAndAdd) == true) {
            return "redirect:/addQuestion?txtCourseID=" + courseID + "&txtContentID=" + quizID + "&txtMaxIndex=" + max;
        }
        return "redirect:/add-quiz-form?txtCourseID=" + courseID + "&txtContentID=" + quizID + "&txtMaxIndex=" + max + "&txtShowQuestion=Show questions from this course";
    }
}
