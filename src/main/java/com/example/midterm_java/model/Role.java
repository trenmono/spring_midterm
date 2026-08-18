package com.example.midterm_java.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
//@NoArgsConstructor
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rId;

    @Column(unique = true)
    private String roleName;

    public Role() {
    }

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public int getRId() {
        return rId;
    }

    public void setRId(int rId) {
        this.rId = rId;
    }

    public Integer getId() {
        return rId;
    }

    public void setId(int rId) {
        this.rId = rId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o instanceof String) {
            return roleName != null && roleName.equalsIgnoreCase((String) o);
        }
        if (o instanceof Role) {
            Role other = (Role) o;
            return rId == other.rId || (roleName != null && roleName.equalsIgnoreCase(other.roleName));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return roleName != null ? roleName.toLowerCase().hashCode() : 0;
    }

    @Override
    public String toString() {
        return roleName != null ? roleName : "";
    }
}
