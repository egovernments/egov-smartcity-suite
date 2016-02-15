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
<%@ include file="/includes/taglibs.jsp"%>
<html>
<head>
<title><s:text name="page.title.renewtrade" /></title>
<sx:head />
</head>
<body>
	<div class="row">
		<div class="col-md-12">
			<s:if test="%{hasErrors()}">
				<div align="center" class="error-msg">
					<s:actionerror />
					<s:fielderror />
				</div>
			</s:if>
			<s:if test="%{hasActionMessages()}">
				<div class="messagestyle">
					<s:actionmessage theme="simple" />
				</div>
			</s:if>
			<s:form action="newTradeLicense-renewal" theme="simple" name="renewForm" cssClass="form-horizontal form-groups-bordered">
				<s:token />
				<s:push value="model">
					<s:hidden name="docNumber" />
					<s:hidden name="model.id" />
					<s:hidden id="detailChanged" name="detailChanged"></s:hidden>
					<s:hidden name="feeTypeId" id="feeTypeId" />
					<div class="panel panel-primary" data-collapsed="0">
                            <div class="panel-heading">
								<div class="panel-title" style="text-align:center"> 
									<s:text name="page.title.renewtrade" />
								</div>
                            </div>
                            
							<%@ include file='../common/view.jsp'%>
							
							<div class="panel-heading custom_form_panel_heading">
    							<div class="panel-title">Editable <s:text name='license.details.lbl' /></div>
							</div>
							<div class="form-group">
							    <label class="col-sm-3 control-label text-right"><s:text name='license.category.lbl' /><span class="mandatory"></span></label>
							    <div class="col-sm-3 add-margin">
							        <s:select name="category" id="category" list="dropdownData.categoryList"
								listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('default.select')}" value="%{category.id}" class="form-control" onChange="setupAjaxSubCategory(this);" />
								<egov:ajaxdropdown id="populateSubCategory" fields="['Text','Value']" dropdownId='subCategory' url='domain/commonTradeLicenseAjax-populateSubCategory.action' />
							    </div>
							    
							    <label class="col-sm-2 control-label text-right"><s:text name='license.subCategory.lbl' /><span class="mandatory"></span></label>
							    <div class="col-sm-3 add-margin">
							        <s:select name="tradeName" id="subCategory" list="dropdownData.subCategoryList"
								listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('default.select')}" value="%{tradeName.id}" class="form-control"/>
							    </div> 
							</div>
							
							<div class="form-group">
							    <label class="col-sm-3 control-label text-right"><s:text name='license.uom.lbl' /><span class="mandatory"></span></label>
							     <div class="col-sm-3 add-margin">
							        <s:textfield name="uom" maxlength="8" id="uom" value="%{tradeName.licenseSubCategoryDetails[0].uom.name}"  readOnly="true" class="form-control"  />
							    </div>
							    <label class="col-sm-2 control-label text-right"><s:text name='license.premises.lbl' /><span class="mandatory"></span></label>
							    <div class="col-sm-3 add-margin">
							        <s:textfield name="tradeArea_weight" maxlength="8" id="tradeArea_weight" value="%{tradeArea_weight}" cssClass="form-control patternvalidation"  data-pattern="number"  />
							    </div>
							</div>
							<div>
								<%@ include file='../common/commonWorkflowMatrix.jsp'%>
								<%@ include file='../common/commonWorkflowMatrix-button.jsp'%>
							</div>
						</div>
				</s:push>
			</s:form>
		</div>
	</div>
	<script src="../resources/js/app/newtrade.js"></script>
	<script>
	jQuery('#subCategory').change(function(){
		jQuery.ajax({
			url: "../domain/commonTradeLicenseAjax-ajaxLoadUomName.action", 
			type: "GET",
			data: {
				subCategoryId : jQuery('#subCategory').val(),
				feeTypeId :  jQuery('#feeTypeId').val()
			},
			cache: false,
			dataType: "json",
			success: function (response) {
				jQuery('#uom').val(response.uom);
			}, 
			error: function (response) {
				console.log("failed");
				jQuery('#uom').val('');
				bootbox.alert("No UOM mapped for SubCategory")
			}
		})});
	
	</script>
</body>
</html>
