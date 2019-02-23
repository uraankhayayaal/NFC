package com.example.nfc.service;

public class User {
    private String surname;
    private String firstName;
    private String patronymic;
    private String email;
    private String phone;
    private String inn;
    private String citizenship;
    private String birthDate;
    private String sex;

    public User() {

    }

    public User(String surname, String firstName, String patronymic, String email, String phone, String inn, String citizenship, String birthDate, String sex) {
        this.surname = surname;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.email = email;
        this.phone = phone;
        this.inn = inn;
        this.citizenship = citizenship;
        this.birthDate = birthDate;
        this.sex = sex;
    }

    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInn() {
        return inn;
    }
    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getCitizenship() {
        return citizenship;
    }
    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

}
