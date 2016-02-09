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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<title>
	<s:if test="%{userMode=='new'}">
		<s:text name='licenseCategory.create' />
	</s:if>
	<s:elseif test="%{userMode=='edit'}">
		<s:text name='licenseCategory.modify' />
	</s:elseif>	
	<s:elseif test="%{userMode=='view'}">
		<s:text name='licenseCategory.view' />
	</s:elseif>	
	</title>
	<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css' context='/egi'/>"/>
	<script>

	function bodyOnLoad(){
		if(dom.get("userMode").value=='view' || dom.get("userMode").value=='success'){
			 dom.get("code").readOnly=true;
			 dom.get("name").readOnly=true;
			 jQuery("span").remove(".mandatory");
		} 
	}

	function reload(){
		dom.get("code").value="";
		dom.get("name").value="";
		document.licenseCategoryForm.action='${pageContext.request.contextPath}/masters/licenseCategory-newform.action';
    	document.licenseCategoryForm.submit();
		
	}

	function validateFormAndSubmit(){
		var code= dom.get("code").value;
		var name= dom.get("name").value;
		if (name == '' || name == null){
			showMessage('category_error', '<s:text name="tradelic.master.tradecategoryname.null" />');
			return false;
		} else if (code == '' || code == null){
			showMessage('category_error', '<s:text name="tradelic.master.tradecategorycode.null" />');
			return false;
		}
		else {
		    	clearMessage('category_error')
		    	document.licenseCategoryForm.action='${pageContext.request.contextPath}/masters/licenseCategory-save.action';
		    	document.licenseCategoryForm.submit();
		 	}
	}

	function validateData(obj,param){
		clearMessage('category_error');
		var screenType="categoryMaster"; 
		var name="";
		var code="";
		if(param=="name")
			name=obj.value;
		else if(param=="code")
			code=obj.value;
		makeJSONCall(["errorMsg","isUnique","paramType"],'${pageContext.request.contextPath}/masters/ajaxMaster-validateActions.action',
		    	{name:name,code:code,screenType:screenType},categorySuccessHandler,categoryFailureHandler);
	}

	categoryFailureHandler=function(){
		   showMessage('category_error','Unable to perform this action');
		   return false;
	}

	categorySuccessHandler = function(req,res){
	    var results=res.results;
	    if(results[0].isUnique=="false"){
		    if(!(results[0].errorMsg=="" || results[0].errorMsg==null)){
		    	showMessage('category_error',results[0].errorMsg);
		    	if(results[0].paramType=="name")
			    	dom.get("name").value="";
		    	else if(results[0].paramType=="code")
			    	dom.get("code").value="";
	 			return false;
	     	} 
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
<body onload="bodyOnLoad();">
	<div id="category_error" class="error-msg" style="display:none;"></div> 
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
			<s:form name="licenseCategoryForm" action="licenseCategory" theme="simple"
				cssClass="form-horizontal form-groups-bordered"> 
				<s:token name="%{tokenName()}"/> 
				<s:push value="model">
				<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading">
						<div class="panel-title text-left">
							<s:if test="%{userMode=='new'}">
								<s:text name='licenseCategory.create' />
							</s:if>
							<s:elseif test="%{userMode=='edit'}">
								<s:text name='licenseCategory.modify' />
							</s:elseif>	
							<s:elseif test="%{userMode=='view'}">
								<s:text name='licenseCategory.view' />
							</s:elseif>	
						</div>
					</div>
					<div class="panel-body custom-form">
					
						<s:hidden name="id" /> 
						<s:hidden name="userMode" id="userMode"/>
					
						<div class="form-group">
							<label for="field-1" class="col-sm-2 control-label text-right"><s:text
									name="licenseCategory.name.lbl" /><span id="mandatory" class="mandatory"></span></label>
							<div class="col-sm-3 add-margin">
								<s:textfield id="name"	name="name" value="%{name}" class="form-control patternvalidation" data-pattern="alphanumericwithspacehyphenunderscore" maxLength="64"  onchange="return validateData(this,'name')"/>
							</div>
							
							<label for="field-1" class="col-sm-2 control-label text-right"><s:text
									name="licenseCategory.code.lbl" /><span id="mandatory" class="mandatory"></span></label>
							<div class="col-sm-3 add-margin">
								<s:textfield id="code"	name="code" value="%{code}" class="form-control patternvalidation" data-pattern="alphanumericwithspacehyphenunderscore" maxLength="32"  onchange="return validateData(this,'code')"/>
							</div>
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