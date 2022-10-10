package com.fsoft.team.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_roles")

public class Role implements Serializable {

    @Id
    @Column(name = "roleID", length = 20)
    private String roleID;

    @Column(name = "roleName", length = 255)
    private String roleName;
    
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<User> listUser;

//    @Override
//    public String getAuthority() {
//        return roleName;
//    }
}
