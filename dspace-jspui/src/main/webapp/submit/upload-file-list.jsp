<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - List of uploaded files
  -
  - Attributes to pass in to this page:
  -   just.uploaded     - Boolean indicating if a file has just been uploaded
  -                       so a nice thank you can be displayed.
  -   show.checksums    - Boolean indicating whether to show checksums
  -
  - FIXME: Assumes each bitstream in a separate bundle.
  -        Shouldn't be a problem for early adopters.
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport" %>

<%@ page import="org.dspace.core.Context" %>
<%@ page import="org.dspace.app.webui.servlet.SubmissionController" %>
<%@ page import="org.dspace.submit.AbstractProcessingStep" %>
<%@ page import="org.dspace.app.util.SubmissionInfo" %>
<%@ page import="org.dspace.app.webui.util.UIUtil" %>
<%@ page import="org.dspace.content.Bitstream" %>
<%@ page import="org.dspace.content.BitstreamFormat" %>
<%@ page import="org.dspace.content.Bundle" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    // Obtain DSpace context
    Context context = UIUtil.obtainContext(request);    

	//get submission information object
    SubmissionInfo subInfo = SubmissionController.getSubmissionInfo(context, request);

    boolean justUploaded = ((Boolean) request.getAttribute("just.uploaded")).booleanValue();
    boolean showChecksums = ((Boolean) request.getAttribute("show.checksums")).booleanValue();
    
    request.setAttribute("LanguageSwitch", "hide");
    boolean allowFileEditing = !subInfo.isInWorkflow() || ConfigurationManager.getBooleanProperty("workflow", "reviewer.file-edit");
%>

<dspace:layout locbar="off" titlekey="jsp.submit.upload-file-list.title">
	<form class="margin-top" action="<%= request.getContextPath() %>/submit" method="post" onkeydown="return disableEnterKey(event);">
               
		<div class="row margin-top-2">
			<div class="col-lg-12">
	        	<jsp:include page="/submit/progressbar.jsp" />
			</div>
		</div>
		
		
		<%= SubmissionController.getSubmissionParameters(context, request) %>
        <div class="panel panel-default margin-top-2">
			<div class="panel-heading">
<%
    if (justUploaded)
    {
%>
				<h3><fmt:message key="jsp.submit.upload-file-list.heading1"/></h3>
		        <p><fmt:message key="jsp.submit.upload-file-list.info1"/></p>
<%
    }
    else
    {
%>
	    		<h3><fmt:message key="jsp.submit.upload-file-list.heading2"/></h3>
<%
    }
%>			
				<p><fmt:message key="jsp.submit.upload-file-list.info2"/>&nbsp;&nbsp;&nbsp;<dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.index\") + \"#uploadedfile\"%>"><fmt:message key="jsp.morehelp"/></dspace:popup></p>
			</div>
			<div class="panel-body">
        
        <table class="table table-striped" align="center" summary="Table dispalying your submitted files">
            <tr>
				<th id="t1" class="oddRowEvenCol"><fmt:message key="jsp.submit.upload-file-list.tableheading1"/></th>
                <th id="t2" class="oddRowOddCol"><fmt:message key="jsp.submit.upload-file-list.tableheading2"/></th>
                <th id="t3" class="oddRowEvenCol"><fmt:message key="jsp.submit.upload-file-list.tableheading3"/></th>
                <th id="t4" class="oddRowOddCol"><fmt:message key="jsp.submit.upload-file-list.tableheading4"/></th>
                <th id="t5" class="oddRowEvenCol"><fmt:message key="jsp.submit.upload-file-list.tableheading5"/></th>
<%
    if (showChecksums)
    {
%>

                <th id="t6" class="oddRowOddCol"><fmt:message key="jsp.submit.upload-file-list.tableheading6"/></th>
<%
    }
    
    // Don't display last column ("Remove") in workflow mode
    if (allowFileEditing)
    {
        // Whether it's an odd or even column depends on whether we're showing checksums
        String column = (showChecksums ? "Even" : "Odd");
%>
                <th id="t7" class="oddRow<%= column %>Col">&nbsp;</th>
<%
    }
%>
            </tr>

