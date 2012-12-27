/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.wrappers;

import org.dspace.eperson.EPerson;

public class EPersonWrapper {
    private boolean isLogged;
    private EPerson person;

    public EPersonWrapper() {
        this.isLogged = false;
    }

    public EPersonWrapper(EPerson ep) {
        this.person = ep;
        this.isLogged = true;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean isLogged) {
        this.isLogged = isLogged;
    }

    public EPerson getPerson() {
        return person;
    }

    public void setPerson(EPerson person) {
        this.person = person;
    }

    public boolean isGuest() {
        return (!this.isLogged());
    }
}
