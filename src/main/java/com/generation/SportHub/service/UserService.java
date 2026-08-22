package com.generation.SportHub.service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.generation.SportHub.converters.PersonConverter;
import com.generation.SportHub.converters.UserConverter;
import com.generation.SportHub.dto.UserDTO;
import com.generation.SportHub.entity.User;
import com.generation.SportHub.repository.UserRepository;

@Service
public class UserService extends GenericService<Long, User, UserDTO, UserConverter, UserRepository>{

    

    public UserService(
        UserRepository repository, 
        ApplicationContext context, 
        UserConverter converter, PersonConverter personConverter) {
        super(repository, converter, context);
        
    }
    @Override
    public User construct(Map<String, String> params) {
        User u= getContext().getBean(User.class, params);
        return u;
    }

    public User getByEmailAndPassword(String email, String password){
        Optional <User> u= getRepository().findByEmailIgnoreCase(email);

        try {
            return u.orElseThrow();
        } catch (NoSuchElementException e) {
            
            return null;
        }
    }
    

}
