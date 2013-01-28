/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dspace.orm.entity.Eperson;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * This class implements an authentication object, that is,
 * an object used by the Spring Framework and stored in the session.
 * 
 * It acts like a wrapper for the eperson object, and will also replace most of the
 * Context (DSpace core class) functionality.
 * 
 * @author João Melo <jmelo@lyncode.com>
 *
 */
public class DSpaceUser implements Authentication {
	private static final long serialVersionUID = 3600900416727471521L;
    private Eperson person;
    private List<DSpaceRole> roles;

    public DSpaceUser() {
        this.roles = new ArrayList<DSpaceRole>();
        this.roles.add(DSpaceRole.GUEST);
    }

    public DSpaceUser(Eperson ep) {
        this.person = ep;
        this.roles = new ArrayList<DSpaceRole>();
        // TODO: Add roles
        this.roles.add(DSpaceRole.ADMIN);
    }

    public Eperson getPerson() {
        return person;
    }
    
    public boolean isGuest() {
        return (this.roles.contains(DSpaceRole.GUEST));
    }
    
    public boolean isAdmin () {
    	return (this.roles.contains(DSpaceRole.ADMIN));
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles;
	}

	@Override
	public String getName() {
		if (!this.isGuest())
			return this.getPerson().getEmail();
		return null;
	}

	@Override
	public Object getCredentials() {
		return null; // not required
	}

	@Override
	public Object getDetails() {
		return null; // Not required
	}

	@Override
	public Object getPrincipal() {
		return null; // Not required
	}

	@Override
	public boolean isAuthenticated() {
		return !this.isGuest();
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated)
			throws IllegalArgumentException {
		throw new IllegalArgumentException("Cannot force the authenticated state");
	}
}
