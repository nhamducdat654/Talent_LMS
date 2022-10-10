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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_progess")
public class Progress implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long progessID;

    @Column(name = "completeDate")
    private LocalDateTime completeDate;

    @ManyToOne()
    @JoinColumn(name = "userName")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "contentUserID")
    private ContentUser contentuser;

    @ManyToOne()
    @JoinColumn(name = "quizUserID")
    private QuizUser quizuser;
}
