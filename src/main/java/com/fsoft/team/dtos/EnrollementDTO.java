package com.fsoft.team.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollementDTO {
    private int enrollId;
    private LocalDateTime enroll_date;
    private boolean isComplete;
    private String role;
    private int courseId;
    private String username;
    private LocalDateTime date_complete;
}
