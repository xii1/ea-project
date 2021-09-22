package edu.miu.cs.appointmentsystem.services.dto.base;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public abstract class DtoBase {
    private Long id;
    private String createdBy;
    private String updatedBy;
    // @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-mm-dd")
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
