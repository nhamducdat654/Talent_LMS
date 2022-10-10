package com.fsoft.team.repository;

import com.fsoft.team.entity.Enrollement;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EnrollementRepository extends JpaRepository<Enrollement, Long> {

    @Query("SELECT e from Enrollement e where e.course.courseID = ?1")
    public List<Enrollement> getEnrollWithCourseID(Long courseID);

    @Query("select e.course.courseID from Enrollement  e where e.user.username = ?1")
    public List<Long> getCourseIDByUserName(String username);

    @Query("select e from Enrollement  e where e.user.username = ?1 and e.course.courseID = ?2")
    public Enrollement getEnrollementByUserNameAndCourseID(String username,Long courseID);

    @Query("SELECT e FROM Enrollement e WHERE e.user.username = ?1")
    List<Enrollement> getEnrollementByUsername(String username);

    @Transactional
    @Modifying
    @Query("DELETE FROM Enrollement  e WHERE e.enrollID = :id")
    int delete(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Enrollement e SET e.dateComplete = null WHERE e.enrollID = :enrollId")
    void updateDisable(@Param("enrollId") Long enrollId);


    public boolean existsEnrollmentByUser_UsernameAndCourse_CourseIDIs(String username, Long courseID);

    @Modifying
    @Query(value = "delete from tbl_enrollements where username = :username and courseID = :courseID", nativeQuery = true)
    @javax.transaction.Transactional
    public void removeEnrollment(@Param("username") String username, @Param("courseID") Long courseID);

    @Modifying
    @Query(value = "insert into tbl_enrollements(enroll_date, is_complete, username, courseid, card_number, check_disable, role) values (:enrollDate, :isComplete, :username, :courseID, :cardNumber, 1, :role)", nativeQuery = true)
    @javax.transaction.Transactional
    public void insertEnrollment(@Param("enrollDate") LocalDateTime enrollDate, @Param("isComplete") boolean isComplete,
                                 @Param("username") String username, @Param("courseID") Long courseID, @Param("cardNumber")String cardNumber, @Param("role") String role);
    @Transactional
    @Modifying
   @Query("UPDATE Enrollement e SET e.isComplete = FALSE WHERE e.enrollID = :enrollId")
    void updateIsComplete(@Param("enrollId") Long enrollId);
}
