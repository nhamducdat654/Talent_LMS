package com.fsoft.team.repository;

import com.fsoft.team.dtos.CategoryDTO;
import com.fsoft.team.dtos.SectionDTO;
import com.fsoft.team.entity.Content;
import com.fsoft.team.entity.Course;
import com.fsoft.team.entity.Question;
import com.fsoft.team.entity.Quiz;
import com.fsoft.team.entity.Section;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c from Course c where c.user.username = ?1")
    List<Course> findByUsername(String username, Pageable pageable);

    @Query("SELECT c from Course c where c.user.username = ?1")
    List<Course> getAllCourseByUsername(String username);

    @Query("SELECT c FROM Course c WHERE c.user.username = ?1")
    public List<Course> getOwnerCourse(String username);

    @Query("SELECT c from Course c JOIN Enrollement e ON c.courseID = e.course.courseID WHERE e.user.username = ?1 AND (c.status = 1 OR e.user.role.roleID != 'Learner')")
    public List<Course> getEnrollCourse(String username);

    @Query("SELECT c from Course c WHERE c.status = 1")
    public List<Course> getCourseCatalog();

    @Query("SELECT new com.fsoft.team.dtos.CategoryDTO(c.category.categoryID, '', COUNT(c.category.categoryID)) FROM Course c WHERE c.status = 1 GROUP BY c.category.categoryID")
    public List<CategoryDTO> getCatgoryHasCourse();

    @Query("SELECT s FROM Section s WHERE s.course.courseID = ?1 ORDER BY s.sectionIndex ASC")
    public List<Section> getSection(Long courseID);

    @Query("SELECT ct FROM Content ct WHERE ct.course.courseID = ?1 ORDER BY ct.contentIndex ASC")
    public List<Content> getContent(Long courseID);

    @Query("SELECT q FROM Quiz q WHERE q.course.courseID = ?1 ORDER BY q.quizIndex ASC")
    public List<Quiz> getListQuiz(Long courseID);

    @Query("SELECT new com.fsoft.team.dtos.SectionDTO(co.section.sectionID, COUNT(co.section.sectionID)) FROM Content co WHERE co.course.courseID = ?1 GROUP BY co.section.sectionID")
    public List<SectionDTO> getCountContent(Long courseID);

    @Query("SELECT new com.fsoft.team.dtos.SectionDTO(q.section.sectionID, COUNT(q.section.sectionID)) FROM Quiz q WHERE q.course.courseID = ?1 GROUP BY q.section.sectionID")
    public List<SectionDTO> getCountQuiz(Long courseID);

    @Query("SELECT qe FROM Question qe WHERE qe.course.courseID = ?1 AND qe.quiz.quizID = null")
    public List<Question> getListQuestionOfCourse(Long courseID);

    @Query("SELECT COUNT(s) FROM Section s WHERE s.course.courseID = ?1")
    public Long getIndexSection(Long courseID);

    @Query("SELECT COUNT(c) FROM Course c WHERE c.user.username = ?1")
    public Long countCourseByUserName(String username);

    public Course findCourseEntityByCourseID(Long courseID);
}
