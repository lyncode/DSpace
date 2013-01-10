/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.invokers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dspace.orm.dao.api.ICommunityDao;
import org.springframework.beans.factory.annotation.Autowired;

public class CommunityRelated {
    private static Logger log = LogManager.getLogger(CommunityRelated.class);
    @Autowired
    HttpServletRequest request;
    @Autowired
    ICommunityDao comRep;

    public void getCommunityList(Map<String, Object> model) {
    	log.debug("Communities listed");
        model.put("communities", comRep.selectTop());
    }
}
