/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.controllers;


import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.dspace.orm.dao.api.IHandleDao;
import org.dspace.orm.entity.DSpaceObject;
import org.dspace.orm.entity.Handle;
import org.dspace.orm.entity.content.DSpaceObjectType;
import org.dspace.services.DiscoveryService;
import org.dspace.services.discovery.DiscoverException;
import org.dspace.services.discovery.DiscoverQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller to search
 * 
 * @author João Melo <jmelo@lyncode.com>
 */
@Controller
@DependsOn("dspaceServices")
public class SearchController {
	private static Logger log = LogManager.getLogger(SearchController.class);
	@Autowired DiscoveryService search;
	@Autowired IHandleDao handleDao;
	
    @RequestMapping(value = "/search")
    public String searchAction(HttpServletRequest request, ModelMap map) {
    	String query = request.getParameter("query");
    	String handle = request.getParameter("handle");

    	DiscoverQuery itemQuery = new DiscoverQuery();
    	DiscoverQuery communityQuery = new DiscoverQuery();
    	DiscoverQuery collectionQuery = new DiscoverQuery();
    	if (query != null) {
    		itemQuery.setQuery(ClientUtils.escapeQueryChars(query)+" AND search.resourcetype:"+DSpaceObjectType.ITEM.getId());
    		communityQuery.setQuery(ClientUtils.escapeQueryChars(query)+" AND search.resourcetype:"+DSpaceObjectType.COMMUNITY.getId());
    		collectionQuery.setQuery(ClientUtils.escapeQueryChars(query)+" AND search.resourcetype:"+DSpaceObjectType.COLLECTION.getId());
    	}
    	else {
    		itemQuery.setQuery("search.resourcetype:"+DSpaceObjectType.ITEM.getId());
    		communityQuery.setQuery("search.resourcetype:"+DSpaceObjectType.COMMUNITY.getId());
    		collectionQuery.setQuery("search.resourcetype:"+DSpaceObjectType.COLLECTION.getId());
    	}
    	
    	
    	if (handle != null) {
    		// Restriction set
    		Handle h = handleDao.selectByHandle(handle);
    		if (h != null) {
    			DSpaceObject obj = h.toDSpaceObject();
    			itemQuery.setDSpaceObject(obj);
    			collectionQuery.setDSpaceObject(obj);
    			communityQuery.setDSpaceObject(obj);
    			map.addAttribute("object", obj);
    		}
    	}
    	
    	try {
    		map.addAttribute("items", search.search(itemQuery));
    		map.addAttribute("collections", search.search(collectionQuery));
    		map.addAttribute("communities", search.search(communityQuery));
			
	        return "search/index";	
		} catch (DiscoverException e) {
			log.error("Error while trying to search on discovery", e);
			return "error/search";
		}
    }
}
