<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@ page import="org.dspace.core.Context" %>
<%@ page import="org.dspace.app.webui.servlet.SubmissionController" %>
<%@ page import="org.dspace.submit.AbstractProcessingStep" %>
<%@ page import="org.dspace.submit.step.UploadStep" %>
<%@ page import="org.dspace.app.util.DCInputSet" %>
<%@ page import="org.dspace.app.util.DCInputsReader" %>
<%@ page import="org.dspace.app.util.SubmissionInfo" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>


<%
    request.setAttribute("LanguageSwitch", "hide");

    // Obtain DSpace context
    Context context = UIUtil.obtainContext(request);    

	//get submission information object
    SubmissionInfo subInfo = SubmissionController.getSubmissionInfo(context, request);
   
 	// Determine whether a file is REQUIRED to be uploaded (default to true)
 	boolean fileRequired = ConfigurationManager.getBooleanProperty("webui.submit.upload.required", true);
%>


<dspace:layout locbar="off"
               titlekey="jsp.submit.choose-file.title"
               nocache="true">
	<form class="margin-top" action="<%= request.getContextPath() %>/submit"  enctype="multipart/form-data" method="post" onkeydown="return disableEnterKey(event);">
               
		<div class="row margin-top-2">
			<div class="col-lg-12">
	        	<jsp:include page="/submit/progressbar.jsp" />
			</div>
		</div>
		
		
		<%= SubmissionController.getSubmissionParameters(context, request) %>
        <div class="panel panel-default margin-top-2">
			<div class="panel-heading">
				<h3><fmt:message key="jsp.submit.choose-file.heading"/></h3>
    
		        <%-- <p>Please enter the name of
		        <%= (si.submission.hasMultipleFiles() ? "one of the files" : "the file" ) %> on your
		        local hard drive corresponding to your item.  If you click "Browse...", a
		        new window will appear in which you can locate and select the file on your
		        local hard drive. <object><dspace:popup page="/help/index.html#upload">(More Help...)</dspace:popup></object></p> --%>

				<p><fmt:message key="jsp.submit.choose-file.info1"/>
					<dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.index\") + \"#upload\"%>"><fmt:message key="jsp.morehelp"/></dspace:popup></p>
        
		        <%-- FIXME: Collection-specific stuff should go here? --%>
		        <%-- <p class="submitFormHelp">Please also note that the DSpace system is
		        able to preserve the content of certain types of files better than other
		        types.
		        <dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.formats\")%>">Information about file types</dspace:popup> and levels of
		        support for each are available.</p> --%>
		        
				<p><fmt:message key="jsp.submit.choose-file.info6"/>
		        <dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.formats\")%>"><fmt:message key="jsp.submit.choose-file.info7"/></dspace:popup>
		        </p>
			</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-lg-12">
						<div class="form-group">
							<label><fmt:message key="jsp.submit.choose-file.document"/></label>
							<input type="file" class="form-control" size="40" name="file" id="tfile" />
						</div>
<%
    if (subInfo.getSubmissionItem().hasMultipleFiles())
    {
%>
						<div class="form-group">
							<p><fmt:message key="jsp.submit.choose-file.info9"/></p>
							<label><fmt:message key="jsp.submit.choose-file.filedescr"/></label></label>
							<input type="text" name="description" id="tdescription" class="form-control"/>
						</div>
<%
    }
%>
					</div>
				</div>
			</div>
			<div class="panel-footer">
				<div class="pull-right">
				<%  //if not first step, show "Previous" button
					if(!SubmissionController.isFirstStep(request, subInfo))
					{ %>
                        <button class="btn btn-default" type="submit" name="<%=AbstractProcessingStep.PREVIOUS_BUTTON%>"><fmt:message key="jsp.submit.general.previous"/></button>
				<%  } %>
					<button class="btn btn-primary" type="submit" name="<%=UploadStep.SUBMIT_UPLOAD_BUTTON%>"><fmt:message key="jsp.submit.general.next"/></button>
					<%
                        //if upload is set to optional, or user returned to this page after pressing "Add Another File" button
                    	if (!fileRequired || UIUtil.getSubmitButton(request, "").equals(UploadStep.SUBMIT_MORE_BUTTON))
                        {
                    %>
                    <button class="btn btn-default" type="submit" name="<%=UploadStep.SUBMIT_SKIP_BUTTON%>"><fmt:message key="jsp.submit.choose-file.skip"/></button>
                    <%
                        }
                    %>   
					<button class="btn btn-default" type="submit" name="<%=AbstractProcessingStep.CANCEL_BUTTON%>"><fmt:message key="jsp.submit.general.cancel-or-save.button"/></button>
				</div>	
				<div class="clearfix"></div>
			</div>
		</div>
    </form>

</dspace:layout>
