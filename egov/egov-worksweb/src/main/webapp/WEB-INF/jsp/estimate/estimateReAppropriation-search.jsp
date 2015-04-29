<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<html>
	<head>
		<title><s:text name='page.title.estimate.reappropriation' />
		</title>
	</head>
	<script src="<egov:url path='js/works.js'/>"></script>
	<script src="<egov:url path='js/helper.js'/>"></script>

	<script type="text/javascript">

		function enableFields(){
			for(i=0;i<document.estimateReApprForm.elements.length;i++){
	        	document.estimateReApprForm.elements[i].disabled=false;
			}
		} 	

		function setupPreparedByList(elem){
		    var deptId=elem.options[elem.selectedIndex].value;
		    populatepreparedBy({executingDepartment:deptId});
		}

		function populateIsChecked(obj){
		   if(obj.checked){
			 	obj.value=true;
			 	obj.checked=true;
			}
			else if(!obj.checked){
				dom.get("slectAllEstimates").checked=false;
			 	dom.get("slectAllEstimates").value=false;
			 	obj.value=false;
			 	obj.checked=false;
			}
		}

	function checkDepartment(){
		var deptId = document.getElementById("executingDepartment").value;
		
		var myDepartmentSuccessHandler = function(req,res) {
				if(res.results[0].value=='true'){
					dom.get("newExecutingDepartment").selectedIndex=0;
					dom.get("newExecDeptLabelDiv").style.visibility='hidden';
					dom.get("newExecutingDepartment").style.visibility='hidden';
					dom.get("isJurisdictionChange").checked=false;
					dom.get("isJurisdictionChange").value=false;
					dom.get("isJurisdictionChange").style.visibility='hidden';
					dom.get("newJurisdictionLabelDiv").style.visibility='hidden';
					dom.get("workflow_info_and_actions").style.display='none';
					dom.get("save_actions").style.display='block';							
				}
				else{
					dom.get("newExecDeptLabelDiv").style.visibility='visible';
					dom.get("newExecutingDepartment").style.visibility='visible';
					dom.get("isJurisdictionChange").style.visibility='visible';					
					dom.get("newJurisdictionLabelDiv").style.visibility='visible';									
					dom.get("workflow_info_and_actions").style.display='block';
					dom.get("approverCommentsRow").style.display='block';
					dom.get("save_actions").style.display='none';							
				}
            };
            
		var myDepartmentFailureHandler = function() {
	            dom.get("estimate_reappr_error").style.display='';
	            document.getElementById("estimate_reappr_error").innerHTML='<s:text name="estimate.reappr.dept.check.fail"/>';
	        };
	 	makeJSONCall(["value"],'${pageContext.request.contextPath}/estimate/ajaxEstimate!isSkipDepartmentChange.action',{departmentId:deptId},myDepartmentSuccessHandler,myDepartmentFailureHandler) ;
	}

	function resetReAppropriationDetails(){
		if(dom.get("newWardSearch")!=null){
			dom.get("newWardSearch").value='';
			dom.get("newWardID").value = -1;
		}

		if(dom.get("newExecutingDepartment")!=null){
			dom.get("newExecutingDepartment").value=-1;
		}

		if(dom.get("departmentid")!=null){
			dom.get("departmentid").value=-1;
		}

	}

		function validateSearch(){
			if(dom.get("wardSearch").value==''){
				dom.get("wardID").value = -1;
			}
			resetReAppropriationDetails();
			clearMessage('estimatereappr_error');
			document.getElementById("estimatereappr_error").style.display='none';
			if(dom.get("executingDepartment").value=='' || dom.get("executingDepartment").value==-1){
				document.getElementById("estimatereappr_error").style.display='';
        		document.getElementById("estimatereappr_error").innerHTML='<s:text name="estimate.reappropriation.old.exec.dept.null" />';
				return false;
			}

			
			return true;
		}

		function selectAllEstimates(obj) {
			var length=eval('<s:property value="abstractEstimateList.size()" />')+1;
			var i;
		   if(obj.checked){
			 	obj.value=true;
			 	obj.checked=true;
				for(i=1;i<length;i++) {
					var id='isEstimateSelected'+i.toString();
					dom.get(id).checked=true;
					dom.get(id).value=true;
				}
			}
			else if(!obj.checked){
			 	obj.value=false;
			 	obj.checked=false;
				for(i=1;i<length;i++) {
					var id='isEstimateSelected'+i.toString();
					dom.get(id).checked=false;
					dom.get(id).value=false;
				}
			}
		}

		function enableSelectEstimates() {
			var length=eval('<s:property value="abstractEstimateList.size()" />')+1;
			var i;
			for(i=1;i<length;i++) {
				var id='isEstimateSelected'+i.toString();
				dom.get(id).disabled=false;
			}
		}

		function disableSelectEstimates() {
			var length=eval('<s:property value="abstractEstimateList.size()" />')+1;
			var i;
			for(i=1;i<length;i++) {
				var id='isEstimateSelected'+i.toString();
				dom.get(id).disabled=true;
			}
		}
	
	 	function validateSubmit(){
			clearMessage('estimatereappr_error');
			document.getElementById("estimatereappr_error").style.display='none';
			if(dom.get("newWardSearch").value==''){
				dom.get("newWardID").value = -1;
			}
			if(dom.get("newExecutingDepartment")!=null && dom.get("newExecutingDepartment").value==-1 && dom.get("newWardID").value==-1){
				document.getElementById("estimatereappr_error").style.display='';
        		document.getElementById("estimatereappr_error").innerHTML='<s:text name="estimate.reappropriation.new.change.null" />';
        		return false;
			}
			var isEstimateSelected=false;
			
			var length=eval('<s:property value="abstractEstimateList.size()" />')+1;
			var i;
			for(i=1;i<length;i++) {
				var id='isEstimateSelected'+i.toString();
				if(dom.get(id).checked){
					isEstimateSelected=true;
					break;
				}
			}
			if(!isEstimateSelected){
				document.getElementById("estimatereappr_error").style.display='';
        		document.getElementById("estimatereappr_error").innerHTML='<s:text name="estimate.reappropriation.estimates.null" />';
        		return false;
			} 
  			return true;
	  	}

		function disableExecDept(){
			if(document.forms[0].isJurisdictionChange.checked){
				dom.get("newExecutingDepartment").selectedIndex=0;
				dom.get("newExecDeptLabelDiv").style.visibility='hidden';
				dom.get("newExecutingDepartment").style.visibility='hidden';
				dom.get("newUserDepartment").selectedIndex=0;
				dom.get("newUserDeptLabelDiv").style.visibility='hidden';
				dom.get("newUserDepartment").style.visibility='hidden';
				dom.get("workflow_info_and_actions").style.display='none';
				dom.get("approverCommentsRow").style.display='none';
				dom.get("save_actions").style.display='block';							
			}
			else{
				dom.get("newExecDeptLabelDiv").style.visibility='visible';
				dom.get("newExecutingDepartment").style.visibility='visible';
				dom.get("newUserDeptLabelDiv").style.visibility='visible';
				dom.get("newUserDepartment").style.visibility='visible';
				dom.get("workflow_info_and_actions").style.display='block';
				dom.get("approverCommentsRow").style.display='block';
				dom.get("save_actions").style.display='none';							
			}
		}

		function validateDate(){
			clearMessage('estimatereappr_error');
			document.getElementById("estimatereappr_error").style.display='none';
			var error=false;
			if(dom.get("toDate")!=null) {
				validateDateFormat(dom.get("toDate"));
			}

			if(dom.get("fromDate")!=null){
				validateDateFormat(dom.get("fromDate"));
			}
			var links=document.estimateReApprForm.getElementsByTagName("span");
			for(i=0;i<links.length;i++) {
        		if(links[i].innerHTML=='x' && links[i].style.display!='none'){
        			error=true;
					document.getElementById("estimatereappr_error").style.display='';
        			document.getElementById("estimatereappr_error").innerHTML='<s:text name="contractor.validate_x.message" />';
     		      	break;
        		}
    		}
			if(error){
				return false;
			}

	  		var currentDate=getCurrentDate();
	  		if(dom.get("toDate").value!=''){
	  			var toDate=dom.get("toDate").value;
				if(!compareDate(toDate,currentDate)){
					document.getElementById("estimatereappr_error").style.display='';
	        		document.getElementById("estimatereappr_error").innerHTML='<s:text name="estimate.reappropriation.toDate.date.error1"/>';
					return false;
				}

	  		}

	  		if(dom.get("fromDate").value!=''){
	  			var fromDate=dom.get("fromDate").value;
				if(!compareDate(fromDate,currentDate)){
					document.getElementById("estimatereappr_error").style.display='';
	        		document.getElementById("estimatereappr_error").innerHTML='<s:text name="estimate.reappropriation.fromDate.date.error1"/>';
					return false;
				}

	  		}

	  		if(dom.get("toDate").value!='' && dom.get("fromDate").value!=''){
	  			var toDate=dom.get("toDate").value;
	  			var fromDate=dom.get("fromDate").value;
				if(!compareDate(fromDate,toDate)){
					document.getElementById("estimatereappr_error").style.display='';
	        		document.getElementById("estimatereappr_error").innerHTML='<s:text name="estimate.reappropriation.fromDate.date.error2"/>';
					return false;
				}
	  		}
	  		
	  		return true;
		}

	var wardSearchSelectionHandler = function(sType, arguments) { 
            var oData = arguments[2];
            dom.get("wardSearch").value=oData[0];
            dom.get("wardID").value = oData[1];
        }
        
	var wardSelectionEnforceHandler = function(sType, arguments) {
    	warn('improperWardSelection');
	}
	
	var newWardSearchSelectionHandler = function(sType, arguments) { 
        var oData = arguments[2];
        dom.get("newWardSearch").value=oData[0];
        dom.get("newWardID").value = oData[1];
    }
        
	var newWardSelectionEnforceHandler = function(sType, arguments) {
    	warn('improperWardSelection');
	}
		
	function compareDate(obj1,obj2){
		if(obj1=='' || obj2==''){
			return false;
		}
		var dt1  = parseInt(obj1.substring(0,2),10);
		var mon1 = parseInt(obj1.substring(3,5),10);
		var yr1  = parseInt(obj1.substring(6,10),10);
		var date1 = new Date(eval(yr1), eval(mon1)-1,eval(dt1));
		var dt2  = parseInt(obj2.substring(0,2),10);
		var mon2 = parseInt(obj2.substring(3,5),10);
		var yr2  = parseInt(obj2.substring(6,10),10);
		var date2 = new Date(eval(yr2),eval(mon2)-1,eval(dt2)); 
		if(date2 < date1){
         	return false;
		}else{
			return true;
		} 
	}

