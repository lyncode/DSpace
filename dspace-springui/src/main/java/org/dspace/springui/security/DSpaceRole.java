/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * Used to implement a GrantedAuthority, could be seen as a ROLE.
 * 
 * One class to Role them all! :D
 * 
 * @author João Melo <jmelo@lyncode.com>
 *
 */
public class DSpaceRole implements GrantedAuthority {
	private static final long serialVersionUID = -7757538254361121147L;
	public static DSpaceRole ADMIN = new DSpaceRole(PredefinedRole.ADMIN);
	public static DSpaceRole GUEST = new DSpaceRole(PredefinedRole.GUEST);
	
	private String role;
	
	public DSpaceRole (PredefinedRole role) {
		this.role = "ROLE_"+role.name();
	}
	
	@Override
	public String getAuthority() {
		return role;
	}
	
	public enum PredefinedRole {
		ADMIN,
		GUEST
	}
}
