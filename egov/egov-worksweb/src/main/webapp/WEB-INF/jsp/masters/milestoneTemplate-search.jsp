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
<%@ include file="/includes/taglibs.jsp"%>
<script>
function setupSubTypes(elem) {
	categoryId = elem.options[elem.selectedIndex].value;
	populatesubType({
		category : categoryId
	});
}

loadSubType = function(req, res) {
	if (dom.get('subTypeOfWork').value != "") {
		dom.get('subTypeOfWork').value = dom.get('subTypeOfWork').value;
	}
}

function submitMilestoneTemplateSearchForm() {
	var mode=document.getElementById('mode').value;
	document.milestoneTemplateSearchForm.status.disabled = false;
	document.milestoneTemplateSearchForm.action = '${pageContext.request.contextPath}/masters/milestoneTemplate-searchDetails.action?mode='+mode;
	document.milestoneTemplateSearchForm.submit();

}

function bodyOnLoad() {
	if (dom.get('typeOfWork').value != "") {
		dom.get('typeOfWork').value = dom.get('typeOfWork').value;
		populatesubType({
			category : dom.get('typeOfWork').value
		});
	}
}
</script>

<html>
<title><s:text name='page.title.milestone.template.search' /></title>
<body onload="bodyOnLoad()" class="yui-skin-sam">
<script
	src="<cdn:url value='/resources/js/milestonetemplate/milestonetemplate.js?rnd=${app_release_no}'/>"></script>
		
<s:hidden name="mode" id="mode" value="%{mode}" />
<input type="hidden" value="<s:text name='milestontemplate.modify.select' />" id='selectMilestoneTemplate'>
	<div class="new-page-header">
		<s:text name="page.title.milestone.template.search" />
	</div>
<div id="milestonetemplateerror" class="alert alert-danger" style="display:none;"></div>
	<s:if test="%{hasErrors()}">
		<div id="errorstyle" class="alert alert-danger">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:if test="%{hasActionMessages()}">
		<div class="messagestyle">
			<s:actionmessage theme="simple" />
		</div>
	</s:if>

	<s:form theme="simple" name="milestoneTemplateSearchForm" action="milestoneTemplate-searchDetails" cssClass="form-horizontal form-groups-bordered">
		<div class="alert alert-danger" id="milestoneTemplate_error"
			style="display: none;"></div>

		<div class="panel panel-primary" data-collapsed="0"
			style="text-align: left">
			<div class="panel-heading">
				<div class="panel-title">
					<s:text name='title.search.criteria' />
				</div>
			</div>
			<div class="form-group">
			<label class="col-sm-2 control-label text-right"> <s:text
					name="milestone.template.type" /> <span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
				<s:select headerKey="-1"
					headerValue="%{getText('estimate.default.select')}" name="typeOfWork"
					id="workType" cssClass="form-control"
					list="dropdownData.parentCategoryList" listKey="id"
					listValue="description" value="%{typeOfWork.id}"
					onChange="setupSubTypes(this);" />
				<egov:ajaxdropdown id="categoryDropdown" fields="['Text','Value']"
					dropdownId='subType'
					url='estimate/ajaxEstimate-subcategories.action'
					selectedValue="%{subTypeOfWork.id}" />
			</div>
			<label class="col-sm-2 control-label text-right"> <s:text
					name="milestone.template.subtype" />
			</label>
			<div class="col-sm-3 add-margin">
				<s:select headerKey="-1"
					headerValue="%{getText('estimate.default.select')}" name="subTypeOfWork"
					value="%{subType.id}" id="subType" cssClass="form-control"
					list="dropdownData.categoryList" listKey="id"
					listValue="description" />
			</div>
		</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label text-right"> <s:text
							name="milestone.template.search.code" />
					</label>
					<div class="col-sm-3 add-margin">
						<s:textfield name="code" value="%{code}" id="code"
							cssClass="form-control" />
					</div>
					<label class="col-sm-2 control-label text-right"> <s:text
							name="milestone.template.search.status" />
					</label>
					<div class="col-sm-3 add-margin">
						<s:select headerKey="-1"
							headerValue="%{getText('default.dropdown.select')}"
							list="#{'0':'INACTIVE', '1':'ACTIVE'}" name="status"
							value="%{status}" id="status" cssClass="form-control" />
					</div>
				</div>
			</div>

		<div class="row">
			<div class="col-xs-12 text-center buttonholdersearch">
				<input type="submit" class="btn btn-primary" value="Search"	id="searchButton" name="button" onclick="submitMilestoneTemplateSearchForm()" />&nbsp;
				<input type="button" class="btn btn-default" value="Reset" id="resetbutton" name="clear" onclick="this.form.reset();">&nbsp;
				<input type="button" class="btn btn-default" value="Close" id="closeButton" name="closeButton" onclick="window.close();" />
			</div>
		</div>
		<%@ include file='milestoneTemplate-searchResults.jsp'%>
		<s:hidden name="sourcepage" id="sourcepage" value="%{sourcepage}" />
	</s:form>
</body>
</html>
