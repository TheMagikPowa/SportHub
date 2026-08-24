package com.generation.SportHub.service;

import java.util.Map;


import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.generation.SportHub.converters.PersonConverter;
import com.generation.SportHub.dto.PersonDTO;
import com.generation.SportHub.entity.Person;
import com.generation.SportHub.repository.PersonRepository;

@Service
public class PersonService extends GenericService<Long, Person, PersonDTO, PersonConverter, PersonRepository>{

    private final PersonConverter personConverter; 
    private final PersonRepository pRepo;

    public PersonService(PersonRepository pr, PersonConverter pc, ApplicationContext ac){
        super(pr, pc, ac);
        this.personConverter= pc;
        this.pRepo = pr;
    }
    @Override
    public Person construct(Map<String, String> params) {
        Person p= getContext().getBean(Person.class, params);
        return p;
        
    }

    //metodo per creare una nuova utenza di tipo person
    public Person createNewPerson (PersonDTO personDTO) throws Exception {
        // TODO controllare se la mail che arriva già è nel db: 
        // - se già esiste non va creata una nuova utenza
        // se non esiste si può creare la persona 

        if(pRepo.existsByEmailIgnoreCase(personDTO.email())) {
        throw new Exception("Utenza già presente a sistema, impossibile procedere");
        
        } else {
        Person newPerson = personConverter.fromDtoToEntity(personDTO);
         //salvataggio sul db 
        return pRepo.save(newPerson);
       
        }
    }

    public Person updatePerson(Long id, PersonDTO pDTO) throws Exception {

        Person updatedPerson = pRepo.findById(id).orElseThrow(() -> new Exception ("Utente non trovato"));

        updatedPerson.setName(pDTO.name());
        updatedPerson.setSurname(pDTO.surname());
        updatedPerson.setUsername(pDTO.username());
        updatedPerson.setDob(pDTO.dob());
        updatedPerson.setGender(pDTO.gender());

        return pRepo.save(updatedPerson);
    }

    

    
}
