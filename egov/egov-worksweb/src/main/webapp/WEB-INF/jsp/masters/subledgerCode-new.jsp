<!-- -------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency,
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It
# 	   is required that all modified versions of this material be marked in
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program
# 	   with regards to rights under trademark law for use of the trade names
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ include file="/includes/taglibs.jsp" %> 

<html>
  <head>
     <title><s:text name='page.title.subledgerCode'/></title>
  </head>
<script src="<egov:url path='resources/js/works.js'/>"></script>
  <script type="text/javascript">
  
  var currentDate='<%=new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date())%>';
  
  function onBodyLoad(){
		// To show fields based on Account Entity Types - Deposit / Project Code  
   	  	<s:if test="%{prjctCode==true}">
   	  		document.subledgerCode.prjctCode.checked=true;
   	  		document.subledgerCode.prjctCode.value=true;
   	  		document.subledgerCode.depCode.checked=false;
   	  		document.subledgerCode.depCode.value=false;
  	  		dom.get("codeName1").innerHTML='<span class="mandatory">*</span>'+"<s:text name="projectCode.name" />";
  	  		dom.get("description1").innerHTML="<s:text name="projectCode.description" />";  
  	  	</s:if>	 
  	  	<s:if test="%{depCode==true}"> 
   	  		document.subledgerCode.prjctCode.checked=false;
   	  		document.subledgerCode.prjctCode.value=false;
   	  		document.subledgerCode.depCode.checked=true;
   	  		document.subledgerCode.depCode.value=true;
  	  		dom.get("codeName1").innerHTML='<span class="mandatory">*</span>'+"<s:text name="depositCode.work.name" />";
  	  		dom.get("description1").innerHTML="<s:text name="depositCode.work.description" />";
  	  	</s:if>	
  	  	
  		// To Enable onChange Event for parent dropdowns in case child dropdowns are present
  	  	if(document.subledgerCode.scheme!=undefined)
  	  	    	   document.subledgerCode.fund.setAttribute("onChange","setupSchemes(this);"); 
	  	if(document.subledgerCode.subScheme!=undefined)
  	  	    	   document.subledgerCode.scheme.setAttribute("onChange","setupSubSchemes(this);");  
  	  	if(document.subledgerCode.ward!=undefined)
  	  	    	   document.subledgerCode.zone.setAttribute("onChange","setupAjaxWards(this);"); 
  	  	if(document.subledgerCode.subTypeOfWork!=undefined)
  	  	    	   document.subledgerCode.typeOfWork.setAttribute("onChange","setupSubTypes(this);");
  }
  
  // To show fields based on Account Entity Types - Deposit / Project Code 
  function viewInputFields(val){
	    if(val=='dc'){
  	  		window.open('${pageContext.request.contextPath}/masters/subledgerCode!newform.action?depCode=true','_self');
	    }else if(val=='pc'){  
	    	window.open('${pageContext.request.contextPath}/masters/subledgerCode!newform.action?prjctCode=true','_self');
	    }
  }
   	
  //To load SubType Of Work based on Type Of Work 	
  function setupSubTypes(elem){
   		clearMessage('subledgerCodeError');
	    categoryId=elem.options[elem.selectedIndex].value;
	    populatesubTypeOfWork({category:categoryId}); 
	}
	
  //To load Ward based on Zone
  function setupAjaxWards(elem){
 		clearMessage('subledgerCodeError');
	    zone_id=elem.options[elem.selectedIndex].value;
	    populateward({zoneId:zone_id});
	}
  
  //To load Scheme based on Fund
  function setupSchemes(elem){
		clearMessage('subledgerCodeError');
	    var fundElem = document.subledgerCode.fund;
		var fundId = fundElem.options[elem.selectedIndex].value;
		if(fundId !='-1'){
			var id=elem.options[elem.selectedIndex].value;
		    var date=currentDate;
		    populatescheme({fundId:id,estimateDate:date});
	    }
	}
	
	//To load SubScheme based on Scheme
	function setupSubSchemes(elem){
		clearMessage('subledgerCodeError');
		var id=elem.options[elem.selectedIndex].value;
	    var date=currentDate;
	  	populatesubScheme({schemeId:id,estimateDate:date});
		
	}
	
	//Show Message if scheme is selected without selecting  Fund 
	function checkFund(elem){
	 var fundElem = document.subledgerCode.fund;
	 if(fundElem.value =='-1' && elem.value=='-1')
	   		showMessage('subledgerCodeError',"<s:text name="sl.checkFund.null" />");
	}
	
	//Show Message if SubScheme is selected without selecting Scheme 
	function checkScheme(elem){
	 var schemeElem = document.subledgerCode.scheme;
	 if(schemeElem.value =='-1' && elem.value=='-1')
	   	showMessage('subledgerCodeError',"<s:text name="sl.checkScheme.null" />");
	}
	
	//Show Message if SubType Of Work is selected without selecting Type Of Work 
	function checkTypeOfWork(elem){
		var typeOfWorkElem =document.subledgerCode.typeOfWork;
		if(typeOfWorkElem.value =='-1' && elem.value=='-1')
	   		showMessage('subledgerCodeError',"<s:text name="sl.checkTypeOfWork.null" />");
	}
	
	//Show Message if Ward is selected without selecting Zone 
	function checkZone(elem){
		var zoneElem = document.subledgerCode.zone;
		if(zoneElem.value =='-1' && elem.value=='-1')
	   		showMessage('subledgerCodeError',"<s:text name="sl.checkZone.null" />");
	}
	
	//To Validate Fields Before Saving the Transaction	
	function validateBeforeSubmit(){
	//Fields to be shown are defined in python script. 
	//This piece of code is to validate mandatory fields returned from the script and other default fields.
	 	for(i=0;i<document.subledgerCode.elements.length;i++){
		 	 if(document.subledgerCode.elements[i].value=="" || document.subledgerCode.elements[i].value==null || document.subledgerCode.elements[i].value=='-1'){
				 	if(document.getElementById((document.subledgerCode.elements[i].id)+1).innerHTML.split("*")[1]!=undefined){
			 	 	    dom.get("subledgerCodeError").innerHTML=document.getElementById((document.subledgerCode.elements[i].id)+1).innerHTML.split(":")[0].split("*")[1]+" is required";
			 	 	    dom.get("subledgerCodeError").style.display=''; 
			 	 	 	return false;
			 	 	 }
		 	   }
	 	}
	  dom.get("subledgerCodeError").style.display='none'; 
	  dom.get("subledgerCodeError").innerHTML='';
	  return true; 
	}
  
  </script>
  <body  onload="onBodyLoad()" class="simple">
  	<div class="new-page-header">
		Generate Deposit Code
	</div>
    <s:form action="subledgerCode" theme="simple" name="subledgerCode" cssClass="form-horizontal form-groups-bordered"> 
    <s:token/>
    <s:push value="model">  
    <div id="subledgerCodeError" class="alert alert-danger" style="display:none;"></div>
    <s:if test="%{hasErrors()}">
        <div id="errorstyle" class="alert alert-danger" >
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
    <s:if test="%{hasActionMessages()}">
        <div class="alert alert-success">
        	<s:actionmessage theme="simple"/>
        </div>
    </s:if>
    
    <div class="panel panel-primary" data-collapsed="0" style="text-align:left">
				<div class="panel-heading">
					<div class="panel-title"></div>
				</div>
				<div class="panel-body no-margin-bottom">
					<div class="form-group">
						<label class="col-sm-2 control-label text-right">
						    <s:text name="subledgerCode.type.depositCode" />
						</label>
						<div class="col-sm-3 add-margin">
							<input name="depCode" type="radio" id="depCode" onClick="viewInputFields('dc');"  value='%{depCode}'/>
						</div>
						<label class="col-sm-2 control-label text-right">
						    <s:text name="subledgerCode.type.projectCode" />
						</label>
						<div class="col-sm-3 add-margin">
							<input name="prjctCode" type="radio" id="prjctCode" onClick="viewInputFields('pc');"/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-2 control-label text-right">
						    <s:text name="depositCode.work.name" /> <span class="mandatory"></span>
						</label>
						<div class="col-sm-3 add-margin">
							<s:textfield name="codeName" type="text" cssClass="form-control" id="codeName" value="%{codeName}"/>
						</div>
						<label class="col-sm-2 control-label text-right">
						    <s:text name="depositCode.work.description" />
						</label>
						<div class="col-sm-3 add-margin">
							<s:textarea name="description" cols="35" cssClass="form-control" id="description" value="%{description}"/>
						</div>
					</div>
					
					<div class="form-group">
						<label id="financialYear1" class="col-sm-2 control-label text-right">
						    <s:text name='subledgerCode.financialYear'/> <span class="mandatory"></span>
						</label>
						<div class="col-sm-3 add-margin">
							<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="financialYear" id="financialYear" cssClass="form-control" list="dropdownData.financialYearList" listKey="id" listValue="finYearRange" value="%{currentFinancialYearId}" />
						</div>
						<s:if test="%{depCode==true || (prjctCode==true && list.contains('fund') || list.contains('scheme') || list.contains('subScheme')) }" >
							<label class="col-sm-2 control-label text-right">
							    <s:text name='subledgerCode.fund'/><span class="mandatory"></span>
							</label>
							<div class="col-sm-3 add-margin">
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="fund" id="fund" cssClass="form-control" list="dropdownData.fundList" listKey="id" listValue="name" value="%{fund.id}" />
						         	<egov:ajaxdropdown id="schemeDropdown" fields="['Text','Value']" dropdownId='scheme' url='estimate/ajaxFinancialDetail!loadSchemes.action' selectedValue="%{scheme.id}"/>
							</div>
					    </s:if>  
					</div>
					
					<div class="form-group">
						<s:if test="%{list.contains('department')}" >
			          		 <label class="col-sm-2 control-label text-right">
							    <s:text name="subledgerCode.executing.department" /> <span class="mandatory"></span>
							</label>
							<div class="col-sm-3 add-margin">
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="department" id="department" cssClass="form-control" list="dropdownData.departmentList" listKey="id" listValue="deptName" value="%{department.id}" />
							</div>
			          	</s:if> 
						<label class="col-sm-2 control-label text-right">
						    <s:text name="subledgerCode.fundSource.name" /> <span class="mandatory"></span>
						</label>
						<div class="col-sm-3 add-margin">
							<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="fundSource" id="fundSource" 
		               			 cssClass="form-control" list="dropdownData.fundSourceList" listKey="id" listValue="name" value="%{fundSource.id}"/>
						</div>
					</div>
					
					<s:if test="%{list.contains('typeOfWork') || list.contains('subTypeOfWork') || list.contains('subTypeOfWork')}" >
						<div class="form-group">
							<s:if test="%{list.contains('typeOfWork') || list.contains('subTypeOfWork')}" >
				            	<label class="col-sm-2 control-label text-right">
								    <s:text name="subledgerCode.typeOfWork" /> <span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="typeOfWork" id="typeOfWork" cssClass="form-control" list="dropdownData.typeOfWorkList" listKey="id" listValue="description" value="%{typeOfWork.id}"/>
					            	<egov:ajaxdropdown id="subTypeOfWorkDropdown" fields="['Text','Value']" dropdownId='subTypeOfWork' url='estimate/ajaxEstimate!subcategories.action' selectedValue="%{subTypeOfWork.id}"/>
								</div>
							</s:if>
							
							<s:if test="%{list.contains('subTypeOfWork')}" >
				            	<label class="col-sm-2 control-label text-right">
								    <s:text name="subledgerCode.subTypeOfWork" /><span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="subTypeOfWork" value="%{subTypeOfWork.id}" id="subTypeOfWork" cssClass="form-control" list="dropdownData.subTypeOfWorkList" listKey="id" listValue="description" onClick="checkTypeOfWork(this);"/>
								</div>
				            </s:if>
						</div>
					</s:if>
					
					<s:if test="%{list.contains('function') || list.contains('natureOfWork')}" >
						<div class="form-group">
							<s:if test="%{list.contains('function')}" >
				       			<label class="col-sm-2 control-label text-right">
								    <s:text name='subledgerCode.function'/> <span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="function" id="function" cssClass="form-control" list="dropdownData.functionList" listKey="id" listValue="name" value="%{function.id}" />
								</div>
				          	</s:if>  
							<s:if test="%{list.contains('natureOfWork')}" >
				           		<label class="col-sm-2 control-label text-right">
								    <s:text name="subledgerCode.natureOfWork" /> <span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="natureOfWork" id="natureOfWork" cssClass="form-control" list="dropdownData.natureOfWorkList" listKey="id" listValue="name" value="%{natureOfWork.id}" />
								</div>
				       		</s:if>
						</div>
					</s:if>
					
					<s:if test="%{list.contains('zone') || list.contains('ward') || list.contains('ward')}" >
					<div class="form-group">
					    <s:if test="%{list.contains('zone') || list.contains('ward')}" >
				          		<label class="col-sm-2 control-label text-right">
								    <s:text name="subledgerCode.zone" /> <span class="mandatory"></span>
								</label>
								<div class="col-sm-3 add-margin">
									<s:select id="zone" name="zone" cssClass="form-control" 
																list="dropdownData.zoneList" listKey="id" listValue="name" 
																headerKey="-1" headerValue="%{getText('default.dropdown.select')}"
																value="%{zone.id}" />	
																<egov:ajaxdropdown id="populateWard"
													fields="['Text','Value']" dropdownId='ward'
													url='masters/ajaxSubledgerCode!populateWard.action' />
								</div>
				         </s:if> 
						
						<s:if test="%{list.contains('ward')}" >     
			                <label class="col-sm-2 control-label text-right">
							    <s:text name="subledgerCode.ward" /><span class="mandatory"></span>
							</label>
							<div class="col-sm-3 add-margin">
								<s:select id="ward" name="ward" cssClass="form-control" 
													list="dropdownData.wardList" listKey="id" listValue="name" 
													headerKey="-1" headerValue="%{getText('default.dropdown.select')}" value="%{ward.id}" onClick="checkZone(this);"/>
							</div>
						</s:if>
						
					</div>
					</s:if>
					
					<s:if test="%{list.contains('scheme') || list.contains('subScheme') }" >
					<div class="form-group">
						<s:if test="%{list.contains('scheme') || list.contains('subScheme') }" >   
			                <label class="col-sm-2 control-label text-right">
							    <s:text name='subledgerCode.scheme'/> <span class="mandatory"></span>
							</label>
							<div class="col-sm-3 add-margin">
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="scheme" id="scheme" cssClass="form-control" list="dropdownData.schemeList" listKey="id" listValue="name" value="%{scheme.id}"  onClick="checkFund(this);"/>
				                <egov:ajaxdropdown id="subSchemeDropdown" fields="['Text','Value']" dropdownId='subScheme' url='estimate/ajaxFinancialDetail!loadSubSchemes.action' selectedValue="%{scheme.id}"/>
							</div>
			             </s:if>
			             
			             <s:if test="%{list.contains('subScheme')}" >   
			                <label class="col-sm-2 control-label text-right">
							    <s:text name='subledgerCode.subscheme'/><span class="mandatory"></span>
							</label>
							<div class="col-sm-3 add-margin">
								<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}" name="subScheme" id="subScheme" cssClass="form-control" list="dropdownData.subSchemeList" listKey="id" listValue="name" value="%{subScheme.id}" onClick="checkScheme(this);"/>
							</div>
			 			 </s:if> 
						
					</div>
				</s:if>
					
					
				</div>
	</div>
    
     
    <div class="row">
		<div class="col-xs-12 text-center buttonholdersearch">
			<s:submit cssClass="btn btn-primary" value="Save" id="saveButton" name="saveButton" method="save" onclick="return validateBeforeSubmit();"/>
			&nbsp;
			<input type="button" class="btn btn-default" value="Close" id="closeButton" name="closeButton" onclick="confirmClose('<s:text name='subledger.close.confirm'/>');"/>
		</div>
	</div>
    
    </s:push>
   </s:form>
  </body>
</html>
