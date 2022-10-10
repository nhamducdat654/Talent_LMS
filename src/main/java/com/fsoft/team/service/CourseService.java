package com.fsoft.team.service;

import com.fsoft.team.dtos.CategoryDTO;
import com.fsoft.team.dtos.SectionDTO;
import com.fsoft.team.entity.Category;
import com.fsoft.team.entity.Content;
import com.fsoft.team.entity.Course;
import com.fsoft.team.entity.Enrollement;
import com.fsoft.team.entity.Question;
import com.fsoft.team.entity.Quiz;
import com.fsoft.team.entity.Section;
import java.util.List;
import java.util.Optional;

public interface CourseService {

    public Course addCourse(Course course);

    public List<Course> getCourseByUsername(String username, int page, int size);

    public Course getCourseById(Long id);

    public void deleteACourse(Long id);
    
    List<Course> getAllCourseByUsername(String username);

    List<Category> getCategoryOfMyCourse(String username);

    List<Course> getMyCourse(String username);

    List<Course> getCourseCatalog();

    List<CategoryDTO> getCategoryCatalog();

    Course getCourseDetail(Long courseID);

    List<Section> getSection(Long courseID);

    List<Content> getContent(Long courseID);

    List<Quiz> getListQuiz(Long courseID);

    List<SectionDTO> getCountSection(Long courseID);

    Content saveOrUpdateContent(Content content);

    void enrollThisCourse(Enrollement enrollement);

    List<Question> getListQuestionOfCourse(Long courseID);
    List<Question> getListAllQuestion();
    
    Quiz addQuiz(Quiz quiz);

    void addQuestionOfQuiz(Long[] questionID, Quiz quiz);

    void updateQuestionOfQuiz(Long[] questionID, Quiz quiz);

    Long getIndexSection(Long courseID);

    void saveOrUpdateSection(Section section);

    Content getContentDetail(Long contentID);

    void deleteContentOrQuiz(Long contentID, String contentOrQuiz);

    void deleteSection(Long sectionID);

    void updateListSectionAfterDelete(long sectionID, long courseID);

    void updateListSectionAfterUpdate(long sectionID, long courseID, int newIndex);

    void updateListContentAfterDelete(long contentID, long sectionID, long courseID, String contentOrQuiz);

    void updateListContentAfterUpdate(long contentID, long sectionID, long courseID, int newIndex, String contentOrQuiz);

    List<Question> getListQuestionOfQuiz(long quizID);

    Quiz getQuiz(long quizID);
    
    Long countCourseByUserName(String username);

    List<Course> findAllCourse();
    Optional<Course> findCourseByID(Long id);
}
