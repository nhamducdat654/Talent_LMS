package com.fsoft.team.controllers;

import com.fsoft.team.dtos.SectionDTO;
import com.fsoft.team.entity.Answer;
import com.fsoft.team.entity.Content;
import com.fsoft.team.entity.Course;
import com.fsoft.team.entity.Enrollement;
import com.fsoft.team.entity.Progress;
import com.fsoft.team.entity.Question;
import com.fsoft.team.entity.Quiz;
import com.fsoft.team.entity.QuizUser;
import com.fsoft.team.entity.Section;
import com.fsoft.team.entity.User;
import com.fsoft.team.service.ContentService;
import com.fsoft.team.service.ContentUserService;
import com.fsoft.team.service.CourseService;
import com.fsoft.team.service.EnrollementService;
import com.fsoft.team.service.ProgressService;
import com.fsoft.team.service.QuizService;
import com.fsoft.team.service.QuizUserService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
public class QuizController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizUserService quizUserService;

    @Autowired
    private ProgressService progressService;

    @Autowired
    private EnrollementService enrollementService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private ContentUserService contentUserService;

    @GetMapping("/doQuiz")
    public String showQuiz(Model model, @RequestParam(name = "quizid") long id, @RequestParam("txtMaxIndex") int maxIndex) {
        model.addAttribute("COUNTQUIZ", quizService.countByQuizID(id));

        Quiz quiz = courseService.getQuiz(id);
        model.addAttribute("QUIZ", quiz);

        List<Section> listSection = courseService.getSection(quiz.getCourse().getCourseID());
        model.addAttribute("COURSE_SECTION", listSection);

        List<Content> listContent = courseService.getContent(quiz.getCourse().getCourseID());
        model.addAttribute("LIST_CONTENT", listContent);

        List<Quiz> listQuiz = courseService.getListQuiz(quiz.getCourse().getCourseID());
        model.addAttribute("LIST_QUIZ", listQuiz);

        List<SectionDTO> count = courseService.getCountSection(quiz.getCourse().getCourseID());
        model.addAttribute("COUNT", count);

        model.addAttribute("MAX_INDEX", maxIndex);

        User user = (User) model.getAttribute("USER");
        List<Progress> listProgress = progressService.getListProgressByUser(user.getUsername());
        model.addAttribute("LIST_PROGRESS", listProgress);

        for (Progress progress : listProgress) {
            try {
                if (progress.getQuizuser().getQuiz().getQuizID() == id) {
                    model.addAttribute("COMPLETE_CONTENT", progress);
                }
            } catch (Exception e) {

            }
        }

        List<Course> myCourse = courseService.getMyCourse(user.getUsername());
        model.addAttribute("PROGRESS", getProgress(user, myCourse, quiz.getCourse().getCourseID()));

        Enrollement en = enrollementService.getEnrollementByUserNameAndCourseID(user.getUsername(), quiz.getCourse().getCourseID());
        model.addAttribute("ENROLLEMENT", en);

        return "doQuiz";
    }

    @GetMapping("/startQuiz")
    public String startQuiz(Model model, @RequestParam(name = "quizid") Long id, @RequestParam("txtMaxIndex") int maxIndex) {
        List<Question> showQuestion = quizService.showQuestion(id);
        List<Answer> showAnswer = quizService.showAnswer();
        model.addAttribute("SHOWANSWER", showAnswer);
        model.addAttribute("SHOWQUESTION", showQuestion);

        Quiz quiz = courseService.getQuiz(id);
        model.addAttribute("QUIZ", quiz);

        List<Section> listSection = courseService.getSection(quiz.getCourse().getCourseID());
        model.addAttribute("COURSE_SECTION", listSection);

        List<Content> listContent = courseService.getContent(quiz.getCourse().getCourseID());
        model.addAttribute("LIST_CONTENT", listContent);

        List<Quiz> listQuiz = courseService.getListQuiz(quiz.getCourse().getCourseID());
        model.addAttribute("LIST_QUIZ", listQuiz);

        List<SectionDTO> count = courseService.getCountSection(quiz.getCourse().getCourseID());
        model.addAttribute("COUNT", count);

        model.addAttribute("MAX_INDEX", maxIndex);

        User user = (User) model.getAttribute("USER");

        List<Progress> listProgress = progressService.getListProgressByUser(user.getUsername());
        model.addAttribute("LIST_PROGRESS", listProgress);

        List<Course> myCourse = courseService.getMyCourse(user.getUsername());
        model.addAttribute("PROGRESS", getProgress(user, myCourse, quiz.getCourse().getCourseID()));

        Enrollement en = enrollementService.getEnrollementByUserNameAndCourseID(user.getUsername(), quiz.getCourse().getCourseID());
        model.addAttribute("ENROLLEMENT", en);
        return "startQuiz";
    }

    @GetMapping("/resultQuiz")
    public String showResultQuiz(Model model, @RequestParam(name = "txtArray") String[] arrayAnswerByUser,
            @RequestParam(name = "quizid") long quizid, @RequestParam("txtMaxIndex") int maxIndex) {

        List<Answer> showAnswer = quizService.showAnswerByQuizID(quizid);
        List<Question> showQuestion = quizService.showQuestion(quizid);
        model.addAttribute("SHOWANSWER", showAnswer);
        model.addAttribute("SHOWQUESTION", showQuestion);

        Quiz quiz = courseService.getQuiz(quizid);
        model.addAttribute("QUIZ", quiz);

        List<Section> listSection = courseService.getSection(quiz.getCourse().getCourseID());
        model.addAttribute("COURSE_SECTION", listSection);

        List<Content> listContent = courseService.getContent(quiz.getCourse().getCourseID());
        model.addAttribute("LIST_CONTENT", listContent);

        List<Quiz> listQuiz = courseService.getListQuiz(quiz.getCourse().getCourseID());
        model.addAttribute("LIST_QUIZ", listQuiz);

        List<SectionDTO> count = courseService.getCountSection(quiz.getCourse().getCourseID());
        model.addAttribute("COUNT", count);

        List<Boolean> getResultMyAns = new ArrayList<>();
        int totalScore = 100;
        Long countQuestion = quizService.countByQuizID(quizid);

        float scoreEachQues = totalScore / countQuestion;
        float resultScoreForUser = 0;
        boolean checkTrue = true;
        for (int i = 0; i < arrayAnswerByUser.length; i++) {
            for (int j = 0; j < showAnswer.size(); j++) {
                if (Long.parseLong(arrayAnswerByUser[i]) == showAnswer.get(j).getAnswerID()) {
                    getResultMyAns.add(showAnswer.get(j).isTrue());
                    if (showAnswer.get(j).isTrue() == checkTrue) {
                        resultScoreForUser += scoreEachQues;
                    }
                }
            }
        }

        User user = (User) model.getAttribute("USER");

        QuizUser quizUser = new QuizUser();

        if (resultScoreForUser >= quiz.getPassScore()) {
            quizUser.setPass(true);
        } else {
            quizUser.setPass(false);
        }
        quizUser.setPoint(resultScoreForUser);
        quizUser.setQuiz(quiz);
        quizUser.setUser(user);
        quizUser.setDoQuizDate(LocalDateTime.now());

        if (quizUserService.checkExistQuizUser(quizid, user.getUsername())) {
            QuizUser newQuizUser;
            try {
                newQuizUser = quizUserService.save(quizUser);
            } catch (Exception e) {
                newQuizUser = quizUserService.save(quizUser);
            }

            if (quizUser.isPass() == true) {
                Progress progress = new Progress();
                progress.setCompleteDate(newQuizUser.getDoQuizDate());
                progress.setQuizuser(newQuizUser);
                progress.setUser(user);
                progressService.saveProgress(progress);
            }
        }
        model.addAttribute("QUIZUSER", quizUser);
        model.addAttribute("GETRESULTMYANS", getResultMyAns);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" dd/MM/yyyy, HH:mm");
        model.addAttribute("DATEDOQUIZ", LocalDateTime.now().format(formatter));

        model.addAttribute("MAX_INDEX", maxIndex);

        List<Course> myCourse = courseService.getMyCourse(user.getUsername());
        model.addAttribute("PROGRESS", getProgress(user, myCourse, quiz.getCourse().getCourseID()));

        List<Progress> listProgress = progressService.getListProgressByUser(user.getUsername());
        model.addAttribute("LIST_PROGRESS", listProgress);

        Enrollement en = enrollementService.getEnrollementByUserNameAndCourseID(user.getUsername(), quiz.getCourse().getCourseID());
        model.addAttribute("ENROLLEMENT", en);

        int prog = getProgress(user, myCourse, quiz.getCourse().getCourseID());
        if (prog == 100) {
            if (!en.isComplete()) {
                en.setComplete(true);
                en.setDateComplete(LocalDateTime.now());
                try {
                    enrollementService.addNewEnroll(en);
                } catch (Exception e) {
                    enrollementService.addNewEnroll(en);
                }
            }
        }
        return "resultQuiz";
    }

    private Long countContentUserIDByContentIDAndUserName(Long courseID, String username) {
        Long count = 0L;
        List<Long> getContentIDByCourseID = contentService.getContentIDByCourseID(courseID);
        if (getContentIDByCourseID.size() > 0) {
            for (Long l : getContentIDByCourseID) {
                count += contentUserService.countContentUserIDByContentIDAndUserName(username, l);
            }
        }
        return count;
    }

    private Long countQuizUserIDByContentIDAndUserName(Long courseID, String username) {
        Long count = 0L;
        List<Long> getQuizIDByCourseID = quizService.getQuizIDByCourseID(courseID);

        if (getQuizIDByCourseID.size() > 0) {
            for (Long l : getQuizIDByCourseID) {

                count += quizUserService.countQuizUserIDByContentIDAndUserName(username, l);
            }
        }
        return count;
    }

    private int getProgress(User user, List<Course> myCourse, Long showContentCourseID) {
        int count = 0;
        List<Long> getCourseIDByUserName = enrollementService.getCourseIDByUserName(user.getUsername());

        int progress = -1;

        if (getCourseIDByUserName.size() > 0) {
            for (int i = 0; i < getCourseIDByUserName.size(); i++) {
                Long countContentByCourseID = contentService.countContentByCourseID(getCourseIDByUserName.get(i));
                Long countQuizByCourseID = quizService.countQuizByCourseID(getCourseIDByUserName.get(i));
                Long totalOfContent = countContentByCourseID + countQuizByCourseID;

                Long countContentUserID = countContentUserIDByContentIDAndUserName(getCourseIDByUserName.get(i), user.getUsername());
                Long countQuizUserID = countQuizUserIDByContentIDAndUserName(getCourseIDByUserName.get(i), user.getUsername());
                Long countProgressByUserName = countContentUserID + countQuizUserID;

                if (countProgressByUserName == 0) {
                    progress = 0;
                } else {
                    if (countContentByCourseID == 0 && countQuizByCourseID == 0) {
                        progress = 0;
                    } else {
                        float percentOfContent = (float) 100 / totalOfContent;
                        progress = Math.round(percentOfContent
                                * Float.parseFloat(String.valueOf(countProgressByUserName)));
                    }
                }
                if (myCourse.get(i).getCourseID() == showContentCourseID) {
                    return progress;
                }
            }
        }
        return count;
    }
}
