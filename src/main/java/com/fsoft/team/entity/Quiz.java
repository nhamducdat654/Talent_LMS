package com.fsoft.team.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_quizs")
public class Quiz implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizID;

    @Column(name = "unitName", length = 255)
    private String unitName;

    @Column(name = "dateOfCreate")
    private LocalDateTime dateOfCreate;

    @Column(name = "quizIndex")
    private int quizIndex;

    @Column(name = "timeLimit")
    private int timeLimit;

    @Column(name = "passScore")
    private float passScore;

    @ManyToOne()
    @JoinColumn(name = "courseID")
    private Course course;

    @ManyToOne()
    @JoinColumn(name = "sectionID")
    private Section section;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<QuizUser> listQuizUser;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<Question> listQuestion;
}
