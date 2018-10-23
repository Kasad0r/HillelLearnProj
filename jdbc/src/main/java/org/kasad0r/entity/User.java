package org.kasad0r.entity;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    private int id;
    private String email;
    private String name;
    private String phone;

    private List<Skill> skills;
    private List<Tool> tools;
}
