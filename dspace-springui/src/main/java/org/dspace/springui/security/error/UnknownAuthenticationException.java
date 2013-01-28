/**
 * 
 */
package org.dspace.springui.security.error;

import org.springframework.security.core.AuthenticationException;

/**
 * Occurs when the authentication request is not handled by the current authenticator
 * 
 * @author João Melo <jmelo@lyncode.com>
 *
 */
public class UnknownAuthenticationException extends AuthenticationException {
	private static final long serialVersionUID = -7238985955750637093L;

	public UnknownAuthenticationException () {
		super("Unknown authentication request");
	}
	
	/**
	 * @param msg
	 */
	public UnknownAuthenticationException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param msg
	 * @param t
	 */
	public UnknownAuthenticationException(String msg, Throwable t) {
		super(msg, t);
		// TODO Auto-generated constructor stub
	}

}
