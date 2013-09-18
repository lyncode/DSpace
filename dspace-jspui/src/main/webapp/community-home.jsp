<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Community home JSP
  -
  - Attributes required:
  -    community             - Community to render home page for
  -    collections           - array of Collections in this community
  -    subcommunities        - array of Sub-communities in this community
  -    last.submitted.titles - String[] of titles of recently submitted items
  -    last.submitted.urls   - String[] of URLs of recently submitted items
  -    admin_button - Boolean, show admin 'edit' button
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="org.dspace.app.webui.components.RecentSubmissions" %>

<%@ page import="org.dspace.app.webui.servlet.admin.EditCommunitiesServlet" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.browse.BrowseIndex" %>
<%@ page import="org.dspace.browse.ItemCounter" %>
<%@ page import="org.dspace.content.*" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>


<%
    // Retrieve attributes
    Community community = (Community) request.getAttribute( "community" );
    Collection[] collections =
        (Collection[]) request.getAttribute("collections");
    Community[] subcommunities =
        (Community[]) request.getAttribute("subcommunities");
    
    RecentSubmissions rs = (RecentSubmissions) request.getAttribute("recently.submitted");
    
    String query = request.getParameter("query");
    if (query == null) query = "";
    
    Boolean editor_b = (Boolean)request.getAttribute("editor_button");
    boolean editor_button = (editor_b == null ? false : editor_b.booleanValue());
    Boolean add_b = (Boolean)request.getAttribute("add_button");
    boolean add_button = (add_b == null ? false : add_b.booleanValue());
    Boolean remove_b = (Boolean)request.getAttribute("remove_button");
    boolean remove_button = (remove_b == null ? false : remove_b.booleanValue());

	// get the browse indices
    BrowseIndex[] bis = BrowseIndex.getBrowseIndices();

    // Put the metadata values into guaranteed non-null variables
    String name = community.getMetadata("name");
    String intro = community.getMetadata("introductory_text");
    String copyright = community.getMetadata("copyright_text");
    String sidebar = community.getMetadata("side_bar_text");
    Bitstream logo = community.getLogo();
    
    boolean feedEnabled = ConfigurationManager.getBooleanProperty("webui.feed.enable");
    String feedData = "NONE";
    if (feedEnabled)
    {
        feedData = "comm:" + ConfigurationManager.getProperty("webui.feed.formats");
    }
    
    ItemCounter ic = new ItemCounter(UIUtil.obtainContext(request));
%>

<%@page import="org.dspace.app.webui.servlet.MyDSpaceServlet"%>
<dspace:layout locbar="commLink" title="<%= name %>" feedData="<%= feedData %>">


	<div class="row">
		<div class="col-lg-6">
			<h3>
				<%= name %> 
				<small>
				<% if(ConfigurationManager.getBooleanProperty("webui.strengths.show")) { %>
                <%= ic.getCount(community) %>
				<% } %>
				</small>
			</h3>
		</div>
		<div class="col-lg-6">
			<%  if (logo != null) { %>
        	<img alt="Logo" src="<%= request.getContextPath() %>/retrieve/<%= logo.getID() %>" /> 
			<% } %>
		</div>
	</div>
	
	<div class="row">
		<div class="col-lg-12">
			<%= intro %>
		</div>
	</div>

	<div class="row">
		<div class="col-lg-12">
			<div>
	    		<form action="" method="get">
	    			<input type="hidden" name="location" value="<%= community.getHandle() %>" />
	    			<div class="form-group">
	                  <div class="input-group">
	                    <span class="input-group-addon"><i class="icon-search"></i></span>
	                    <input type="text" name="query" class="form-control" placeholder="<fmt:message key="jsp.search.title"/>" value="<%=query %>" />
	                    <span class="input-group-btn">
	                      <button type="submit" class="btn btn-default" name="submit_search" type="button"><fmt:message key="jsp.general.search.button"/></button>
	                    </span>
	                  </div>
	                </div>
	    		</form>
    		</div>
    		
    		<div>
    			<div class="browse-label"><fmt:message key="jsp.general.orbrowse"/></div>
    			<div class="text-center">
<%
	for (int i = 0; i < bis.length; i++) { String key = "browse.menu." + bis[i].getName();
%>
	                <form class="browse-items" method="get" action="<%= request.getContextPath() %>/handle/<%= community.getHandle() %>/browse">
	                	<input type="hidden" name="type" value="<%= bis[i].getName() %>"/>
						<%-- <input type="hidden" name="community" value="<%= community.getHandle() %>" /> --%>
						<input type="submit" name="submit_browse" class="btn btn-link" value="<fmt:message key="<%= key %>"/>"/>
					</form>
<%	
	}