function trim(str) {
        return str.replace(/^\s+|\s+$/g,"");
}

function validate(obj,text){
	if(text=='submit_for_approval') {
	    if(dom.get("newExecutingDepartment")!=null && dom.get("newExecutingDepartment").value==-1) {
				clearMessage('estimatereappr_error');
				document.getElementById("estimatereappr_error").style.display='none';
				if(dom.get("newWardSearch").value==''){
					dom.get("newWardID").value = -1;
				}
				document.getElementById("estimatereappr_error").style.display='';
	       		document.getElementById("estimatereappr_error").innerHTML='<s:text name="estimate.reappropriation.new.exec.dept.null" />';
	       		return false;
	    }
	}
	if(obj!="cancel"){
		if(!validateUser(text))
			return false;
	}
	if(!validateSubmit())
	  return false;
	if(obj=="reject"){
		var remarks=document.getElementById("approverComments").value;
		if(trim(remarks)==""){
			dom.get("estimatereappr_error").style.display='';
    		document.getElementById("estimatereappr_error").innerHTML='<s:text name="estimate.reappropriation.remarks.null" />';
    		return false;
		}
	}
	enableFields();
	return true;
}
function execDeptChange(obj)
{
	var msg='<s:text name="estimate.reappropriation.exec.dept.confirm"/>';
	if(!confirmCancel(msg,'')) {
		obj.value=-1;
	}
}
function userDeptChange(obj)
{
	var msg='<s:text name="estimate.reappropriation.user.dept.confirm"/>';
	if(!confirmCancel(msg,'')) {
		obj.value=-1;
	}
}

