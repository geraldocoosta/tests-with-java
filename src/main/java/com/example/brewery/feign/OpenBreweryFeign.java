package com.example.brewery.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.brewery.dto.BreweryDTO;

@FeignClient(name = "breweries-service", url = "${open-brewery.url}")
public interface OpenBreweryFeign {

	@GetMapping("/breweries/")
	List<BreweryDTO> getBreweries(@RequestParam(value="by_city") String byCity,
										@RequestParam(value="page") String page,
							            @RequestParam(value="per_page") String perPage,
							            @RequestParam(value="sort") String sort);

	@GetMapping("/breweries/{id}")
	BreweryDTO findById(@PathVariable String id);
}
