package com.fsoft.team.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private Long courseId;
    private String description;
    private String courseImg;
    private String courseName;
    private LocalDateTime date_of_create;
    private float price;
    private int status;
    private LocalDateTime enroll_date;
    private LocalDateTime date_complete;
    private String roleName;
    private String userOfCourse;
    private int checkDisableEnroll;
    private Long enrollId;
    private Long categoryId;
    private String username;
}
