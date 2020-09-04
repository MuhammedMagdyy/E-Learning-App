package com.example.e_learning.models;

public class Data {
    public String full_name, phone_student, phone_parents;

    public Data(String full_name, String phone_student, String phone_parents) {
        this.full_name = full_name;
        this.phone_student = phone_student;
        this.phone_parents = phone_parents;
    }


    @Override
    public String toString() {
        return "Data{" +
                "full_name='" + full_name + '\'' +
                ", phone_student='" + phone_student + '\'' +
                ", phone_parents='" + phone_parents + '\'' +
                '}';
    }
}
