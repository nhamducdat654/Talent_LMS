package com.fsoft.team.controllers;

import com.fsoft.team.dtos.SectionDTO;
import com.fsoft.team.entity.*;
import com.fsoft.team.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"USER"})
public class ContentController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private ContentUserService contentUserService;

    @Autowired
    private ProgressService progressService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private EnrollementService enrollementService;

    @Autowired
    private QuizUserService quizUserService;

    @GetMapping("/doContent")
    public String showContent(Model model, @RequestParam(name = "contentid") long id, @RequestParam("txtMaxIndex") int maxIndex) {

        Content showContent = contentService.showContent(id);
        model.addAttribute("DOCONTENT", showContent);

        User user = (User) model.getAttribute("USER");
        List<Course> myCourse = courseService.getMyCourse(user.getUsername());

        model.addAttribute("PROGRESS", getProgress(user, myCourse, showContent.getCourse().getCourseID()));

        model.addAttribute("USER", user);
        List<Section> listSection = courseService.getSection(showContent.getCourse().getCourseID());
        model.addAttribute("COURSE_SECTION", listSection);

        List<Content> listContent = courseService.getContent(showContent.getCourse().getCourseID());
        model.addAttribute("LIST_CONTENT", listContent);

        List<Quiz> listQuiz = courseService.getListQuiz(showContent.getCourse().getCourseID());
        model.addAttribute("LIST_QUIZ", listQuiz);

        List<SectionDTO> count = courseService.getCountSection(showContent.getCourse().getCourseID());
        model.addAttribute("COUNT", count);

        model.addAttribute("MAX_INDEX", maxIndex);

        List<Progress> listProgress = progressService.getListProgressByUser(user.getUsername());
        model.addAttribute("LIST_PROGRESS", listProgress);

        for (Progress progress : listProgress) {
            try {
                if (progress.getContentuser().getContent().getContentID() == id) {
                    model.addAttribute("COMPLETE_CONTENT", progress);
                }
            } catch (Exception e) {

            }
        }

        Enrollement en = enrollementService.getEnrollementByUserNameAndCourseID(user.getUsername(), showContent.getCourse().getCourseID());
        model.addAttribute("ENROLLEMENT", en);

        return "doContent";
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

    @GetMapping("/complete-content")
    public String compeleContent(Model model, @RequestParam(name = "contentid") Long contentid, @RequestParam("txtNextIndex") int nextIndex,
            @RequestParam("txtSectionID") String sectionID, @RequestParam("txtCourseID") long courseID) {

        ContentUser contentUser = new ContentUser();

        User user = (User) model.getAttribute("USER");

        Content content = new Content();
        content.setContentID(contentid);

        contentUser.setDateComplete(LocalDateTime.now());
        contentUser.setContent(content);
        contentUser.setComplete(true);
        contentUser.setUser(user);

        if (contentUserService.checkExistContentUser(contentid, user.getUsername())) {

            ContentUser newContentUser = contentUserService.saveContentUser(contentUser);
            Progress progress = new Progress();
            progress.setCompleteDate(newContentUser.getDateComplete());
            progress.setContentuser(newContentUser);
            progress.setUser(user);
            progressService.saveProgress(progress);

        }

        Content anotherContent = contentService.showContent(contentid);

        List<Course> myCourse = courseService.getMyCourse(user.getUsername());
        return "redirect:/next-content?txtCourseID=" + courseID + "&txtSectionID=" + sectionID + "&txtNextIndex=" + nextIndex + "&txtProgress=" + getProgress(user, myCourse, anotherContent.getCourse().getCourseID());
    }

    @GetMapping("/next-content")
    public String nextContent(@RequestParam("txtCourseID") long courseID, @RequestParam("txtSectionID") String sectionid, @RequestParam("txtNextIndex") int nextIndex,
            @RequestParam("txtProgress") int prog, Model model) {
        User user = (User) model.getAttribute("USER");
        if (prog == 100) {
            Enrollement en = enrollementService.getEnrollementByUserNameAndCourseID(user.getUsername(), courseID);
            if (!en.isComplete()) {
                en.setComplete(true);
                en.setDateComplete(LocalDateTime.now());
                enrollementService.addNewEnroll(en);
            }
        }
        Long sectionID = null;

        if (!sectionid.equals("null")) {
            sectionID = Long.parseLong(sectionid);
        }
        List<SectionDTO> count = courseService.getCountSection(courseID);

        List<Content> listContent = courseService.getContent(courseID);
        for (Content content : listContent) {
            if (content.getSection().getSectionID() == sectionID && content.getContentIndex() == nextIndex) {

                long max = 1;
                for (SectionDTO sectionDTO : count) {
                    if (sectionDTO.getSectionID() == sectionID) {
                        max = sectionDTO.getCountSection();
                    }
                }
                return "redirect:/doContent?contentid=" + content.getContentID() + "&txtMaxIndex=" + max;
            }
        }

        List<Quiz> listQuiz = courseService.getListQuiz(courseID);
        for (Quiz quiz : listQuiz) {
            if (quiz.getSection().getSectionID() == sectionID && quiz.getQuizIndex() == nextIndex) {
                long max = 1;
                for (SectionDTO sectionDTO : count) {
                    if (sectionDTO.getSectionID() == sectionID) {
                        max = sectionDTO.getCountSection();
                    }
                }

                return "redirect:/doQuiz?quizid=" + quiz.getQuizID() + "&txtMaxIndex=" + max;
            }
        }

        List<Section> listSection = courseService.getSection(courseID);
        int sectionIndex = 1;
        for (Section section : listSection) {
            if (section.getSectionID() == sectionID) {
                sectionIndex = section.getSectionIndex() + 1;
            }
        }

        for (Section section : listSection) {
            if (sectionIndex == section.getSectionIndex()) {
                for (Content content : listContent) {
                    if (content.getSection().getSectionID() == section.getSectionID() && content.getContentIndex() == 1) {
                        long max = 1;
                        for (SectionDTO sectionDTO : count) {
                            if (sectionDTO.getSectionID() == sectionID) {
                                max = sectionDTO.getCountSection();
                            }
                        }

                        return "redirect:/doContent?contentid=" + content.getContentID() + "&txtMaxIndex=" + max;
                    }
                }

                for (Quiz quiz : listQuiz) {
                    if (quiz.getSection().getSectionID() == section.getSectionID() && quiz.getQuizIndex() == 1) {
                        long max = 1;
                        for (SectionDTO sectionDTO : count) {
                            if (sectionDTO.getSectionID() == sectionID) {
                                max = sectionDTO.getCountSection();
                            }
                        }

                        return "redirect:/doQuiz?quizid=" + quiz.getQuizID() + "&txtMaxIndex=" + max;
                    }
                }
            }
        }

        return null;
    }

    @GetMapping("/previous-content")
    public String previousContent(@RequestParam("txtCourseID") long courseID, @RequestParam("txtSectionID") String sectionid, @RequestParam("txtPreIndex") int previousIndex) {

        Long sectionID = null;
        if (!sectionid.equals("null")) {
            sectionID = Long.parseLong(sectionid);
        }
        List<SectionDTO> count = courseService.getCountSection(courseID);
        List<Content> listContent = courseService.getContent(courseID);
        List<Quiz> listQuiz = courseService.getListQuiz(courseID);

        if (previousIndex > 0) {
            for (Content content : listContent) {
                if (content.getSection().getSectionID() == sectionID && content.getContentIndex() == previousIndex) {

                    long max = 1;
                    for (SectionDTO sectionDTO : count) {
                        if (sectionDTO.getSectionID() == sectionID) {
                            max = sectionDTO.getCountSection();
                        }
                    }
                    return "redirect:/doContent?contentid=" + content.getContentID() + "&txtMaxIndex=" + max;
                }
            }

            for (Quiz quiz : listQuiz) {
                if (quiz.getSection().getSectionID() == sectionID && quiz.getQuizIndex() == previousIndex) {
                    long max = 1;
                    for (SectionDTO sectionDTO : count) {
                        if (sectionDTO.getSectionID() == sectionID) {
                            max = sectionDTO.getCountSection();
                        }
                    }

                    return "redirect:/doQuiz?quizid=" + quiz.getQuizID() + "&txtMaxIndex=" + max;
                }
            }
        } else {
            List<Section> listSection = courseService.getSection(courseID);
            int sectionIndex = 0;
            for (Section section : listSection) {
                if (section.getSectionID() == sectionID) {
                    sectionIndex = section.getSectionIndex() - 1;
                }
            }

            if (sectionIndex > 0) {
                for (Section section : listSection) {
                    if (sectionIndex == section.getSectionIndex()) {
                        long max = 1;
                        for (SectionDTO sectionDTO : count) {
                            if (sectionDTO.getSectionID() == section.getSectionID()) {
                                max = sectionDTO.getCountSection();
                            }
                        }
                        for (Content content : listContent) {
                            if (content.getSection().getSectionID() == section.getSectionID() && content.getContentIndex() == max) {
                                return "redirect:/doContent?contentid=" + content.getContentID() + "&txtMaxIndex=" + max;
                            }
                        }

                        for (Quiz quiz : listQuiz) {
                            if (quiz.getSection().getSectionID() == section.getSectionID() && quiz.getQuizIndex() == max) {
                                return "redirect:/doQuiz?quizid=" + quiz.getQuizID() + "&txtMaxIndex=" + max;
                            }
                        }
                    }
                }
            } else {
                long max = 1;
                for (SectionDTO sectionDTO : count) {
                    if (sectionDTO.getSectionID() == null) {
                        max = sectionDTO.getCountSection();
                    }
                }
                for (Content content : listContent) {
                    if (content.getSection().getSectionID() == null && content.getContentIndex() == max) {
                        return "redirect:/doContent?contentid=" + content.getContentID() + "&txtMaxIndex=" + max;
                    }
                }

                for (Quiz quiz : listQuiz) {
                    if (quiz.getSection().getSectionID() == null && quiz.getQuizIndex() == max) {
                        return "redirect:/doQuiz?quizid=" + quiz.getQuizID() + "&txtMaxIndex=" + max;
                    }
                }
            }
        }
        return null;
    }

}
