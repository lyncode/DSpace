/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.wrappers;

import org.dspace.content.Community;

public class CommunityWrapper {
    private Community comm;

    public CommunityWrapper(Community c) {
        this.comm = c;
    }

    public Community getInner() {
        return comm;
    }

    public String getIntroductoryText() {
        return comm.getMetadata("introductory_text");
    }
}
