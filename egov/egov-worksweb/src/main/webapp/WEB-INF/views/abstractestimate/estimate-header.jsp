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

<style>
#warning {
  display:none;
  color:blue;
}
</style>

<script>

var warnings=new Array();
warnings['natureOfWorkChanged']='<s:text name="estimate.header.warning.natureOfWorkChanged"/>'
warnings['dateChanged']='<s:text name="estimate.header.warning.dateChanged"/>'
warnings['improperWardSelection']='<s:text name="estimate.header.warning.improperWardSelection"/>'

function warn(type){ 
    dom.get(type+"Warning").innerHTML=warnings[type]
    dom.get(type+"Warning").style.display='';
    YAHOO.lang.later(3000,null,function(){dom.get(type+"Warning").style.display='none';});
}

function setupSubTypes(elem){
    categoryId=elem.options[elem.selectedIndex].value;
    populatecategory({category:categoryId});
}

var wardSearchSelectionHandler = function(sType, arguments) { 
            var oData = arguments[2];
            dom.get("wardSearch").value=oData[0];
            dom.get("wardID").value = oData[1];
        }
        
var wardSelectionEnforceHandler = function(sType, arguments) {
    warn('improperWardSelection');
}


function validateCharacters(obj,msg){
	var string=obj.value;
	string=string.replace(/(\r\n|\r|\n){3,}/g,"\n\n\n");
	string=string.replace(/(\t)/g," ");
	string=string.replace(/([ ]){3,}/g,"  ");
	obj.value=string;
	if(string.search(/http:/i)>-1){
		alert(msg);
		return false;
	}
	else{
		return true;
	}
}

function validateHeaderBeforeSubmit(abstractEstimateForm) {
	var nameObj=document.getElementById('name');
	var descriptionObj=document.getElementById('description');
	if(!validateCharacters(nameObj,'<s:text name="estimate.name.error" />') || !validateCharacters(descriptionObj,'<s:text name="estimate.description.error" />')){
		return false;
	}
    var wardName = abstractEstimateForm.wardSearch.value;
    var workType=document.getElementById('parentCategory').value;
      
      if (wardName.length == 0) {
        abstractEstimateForm.wardID.value = -1;
       }
      if(document.getElementById('wardSearch').value=='')
      {
    	  dom.get("worktypeerror").style.display='';
          dom.get("worktypeerror").innerHTML='<s:text name="estimate.ward.null" />';
          window.scroll(0,0);
          return false;   
      }
      else
      {
          if(dom.get("worktypeerror").innerHTML=='<s:text name="estimate.ward.null" />')
          {
        	  dom.get("worktypeerror").style.display='none';
              dom.get("worktypeerror").innerHTML='';    
          }    
      }
      if(dom.get('natureOfWork').value==-1)
      {
    	  dom.get("worktypeerror").style.display='';
          dom.get("worktypeerror").innerHTML='<s:text name="estimate.natureofwork.null" />';
          window.scroll(0,0);
          return false;   
      }
      else
      {
          if(dom.get("worktypeerror").innerHTML=='<s:text name="estimate.natureofwork.null" />')
          {
        	  dom.get("worktypeerror").style.display='none';
              dom.get("worktypeerror").innerHTML='';    
          }    
      }
      if(document.getElementById('name').value=='')
      {
    	  dom.get("worktypeerror").style.display='';
          dom.get("worktypeerror").innerHTML='<s:text name="estimate.name.null" />';
          window.scroll(0,0);
          return false;   
      }
      else
      {
          if(dom.get("worktypeerror").innerHTML=='<s:text name="estimate.name.null" />')
          {
        	  dom.get("worktypeerror").style.display='none';
              dom.get("worktypeerror").innerHTML='';    
          }    
      }
      if(document.getElementById('description').value=='')
      {
    	  dom.get("worktypeerror").style.display='';
          dom.get("worktypeerror").innerHTML='<s:text name="estimate.desc.null" />';
          window.scroll(0,0);
          return false;   
      }
      else
      {
          if(dom.get("worktypeerror").innerHTML=='<s:text name="estimate.desc.null" />')
          {
        	  dom.get("worktypeerror").style.display='none';
              dom.get("worktypeerror").innerHTML='';    
          }    
      }
      if(dom.get('fundSource').value==-1)
      {
    	  dom.get("worktypeerror").style.display='';
          dom.get("worktypeerror").innerHTML='<s:text name="estimate.fund.null" />';
          window.scroll(0,0);
          return false;   
      }
      else
      {
          if(dom.get("worktypeerror").innerHTML=='<s:text name="estimate.fund.null" />')
          {
        	  dom.get("worktypeerror").style.display='none';
              dom.get("worktypeerror").innerHTML='';    
          }    
      }
      
   	 if(workType=='-1'){
       
   	    dom.get("worktypeerror").style.display='';
        dom.get("worktypeerror").innerHTML='<s:text name="estimate.worktype.null" />';
        window.scroll(0,0);
        return false;
	}
   	else{
        dom.get("worktypeerror").style.display='none';
        dom.get("worktypeerror").innerHTML='';
        return true;
    }
}

