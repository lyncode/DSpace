/**
 * 
 */
package org.dspace.springui.security.authenticators.password;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * Password authentication request
 * 
 * @author João Melo <jmelo@lyncode.com>
 *
 */
public class PasswordAuthentication implements Authentication {
	private static final long serialVersionUID = 5162831980239633255L;
	private String email;
	private String password;
	private Object details = null;
	
	public PasswordAuthentication(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	public String getEmail () {
		return this.email;
	}
	
	public String getPassword () {
		return this.password;
	}

	/* (non-Javadoc)
	 * @see java.security.Principal#getName()
	 */
	@Override
	public String getName() {
		return this.email;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#getAuthorities()
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null; // No authorities at the moment
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#getCredentials()
	 */
	@Override
	public Object getCredentials() {
		return this.password;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#getDetails()
	 */
	@Override
	public Object getDetails() {
		return details;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#getPrincipal()
	 */
	@Override
	public Object getPrincipal() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#isAuthenticated()
	 */
	@Override
	public boolean isAuthenticated() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.Authentication#setAuthenticated(boolean)
	 */
	@Override
	public void setAuthenticated(boolean isAuthenticated)
			throws IllegalArgumentException {

	}

	public void setDetails(Object buildDetails) {
		this.details = buildDetails;
	}

}
