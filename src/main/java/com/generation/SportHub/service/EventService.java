package com.generation.SportHub.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.generation.SportHub.converters.EventConverter;
import com.generation.SportHub.dto.EventDTO;
import com.generation.SportHub.entity.Buyer;
import com.generation.SportHub.entity.Event;
import com.generation.SportHub.entity.EventAnswer;
import com.generation.SportHub.repository.BuyerRepository;
import com.generation.SportHub.repository.EventAnswerRepository;
import com.generation.SportHub.repository.EventRepository;

import jakarta.persistence.EntityNotFoundException;



@Service
public class EventService extends GenericService<Long, Event, EventDTO, EventConverter, EventRepository> {
    
    private final EventRepository eRepo;
    private final EventAnswerRepository eventAnswerRepo;
    private final BuyerRepository bRepo;


    public EventService(EventRepository er, EventConverter ec, ApplicationContext ac,EventAnswerRepository eventAnswerRepo, BuyerRepository bRepo){
        super(er, ec, ac);
        this.eRepo=er;
        this.eventAnswerRepo= eventAnswerRepo;
        this.bRepo = bRepo;
    }

    @Override
    public Event construct(Map<String, String> params) {
        Event e= getContext().getBean(Event.class, params);
        return e;
    }

    public List<Event> getAllEvents() {
        return eRepo.findAll();
    }

    public Event getEventById(Long id) {
        return eRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Event not found"));
    }

    @Transactional  
    public Event createEvent(Event event, Authentication authentication) {
        // 1. Ricaviamo l'email (o lo username) dell'utente loggato dall'Authentication
        String email = authentication.getName();
        
        // 2. Cerchiamo il Buyer nel database usando l'email
        Buyer buyer = bRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Buyer not found for email: " + email));
       
        // 3. Associamo il buyer, la data di creazione e salviamo
        event.setBuyer(buyer);
        event.setMessageTime(Instant.now());

        return eRepo.save(event);
    }
    
   @Transactional
    public void addAnswerToEvent(Long eventId, EventAnswer answer, Authentication authentication) {
  
        // 1. Ricaviamo l'email (o lo username) dell'utente loggato dall'Authentication
        String email = authentication.getName();
        
        // 2. Troviamo l'evento nel database
        Event event = eRepo.findById(eventId)
            .orElseThrow(() -> new RuntimeException("We couldn't find the event you are searching for, sorry!"));
     
        // 3. Troviamo il Buyer usando l'email dell'utente autenticato (anzichÃ© l'ID)
        Buyer buyer = bRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("We couldn't find the user for email: " + email));

        // 4. Associazioni e salvataggio
        answer.setEvent(event);
        answer.setBuyer(buyer);
        answer.setCreatedAt(Instant.now());

        eventAnswerRepo.save(answer);
    }

    @Transactional
    public void deleteIfAllowed(Long id, Authentication authentication) {
    Event event = getEventById(id);
    boolean privileged = authentication.getAuthorities().stream()
            .anyMatch(authority ->
                    authority.getAuthority().equals("ROLE_ADMIN") ||
                    authority.getAuthority().equals("ROLE_STAFF"));
    boolean owner = event.getBuyer().getEmail()
            .equalsIgnoreCase(authentication.getName());
    if (!privileged && !owner) {
        throw new AccessDeniedException("Non puoi cancellare questo post");
    }
    eRepo.delete(event);
    }
}