package org.springframework.samples.petclinic.cell;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;

import org.springframework.samples.petclinic.card.CARD_TYPE;
import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Data;

@Data
@Entity
public class Cell extends NamedEntity {
    
    private Integer position;
    
    @Enumerated(EnumType.STRING)
    @NotEmpty
    private CARD_TYPE card;  
}
