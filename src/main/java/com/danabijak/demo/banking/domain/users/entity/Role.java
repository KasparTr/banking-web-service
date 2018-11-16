package com.danabijak.demo.banking.domain.users.entity;

import javax.persistence.Entity;

import com.danabijak.demo.banking.domain.transactions.entity.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Role extends BaseEntity{
	
	public enum NAME {
	    USER, ACTUATOR, ADMIN, BANK
	} 

    private NAME name;

    Role(){}
    
	public Role(NAME name) {
		super();
		this.name = name;
	}

	public NAME getName() {
		return name;
	}

	public void setName(NAME name) {
		this.name = name;
	}
    
    
    
}