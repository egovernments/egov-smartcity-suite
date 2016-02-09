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
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title><s:text name='licenseSubCategory.Search' /></title>
	<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css' context='/egi'/>"/>
	<script>
	function validateFormAndSubmit(){
			var subCategoryId= dom.get("subCategoryId").value;
			 if (subCategoryId == "-1"){
				showMessage('subCategory_error', '<s:text name="licenseSubCategory.subCategory.null" />');
				return false;
			} else {
			    	clearMessage('subCategory_error')
			    	document.licenseSubCategoryForm.action='${pageContext.request.contextPath}/masters/licenseSubCategory-newform.action?id=' + subCategoryId;
			    	document.licenseSubCategoryForm.submit();
			 	}
		}
	</script>
	<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.8/datatable/assets/skins/sam/datatable.css"/>	
	<link rel="stylesheet" type="text/css" href="/egi/commonyui/yui2.8/assets/skins/sam/autocomplete.css" />	
	
	<script type="text/javascript" src="/egi/commonyui/yui2.8/yahoo-dom-event/yahoo-dom-event.js"></script> 
	<script type="text/javascript" src="/egi/commonyui/yui2.8/element/element-min.js"></script>
	<script type="text/javascript" src="/egi/commonyui/yui2.8/connection/connection-min.js"></script>
	<script type="text/javascript" src="/egi/commonyui/yui2.8/datasource/datasource-min.js"></script>
	<script type="text/javascript" src="/egi/commonyui/yui2.8/datatable/datatable-min.js"></script>
	
	<script type="text/javascript" src="<c:url value='/resources/js/helper.js'/>"></script>
</head>
<body>
	<div id="subCategory_error" class="error-msg" style="display:none;"></div> 
	<div class="row">
		<div class="col-md-12">
			<div class="panel-body">
			<s:if test="%{hasErrors()}">
				<div align="left">
					<s:actionerror />
					<s:fielderror/>
				</div>			
			</s:if>
			<s:form name="licenseSubCategoryForm" action="licenseSubCategory" theme="simple"
				cssClass="form-horizontal form-groups-bordered"> 
				<s:token name="%{tokenName()}"/> 
				<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading">
						<div class="panel-title text-left"><s:text name='licenseSubCategory.Search' /></div>
					</div>
					<div class="panel-body custom-form">
						<s:hidden name="userMode" id="userMode"/>
						<div class="form-group">
							<label for="field-1" class="col-sm-4 control-label text-right"><s:text
									name="licenseSubCategory.subCategory.lbl" /><span class="mandatory"></span></label>
							<div class="col-sm-4 add-margin">
								<s:select headerKey="-1"
										headerValue="%{getText('default.select')}" name="subCategoryId"
										id="subCategoryId" listKey="key" listValue="value"
										list="licenseSubCategoryMap" cssClass="form-control" value="%{subCategoryId}" 
										/>
							</div>
						</div>
					</div>
				</div>
			</s:form>

			<div class="row">
				<div class="text-center">
				<s:if test="%{userMode=='edit'}">
					<button type="button" id="btnedit" class="btn btn-primary" onclick="return validateFormAndSubmit();">
						Modify</button>
				</s:if>
				<s:elseif test="%{userMode=='view'}">
					<button type="button" id="btnview" class="btn btn-primary" onclick="return validateFormAndSubmit();">
						View</button>
				</s:elseif>
					<button type="button" id="btnclose" class="btn btn-default" onclick="window.close();">
						Close</button>
				</div>
			</div>
		</div>
		</div>
	</div>
</body>
</html>