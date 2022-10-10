package com.fsoft.team.entity;

import java.io.Serializable;
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
@Table(name = "tbl_contents")
public class Content implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentID;
    
    @Column(name = "unitName", length = 255)
    private String unitName;

    @Column(name = "contentBody", length = 4000)
    private String contentBody;

    @Column(name = "contentIndex")
    private int contentIndex;

    @ManyToOne()
    @JoinColumn(name = "courseID")
    private Course course;
    
    @ManyToOne()
    @JoinColumn(name = "sectionID")
    private Section section;
    
    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL)
    private List<ContentUser> listContentUser;
    
    public Content(String unitName, String contentBody, int contentIndex, Course course, Section section) {
        this.unitName = unitName;
        this.contentBody = contentBody;
        this.contentIndex = contentIndex;
        this.course = course;
        this.section = section;
    }
}