package com.danabijak.demo.banking.users.http;

public class UserClientResponse {
	public final String username;
	public final String token_link;
	public final long id;
	
	public UserClientResponse(long id, String username, String token_link) {
		super();
		this.username = username;
		this.token_link = token_link;
		this.id = id;
	}
	
	

}
