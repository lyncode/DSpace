/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.security;

import java.util.List;

import org.dspace.springui.security.error.UnknownAuthenticatorException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * This is the DSpace authentication provider, it acts like an abstraction of
 * real DSpace providers. With this it's possible to add multiple providers
 * and controlling the complete process pipeline (instead of diving into the Spring one).
 * 
 * @author João Melo <jmelo@lyncode.com>
 *
 */
public class DSpaceAuthenticationService implements AuthenticationProvider {
	private List<Authenticator> providers;
	
	public void setProviders (List<Authenticator> providers) {
		this.providers = providers;
	}
	
	private List<Authenticator> getProviders () {
		return providers;
	}
	
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		for (Authenticator pvd : this.getProviders())
			if (pvd.isActive() && pvd.supports(authentication.getClass()))
				return pvd.authenticate(authentication);
		
		throw new UnknownAuthenticatorException();
	}

	@Override
	public boolean supports(Class<?> authentication) {
		for (Authenticator pvd : this.getProviders())
			if (pvd.isActive() && pvd.supports(authentication))
				return true;
		return false;
	}
	
}
