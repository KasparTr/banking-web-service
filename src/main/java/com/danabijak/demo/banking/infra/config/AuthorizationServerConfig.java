package com.danabijak.demo.banking.infra.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	/*
	 * defines the security constraints on the token endpoint.
	 * 
	*/
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.checkTokenAccess("isAuthenticated()");
	}

	/*
	 * A configurer that defines the client details service. 
	 * Client details can be initialized, or you can just refer to an existing store!
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		//TODO: Refer to existing store of users
		clients.inMemory().withClient("my-trusted-client")
		.authorizedGrantTypes("client_credentials","password")
		.authorities("ROLE_CLIENT","ROLE_TRUSTED_CLIENT")
		.scopes("read","write","trust")
		.resourceIds("oauth2-resource")
		.accessTokenValiditySeconds(5000)
		.secret(passwordEncoder.encode("secret"));
	}

	
	/*
	 * defines the authorization and token endpoints and the token services.
	*/
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager);
	}
	
}
