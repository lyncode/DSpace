<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - UI page for selection of collection.
  -
  - Required attributes:
  -    collections - Array of collection objects to show in the drop-down.
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.core.Context" %>
<%@ page import="org.dspace.app.webui.servlet.SubmissionController" %>
<%@ page import="org.dspace.submit.AbstractProcessingStep" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.content.Collection" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>
	
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%
    request.setAttribute("LanguageSwitch", "hide");

    //get collections to choose from
    Collection[] collections =
        (Collection[]) request.getAttribute("collections");

	//check if we need to display the "no collection selected" error
    Boolean noCollection = (Boolean) request.getAttribute("no.collection");

    // Obtain DSpace context
    Context context = UIUtil.obtainContext(request);
%>

<dspace:layout locbar="off"
               titlekey="jsp.submit.select-collection.title"
               nocache="true">

	<div class="row margin-top-2">
		<div class="col-md-8 col-md-offset-2">
		<%  if (collections.length > 0)
		    {
		%>
			<form class="form-horizontal" action="<%= request.getContextPath() %>/submit" method="post" onkeydown="return disableEnterKey(event);">
				<%= SubmissionController.getSubmissionParameters(context, request) %>
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3><fmt:message key="jsp.submit.select-collection.heading"/></h3>
						<p>
							<fmt:message key="jsp.submit.select-collection.info1"/>
							<dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.index\") + \"#choosecollection\"%>"><fmt:message key="jsp.morehelp"/> </dspace:popup>
						</p>
					</div>
					<div class="panel-body">
						<div class="col-md-10 col-md-offset-1">
		<%
				//if no collection was selected, display an error
				if((noCollection != null) && (noCollection.booleanValue()==true))
				{
		%>
					    <div class="alert alert-danget">
					    	<p><fmt:message key="jsp.submit.select-collection.no-collection"/></p>
					    </div>
		<%
				}
		%> 		
			   			<div class="form-group">
							<label><fmt:message key="jsp.submit.select-collection.collection"/></label>
							<select class="form-control" name="collection" id="tcollection">
			                   	<option value="-1"></option>
		<%
		        for (int i = 0; i < collections.length; i++)
		        {
		%>
							<option value="<%= collections[i].getID() %>"><%= collections[i].getMetadata("name") %></option>
		<%
		        }
		%>
			            	</select>
			            </div>
			            </div>
					</div>
					<div class="panel-footer">
						<div class="pull-right">
							<button class="btn btn-primary" type="submit" name="<%=AbstractProcessingStep.NEXT_BUTTON%>"><fmt:message key="jsp.submit.general.next"/></button>
							<button class="btn btn-default" type="submit" name="<%=AbstractProcessingStep.CANCEL_BUTTON%>"><fmt:message key="jsp.submit.select-collection.cancel"/></button>
						</div>
						<div class="clearfix"></div>
					</div>
				</div>
		    </form>
		<%  } else { %>
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3><fmt:message key="jsp.submit.select-collection.heading"/></h3>
					<p>
						<fmt:message key="jsp.submit.select-collection.info1"/>
						<dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.index\") + \"#choosecollection\"%>"><fmt:message key="jsp.morehelp"/> </dspace:popup>
					</p>
				</div>
				<div class="panel-body">
					<div class="alert alert-danger">
						<p><fmt:message key="jsp.submit.select-collection.none-authorized"/></p>
					</div>
				</div>
			</div>
		<%  } %>
		</div>
	</div>
	
</dspace:layout>