%>
				</div>
				<div class="clearfix"></div>
    		</div>
		</div>
	</div>

<%
    if (collections.length != 0)
    {
%>
	<div class="row">
		<div class="col-lg-12">
			<h4><fmt:message key="jsp.community-home.heading2"/></h4>  
        	<ul class="list-group">
<%
        for (int i = 0; i < collections.length; i++)
        {
%>
    			<li class="list-group-item">
<%
            if(ConfigurationManager.getBooleanProperty("webui.strengths.show"))
            {
%>
    				<span class="badge"><%= ic.getCount(collections[i]) %></span>
<%
            }
%>
	      			<a href="<%= request.getContextPath() %>/handle/<%= collections[i].getHandle() %>">
	      				<%= collections[i].getMetadata("name") %>
	      			</a>
		
		<%--
	    <% if (remove_button) { %>
	    <td>
	      <form method="post" action="<%=request.getContextPath()%>/tools/edit-communities">
	          <input type="hidden" name="parent_community_id" value="<%= community.getID() %>" />
	          <input type="hidden" name="community_id" value="<%= community.getID() %>" />
	          <input type="hidden" name="collection_id" value="<%= collections[i].getID() %>" />
	          <input type="hidden" name="action" value="<%=EditCommunitiesServlet.START_DELETE_COLLECTION%>" />
	          <input type="image" src="<%= request.getContextPath() %>/image/remove.gif" />
	      </form>
	    </td>
	    <% } %>
	    </tr>
	    </table>
      <p class="collectionDescription"><%= collections[i].getMetadata("short_description") %></p>
       --%>
    			</li>
<%
        }
%>
  			</ul>
 		</div>
  	</div>
<%
    }
%>

<%
    if (subcommunities.length != 0)
    {
%>
	<div class="row">
		<div class="col-lg-12">
			<h4><fmt:message key="jsp.community-home.heading3"/></h4>
			<ul class="list-group">
<%
        for (int j = 0; j < subcommunities.length; j++)
        {
%>
            	<li class="list-group-item">
<%
                if (ConfigurationManager.getBooleanProperty("webui.strengths.show"))
                {
%>
                    <span class="badge"><%= ic.getCount(subcommunities[j]) %></span>
<%
                }
%>
			    
	                <a href="<%= request.getContextPath() %>/handle/<%= subcommunities[j].getHandle() %>">
	                <%= subcommunities[j].getMetadata("name") %></a>
	            <%--
	    		<% if (remove_button) { %>
			    <td>
	                <form method="post" action="<%=request.getContextPath()%>/tools/edit-communities">
			          <input type="hidden" name="parent_community_id" value="<%= community.getID() %>" />
			          <input type="hidden" name="community_id" value="<%= subcommunities[j].getID() %>" />
			          <input type="hidden" name="action" value="<%=EditCommunitiesServlet.START_DELETE_COMMUNITY%>" />
	                  <input type="image" src="<%= request.getContextPath() %>/image/remove.gif" />
	                </form>
			    </td>
	    		<% } %>
			    </tr>
			    </table>
                <p class="collectionDescription"><%= subcommunities[j].getMetadata("short_description") %></p>
                 --%>
            	</li>
<%
        }
%>
        	</ul>
 		</div>
  	</div>
<%
    }
