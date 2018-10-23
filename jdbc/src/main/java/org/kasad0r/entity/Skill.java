package org.kasad0r.entity;

import lombok.Data;

@Data
public class Skill {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public Skill setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Skill setName(String name) {
        this.name = name;
        return this;
    }
}
