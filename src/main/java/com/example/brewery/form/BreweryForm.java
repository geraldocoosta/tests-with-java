package com.example.brewery.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BreweryForm {
	
    @NotBlank(message = "idBrewery is mandatory")
	private String idBrewery;
	@Min(value = 1, message = "minimun value for field rate is {value}")
	@Max(value = 5, message = "minimum value for field rate is {value}")
	@NotNull(message = "rate is mandatory")
	private Integer rate;
    @NotBlank(message = "email is mandatory")
    @Email
	private String email;
}
