<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Collection home JSP
  -
  - Attributes required:
  -    collection  - Collection to render home page for
  -    community   - Community this collection is in
  -    last.submitted.titles - String[], titles of recent submissions
  -    last.submitted.urls   - String[], corresponding URLs
  -    logged.in  - Boolean, true if a user is logged in
  -    subscribed - Boolean, true if user is subscribed to this collection
  -    admin_button - Boolean, show admin 'edit' button
  -    editor_button - Boolean, show collection editor (edit submitters, item mapping) buttons
  -    show.items - Boolean, show item list
  -    browse.info - BrowseInfo, item list
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="org.dspace.app.webui.components.RecentSubmissions" %>

<%@ page import="org.dspace.app.webui.servlet.admin.EditCommunitiesServlet" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.browse.BrowseIndex" %>
<%@ page import="org.dspace.browse.BrowseInfo" %>
<%@ page import="org.dspace.browse.ItemCounter"%>
<%@ page import="org.dspace.content.*"%>
<%@ page import="org.dspace.core.ConfigurationManager"%>
<%@ page import="org.dspace.core.Context" %>
<%@ page import="org.dspace.eperson.Group"     %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="java.net.URLEncoder" %>


<%
    // Retrieve attributes
    Collection collection = (Collection) request.getAttribute("collection");
    Community  community  = (Community) request.getAttribute("community");
    Group      submitters = (Group) request.getAttribute("submitters");

    RecentSubmissions rs = (RecentSubmissions) request.getAttribute("recently.submitted");
    
    boolean loggedIn =
        ((Boolean) request.getAttribute("logged.in")).booleanValue();
    boolean subscribed =
        ((Boolean) request.getAttribute("subscribed")).booleanValue();
    Boolean admin_b = (Boolean)request.getAttribute("admin_button");
    boolean admin_button = (admin_b == null ? false : admin_b.booleanValue());

    Boolean editor_b      = (Boolean)request.getAttribute("editor_button");
    boolean editor_button = (editor_b == null ? false : editor_b.booleanValue());

    Boolean submit_b      = (Boolean)request.getAttribute("can_submit_button");
    boolean submit_button = (submit_b == null ? false : submit_b.booleanValue());


    String query = request.getParameter("query");
    if (query == null) query = "";
    
	// get the browse indices
    BrowseIndex[] bis = BrowseIndex.getBrowseIndices();

    // Put the metadata values into guaranteed non-null variables
    String name = collection.getMetadata("name");
    String intro = collection.getMetadata("introductory_text");
    if (intro == null)
    {
        intro = "";
    }
    String copyright = collection.getMetadata("copyright_text");
    if (copyright == null)
    {
        copyright = "";
    }
    String sidebar = collection.getMetadata("side_bar_text");
    if(sidebar == null)
    {
        sidebar = "";
    }

    String communityName = community.getMetadata("name");
    String communityLink = "/handle/" + community.getHandle();

    Bitstream logo = collection.getLogo();
    
    boolean feedEnabled = ConfigurationManager.getBooleanProperty("webui.feed.enable");
    String feedData = "NONE";
    if (feedEnabled)
    {
        feedData = "coll:" + ConfigurationManager.getProperty("webui.feed.formats");
    }
    
    ItemCounter ic = new ItemCounter(UIUtil.obtainContext(request));

    Boolean showItems = (Boolean)request.getAttribute("show.items");
    boolean show_items = showItems != null ? showItems.booleanValue() : false;
%>

