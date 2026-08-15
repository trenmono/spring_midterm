package com.example.midterm_java.model;

import jakarta.persistence.*;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int rId;

    @Column(unique=true)
    private String roleName;

    public Role() {
    }

    public Role(int rId, String roleName) {
        this.rId = rId;
        this.roleName = roleName;
    }

    public int getRId() {
        return rId;
    }

    public void setRId(int rId) {
        this.rId = rId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
