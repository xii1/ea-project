package edu.miu.cs.appointmentsystem.domain.enums;

/**
 * @author XIII
 */
public enum Role implements StringEnum {
    ADMIN("ADMIN"), PROVIDER("PROVIDER"), CLIENT("CLIENT");

    String name;

    Role(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return name;
    }
}