%>

  <p class="copyrightText"><%= copyright %></p>

  <dspace:sidebar>
    
    <% if(editor_button || add_button)  // edit button(s)
    { %>

		<form name="editbutton" method="post" action="<%=request.getContextPath()%>/tools/edit-communities">
			<input type="hidden" name="community_id" value="<%= community.getID() %>" />
			<input type="hidden" name="action" value="<%=EditCommunitiesServlet.START_EDIT_COMMUNITY%>" />
		</form>
		<form name="createCollection" method="post" action="<%=request.getContextPath()%>/tools/collection-wizard">
			<input type="hidden" name="community_id" value="<%= community.getID() %>" />
		</form>
		<form name="createSubCommunity" method="post" action="<%=request.getContextPath()%>/tools/edit-communities">
			<input type="hidden" name="action" value="<%= EditCommunitiesServlet.START_CREATE_COMMUNITY%>" />
			<input type="hidden" name="parent_community_id" value="<%= community.getID() %>" />
	                    <%--<input type="submit" name="submit" value="Create Sub-community" />--%>
	    </form>
	    <form name="exportComm" method="post" action="<%=request.getContextPath()%>/mydspace">
                  <input type="hidden" name="community_id" value="<%= community.getID() %>" />
                  <input type="hidden" name="step" value="<%= MyDSpaceServlet.REQUEST_EXPORT_ARCHIVE %>" />
        </form>
        <form name="migrateArchive" method="post" action="<%=request.getContextPath()%>/mydspace">
                <input type="hidden" name="community_id" value="<%= community.getID() %>" />
                <input type="hidden" name="step" value="<%= MyDSpaceServlet.REQUEST_MIGRATE_ARCHIVE %>" />
        </form>
        <form name="metadataExport" method="post" action="<%=request.getContextPath()%>/dspace-admin/metadataexport">
                 <input type="hidden" name="handle" value="<%= community.getHandle() %>" />
        </form>
    
    	<h4><fmt:message key="jsp.admintools"/></h4>
		<ul class="nav nav-pills nav-stacked">
<% if( editor_button ) { %>
            <li>
                  <a href="#" onclick="document.editbutton.submit();"><fmt:message key="jsp.general.edit.button"/></a>
            </li>
<% } %>
<% if(add_button) { %>
			<li>
				<a href="#" onclick="document.createCollection.submit();"><fmt:message key="jsp.community-home.create1.button"/></a>
			</li>
			<li>
				<a href="#" onclick="document.createSubCommunity.submit();"><fmt:message key="jsp.community-home.create2.button"/></a>
			</li>
<% } %>
<% if( editor_button ) { %>
            <li>
            	<a href="#" onclick="document.exportComm.submit();"><fmt:message key="jsp.mydspace.request.export.community"/></a>
            </li>
            <li>
            	<a href="#" onclick="document.migrateArchive.submit();"><fmt:message key="jsp.mydspace.request.export.migratecommunity"/></a>
            </li>
            <li>
            	<a href="#" onclick="document.metadataExport.submit();"><fmt:message key="jsp.general.metadataexport.button"/></a>
            </li>
<% } %>
            <li>
            	<dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.site-admin\")%>"><fmt:message key="jsp.adminhelp"/></dspace:popup>
            </li>
		</ul>
		
		
<% } %>
   
    <%-- Recently Submitted items --%>
	<h3><fmt:message key="jsp.community-home.recentsub"/></h3>
<%
	if (rs != null)
	{
		Item[] items = rs.getRecentSubmissions();
		for (int i = 0; i < items.length; i++)
		{
			DCValue[] dcv = items[i].getMetadata("dc", "title", null, Item.ANY);
			String displayTitle = "Untitled";
			if (dcv != null)
			{
				if (dcv.length > 0)
				{
					displayTitle = dcv[0].value;
				}
			}
			%><p class="recentItem"><a href="<%= request.getContextPath() %>/handle/<%= items[i].getHandle() %>"><%= displayTitle %></a></p><%
		}
	}
%>
    <p>&nbsp;</p>    
<%
    if(feedEnabled)
    {
%>
    <center>
    <h4><fmt:message key="jsp.community-home.feeds"/></h4>
<%
    	String[] fmts = feedData.substring(5).split(",");
    	String icon = null;
    	int width = 0;
    	for (int j = 0; j < fmts.length; j++)
    	{
    		if ("rss_1.0".equals(fmts[j]))
    		{
    		   icon = "rss1.gif";
    		   width = 80;
    		}
    		else if ("rss_2.0".equals(fmts[j]))
    		{
    		   icon = "rss2.gif";
    		   width = 80;
    		}
    		else
    	    {
    	       icon = "rss.gif";
    	       width = 36;
    	    }
%>
    <a href="<%= request.getContextPath() %>/feed/<%= fmts[j] %>/<%= community.getHandle() %>"><img src="<%= request.getContextPath() %>/image/<%= icon %>" alt="RSS Feed" width="<%= width %>" height="15" vspace="3" border="0" /></a>
<%
    	}
%>
    </center>
<%
    }
%>

    <%= sidebar %>
	<%@ include file="discovery/static-sidebar-facet.jsp" %>
  </dspace:sidebar>


	<div class="row">
		<div class="col-lg-12">
			<div class="alert alert-info text-center">
					<a class="btn btn-primary" href="<%= request.getContextPath() %>/handle/<%= community.getHandle() %>/statistics"><fmt:message key="jsp.community-home.display-statistics"/></a>
			</div>
		</div>
	</div>


</dspace:layout>

