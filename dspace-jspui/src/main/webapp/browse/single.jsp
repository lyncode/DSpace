<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - 
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="org.dspace.browse.BrowseInfo" %>
<%@ page import="org.dspace.browse.BrowseIndex" %>
<%@ page import="org.dspace.content.Collection" %>
<%@ page import="org.dspace.content.Community" %>
<%@ page import="org.dspace.content.DCDate" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="org.dspace.core.Utils" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>

<%
    request.setAttribute("LanguageSwitch", "hide");

	//First, get the browse info object
	BrowseInfo bi = (BrowseInfo) request.getAttribute("browse.info");
	BrowseIndex bix = bi.getBrowseIndex();

	//values used by the header
	String scope = "";
	String type = "";

	Community community = null;
	Collection collection = null;
	if (bi.inCommunity())
	{
		community = (Community) bi.getBrowseContainer();
	}
	if (bi.inCollection())
	{
		collection = (Collection) bi.getBrowseContainer();
	}
	
	if (community != null)
	{
		scope = "\"" + community.getMetadata("name") + "\"";
	}
	if (collection != null)
	{
		scope = "\"" + collection.getMetadata("name") + "\"";
	}
	
	type = bix.getName();
	
	//FIXME: so this can probably be placed into the Messages.properties file at some point
	// String header = "Browsing " + scope + " by " + type;
	
	// get the values together for reporting on the browse values
	// String range = "Showing results " + bi.getStart() + " to " + bi.getFinish() + " of " + bi.getTotal();
	
	// prepare the next and previous links
	String linkBase = request.getContextPath() + "/";
	if (collection != null)
	{
		linkBase = linkBase + "handle/" + collection.getHandle() + "/";
	}
	if (community != null)
	{
		linkBase = linkBase + "handle/" + community.getHandle() + "/";
	}
	
	String direction = (bi.isAscending() ? "ASC" : "DESC");
	String sharedLink = linkBase + "browse?type=" + URLEncoder.encode(bix.getName()) + 
						"&amp;order=" + URLEncoder.encode(direction) + 
						"&amp;rpp=" + URLEncoder.encode(Integer.toString(bi.getResultsPerPage()));
	
	// prepare the next and previous links
	String next = sharedLink;
	String prev = sharedLink;
	
	if (bi.hasNextPage())
    {
        next = next + "&amp;offset=" + bi.getNextOffset();
    }

	if (bi.hasPrevPage())
    {
        prev = prev + "&amp;offset=" + bi.getPrevOffset();
    }

	// prepare a url for use by form actions
	String formaction = request.getContextPath() + "/";
	if (collection != null)
	{
		formaction = formaction + "handle/" + collection.getHandle() + "/";
	}
	if (community != null)
	{
		formaction = formaction + "handle/" + community.getHandle() + "/";
	}
	formaction = formaction + "browse";
	
	String ascSelected = (bi.isAscending() ? "selected=\"selected\"" : "");
	String descSelected = (bi.isAscending() ? "" : "selected=\"selected\"");
	int rpp = bi.getResultsPerPage();
	
//	 the message key for the type
	String typeKey = "browse.type.metadata." + bix.getName();
%>

<dspace:layout titlekey="browse.page-title">

	<div class="row">
		<div class="col-lg-12">
			<%-- Build the header (careful use of spacing) --%>
			<h3>
				<fmt:message key="browse.single.header">
					<fmt:param value="<%= scope %>"/>
				</fmt:message> 
				<small><fmt:message key="<%= typeKey %>"/></small>
			</h3>
		</div>
	</div>

	<hr />
	
	<div class="row">
		<div class="col-lg-12">
			<form class="text-center" method="get" action="<%= formaction %>">
			<input type="hidden" name="type" value="<%= bix.getName() %>"/>
			<input type="hidden" name="order" value="<%= direction %>"/>
			<input type="hidden" name="rpp" value="<%= rpp %>"/>
<%
		if (bi.hasAuthority()) {
%>
				<input type="hidden" name="authority" value="<%=bi.getAuthority() %>"/>
<%
		} else if (bi.hasValue()) {
%>
				<input type="hidden" name="value" value="<%= bi.getValue() %>"/>
<%
		}
