package com.devsuperior.demo.services;

import com.devsuperior.demo.dto.CityDTO;
import com.devsuperior.demo.entities.City;
import com.devsuperior.demo.exceptions.DataIntegrationViolated;
import com.devsuperior.demo.exceptions.ResourceNotFoundException;
import com.devsuperior.demo.repositories.CityRepository;
import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

    @Autowired
    private CityRepository repository;


    @Transactional(readOnly = true)
    public List<CityDTO> findAll(){
        List<City> result = repository.findAll(Sort.by("name"));
        return result.stream().map(CityDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CityDTO findById(Long id){
        City city = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ID não encontrado " + id));
        return new CityDTO(city);
    }


    @Transactional
    public CityDTO insert(CityDTO dto){
        City entity = new City();
        entity.setId(null);
        entity.setName(dto.getName());
        entity = repository.save(entity);
        dto.setName(entity.getName());
        return dto;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id){
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("ID não encontrado " + id);
        }
        try{
            repository.deleteById(id);
        }catch(DataIntegrityViolationException exception){
            throw new DataIntegrationViolated("Integridade violada");
        }
    }

}