function trim(str) {
        return str.replace(/^\s+|\s+$/g,"");
}

var previousDate;

function dateChange(){
	var estimateDate=document.forms[0].estimateDate.value;
	var id=document.forms[0].id.value;
	var hiddenEstimateDate=document.forms[0].hiddenEstimateDate.value;
	if(hiddenEstimateDate!= ''){
		previousDate=hiddenEstimateDate;
	}
	else{
		previousDate=estimateDate;
	}
	if(estimateDate!=''){
		javascript:warn('dateChanged');
    		resetOverheads();
	}
	
	if(trim(estimateDate)!='' && hiddenEstimateDate!= '' && hiddenEstimateDate!=estimateDate){
		popup('popUpDiv');		
	}
	
	else if(id!='' && id!=undefined && estimateDate!='' && hiddenEstimateDate!=estimateDate){
		popup('popUpDiv');
	}
	document.forms[0].hiddenEstimateDate.value=document.forms[0].estimateDate.value;
}

function resetDetails(){    
	resetSorTable();
	resetNonSorTable();
    var elem = document.getElementById('type');
    resetOverheads();
}

function resetPreviousDate(){
	document.forms[0].hiddenEstimateDate.value=previousDate;
	document.forms[0].estimateDate.value=previousDate;
}

function clearMsg(obj)
{
	if(obj.value==dom.get("executingDepartment").value && '<s:property value="%{errorCode}" />'=='estimate.depositworks.dept.check')
	{
		dom.get('errorstyle').style.display='none';
	}	
}
function openMap()
{
	var params = [
	              'height='+screen.height,
	              'width='+screen.width,
	              'fullscreen=yes' 
	          ].join(',');
	var popup ;
	var  lat = document.getElementById("latitude").value ;
	var lon = document.getElementById("longitude").value ;
	var status = '<s:property value="%{egwStatus.code}" />';
	if(status==null || status=='' || status =='NEW' || status=='REJECTED')
	{
		if(lat!='' && lon!='')
		{
			popup = window.open('${pageContext.request.contextPath}/estimate/abstractEstimate-maps.action?mapMode=edit&latitude='+lat+'&longitude='+lon,'popup_window', params);
		}
		else
		{
			popup = window.open('${pageContext.request.contextPath}/estimate/abstractEstimate-maps.action?mapMode=edit','popup_window', params);	
		}
	}	
	else
	{
		if(lat!='' && lon!='')
		{
			popup = window.open('${pageContext.request.contextPath}/estimate/abstractEstimate-maps.action?mapMode=view&latitude='+lat+'&longitude='+lon,'popup_window', params);
		}
		else
			return;
	}	
	popup.moveTo(0,0);
}
function updateLocation(values)
{
	if(values!=null && values.length>0)
	{
		document.getElementById("latitude").value=values[0];
		document.getElementById("longitude").value=values[1];
		document.getElementById("location").value=values[2];
		document.getElementById("latlonDiv").style.display="";
	}
	if(values==null)
	{
		document.getElementById("latitude").value="";
		document.getElementById("longitude").value="";
		document.getElementById("location").value="";
		document.getElementById("latlonDiv").style.display="none";	
	}			
}

function jurisdictionSearchParameters(){
	return "isBoundaryHistory=false";
}
</script>


<!-- <div id="blanket" style="display:none;"></div>
<div id="popUpDiv" style="display:none;" ><s:text="estimate.dateChange.warning"/>(<a href="#" onclick="popup('popUpDiv');resetDetails();">Yes</a>/<a href="#" onclick="popup('popUpDiv');resetPreviousDate();">No</a>)?</div>

<div id="popLoadingDiv" style="display:none;font-size: 12px;font-weight: bold;color: #cc0000;position:absolute;
	width:350px;height:150px;z-index: 9002;text-align: center;" >
	
	<span>
		<p>Loading Enclosure.....</p>
		<img src="/egworks/resources/erp2/images/loading.gif" alt="Help" width="50" height="50" border="0" />
	</span>
