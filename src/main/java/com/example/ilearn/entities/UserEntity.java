package com.example.ilearn.entities;

import com.example.ilearn.models.LoginInputs;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(25)")
    private String login;

    private String password;

    @Column(columnDefinition = "varchar(256)")
    private String email;

    @Column(columnDefinition = "varchar(25)")
    private String role;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<CourseEntity> courses;

//    @ManyToMany
//    @JoinTable(name = "cart",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "course_id"))
//    private List<CourseEntity> purchasedCourses;

    public UserEntity(LoginInputs loginInputs){
        this.login = loginInputs.getLogin();
        this.password = loginInputs.getPassword();
        this.email = loginInputs.getEmail();
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

}
