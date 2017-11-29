<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<spring:eval expression="@environment.getProperty('user.pwd.strength')" var="pwdstrengthmsg"/>
		<spring:message code="usr.pwd.strength.msg.${pwdstrengthmsg}" var="pwdmsg" htmlEscape="true"/>
<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				&nbsp;
			</div>
			<div class="panel-body custom-form">
				<c:choose>
					<c:when test="${valid}">
						<form:form id="password-form" cssClass="form-horizontal form-groups-bordered">
							<c:if test="${not empty param.error}">
								<div id="error-msg" class="error-msg text-center add-margin"><spring:message code="${param.error}"/></div>
							</c:if>
							<div class="form-group">
								<label class="col-sm-4 control-label">
									<spring:message code="lbl.new.pwd"/> <span class="mandatory"></span>
								</label>
								<div class="col-sm-4" >
									<input style="display:none" type="password">
									<input type="password" name="newPassword" autocomplete="new-password" placeholder="Enter your new password" class="form-control check-password" id="new-pass" required="required" maxlength="32" data-toggle="popover" data-content='${pwdmsg}'/>
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-4 control-label">
									<spring:message code="lbl.pwd.confirm"/> <span class="mandatory"></span>
								</label>
								<div class="col-sm-4" >
									<input type="password" name="confirmPwd" autocomplete="new-password" placeholder="Re-enter your new password" class="form-control check-password" id="retype-pass" required="required" maxlength="32"/>
									<div class="password-error error-msg display-hide"><spring:message code="lbl.pwd.mismatch"/> </div>
								</div>
							</div>
							<div class="form-group text-center">
								<div class="col-md-12 add-margin">
									<button type="submit" class="btn btn-primary"><spring:message code="title.reset.password"/></button>
									<button type="button" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close"/></button>
								</div>
							</div>
						</form:form>
					</c:when>
					<c:otherwise>
						<div class="alert alert-error" role="alert">
                            <div class="error-msg text-center add-margin">
                                <spring:message code="err.reset.password.invalid"/>
                            </div>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</div>
<script>
$('.check-password').blur(function(){
	if(($('#new-pass').val()!="") && ($('#retype-pass').val()!=""))
	{
		if ($('#new-pass').val() === $('#retype-pass').val()) {
			return true;
			
		}else{
			$('.password-error').show();
			$('#retype-pass').addClass('error');
			return false;
		}
	}
});
$('#new-pass').popover({ trigger: "focus",placement: "bottom"})
</script>
