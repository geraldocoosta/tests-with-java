package com.example.brewery.service;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.brewery.dto.BreweryDTO;
import com.example.brewery.entity.BreweryRate;
import com.example.brewery.entity.Rate;
import com.example.brewery.exception.NotFoundException;
import com.example.brewery.feign.OpenBreweryFeign;
import com.example.brewery.form.BreweryForm;
import com.example.brewery.reposity.BreweryRateRepository;
import com.example.brewery.service.impl.BreweryServiceImpl;

@ExtendWith(SpringExtension.class)
class BreweryServiceTest {

	@InjectMocks
	private BreweryServiceImpl breweryService;
	@Mock
	private BreweryRateRepository repository;
	@Mock
	private OpenBreweryFeign feign;
	
    @BeforeEach
    void setUp(){
    	BDDMockito.when(feign.findById(ArgumentMatchers.anyString()))
    		.thenReturn(createValidBreweryDTO());
    }
    
	@Test
    @DisplayName("Save new brewery rate successfully")
	void saveBreweryRate_WhenSuccessful()  {

		BreweryForm build = BreweryForm.builder()
										.email("teste@teste.com")
										.idBrewery("10-barrel-brewing-co-san-diego")
										.rate(4).build();
		
        Assertions.assertThatCode(() -> breweryService.saveBreweryRate(build)).doesNotThrowAnyException();
	}
	
	@Test
    @DisplayName("Save new brewery rate with Exception for brewery not found")
	void saveBreweryRate_ThrowsError_WhenDuplicationEmail() {
    	BDDMockito.when(feign.findById(ArgumentMatchers.anyString()))
					.thenThrow(new RuntimeException("Não encontrado"));

		BreweryForm build = BreweryForm.builder()
										.email("teste5@teste.com")
										.idBrewery("0-barrel-brewing-co-san-diego")
										.rate(4).build();
        
		Assertions.assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> breweryService.saveBreweryRate(build));
	}
	
	
	private BreweryDTO createValidBreweryDTO() {
		return BreweryDTO.builder()
			    .id("10-barrel-brewing-co-san-diego")
			    .name("10 Barrel Brewing Co")
			    .breweryType("large")
			    .street("1501 E St")
			    .city("San Diego")
			    .state("California")
			    .postalCode("92101-6618")
			    .country("United States")
			    .longitude("-117.129593")
			    .latitude("32.714813")
			    .phone("6195782311")
			    .websiteUrl("http://10barrel.com")
			    .updatedAt(LocalDateTime.now())
			    .build();
	}

	@Test
	@DisplayName("Search details breweries not found")
	void detailBrewery_Not_Found()  {
		BDDMockito.when(feign.findById(ArgumentMatchers.anyString()))
				.thenThrow(new NotFoundException("Não encontrado"));

		Assertions.assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> breweryService.detailBrewery(""));
	}

	@Test
	@DisplayName("Search details breweries")
	void detailBrewery() {
		Assertions.assertThatCode(() -> breweryService.detailBrewery("10-barrel-brewing-co-san-diego")).doesNotThrowAnyException();
	}
	
	@Test
	@DisplayName("Search breweries")
	void searchBrewery()  {
		Assertions.assertThatCode(() -> breweryService.findBreweries("","","","")).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Find top ten breweries")
	void findTopTenBreweries()  {
		List<Rate> ratings = List.of(new Rate(4, "teste@email.com"));

		List<BreweryRate> breweryRateList = List.of(BreweryRate.builder()
				.id("das")
				.average(4.0)
				.brewingId("123")
				.rate(ratings)
				.build());

		BDDMockito.when(repository.findAll(Sort.by(Sort.Direction.DESC, "average")))
				.thenReturn(breweryRateList);

		Assertions.assertThatCode(() -> breweryService.findTopTenBreweries()).doesNotThrowAnyException();
	}
	
}
