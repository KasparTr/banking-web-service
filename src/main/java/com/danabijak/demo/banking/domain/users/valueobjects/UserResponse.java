package com.danabijak.demo.banking.domain.users.valueobjects;

public class UserResponse {
	public final String username;
	public final String token_link;
	public final long id;
	public final long attachedAccountId;
	
	public UserResponse(long id, String username, String token_link, long accountId) {
		super();
		this.username = username;
		this.token_link = token_link;
		this.id = id;
		this.attachedAccountId = accountId;
	}
	
	

}
