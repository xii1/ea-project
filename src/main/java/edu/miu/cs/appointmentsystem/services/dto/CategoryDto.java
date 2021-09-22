package edu.miu.cs.appointmentsystem.services.dto;

import edu.miu.cs.appointmentsystem.services.dto.base.DtoBase;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.time.LocalTime;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
public class CategoryDto extends DtoBase {
    private static final String CATEGORY_NAME_REQUIRED = "The name is required.";
    private static final String CATEGORY_NAME_MIN_LENGTH = "The name must be more than 2 characters.";
    private static final String CATEGORY_NAME_MAX_LENGTH = "The note must be no longer than 200 characters.";
    private static final String DEFAULT_DURATION_REQUIRED = "The default duration is required.";

    @NotEmpty(message = CATEGORY_NAME_REQUIRED)
    @Length(min = 2, message = CATEGORY_NAME_MIN_LENGTH)
    @Length(max = 200, message = CATEGORY_NAME_MAX_LENGTH)
    private String name;

    @NotNull(message = DEFAULT_DURATION_REQUIRED)
    private LocalTime defaultDuration;
}
