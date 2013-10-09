<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Renders a whole HTML page for displaying item metadata.  Simply includes
  - the relevant item display component in a standard HTML page.
  -
  - Attributes:
  -    display.all - Boolean - if true, display full metadata record
  -    item        - the Item to display
  -    collections - Array of Collections this item appears in.  This must be
  -                  passed in for two reasons: 1) item.getCollections() could
  -                  fail, and we're already committed to JSP display, and
  -                  2) the item might be in the process of being submitted and
  -                  a mapping between the item and collection might not
  -                  appear yet.  If this is omitted, the item display won't
  -                  display any collections.
  -    admin_button - Boolean, show admin 'edit' button
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="org.dspace.content.Collection" %>
<%@ page import="org.dspace.content.DCValue" %>
<%@ page import="org.dspace.content.Item" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@ page import="org.dspace.handle.HandleManager" %>
<%@ page import="org.dspace.license.CreativeCommons" %>

<%
    // Attributes
    Boolean displayAllBoolean = (Boolean) request.getAttribute("display.all");
    boolean displayAll = (displayAllBoolean != null && displayAllBoolean.booleanValue());
    Boolean suggest = (Boolean)request.getAttribute("suggest.enable");
    boolean suggestLink = (suggest == null ? false : suggest.booleanValue());
    Item item = (Item) request.getAttribute("item");
    Collection[] collections = (Collection[]) request.getAttribute("collections");
    Boolean admin_b = (Boolean)request.getAttribute("admin_button");
    boolean admin_button = (admin_b == null ? false : admin_b.booleanValue());

    // get the workspace id if one has been passed
    Integer workspace_id = (Integer) request.getAttribute("workspace_id");

    // get the handle if the item has one yet
    String handle = item.getHandle();

    // CC URL & RDF
    String cc_url = CreativeCommons.getLicenseURL(item);
    String cc_rdf = CreativeCommons.getLicenseRDF(item);

    // Full title needs to be put into a string to use as tag argument
    String title = "";
    if (handle == null)
 	{
		title = "Workspace Item";
	}
	else 
	{
		DCValue[] titleValue = item.getDC("title", null, Item.ANY);
		if (titleValue.length != 0)
		{
			title = titleValue[0].value;
		}
		else
		{
			title = "Item " + handle;
		}
	}

    String displayStyle = (displayAll ? "full" : "");
    String locationLink = request.getContextPath() + "/handle/" + handle;
%>

<%@page import="org.dspace.app.webui.servlet.MyDSpaceServlet"%>
<dspace:layout title="<%= title %>">

<%
    if (handle != null)
    {
%>
	<div class="row">
		<div class="col-lg-12">
				<div class="row">
<%
	int cols = 12;
	if (admin_button) cols = 9;
%>
					<div class="col-lg-<%=cols%>">
						<div class="alert alert-info">
                			<fmt:message key="jsp.display-item.identifier"/>
                			<code class="text-danger"><%= HandleManager.getCanonicalForm(handle) %></code>
                		</div>
					</div>
<% if (admin_button) { %>
					<div class="col-lg-3">
						<form name="exportArchive" method="post" action="<%= request.getContextPath() %>/mydspace">
		                    <input type="hidden" name="item_id" value="<%= item.getID() %>" />
		                    <input type="hidden" name="step" value="<%= MyDSpaceServlet.REQUEST_EXPORT_ARCHIVE %>" />
		                </form>
		                <form name="migrateArchive" method="post" action="<%= request.getContextPath() %>/mydspace">
		                    <input type="hidden" name="item_id" value="<%= item.getID() %>" />
		                    <input type="hidden" name="step" value="<%= MyDSpaceServlet.REQUEST_MIGRATE_ARCHIVE %>" />
		                </form>
		                <form name="metadataExport" method="post" action="<%= request.getContextPath() %>/dspace-admin/metadataexport">
		                    <input type="hidden" name="handle" value="<%= item.getHandle() %>" />
		                </form>
		                <form name="editItem" method="get" action="<%= request.getContextPath() %>/tools/edit-item">
		                    <input type="hidden" name="item_id" value="<%= item.getID() %>" />
		                    <%--<input type="submit" name="submit" value="Edit...">--%>
		                </form>
		                <ul class="nav nav-pills nav-stacked">
		                	<li>
		                    	<a href="#" onclick="document.editItem.submit();"><fmt:message key="jsp.general.edit.button"/></a>
		                	</li>
		                	<li>
		                		<a href="#" onclick="document.exportArchive.submit();"><fmt:message key="jsp.mydspace.request.export.item"/></a>
		                	</li>
		                	<li>
		                		<a href="#" onclick="document.migrateArchive.submit();"><fmt:message key="jsp.mydspace.request.export.migrateitem"/></a>
		                	</li>
		                	<li>
		                    	<a href="#" onclick="document.metadataExport.submit();"><fmt:message key="jsp.general.metadataexport.button"/></a>
		                	</li>
		                </ul>
					</div>
<% } %>
				</div>
		</div>
	</div>
<%
}
%>
    <dspace:item-preview item="<%= item %>" />
    <dspace:item item="<%= item %>" collections="<%= collections %>" style="<%= displayStyle %>" />

