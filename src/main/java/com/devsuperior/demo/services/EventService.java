package com.devsuperior.demo.services;

import com.devsuperior.demo.dto.EventDTO;
import com.devsuperior.demo.entities.City;
import com.devsuperior.demo.entities.Event;
import com.devsuperior.demo.exceptions.ResourceNotFoundException;
import com.devsuperior.demo.repositories.CityRepository;
import com.devsuperior.demo.repositories.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class EventService {

    @Autowired
    private EventRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private CityRepository cityRepository;

    @Transactional
    public EventDTO update (Long id, EventDTO dto){
        try{
            Event entity = repository.getReferenceById(id);
            entity.setId(id);
            entity.setName(dto.getName());
            entity.setDate(dto.getDate());
            entity.setUrl(dto.getUrl());
            City city = cityRepository.getReferenceById(dto.getCityId());
            entity.setCity(city);
            entity = repository.save(entity);
            return new EventDTO(entity);

        }catch(EntityNotFoundException exception){
            throw new ResourceNotFoundException("id not found " + id);
        }
    }
}
