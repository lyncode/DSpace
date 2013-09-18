<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Workspace Options page, so the user may edit, view, add notes to or remove
  - the workspace item
  -
  - Attributes:
  -    wsItem   - WorkspaceItem containing the current item to be worked on
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page import="org.dspace.app.webui.servlet.MyDSpaceServlet" %>
<%@ page import="org.dspace.content.DCValue" %>
<%@ page import="org.dspace.content.Item" %>
<%@ page import="org.dspace.content.WorkspaceItem" %>
<%@ page import="org.dspace.eperson.EPerson" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>
<%@ page import="org.dspace.core.Utils" %>

<%
    // get the workspace item from the request
    WorkspaceItem workspaceItem =
        (WorkspaceItem) request.getAttribute("wsItem");

    // get the title and submitter of the item
    DCValue[] titleArray =
         workspaceItem.getItem().getDC("title", null, Item.ANY);
//    String title = (titleArray.length > 0 ? titleArray[0].value : "Untitled");
    EPerson submitter = workspaceItem.getItem().getSubmitter();
%>

<dspace:layout locbar="link"
               parentlink="/mydspace"
               parenttitlekey="jsp.mydspace"
               titlekey="jsp.workspace.ws-main.title">
               
    <div class="row">
    	<div class="col-lg-12">
    		<div class="pull-right">
    			<dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.index\") + \"#mydspace\"%>"><fmt:message key="jsp.help"/></dspace:popup>
    		</div>
    		<h2><fmt:message key="jsp.workspace.ws-main.wsitem"/></h2>
    	</div>
    </div>
    

<%--    <h2><%= title %></h2> --%>
<%
		if (titleArray.length > 0)
		{
%>
			<h3><%= titleArray[0].value %> <small><fmt:message key="jsp.dspace-admin.supervise-link.submittedby" /> <strong><a href="mailto:<%= submitter.getEmail() %>"><%= Utils.addEntities(submitter.getFullName()) %></a></strong></small></h3>
<%
		}
		else
		{
%>
			<h3><fmt:message key="jsp.general.untitled"/> <small><fmt:message key="jsp.dspace-admin.supervise-link.submittedby" /> <strong><a href="mailto:<%= submitter.getEmail() %>"><%= Utils.addEntities(submitter.getFullName()) %></a></strong></small></h3>
<%
		}
%>

	<hr />

	<div class="alert alert-info">
	<p><fmt:message key="jsp.workspace.ws-main.submitmsg"/> 
    	<strong><%= workspaceItem.getCollection().getMetadata("name") %></strong></p>
	</div>

	<div class="row">
		<div class="col-lg-3"></div>
		<div class="col-lg-6">
                <form name="editMSG" action="<%= request.getContextPath() %>/mydspace" method="post">
                    <input type="hidden" name="step" value="<%= MyDSpaceServlet.MAIN_PAGE %>"/>
                    <input type="hidden" name="workspace_id" value="<%= workspaceItem.getID() %>"/>
                    <input type="hidden" name="resume" value="<%= workspaceItem.getID() %>"/>
                    <input type="hidden" name="submit_resume" value="<fmt:message key="jsp.workspace.ws-main.button.edit"/>"/>
                </form>
                <form name="viewITEM" action="<%= request.getContextPath() %>/view-workspaceitem" method="post">
                   <input type="hidden" name="workspace_id" value="<%= workspaceItem.getID() %>"/>
                   <input type="hidden" name="submit_view" value="<fmt:message key="jsp.workspace.ws-main.button.view"/>"/>
                </form>
                <form name="removeITEM" action="<%= request.getContextPath() %>/mydspace" method="post">
                    <input type="hidden" name="step" value="<%= MyDSpaceServlet.MAIN_PAGE %>"/>
                    <input type="hidden" name="workspace_id" value="<%= workspaceItem.getID() %>"/>
                    <input type="hidden" name="submit_delete" value="<fmt:message key="jsp.workspace.ws-main.button.remove"/>"/>
                </form>
			<div class="list-group">
				<a class="list-group-item" href="#" onclick="document.editMSG.submit();">
					<h4><fmt:message key="jsp.workspace.ws-main.button.edit"/></h4>
                	<p><fmt:message key="jsp.workspace.ws-main.editmsg"/></p>
                </a>
				<a class="list-group-item" href="#" onclick="document.viewITEM.submit();">
					<h4 ><fmt:message key="jsp.workspace.ws-main.button.view"/></h4>
                	<p><fmt:message key="jsp.workspace.ws-main.viewmsg"/></p>
                </a>
				<a class="list-group-item" href="#" onclick="document.removeITEM.submit();">
					<h4 class="text-danger"><fmt:message key="jsp.workspace.ws-main.button.remove"/></h4>
                	<p class="text-danger"><fmt:message key="jsp.workspace.ws-main.removemsg"/></p>
                </a>
			</div>
		</div>
		<div class="col-lg-3"></div>
	</div>

<p><a href="<%= request.getContextPath() %>/mydspace"><fmt:message key="jsp.mydspace.general.returnto-mydspace"/></a></p>

</dspace:layout>
