<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Forgotten password DSpace form
  -
  - Form where new users enter their email address to get a token to enter a
  - new password.
  -
  - Attributes to pass in:
  -     retry  - if set, this is a retry after the user entered an invalid email
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"
    prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page import="org.dspace.app.webui.servlet.RegisterServlet" %>

<%
    boolean retry = (request.getAttribute("retry") != null);
%>

<dspace:layout titlekey="jsp.register.forgot-password.title">

<div class="row">
	<div class="col-lg-2"></div>
	<div class="col-lg-8">
		<form action="<%= request.getContextPath() %>/forgot" method="post"
			class="form-horizontal">
        	<input type="hidden" name="step" value="<%= RegisterServlet.ENTER_EMAIL_PAGE %>"/>
			<fieldset>
				<legend>
					<fmt:message key="jsp.register.forgot-password.title"/>
				</legend>
				<% if (retry) { %>
				<div class="alert alert-dismissable alert-danger">
	              <button type="button" class="close" data-dismiss="alert">×</button>
	              <fmt:message key="jsp.register.new-user.info1"/>
	            </div>
				<%  } %>
				
				<div class="form-group">
					<label for="inputEmail" class="col-lg-2 control-label"><fmt:message key="jsp.register.forgot-password.email.field"/></label>
					<div class="col-lg-10">
						<input type="text" class="form-control" name="email"
							id="inputEmail" placeholder="<fmt:message key="jsp.register.forgot-password.email.field"/>" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-lg-12">
						<div class="pull-right">
							<button type="submit" name="submit" class="btn btn-primary">
								<fmt:message key="jsp.register.forgot-password.forgot.button"/>
							</button>
						</div>
					</div>
				</div>
				
			</fieldset>
			
			
		</form>
	</div>
	<div class="col-lg-2"></div>
</div>
    
</dspace:layout>
