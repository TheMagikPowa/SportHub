package com.generation.SportHub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class BuyerDTO extends PersonDTO {

    private boolean active;

    public boolean isActive() { 
        
        return active; 
    }

    //public void setActive(boolean active) { this.active = active; }

}
