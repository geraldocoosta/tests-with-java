package com.example.brewery.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.brewery.form.BreweryForm;
import com.example.brewery.util.testcontainer.MongoDbContainerUtil;
import com.example.brewery.util.wiremock.WireMockConfig;
import com.example.brewery.util.wiremock.WireMockConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
@ContextConfiguration(classes = { WireMockConfig.class })
class BreweryControllerIT extends MongoDbContainerUtil {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
    @Autowired
    private WireMockServer mockServer;
	
    @BeforeEach
    void setUp()  {
    	WireMockConfiguration.setupMockBreweryErrorResponse(mockServer);
    	WireMockConfiguration.setupMockBreweryResponse(mockServer);
    	WireMockConfiguration.setupMockBreweryResponse2(mockServer);
    	WireMockConfiguration.setupMockBreweryResponse3(mockServer);
    	WireMockConfiguration.setupMockBreweriesListResponse(mockServer);
    	WireMockConfiguration.setupMockBreweriesListWithPageResponse(mockServer);
    	WireMockConfiguration.setupMockBreweriesListWithPerPageResponse(mockServer);
    	WireMockConfiguration.setupMockBreweriesListWithCityPageResponse(mockServer);
    }

    
	@Test
    @DisplayName("Save new brewery rate successfully")
	void saveBreweryRate_ReturnsCreated_WhenSuccessful() throws Exception {
		
		BreweryForm build = BreweryForm.builder()
										.email("teste123123@teste.com")
										.idBrewery("10-barrel-brewing-co-san-diego")
										.rate(4).build();
		mockMvc.perform(post("/v1/brewery")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(build)))
				.andExpect(status().isCreated());
	}
	
	@Test
    @DisplayName("Save new brewery rate with bad request")
	void saveBreweryRate_ReturnsBadRequest_WhenMissingEmail() throws Exception {

		BreweryForm build = BreweryForm.builder()
										.email("")
										.idBrewery("10-barrel-brewing-co-san-diego")
										.rate(4).build();
		mockMvc.perform(post("/v1/brewery")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(build)))
				.andExpect(status().isBadRequest());
	}
	
	@Test
    @DisplayName("Save new brewery rate with bad request for email duplication in same brewery")
	void saveBreweryRate_ReturnsBadRequest_WhenDuplicationEmail() throws Exception {

		BreweryForm build = BreweryForm.builder()
										.email("teste5@teste.com")
										.idBrewery("10-barrel-brewing-co-san-diego")
										.rate(4).build();
		
		mockMvc.perform(post("/v1/brewery")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(build)))
				.andExpect(status().isCreated());
		
		mockMvc.perform(post("/v1/brewery")
					.contentType("application/json")
					.content(objectMapper.writeValueAsString(build)))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Detail breweries successfully with brewery exists in db")
	void detailBreweries_ReturnsSucess_WhenExistingInDB() throws Exception {

		BreweryForm build = BreweryForm.builder()
				.email("teste566@teste.com")
				.idBrewery("12-west-brewing-company-production-facility-mesa")
				.rate(4).build();

		mockMvc.perform(post("/v1/brewery")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(build)))
				.andExpect(status().isCreated());

		mockMvc.perform(get("/v1/brewery/{id}", "12-west-brewing-company-production-facility-mesa")
						.contentType("application/json"))
				.andExpect(status().isOk());

	}

	@Test
	@DisplayName("Detail breweries successfully with brewery not exists in db")
	void detailBreweries_ReturnsSucess_WhenNotExistingInDB() throws Exception {
		mockMvc.perform(get("/v1/brewery/{id}", "10-barrel-brewing-co-bend-pub-bend")
						.contentType("application/json"))
				.andExpect(status().isOk());
	}
	
	@Test
    @DisplayName("Save new brewery rate with successfully with more than one email")
	void saveBreweryRate_ReturnsCreated_WhenMoreEmails() throws Exception {

		BreweryForm build = BreweryForm.builder()
										.email("teste2@teste.com")
										.idBrewery("10-barrel-brewing-co-san-diego")
										.rate(3).build();
		
		mockMvc.perform(post("/v1/brewery")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(build)))
				.andExpect(status().isCreated());
		
		build = BreweryForm.builder()
				.email("testeteste@teste.com")
				.idBrewery("10-barrel-brewing-co-san-diego")
				.rate(5).build();
		
		mockMvc.perform(post("/v1/brewery")
					.contentType("application/json")
					.content(objectMapper.writeValueAsString(build)))
				.andExpect(status().isCreated());
	}

	@Test
    @DisplayName("Save new brewery rate return Not Found when no exists Brewery")
	void saveBreweryRate_ReturnsNotFound_WhenBreweryNotFound() throws Exception {

		BreweryForm build = BreweryForm.builder()
										.email("teste2@teste.com")
										.idBrewery("0-barrel-brewing-co-san-diego")
										.rate(3).build();
		
		mockMvc.perform(post("/v1/brewery")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(build)))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Find brewery return successfully.")
	void findBreweries() throws Exception {

		mockMvc.perform(get("/v1/brewery")
						.contentType("application/json"))
				.andExpect(status().is2xxSuccessful());

	}

	@Test
	@DisplayName("Find brewery return successfully, using the parameter by_city")
	void findBreweries_Using_Parameter_By_City() throws Exception {

		mockMvc.perform(get("/v1/brewery?by_city=san_diego")
						.contentType("application/json"))
				.andExpect(status().is2xxSuccessful() );

	}

	@Test
	@DisplayName("Find brewery return successfully, using the parameter per_page")
	void findBreweries_Using_Parameter_Per_Page() throws Exception {

		mockMvc.perform(get("/v1/brewery?per_page=1")
						.contentType("application/json"))
				.andExpect(status().is2xxSuccessful() );

	}

	@Test
	@DisplayName("Find brewery return successfully, using the parameter page")
	void findBreweries_Using_Parameter_Page() throws Exception {

		mockMvc.perform(get("/v1/brewery?page=1")
						.contentType("application/json"))
				.andExpect(status().is2xxSuccessful() );

	}

	@Test
	@DisplayName("Find brewery top ten return successfully.")
	void findTopTenBreweries() throws Exception {

		mockMvc.perform(get("/v1/brewery/topTen")
						.contentType("application/json"))
				.andExpect(status().is2xxSuccessful());

	}
}
