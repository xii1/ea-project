package edu.miu.cs.appointmentsystem.services.dto;

import edu.miu.cs.appointmentsystem.services.dto.base.DtoBase;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class ProviderDto extends DtoBase {

    private static final String PROVIDER_NAME_IS_REQUIRED = "Provider name is required !";
    private static final String THE_NAME_MUST_BE_NO_LONGER_THAN_256_CHARACTERS = "Must be no longer than 256 characters";

    @NotNull(message = PROVIDER_NAME_IS_REQUIRED)
    @Length(max = 256, message = THE_NAME_MUST_BE_NO_LONGER_THAN_256_CHARACTERS)
    private String name;
}
