package com.fsoft.team.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_enrollements")

public class Enrollement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollID;

    @Column(name = "enrollDate")
    private LocalDateTime enrollDate;

    @NotNull
    @Column(name = "role")
    private String role;
    
    @Column(name = "isComplete")
    private boolean isComplete;

    @Column(name = "dateComplete")
    private LocalDateTime dateComplete;

    @Column(name = "checkDisable")
    private int checkDisable;
    
    @ManyToOne()
    @JoinColumn(name = "username")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "courseID")
    private Course course;

    @ManyToOne()
    @JoinColumn(name = "cardNumber")
    private Credit credit;
    
    public Enrollement(LocalDateTime enrollDate, boolean isComplete, User user, Course course) {
        this.enrollDate = enrollDate;
        this.isComplete = isComplete;
        this.user = user;
        this.course = course;
    }
}
