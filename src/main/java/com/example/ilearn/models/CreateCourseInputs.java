package com.example.ilearn.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCourseInputs {
    private String title;
    private String description;
    private Long price;
}
