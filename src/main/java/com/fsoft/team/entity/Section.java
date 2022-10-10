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
@Table(name = "tbl_sections")
public class Section implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sectionID;

    @Column(name = "sectionName", length = 255)
    private String sectionName;

    @Column(name = "sectionIndex")
    private int sectionIndex;

    @ManyToOne()
    @JoinColumn(name = "courseID")
    private Course course;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<Content> listContent;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<Quiz> listQuiz;
    
    public Section(Long id) {
        this.sectionID = id;
    }
}