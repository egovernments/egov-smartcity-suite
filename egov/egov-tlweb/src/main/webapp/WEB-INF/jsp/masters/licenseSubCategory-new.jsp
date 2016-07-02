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
	<title>
	<s:if test="%{userMode=='new'}">
		<s:text name='licenseSubCategory.create' />
	</s:if>
	<s:elseif test="%{userMode=='edit'}">
		<s:text name='licenseSubCategory.modify' />
	</s:elseif>	
	<s:elseif test="%{userMode=='view'}">
		<s:text name='licenseSubCategory.view' />
	</s:elseif>	
	</title>
	<script>

	function bodyOnLoad(){
		if(document.getElementById("userMode").value=='view'  || document.getElementById("userMode").value=='success'){
			 for(i=0;i<document.licenseSubCategoryForm.elements.length;i++){ 
					if(document.licenseSubCategoryForm.elements[i].id!='btnclose'){
					document.licenseSubCategoryForm.elements[i].disabled=true;
					document.licenseSubCategoryForm.elements[i].readonly=true;
					} 
			 }
			 subCategoryMappingDataTable.removeListener('cellClickEvent');
			 jQuery("span").remove(".mandatory");  
		}
	}


	function reload(){
		document.licenseSubCategoryForm.reset();
		document.licenseSubCategoryForm.action='${pageContext.request.contextPath}/masters/licenseSubCategory-newform.action';
    	document.licenseSubCategoryForm.submit();
	}

	function validateFormAndSubmit(){
		var code= document.getElementById("code").value;
		var name= document.getElementById("name").value;
		var categoryId= document.getElementById("categoryId").value;
		if (categoryId == '-1'){
			showMessage('subcategory_error', '<s:text name="tradelic.master.tradesubcategoryid.null" />');
			return false;
		}  else if (name == '' || name == null){
			showMessage('subcategory_error', '<s:text name="tradelic.master.tradesubcategoryname.null" />');
			return false;
		} else if (code == '' || code == null){
			showMessage('subcategory_error', '<s:text name="tradelic.master.tradesubcategorycode.null" />');
			return false;
		}
		if(!validateMappingDetails())
			return false;
		else {
		    	clearMessage('subcategory_error')
		    	document.licenseSubCategoryForm.action='${pageContext.request.contextPath}/masters/licenseSubCategory-save.action';
		    	document.licenseSubCategoryForm.submit();
		 	}
	}

	function validateMappingDetails(){ 
		var records= subCategoryMappingDataTable.getRecordSet();
	   	for(var i=0;i<subCategoryMappingDataTable.getRecordSet().getLength();i++)
	   	{
	   	  	var record = subCategoryMappingDataTable.getRecord(i);
	   		 if(document.getElementById("feeType"+record.getId()).value==0 || document.getElementById("rateType"+record.getId()).value==0 ||
	   				document.getElementById("uom"+record.getId()).value==0){
	   			document.getElementById("scDtl_error").innerHTML='Please select all the details for '+(i+1)+' row.'; 
	            document.getElementById("scDtl_error").style.display='';
	            return false;
	    	  }
	   	}
	   	return true;
	}

	function validateData(obj,param){
		clearMessage('subcategory_error');
		var screenType="subcategoryMaster"; 
		var name="";
		var code="";
		var subcategoryid= document.getElementById("licenseSubCategory_id").value;
		if(param=="name")
			name=obj.value;
		else if(param=="code")
			code=obj.value;
		makeJSONCall(["errorMsg","isUnique","paramType"],'${pageContext.request.contextPath}/masters/ajaxMaster-validateActions.action',
		    	{name:name, code:code, subcategoryId:subcategoryid, screenType:screenType},subcategorySuccessHandler,subcategoryFailureHandler);
	}

	subcategoryFailureHandler=function(){
		   showMessage('subcategory_error','Unable to perform this action');
		   return false;
	}

	subcategorySuccessHandler = function(req,res){
	    var results=res.results;
	    if(results[0].isUnique=="false"){
		    if(!(results[0].errorMsg=="" || results[0].errorMsg==null)){
		    	showMessage('subcategory_error',results[0].errorMsg);
		    	if(results[0].paramType=="name")
			    	document.getElementById("name").value="";
		    	else if(results[0].paramType=="code")
			    	document.getElementById("code").value="";
	 			return false;
	     	} 
	    }
	 }
	

	</script>