<%
    String row = "even";

    Bitstream[] bitstreams = subInfo.getSubmissionItem().getItem().getNonInternalBitstreams();
    Bundle[] bundles = null;

    if (bitstreams[0] != null) {
        bundles = bitstreams[0].getBundles();
    }

    for (int i = 0; i < bitstreams.length; i++)
    {
        BitstreamFormat format = bitstreams[i].getFormat();
        String description = bitstreams[i].getFormatDescription();
        String supportLevel = LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.upload-file-list.supportlevel1");

        if(format.getSupportLevel() == 1)
        {
            supportLevel = LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.upload-file-list.supportlevel2");
        }

        if(format.getSupportLevel() == 0)
        {
            supportLevel = LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.upload-file-list.supportlevel3");
        }

        // Full param to dspace:popup must be single variable
        String supportLevelLink = LocaleSupport.getLocalizedMessage(pageContext, "help.formats") +"#" + supportLevel;
%>
            <tr>
		<td headers="t1" class="<%= row %>RowEvenCol" align="center">
		    <input type="radio" name="primary_bitstream_id" value="<%= bitstreams[i].getID() %>"
			   <% if (bundles[0] != null) {
				if (bundles[0].getPrimaryBitstreamID() == bitstreams[i].getID()) { %>
			       	  <%="checked='checked'" %>
			   <%   }
			      } %> />
		</td>
                <td headers="t2" class="<%= row %>RowOddCol"><a href="<%= request.getContextPath() %>/retrieve/<%= bitstreams[i].getID() %>/<%= org.dspace.app.webui.util.UIUtil.encodeBitstreamName(bitstreams[i].getName()) %>" target="_blank"><%= bitstreams[i].getName() %></a></td>
                <td headers="t3" class="<%= row %>RowEvenCol"><%= bitstreams[i].getSize() %> bytes</td>
                <td headers="t4" class="<%= row %>RowOddCol">
                    <%= (bitstreams[i].getDescription() == null || bitstreams[i].getDescription().equals("")
                        ? LocaleSupport.getLocalizedMessage(pageContext, "jsp.submit.upload-file-list.empty1")
                        : bitstreams[i].getDescription()) %>
                    <button class="btn btn-default btn-xs" type="submit" name="submit_describe_<%= bitstreams[i].getID() %>"><fmt:message key="jsp.submit.upload-file-list.button1"/></button>
                </td>
                <td headers="t5" class="<%= row %>RowEvenCol">
                    <%= description %> <dspace:popup page="<%= supportLevelLink %>">(<%= supportLevel %>)</dspace:popup>
                    <button class="btn btn-default btn-xs" type="submit" name="submit_format_<%= bitstreams[i].getID() %>"><fmt:message key="jsp.submit.upload-file-list.button1"/></button>
                </td>
<%
        // Checksum
        if (showChecksums)
        {
%>
                <td headers="t6" class="<%= row %>RowOddCol">
                    <code><%= bitstreams[i].getChecksum() %> (<%= bitstreams[i].getChecksumAlgorithm() %>)</code>
                </td>
<%
        }

        // Don't display "remove" button in workflow mode
        if (allowFileEditing)
        {
            // Whether it's an odd or even column depends on whether we're showing checksums
            String column = (showChecksums ? "Even" : "Odd");
%>
                <td headers="t7" class="<%= row %>Row<%= column %>Col">
                    <button class="btn btn-danger btn-xs" type="submit" name="submit_remove_<%= bitstreams[i].getID() %>"><fmt:message key="jsp.submit.upload-file-list.button2"/></button>
                </td>
<%
        }
%>
            </tr>
<%
        row = (row.equals("even") ? "odd" : "even");
    }
%>
        </table>

<%-- HACK:  Need a space - is there a nicer way to do this than <br> or a --%>
<%--        blank <p>? --%>
<%
    // Don't allow files to be added in workflow mode
    if (allowFileEditing)
    {
%>
            <center><button class="btn btn-primary" type="submit" name="submit_more"><fmt:message key="jsp.submit.upload-file-list.button4"/></button></center>
<%
    }
%>


<%-- Show information about how to verify correct upload, but not in workflow
     mode! --%>
<%
    if (allowFileEditing)
    {
%>
        <p class="margin-top-2 uploadHelp"><fmt:message key="jsp.submit.upload-file-list.info3"/></p>
        <ul class="uploadHelp">
            <li class="uploadHelp"><fmt:message key="jsp.submit.upload-file-list.info4"/></li>
<%
        if (showChecksums)
        {
%>
            <li class="uploadHelp"><fmt:message key="jsp.submit.upload-file-list.info5"/>
            <dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.index\") + \"#checksum\"%>"><fmt:message key="jsp.submit.upload-file-list.help1"/></dspace:popup></li>
            
        </ul>
<%
        }
        else
        {
%>
            <li class="uploadHelp"><fmt:message key="jsp.submit.upload-file-list.info6"/>
            <dspace:popup page="<%= LocaleSupport.getLocalizedMessage(pageContext, \"help.index\") + \"#checksum\"%>"><fmt:message key="jsp.submit.upload-file-list.help2"/></dspace:popup> 
            </li>
        </ul>
            
            <center>
            <button class="btn btn-info" type="submit" name="submit_show_checksums"><fmt:message key="jsp.submit.upload-file-list.button3"/></button>
            </center>
<%
        }
%>
        <br />
<%
    }
%>    

			</div>
			<div class="panel-footer">
				<div class="pull-right">
				<%  //if not first step, show "Previous" button
					if(!SubmissionController.isFirstStep(request, subInfo))
					{ %>
                        <button class="btn btn-default" type="submit" name="<%=AbstractProcessingStep.PREVIOUS_BUTTON%>"><fmt:message key="jsp.submit.general.previous"/></button>
				<%  } %>
					<button class="btn btn-primary" type="submit" name="<%=AbstractProcessingStep.NEXT_BUTTON%>"><fmt:message key="jsp.submit.general.next"/></button>
					<button class="btn btn-default" type="submit" name="<%=AbstractProcessingStep.CANCEL_BUTTON%>"><fmt:message key="jsp.submit.general.cancel-or-save.button"/></button>
				</div>	
				<div class="clearfix"></div>
			</div>
		</div>
    </form>
</dspace:layout>
