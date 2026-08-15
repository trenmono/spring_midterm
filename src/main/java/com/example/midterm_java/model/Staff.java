package com.example.midterm_java.model;

import jakarta.persistence.*;

@Entity
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sId;

    private String userName;

    private String password;

    @ManyToOne
    @JoinColumn(name ="role_Id")
    private Role role;

    public Staff() {
    }

    public Staff(int sId, String userName, String password, Role role) {
        this.sId = sId;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    public int getSId() {
        return sId;
    }

    public void setSId(int sId) {
        this.sId = sId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
