package com.example.brewery.service;

import java.util.List;

import javax.validation.Valid;

import com.example.brewery.dto.FullBrewery;
import com.example.brewery.form.BreweryForm;

public interface BreweryService {

	void saveBreweryRate(@Valid BreweryForm form);
	List<FullBrewery> findBreweries(String byCity, String page, String perPage, String sort);
	FullBrewery detailBrewery(String id);
	List<FullBrewery>  findTopTenBreweries();
}
