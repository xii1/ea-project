package edu.miu.cs.appointmentsystem.domain.enums;

public enum Gender implements StringEnum {
    MALE("MALE"), FEMALE("FEMALE"), UNKNOWN("UNKNOWN");

    String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    @Override
    public String getValue() {
        return gender;
    }
}
