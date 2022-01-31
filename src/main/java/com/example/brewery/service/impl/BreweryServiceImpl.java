package com.example.brewery.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.brewery.dto.BreweryDTO;
import com.example.brewery.dto.FullBrewery;
import com.example.brewery.entity.BreweryRate;
import com.example.brewery.entity.Rate;
import com.example.brewery.exception.BadRequestException;
import com.example.brewery.feign.OpenBreweryFeign;
import com.example.brewery.form.BreweryForm;
import com.example.brewery.reposity.BreweryRateRepository;
import com.example.brewery.service.BreweryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BreweryServiceImpl implements BreweryService {

	private final BreweryRateRepository repository;
	private final OpenBreweryFeign breweryFeign;
	
	@Override
	public void saveBreweryRate(@Valid BreweryForm form) {
		BreweryDTO brewery = findBreweryById(form.getIdBrewery());
		repository.findByBrewingId(brewery.getId())
			.ifPresentOrElse(p -> updateBrewingRate(p, form), () -> saveNewBrewingRate(form));
		
	}

	@Override
	public List<FullBrewery> findBreweries(String byCity, String page, String perPage, String sort) {
		List<BreweryDTO> brewaries = breweryFeign.getBreweries(byCity, page, perPage, "name:asc");
		List<FullBrewery> brewariesWithRate = new ArrayList<>();
		brewaries.forEach( b -> {				
			Optional<BreweryRate> rate = repository.findByBrewingId(b.getId());

			FullBrewery completeBrewary = new FullBrewery();
			if (rate.isPresent()) {
				BreweryRate breweryRate = rate.get();
				completeBrewary = completeBrewary.returnCompleteBrewery( b, breweryRate.getAverage(), breweryRate.getRate().size());	
			} else {
				completeBrewary = completeBrewary.returnCompleteBrewery(b, 0.0, 0);				
			}
			
			brewariesWithRate.add(completeBrewary);
		});
		
		brewariesWithRate.sort(Comparator.comparing(FullBrewery::getAverage).reversed());
		return brewariesWithRate;
	}
	
	@Override
	public FullBrewery detailBrewery(String id) {
		BreweryDTO brewery = findBreweryById(id);
		
		FullBrewery completeBrewary = new FullBrewery();
		Optional<BreweryRate> optionalBreweryRate = repository.findByBrewingId(brewery.getId());
		
		if(optionalBreweryRate.isPresent()){
			BreweryRate breweryRate = optionalBreweryRate.get();
			return completeBrewary.returnCompleteBrewery(brewery, breweryRate.getAverage(), breweryRate.getRate().size());
		}
		
		return completeBrewary.returnCompleteBrewery(brewery, 0.0, 0);
	}
	

	@Override
	public List<FullBrewery> findTopTenBreweries() {
		List<BreweryRate> breweries = repository.findAll(Sort.by(Sort.Direction.DESC, "average"));
		List<FullBrewery> brewariesWithRate = new ArrayList<>();
		breweries.forEach( b -> {				
			BreweryDTO dto = findBreweryById(b.getBrewingId());

			FullBrewery completeBrewary = new FullBrewery();
			completeBrewary = completeBrewary.returnCompleteBrewery( dto, b.getAverage(), b.getRate().size());	
						
			brewariesWithRate.add(completeBrewary);
		});
		return brewariesWithRate.subList(0, brewariesWithRate.size() < 10 ? brewariesWithRate.size() : 10);
	}


	private BreweryDTO findBreweryById(String id) {
		return breweryFeign.findById(id);
	}

	
	private void saveNewBrewingRate(BreweryForm form) {
		List<Rate> ratings = List.of(new Rate(form.getRate(), form.getEmail()));
		BreweryRate build = BreweryRate.builder()
				.average(form.getRate().doubleValue())
				.brewingId(form.getIdBrewery())
				.rate(ratings)
				.build();
		repository.save(build);
	}
	
	private void updateBrewingRate(BreweryRate brewingRate, BreweryForm form) {
		List<Rate> ratings = brewingRate.getRate();
		ratings = ratings == null ? new ArrayList<>() : ratings;
		boolean isEmailVoted = ratings.stream()
									.anyMatch(br -> br.getEmail().equals(form.getEmail()));
		if (isEmailVoted) {
			throw new BadRequestException("Email alredy voted at this brewing");
		}			
		ratings.add(new Rate(form.getRate(), form.getEmail()));
		Integer total = ratings.stream().map(Rate::getRateBrewery).reduce(0, Integer::sum);
		brewingRate.setAverage(total.doubleValue() / ratings.size());
		brewingRate.setRate(ratings);
		repository.save(brewingRate);
	}
	
	
}	
