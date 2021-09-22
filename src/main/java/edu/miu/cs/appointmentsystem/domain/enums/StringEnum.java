package edu.miu.cs.appointmentsystem.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author XIII
 */
public interface StringEnum {
    @JsonValue
    String getValue();
}