%>
<%
	if (bix.isDate())
	{
%>
				<div class="form-inline">
				 	<label><fmt:message key="browse.nav.date.jump"/></label>
					<div class="form-group">
						<select class="form-control" name="year">
	                                <option selected="selected" value="-1"><fmt:message key="browse.nav.year"/></option>
<%
			int thisYear = DCDate.getCurrent().getYear();
			for (int i = thisYear; i >= 1990; i--)
			{
%>
	                                <option><%= i %></option>
<%
			}
%>
	                                <option>1985</option>
	                                <option>1980</option>
	                                <option>1975</option>
	                                <option>1970</option>
	                                <option>1960</option>
	                                <option>1950</option>
						</select>
					</div>
					<div class="form-group">
						<select class="form-control" name="month">
	                                <option selected="selected" value="-1"><fmt:message key="browse.nav.month"/></option>
<%
			for (int i = 1; i <= 12; i++)
			{
%>
	                                <option value="<%= i %>"><%= DCDate.getMonthName(i, UIUtil.getSessionLocale(request)) %></option>
<%
			}
%>
						</select>
					</div>
		            <button class="btn btn-default" type="submit"><fmt:message key="browse.nav.go"/></button>
				</div>
<%
	}
	
	// If we are not browsing by a date, render the string selection header //
	else
	{
%>	
				<div>
					<label><fmt:message key="browse.nav.jump"/></label>
					<a href="<%= sharedLink %>&amp;starts_with=0">0-9</a>
<%
	    for (char c = 'A'; c <= 'Z'; c++)
	    {
%>
	                <a href="<%= sharedLink %>&amp;starts_with=<%= c %>"><%= c %></a>
<%
	    }
%>
					<div class="form-inline">
	    				<label><fmt:message key="browse.nav.enter"/></label>
	    				<div class="form-group">
	    					<input class="form-control" type="text" name="starts_with">
	    				</div>
	    				<button class="btn btn-default" type="submit"><fmt:message key="browse.nav.go"/></button>
					</div>
				</div>
<%
	}
%>

			</form>
		</div>
	</div>
	
	<hr />

	<div class="row">
		<div class="col-lg-12">
			<form class="form-inline text-center" method="get" action="<%= formaction %>">
				<input type="hidden" name="type" value="<%= bix.getName() %>"/>
				<label><fmt:message key="browse.single.order"/></label>
				<div class="form-group">
					<select class="form-control" name="order">
						<option value="ASC" <%= ascSelected %>><fmt:message key="browse.order.asc" /></option>
						<option value="DESC" <%= descSelected %>><fmt:message key="browse.order.desc" /></option>
					</select>
				</div>
				
				<label><fmt:message key="browse.single.rpp"/></label>
				<div class="form-group">
					<select class="form-control" name="rpp">
			<%
				for (int i = 5; i <= 100 ; i += 5)
				{
					String selected = (i == rpp ? "selected=\"selected\"" : "");
			%>	
						<option value="<%= i %>" <%= selected %>><%= i %></option>
			<%
				}
			%>
					</select>
				</div>
				
				<button class="btn btn-default" type="submit" name="submit_browse"><fmt:message key="jsp.general.update"/></button>
			</form>
		</div>
	</div>
	
	<hr />

	<%-- give us the top report on what we are looking at --%>
	<div class="row">
		<div class="col-lg-12">
			<div class="well well-sm text-center">
				<fmt:message key="browse.single.range">
					<fmt:param value="<%= Integer.toString(bi.getStart()) %>"/>
					<fmt:param value="<%= Integer.toString(bi.getFinish()) %>"/>
					<fmt:param value="<%= Integer.toString(bi.getTotal()) %>"/>
				</fmt:message>
			</div>
		</div>
	</div>

	<%--  do the top previous and next page links --%>
	<div align="center">
<% 
	if (bi.hasPrevPage())
	{
%>
	<a href="<%= prev %>"><fmt:message key="browse.single.prev"/></a>&nbsp;
<%
	}
%>

<%
	if (bi.hasNextPage())
	{
%>
	&nbsp;<a href="<%= next %>"><fmt:message key="browse.single.next"/></a>
<%
	}
%>
	</div>


	<%-- THE RESULTS --%>
    <table align="center" class="table table-hover table-striped" summary="This table displays a list of results">
    	<thead>
    		<th><fmt:message key="<%= typeKey %>"/></th>
    		<th>Count</th>
    	</thead>
    	
<%
    // Row: toggles between Odd and Even
    String row = "odd";
    String[][] results = bi.getStringResults();

    for (int i = 0; i < results.length; i++)
    {
%>
            <tr>
                <td>
                    <a href="<%= sharedLink %><% if (results[i][1] != null) { %>&amp;authority=<%= URLEncoder.encode(results[i][1], "UTF-8") %>" class="authority <%= bix.getName() %>"><%= Utils.addEntities(results[i][0]) %></a> <% } else { %>&amp;value=<%= URLEncoder.encode(results[i][0], "UTF-8") %>"><%= Utils.addEntities(results[i][0]) %></a> <% } %>
                </td>
                <td><%= StringUtils.isNotBlank(results[i][2])?results[i][2]:"0"%></td>
            </tr>
<%
        row = ( row.equals( "odd" ) ? "even" : "odd" );
    }
%>
        </table>

	<%--  do the bottom previous and next page links --%>
	<div align="center">
<% 
	if (bi.hasPrevPage())
	{
%>
	<a href="<%= prev %>"><fmt:message key="browse.single.prev"/></a>&nbsp;
<%
	}
%>

<%
	if (bi.hasNextPage())
	{
%>
	&nbsp;<a href="<%= next %>"><fmt:message key="browse.single.next"/></a>
<%
	}
%>
	</div>

	<%-- dump the results for debug (uncomment to enable) --%>
	<%-- 
	<!-- <%= bi.toString() %> -->
    --%>
    
</dspace:layout>