<%@page import="org.dspace.app.webui.servlet.MyDSpaceServlet"%>
<dspace:layout locbar="commLink" title="<%= name %>" feedData="<%= feedData %>">


	<div class="row">
		<div class="col-lg-6">
			<h3>
				<%= name %> 
				<small>
				<% if(ConfigurationManager.getBooleanProperty("webui.strengths.show")) { %>
                <%= ic.getCount(collection) %>
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
	    			<input type="hidden" name="location" value="<%= collection.getHandle() %>" />
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
	                <form class="browse-items" method="get" action="<%= request.getContextPath() %>/handle/<%= collection.getHandle() %>/browse">
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

<% if (show_items)
   {
        BrowseInfo bi = (BrowseInfo) request.getAttribute("browse.info");
        BrowseIndex bix = bi.getBrowseIndex();

        // prepare the next and previous links
        String linkBase = request.getContextPath() + "/handle/" + collection.getHandle();
        
        String next = linkBase;
        String prev = linkBase;
        
        if (bi.hasNextPage())
        {
            next = next + "?offset=" + bi.getNextOffset();
        }
        
        if (bi.hasPrevPage())
        {
            prev = prev + "?offset=" + bi.getPrevOffset();
        }

        String bi_name_key = "browse.menu." + bi.getSortOption().getName();
        String so_name_key = "browse.order." + (bi.isAscending() ? "asc" : "desc");
%>

	<div class="row">
		<div class="col-lg-12">
			<div class="well well-sm">
		        <fmt:message key="jsp.collection-home.content.range">
		            <fmt:param value="${bi_name}"/>
		            <fmt:param value="${so_name}"/>
		            <fmt:param value="<%= Integer.toString(bi.getStart()) %>"/>
		            <fmt:param value="<%= Integer.toString(bi.getFinish()) %>"/>
		            <fmt:param value="<%= Integer.toString(bi.getTotal()) %>"/>
		        </fmt:message>
		    </div>
		</div>
	</div>
    <%-- give us the top report on what we are looking at
    <fmt:message var="bi_name" key="<%= bi_name_key %>"/>
    <fmt:message var="so_name" key="<%= so_name_key %>"/> --%>

	<div class="row">
		<div class="col-lg-12">
			<ul class="pager">
<% 
      if (bi.hasPrevPage())
      {
%>
                <li><a href="<%= prev %>"><fmt:message key="browse.full.prev"/></a></li>
<%
      }

      if (bi.hasNextPage())
      {
%>
                <li><a href="<%= next %>"><fmt:message key="browse.full.next"/></a></li>
<%
      }
%>
            </ul>
		</div>
	</div>

<%-- output the results using the browselist tag --%>
<%
      if (bix.isMetadataIndex())
      {
%>
      <dspace:browselist browseInfo="<%= bi %>" emphcolumn="<%= bix.getMetadata() %>" />
<%
      }
      else
      {
%>
      <dspace:browselist browseInfo="<%= bi %>" emphcolumn="<%= bix.getSortOption().getMetadata() %>" />
<%
      }
%>


	<div class="row">
		<div class="col-lg-12">
			<ul class="pager">
<% 
      if (bi.hasPrevPage())
      {
%>
                <li><a href="<%= prev %>"><fmt:message key="browse.full.prev"/></a></li>
<%
      }

      if (bi.hasNextPage())
      {
%>
                <li><a href="<%= next %>"><fmt:message key="browse.full.next"/></a></li>
<%
      }
%>
            </ul>
		</div>
	</div>

<%
   } // end of if (show_title)
%>

	

<%  if (submit_button) { %>
        		<form name="submitToCollection" class="form-inline text-center margin-top" method="post" action="<%= request.getContextPath() %>/submit">
            		<input type="hidden" name="collection" value="<%= collection.getID() %>" />
				</form>
<% } %>
<%  if (loggedIn && subscribed) { %>
        		<form name="subscribe" class="form-inline text-center margin-top" method="get" action="">
        			<input type="hidden" name="submit_unsubscribe" value="<fmt:message key="jsp.collection-home.unsub"/>" />
				</form>
<%  } else { %>
        		<form name="subscribe" class="form-inline text-center margin-top" method="get" action="">
        			<input type="hidden" name="submit_subscribe" value="<fmt:message key="jsp.collection-home.subscribe"/>" />
				</form>
<%  } %>
	<div class="row">
		<div class="col-lg-12">
			<div class="alert alert-info text-center">
<%  if (submit_button) { %>
					<a class="btn btn-primary" href="#" onclick="document.submitToCollection.submit();">
						<fmt:message key="jsp.collection-home.submit.button"/>
					</a>
<%  } %>
					<a class="btn btn-primary" href="#" onclick="document.subscribe.submit();">
					
<%  if (loggedIn && subscribed) { %>
						<fmt:message key="jsp.collection-home.unsub"/>
<%  } else { %>
						<fmt:message key="jsp.collection-home.subscribe"/>
<%  } %>
					</a>
					<a class="btn btn-primary" href="<%= request.getContextPath() %>/handle/<%= collection.getHandle() %>/statistics"><fmt:message key="jsp.collection-home.display-statistics"/></a>
			</div>
		</div>
	</div>
    
  	<p class="copyrightText"><%= copyright %></p>

  <dspace:sidebar>
<% if(admin_button || editor_button ) { %>

		<form name="editbutton" method="post" action="<%=request.getContextPath()%>/tools/edit-communities">
                  <input type="hidden" name="collection_id" value="<%= collection.getID() %>" />
                  <input type="hidden" name="community_id" value="<%= community.getID() %>" />
                  <input type="hidden" name="action" value="<%= EditCommunitiesServlet.START_EDIT_COLLECTION %>" />
		</form>
		<form name="itemMap" method="post" action="<%=request.getContextPath()%>/tools/itemmap">
                  <input type="hidden" name="cid" value="<%= collection.getID() %>" />
				  <input type="hidden" value="<fmt:message key="jsp.collection-home.item.button"/>" />                  
        </form>
        <form name="editSubmitters" method="get" action="<%=request.getContextPath()%>/tools/group-edit">
		        <input type="hidden" name="group_id" value="<%=submitters.getID()%>" />
		        <input type="hidden" name="submit_edit" value="<fmt:message key="jsp.collection-home.editsub.button"/>" />
		</form>
	    <form name="exportColl" method="post" action="<%=request.getContextPath()%>/mydspace">
                  <input type="hidden" name="collection_id" value="<%= collection.getID() %>" />
                  <input type="hidden" name="step" value="<%= MyDSpaceServlet.REQUEST_EXPORT_ARCHIVE %>" />
        </form>
        <form name="migrateArchive" method="post" action="<%=request.getContextPath()%>/mydspace">
                  <input type="hidden" name="collection_id" value="<%= collection.getID() %>" />
                  <input type="hidden" name="step" value="<%= MyDSpaceServlet.REQUEST_EXPORT_ARCHIVE %>" />
        </form>
        <form name="metadataExport" method="post" action="<%=request.getContextPath()%>/dspace-admin/metadataexport">
               <input type="hidden" name="handle" value="<%= collection.getHandle() %>" />
        </form>
        
    	<h4><fmt:message key="jsp.admintools"/></h4>
		<ul class="nav nav-pills nav-stacked">
<% if( editor_button ) { %>
            <li>
                  <a href="#" onclick="document.editbutton.submit();"><fmt:message key="jsp.general.edit.button"/></a>
            </li>
<% } %>

<% if( admin_button ) { %>
			<li>
				<a href="#" onclick="document.itemMap.submit();"><fmt:message key="jsp.collection-home.item.button"/></a>
			</li>
<% if(submitters != null) { %>
			<li>
				<a href="#" onclick="document.editSubmitters.submit();"><fmt:message key="jsp.collection-home.editsub.button"/></a>
			</li>
<% } %>
<% if( editor_button || admin_button) { %>
            <li>
            	<a href="#" onclick="document.exportColl.submit();"><fmt:message key="jsp.mydspace.request.export.collection"/></a>
            </li>
            <li>
            	<a href="#" onclick="document.migrateArchive.submit();"><fmt:message key="jsp.mydspace.request.export.migratecommunity"/></a>
            </li>
            <li>
            	<a href="#" onclick="document.metadataExport.submit();"><fmt:message key="jsp.general.metadataexport.button"/></a>
            </li>
<% } %>
			<li>
				<dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.collection-admin\")%>"><fmt:message key="jsp.adminhelp"/></dspace:popup>
			</li>
<% } %>
		</ul>
<%  } %>

<%
	if (rs != null)
	{
%>
	<h3><fmt:message key="jsp.collection-home.recentsub"/></h3>
<%
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
%>
    <p>&nbsp;</p>
<%      } %>

<%
    if(feedEnabled)
    {
%>
    <center>
    <h4><fmt:message key="jsp.collection-home.feeds"/></h4>
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
    <a href="<%= request.getContextPath() %>/feed/<%= fmts[j] %>/<%= collection.getHandle() %>"><img src="<%= request.getContextPath() %>/image/<%= icon %>" alt="RSS Feed" width="<%= width %>" height="15" vspace="3" border="0" /></a>
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

</dspace:layout>

