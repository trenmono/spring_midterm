package com.example.midterm_java.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sId;

    private String userName;

    private String password;

    @ManyToOne
    @JoinColumn(name ="role_Id")
    private Role role;
}
