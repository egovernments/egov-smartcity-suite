<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>


<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0">
			<div class="panel-heading">
				<div class="panel-title"><spring:message code="lbl.create.bankaccount" /></div>
				<input type="hidden" name="bankaccount" value="${bankaccount.id}" />
			</div>
			<div class="panel-body">
				<div class="form-group">
					<label class="col-sm-3 control-label text-right"><spring:message code="lbl.bankbranch" /> <span class="mandatory"></span> </label>
					<div class="col-sm-3 add-margin">
						<form:select path="bankbranch" data-first-option="false" id="bankbranch" class="form-control" required="required">
							<form:option value=""><spring:message code="lbl.select" /></form:option>
							<c:forEach var="branch" items="${bankbranches}">
							<form:option  value="${branch.id}" ><c:out value="${branch.bank.name} - ${branch.branchname}"/>
							</form:option>
							</c:forEach>
						</form:select>
						<form:errors path="bankbranch" cssClass="error-msg" />
					</div>
					<label class="col-sm-2 control-label text-right"></label>
					<div class="col-sm-3 add-margin">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label text-right"><spring:message code="lbl.accountnumber" /> <span class="mandatory"></span> </label>
					<div class="col-sm-3 add-margin">
						<form:input path="accountnumber" class="form-control text-left patternvalidation" data-pattern="alphanumeric" maxlength="50" required="required" />
						<form:errors path="accountnumber" cssClass="error-msg" />
					</div>
					<label class="col-sm-2 control-label text-right"><spring:message code="lbl.fund" /> <span class="mandatory"></span></label>
					<div class="col-sm-3 add-margin">
						<form:select path="fund" data-first-option="false" id="fund" class="form-control" required="required">
							<form:option value=""><spring:message code="lbl.select" /></form:option>
							<form:options items="${funds}" itemValue="id" itemLabel="name" />
						</form:select>
						<form:errors path="fund" cssClass="error-msg" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label text-right"><spring:message code="lbl.accounttype" /> <span class="mandatory"></span> </label>
					<div class="col-sm-3 add-margin">
						<form:select path="accounttype" data-first-option="false" id="accounttype" class="form-control" required="required">
							<form:option value=""><spring:message code="lbl.select" /></form:option>
							<c:forEach items="${accounttypes}" var="accounttype" >
							   <form:option value="${accounttype.glcode} - ${accounttype.name}">${accounttype.glcode} - ${accounttype.name}</form:option>
							</c:forEach>
						</form:select>
						<form:errors path="accounttype" cssClass="error-msg" />
					</div>
					<label class="col-sm-2 control-label text-right"><spring:message code="lbl.description" /></label>
					<div class="col-sm-3 add-margin">
						<form:textarea path="narration" id="narration" class="form-control" maxlength="250"></form:textarea>
						<form:errors path="narration" cssClass="error-msg" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label text-right"><spring:message code="lbl.payto" /></label>
					<div class="col-sm-3 add-margin">
						<form:input path="payTo" class="form-control text-left patternvalidation" data-pattern="alphanumeric" maxlength="50" />
						<form:errors path="payTo" cssClass="error-msg" />
					</div>
					<label class="col-sm-2 control-label text-right"><spring:message code="lbl.usagetype" /><span class="mandatory"></span> </label>
					<div class="col-sm-3 add-margin">
						<form:select path="type" data-first-option="false" id="type" class="form-control" required="required">
							<form:option value=""><spring:message code="lbl.select" /></form:option>
							<c:forEach items="${usagetypes}" var="usagetype" >
							   <form:option value="${usagetype}">${usagetype}</form:option>
							</c:forEach>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label text-right"><spring:message code="lbl.isactive" /> </label>
					<div class="col-sm-3 add-margin">
						<form:checkbox path="isactive" />
						<form:errors path="isactive" cssClass="error-msg" />
					</div>
					<c:if test="${autoglcode}">
						<label class="col-sm-2 control-label text-right"></label>
						<div class="col-sm-3 add-margin">
						</div>
					</c:if>
					<c:if test="${!autoglcode}">
						<label class="col-sm-2 control-label text-right"><spring:message code="lbl.gl.code" /><span class="mandatory"></span></label>
						<div class="col-sm-3 add-margin">
						<form:input path="chartofaccounts.glcode" class="form-control text-left patternvalidation" data-pattern="numeric" maxlength="50" required="required" />
						<form:errors path="chartofaccounts.glcode" cssClass="error-msg" />
						</div>
					</c:if>
					
				</div>
			</div>
		</div>
	</div>
</div>
