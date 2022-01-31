package com.example.brewery.util.wiremock;

import java.io.IOException;
import java.nio.charset.Charset;

import org.eclipse.jetty.io.RuntimeIOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

public class WireMockConfiguration {

	private static final String FIND_BREWERY_BY_ID_JSON = "payloads/find-brewery-by-id.json";
	private static final String FIND_ALL_BREWERIES_JSON = "payloads/find-all-breweries.json";
	private static final String FIND_BREWERY_BY_ID_WITH_ERROR_JSON = "payloads/find-brewery-by-id-with-error.json";


	public static void setupMockBreweryResponse(WireMockServer mockService) {
		setupMock(mockService, FIND_BREWERY_BY_ID_JSON, "/breweries/10-barrel-brewing-co-san-diego", HttpStatus.OK);
	}


	public static void setupMockBreweryResponse2(WireMockServer mockService) {
		setupMock(mockService, FIND_BREWERY_BY_ID_JSON, "/breweries/12-west-brewing-company-production-facility-mesa", HttpStatus.OK);
	}

	public static void setupMockBreweryResponse3(WireMockServer mockService) {
		setupMock(mockService, FIND_BREWERY_BY_ID_JSON, "/breweries/10-barrel-brewing-co-bend-pub-bend", HttpStatus.OK);
	}
	
	public static void setupMockBreweryErrorResponse(WireMockServer mockService) {
		setupMock(mockService, FIND_BREWERY_BY_ID_WITH_ERROR_JSON, "/breweries/0-barrel-brewing-co-san-diego", HttpStatus.NOT_FOUND);
	}
	
	public static void setupMockBreweriesListResponse(WireMockServer mockService) {
		setupMock(mockService, FIND_ALL_BREWERIES_JSON, "/breweries/?sort=name%3Aasc", HttpStatus.OK);
	}
	
	public static void setupMockBreweriesListWithPageResponse(WireMockServer mockService) {
		setupMock(mockService, FIND_ALL_BREWERIES_JSON, "/breweries/?page=1&sort=name%3Aasc", HttpStatus.OK);
	}
	
	public static void setupMockBreweriesListWithPerPageResponse(WireMockServer mockService) {
		setupMock(mockService, FIND_ALL_BREWERIES_JSON, "/breweries/?per_page=1&sort=name%3Aasc", HttpStatus.OK);
	}
	
	public static void setupMockBreweriesListWithCityPageResponse(WireMockServer mockService) {
		setupMock(mockService, FIND_ALL_BREWERIES_JSON, "/breweries/?by_city=san_diego&sort=name%3Aasc", HttpStatus.OK);
	}
	
	
	private static void setupMock(WireMockServer mockService, String resource, String url, HttpStatus status) {
		try {
			String body = StreamUtils.copyToString(WireMockConfiguration.class.getClassLoader()
					.getResourceAsStream(resource),
					Charset.defaultCharset());
			mockService.stubFor(WireMock.get(WireMock.urlEqualTo(url))
					.willReturn(WireMock.aResponse().withStatus(status.value())
							.withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
							.withBody(body)));
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}
}
