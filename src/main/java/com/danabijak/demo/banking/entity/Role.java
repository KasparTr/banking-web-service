package com.danabijak.demo.banking.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
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