function jurisdictionSearchParametersForSearch(){
	return "isBoundaryHistory=true";
}
function jurisdictionSearchParametersForUpdate(){
	return "isBoundaryHistory=false";
}
  </script>
	<body>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>

		<s:form theme="simple" name="estimateReApprForm"
			id="estimateReApprForm">
			<s:token />
			<s:push value="model">
				<input type="hidden" name="isEnableSelect" id="isEnableSelect" />
				<s:hidden name="sourcepage" value="%{sourcepage}" id="sourcepage" />
				<s:hidden name="id" value="%{id}" id="id" />
				<div id="estimatereappr_error" class="errorstyle"
					style="display: none;"></div>

				<div class="formmainbox">
					<div class="insidecontent">
						<div class="rbroundbox2">
							<div class="rbtop2">
								<div></div>
							</div>
							<s:if test="%{sourcepage!='inbox'}">
								<div class="rbcontent2">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td colspan="4">
												<table width="100%" border="0" cellspacing="0"
													cellpadding="0">
													<tr>
														<td class="headingwk" align="left">
															<div class="arrowiconwk">
																<img
																	src="${pageContext.request.contextPath}/image/arrow.gif" />
															</div>
															<div class="headerplacer">
																<s:text name='title.search.criteria' />
															</div>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td class="whiteboxwk">
												<s:text name="estimate.reappropriation.fromdate" />
											</td>

											<td class="whitebox2wk">
												<s:date name="fromDate" var="fromDateFormat"
													format="dd/MM/yyyy" />
												<s:textfield name="fromDate" value="%{fromDateFormat}"
													id="fromDate" cssClass="selectwk"
													onBlur="validateDate();"
													onChange="validateDate();"
													onfocus="javascript:vDateType='3';"
													onkeyup="DateFormat(this,this.value,event,false,'3')" />
												<a
													href="javascript:show_calendar('forms[0].fromDate',null,null,'DD/MM/YYYY');"
													onmouseover="window.status='Date Picker';return true;"
													onmouseout="window.status='';return true;"> <img
														src="${pageContext.request.contextPath}/image/calendar.png"
														alt="Calendar" width="16" height="16" border="0"
														align="absmiddle" /> </a>
												<span id="errorfromDate"
													style="display: none; color: red; font-weight: bold">x</span>
											</td>
											<td class="whiteboxwk">
												<s:text name="estimate.reappropriation.todate" />
											</td>
											<td class="whitebox2wk">
												<s:date name="toDate" var="toDateFormat" format="dd/MM/yyyy" />
												<s:textfield name="toDate" value="%{toDateFormat}"
													id="toDate" cssClass="selectwk"
													onBlur="validateDate();"
													onChange="validateDate();"
													onfocus="javascript:vDateType='3';"
													onkeyup="DateFormat(this,this.value,event,false,'3')" />
												<a
													href="javascript:show_calendar('forms[0].toDate',null,null,'DD/MM/YYYY');"
													onmouseover="window.status='Date Picker';return true;"
													onmouseout="window.status='';return true;"> <img
														src="${pageContext.request.contextPath}/image/calendar.png"
														alt="Calendar" width="16" height="16" border="0"
														align="absmiddle" /> </a>
												<span id="errortoDate"
													style="display: none; color: red; font-weight: bold">x</span>
											</td>
										</tr>
										<tr>
											<td class="greyboxwk">
												<s:text name="estimate.reappropriation.createdby" />
											</td>
											<td class="greybox2wk">
												<s:select headerKey="-1"
													headerValue="%{getText('estimate.default.select')}"
													name="createdById" value="%{createdById}" id="createdById"
													cssClass="selectwk" list="dropdownData.createdByList"
													listKey="id" listValue="userName" />
											</td>
											<td class="greyboxwk">
												<s:text name="estimate.reappropriation.preparedby" />
											</td>
											<td class="greybox2wk">
												<s:select headerKey="-1"
													headerValue="%{getText('estimate.default.select')}"
													name="preparedBy" value="%{preparedBy}" id="preparedBy"
													cssClass="selectwk" list="dropdownData.preparedByList"
													listKey="id" listValue="employeeName" />
											</td>
										</tr>
										<tr>
											<td class="whiteboxwk"><span class="mandatory">*</span>
												<s:text name="estimate.reappropriation.exec.dept" />
											</td>
											<td class="whitebox2wk">
												<s:select headerKey="-1"
													headerValue="%{getText('estimate.default.select')}"
													name="execDept" id="executingDepartment"
													cssClass="selectwk"
													list="dropdownData.searchExecutingDepartmentList" listKey="id"
													listValue="deptName" value="%{execDept}"
													onChange="setupPreparedByList(this);" />
												<egov:ajaxdropdown id="preparedBy"
													fields="['Text','Value','Designation']"
													dropdownId='preparedBy' optionAttributes='Designation'
													url='estimate/ajaxEstimate!usersInExecutingDepartment.action' />
											</td>
											<td class="whiteboxwk">
												<s:text name="estimate.reappropriation.jurisdiction" />
											</td>
											<td class="whitebox2wk">
												<div class="yui-skin-sam">
													<div id="wardSearch_autocomplete">
														<div>
															<s:textfield id="wardSearch" type="text" name="wardName"
																value="%{boundary.parent?(boundary.parent?boundary.name+'('+boundary.parent.name+')':''):(boundary.name?boundary.name:'')}"
																class="selectwk" />
															<s:hidden id="wardID" name="boundaryId"
																value="%{boundaryId}" />
														</div>
														<span id="wardSearchResults"></span>
													</div>
												</div>
												<egov:autocomplete name="wardSearch" width="20"
													field="wardSearch" url="wardSearch!searchAjax.action?"
													queryQuestionMark="false" results="wardSearchResults"
													handler="wardSearchSelectionHandler"
													forceSelectionHandler="wardSelectionEnforceHandler" paramsFunction="jurisdictionSearchParametersForSearch"/>
												<span class='warning' id="improperWardSelectionWarning"></span>
											</td>
										</tr>
										<tr>
											<td class="greyboxwk">
												<s:text name="estimate.reappropriation.estimate.number" />
											</td>
											<td class="greybox2wk">
												<s:textfield name="estimateNumber" value="%{estimateNumber}"
													id="estimateNumber" cssClass="selectwk" />
											</td>
											<td class="greyboxwk">
												<s:text name="estimate.reappropriation.status" />
											</td>
											<td width="21%" class="greybox2wk">
												<s:select id="estimateStatus" name="estimateStatus"
													headerKey="-1" headerValue="ALL" cssClass="selectwk"
													list="%{estimateStatuses}" listKey="code" disabled="true"
													listValue="description" />
											</td>
										</tr>
									</table>
								</div>

								<div id="mandatary" align="right" class="mandatory"
									style="font-size: 11px; padding-right: 20px;">
									*
									<s:text name="message.mandatory" />
								</div>

								<div class="buttonholderwk" id="slCodeButtons">
									<s:submit cssClass="buttonadd" value="SEARCH"
										id="searchButton" name="searchButton" method="searchDetails"
										onclick="return validateSearch();" />
									&nbsp;
									<input type="button" class="buttonfinal" value="CLEAR"
										id="clearButton" name="clearButton"
										onclick="window.open('${pageContext.request.contextPath}/estimate/estimateReAppropriation!search.action?sourcepage=create','_self');" />
									&nbsp;
									<input type="button" class="buttonfinal" value="CLOSE"
										id="closeButton" name="closeButton"
										onclick="window.close();"  />
								</div>
							</s:if>
							<br />
							<div class="rbcontent2">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td></td>
									</tr>
									<s:if test="%{abstractEstimateList.size() != 0}">
										<tr>
											<td>
												<table width="100%" border="0" cellspacing="0"
													cellpadding="0">
													<tr>
														<td class="headingwk" align="left">
															<div class="arrowiconwk">
																<img
																	src="${pageContext.request.contextPath}/image/arrow.gif" />
															</div>
															<div class="headerplacer">
																<s:text name='search.result' />
															</div>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td>
												<display:table name="abstractEstimateList" uid="currentRow"
													cellpadding="0" cellspacing="0" requestURI=""
													style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">

													<display:column headerClass="pagetableth"
														class="pagetabletd" 
														title='<input type="checkbox" id="slectAllEstimates" onclick="selectAllEstimates(this)" />'
														style="width:3%;text-align:center">
														<input type='hidden'
															name='estimateReApprDetails[<s:property value='#attr.currentRow_rowNum' />].estimate.id'
															value='<s:property value="#attr.currentRow.id" />' />
														<s:if test="%{model!=null && model.id!=null && model.status.code!='NEW' && model.status.code!='REJECTED'}" >
															<input type='checkbox' id='isEstimateSelected<s:property value='#attr.currentRow_rowNum' />'  
																name='estimateReApprDetails[<s:property value='#attr.currentRow_rowNum' />].isChecked' 
																onclick='populateIsChecked(this);' value='true' checked='true' readonly='true' disabled='true'/>
														</s:if>
														<s:elseif test="%{model!=null && model.id!=null && (model.status.code=='NEW' || model.status.code=='REJECTED')}" >
															<input type='checkbox' id='isEstimateSelected<s:property value='#attr.currentRow_rowNum' />'    
																name='estimateReApprDetails[<s:property value='#attr.currentRow_rowNum' />].isChecked' 
																onclick='populateIsChecked(this);' value='true' checked='true'/>
														</s:elseif>
														<s:else>
															<input type='checkbox' id='isEstimateSelected<s:property value='#attr.currentRow_rowNum' />'   
																name='estimateReApprDetails[<s:property value='#attr.currentRow_rowNum' />].isChecked' 
																onclick='populateIsChecked(this);' value='false' />
														</s:else>
													</display:column>

													<display:column title="Sl.No"
														titleKey='estimate.search.slno' headerClass="pagetableth"
														class="pagetabletd" style="width:4%;text-align:right">
														<s:property
															value="#attr.currentRow_rowNum" />
													</display:column>

													<display:column headerClass="pagetableth"
														class="pagetabletd" title="Estimate Number"
														style="width:10%;text-align:left"
														property="estimateNumber" />
													<display:column headerClass="pagetableth"
														class="pagetabletd" title="Executing Department"
														titleKey='estimate.search.executingdept'
														style="width:8%;text-align:left"
														property='executingDepartment.deptName' />
													<display:column headerClass="pagetableth"
														class="pagetabletd" title="User Department"
														titleKey='plannedEstimateReport.userDept'
														style="width:8%;text-align:left"
														property='userDepartment.deptName' />	
													<display:column headerClass="pagetableth"
														class="pagetabletd" title="Name"
														titleKey='estimate.search.name'
														style="width:20%;text-align:left" property='name' />

													<display:column headerClass="pagetableth"
														class="pagetabletd"
														title="Status"
														titleKey='estimate.search.status'
														style="width:10%;text-align:left">
														<s:if
															test="%{#attr.currentRow.state.previous.value=='ADMIN_SANCTIONED' || #attr.currentRow.state.previous.value=='CANCELLED'}">
															<s:property value="#attr.currentRow.state.previous.value" />
														</s:if>
														<s:else>
															<s:property value="#attr.currentRow.state.value" />
														</s:else>
													</display:column>
													<display:column headerClass="pagetableth"
														class="pagetabletd" title="Type"
														titleKey='estimate.search.type'
														style="width:10%;text-align:left" property='type.name' />
													<display:column headerClass="pagetableth"
														class="pagetabletd" title="Estimate Date"
														titleKey='estimate.search.estimateDate'
														style="width:10%;text-align:center">
														<s:date name="#attr.currentRow.estimateDate"
															format="dd/MM/yyyy" />
													</display:column>
													<display:column headerClass="pagetableth"
														class="pagetabletd" title="Total"
														titleKey='estimate.search.total'
														style="width:10%;text-align:right"
														property='totalAmount.formattedString' />
												</display:table>
											</td>
										</tr>
									</s:if>
								</table>
								<br />
								<s:if test="%{abstractEstimateList.size() != 0}">
									<table>
										<tr>
											<td class="whiteboxwk">
												<s:checkbox id="isJurisdictionChange"
													name="isJurisdictionChange" onclick="disableExecDept();" />
											</td>
											<td colspan="3" class="whitebox2wk">
												<div id="newJurisdictionLabelDiv">
													<s:text name="estimate.reappropriation.jurisdiction.change" />
												</div>
											</td>
										</tr>
										<tr>
											<td class="whiteboxwk">
												<div id="newExecDeptLabelDiv">
													<s:text name="estimate.reappropriation.exec.dept" />
												</div>
											</td>
											<td class="whitebox2wk">
												<s:select headerKey="-1"
													headerValue="%{getText('estimate.default.select')}"
													name="department" id="newExecutingDepartment"
													cssClass="selectwk" onchange="execDeptChange(this)"
													list="dropdownData.newExecutingDepartmentlist" listKey="id"
													listValue="deptName" value="%{department.id}" />
											</td>
											<td class="whiteboxwk">
												<s:text name="estimate.reappropriation.jurisdiction" />
											</td>
											<td class="whitebox2wk">
												<div class="yui-skin-sam">
													<div id="newWardSearch_autocomplete">
														<div>
															<s:textfield id="newWardSearch" type="text"
																name="wardName"
																value="%{ward.parent?(ward.parent?ward.name+'('+ward.parent.name+')':''):(ward.name?ward.name:'')}"
																class="selectwk" />
															<s:hidden id="newWardID" name="ward" value="%{ward.id}" />
														</div>
														<span id="newWardSearchResults"></span>
													</div>
												</div>
												<egov:autocomplete name="newWardSearch" width="20"
													field="newWardSearch" url="wardSearch!searchAjax.action?"
													queryQuestionMark="false" results="newWardSearchResults"
													handler="newWardSearchSelectionHandler"
													forceSelectionHandler="newWardSelectionEnforceHandler" paramsFunction="jurisdictionSearchParametersForUpdate"/>
												<span class='warning' id="improperWardSelectionWarning" ></span>
											</td>
										</tr>
										<tr>
											<td class="whiteboxwk">
												<div id="newUserDeptLabelDiv">
													<s:text name="estimate.reappropriation.user.dept" />
												</div>
											</td>
											<td class="whitebox2wk">
												<s:select headerKey="-1"
													headerValue="%{getText('estimate.default.select')}"
													name="userDepartment" id="newUserDepartment"
													cssClass="selectwk" onchange="userDeptChange(this)"
													list="dropdownData.newExecutingDepartmentlist" listKey="id"
													listValue="deptName" value="%{userDepartment.id}" />
											</td>
										</tr>
									</table>
									<s:hidden name="scriptName" id="scriptName"
										value="EstimateReAppropriation"></s:hidden>
									<s:hidden name="actionName" id="actionName" />
									<div id="workflow_info_and_actions" style="display:none;">
										<div id="manual_workflow">
											<%@ include file="../workflow/workflow.jsp"%>
										</div>
										<div class="buttonholderwk">
											<s:if
												test="%{(sourcepage=='inbox' || model.status==null || hasErrors())}">
												<s:iterator value="%{validActions}">
													<s:if test="%{description!=''}">
														<s:if test="%{description=='CANCEL'}">
															<s:submit type="submit" cssClass="buttonfinal"
																value="%{description}" id="%{name}" name="%{name}"
																method="cancelWorkflow"
																onclick="document.estimateReApprForm.actionName.value='%{name}';return validate('cancel','%{name}');" />
														</s:if>
														<s:elseif test="%{description=='REJECT'}">
															<s:submit type="submit" cssClass="buttonfinal"
																value="%{description}" id="%{name}" name="%{name}"
																method="rejectWorkflow"
																onclick="document.estimateReApprForm.actionName.value='%{name}';return validate('reject','%{name}');" />
														</s:elseif>
														<s:else>
															<s:submit type="submit" cssClass="buttonfinal"
																value="%{description}" id="%{name}" name="%{name}"
																method="save"
																onclick="document.estimateReApprForm.actionName.value='%{name}';return validate('noncancel','%{name}');" />
														</s:else>
													</s:if>
												</s:iterator>
											</s:if>
											<input type="button" class="buttonfinal" value="CLOSE"
												id="wfcloseButton" 
												onclick="window.close();" />
										</div>
									</div>
									<div id="save_actions" class="buttonholderwk"
										style="display: none;">
										<br />
										<s:submit type="submit" cssClass="buttonfinal" value="SAVE"
											id="save" name="save" method="save"
											onclick="document.estimateReApprForm.actionName.value='save';return validate('noncancel','save');" />
										<input type="button" class="buttonfinal" value="CLOSE"
												id="nowfcloseButton" 
												onclick="window.close();" />
									</div>
								</s:if>
								<br />
							</div>
						</div>
					</div>
				</div>
			</s:push>
		</s:form>
		<script type="text/javascript">
