package com.example.brewery.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FullBrewery {
	
	private String id;
	private String name;
	private String breweryType;
	private String street;
	private String city;
	private String state;
	private String postalCode;
	private String country;
	private String longitude;
	private String latitude;
	private String phone;
	private String websiteUrl;
	private LocalDateTime updatedAt;
	private List<String> tagList;
	private Double average;
	private int numberOfVotes;
	
	

	public FullBrewery returnCompleteBrewery(BreweryDTO b, Double average, int numberOfVotes) {
		this.id = b.getId();
		this.name = b.getName();
		this.breweryType = b.getBreweryType();
		this.street = b.getStreet();
		this.city = b.getCity();
		this.state = b.getState();
		this.postalCode = b.getPostalCode();
		this.country = b.getCountry();
		this.longitude = b.getLongitude();
		this.latitude = b.getLatitude();
		this.phone = b.getPhone();
		this.websiteUrl = b.getWebsiteUrl();
		this.updatedAt = b.getUpdatedAt();
		this.tagList = b.getTagList();
		this.average = average;
		this.numberOfVotes = numberOfVotes;
		
		return this;
	}
	
}
