<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Remove Item page
  -
  -  Attributes:
  -      workspace.item - the workspace item the user wishes to delete
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="org.dspace.app.webui.servlet.MyDSpaceServlet" %>
<%@ page import="org.dspace.content.WorkspaceItem" %>

<%
    WorkspaceItem wi = (WorkspaceItem) request.getAttribute("workspace.item");
%>

<dspace:layout locbar="link"
               parentlink="/mydspace"
               parenttitlekey="jsp.mydspace"
               titlekey="jsp.mydspace.remove-item.title"
               nocache="true">

	<div class="panel panel-danger">
		<div class="panel-heading">
			<h3 class="panel-title">
				<fmt:message key="jsp.mydspace.remove-item.title" />
			</h3>
		</div>
		<div class="panel-body">
			<fmt:message key="jsp.mydspace.remove-item.confirmation" />
		</div>
		<div class="panel-footer">
			<div class="pull-right">
				<form action="<%=request.getContextPath()%>/mydspace"
					method="post">
					<input type="hidden" name="workspace_id" value="<%=wi.getID()%>" />
					<input type="hidden" name="step"
						value="<%=MyDSpaceServlet.REMOVE_ITEM_PAGE%>" />

					<button class="btn btn-danger" type="submit" name="submit_delete">
						<fmt:message key="jsp.mydspace.remove-item.remove.button" />
					</button>
					<button class="btn btn-default" type="submit" name="submit_cancel">
						<fmt:message key="jsp.mydspace.remove-item.cancel.button" />
					</button>
				</form>
			</div>
			<div class="clearfix"></div>
		</div>
	</div>

	<dspace:item item="<%=wi.getItem()%>"/>
</dspace:layout>
