package com.kafkaconsumer.kafkaconsumer.model;

public class Users {

    private String name;

    private String age;

    private Long salary;

    public Users() {
    }

    public Users(String name, String age, Long salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Users [age=" + age + ", name=" + name + ", salary=" + salary + "]";
    }

   

    
    
}