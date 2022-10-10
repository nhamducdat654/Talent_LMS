package com.fsoft.team.repository;

import com.fsoft.team.entity.Section;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

    @Query("SELECT s FROM Section s WHERE s.sectionIndex > ?1 AND s.course.courseID = ?2")
    public List<Section> getListSectionDelete(int sectionIndex, Long courseID);

    @Query("SELECT s FROM Section s WHERE s.sectionIndex > ?1 AND s.sectionIndex <= ?2 AND s.course.courseID = ?3")
    public List<Section> getListSectionUpdateInc(int sectionIndex, int newIndex, Long courseID);

    @Query("SELECT s FROM Section s WHERE s.sectionIndex < ?1 AND s.sectionIndex >= ?2 AND s.course.courseID = ?3")
    public List<Section> getListSectionUpdateDes(int sectionIndex, int newIndex, Long courseID);
}
