<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Component which displays a login form and associated information
  --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div class="row">
	<div class="col-lg-2"></div>
	<div class="col-lg-8">
		<form name="loginform" id="loginform" method="post"
			action="<%= request.getContextPath() %>/password-login"
			class="form-horizontal">
			<fieldset>
				<legend>
					<fmt:message key="jsp.login.password.heading" />
				</legend>
				<div class="form-group">
					<label for="inputEmail" class="col-lg-4 control-label"><fmt:message key="jsp.components.login-form.email" /></label>
					<div class="col-lg-8">
						<input type="text" class="form-control" name="login_email"
							id="inputEmail" placeholder="<fmt:message key="jsp.components.login-form.email" />" />
					</div>
				</div>
				<div class="form-group">
					<label for="inputPassword" class="col-lg-4 control-label"><fmt:message key="jsp.components.login-form.password" /></label>
					<div class="col-lg-8">
						<input type="password" class="form-control" name="login_password"
							id="inputPassword" placeholder="<fmt:message key="jsp.components.login-form.password" />" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-lg-12">
						<div class="pull-right">
							<button type="submit" name="login_submit" class="btn btn-primary">
								<fmt:message key="jsp.components.login-form.login" />
							</button>
						</div>
					</div>
				</div>
			</fieldset>
		</form>

		<script type="text/javascript">
		document.loginform.login_email.focus();
	  </script>
	  
	  
		<p class="text-center">
			<strong><a href="<%= request.getContextPath() %>/register"><fmt:message
						key="jsp.components.login-form.newuser" /></a></strong>
		</p>
		<p class="text-center">
			<a href="<%= request.getContextPath() %>/forgot"><fmt:message
					key="jsp.components.login-form.forgot" /></a>
		</p>

	</div>
	<div class="col-lg-2"></div>
</div>