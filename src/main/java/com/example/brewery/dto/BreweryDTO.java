package com.example.brewery.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BreweryDTO {
	private String id;
	private String name;
	@JsonProperty("brewery_type")
	private String breweryType;
	private String street;
	private String city;
	private String state;
	@JsonProperty("postal_code")
	private String postalCode;
	private String country;
	private String longitude;
	private String latitude;
	private String phone;
	@JsonProperty("website_url")
	private String websiteUrl;
	@JsonProperty("updated_at")
	private LocalDateTime updatedAt;	
	private List<String> tagList;
}
