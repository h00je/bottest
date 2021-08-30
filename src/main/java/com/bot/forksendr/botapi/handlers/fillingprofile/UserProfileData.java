package com.bot.forksendr.botapi.handlers.fillingprofile;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * Данные анкеты пользователя
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileData {
    private String name;
    private String surname;
    private String gender;
//    String color;
//    String movie;
//    String song;
    private int age;
    //private int number;

    public UserProfileData() {
    }

    protected boolean canEqual(final Object other) {
        return other instanceof UserProfileData;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getGender() {
        return this.gender;
    }

    public int getAge() {
        return this.age;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof UserProfileData)) return false;
        final UserProfileData other = (UserProfileData) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$surname = this.getSurname();
        final Object other$surname = other.getSurname();
        if (this$surname == null ? other$surname != null : !this$surname.equals(other$surname)) return false;
        final Object this$gender = this.getGender();
        final Object other$gender = other.getGender();
        if (this$gender == null ? other$gender != null : !this$gender.equals(other$gender)) return false;
        if (this.getAge() != other.getAge()) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $surname = this.getSurname();
        result = result * PRIME + ($surname == null ? 43 : $surname.hashCode());
        final Object $gender = this.getGender();
        result = result * PRIME + ($gender == null ? 43 : $gender.hashCode());
        result = result * PRIME + this.getAge();
        return result;
    }

    public String toString() {
        return "UserProfileData(name=" + this.getName() + ", surname=" + this.getSurname() + ", gender=" + this.getGender() + ", age=" + this.getAge() + ")";
    }
}