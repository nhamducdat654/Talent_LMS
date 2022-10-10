package com.fsoft.team.serviceImp;

import com.fsoft.team.dtos.CategoryDTO;
import com.fsoft.team.dtos.SectionDTO;
import com.fsoft.team.entity.Answer;
import com.fsoft.team.entity.Category;
import com.fsoft.team.entity.Content;
import com.fsoft.team.entity.Course;
import com.fsoft.team.entity.Enrollement;
import com.fsoft.team.entity.Question;
import com.fsoft.team.entity.Quiz;
import com.fsoft.team.entity.Section;
import com.fsoft.team.repository.AnswerRepository;
import com.fsoft.team.repository.CategoryRepository;
import com.fsoft.team.repository.ContentRepository;
import com.fsoft.team.repository.CourseRepository;
import com.fsoft.team.repository.EnrollementRepository;
import com.fsoft.team.repository.QuestionRepository;
import com.fsoft.team.repository.QuizRepository;
import com.fsoft.team.repository.SectionRepository;
import com.fsoft.team.service.CourseService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImp implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private EnrollementRepository enrollementRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Override
    public List<Category> getCategoryOfMyCourse(String username) {
        List<Course> myCourses = getMyCourse(username);
        List<Category> myCategory = new ArrayList<>();
        for (int i = 0; i < myCourses.size(); i++) {
            boolean flag = false;
            for (int j = 0; j < myCategory.size(); j++) {
                if (myCategory.get(j).getCategoryID() == myCourses.get(i).getCategory().getCategoryID()) {
                    flag = true;
                }
            }
            if (!flag) {
                myCategory.add(myCourses.get(i).getCategory());
            }
        }

        return myCategory;
    }

    @Override
    public List<Course> getMyCourse(String username) {

        List<Course> enrollCourse = courseRepository.getEnrollCourse(username);
        for (int i = 0; i < enrollCourse.size(); i++) {
            try {
                enrollCourse.get(i).getCategory().getCategoryID();
            } catch (NullPointerException ex) {
                enrollCourse.get(i).setCategory(new Category(null, "General"));
            }
        }

        return enrollCourse;
    }

    @Override
    public List<Course> getCourseCatalog() {
        List<Course> courseCatalog = courseRepository.getCourseCatalog();
        for (int i = 0; i < courseCatalog.size(); i++) {
            try {
                courseCatalog.get(i).getCategory().getCategoryID();
            } catch (NullPointerException ex) {
                courseCatalog.get(i).setCategory(new Category(null, "General"));
            }
        }
        return courseCatalog;
    }

    @Override
    public List<CategoryDTO> getCategoryCatalog() {
        List<CategoryDTO> cateDTO = courseRepository.getCatgoryHasCourse();

        long count = 0;
        List<Course> courseCatalog = getCourseCatalog();
        for (int i = 0; i < courseCatalog.size(); i++) {
            if (courseCatalog.get(i).getCategory().getCategoryID() == null) {
                count++;
            }
        }

        for (int i = 0; i < cateDTO.size(); i++) {
            if (cateDTO.get(i).getCategoryID() == null) {
                cateDTO.get(i).setCountCategory(count);
                cateDTO.get(i).setCategoryName("General");
            } else {
                cateDTO.get(i).setCategoryName(categoryRepository.getById(cateDTO.get(i).getCategoryID()).getCategoryName());
            }
        }
        return cateDTO;
    }

    @Override
    public Course getCourseDetail(Long courseID) {
        Course courseDetail = courseRepository.getById(courseID);
        try {
            courseDetail.getCategory().getCategoryID();
        } catch (NullPointerException ex) {
            courseDetail.setCategory(new Category(null, "General"));
        }
        return courseDetail;
    }

    @Override
    public List<Section> getSection(Long courseID) {
        return courseRepository.getSection(courseID);
    }

    @Override
    public List<Content> getContent(Long courseID) {
        List<Content> listContent = courseRepository.getContent(courseID);
        for (int i = 0; i < listContent.size(); i++) {
            try {
                listContent.get(i).getSection().getSectionID();
            } catch (NullPointerException ex) {
                listContent.get(i).setSection(new Section());
            }
        }

        return listContent;
    }

    @Override
    public List<Quiz> getListQuiz(Long courseID) {
        List<Quiz> listQuiz = courseRepository.getListQuiz(courseID);
        for (int i = 0; i < listQuiz.size(); i++) {
            try {
                listQuiz.get(i).getSection().getSectionID();
            } catch (NullPointerException ex) {
                listQuiz.get(i).setSection(new Section());
            }
        }

        return listQuiz;
    }

    @Override
    public List<SectionDTO> getCountSection(Long courseID) {
        List<SectionDTO> sectionContent = courseRepository.getCountContent(courseID);
        long countContent = 0;
        List<Content> content = getContent(courseID);
        for (int i = 0; i < content.size(); i++) {
            if (content.get(i).getSection().getSectionID() == null) {
                countContent++;
            }
        }

        for (int i = 0; i < sectionContent.size(); i++) {
            if (sectionContent.get(i).getSectionID() == null) {
                sectionContent.get(i).setCountSection(countContent);
            }
        }

        List<SectionDTO> sectionQuiz = courseRepository.getCountQuiz(courseID);
        long countQuiz = 0;
        List<Quiz> quiz = getListQuiz(courseID);
        for (int i = 0; i < quiz.size(); i++) {
            if (quiz.get(i).getSection().getSectionID() == null) {
                countQuiz++;
            }
        }

        for (int i = 0; i < sectionQuiz.size(); i++) {
            if (sectionQuiz.get(i).getSectionID() == null) {
                sectionQuiz.get(i).setCountSection(countQuiz);
            }
        }
        int size = sectionContent.size();
        for (int i = 0; i < sectionQuiz.size(); i++) {
            boolean flag = true;
            for (int j = 0; j < size; j++) {
                if (sectionQuiz.get(i).getSectionID() == sectionContent.get(j).getSectionID()) {
                    sectionContent.get(j).setCountSection(sectionQuiz.get(i).getCountSection() + sectionContent.get(j).getCountSection());
                    flag = false;
                }
            }
            if (flag) {
                sectionContent.add(sectionQuiz.get(i));
            }
        }
        return sectionContent;
    }

    @Override
    public Content saveOrUpdateContent(Content content) {
        return contentRepository.save(content);
    }

    @Override
    public void enrollThisCourse(Enrollement enrollement) {
        enrollementRepository.save(enrollement);
    }

    @Override
    public List<Question> getListQuestionOfCourse(Long courseID) {
        return questionRepository.getListQuestionOfCourse(courseID);
    }

    @Override
    public List<Question> getListAllQuestion() {
        return questionRepository.getListAllQuestion();
    }

    @Override
    public Quiz addQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Override
    public void addQuestionOfQuiz(Long[] questionID, Quiz quiz) {
        for (Long id : questionID) {

            Question oldQuestion = questionRepository.getById(id);
            Question newQuestion = new Question();
            newQuestion.setQuestionName(oldQuestion.getQuestionName());
            newQuestion.setQuiz(quiz);
            newQuestion.setCourse(oldQuestion.getCourse());
            Question question = questionRepository.save(newQuestion);

            List<Answer> listAnswer = answerRepository.getListAnswerOfQuestion(id);
            for (Answer answer : listAnswer) {
                Answer newAnswer = new Answer();
                newAnswer.setAnswerContent(answer.getAnswerContent());
                newAnswer.setTrue(answer.isTrue());
                newAnswer.setQuestion(question);
                answerRepository.save(newAnswer);
            }
        }
    }

    @Override
    public void updateQuestionOfQuiz(Long[] questionID, Quiz quiz) {
        List<Question> listQuestionOfQuiz = questionRepository.getListQuestionOfQuiz(quiz.getQuizID());

        List<Long> qID = new ArrayList<>();
        for (Long id : questionID) {
            boolean flag = true;
            for (Question questionOfQuiz : listQuestionOfQuiz) {
                if (id == questionOfQuiz.getQuestionID()) {
                    flag = false;
                }
            }
            if (flag) {
                qID.add(id);
            }
        }
        Long[] questID = qID.toArray(new Long[0]);
        addQuestionOfQuiz(questID, quiz);

        for (Question question : listQuestionOfQuiz) {
            boolean flag = true;
            for (Long long1 : questionID) {
                if (long1 == question.getQuestionID()) {
                    flag = false;
                }
            }
            if (flag) {
                questionRepository.deleteById(question.getQuestionID());
            }
        }
    }

    @Override
    public Long getIndexSection(Long courseID) {
        return courseRepository.getIndexSection(courseID);
    }

    @Override
    public void saveOrUpdateSection(Section section) {
        sectionRepository.save(section);
    }

    @Override
    public Content getContentDetail(Long contentID) {
        Content content = contentRepository.getById(contentID);
        try {
            content.getSection().getSectionID();
        } catch (NullPointerException ex) {
            content.setSection(new Section());
        }
        return content;
    }

    @Override
    public void deleteContentOrQuiz(Long contentID, String contentOrQuiz) {
        if (contentOrQuiz.equals("content")) {
            contentRepository.deleteById(contentID);
        } else {
            quizRepository.deleteById(contentID);
        }
    }

    @Override
    public void deleteSection(Long sectionID) {
        sectionRepository.deleteById(sectionID);
    }

    @Override
    public void updateListSectionAfterDelete(long sectionID, long courseID) {
        Section section = sectionRepository.getById(sectionID);

        List<Section> listSection = sectionRepository.getListSectionDelete(section.getSectionIndex(), courseID);
        for (Section section1 : listSection) {
            section1.setSectionIndex(section1.getSectionIndex() - 1);
        }
        sectionRepository.saveAll(listSection);
    }

    @Override
    public void updateListSectionAfterUpdate(long sectionID, long courseID, int newIndex) {
        Section section = sectionRepository.getById(sectionID);

        if (newIndex > section.getSectionIndex()) {
            List<Section> listSection = sectionRepository.getListSectionUpdateInc(section.getSectionIndex(), newIndex, courseID);
            for (Section section1 : listSection) {
                section1.setSectionIndex(section1.getSectionIndex() - 1);
            }
            sectionRepository.saveAll(listSection);
        }
        if (newIndex < section.getSectionIndex()) {
            List<Section> listSection = sectionRepository.getListSectionUpdateDes(section.getSectionIndex(), newIndex, courseID);
            for (Section section1 : listSection) {
                section1.setSectionIndex(section1.getSectionIndex() + 1);
            }
            sectionRepository.saveAll(listSection);
        }
    }

    @Override
    public void updateListContentAfterDelete(long contentID, long sectionID, long courseID, String contentOrQuiz) {
        int index;
        if (contentOrQuiz.equals("content")) {
            Content content = contentRepository.getById(contentID);
            index = content.getContentIndex();
        } else {
            Quiz quiz = quizRepository.getById(contentID);
            index = quiz.getQuizIndex();
        }
        if (sectionID > 0) {
            List<Content> listContent = contentRepository.getListContentDelete(index, sectionID, courseID);
            for (Content content : listContent) {
                content.setContentIndex(content.getContentIndex() - 1);
            }
            contentRepository.saveAll(listContent);
            List<Quiz> listQuiz = quizRepository.getListQuizDelete(index, sectionID, courseID);
            for (Quiz quiz : listQuiz) {
                quiz.setQuizIndex(quiz.getQuizIndex() - 1);
            }
            quizRepository.saveAll(listQuiz);
        } else {
            List<Content> listContent = contentRepository.getListContentDeleteNull(index, courseID);
            for (Content content : listContent) {
                content.setContentIndex(content.getContentIndex() - 1);
            }
            contentRepository.saveAll(listContent);
            List<Quiz> listQuiz = quizRepository.getListQuizDeleteNull(index, courseID);
            for (Quiz quiz : listQuiz) {
                quiz.setQuizIndex(quiz.getQuizIndex() - 1);
            }
            quizRepository.saveAll(listQuiz);
        }
    }

    @Override
    public void updateListContentAfterUpdate(long contentID, long sectionID, long courseID, int newIndex, String contentOrQuiz) {

        long id = 0;
        int oldIndex;

        if (contentOrQuiz.equals("content")) {
            Content content = contentRepository.getById(contentID);
            oldIndex = content.getContentIndex();
            try {
                id = content.getSection().getSectionID();
            } catch (Exception e) {
            }
        } else {
            Quiz quiz = quizRepository.getById(contentID);
            oldIndex = quiz.getQuizIndex();
            try {
                id = quiz.getSection().getSectionID();
            } catch (Exception e) {
            }
        }

        if (id == sectionID) {
            if (newIndex > oldIndex) {
                if (sectionID > 0) {
                    List<Content> listContent = contentRepository.getListContentUpdateInc(oldIndex, newIndex, sectionID, courseID);
                    for (Content content1 : listContent) {
                        content1.setContentIndex(content1.getContentIndex() - 1);
                    }
                    contentRepository.saveAll(listContent);

                    List<Quiz> listQuiz = quizRepository.getListQuizUpdateInc(oldIndex, newIndex, sectionID, courseID);
                    for (Quiz quiz : listQuiz) {
                        quiz.setQuizIndex(quiz.getQuizIndex() - 1);
                    }
                    quizRepository.saveAll(listQuiz);
                } else {
                    List<Content> listContent = contentRepository.getListContentUpdateIncNull(oldIndex, newIndex, courseID);
                    for (Content content1 : listContent) {
                        content1.setContentIndex(content1.getContentIndex() - 1);
                    }
                    contentRepository.saveAll(listContent);

                    List<Quiz> listQuiz = quizRepository.getListQuizUpdateIncNull(oldIndex, newIndex, courseID);

                    for (Quiz quiz : listQuiz) {
                        quiz.setQuizIndex(quiz.getQuizIndex() - 1);
                    }
                    quizRepository.saveAll(listQuiz);

                }
            }
            if (newIndex < oldIndex) {
                if (sectionID > 0) {
                    List<Content> listContent = contentRepository.getListContentUpdateDes(oldIndex, newIndex, sectionID, courseID);
                    for (Content content1 : listContent) {
                        content1.setContentIndex(content1.getContentIndex() + 1);
                    }
                    contentRepository.saveAll(listContent);

                    List<Quiz> listQuiz = quizRepository.getListQuizUpdateDes(oldIndex, newIndex, sectionID, courseID);
                    for (Quiz quiz : listQuiz) {
                        quiz.setQuizIndex(quiz.getQuizIndex() + 1);
                    }
                    quizRepository.saveAll(listQuiz);
                } else {
                    List<Content> listContent = contentRepository.getListContentUpdateDesNull(oldIndex, newIndex, courseID);
                    for (Content content1 : listContent) {
                        content1.setContentIndex(content1.getContentIndex() + 1);
                    }
                    contentRepository.saveAll(listContent);

                    List<Quiz> listQuiz = quizRepository.getListQuizUpdateDesNull(oldIndex, newIndex, courseID);
                    for (Quiz quiz : listQuiz) {
                        quiz.setQuizIndex(quiz.getQuizIndex() + 1);
                    }
                    quizRepository.saveAll(listQuiz);
                }
            }
        } else {
            if (id > 0) {
                List<Content> listContent = contentRepository.getListContentDelete(oldIndex, id, courseID);
                for (Content content1 : listContent) {
                    content1.setContentIndex(content1.getContentIndex() - 1);
                }
                contentRepository.saveAll(listContent);

                List<Quiz> listQuiz = quizRepository.getListQuizDelete(oldIndex, id, courseID);
                for (Quiz quiz : listQuiz) {
                    quiz.setQuizIndex(quiz.getQuizIndex() - 1);
                }
                quizRepository.saveAll(listQuiz);

                if (sectionID > 0) {
                    List<Content> listContent2 = contentRepository.getListContentUpdateIncOrther(newIndex, sectionID, courseID);
                    for (Content content1 : listContent2) {
                        content1.setContentIndex(content1.getContentIndex() + 1);
                    }
                    contentRepository.saveAll(listContent2);

                    List<Quiz> listQuiz1 = quizRepository.getListQuizUpdateIncOrther(newIndex, sectionID, courseID);
                    for (Quiz quiz1 : listQuiz1) {
                        quiz1.setQuizIndex(quiz1.getQuizIndex() + 1);
                    }
                    quizRepository.saveAll(listQuiz1);
                } else {
                    List<Content> listContent2 = contentRepository.getListContentUpdateIncOrtherNull(newIndex, courseID);
                    for (Content content1 : listContent2) {
                        content1.setContentIndex(content1.getContentIndex() + 1);
                    }
                    contentRepository.saveAll(listContent2);

                    List<Quiz> listQuiz1 = quizRepository.getListQuizUpdateIncOrtherNull(newIndex, courseID);
                    for (Quiz quiz1 : listQuiz1) {
                        quiz1.setQuizIndex(quiz1.getQuizIndex() + 1);
                    }
                    quizRepository.saveAll(listQuiz1);
                }
            } else {
                List<Content> listContent = contentRepository.getListContentDeleteNull(oldIndex, courseID);
                for (Content content1 : listContent) {
                    content1.setContentIndex(content1.getContentIndex() - 1);
                }
                contentRepository.saveAll(listContent);

                List<Quiz> listQuiz = quizRepository.getListQuizDeleteNull(oldIndex, courseID);
                for (Quiz quiz : listQuiz) {
                    quiz.setQuizIndex(quiz.getQuizIndex() - 1);
                }
                quizRepository.saveAll(listQuiz);

                List<Content> listContent2 = contentRepository.getListContentUpdateIncOrther(newIndex, sectionID, courseID);
                for (Content content1 : listContent2) {
                    content1.setContentIndex(content1.getContentIndex() + 1);
                }
                contentRepository.saveAll(listContent2);

                List<Quiz> listQuiz1 = quizRepository.getListQuizUpdateIncOrther(newIndex, sectionID, courseID);
                for (Quiz quiz1 : listQuiz1) {
                    quiz1.setQuizIndex(quiz1.getQuizIndex() + 1);
                }
                quizRepository.saveAll(listQuiz1);
            }
        }
    }

    @Override
    public List<Question> getListQuestionOfQuiz(long quizID) {
        return questionRepository.getListQuestionOfQuiz(quizID);
    }

    @Override
    public Quiz getQuiz(long quizID) {
        Quiz quiz = quizRepository.getById(quizID);
        try {
            quiz.getSection().getSectionID();
        } catch (Exception e) {
            quiz.setSection(null);
        }

        return quiz;
    }

    @Override
    public Course addCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getCourseByUsername(String username, int page, int size) {
        List<Course> result = courseRepository.findByUsername(username, PageRequest.of(page, size));
        for (Course c : result) {
            try {
                c.getCategory().getCategoryID();
            } catch (NullPointerException e) {
                c.setCategory(new Category(null, "General"));
                System.out.println(e.getMessage());
            }

        }
        return result;
    }

    @Override
    public List<Course> getAllCourseByUsername(String username) {
        List<Course> result = courseRepository.getAllCourseByUsername(username);
        for (Course c : result) {
            try {
                c.getCategory().getCategoryID();
            } catch (NullPointerException e) {
                c.setCategory(new Category(null, "General"));
                System.out.println(e.getMessage());
            }

        }
        return result;
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).get();
    }

    @Override
    public void deleteACourse(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    public Long countCourseByUserName(String username) {
        return courseRepository.countCourseByUserName(username);
    }

    @Override
    public List<Course> findAllCourse() {
        List<Course> allCourse = courseRepository.findAll();
        for (Course course : allCourse) {
            try {
                course.getCategory().getCategoryID();
            } catch(Exception e) {
                course.setCategory(new Category(null, "General"));
            }
        }
        return allCourse;
    }

    @Override
    public Optional<Course> findCourseByID(Long id) {
        return courseRepository.findById(id);
    }
}