<s:if test="%{abstractEstimateList.size() != 0 }">
	if(document.getElementById("executingDepartment")!=null) {
		checkDepartment();
	}
</s:if>
<s:if test="%{sourcepage == 'inbox' && abstractEstimateList.size() != 0 && model.id!=null && model.status.code!='CHECKED'}">
	dom.get("slectAllEstimates").checked=true;
	dom.get("workflow_info_and_actions").style.display='block';
	dom.get("approverCommentsRow").style.display='block';
	dom.get("isJurisdictionChange").checked=false;
	dom.get("isJurisdictionChange").value=false;
	dom.get("isJurisdictionChange").style.visibility='hidden';
	dom.get("newJurisdictionLabelDiv").style.visibility='hidden';
	dom.get("newWardSearch").disabled=true;
	dom.get("newExecutingDepartment").disabled=true;
	dom.get("slectAllEstimates").disabled=true;
	disableSelectEstimates();
</s:if>
<s:if test="%{sourcepage == 'inbox' && abstractEstimateList.size() != 0 && model.status.code=='CHECKED'}">
	dom.get("slectAllEstimates").checked=true;
	dom.get("workflow_info_and_actions").style.display='block';
	dom.get("approverCommentsRow").style.display='block';
	dom.get("workflowDetials").style.display='none';
	dom.get("isJurisdictionChange").checked=false;
	dom.get("isJurisdictionChange").value=false;
	dom.get("isJurisdictionChange").style.visibility='hidden';
	dom.get("newJurisdictionLabelDiv").style.visibility='hidden';
	dom.get("newWardSearch").disabled=true;
	dom.get("newExecutingDepartment").disabled=true;
	dom.get("slectAllEstimates").disabled=true;
	disableSelectEstimates();
</s:if>
<s:if test="%{sourcepage == 'inbox' && abstractEstimateList.size() != 0 && model.id!=null && (model.status.code=='NEW' || model.status.code=='REJECTED')}">
	dom.get("slectAllEstimates").checked=true;
	dom.get("newWardSearch").disabled=false;
	dom.get("newExecutingDepartment").disabled=false;
	dom.get("slectAllEstimates").disabled=false;
	enableSelectEstimates();
</s:if>
<s:if test="%{(sourcepage == 'inbox' || hasErrors()) && abstractEstimateList.size() != 0}">
	if(document.getElementById("departmentid")!=null && document.getElementById("departmentid").value!=-1){
		populateDesignation();
	}
</s:if>
<s:if test="%{isError && abstractEstimateList.size() != 0}">
	dom.get("slectAllEstimates").checked=true;
	selectAllEstimates(dom.get("slectAllEstimates"));	
</s:if>
</script>
</body>
</html>
