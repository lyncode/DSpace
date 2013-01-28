/**
 * 
 */
package org.dspace.springui.security.authenticators;

import org.apache.commons.codec.DecoderException;
import org.dspace.eperson.PasswordHash;
import org.dspace.orm.dao.api.IEpersonDao;
import org.dspace.orm.entity.Eperson;
import org.dspace.services.ConfigurationService;
import org.dspace.springui.security.Authenticator;
import org.dspace.springui.security.DSpaceUser;
import org.dspace.springui.security.authenticators.password.PasswordAuthentication;
import org.dspace.springui.security.error.UnknownAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Password authenticator (for DSpace)
 * 
 * @author João Melo <jmelo@lyncode.com>
 *
 */
public class PasswordAuthenticator implements Authenticator {
	@Autowired ConfigurationService configs;
	@Autowired IEpersonDao epersonDao;
	
	private Boolean isActive = null;
	
	/**
	 * 
	 */
	public PasswordAuthenticator() {
		
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.AuthenticationProvider#authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		if (authentication instanceof PasswordAuthentication) {
			return this.authenticate((PasswordAuthentication) authentication);
		}
		throw new UnknownAuthenticationException();
	}

	private Authentication authenticate(PasswordAuthentication authentication) {
		Eperson e = epersonDao.selectByEmail(authentication.getName());
		if (e != null) {
			try {
				PasswordHash hash = new PasswordHash(e.getDigestAlgorithm(), e.getSalt(), e.getPassword());
				if (hash.matches(authentication.getPassword()))
					return new DSpaceUser(e);
				else
					throw new BadCredentialsException("Username/Password invalid");
			} catch (DecoderException e1) {
				throw new BadCredentialsException("Username/Password invalid");
			}
		} else
			throw new BadCredentialsException("Username/Password invalid");
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.AuthenticationProvider#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return (authentication.isAssignableFrom(PasswordAuthentication.class));
	}

	/* (non-Javadoc)
	 * @see org.dspace.springui.security.Authenticator#isActive()
	 */
	@Override
	public boolean isActive() {
		if (isActive == null) {
			isActive = configs.getPropertyAsType("springui.authentication.password", true);
		}
		return isActive;
	}

}
