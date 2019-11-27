package com.demo;

public class ModuleTwo {
    private String name;
    private String description;
    private String over;

    public ModuleTwo() {
    }

    public ModuleTwo(String name, String description, String over) {
        this.name = name;
        this.description = description;
        this.over = over;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOver() {
        return over;
    }

    public void setOver(String over) {
        this.over = over;
    }
}
