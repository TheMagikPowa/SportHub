package com.generation.SportHub.service;

import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.generation.SportHub.converters.PersonConverter;
import com.generation.SportHub.dto.PersonDTO;
import com.generation.SportHub.entity.Person;
import com.generation.SportHub.repository.PersonRepository;

public class PersonService extends GenericService<Long, Person, PersonDTO, PersonConverter, PersonRepository>{

    public PersonService(PersonRepository pr, PersonConverter pc, ApplicationContext ac){
        super(pr, pc, ac);
    }
    @Override
    public Person construct(Map<String, String> params) {
        Person p= getContext().getBean(Person.class, params);
        return p;
        
    }

    
    
}
