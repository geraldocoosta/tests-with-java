package com.example.brewery.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.brewery.dto.FullBrewery;
import com.example.brewery.exception.dto.ExceptionDetails;
import com.example.brewery.exception.dto.ValidationExceptionDetails;
import com.example.brewery.form.BreweryForm;
import com.example.brewery.service.BreweryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Tag(name = "Brewery Api")
@RequestMapping("/v1/brewery")
@RestController
@RequiredArgsConstructor
@Log4j2
public class BreweryController {
	
	private final BreweryService breweryService;
	
	@PostMapping 
    @Operation(summary = "Save browery rate", description = "Save a rating for brewery")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Successful Operation"),
			@ApiResponse(responseCode = "404", description = "Brewery not found", content = @Content(schema = @Schema(implementation = ExceptionDetails.class))),
			@ApiResponse(responseCode = "400", description = "Email alredy voted at this brewing or missing required params in form", content = @Content(schema = @Schema(implementation = ValidationExceptionDetails.class))),
	})
	public ResponseEntity<Void> saveBreweryRate(@RequestBody @Valid BreweryForm form) {
		log.info("Received request with object {}", form);
		breweryService.saveBreweryRate(form);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping
    @Operation(summary = "Find broweries", description = "Find for breweries")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful Operation")
	})
	public ResponseEntity<List<FullBrewery>> findBreweries(@RequestParam(value="by_city", required = false) String byCity,
			@RequestParam(value="page",required = false) String page ,
            @RequestParam(value="per_page", required = false) String perPage,
            @RequestParam(value="sort", required = false) String sort) {
		return new ResponseEntity<>(breweryService.findBreweries(byCity, page, perPage, sort), HttpStatus.OK);
	}
	
	@GetMapping("/{idBrewery}")
    @Operation(summary = "Detail browery", description = "Detail rating for brewery")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful Operation"),
			@ApiResponse(responseCode = "404", description = "Brewery not found", content = @Content(schema = @Schema(implementation = ExceptionDetails.class))),
	})
	@ResponseBody
	public ResponseEntity<FullBrewery> detailBreweries(@PathVariable String idBrewery) {
		FullBrewery breweryDetail = breweryService.detailBrewery(idBrewery);
		return new ResponseEntity<>(breweryDetail, HttpStatus.OK);
	}
	
	@GetMapping("/topTen")
    @Operation(summary = "Find top 10 broweries", description = "Find top 10 broweries")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successful Operation")
	})
	@ResponseBody
	public ResponseEntity<List<FullBrewery>> findTopTenBreweries() {
		return new ResponseEntity<>(breweryService.findTopTenBreweries(), HttpStatus.OK);
	}
	
}
