package edu.miu.cs.appointmentsystem.integration.models;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class Email implements Serializable {
    private String subject;
    private String to;
    private String body;

}
