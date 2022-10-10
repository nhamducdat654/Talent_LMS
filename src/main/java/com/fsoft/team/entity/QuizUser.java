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
@Table(name = "tbl_quizuser")
public class QuizUser implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizUserID;

    @Column(name = "isPass")
    private boolean isPass;
    
    @Column(name = "doQuizDate")
    private LocalDateTime doQuizDate;
    
    @Column(name = "point")
    private float point;
    
    @ManyToOne()
    @JoinColumn(name = "quizID")
    private Quiz quiz;

    @ManyToOne()
    @JoinColumn(name = "username")
    private User user;

    @OneToMany(mappedBy = "quizuser", cascade = CascadeType.ALL)
    private List<Progress> listProgress;
    
}
