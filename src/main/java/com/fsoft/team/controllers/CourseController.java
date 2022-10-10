package com.fsoft.team.controllers;

import com.fsoft.team.dtos.CategoryDTO;
import com.fsoft.team.dtos.ProgressDTO;
import com.fsoft.team.dtos.SectionDTO;
import com.fsoft.team.entity.Answer;
import com.fsoft.team.entity.Category;
import com.fsoft.team.entity.Content;
import com.fsoft.team.entity.Course;
import com.fsoft.team.entity.Enrollement;
import com.fsoft.team.entity.Progress;
import com.fsoft.team.entity.Question;
import com.fsoft.team.entity.Quiz;
import com.fsoft.team.entity.Role;
import com.fsoft.team.entity.Section;
import com.fsoft.team.entity.User;
import com.fsoft.team.repository.EnrollementRepository;
import com.fsoft.team.service.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

@Controller
@SessionAttributes({"USER", "ROLE"})
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private EnrollementService enrollementService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private ProgressService progressService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private QuizService quizService;
    @Autowired
    private QuizUserService quizUserService;
    @Autowired
    private ContentUserService contentUserService;

    @Autowired
    private EnrollementRepository enrollementRepository;

    @GetMapping("/home-page")
    public String homePage(Model model, Authentication au) {
        String username = au.getName();
        User user = userService.getUserByUserName(username);
        model.addAttribute("USER",user);
        List<Course> myCourse = courseService.getMyCourse(user.getUsername());

        if (user.getRole().getRoleID() == "ROLE_LEANER") {

            List<Long> getCourseIDByUserName = enrollementService.getCourseIDByUserName(user.getUsername());

            int progress = -1;

            List<ProgressDTO> progressDTOList = null;

            if (getCourseIDByUserName.size() > 0) {
                progressDTOList = new ArrayList<>();
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
                    ProgressDTO progressDTO = new ProgressDTO();
                    progressDTO.setCourseID(myCourse.get(i).getCourseID());
                    progressDTO.setCalcProgress(progress);
                    progressDTOList.add(progressDTO);

                }
            }
            model.addAttribute("EACH_PROGRESS", progressDTOList);
        }

        model.addAttribute("MY_COURSE", myCourse);

        List<Category> cate = courseService.getCategoryOfMyCourse(user.getUsername());
        model.addAttribute("CATE", cate);
        return "home";

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

    @GetMapping("/course-catalog")
    public String courseCatalog(Model model) {

        User user = (User) model.getAttribute("USER");
        List<Course> myCourse = courseService.getMyCourse(user.getUsername());
        model.addAttribute("MY_COURSE", myCourse);

        List<Course> courseCatalog = courseService.getCourseCatalog();
        model.addAttribute("COURSE_CATALOG", courseCatalog);

        List<Long> notMyCourse = new ArrayList<>();
        for (Course courseCata : courseCatalog) {
            boolean flag = true;
            for (Course mCourse : myCourse) {
                if (courseCata.getCourseID() == mCourse.getCourseID()) {
                    flag = false;
                }
            }
            if (flag) {
                notMyCourse.add(courseCata.getCourseID());
            }
        }
        model.addAttribute("NOT_OWN", notMyCourse);

        List<CategoryDTO> categoryCatalog = courseService.getCategoryCatalog();
        model.addAttribute("CATEGORY_CATALOG", categoryCatalog);

        return "courseCatalog";
    }

    @GetMapping("/course-detail")
    public String courseDetail(Model model, @RequestParam("txtCourseID") long courseID, @RequestParam("txtURL") String page) {
        Course courseDetail = courseService.getCourseDetail(courseID);
        model.addAttribute("COURSE_DETAIL", courseDetail);

        User user = (User) model.getAttribute("USER");
        List<Course> myCourse = courseService.getMyCourse(user.getUsername());
        model.addAttribute("MY_COURSE", myCourse);

        boolean flag = true;
        for (Course course : myCourse) {
            if (course.getCourseID() == courseDetail.getCourseID()) {
                flag = false;
            }
        }
        if (flag) {
            model.addAttribute("NOT_OWN", courseDetail.getCourseID());
        }

        List<Section> listSection = courseService.getSection(courseID);
        model.addAttribute("COURSE_SECTION", listSection);

        List<Content> listContent = courseService.getContent(courseID);
        model.addAttribute("LIST_CONTENT", listContent);

        List<Quiz> listQuiz = courseService.getListQuiz(courseID);
        model.addAttribute("LIST_QUIZ", listQuiz);

        List<SectionDTO> count = courseService.getCountSection(courseID);
        model.addAttribute("COUNT", count);

        List<Course> ownCourse = courseService.getAllCourseByUsername(user.getUsername());
        boolean flag2 = true;
        for (Course course : ownCourse) {
            if (course.getCourseID() == courseDetail.getCourseID()) {
                flag2 = false;
            }
        }

        if (page.equals("catalog") || user.getRole().getRoleID().equals("ROLE_LEANER") || flag2) {

            List<Progress> listProgress = progressService.getListProgressByUser(user.getUsername());
            model.addAttribute("LIST_PROGRESS", listProgress);
            model.addAttribute("isEnrolled", enrollementRepository.existsEnrollmentByUser_UsernameAndCourse_CourseIDIs(user.getUsername(), courseID));
            return "detailCourse";
        } else {
            model.addAttribute("UNITS", listContent.size() + listQuiz.size());
            return "course";
        }
    }

    @GetMapping("enroll-this-course")
    public String enrollCourse(Model model, @RequestParam("txtCourseID") long courseID) {
        User user = (User) model.getAttribute("USER");

        Enrollement enrollement = new Enrollement(LocalDateTime.now(), false, user, new Course(courseID));
        enrollement.setCheckDisable(1);
        enrollement.setRole(user.getRole().getRoleName());
        courseService.enrollThisCourse(enrollement);

        return "redirect:/course-detail?txtCourseID=" + courseID + "&txtURL=catalog";
    }

    @GetMapping("/add-content-form")
    public String addContentForm(Model model, @RequestParam("txtCourseID") long courseID, @RequestParam("txtContentID") Long contentID, @RequestParam("txtMaxIndex") int max) {
        Course course = courseService.getCourseDetail(courseID);
        model.addAttribute("COURSE", course);

        List<Section> listSection = courseService.getSection(courseID);
        model.addAttribute("COURSE_SECTION", listSection);

        Content content = null;
        if (contentID > 0) {
            content = courseService.getContentDetail(contentID);

            model.addAttribute("MAX_INDEX", max);
            model.addAttribute("CONTENT_DETAIL", content);

            List<SectionDTO> count = courseService.getCountSection(courseID);
            model.addAttribute("COUNT", count);

            List<SectionDTO> listSectionDTO = new ArrayList<>();
            for (Section section : listSection) {
                SectionDTO dto = new SectionDTO();
                dto.setSectionID(section.getSectionID());
                dto.setSectionName(section.getSectionName());
                listSectionDTO.add(dto);
            }

            for (SectionDTO sectionDTO : listSectionDTO) {
                boolean flag = true;
                for (SectionDTO countSection : count) {
                    if (sectionDTO.getSectionID().equals(countSection.getSectionID())) {
                        sectionDTO.setCountSection(countSection.getCountSection());
                        flag = false;
                    }
                }
                if (flag) {
                    sectionDTO.setCountSection((long) 0);
                }
            }

            model.addAttribute("COURSE_SECTION", listSectionDTO);

            boolean flag = true;
            for (SectionDTO sectionDTO : count) {
                if (sectionDTO.getSectionID() == null) {
                    model.addAttribute("COUNT_NULL", sectionDTO.getCountSection());
                    flag = false;
                }
            }
            if (flag) {
                model.addAttribute("COUNT_NULL", 0);
            }
        }

        return "addContent";
    }

    @GetMapping("/add-webcontent-form")
    public String addWebContentForm(Model model, @RequestParam("txtCourseID") long courseID, @RequestParam("txtContentID") Long contentID, @RequestParam("txtMaxIndex") int max) {
        Course course = courseService.getCourseDetail(courseID);
        model.addAttribute("COURSE", course);

        List<Section> listSection = courseService.getSection(courseID);
        model.addAttribute("COURSE_SECTION", listSection);

        Content content = null;
        if (contentID != 0) {
            content = courseService.getContentDetail(contentID);
            model.addAttribute("CONTENT_DETAIL", content);
            model.addAttribute("MAX_INDEX", max);

            List<SectionDTO> count = courseService.getCountSection(courseID);
            model.addAttribute("COUNT", count);

            List<SectionDTO> listSectionDTO = new ArrayList<>();
            for (Section section : listSection) {
                SectionDTO dto = new SectionDTO();
                dto.setSectionID(section.getSectionID());
                dto.setSectionName(section.getSectionName());
                listSectionDTO.add(dto);
            }

            for (SectionDTO sectionDTO : listSectionDTO) {
                boolean flag = true;
                for (SectionDTO countSection : count) {
                    if (sectionDTO.getSectionID().equals(countSection.getSectionID())) {
                        sectionDTO.setCountSection(countSection.getCountSection());
                        flag = false;
                    }
                }
                if (flag) {
                    sectionDTO.setCountSection((long) 0);
                }
            }
            model.addAttribute("COURSE_SECTION", listSectionDTO);

            boolean flag = true;
            for (SectionDTO sectionDTO : count) {
                if (sectionDTO.getSectionID() == null) {
                    model.addAttribute("COUNT_NULL", sectionDTO.getCountSection());
                    flag = false;
                }
            }
            if (flag) {
                model.addAttribute("COUNT_NULL", 0);
            }
        }

        return "addWebContent";
    }

    @GetMapping("/add-quiz-form")
    public String addQuizForm(Model model, @RequestParam("txtCourseID") long courseID, @RequestParam("txtContentID") long quizID, @RequestParam("txtMaxIndex") int max, @RequestParam("txtShowQuestion") String showQuestion) {

        Course course = courseService.getCourseDetail(courseID);
        model.addAttribute("COURSE", course);

        List<Section> listSection = courseService.getSection(courseID);
        model.addAttribute("COURSE_SECTION", listSection);

        List<Question> listQuestion;
        if (showQuestion.equals("Show questions from this course")) {
            listQuestion = courseService.getListQuestionOfCourse(courseID);
            model.addAttribute("LIST_QUESTION", listQuestion);
            model.addAttribute("SHOW_QUESTION", "Show questions from all courses");
        } else {
            listQuestion = courseService.getListAllQuestion();
            model.addAttribute("LIST_QUESTION", listQuestion);
            model.addAttribute("SHOW_QUESTION", "Show questions from this course");
        }
        if (max == 0) {
            return "addQuiz";
        } else {
            List<Question> listQuestionOfQuiz = courseService.getListQuestionOfQuiz(quizID);
            model.addAttribute("LIST_QUESTION_OF_QUIZ", listQuestionOfQuiz);

            for (Question question : listQuestionOfQuiz) {
                boolean flag = true;
                for (Question question1 : listQuestion) {
                    if (question.getQuestionName().equals(question1.getQuestionName())) {
                        flag = false;
                    }
                }
                if (flag) {
                    listQuestion.add(question);
                }
            }

            for (int i = 0; i < listQuestion.size(); i++) {
                for (int j = 0; j < listQuestionOfQuiz.size(); j++) {
                    if (listQuestion.get(i).getQuestionName().equals(listQuestionOfQuiz.get(j).getQuestionName())) {
                        listQuestion.get(i).setQuiz(listQuestionOfQuiz.get(j).getQuiz());
                        listQuestion.get(i).setQuestionID(listQuestionOfQuiz.get(j).getQuestionID());
                    }
                }
            }
            for (Question question : listQuestion) {
                try {
                    question.getQuiz().getQuizID();
                } catch (Exception e) {
                    question.setQuiz(new Quiz());
                }
            }
            model.addAttribute("LIST_QUESTION", listQuestion);

            Quiz quiz = courseService.getQuiz(quizID);
            model.addAttribute("QUIZ", quiz);

            model.addAttribute("MAX_INDEX", max);

            List<SectionDTO> count = courseService.getCountSection(courseID);
            model.addAttribute("COUNT", count);

            List<SectionDTO> listSectionDTO = new ArrayList<>();
            for (Section section : listSection) {
                SectionDTO dto = new SectionDTO();
                dto.setSectionID(section.getSectionID());
                dto.setSectionName(section.getSectionName());
                listSectionDTO.add(dto);
            }

            for (SectionDTO sectionDTO : listSectionDTO) {
                boolean flag = true;
                for (SectionDTO countSection : count) {
                    if (sectionDTO.getSectionID().equals(countSection.getSectionID())) {
                        sectionDTO.setCountSection(countSection.getCountSection());
                        flag = false;
                    }
                }
                if (flag) {
                    sectionDTO.setCountSection((long) 0);
                }
            }
            model.addAttribute("COURSE_SECTION", listSectionDTO);

            boolean flag = true;
            for (SectionDTO sectionDTO : count) {
                if (sectionDTO.getSectionID() == null) {
                    model.addAttribute("COUNT_NULL", sectionDTO.getCountSection());
                    flag = false;
                }
            }
            if (flag) {
                model.addAttribute("COUNT_NULL", 0);
            }

            return "editQuiz";
        }
    }

    @GetMapping("/add-content")
    public String addContent(Model model, @RequestParam("txtSectionID") String sectionID, @RequestParam("txtEditor") String editor, @RequestParam("txtCourseID") long courseID, @RequestParam("txtUnitName") String unitName, @RequestParam("txtSelect") String select, @RequestParam("txtContentID") long contentID, @RequestParam("txtContentIndex") int contentIndex) {

        List<SectionDTO> listSectionDTO = courseService.getCountSection(courseID);

        Content content = null;
        if (!sectionID.isEmpty()) {
            long section = 0;
            section = Long.parseLong(sectionID);

            long index = 0;

            for (SectionDTO sectionDTO : listSectionDTO) {
                if (sectionDTO.getSectionID() != null) {
                    if (sectionDTO.getSectionID() == section) {
                        index = sectionDTO.getCountSection() + 1;
                    }
                }
            }
            if (index == 0) {
                index = 1;
            }

            content = new Content(unitName, editor, (int) index, new Course(courseID), new Section(section));
        } else {
            long index = 0;

            for (SectionDTO sectionDTO : listSectionDTO) {
                if (sectionDTO.getSectionID() == null) {
                    index = sectionDTO.getCountSection() + 1;
                }
            }
            if (index == 0) {
                index = 1;
            }

            content = new Content(unitName, editor, (int) index, new Course(courseID), null);
        }

        if (contentID > 0) {
            content.setContentID(contentID);
            content.setContentIndex(contentIndex);
            long secID = 0;
            if (!sectionID.isEmpty()) {
                secID = Long.parseLong(sectionID);
            }
            try {
                courseService.updateListContentAfterUpdate(contentID, secID, courseID, contentIndex, "content");
            } catch (Exception e) {
                courseService.updateListContentAfterUpdate(contentID, secID, courseID, contentIndex, "content");
            }
        }

        Content newContent = null;
        try {
            newContent = courseService.saveOrUpdateContent(content);
        } catch (Exception ex) {
            newContent = courseService.saveOrUpdateContent(content);
        }

        if (select.equals("addAnother")) {
            Course course = courseService.getCourseDetail(courseID);
            model.addAttribute("COURSE", course);

            List<Section> listSection = courseService.getSection(courseID);
            model.addAttribute("COURSE_SECTION", listSection);

            return "addContent";
        } else if (select.equals("backUnitList")) {
            return "redirect:/course-detail?txtCourseID=" + courseID + "&txtURL=home";
        } else {
            List<SectionDTO> count = courseService.getCountSection(courseID);

            long max = 1;
            for (SectionDTO sectionDTO : count) {
                if (sectionDTO.getSectionID() == newContent.getSection().getSectionID()) {
                    max = sectionDTO.getCountSection();
                }
            }

            return "redirect:/doContent?contentid=" + newContent.getContentID() + "&txtMaxIndex=" + max;
        }
    }

    @GetMapping("/delete-content")
    public String deleteContent(@RequestParam("txtCourseID") long courseID, @RequestParam("txtContentID") long contentID, @RequestParam("txtContentOrQuiz") String contentOrQuiz, @RequestParam("txtSectionID") Long sectionID) {

        courseService.updateListContentAfterDelete(contentID, sectionID, courseID, contentOrQuiz);
        courseService.deleteContentOrQuiz(contentID, contentOrQuiz);
        return "redirect:/course-detail?txtCourseID=" + courseID + "&txtURL=home";
    }

    @GetMapping("/add-webcontent")
    public String addWebContent(Model model, @RequestParam("txtCourseID") long courseID, @RequestParam("txtUnitName") String unitName, @RequestParam("txtSectionID") String sectionID, @RequestParam("txtUrlAddress") String url, @RequestParam("txtSelect") String select, @RequestParam("txtContentID") long contentID, @RequestParam("txtContentIndex") int contentIndex) {

        List<SectionDTO> listSectionDTO = courseService.getCountSection(courseID);

        Content content = null;
        if (!sectionID.isEmpty()) {
            long section = 0;
            section = Long.parseLong(sectionID);

            long index = 0;

            for (SectionDTO sectionDTO : listSectionDTO) {
                if (sectionDTO.getSectionID() != null) {
                    if (sectionDTO.getSectionID() == section) {
                        index = sectionDTO.getCountSection() + 1;
                    }
                }
            }
            if (index == 0) {
                index = 1;
            }
            content = new Content(unitName, url, (int) index, new Course(courseID), new Section(section));
        } else {
            long index = 0;

            for (SectionDTO sectionDTO : listSectionDTO) {
                if (sectionDTO.getSectionID() == null) {
                    index = sectionDTO.getCountSection() + 1;
                }
            }
            if (index == 0) {
                index = 1;
            }
            content = new Content(unitName, url, (int) index, new Course(courseID), null);
        }

        if (contentID > 0) {
            content.setContentID(contentID);
            content.setContentIndex(contentIndex);
            long secID = 0;
            if (!sectionID.isEmpty()) {
                secID = Long.parseLong(sectionID);
            }
            try {
                courseService.updateListContentAfterUpdate(contentID, secID, courseID, contentIndex, "content");
            } catch (Exception e) {
                courseService.updateListContentAfterUpdate(contentID, secID, courseID, contentIndex, "content");
            }
        }

        Content newContent = null;
        try {
            newContent = courseService.saveOrUpdateContent(content);
        } catch (Exception e) {
            newContent = courseService.saveOrUpdateContent(content);
        }

        if (select.equals("addAnother")) {
            Course course = courseService.getCourseDetail(courseID);
            model.addAttribute("COURSE", course);

            List<Section> listSection = courseService.getSection(courseID);
            model.addAttribute("COURSE_SECTION", listSection);

            return "addWebContent";
        } else if (select.equals("backUnitList")) {
            return "redirect:/course-detail?txtCourseID=" + courseID + "&txtURL=home";
        } else {
            List<SectionDTO> count = courseService.getCountSection(courseID);

            long max = 1;
            for (SectionDTO sectionDTO : count) {
                if (sectionDTO.getSectionID() == newContent.getSection().getSectionID()) {
                    max = sectionDTO.getCountSection();
                }
            }
            return "redirect:/doContent?contentid=" + newContent.getContentID() + "&txtMaxIndex=" + max;
        }
    }

    @GetMapping("add-quiz")
    public String addQuiz(Model model, @RequestParam("txtCourseID") long courseID, @RequestParam("txtUnitName") String unitName, @RequestParam("txtSection") String sectionID, @RequestParam("txtListQuestion") Long[] question, @RequestParam("txtSelect") String select, @RequestParam("txtTimeLimit") int timeLimit, @RequestParam("txtPassScore") float passScore) {

        List<SectionDTO> listSectionDTO = courseService.getCountSection(courseID);

        Quiz quiz = new Quiz();
        quiz.setUnitName(unitName);
        quiz.setCourse(new Course(courseID));
        quiz.setTimeLimit(timeLimit);
        quiz.setPassScore(passScore);
        quiz.setDateOfCreate(LocalDateTime.now());

        if (!sectionID.isEmpty()) {
            long section = 0;
            section = Long.parseLong(sectionID);

            long index = 0;

            for (SectionDTO sectionDTO : listSectionDTO) {
                if (sectionDTO.getSectionID() != null) {
                    if (sectionDTO.getSectionID() == section) {
                        index = sectionDTO.getCountSection() + 1;
                    }
                }
            }
            if (index == 0) {
                index = 1;
            }
            quiz.setSection(new Section(section));
            quiz.setQuizIndex((int) index);
        } else {
            long index = 0;

            for (SectionDTO sectionDTO : listSectionDTO) {
                if (sectionDTO.getSectionID() == null) {
                    index = sectionDTO.getCountSection() + 1;
                }
            }
            if (index == 0) {
                index = 1;
            }
            quiz.setSection(null);
            quiz.setQuizIndex((int) index);
        }

        Quiz newQuiz = null;
        try {
            newQuiz = courseService.addQuiz(quiz);
            courseService.addQuestionOfQuiz(question, newQuiz);

        } catch (Exception e) {
            newQuiz = courseService.addQuiz(quiz);
            courseService.addQuestionOfQuiz(question, newQuiz);
        }

        if (select.equals("addAnother")) {
            Course course = courseService.getCourseDetail(courseID);
            model.addAttribute("COURSE", course);

            List<Section> listSection = courseService.getSection(courseID);
            model.addAttribute("COURSE_SECTION", listSection);

            List<Question> listQuestion = courseService.getListQuestionOfCourse(courseID);
            model.addAttribute("LIST_QUESTION", listQuestion);

            model.addAttribute("SHOW_QUESTION", "Show questions from all courses");

            return "addQuiz";
        } else if (select.equals("backUnitList")) {
            return "redirect:/course-detail?txtCourseID=" + courseID + "&txtURL=home";
        } else {

            List<SectionDTO> count = courseService.getCountSection(courseID);

            long max = 1;
            for (SectionDTO sectionDTO : count) {
                if (sectionDTO.getSectionID() == newQuiz.getSection().getSectionID()) {
                    max = sectionDTO.getCountSection();
                }
            }

            return "redirect:/doQuiz?quizid=" + newQuiz.getQuizID() + "&txtMaxIndex=" + max;
        }
    }

    @GetMapping("edit-quiz")
    public String editQuiz(Model model, @RequestParam("txtCourseID") long courseID, @RequestParam("txtUnitName") String unitName, @RequestParam("txtSection") String sectionID, @RequestParam("txtListQuestion") Long[] question, @RequestParam("txtSelect") String select, @RequestParam("txtTimeLimit") int timeLimit, @RequestParam("txtPassScore") float passScore, @RequestParam("txtQuizID") long quizID, @RequestParam("txtIndex") int index) {

        Quiz quiz = courseService.getQuiz(quizID);

        quiz.setUnitName(unitName);
        quiz.setTimeLimit(timeLimit);
        quiz.setPassScore(passScore);

        long secID = 0;
        if (!sectionID.isEmpty()) {
            secID = Long.parseLong(sectionID);
        }

        try {
            courseService.updateListContentAfterUpdate(quizID, secID, courseID, index, "quiz");
        } catch (Exception e) {
            courseService.updateListContentAfterUpdate(quizID, secID, courseID, index, "quiz");
        }

        if (!sectionID.isEmpty()) {
            long section = 0;
            section = Long.parseLong(sectionID);
            quiz.setSection(new Section(section));
        } else {
            quiz.setSection(null);
        }

        quiz.setQuizIndex(index);

        Quiz newQuiz = null;
        try {
            newQuiz = courseService.addQuiz(quiz);
            courseService.updateQuestionOfQuiz(question, newQuiz);
        } catch (Exception e) {
            newQuiz = courseService.addQuiz(quiz);
            courseService.updateQuestionOfQuiz(question, newQuiz);
        }
        if (select.equals("addAnother")) {
            Course course = courseService.getCourseDetail(courseID);
            model.addAttribute("COURSE", course);

            List<Section> listSection = courseService.getSection(courseID);
            model.addAttribute("COURSE_SECTION", listSection);

            List<Question> listQuestion = courseService.getListQuestionOfCourse(courseID);
            model.addAttribute("LIST_QUESTION", listQuestion);

            model.addAttribute("SHOW_QUESTION", "Show questions from all courses");
            return "addQuiz";
        } else if (select.equals("backUnitList")) {
            return "redirect:/course-detail?txtCourseID=" + courseID + "&txtURL=home";
        } else {
            List<SectionDTO> count = courseService.getCountSection(courseID);

            long max = 1;
            for (SectionDTO sectionDTO : count) {
                if (sectionDTO.getSectionID() == newQuiz.getSection().getSectionID()) {
                    max = sectionDTO.getCountSection();
                }
            }

            return "redirect:/doQuiz?quizid=" + newQuiz.getQuizID() + "&txtMaxIndex=" + max;
        }
    }

    @GetMapping("add-section")
    public String addSection(@RequestParam("txtSectionID") long sectionID, @RequestParam("txtSectionName") String sectionName, @RequestParam("txtCourseID") long courseID, @RequestParam("txtSectionIndex") int sectionIndex) {

        long index = courseService.getIndexSection(courseID) + 1;

        Section section = new Section((long) 0, sectionName, (int) index, new Course(courseID), null, null);

        if (sectionID > 0) {
            section.setSectionID(sectionID);
            section.setSectionIndex(sectionIndex);

            courseService.updateListSectionAfterUpdate(sectionID, courseID, sectionIndex);
        }

        courseService.saveOrUpdateSection(section);

        return "redirect:/course-detail?txtCourseID=" + courseID + "&txtURL=home";
    }

    @GetMapping("delete-section")
    public String deleteSection(@RequestParam("txtSectionID") long sectionID, @RequestParam("txtCourseID") long courseID) {

        boolean flag = true;
        List<SectionDTO> count = courseService.getCountSection(courseID);
        for (SectionDTO sectionDTO : count) {
            if (sectionDTO.getSectionID() != null && sectionDTO.getSectionID() == sectionID) {
                flag = false;
            }
        }

        if (flag) {
            try {
                courseService.updateListSectionAfterDelete(sectionID, courseID);
                courseService.deleteSection(sectionID);
            } catch (Exception ex) {

                courseService.updateListSectionAfterDelete(sectionID, courseID);
                courseService.deleteSection(sectionID);
            }
        }

        return "redirect:/course-detail?txtCourseID=" + courseID + "&txtURL=home";
    }

    @GetMapping("/addCourse")
    public String addCourseHome(Model model) {
        List<Category> result = categoryService.getAllCategory();
        model.addAttribute("ALLCATE", result);
        return "addCourse";
    }

    @PostMapping("/addNewCourse")
    public String courseHome(@RequestParam(name = "image") MultipartFile multipartFile,
            @RequestParam(name = "txtName") String name,
            @RequestParam(name = "txtTextArea") String description,
            @RequestParam(name = "txtPrice") String price,
            @RequestParam(name = "txtValueSearch") String cateName,
            @RequestParam(name = "url") String url,
            Model model
    ) {

        Course course = new Course();
        course.setDateOfCreate(LocalDateTime.now());
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if (!fileName.isEmpty()) {
            String uploadDir = "src/main/resources/static";
            uploadFile(uploadDir, "img/" + fileName, multipartFile);
            course.setCourseImg("img/" + fileName);
        }
        course.setCourseDes(description);
        course.setCourseName(name);
        if (!price.isEmpty()) {
            course.setPrice(Float.parseFloat(price));
        } else {
            course.setPrice(0);
        }
        course.setStatus(1);
        if (!cateName.isEmpty()) {
            if (cateName.contains("(+ add category)")) {
                Category category = categoryService.addCategory(cateName);
                course.setCategory(category);
            } else {
                String idCate = cateName.split(". ")[0];
                Category cate = categoryService.getCateById(Long.parseLong(idCate));
                course.setCategory(cate);
            }

        }

        User user = (User) model.getAttribute("USER");
        course.setUser(user);
        Course newCourse = courseService.addCourse(course);
        Enrollement enrollement = new Enrollement();
        enrollement.setEnrollDate(LocalDateTime.now());
        enrollement.setComplete(false);
        enrollement.setUser(user);
        enrollement.setCourse(course);
        enrollement.setCheckDisable(1);
        enrollement.setRole(user.getRole().getRoleName());
        enrollementService.addNewEnroll(enrollement);
        if (url.isEmpty()) {
            url = "redirect:/editUsers?txtid=" + enrollement.getCourse().getCourseID();
        } else if (url.equals("addContent")) {
            url = "redirect:/add-content-form?txtCourseID=" + newCourse.getCourseID() + "&txtContentID=0&txtMaxIndex=0";
        } else if (url.equals("addCourse")) {
            url = "redirect:/addCourse";
        } else {
            url = "redirect:/courseList";
        }
        return url;
    }

    @GetMapping("/unEnroll")
    public String unEnroll(@RequestParam(name = "txtArray") String[] array, HttpServletRequest request) {
        String courseid = array[0];
        List<Enrollement> listEnroll = enrollementService.getEnrollWithCourseID(Long.parseLong(courseid));
        for (int i = 1; i < array.length; i++) {
            for (int j = 0; j < listEnroll.size(); j++) {
                if (array[i].equalsIgnoreCase(listEnroll.get(j).getUser().getUsername())) {
                    enrollementService.deleteEnroll(listEnroll.get(j).getEnrollID());
                }
            }
        }
        return "redirect:/editUsers?txtid=" + listEnroll.get(0).getCourse().getCourseID()
                + "&txtCount=" + request.getParameter("txtCount");

    }

    @GetMapping("/editUsers")
    public String editUsers(Model model, HttpServletRequest request) {
        String idCourse = request.getParameter("txtid");

        Long countAllUser = userService.countAllUser();
        String count = request.getParameter("txtCount");
        if (count == null) {
            if (countAllUser < 10) {
                count = String.valueOf(countAllUser);
            } else {
                count = "10";
            }
        } else {

            if (countAllUser < Long.parseLong(count)) {
                count = String.valueOf(countAllUser);
            }
        }
        List<Enrollement> listEnroll = enrollementService.getEnrollWithCourseID(Long.parseLong(idCourse));
        List<User> getAllUser = userService.getAllUser(0, Integer.parseInt(count));

        for (int i = 0; i < getAllUser.size(); i++) {
            for (int j = 0; j < listEnroll.size(); j++) {
                if (getAllUser.get(i).getUsername().equalsIgnoreCase(listEnroll.get(j).getUser().getUsername())) {
                    getAllUser.remove(getAllUser.get(i));
                    i--;
                    break;
                }
            }
        }

        model.addAttribute("FORMATTER", DateTimeFormatter.ofPattern(" dd/MM/yyyy, HH:mm"));

        model.addAttribute("countUser", Long.parseLong(count));
        model.addAttribute("countAllUser", countAllUser);
        model.addAttribute("courseTitle", courseService.getCourseById(Long.parseLong(idCourse)).getCourseName());
        model.addAttribute("courseid", listEnroll.get(0).getCourse().getCourseID());
        model.addAttribute("MAINUSER", listEnroll.get(0).getUser().getUsername());
        model.addAttribute("USERS", getAllUser);
        model.addAttribute("ENROLL", listEnroll);
        model.addAttribute("ROLE", "user");
        return "editCourse";
    }

    @GetMapping("/enroll")
    public String enrollAlotsUser(@RequestParam(name = "txtArray") String[] array, HttpServletRequest request) {
        String coureid = array[0];
        List<String> allName = new ArrayList<>();

        for (int i = 1; i < array.length; i++) {
            allName.add(array[i]);
        }
        List<Enrollement> listEnroll = enrollementService.getEnrollWithCourseID(Long.parseLong(coureid));

        for (int i = 0; i < allName.size(); i++) {
            for (int j = 0; j < listEnroll.size(); j++) {
                if (allName.get(i).equalsIgnoreCase(listEnroll.get(j).getUser().getUsername())) {
                    allName.remove(allName.get(i));
                    i--;
                    break;
                }
            }
        }

        for (String s : allName) {
            Enrollement enrollement = new Enrollement();
            enrollement.setEnrollDate(LocalDateTime.now());
            enrollement.setComplete(false);
            Course course = courseService.getCourseById(Long.parseLong(coureid));
            enrollement.setCourse(course);
            User user = userService.getUserByUserName(s);
            enrollement.setUser(user);
            enrollement.setCheckDisable(1);
            enrollement.setRole(user.getRole().getRoleName());
            enrollementService.addNewEnroll(enrollement);
        }
        return "redirect:/editUsers?txtid=" + coureid + "&txtCount=" + request.getParameter("txtCount");
    }

    @GetMapping("/addUserInCouser")
    public String addUserInCouser(@RequestParam(name = "txtCourseID") String courseid,
            @RequestParam(name = "txtUserName") String userName) {
        Enrollement enrollement = new Enrollement();
        enrollement.setEnrollDate(LocalDateTime.now());
        enrollement.setComplete(false);
        Course course = courseService.getCourseById(Long.parseLong(courseid));
        enrollement.setCourse(course);
        User user = userService.getUserByUserName(userName);
        enrollement.setUser(user);
        enrollement.setCheckDisable(1);
        enrollement.setRole(user.getRole().getRoleName());
        enrollementService.addNewEnroll(enrollement);
        return "redirect:/editUsers?txtid=" + enrollement.getCourse().getCourseID();
    }

    @GetMapping("/deleteEnrol")
    public String deleteEnroll(@RequestParam(name = "txtid") String id) {
        Enrollement enrollement = enrollementService.deleteEnroll(Long.parseLong(id));
        return "redirect:/editUsers?txtid=" + enrollement.getCourse().getCourseID();
    }

    @GetMapping("/courseList")
    public String courseListHome(Model model, HttpServletRequest request) {
        String count = request.getParameter("txtCount");

        User user = (User) model.getAttribute("USER");

        Long countAll = courseService.countCourseByUserName(user.getUsername());

        if (count == null) {
            if (countAll >= 10) {
                count = "10";
            } else {
                count = String.valueOf(countAll);
            }

        } else {
            if (countAll < Long.parseLong(count)) {
                count = String.valueOf(countAll);
            }
        }

        int page = Integer.parseInt(count);
        if (page > 0) {
            List<Course> result = courseService.getCourseByUsername(user.getUsername(), 0, page);
            model.addAttribute("COURSE", result);
        } else {
            model.addAttribute("COURSE", null);
        }
        model.addAttribute("NUMCOURSE", Long.parseLong(count));
        model.addAttribute("COUNTCOURSE", countAll);
        return "courseList";
    }

    @GetMapping("/editCourse")
    public String editCourseByID(Model model, @RequestParam(name = "txtid") String id) {
        Course course = courseService.getCourseById(Long.parseLong(id));
        if (course.getCategory() == null) {
            course.setCategory(new Category(null, ""));
        }
        model.addAttribute("EDIT", course);
        List<Category> result = categoryService.getAllCategory();
        model.addAttribute("ALLCATE", result);
        model.addAttribute("ROLE", "course");
        return "editCourse";
    }

    @PostMapping("/editCourse")
    public String editCourse(@RequestParam(name = "image") MultipartFile multipartFile,
            @RequestParam(name = "txtid") String id,
            @RequestParam(name = "txtName") String name,
            @RequestParam(name = "txtTextArea") String description,
            @RequestParam(name = "txtPrice") String price,
            @RequestParam(name = "txtValueSearch") String cateName,
            @RequestParam(name = "url") String url, HttpServletRequest request,
            Model model
    ) {
        if (url.isEmpty()) {
            url = "redirect:/courseList";
        } else if (url.equals("add")) {
            url = "redirect:/addCourse";
        }
        Course course = courseService.getCourseById(Long.parseLong(id));
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if (!fileName.isEmpty()) {
            String uploadDir = "src/main/resources/static";
            uploadFile(uploadDir, "img/" + fileName, multipartFile);
            course.setCourseImg("img/" + fileName);
        }

        course.setCourseDes(description);
        course.setCourseName(name);
        if (!price.isEmpty()) {
            course.setPrice(Float.parseFloat(price));
        } else {
            course.setPrice(0);
        }
        course.setStatus(1);
        if (!cateName.isEmpty()) {
            if (cateName.contains("(+ add category)")) {
                Category category = categoryService.addCategory(cateName);
                course.setCategory(category);
            } else {
                String idCate = cateName.split(". ")[0];
                Category cate = categoryService.getCateById(Long.parseLong(idCate));
                course.setCategory(cate);
            }

        }
        User user = (User) model.getAttribute("USER");

        course.setUser(user);
        courseService.addCourse(course);
        String urlNext = request.getParameter("txtValuePage");
        if (urlNext == null) {
            return url;
        } else {
            return "redirect:/editUsers?txtid=" + course.getCourseID();
        }
    }

    @GetMapping("/deleteCoure")
    public String deleteCourse(@RequestParam(name = "txtid") String id) {
        courseService.deleteACourse(Long.parseLong(id));
        return "redirect:/courseList";
    }

    @GetMapping("/cloneCourse")
    public String cloneCourse(@RequestParam(name = "txtid") String id) {
        Course course = courseService.getCourseById(Long.parseLong(id));

        Course courseClone = new Course();
        courseClone.setCourseDes(course.getCourseDes());
        courseClone.setCourseImg(course.getCourseImg());
        courseClone.setCourseName(course.getCourseName() + " (clone)");
        courseClone.setDateOfCreate(LocalDateTime.now());
        courseClone.setPrice(course.getPrice());
        courseClone.setCourseImg(course.getCourseImg());
        courseClone.setStatus(course.getStatus());
        courseClone.setCategory(course.getCategory());
        courseClone.setUser(course.getUser());
        courseService.addCourse(courseClone);
        return "redirect:/courseList";
    }

    private void uploadFile(String uploadDir, String filename, MultipartFile multipartFile) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            InputStream inputStream = multipartFile.getInputStream();
            Path filePath = uploadPath.resolve(filename);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
        }
    }

    @GetMapping("/active")
    public String actvieCourse(@RequestParam(name = "txtArray") String[] array, HttpServletRequest request) {
        for (int i = 0; i < array.length; i++) {
            Course course = courseService.getCourseById(Long.parseLong(array[i]));
            if (course.getStatus() == 0) {
                course.setStatus(1);
                courseService.addCourse(course);
            }
        }
        return "redirect:/courseList?txtCount=" + request.getParameter("txtCount");
    }

    @GetMapping("/deActive")
    public String deActiveCourse(@RequestParam(name = "txtArray") String[] array, HttpServletRequest request) {
        for (int i = 0; i < array.length; i++) {
            Course course = courseService.getCourseById(Long.parseLong(array[i]));
            if (course.getStatus() == 1) {
                course.setStatus(0);
                courseService.addCourse(course);
            }
        }
        return "redirect:/courseList?txtCount=" + request.getParameter("txtCount");
    }

    @GetMapping("/deleteCourses")
    public String deleteCourses(@RequestParam(name = "txtArray") String[] array, HttpServletRequest request) {
        for (int i = 0; i < array.length; i++) {
            courseService.deleteACourse(Long.parseLong(array[i]));
        }
        return "redirect:/courseList?txtCount=" + request.getParameter("txtCount");
    }

    @GetMapping("/addQuestionByImport")
    public String addQuestionByImport(@RequestParam(name = "txtArrayAns") String[] arrayAns,
            @RequestParam(name = "txtArrayQues") String[] arrayQuest,
            @RequestParam(name = "txtArrayCorrect") String[] arrayCorrect,
            @RequestParam(name = "txtCourseID") Long courseid) {

        for (int i = 0; i < arrayAns.length; i++) {
            if (checkDuplicateQuestion(arrayAns[i])) {
                Question questionAdd = new Question();
                Course course = courseService.getCourseById(courseid);
                questionAdd.setCourse(course);
                questionAdd.setDateOfCreate(LocalDateTime.now());
                questionAdd.setQuestionName(arrayAns[i]);
                questionService.saveQuestion(questionAdd);
                int j = 1;
                while (j <= 4) {
                    Answer answer = new Answer();
                    answer.setAnswerContent(arrayQuest[(i * 4) - 1 + j]);
                    answer.setQuestion(questionAdd);
                    if (arrayCorrect[i].split(": ")[1].equalsIgnoreCase(arrayQuest[(i * 4) - 1 + j])) {
                        answer.setTrue(true);
                    } else {
                        answer.setTrue(false);
                    }
                    answerService.saveAnswer(answer);
                    j++;
                }
            }
        }
        return "redirect:/add-quiz-form?txtCourseID=" + courseid + "&txtContentID=0&txtMaxIndex=0&txtShowQuestion=Show questions from this course";
    }

    private boolean checkDuplicateQuestion(String name) {
        List<Question> questionList = courseService.getListAllQuestion();
        if (questionList.size() > 0) {
            for (Question question : questionList) {
                if (question.getQuestionName().equals(name)) {
                    return false;
                }
            }
        }
        return true;
    }
}
