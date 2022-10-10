package com.fsoft.team.repository;

import com.fsoft.team.entity.Content;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    @Query("SELECT co FROM Content co WHERE co.contentIndex > ?1 AND co.section.sectionID = ?2 AND co.course.courseID = ?3")
    public List<Content> getListContentDelete(int contentIndex, Long sectionID, Long courseID);

    @Query("SELECT co FROM Content co WHERE co.contentIndex > ?1 AND co.section.sectionID = null AND co.course.courseID = ?2")
    public List<Content> getListContentDeleteNull(int contentIndex, Long courseID);

    @Query("SELECT co FROM Content co WHERE co.contentIndex > ?1 AND co.contentIndex <= ?2 AND co.section.sectionID = ?3 AND co.course.courseID = ?4")
    public List<Content> getListContentUpdateInc(int contentIndex, int newIndex, Long sectionID, Long courseID);

    @Query("SELECT co FROM Content co WHERE co.contentIndex > ?1 AND co.contentIndex <= ?2 AND co.section.sectionID = null AND co.course.courseID = ?3")
    public List<Content> getListContentUpdateIncNull(int contentIndex, int newIndex, Long courseID);

    @Query("SELECT co FROM Content co WHERE co.contentIndex < ?1 AND co.contentIndex >= ?2 AND co.section.sectionID = ?3 AND co.course.courseID = ?4")
    public List<Content> getListContentUpdateDes(int contentIndex, int newIndex, Long sectionID, Long courseID);

    @Query("SELECT co FROM Content co WHERE co.contentIndex < ?1 AND co.contentIndex >= ?2 AND co.section.sectionID = null AND co.course.courseID = ?3")
    public List<Content> getListContentUpdateDesNull(int contentIndex, int newIndex, Long courseID);

    @Query("SELECT co FROM Content co WHERE co.contentIndex >= ?1 AND co.section.sectionID = ?2 AND co.course.courseID = ?3")
    public List<Content> getListContentUpdateIncOrther(int newIndex, Long sectionID, Long courseID);

    @Query("SELECT co FROM Content co WHERE co.contentIndex >= ?1 AND co.section.sectionID = null AND co.course.courseID = ?2")
    public List<Content> getListContentUpdateIncOrtherNull(int newIndex, Long courseID);

    @Query("SELECT c FROM Content c  WHERE c.contentID = ?1")
    Content showContent(Long contentID);

    @Query("select count(c) from  Content  c where c.course.courseID = ?1")
    public Long countContentByCourseID(Long id);

    @Query("select c.contentID from  Content  c where c.course.courseID = ?1")
    public List<Long> getContentIDByCourseID(Long id);
}
