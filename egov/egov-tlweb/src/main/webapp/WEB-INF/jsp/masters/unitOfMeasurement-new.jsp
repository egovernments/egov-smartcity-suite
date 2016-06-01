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
		<s:text name='uommaster.create' />
	</s:if>
	<s:elseif test="%{userMode=='edit'}">
		<s:text name='uommaster.modify' />
	</s:elseif>	
	<s:elseif test="%{userMode=='view' || userMode=='success'}">
		<s:text name='uommaster.view' />
	</s:elseif>	
	</title>
	<script>

	function bodyOnLoad(){
		checkOrUncheck();
		if(document.getElementById("userMode").value=='view' || document.getElementById("userMode").value=='success'){
			 document.getElementById("code").readOnly=true;
			 document.getElementById("name").readOnly=true;
			 document.getElementById("active").disabled=true;
			 jQuery("span").remove(".mandatory");
		}
		if(document.getElementById("userMode").value=='edit'){
			document.getElementById("code").readOnly=true;
		}
		if(document.getElementById("userMode").value=='new'){
			 document.getElementById("active").checked=true;
		}
	}

	function reload(){
		document.getElementById("code").value="";
		document.getElementById("name").value="";
		document.uomForm.action='${pageContext.request.contextPath}/masters/unitOfMeasurement-newform.action';
    	document.uomForm.submit();
	}

	function checkOrUncheck(){
		if(document.getElementById('active').checked==true){
			document.getElementById('uomActive').value=true;
			document.getElementById('uomActive').checked=true;
		} else {
			document.getElementById('uomActive').value=false;
			document.getElementById('uomActive').checked=false;
		}
	}
	
	function validateFormAndSubmit(){
		var code= document.getElementById("code").value;
		var name= document.getElementById("name").value;
		if (name == '' || name == null){
			showMessage('uom_error', '<s:text name="tradelic.uommaster.name.null" />');
			return false;
		} else if (code == '' || code == null){
			showMessage('uom_error', '<s:text name="tradelic.uommaster.code.null" />');
			return false;
		}
		else {
		    	clearMessage('uom_error');
		    	document.uomForm.action='${pageContext.request.contextPath}/masters/unitOfMeasurement-save.action';
		    	document.uomForm.submit();
		 	}
	}

	function validateData(obj,param){
		clearMessage('uom_error');
		var screenType="uomMaster";
		var name="";
		var code="";
		if(param=="name")
			name=obj.value;
		else if(param=="code")
			code=obj.value;
		makeJSONCall(["errorMsg","isUnique","paramType"],'${pageContext.request.contextPath}/masters/ajaxMaster-validateActions.action',
		    	{name:name,code:code,screenType:screenType},uomSuccessHandler,uomFailureHandler);
	}

	uomFailureHandler=function(){
		   showMessage('uom_error','Unable to perform this action');
		   return false;
	}

	uomSuccessHandler = function(req,res){
	    var results=res.results;
	    if(results[0].isUnique=="false"){
		    if(!(results[0].errorMsg=="" || results[0].errorMsg==null)){
		    	showMessage('uom_error',results[0].errorMsg);
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
	<div id="uom_error" class="error-msg" style="display:none;"></div> 
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
			<s:form name="uomForm" action="unitOfMeasurement" theme="simple"
				cssClass="form-horizontal form-groups-bordered"> 
				<s:token name="%{tokenName()}"/> 
				<s:push value="model">
				<div class="panel panel-primary" data-collapsed="0">
					<div class="panel-heading">
						<div class="panel-title text-left">
							<s:if test="%{userMode=='new'}">
								<s:text name='uommaster.create' />
							</s:if>
							<s:elseif test="%{userMode=='edit'}">
								<s:text name='uommaster.modify' />
							</s:elseif>	
							<s:elseif test="%{userMode=='view'}">
								<s:text name='uommaster.view' />
							</s:elseif>	
							<s:else>
								<s:text name='uommaster.lbl' />
							</s:else>
						</div>
					</div>
					<div class="panel-body custom-form">
					
						<s:hidden name="id"/> 
						<s:hidden name="userMode" id="userMode"/>
						<s:hidden name="uomActive" id="uomActive"/>
					
						<div class="form-group">
							<label for="field-1" class="col-sm-2 control-label text-right"><s:text
									name="uommaster.name.lbl" /><span class="mandatory"></span></label>
							<div class="col-sm-3 add-margin">
								<s:textfield id="name"	name="name" class="form-control patternvalidation" data-pattern="alphabetwithspacehyphenunderscore" maxLength="32" value="%{name}" onchange="return validateData(this,'name')"/>
							</div>
							
							<label for="field-1" class="col-sm-2 control-label text-right"><s:text
									name="uommaster.code.lbl" /><span class="mandatory"></span></label>
							<div class="col-sm-3 add-margin">
								<s:textfield id="code"	name="code" class="form-control patternvalidation" data-pattern="alphabetwithspace" value="%{code}" onchange="return validateData(this,'code')"  maxLength="5"/>
							</div>
						</div>
						
						<div class="form-group">
							<label for="field-1" class="col-sm-2 control-label text-right"><s:text
									name="uommaster.active.lbl" /> :</label>
							<div class="col-sm-3 add-margin text-left">
								<s:checkbox id="active"	name="active" value="%{active}"  onclick="checkOrUncheck();"/>
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