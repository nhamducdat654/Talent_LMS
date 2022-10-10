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
@Table(name = "tbl_courses")

public class Course implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseID;

    @Column(name = "courseName", length = 50)
    private String courseName;

    @Column(name = "description", length = 4000)
    private String courseDes;

    @Column(name = "courseImg", length = 255)
    private String courseImg;

    @Column(name = "price")
    private float price;

    @Column(name = "dateOfCreate")
    private LocalDateTime dateOfCreate;

    @Column(name = "status")
    private int status;

    @ManyToOne()
    @JoinColumn(name = "categoryID")
    private Category category;

    @ManyToOne()
    @JoinColumn(name = "username")
    private User user;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Enrollement> listEnrollement;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Order> listOrder;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Content> listContent;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Quiz> listQuiz;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Section> listSection;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Question> listQuestion;

    public Course(Long id) {
        this.courseID = id;
    }
}
