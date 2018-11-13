package com.danabijak.demo.banking.infra.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import lombok.Getter;

@Entity
@Getter
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class BaseEntity implements Serializable {
    @Id
    @GeneratedValue
    private long id;
    
    public long getId() {
    	return id;
    }
}