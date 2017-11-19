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

<%@ include file="/includes/taglibs.jsp" %>

<html>
<title><s:text name='page.title.milestone.template'/></title>
<body class="yui-skin-sam">
<script src="<cdn:url value='/resources/js/milestonetemplate/milestonetemplate.js?rnd=${app_release_no}'/>"></script>
	<div class="new-page-header">
		<s:if test="%{model.id == null}">
			<s:text name="milestone.template.create" />
		</s:if>
		<s:else>
			<s:text name="milestone.template.modify" />
		</s:else>
	</div>
<div id="milestonetemplateerror" class="alert alert-danger" style="display:none;"></div>
    <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="alert alert-danger" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
    <input type="hidden" value="<s:text name='milestonetemplate.code.not.null' />" id='templateCode'>
    <input type="hidden" value="<s:text name='milestonetemplate.name.not.null' />" id='templateName'>
    <input type="hidden" value="<s:text name='milestonetemplate.description.not.null' />" id='templateDesc'>
    <input type="hidden" value="<s:text name='milestone.template.search.workType.error' />" id='selectTypeOfWork'>
    <input type="hidden" value="<s:text name='milestoneTemplateActivity.desc.null' />" id='description'>
    <input type="hidden" value="<s:text name='milestoneTemplateActivity.stageOrderNo.null' />" id='stageOrderNo'>
    <input type="hidden" value="<s:text name='milestoneTemplateActivity.percentage.null' />" id='tempPercentage'>
    <input type="hidden" value="<s:text name='milestone.activity.total.percentage' />" id='validateTotalPercentage'>
    <s:if test="%{hasActionMessages()}">
        <div class="messagestyle">
        	<s:property value="%{code}"/> &nbsp; <s:actionmessage theme="simple"/>
        </div>
    </s:if>
    <s:form action="milestoneTemplate-save" theme="simple" name="milestoneTemplateForm" cssClass="form-horizontal form-groups-bordered" >
    <s:token/>
	<s:push value="model">
	<s:hidden name="id" value="%{id}" id="id"/>
    <s:hidden name="mode" id="mode" />
    <s:hidden name="sourcepage" value="%{sourcepage}" id="sourcepage"/>
<s:if test="%{id!=null}">
    <s:hidden name="mode" value="edit" id="mode"/>
</s:if> 
	
<%@ include file='milestoneTemplate-header.jsp'%>
<%@ include file='milestoneTemplateActivity.jsp'%>

 <div class="row">
		<div class="col-xs-12 text-center buttonholdersearch">
			<input type="hidden" name="actionName" id="actionName" />
			<s:if test="%{id!=null}" >
				<s:submit type="submit" cssClass="btn btn-primary" value="Modify" id="saveButton" name="button" method="save" onclick="return validateFormBeforeSubmit();" />&nbsp;
			</s:if>
			<s:else>
				<s:submit type="submit" cssClass="btn btn-primary" value="Save"	id="saveButton" name="button" method="save" onclick="return validateFormBeforeSubmit();" />&nbsp;
				<input type="button" class="btn btn-default" value="Clear" id="clear" name="clear" onclick="this.form.reset();">&nbsp;
			</s:else>
				<input type="button" class="btn btn-default" value="Close" id="closeButton" name="closeButton" onclick="window.close();" />
		</div>
 </div>

</s:push>
</s:form>
</body>
</html>
