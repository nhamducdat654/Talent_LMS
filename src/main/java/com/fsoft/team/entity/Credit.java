package com.fsoft.team.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tblCredits")
public class Credit {
    @Id
    @Column(name = "cardNumber")
    private String cardNumber;
    @Column(name = "cvc")
    private String cvc;
    @Column(name = "expirationDate")
    private LocalDateTime expirationDate;
    @ManyToOne
    @JoinColumn(name = "username")
    private User user;
    @OneToMany(mappedBy = "credit", cascade = CascadeType.ALL)
    private List<Enrollement> enrollment;
}
