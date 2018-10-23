package org.kasad0r.entity;

import lombok.Data;

@Data
public class Tool {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public Tool setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Tool setName(String name) {
        this.name = name;
        return this;
    }
}
