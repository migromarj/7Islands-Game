package org.springframework.samples.petclinic.achievement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Data;

@Data
@Entity
@Table(name="achievements")
public class Achievement extends NamedEntity {
    
    @Column(name="name")
    @NotEmpty
    private String name;
    
    @Column(name="description")
    @NotEmpty
    private String description;
    
    @Column(name="icon")
    @NotEmpty
    private String icon;
    
    @Column(name="min_value")
    @NotEmpty
    private Integer minValue;
    
    @Column(name="achievement_type")
    @Enumerated(EnumType.STRING)
    @NotEmpty
    private ACHIEVEMENT_TYPE achievementType;
    
    @Column(name="parameter")
    @Enumerated(EnumType.STRING)
    @NotEmpty
    private PARAMETER parameter;
}