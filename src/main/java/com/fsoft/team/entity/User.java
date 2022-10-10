package com.fsoft.team.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "tbl_users")
public class User implements Serializable {

    @Id
    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "password", length = 50)
    private String password;

    @Column(name = "firstName", length = 50)
    private String firstName;

    @Column(name = "lastName", length = 50)
    private String lastName;

    @Column(name = "bio", length = 800)
    private String bio;

    @Column(name = "userImg", length = 255)
    private String userImg;

    @Column(name = "dateOfCreate")
    private LocalDateTime createDate;

    @Column(name = "status")
    private int status;

    @ManyToOne()
    @JoinColumn(name = "roleID")
    private Role role;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Course> listCourse;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Enrollement> listEnrollement;
    
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> listOrder;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Progress> listProgress;
}
