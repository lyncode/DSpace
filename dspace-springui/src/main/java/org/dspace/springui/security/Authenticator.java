/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.security;

import org.springframework.security.authentication.AuthenticationProvider;

/**
 * An alias.
 * 
 * @author João Melo <jmelo@lyncode.com>
 *
 */
public interface Authenticator extends AuthenticationProvider {
	public boolean isActive();
}
