package com.generation.SportHub.service;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.generation.SportHub.converters.PersonConverter;
import com.generation.SportHub.dto.PersonDTO;
import com.generation.SportHub.entity.Buyer;
import com.generation.SportHub.entity.Person;
import com.generation.SportHub.entity.enums.Role;
import com.generation.SportHub.repository.PersonRepository;

import jakarta.transaction.Transactional;

@Service
public class PersonService extends GenericService<Long, Person, PersonDTO, PersonConverter, PersonRepository>{

    private final PersonConverter personConverter; 
    private final PersonRepository pRepo;
    private final PasswordEncoder passwordEncoder;

    public PersonService(
                PersonRepository pr,
                PersonConverter pc,
                ApplicationContext ac,
                PasswordEncoder passwordEncoder) {

            super(pr, pc, ac);
            this.personConverter = pc;
            this.pRepo = pr;
            this.passwordEncoder = passwordEncoder;
        }

    @Override
    public Person construct(Map<String, String> params) {
        Person p= getContext().getBean(Person.class, params);
        return p;
        
    }

 
     @Transactional
    public Person createNewPerson (PersonDTO personDTO) throws Exception {
        // TODO controllare se la mail che arriva già è nel db: 
        // - se già esiste non va creata una nuova utenza
        // se non esiste si può creare la persona 

        if(pRepo.existsByEmailIgnoreCase(personDTO.email())) {
        throw new Exception("Utenza già presente a sistema, impossibile procedere");
        
        } else {
        if (personDTO.role() == null || personDTO.role() == Role.BUYER) {
            Buyer newBuyer = new Buyer();

            newBuyer.setEmail(personDTO.email());
            newBuyer.setUsername(personDTO.username());
            newBuyer.setName(personDTO.name());
            newBuyer.setSurname(personDTO.surname());
            newBuyer.setDob(personDTO.dob());
            newBuyer.setGender(personDTO.gender());
            newBuyer.setRole(Role.BUYER);
            newBuyer.setPassword(passwordEncoder.encode(personDTO.password()));
            newBuyer.setActive(true);

            return pRepo.save(newBuyer);
}

Person newPerson = personConverter.fromDtoToEntity(personDTO);
return pRepo.save(newPerson);
       
        }
    }

    @Transactional
    public Person updatePerson(Long id, PersonDTO pDTO) throws Exception {

        Person updatedPerson = pRepo.findById(id).orElseThrow(() -> new Exception ("Utente non trovato"));

        updatedPerson.setName(pDTO.name());
        updatedPerson.setSurname(pDTO.surname());
        updatedPerson.setUsername(pDTO.username());
        updatedPerson.setDob(pDTO.dob());
        updatedPerson.setGender(pDTO.gender());

        return pRepo.save(updatedPerson);
    }
    @Transactional
    public void deletePerson(Long id) throws Exception {
        Person person = pRepo.findById(id)
            .orElseThrow(() -> new Exception("Utente non trovato"));
        
        pRepo.delete(person);
    }
    

    
}