<%

    if (displayAll)
    {
%>


<div class="row">
	<div class="col-lg-12">
		<ul class="nav nav-pills">
<%
    if (workspace_id != null) {
%>
			<center>
			    <form class="text-center" method="post" action="<%= request.getContextPath() %>/view-workspaceitem">
			        <input type="hidden" name="workspace_id" value="<%= workspace_id.intValue() %>" />
			        <button class="btn btn-primary" type="submit" name="submit_simple">
			        	<fmt:message key="jsp.display-item.text1"/>
			        </button>
			    </form>
			</center>
<%
    } else {
%>
			<center>
			    <form class="text-center" method="get" action="<%=locationLink %>">
			        <input type="hidden" name="mode" value="simple"/>
			        <button class="btn btn-primary" type="submit" name="submit_simple">
			        	<fmt:message key="jsp.display-item.text1"/>
			        </button>
			    </form>
	    	</center>
<%
    }
%>
		</ul>
	</div>
</div>
<%
} else {
%>

<div class="row">
	<div class="col-lg-12">
<%
        if (workspace_id != null)
        {
%>
			<center>
			    <form method="post" action="<%= request.getContextPath() %>/view-workspaceitem">
			        <input type="hidden" name="workspace_id" value="<%= workspace_id.intValue() %>" />
			        <button class="btn btn-primary" type="submit" name="submit_full">
			        	<fmt:message key="jsp.display-item.text2"/>
			        </button>
			    </form>
    		</center>
<%
        }
        else
        {
%>
			<center>
			    <form method="get" action="<%=locationLink %>">
			        <input type="hidden" name="mode" value="full"/>
			        <button class="btn btn-primary" type="submit" name="submit_simple">
			        	<fmt:message key="jsp.display-item.text2"/>
			        </button>
			    </form>
    		</center>
<%
        }

        if (suggestLink)
        {
%>
			<center>
    			<a class="btn btn-primary" href="<%= request.getContextPath() %>/suggest?handle=<%= handle %>" target="new_window">
       				<fmt:message key="jsp.display-item.suggest"/>
       			</a>
			</center>
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
    if (workspace_id != null)
    {
%>
	
<div align="center">
   <form method="post" action="<%= request.getContextPath() %>/workspace">
        <input type="hidden" name="workspace_id" value="<%= workspace_id.intValue() %>"/>
        <button class="btn btn-primary" type="submit" name="submit_open"><fmt:message key="jsp.display-item.back_to_workspace"/></button>
    </form>
</div>
<%
    }
%>
    <%-- SFX Link --%>
<%
    if (ConfigurationManager.getProperty("sfx.server.url") != null)
    {
        String sfximage = ConfigurationManager.getProperty("sfx.server.image_url");
        if (sfximage == null)
        {
            sfximage = request.getContextPath() + "/image/sfx-link.gif";
        }
%>
    <p align="center">
        <a href="<dspace:sfxlink item="<%= item %>"/>" /><img src="<%= sfximage %>" border="0" alt="SFX Query" /></a>
    </p>
<%
    }
%>
	<div class="row margin-top">
		<div class="col-lg-12">
			<div class="alert alert-info">
<%
    if (cc_url != null)
    {
%>
			    <p class="submitFormHelp"><fmt:message key="jsp.display-item.text3"/> <a href="<%= cc_url %>"><fmt:message key="jsp.display-item.license"/></a><br/>
			    	<a href="<%= cc_url %>"><img src="<%= request.getContextPath() %>/image/cc-somerights.gif" border="0" alt="Creative Commons" /></a>
			    </p>
    <!--
    <%= cc_rdf %>
    -->
<%
    }
%>
   				 <p class="submitFormHelp"><fmt:message key="jsp.display-item.copyright"/></p>
			</div>
		</div>
	</div>
	
	

	<div align="center">
	    <a class="statisticsLink" href="<%= request.getContextPath() %>/handle/<%= handle %>/statistics"><fmt:message key="jsp.display-item.display-statistics"/></a>
	</div>
    <%-- Create Commons Link --%>
</dspace:layout>
