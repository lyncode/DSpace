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
import org.dspace.discovery.DiscoverFacetField;
import org.dspace.discovery.configuration.DiscoveryConfigurationParameters;
import org.dspace.discovery.configuration.DiscoveryConfigurationParameters.SORT;
import org.dspace.orm.dao.api.IHandleDao;
import org.dspace.orm.entity.DSpaceObject;
import org.dspace.orm.entity.Handle;
import org.dspace.orm.entity.content.DSpaceObjectType;
import org.dspace.services.DiscoveryService;
import org.dspace.services.discovery.DiscoverException;
import org.dspace.services.discovery.DiscoverQuery;
import org.dspace.services.discovery.DiscoverResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller to search
 * 
 * @author João Melo <jmelo@lyncode.com> and Miguel Pinto <mpinto@lyncode.com>
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
    	
    	String[] authors = request.getParameterValues("author");
    	String[] dates = request.getParameterValues("date");

    	
    	
    	DiscoverFacetField authorField = new DiscoverFacetField("author", DiscoveryConfigurationParameters.TYPE_TEXT, 5, SORT.COUNT);
    	DiscoverQuery itemQuery = new DiscoverQuery();
    	itemQuery.addFacetField(authorField);
    	
    	DiscoverFacetField dtField = new DiscoverFacetField("dateIssued", DiscoveryConfigurationParameters.TYPE_DATE, 5, SORT.COUNT);
    	
    	itemQuery.addFacetField(dtField);
    
    	
    	DiscoverQuery communityQuery = new DiscoverQuery();
    	DiscoverQuery collectionQuery = new DiscoverQuery();
    	if (query != null) {
    		
    		if(authors==null && dates==null)
        	{
    			itemQuery.setQuery(ClientUtils.escapeQueryChars(query)+" AND search.resourcetype:"+DSpaceObjectType.ITEM.getId());
        	}
    		else
    		{
				String queryItems = ClientUtils.escapeQueryChars(query)+" AND search.resourcetype:"+DSpaceObjectType.ITEM.getId();
				
    			if(authors != null && authors.length>0)
    			{
    				System.out.print("Estou no autor!");
    				queryItems = queryItems + " AND";
    			
    				queryItems = queryItems +  insertFilters(authors, itemQuery, "author");
    			}
    			
    			if(dates!=null && dates.length>0)
    			{
    				System.out.print("Estou no dates!");
	    			queryItems = queryItems + " AND";
	    			
	    			queryItems = queryItems +  insertFilters(dates, itemQuery, "dateIssued.year");
	    		}
    			
    			itemQuery.setQuery(queryItems);
    		}
    		
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
    		DiscoverResult items = search.search(itemQuery);
    		DiscoverResult collections = search.search(collectionQuery);
    		DiscoverResult communities = search.search(communityQuery);
    		
    		if (items.isEmpty() && 
    			collections.isEmpty() &&
    			communities.isEmpty())
    			return "search/nothing";
    		
    		map.addAttribute("items", items);
    		map.addAttribute("collections", collections);
    		map.addAttribute("communities", communities);
			
	        return "search/index";	
		} catch (DiscoverException e) {
			log.error("Error while trying to search on discovery", e);
			return "error/search";
		}
    }

	private String insertFilters(String[] items,
			DiscoverQuery itemQuery, String filter) {
		
		String queryItems = new String();
		System.out.print("Filtro"+filter);
		if(items.length > 1)
		{
			queryItems = queryItems + " (";
		}
		
		for (String item : items)
		{
			queryItems = queryItems + " "+filter+":\""+item+"\"";
		}
		
		if(items.length > 1)
		{
			queryItems = queryItems + " )";
		}
		
		return queryItems;
	}
}
