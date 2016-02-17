<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/includes/taglibs.jsp"%>
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

<div class="main-content"><div class="row"><div class="col-md-12"><div class="panel panel-primary" data-collapsed="0"><div class="panel-heading"><div class="panel-title">FeeType</div></div><div class="panel-body"><form:form role="form" action="feetype/create" modelAttribute="feeType" id="feeTypeform" cssClass="form-horizontal form-groups-bordered" enctype="multipart/form-data"><div class="form-group">
<label class="col-sm-3 control-label text-right"><spring:message code="lbl.name" />
<span class="mandatory"></span>
</label><div class="col-sm-3 add-margin">
<form:input  path="name" class="form-control text-left patternvalidation" data-pattern="alphanumeric" maxlength="32" required="required"/>
</div></div>
<label class="col-sm-2 control-label text-right"><spring:message code="lbl.code" />
</label><div class="col-sm-3 add-margin">
<form:input  path="code" class="form-control text-left patternvalidation" data-pattern="alphanumeric" maxlength="12" />
</div><div class="form-group">
<label class="col-sm-3 control-label text-right"><spring:message code="lbl.feeprocesstype" />
</label><div class="col-sm-3 add-margin">
<form:select path="feeProcessType.id" id="feeProcessType.id" cssClass="form-control" cssErrorClass="form-control error" >
<form:option value=""> <spring:message code="lbl.select"/> </form:option>
<form:options items="${feeProcessTypes}" itemValue="id" itemLabel="name"/>
</form:select>
</div></div>
</form:form> </div></div></div></div>