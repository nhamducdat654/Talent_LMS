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
@Table(name = "tbl_questions")
public class Question implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionID;

    @Column(name = "questionName", length = 4000)
    private String questionName;

    @Column(name = "dateOfCreate")
    private LocalDateTime dateOfCreate;

    @ManyToOne()
    @JoinColumn(name = "quizID")
    private Quiz quiz;

    @ManyToOne()
    @JoinColumn(name = "courseID")
    private Course course;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> listAnswer;
}
