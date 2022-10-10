package com.fsoft.team.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SectionDTO {
    
    private Long sectionID;
    private String sectionName;
    private Long countSection;
    
    public SectionDTO(Long sectionID, Long countSection) {
        this.sectionID = sectionID;
        this.countSection = countSection;
    }

    public SectionDTO(Long sectionID, String sectionName, Long countSection) {
        this.sectionID = sectionID;
        this.sectionName = sectionName;
        this.countSection = countSection;
    }
}