</head>
<body onload="bodyOnLoad();">
	<div id="subcategory_error" class="error-msg" style="display:none;"></div> 
	<div class="row">
		<div class="col-md-12">
			<div class="panel-body">
			<s:if test="%{hasErrors()}">
				<div align="left">
					<s:actionerror />
					<s:fielderror/>
				</div>			
			</s:if>
			<s:if test="%{hasActionMessages()}">
			<div class="messagestyle">
				<s:actionmessage theme="simple" />
			</div>
			</s:if>
			<s:form name="licenseSubCategoryForm" action="licenseSubCategory" theme="simple"
				cssClass="form-horizontal form-groups-bordered"> 
				<s:token name="%{tokenName()}"/> 
				<s:push value="model">
				<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading">
						<div class="panel-title text-left">
							<s:if test="%{userMode=='new'}">
								<s:text name='licenseSubCategory.create' />
							</s:if>
							<s:elseif test="%{userMode=='edit'}">
								<s:text name='licenseSubCategory.modify' />
							</s:elseif>	
							<s:elseif test="%{userMode=='view'}">
								<s:text name='licenseSubCategory.view' />
							</s:elseif>	
						</div>
					</div>
					<div class="panel-body custom-form">
					
						<s:hidden name="id" /> 
						<s:hidden name="userMode" id="userMode"/>
						<s:hidden name="licenseFee" id="licenseFee"/>
						<s:hidden name="feeExists" id="feeExists"/>
						<div class="form-group">
							<label for="field-1" class="col-sm-2 control-label text-right"><s:text
									name="licenseSubCategory.category.lbl" /><span class="mandatory"></span></label>
							<s:if test="%{userMode=='edit'}">
								<div class="col-sm-3 add-margin">
									<s:select headerKey="-1"
											headerValue="%{getText('default.select')}" name="categoryId"
											id="categoryId" listKey="key" listValue="value"
											list="licenseCategoryMap" cssClass="form-control" value="%{categoryId}" disabled="true"/>
								</div>
							</s:if>
							<s:else>
								<div class="col-sm-3 add-margin">
									<s:select headerKey="-1"
											headerValue="%{getText('default.select')}" name="categoryId"
											id="categoryId" listKey="key" listValue="value"
											list="licenseCategoryMap" cssClass="form-control" value="%{categoryId}" 
											/>
								</div>
							</s:else>
						</div>
					
						<div class="form-group">
							<label for="field-1" class="col-sm-2 control-label text-right"><s:text
									name="licenseSubCategory.name.lbl" /><span class="mandatory"></span></label>
							<div class="col-sm-3 add-margin">
								<s:textfield id="name"	name="name" value="%{name}" class="form-control patternvalidation" data-pattern="alphanumericwithspacehyphenunderscore" maxLength="64" onchange="return validateData(this,'name')"/>
							</div>
							<label for="field-1" class="col-sm-2 control-label text-right"><s:text
									name="licenseSubCategory.code.lbl" /><span class="mandatory"></span></label>
							<s:if test="%{userMode=='edit'}">
								<div class="col-sm-3 add-margin">
									<s:textfield id="code"	name="code" value="%{code}" class="form-control patternvalidation" data-pattern="alphanumericwithspacehyphenunderscore" maxLength="32" onchange="return validateData(this,'code')" readonly="true"/>
								</div>
							</s:if>
							<s:else>
								<div class="col-sm-3 add-margin">
									<s:textfield id="code"	name="code" value="%{code}" class="form-control patternvalidation" data-pattern="alphanumericwithspacehyphenunderscore" maxLength="32" onchange="return validateData(this,'code')"/>
								</div>
							</s:else>
						</div>
						
						<div>
            				<%@ include file="subCategory-details.jsp"%>  
						</div>
						
					</div>
				</div>
				</s:push>
			</s:form>

			<div class="row">
				<div class="text-center">
					<s:if test="%{userMode!='view' && userMode!='success'}">
						<button type="submit" id="btnsave" class="btn btn-primary" onclick="return validateFormAndSubmit();">
							Save</button>
						<button type="button" id="btnReset" type="reset" class="btn btn-default" onclick="reload();">
						Reset</button>
					</s:if>
					<button type="button" id="btnclose" class="btn btn-default" onclick="window.close();">
						Close</button>
				</div>
			</div>
		</div>
		</div>
	</div>
</body>
</html>