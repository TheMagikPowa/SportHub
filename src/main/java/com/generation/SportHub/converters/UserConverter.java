package com.generation.SportHub.converters;

import org.springframework.stereotype.Service;

import com.generation.SportHub.dto.UserDTO;
import com.generation.SportHub.entity.User;

@Service
public class UserConverter implements GenericConverter<UserDTO, User> {

    @Override
    public UserDTO fromEntityToDto(User entity) {
        return new UserDTO(
            entity.getId(),
            entity.getEmail(),
            entity.getCreateTime()
        );
    }

    @Override
    public User fromDtoToEntity(UserDTO dto) {
        User entity= new User();
        entity.setEmail(dto.email());
        return entity;
    }
    
}
