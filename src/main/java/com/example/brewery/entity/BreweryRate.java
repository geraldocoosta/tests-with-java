package com.example.brewery.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Document(collection = "breweriesrate")
@Builder
public class BreweryRate {
	@Id
	private String id;
	private String brewingId;
	private Double average;
	private List<Rate> rate;
}
