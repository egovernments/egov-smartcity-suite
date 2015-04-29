<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<html>
	<head>
		<title><s:text name='multiyearEstimateAppr.page.title' />
		</title>
	</head>
	<script src="<egov:url path='js/works.js'/>"></script>
	<script src="<egov:url path='js/helper.js'/>"></script>

	<script type="text/javascript">

		function enableFields(){
			for(i=0;i<document.multiyearEstimateAppr.elements.length;i++){
	        	document.multiyearEstimateAppr.elements[i].disabled=false;
			}
		} 	

		function populateWorkOrderAssigned(elem){
		    var deptId=elem.options[elem.selectedIndex].value;
		    populateengineerIncharge({executingDepartment:deptId});
		    populateengineerIncharge2({executingDepartment:deptId});
		}

		function populateIsSpillOverChecked(obj){
		   if(obj.checked){
			 	obj.value=true;
			 	obj.checked=true;
			}
			else if(!obj.checked){
				dom.get("slectAllSpillOverEstimates").checked=false;
			 	dom.get("slectAllSpillOverEstimates").value=false;
			 	obj.value=false;
			 	obj.checked=false;
			}
		}

		function populateIsMultiyearChecked(obj){
		   if(obj.checked){
			 	obj.value=true;
			 	obj.checked=true;
			}
			else if(!obj.checked){
				dom.get("slectAllMultiyearEstimates").checked=false;
			 	dom.get("slectAllMultiyearEstimates").value=false;
			 	obj.value=false;
			 	obj.checked=false;
			}
		}

		function validateSearch(){
			clearMessage('multiyearEstimateAppr_error');
			document.getElementById("multiyearEstimateAppr_error").style.display='none';
			if(dom.get("userDepartment").value=='' || dom.get("userDepartment").value==-1){
				document.getElementById("multiyearEstimateAppr_error").style.display='';
        		document.getElementById("multiyearEstimateAppr_error").innerHTML='<s:text name="multiyearEstimateAppr.user.dept.null" />';
				return false;
			}
			if(!validateDate()) {
				return false;
			}
			
			return true;
		}

		function selectAllMultiyearEstimates(obj) {
			var length=eval('<s:property value="multiyearEstimateApprSearchResults.size()" />')+1;
			var i;
		   if(obj.checked){
			 	obj.value=true;
			 	obj.checked=true;
				for(i=1;i<length;i++) {
					var id='isMultiyearEstimateSelected'+i.toString();
					dom.get(id).checked=true;
					dom.get(id).value=true;
				}
			}
			else if(!obj.checked){
			 	obj.value=false;
			 	obj.checked=false;
				for(i=1;i<length;i++) {
					var id='isMultiyearEstimateSelected'+i.toString();
					dom.get(id).checked=false;
					dom.get(id).value=false;
				}
			}
		}

		function selectAllSpillOverEstimates(obj) {
			var length=eval('<s:property value="spillOverMultiyearEstimateApprSearchResults.size()" />')+1;
			var i;
		   if(obj.checked){
			 	obj.value=true;
			 	obj.checked=true;
				for(i=1;i<length;i++) {
					var id='isSpillOverEstimateSelected'+i.toString();
					dom.get(id).checked=true;
					dom.get(id).value=true;
				}
			}
			else if(!obj.checked){
			 	obj.value=false;
			 	obj.checked=false;
				for(i=1;i<length;i++) {
					var id='isSpillOverEstimateSelected'+i.toString();
					dom.get(id).checked=false;
					dom.get(id).value=false;
				}
			}
		}


		function enableSelectEstimates() {
			var length=eval('<s:property value="multiyearEstimateApprSearchResults.size()" />')+1;
			var i;
			var id;
			var id1;
			for(i=1;i<length;i++) {
				id='isMultiyearEstimateSelected'+i.toString();
				id1='multiyearEstimateApprAmoount'+i.toString();
				dom.get(id).disabled=false;
				dom.get(id1).disabled=true;
			}
			length=eval('<s:property value="spillOverMultiyearEstimateApprSearchResults.size()" />')+1;
			for(i=1;i<length;i++) {
				id='isSpillOverEstimateSelected'+i.toString();
				id1='spillOverEstimateApprAmoount'+i.toString();
				dom.get(id).disabled=false;
				dom.get(id1).disabled=false;
			}
		}

		function disableSelectEstimates() {
			var length=eval('<s:property value="multiyearEstimateApprSearchResults.size()" />')+1;
			var i;
			var id;
			var id1;
			for(i=1;i<length;i++) {
				id='isMultiyearEstimateSelected'+i.toString();
				id1='multiyearEstimateApprAmoount'+i.toString();
				dom.get(id).disabled=true;
				dom.get(id1).disabled=true;
			}
			length=eval('<s:property value="spillOverMultiyearEstimateApprSearchResults.size()" />')+1;
			for(i=1;i<length;i++) {
				id='isSpillOverEstimateSelected'+i.toString();
				id1='spillOverEstimateApprAmoount'+i.toString();
				dom.get(id).disabled=true;
				dom.get(id1).disabled=true;
			}
		}
	
	 	function validateSubmit(){
			clearMessage('multiyearEstimateAppr_error');
			document.getElementById("multiyearEstimateAppr_error").style.display='none';
			<s:if test="%{model.status==null}">
				if(dom.get("userDepartment").value=='' || dom.get("userDepartment").value==-1){
					document.getElementById("multiyearEstimateAppr_error").style.display='';
	        		document.getElementById("multiyearEstimateAppr_error").innerHTML='<s:text name="multiyearEstimateAppr.user.dept.null" />';
					return false;
				}
			</s:if>
			var links=document.multiyearEstimateAppr.getElementsByTagName("span");
			for(i=0;i<links.length;i++) {
       			if(links[i].innerHTML=='x' && links[i].style.display!='none'){
					document.getElementById("multiyearEstimateAppr_error").style.display='';
    	   			document.getElementById("multiyearEstimateAppr_error").innerHTML='<s:text name="contractor.validate_x.message" />';
    			    return false;
       			}
   			}
			
			var isEstimateSelected=false;
			var multiyearEstimatelength=eval('<s:property value="multiyearEstimateApprSearchResults.size()" />')+1;
			var spillOverEstimatelength=eval('<s:property value="spillOverMultiyearEstimateApprSearchResults.size()" />')+1;
			var i;
			for(i=1;i<multiyearEstimatelength;i++) {
				var id='isMultiyearEstimateSelected'+i.toString();
				if(dom.get(id).checked){
					isEstimateSelected=true;
					break;
				}
			}
			if(!isEstimateSelected){
				for(i=1;i<spillOverEstimatelength;i++) {
					var id='isSpillOverEstimateSelected'+i.toString();
					if(dom.get(id).checked){
						isEstimateSelected=true;
						break;
					}
				}
			}
			if(!isEstimateSelected){
				document.getElementById("multiyearEstimateAppr_error").style.display='';
        		document.getElementById("multiyearEstimateAppr_error").innerHTML='<s:text name="multiyearEstimateAppr.estimates.null" />';
        		return false;
			} 
  			return true;
	  	}

	function validateDate(){
		clearMessage('multiyearEstimateAppr_error');
		document.getElementById("multiyearEstimateAppr_error").style.display='none';
		var error=false;
		if(dom.get("toDate")!=null) {
			validateDateFormat(dom.get("toDate"));
		}

		if(dom.get("fromDate")!=null){
			validateDateFormat(dom.get("fromDate"));
		}
		var links=document.multiyearEstimateAppr.getElementsByTagName("span");
		for(i=0;i<links.length;i++) {
       		if(links[i].innerHTML=='x' && links[i].style.display!='none'){
       			error=true;
				document.getElementById("multiyearEstimateAppr_error").style.display='';
       			document.getElementById("multiyearEstimateAppr_error").innerHTML='<s:text name="contractor.validate_x.message" />';
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
				document.getElementById("multiyearEstimateAppr_error").style.display='';
        		document.getElementById("multiyearEstimateAppr_error").innerHTML='<s:text name="multiyearEstimateAppr.toDate.date.error1"/>';
				return false;
			}

  		}

  		if(dom.get("fromDate").value!=''){
  			var fromDate=dom.get("fromDate").value;
			if(!compareDate(fromDate,currentDate)){
				document.getElementById("multiyearEstimateAppr_error").style.display='';
        		document.getElementById("multiyearEstimateAppr_error").innerHTML='<s:text name="multiyearEstimateAppr.fromDate.date.error1"/>';
				return false;
			}

  		}

  		if(dom.get("toDate").value!='' && dom.get("fromDate").value!=''){
  			var toDate=dom.get("toDate").value;
  			var fromDate=dom.get("fromDate").value;
			if(!compareDate(fromDate,toDate)){
				document.getElementById("multiyearEstimateAppr_error").style.display='';
        		document.getElementById("multiyearEstimateAppr_error").innerHTML='<s:text name="multiyearEstimateAppr.fromDate.date.error2"/>';
				return false;
			}
  		}
  		 
  		return true;
	}
	function validateNumber(obj) {
		var id;
		if(obj.value=='' || isNaN(obj.value)) {
			id=obj.id;
			document.getElementById("error"+id).style.display='';
			obj.focus();
		}
		else {
			id=obj.id;
			document.getElementById("error"+id).style.display='none';
		
		}
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

	var estimateNoSearchSelectionHandler = function(sType, arguments){
		var oData= arguments[2];
	};
function trim(str) {
        return str.replace(/^\s+|\s+$/g,"");
}

function validate(obj,text){
	if(text=='submit_for_approval') {
	    if(dom.get("userDepartment")!=null && dom.get("userDepartment").value==-1) {
				clearMessage('multiyearEstimateAppr_error');
				document.getElementById("multiyearEstimateAppr_error").style.display='none';
				document.getElementById("multiyearEstimateAppr_error").style.display='';
	       		document.getElementById("multiyearEstimateAppr_error").innerHTML='<s:text name="estimate.reappropriation.new.exec.dept.null" />';
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
			dom.get("multiyearEstimateAppr_error").style.display='';
    		document.getElementById("multiyearEstimateAppr_error").innerHTML='<s:text name="estimate.reappropriation.remarks.null" />';
    		return false;
		}
	}
	enableFields();
	return true;
}
  </script>
	<body>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>

		<s:form theme="simple" name="multiyearEstimateAppr"
			id="multiyearEstimateAppr">
			<s:token />
			<s:push value="model">
				<input type="hidden" name="isEnableSelect" id="isEnableSelect" />
				<s:hidden name="sourcepage" value="%{sourcepage}" id="sourcepage" />
				<s:hidden name="id" value="%{id}" id="id" />
				<div id="multiyearEstimateAppr_error" class="errorstyle"
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
												<s:text name="multiyearEstimateAppr.fromdate" />:
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
												<s:text name="multiyearEstimateAppr.todate" />:
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
												<s:text name="multiyearEstimateAppr.exec.dept" />:
											</td>
											<td class="greybox2wk">
												<s:select headerKey="-1"
													headerValue="%{getText('estimate.default.select')}"
													name="execDeptId" id="execDeptId"
													cssClass="selectwk"
													list="dropdownData.userDepartmentList" listKey="id"
													listValue="deptName" value="%{execDeptId}" onchange="populateWorkOrderAssigned(this);"/>
													<egov:ajaxdropdown id="engineerIncharge" fields="['Text','Value']" dropdownId='engineerIncharge' url='workorder/ajaxWorkOrder!getWOAssignedTo1ForDepartment.action'/>
													<egov:ajaxdropdown id="engineerIncharge2" fields="['Text','Value']" dropdownId='engineerIncharge2' url='workorder/ajaxWorkOrder!getWOAssignedTo2ForDepartment.action'/>
											</td>
											<td class="greyboxwk">&nbsp;</td>
											<td class="greybox2wk">&nbsp;</td>
										</tr>
										<tr>
											<td class="whiteboxwk"><span class="mandatory">*</span>
												<s:text name="multiyearEstimateAppr.user.dept" />:
											</td>
											<td class="whitebox2wk">
												<s:select headerKey="-1"
													headerValue="%{getText('estimate.default.select')}"
													name="department" id="userDepartment"
													cssClass="selectwk"
													list="dropdownData.userDepartmentList" listKey="id"
													listValue="deptName" value="%{department.id}" />
											</td>
											<td class="whiteboxwk">
												<s:text name="multiyearEstimateAppr.function" />:
											</td>
											<td class="whitebox2wk">
												<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" 
													name="functionId" id="functionId" cssClass="selectwk" list="dropdownData.functionList" 
													listKey="id" listValue="name" value="%{functionId}" />
											</td>
										</tr>
										<tr>
											<td class="greyboxwk">
												<s:text name="multiyearEstimateAppr.budgethead" />:
											</td>
											<td class="greybox2wk" colspan="3">
													<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" 
														name="budgetGroupId" id="budgetGroupId" cssClass="selectwk" 
														list="dropdownData.budgetGroupList" listKey="id" listValue="name" 
														value="%{budgetGroupId}" />
											</td>
										</tr>
    									<tr>
        									<td class="whiteboxwk">
        										<s:text name="multiyearEstimateAppr.search.user1"/> :
        									</td>
        									<td class="whitebox2wk">
        										<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="engineerIncharge" 
	         										id="engineerIncharge"  cssClass="selectwk" 
	         										list="dropdownData.assignedUserList1" listKey="id" listValue="employeeName" value="%{engineerIncharge}"/>
        									</td>
        									<td class="whiteboxwk">
        										<s:text name="multiyearEstimateAppr.search.user2"/> :
        									</td>
        									<td class="whitebox2wk">
        										<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="engineerIncharge2" 
	         										id="engineerIncharge2"  cssClass="selectwk" 
	         										list="dropdownData.assignedUserList2" listKey="id" listValue="employeeName" value="%{engineerIncharge2}"/>
        									</td>
   										</tr>
   										<tr>
        									<td class="greyboxwk">
        										<s:text name="multiyearEstimateAppr.finyear.range"/> :
        									</td>
                							<td class="greybox2wk"><s:property value="%{finYear.finYearRange}"/>
                							
                							<%-- /* estimate number textfield with auto-populate/auto-complete */ --%>
                							<td class="greyboxwk">
												<s:text name="multiyearEstimateAppr.estimate.number"/> : 
											</td>
											<td class="greybox2wk" >
												<div class="yui-skin-sam">
													<div id="estimateNoSearch_autocomplete">
														<div> 
															<s:textfield id="estimateNoSearch" name="estimateNo" value="%{estimateNo}" cssClass="selectwk" />
														</div>
														<span id="estimateNoSearchResults"></span>
													</div>		
												</div>
												<egov:autocomplete name="estimateNoSearch" width="20" field="estimateNoSearch" 
												url="ajaxEstimate!searchEstimateNumberForYearendAppr.action?" queryQuestionMark="false" results="estimateNoSearchResults" 
												handler="estimateNoSearchSelectionHandler" queryLength="3"/>
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
										onclick="window.open('${pageContext.request.contextPath}/estimate/multiyearEstimateAppropriation!search.action?sourcepage=create','_self');" />
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
										<td>&nbsp;</td>
									</tr>
									<s:if test="%{model.department!=null && model.department.deptName!=null}" >
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
																<s:text name="multiyearEstimateAppr.search.result"><s:param name="value" value="%{model.department.deptName}" /><s:param name="finRange" value="%{finYear.finYearRange}" /></s:text>
															</div>
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</s:if>
									<s:if test="%{multiyearEstimateApprSearchResults.size() != 0}">
										<tr>
											<td>
												<display:table name="multiyearEstimateApprSearchResults" uid="currentRow"
													cellpadding="0" cellspacing="0" requestURI=""
													style="border:1px;width:60%;empty-cells:show;border-collapse:collapse;">

													<display:column headerClass="pagetableth"
														class="pagetabletd" 
														title='<input type="checkbox" id="slectAllMultiyearEstimates" onclick="selectAllMultiyearEstimates(this)" />'
														style="width:3%;text-align:center">
														<input type='hidden'
															name='multiyearEstApprDetails[<s:property value='#attr.currentRow_rowNum' />].estimate.id'
															value='<s:property value="#attr.currentRow.estimate.id" />' />
														<s:if test="%{model!=null && model.id!=null && model.status.code!='NEW' && model.status.code!='REJECTED'}" >
															<input type='checkbox' id='isMultiyearEstimateSelected<s:property value='#attr.currentRow_rowNum' />'  
																name='multiyearEstApprDetails[<s:property value='#attr.currentRow_rowNum' />].isChecked' 
																onclick='populateIsMultiyearChecked(this);' value='true' checked='true' readonly='true' disabled='true'/>
														</s:if>
														<s:elseif test="%{model!=null && model.id!=null && (model.status.code=='NEW' || model.status.code=='REJECTED')}" >
															<input type='checkbox' id='isMultiyearEstimateSelected<s:property value='#attr.currentRow_rowNum' />'    
																name='multiyearEstApprDetails[<s:property value='#attr.currentRow_rowNum' />].isChecked' 
																onclick='populateIsMultiyearChecked(this);' value='true' checked='true'/>
														</s:elseif>
														<s:else>
															<input type='checkbox' id='isMultiyearEstimateSelected<s:property value='#attr.currentRow_rowNum' />'   
																name='multiyearEstApprDetails[<s:property value='#attr.currentRow_rowNum' />].isChecked' 
																onclick='populateIsMultiyearChecked(this);' value='false' />
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
														property="estimate.estimateNumber" />
													<display:column headerClass="pagetableth"
														class="pagetabletd" title="Estimate Date"
														titleKey='estimate.search.estimateDate'
														style="width:6%;text-align:left">
														<s:date name="#attr.currentRow.estimate.estimateDate"
															format="dd/MM/yyyy" />
													</display:column>
													<display:column headerClass="pagetableth"
														class="pagetabletd" title="Total"
														titleKey='estimate.search.total'
														style="width:10%;text-align:right"
														property='estimate.totalAmount.formattedString' />
													<display:column headerClass="pagetableth" class="pagetabletd" 
														title="Percentage Alloted to Current Financiay Year" titleKey="multiyearestimateappr.appr.amount"
														style="width:20%;text-align:right" >
														<s:text name="contractor.format.number" >
												   			<s:param name="rate" value='%{#attr.currentRow.pendingAppr}' /></s:text>
													</display:column>
													<display:column headerClass="pagetableth" class="pagetabletd" 
														title="Alloted Amount" titleKey="multiyearestimateappr.appr.amount"
														style="width:7%;text-align:right" >
														<input type='text' class="amount" id='multiyearEstimateApprAmoount<s:property value='#attr.currentRow_rowNum' />'
															name='multiyearEstApprDetails[<s:property value='#attr.currentRow_rowNum' />].apprAmount'
															value='<s:property value="#attr.currentRow.apprAmount" />' readonly="true" disabled="true"/>
													</display:column>
												</display:table>
											</td>
										</tr>
									</s:if>
									<s:else>
										<s:if test="%{model.department!=null && model.department.deptName!=null}" >
											<tr>
												<td align="center">
													<font color="red"><s:text name="multiyearEstimateAppr.search.result.not.found"/></font>
												</td>
											</tr>
										</s:if>
									</s:else>
								</table>
								<br />
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td>&nbsp;</td>
									</tr>
									<s:if test="%{model.department!=null && model.department.deptName!=null}" >
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
																<s:text name="multiyearEstimateAppr.spillover.search.result"><s:param name="value" value="%{model.department.deptName}" /><s:param name="finRange" value="%{finYear.finYearRange}" /></s:text>
															</div>
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</s:if>
									<s:if test="%{spillOverMultiyearEstimateApprSearchResults.size() != 0}">
										<tr>
											<td>
												<display:table name="spillOverMultiyearEstimateApprSearchResults" uid="currentRow"
													cellpadding="0" cellspacing="0" requestURI=""
													style="border:1px;width:60%;empty-cells:show;border-collapse:collapse;">

													<display:column headerClass="pagetableth"
														class="pagetabletd" 
														title='<input type="checkbox" id="slectAllSpillOverEstimates" onclick="selectAllSpillOverEstimates(this)" />'
														style="width:3%;text-align:center">
														<input type='hidden'
															name='spillOverEstimateApprDetails[<s:property value='#attr.currentRow_rowNum' />].estimate.id'
															value='<s:property value="#attr.currentRow.estimate.id" />' />
														<s:if test="%{model!=null && model.id!=null && model.status.code!='NEW' && model.status.code!='REJECTED'}" >
															<input type='checkbox' id='isSpillOverEstimateSelected<s:property value='#attr.currentRow_rowNum' />'  
																name='spillOverEstimateApprDetails[<s:property value='#attr.currentRow_rowNum' />].isChecked' 
																onclick='populateIsSpillOverChecked(this);' value='true' checked='true' readonly='true' disabled='true'/>
														</s:if>
														<s:elseif test="%{model!=null && model.id!=null && (model.status.code=='NEW' || model.status.code=='REJECTED')}" >
															<input type='checkbox' id='isSpillOverEstimateSelected<s:property value='#attr.currentRow_rowNum' />'    
																name='spillOverEstimateApprDetails[<s:property value='#attr.currentRow_rowNum' />].isChecked' 
																onclick='populateIsSpillOverChecked(this);' value='true' checked='true'/>
														</s:elseif>
														<s:else>
															<input type='checkbox' id='isSpillOverEstimateSelected<s:property value='#attr.currentRow_rowNum' />'   
																name='spillOverEstimateApprDetails[<s:property value='#attr.currentRow_rowNum' />].isChecked' 
																onclick='populateIsSpillOverChecked(this);' value='false' />
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
														property="estimate.estimateNumber" />
													<display:column headerClass="pagetableth"
														class="pagetabletd" title="Executing Department"
														style="width:20%;text-align:left"
														property="estimate.executingDepartment.deptName" />	
													<display:column headerClass="pagetableth"
														class="pagetabletd" title="Estimate Date"
														titleKey='estimate.search.estimateDate'
														style="width:6%;text-align:left">
														<s:date name="#attr.currentRow.estimate.estimateDate"
															format="dd/MM/yyyy" />
													</display:column>
													<display:column headerClass="pagetableth"
														class="pagetabletd" title="Total"
														titleKey='estimate.search.total'
														style="width:10%;text-align:right"
														property='estimate.totalAmount.formattedString' />
													<display:column headerClass="pagetableth" class="pagetabletd" 
														title="Total Expenditure" titleKey="multiyearestimateappr.total.expenditure"
														style="width:10%;text-align:right" >
														<s:text name="contractor.format.number" >
												   			<s:param name="rate" value='%{#attr.currentRow.totalExpenditureAmount}' /></s:text>
													</display:column>
													<display:column headerClass="pagetableth" class="pagetabletd" 
														title="Pending Amount for Appr" titleKey="multiyearestimateappr.pending.appr.amount"
														style="width:10%;text-align:right" >
														<s:text name="contractor.format.number" >
												   			<s:param name="rate" value='%{#attr.currentRow.pendingAppr}' /></s:text>
													</display:column>
													<display:column headerClass="pagetableth" class="pagetabletd" 
														title="Amount Alloted" titleKey="multiyearestimateappr.appr.amount"
														style="width:7%;text-align:right" >
														<input type='text' class="amount" id='spillOverEstimateApprAmoount<s:property value='#attr.currentRow_rowNum' />'
															name='spillOverEstimateApprDetails[<s:property value='#attr.currentRow_rowNum' />].apprAmount'
															value='<s:property value="#attr.currentRow.apprAmount" />' onblur="validateNumber(this);"/><span id='errorspillOverEstimateApprAmoount<s:property value='#attr.currentRow_rowNum' />' style="display: none; color: red; font-weight: bold">x</span>
															
													</display:column>
												</display:table>
											</td>
										</tr>
									</s:if>
									<s:else>
										<s:if test="%{model.department!=null && model.department.deptName!=null}" >
											<tr>
												<td align="center">
													<font color="red"><s:text name="multiyearEstimateAppr.spillover.search.result.not.found"/></font>
												</td>
											</tr>
										</s:if>
									</s:else>
								</table>
									<s:if test="%{spillOverMultiyearEstimateApprSearchResults.size() != 0 || multiyearEstimateApprSearchResults.size() != 0}" >
										<s:hidden name="scriptName" id="scriptName"
											value="MultiyearEstimateAppropriation"></s:hidden>
										<s:hidden name="actionName" id="actionName" />
										<div id="workflow_info_and_actions">
											<br/>
											<div id="manual_workflow">
												<%@ include file="../workflow/workflow.jsp"%>
											</div>
											<div class="buttonholderwk">
												<s:if test="%{(sourcepage=='inbox' || model.status==null || hasErrors())}">
													<s:iterator value="%{validActions}">
														<s:if test="%{description!=''}">
															<s:if test="%{description=='CANCEL'}">
																<s:submit type="submit" cssClass="buttonfinal"
																	value="%{description}" id="%{name}" name="%{name}"
																	method="cancelWorkflow"
																	onclick="document.multiyearEstimateAppr.actionName.value='%{name}';return validate('cancel','%{name}');" />
															</s:if>
															<s:elseif test="%{description=='REJECT'}">
																<s:submit type="submit" cssClass="buttonfinal"
																	value="%{description}" id="%{name}" name="%{name}"
																	method="rejectWorkflow"
																	onclick="document.multiyearEstimateAppr.actionName.value='%{name}';return validate('reject','%{name}');" />
															</s:elseif>
															<s:else>
																<s:submit type="submit" cssClass="buttonfinal"
																	value="%{description}" id="%{name}" name="%{name}"
																	method="save"
																	onclick="document.multiyearEstimateAppr.actionName.value='%{name}';return validate('noncancel','%{name}');" />
															</s:else>
														</s:if>
													</s:iterator>
												</s:if>
												<input type="button" class="buttonfinal" value="CLOSE"
													id="wfcloseButton" 
													onclick="window.close();" />
											</div>
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
<s:if test="%{sourcepage == 'inbox' && (multiyearEstimateApprSearchResults.size() != 0 || spillOverMultiyearEstimateApprSearchResults.size()!=0) && model.id!=null && model.status.code!='CHECKED'}">
	if(dom.get("slectAllSpillOverEstimates")!=null){
		dom.get("slectAllSpillOverEstimates").checked=true;
		dom.get("slectAllSpillOverEstimates").disabled=true;
	}
	if(dom.get("slectAllMultiyearEstimates")!=null){
		dom.get("slectAllMultiyearEstimates").checked=true;
		dom.get("slectAllMultiyearEstimates").disabled=true;
	}
	dom.get("workflow_info_and_actions").style.display='block';
	dom.get("approverCommentsRow").style.display='block';
	disableSelectEstimates();
</s:if>
<s:if test="%{sourcepage == 'inbox' && (multiyearEstimateApprSearchResults.size() != 0 || spillOverMultiyearEstimateApprSearchResults.size()!=0) && model.status.code=='CHECKED'}">
	if(dom.get("slectAllSpillOverEstimates")!=null){
		dom.get("slectAllSpillOverEstimates").checked=true;
		dom.get("slectAllSpillOverEstimates").disabled=true;
	}
	if(dom.get("slectAllMultiyearEstimates")!=null){
		dom.get("slectAllMultiyearEstimates").checked=true;
		dom.get("slectAllMultiyearEstimates").disabled=true;
	}
	dom.get("workflow_info_and_actions").style.display='block';
	dom.get("approverCommentsRow").style.display='block';
	dom.get("workflowDetials").style.display='none';
	disableSelectEstimates();
</s:if>
<s:if test="%{sourcepage == 'inbox' && (multiyearEstimateApprSearchResults.size() != 0 || spillOverMultiyearEstimateApprSearchResults.size()!=0) && model.id!=null && (model.status.code=='NEW' || model.status.code=='REJECTED')}">
	if(dom.get("slectAllSpillOverEstimates")!=null){
		dom.get("slectAllSpillOverEstimates").checked=true;
		dom.get("slectAllSpillOverEstimates").disabled=false;
	}
	if(dom.get("slectAllMultiyearEstimates")!=null){
		dom.get("slectAllMultiyearEstimates").checked=true;
		dom.get("slectAllMultiyearEstimates").disabled=false;
	}
	enableSelectEstimates();
</s:if>
<s:if test="%{(sourcepage == 'inbox' || hasErrors()) && (multiyearEstimateApprSearchResults.size() != 0 || spillOverMultiyearEstimateApprSearchResults.size()!=0)}">
	if(document.getElementById("departmentid")!=null && document.getElementById("departmentid").value!=-1){
		populateDesignation();
	}
</s:if>
<s:if test="%{isError && (multiyearEstimateApprSearchResults.size() != 0 || spillOverMultiyearEstimateApprSearchResults.size()!=0)}">
	if(dom.get("slectAllSpillOverEstimates")!=null){
		dom.get("slectAllSpillOverEstimates").checked=true;
		dom.get("slectAllSpillOverEstimates").disabled=true;
		selectAllSpillOverEstimates(dom.get("slectAllSpillOverEstimates"));
	}
	if(dom.get("slectAllMultiyearEstimates")!=null){
		dom.get("slectAllMultiyearEstimates").checked=true;
		dom.get("slectAllMultiyearEstimates").disabled=false;
		selectAllMultiyearEstimates(dom.get("slectAllMultiyearEstimates"));
	}
</s:if>
<s:if test="%{hasErrors() && (model.status==null) && (multiyearEstimateApprSearchResults.size() != 0 || spillOverMultiyearEstimateApprSearchResults.size()!=0)}">
	if(dom.get("slectAllSpillOverEstimates")!=null){
		dom.get("slectAllSpillOverEstimates").checked=true;
		selectAllSpillOverEstimates(dom.get("slectAllSpillOverEstimates"));
	}
	if(dom.get("slectAllMultiyearEstimates")!=null){
		dom.get("slectAllMultiyearEstimates").checked=true;
		selectAllMultiyearEstimates(dom.get("slectAllMultiyearEstimates"));
	}
</s:if>
</script>
</body>
</html>
