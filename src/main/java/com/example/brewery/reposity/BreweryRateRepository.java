package com.example.brewery.reposity;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.brewery.entity.BreweryRate;

public interface BreweryRateRepository extends MongoRepository<BreweryRate, String> {

	Optional<BreweryRate> findByBrewingId(String id);
}
