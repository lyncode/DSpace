/**
 * 
 */
package org.dspace.springui.security.error;

import org.springframework.security.core.AuthenticationException;

/**
 * Occurs when the authentication service isn't able to find a suitable authenticator to
 * answer the authentication request. 
 * 
 * @author João Melo <jmelo@lyncode.com>
 *
 */
public class UnknownAuthenticatorException extends AuthenticationException {
	private static final long serialVersionUID = -7238985955750637093L;

	public UnknownAuthenticatorException () {
		super("Unknown authentication method");
	}
	
	/**
	 * @param msg
	 */
	public UnknownAuthenticatorException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param msg
	 * @param t
	 */
	public UnknownAuthenticatorException(String msg, Throwable t) {
		super(msg, t);
		// TODO Auto-generated constructor stub
	}

}