</div> -->


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title" style="text-align: left;">
			<spring:message code="lbl.header" />
		</div>
	</div>
	<div class="panel-body custom-form">
	   <div class="form-group">
			<label class="col-sm-2 control-label text-right">
			   <spring:message code="lbl.estimatenumber" />
			</label>
			<div class="col-sm-3 add-margin view-content">
			<form:input path="estimateNumber" class="form-control" name="estimateNumber" readonly="true" value="${lineEstimateDetails.estimateNumber}"/>
			</div>
			
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.workidentificationnumber" />
			</label>
			<div class="col-sm-3 add-margin view-content">
			<form:input path="projectCode.code" class="form-control" name="projectCode.code" readonly="true" value="${lineEstimateDetails.projectCode.code}"/>
			</div>
		</div>
	  <div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.userdepartment" />
			</label>
			<div class="col-sm-3 add-margin">
				<form:input path="executingDepartment" class="form-control" name="executingDepartment" readonly="true" value="${lineEstimateDetails.lineEstimate.executingDepartment.name}"/>
			</div>
			
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.abstractestimatedate" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin">
			<fmt:formatDate value="${currentDate}" var="currDate" pattern="dd-MM-yyyy"/>
				<form:input path="estimateDate" name="estimateDate" class="form-control" value="${currDate}" readonly="true"/>
				<form:errors path="estimateDate" cssClass="add-margin error-msg" />
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.estimate.ward" />
			</label>
			<div class="col-sm-3 add-margin view-content">
			<form:input path="ward" class="form-control" name="ward" readonly="true" value="${lineEstimateDetails.lineEstimate.ward.name}"/>
			</div>
			
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.locality" />
			</label>
			<div class="col-sm-3 add-margin view-content">
			<form:input path="location" class="form-control" name="location" readonly="true" value="${lineEstimateDetails.lineEstimate.location.name}"/>
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.natureofwork" />
			</label>
			<div class="col-sm-3 add-margin view-content">
			<form:input path="natureOfWork" class="form-control" name="natureOfWork" readonly="true" value="${lineEstimateDetails.lineEstimate.natureOfWork.name}"/>
			</div>
			
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.workcategory" />
			</label>
			<div class="col-sm-3 add-margin view-content">
				<form:input path="category" class="form-control" name="category" readonly="true" value="${lineEstimateDetails.lineEstimate.workCategory}"/>
			</div>
		</div> 
		
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.typeofslum" />
			</label>
			<div class="col-sm-3 add-margin view-content">
			<form:input path="" class="form-control" name="" readonly="true" value="${lineEstimateDetails.lineEstimate.typeOfSlum}"/>
			</div>
			
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.beneficiary" />
			</label>
			<div class="col-sm-3 add-margin view-content">
			<form:input path="" class="form-control" name="" readonly="true" value="${lineEstimateDetails.lineEstimate.beneficiary}"/>
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.locality" />
			</label>
			<div class="col-sm-3 add-margin view-content">
			<form:input path="location" class="form-control" name="location" readonly="true" value="${lineEstimateDetails.lineEstimate.location.name}"/>
			</div>
			
			<a href="javascript:openMap();" id="mapAnchor" title="Click here to add/view gis marker on map" class="btn btn-primary"><i class="fa fa-location-arrow icon-inputgroup"></i></a>
		</div> 
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.nameofwork" />
			</label>
			<div class="col-sm-3 add-margin view-content">
			<form:input path="name" class="form-control" name="name" readonly="true" value="${lineEstimateDetails.lineEstimate.typeOfSlum}"/>
			</div>
			
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.workdescription" /><span class="mandatory"></span>
			</label>
			<div class="col-sm-3 add-margin view-content">
				<form:textarea path="lineEstimateDetails" name="lineEstimateDetails" class="form-control"  maxlength="1024" required="required"/>
				<form:errors path="lineEstimateDetails" cssClass="add-margin error-msg" />
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.typeofwork" />
			</label>
			<div class="col-sm-3 add-margin view-content">
			<form:input path="parentCategory" class="form-control" name="parentCategory" readonly="true" value="${lineEstimateDetails.lineEstimate.typeOfWork.code}"/>
			</div>
			
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.subtypeofwork" />
			</label>
			<div class="col-sm-3 add-margin view-content">
			<form:input path="category" class="form-control" name="category" readonly="true" value="${lineEstimateDetails.lineEstimate.subTypeOfWork.code}"/>
			</div>
		</div> 
		
			<div class="form-group">
			<label class="col-sm-2 control-label text-right">
			    <spring:message code="lbl.estimate.value" />
			</label>
			<div class="col-sm-3 add-margin view-content">
			<form:input path="estimateValue" class="form-control" name="estimateValue"/>
			</div>
		</div>
		
</div>   
</div>
    <script>
    	if(document.forms[0].estimateDate.value!=''){
    		document.forms[0].hiddenEstimateDate.value=document.forms[0].estimateDate.value;
    	}
    	else{
    		document.forms[0].hiddenEstimateDate.value=getCurrentDate();
    	}
    </script>