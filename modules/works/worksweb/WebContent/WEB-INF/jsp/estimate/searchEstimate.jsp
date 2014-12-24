<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*" %>

<html>
	<head>
		<s:if test="%{source=='financialdetail'}">
			<title><s:text name='page.title.financial.detail' />
			</title>
		</s:if>
		<s:elseif test="%{source=='technical sanction'}">
			<title><s:text name='page.title.Technical.Sanction' />
			</title>
		</s:elseif>
		<s:elseif test="%{source=='Financial Sanction'}">
			<title><s:text name='page.title.Financial.Sanction' />
			</title>
		</s:elseif>
		<s:elseif test="%{source=='AdministrativeSanction'}">
			<title><s:text name='page.title.Admin.Sanction' />
			</title>
		</s:elseif>
		<s:elseif test="%{source=='SearchEstimateforWO'}">
			<title><s:text name='page.title.Estimate.WorkOrder' />
			</title>
		</s:elseif>
		<s:else>
			<title><s:text name='page.title.search.estimate' />
			</title>
		</s:else>
	
		<script type="text/javascript">
			
		function openContractor(){
			window.open("${pageContext.request.contextPath}/masters/contractor!searchResult.action?&sourcepage=rcEstimateSearch",'',"width=800, height=800, resizable=yes, scrollbars=yes, left=250,top=400");
		}
			
		function update(elemValue) {	
			if(elemValue!="" || elemValue!=null) {
					var a = elemValue.split("`~`");
					var row_id=a[0];
					var id=a[1];
					var code=a[2];
					var name=a[3];
					document.getElementById("contractor").value=name;
				
			}
		}
		
		    function returnBackToParent() { 
			var value = new Array();
			var wind=window.dialogArguments;
			var len=document.forms[0].selectedEstimate.length; 
			var j=0;
			if(len >0){
				for (i = 0; i < len; i++){
					if(document.forms[0].selectedEstimate[i].checked){
						value[j] = document.forms[0].abEstId[i].value;
						j++;
					}
				}
			}
			else{
				if(document.forms[0].selectedEstimate.checked){
					value[j] = dom.get('abEstId').value;
				}
			}
			if(value.length>0)
			{
				var wind;
				var data = new Array();
				wind=window.dialogArguments;
				if(wind==undefined){
					wind=window.opener;
					data='estimate'+'~'+value;
					window.opener.update(data);
				}
				else{
					wind=window.dialogArguments;
					wind.result='estimate'+'~'+value;
				}
				window.close();
			}
			else{
				dom.get("searchEstimate_error").innerHTML='Please Select any one of the estimates'; 
		        dom.get("searchEstimate_error").style.display='';
				return false;
			 }
			 dom.get("searchEstimate_error").style.display='none';
			 dom.get("searchEstimate_error").innerHTML='';
			return true;
		}
		function enableprintButton(){
		document.getElementById('printButton').style.display='';
		document.getElementById('printButton').style.visibility='visible';
		}     
		function disableSelect(){
			document.getElementById('status').disabled=true
		}
		function disableDept(){
			document.getElementById('status').disabled=true
			dom.get('executingDepartment').disabled=true 
		}
		function enableSelect(){
			if(document.getElementById('status')!=null){
				document.getElementById('status').disabled=false
			}
			dom.get('executingDepartment').disabled=false
			if(document.techSanctionEstimatesForm.isSpillOverWorks!=null) {
				document.techSanctionEstimatesForm.isSpillOverWorks.disabled=false;
			}
		}
		
		function enableCreateTrackMilestone(){
				dom.get("trackMilestoneButton").style.visibility='visible';
				dom.get("createMilestoneButton").style.visibility='visible';
				dom.get("viewMilestoneButton").style.visibility='hidden';
		}
		
		function disableCreateTrackMilestone(){
				dom.get("trackMilestoneButton").style.visibility='hidden';
				dom.get("createMilestoneButton").style.visibility='hidden';
				dom.get("viewMilestoneButton").style.visibility='visible';
		}
		
		
		function viewMilestone(){
			clearMessage('error_search');
			var woEstimateId=document.getElementById('woEstimateId').value;
			if(woEstimateId==null || woEstimateId==''){
				showMessage('error_search','<s:text name="search.estimate.milestone.view"/>');
			}
			else{
				clearMessage('error_search');
				window.open('${pageContext.request.contextPath}/milestone/trackMilestone!view.action?woEstimateId='+woEstimateId+'&mode=view','_self');
			}
		}
		
		function createMilestone(){
			clearMessage('error_search');
			var woEstimateId=document.getElementById('woEstimateId').value;
			if(woEstimateId==null || woEstimateId==''){
				showMessage('error_search','<s:text name="search.estimate.milestone.create"/>');
			}
			else{
				clearMessage('error_search');
				window.open('${pageContext.request.contextPath}/milestone/milestone!newform.action?woEstimateId='+woEstimateId,'_self');
			}
		}
		
		function trackMilestone(){
			clearMessage('error_search');
			var woEstimateId=document.getElementById('woEstimateId').value;
			if(woEstimateId==null || woEstimateId==''){
				showMessage('error_search','<s:text name="search.estimate.milestone.create"/>');
			}
			else{
				clearMessage('error_search');
				window.open('${pageContext.request.contextPath}/milestone/trackMilestone!newform.action?woEstimateId='+woEstimateId,'_self');
			}
		}
		
		function populateUser1(obj){
			var elem=document.getElementById('executingDepartment');
			deptId=elem.options[elem.selectedIndex].value;
			populateengineerIncharge({desgId:obj.value,executingDepartment:deptId})
		}
		function populateUser2(obj){
			var elem=document.getElementById('executingDepartment');
			deptId=elem.options[elem.selectedIndex].value;
			populateengineerIncharge2({desgId:obj.value,executingDepartment:deptId})
		}
		
		function populateDesignation1(){	
			if(dom.get("executingDepartment").value!="" && dom.get("executingDepartment").value!="-1"){
				populateassignedTo1({departmentName:dom.get("executingDepartment").options[dom.get("executingDepartment").selectedIndex].text})
				populateassignedTo2({departmentName:dom.get("executingDepartment").options[dom.get("executingDepartment").selectedIndex].text})
			}
			else {removeAllOptions(dom.get("assignedTo1"));
			removeAllOptions(dom.get("engineerIncharge"));
			<s:if test="%{'yes'.equals(assignedToRequiredOrNot)}">
			removeAllOptions(dom.get("assignedTo2")); removeAllOptions(dom.get("engineerIncharge2"));
			</s:if>
			}
		}
		
		
		function addFinancial(){
			document.getElementById('status').disabled=false;
			var id = document.techSanctionEstimatesForm.estimateId.value;
			if(id==null || id==''){
				showMessage('error_search','Please Choose An Estimate Before Adding Financial Details');
			}
			else{
				clearMessage('error_search')
				window.open('${pageContext.request.contextPath}/estimate/financialDetail.action?estimateId='+id,'_self');
			} 
		}
		
		function createNegotiation(){
			document.getElementById('status').disabled=false;
			var id = document.techSanctionEstimatesForm.estimateId.value;
			
		
			if(id==null || id==''){
				showMessage('error_search','<s:text name="search.estimate.negotiation.create"/>');
				disableSelect();
			}
			else{
				clearMessage('error_search')
				window.open('${pageContext.request.contextPath}/tender/tenderNegotiation!newform.action?tenderSource=estimate&estimateId='+id,'_self');
			}
		}
		
		function createWO(){
		 	var id = document.techSanctionEstimatesForm.estimateId.value;
			if(id==null || id==''){
				showMessage('error_search','<s:text name="search.estimate.WorkOrder.create"/>');
				disableSelect();
			}
			else{
				 clearMessage('error_search');
			}
		}
		function createSO(){
			var id="";
			for (i=0; i<estimateArr.length; i++) {
				if(id == ''){
					id=estimateArr[i];
				}else{
					id=id+"`~`"+estimateArr[i];
				}
				
			}
			if(id==null || id==''){
				showMessage('error_search','Please select at least one estimate');
				disableSelect();
			}
			else{
				 clearMessage('error_search');
				 window.open('${pageContext.request.contextPath}/serviceOrder/serviceOrder!newform.action?estimateId='+id,'_self');
			}
		 	
		}
		function validateSearch()
		{
		 	if(dom.get('source').value=="searchEstimateForMilestone" || dom.get('source').value=="viewMilestone"){
				if(dom.get('executingDepartment').value==-1){
					dom.get("searchEstimate_error").innerHTML='Please select Executing Department'; 
		        	dom.get("searchEstimate_error").style.display='';
		        	return false;
				}
			 }
		 <s:if test="%{source!='searchRCEstimate' && source!='searchEstimateForMilestone' && source!='viewMilestone'}"> 
			if(dom.get('type').value==-1 && dom.get('status').value==-1 && 
			dom.get('location').value=="" && dom.get('executingDepartment').value==-1 && dom.get('fromDate').value=="" && dom.get('toDate').value==""  &&  dom.get('preparedBy').value==-1 &&
			dom.get('parentCategory').value==-1 && dom.get('category').value==-1 && dom.get('description').value=="" && dom.get('techSanctionNumber').value=="")
			{
				dom.get("searchEstimate_error").innerHTML='Please Select any one of the Search Parameters'; 
		        dom.get("searchEstimate_error").style.display='';
		        return false;
			 }
			</s:if> 
			 <s:if test="%{source=='searchRCEstimate'}"> 
			 	if(dom.get('contractor').value!="" && dom.get('rateContract').value=="false"){
			 		dom.get("searchEstimate_error").innerHTML='Please select the Rate Contract Check Box'; 
		        dom.get("searchEstimate_error").style.display='';
		        return false;
			 	}
			 	</s:if>
			 	
			 <s:if test="%{source=='createServiceOrderNew'}"> 
			 	if(dom.get('executingDepartment').value==-1){
			 		dom.get("searchEstimate_error").innerHTML='Please Select Executing Department'; 
		        dom.get("searchEstimate_error").style.display='';
		        return false;
			 	}
			 </s:if>
			 dom.get("searchEstimate_error").style.display='none';
			 dom.get("searchEstimate_error").innerHTML='';
			return true;
		}	
		
		function setupSubTypes(elem){
			categoryId=elem.options[elem.selectedIndex].value;
		    populatecategory({category:categoryId});
		}
		
		function setupPreparedByList(elem){
		 	<s:if test="%{!(('wp').equals(source) || ('tenderFile').equals(source))}">
		    deptId=elem.options[elem.selectedIndex].value; 
		    populatepreparedBy({executingDepartment:deptId});
		    </s:if>
		}
		
		function checkPrint(){
		
			if(dom.get('type').value==-1 && dom.get('status').value==-1 && 
			dom.get('location').value=="" && dom.get('executingDepartment').value==-1 && 
			dom.get('fromDate').value=="" && dom.get('toDate').value==""  &&  dom.get('preparedBy').value==-1 &&
			dom.get('parentCategory').value==-1 && dom.get('category').value==-1 && dom.get('description').value=="")
			{
				dom.get("searchEstimate_error").innerHTML='Please search the Estimate Before Printing'; 
		        dom.get("searchEstimate_error").style.display='';
		       	return false;
			 }
			 dom.get("searchEstimate_error").style.display='none';
			 dom.get("searchEstimate_error").innerHTML='';
			 openPrint();
		}
		
		function sortBy() {
			if($F('selectedorder') == 'false')
				$('selectedorder').value =true;
			else
				$('selectedorder').value =false;
			
			//alert($F('selectedorder'));
			this.document.techSanctionEstimatesForm.action = '${pageContext.request.contextPath}/estimate/searchEstimate!search.action';
			this.document.techSanctionEstimatesForm.submit();
		}
		
		function openPrint(){
				var actionUrl = '${pageContext.request.contextPath}/estimate/searchEstimate!printpage.action?decorate=false';
				var params    = $('techSanctionEstimatesForm').serialize();
				
			 	var ajaxCall = new Ajax.Request(actionUrl,
				{
						parameters:params,
						method:'post',
					 	onSuccess:function(transport){
							var printWin = window.open("","printSpecial","menubar=no,toolbar=no,location=no,status=no,resizable=yes,scrollbars=yes");
							printWin.document.open();
							printWin.document.write(transport.responseText);
							printWin.document.close();
							printWin.print();
					   }
					 	
				});
		 //window.open(actionUrl+'?'+params,'mywindow',"height=650,width=980,scrollbars=yes,left=0,top=0,status=yes");
		 
		}
			
		function checkSpillOverWorks(obj){ 
		   if(obj.checked){
			 document.techSanctionEstimatesForm.isSpillOverWorks.value=true;
			}
			else if(!obj.checked){
			 document.techSanctionEstimatesForm.isSpillOverWorks.value=false;
			}
		}
		
		function enableRateContractCheckBox(){ 
			if(document.getElementById('rateContract')!=null){
				document.getElementById('rateContract').disabled=false;
			}
		}
		
		
		</script>
	</head>
	<body>
		<div class="errorstyle" id="searchEstimate_error"
			style="display: none;"></div>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:form name="techSanctionEstimatesForm" action="searchEstimate!search.action"
			id="techSanctionEstimatesForm" theme="simple"
			onsubmit="enableSelect();enableRateContractCheckBox();">
			<s:hidden name="selectedorder" id="selectedorder" />
			<div class="formmainbox">
				<div class="insidecontent">
					<div id="printContent" class="rbroundbox2">
						<div class="rbtop2">
							<div></div>
						</div>
						<div class="rbcontent2">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td>
										&nbsp;
									</td>
								</tr>
								
								<s:if
									test="%{estimateOrWpSearchReq!=null && estimateOrWpSearchReq =='both'}">
									<tr>
										<td>
											<%@ include file="../tender/estimateOrWpSearch.jsp"%>
										</td>
										<script>
 				document.forms[0].tenderForEst.checked=true;
			</script>
									</tr>
								</s:if>
								<tr>
									<td>
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td colspan="4" class="headingwk">
													<div class="arrowiconwk">
														<img
															src="${pageContext.request.contextPath}/image/arrow.gif" />
													</div>
													<div class="headplacer">
														<s:text name="page.subheader.search.estimate" />
													</div>
												</td>
											</tr>
											
											<s:if test="%{source=='searchRCEstimate'}">
											<tr>
											<td colspan="4">
											<%@ include file="../tender/searchTenderFileForWO.jsp"%>
											</td>
										<script>
 										document.forms[0].rcEst.checked=true;
										</script>
											</tr>
											</s:if>
											<s:if test="%{source=='createNegotiationNew'}">
												<tr>
													<td width="11%" class="greyboxwk">
														<s:text name="estimate.search.estimateStatus" />
														:
													</td>
													<td width="21%" class="greybox2wk">
														<s:select id="status" name="status" headerKey="-1"
															headerValue="ALL" cssClass="selectwk"
															list="%{estimateStatuses}" listKey="code"
															listValue="description" />

													</td>
													<script>disableSelect();</script>
													<td width="15%" class="greyboxwk">
														<s:text name="estimate.executing.department" />
														:
													</td>
													<td width="53%" class="greybox2wk">
														<s:select headerKey="-1"
															headerValue="%{getText('estimate.default.select')}"
															name="execDept" id="executingDepartment"
															cssClass="selectwk"
															list="dropdownData.executingDepartmentList" listKey="id"
															listValue="deptName" value="%{execDept}" />
													</td>
													<!-- commented to enable department drop down -->
												<!--  	<s:if test="%{negoCreatedBy=='no'}">  
														<script>disableDept();</script>
													</s:if> -->

													<s:if test="%{source=='wp' || source=='tenderFile'}">
														<script>disableDept();</script>
													</s:if>
												</tr>
											</s:if>
											<s:elseif test="%{source=='SearchEstimateforWO'}">
												<tr>
													<td width="11%" class="greyboxwk">
														<s:text name="search.estimate.negotiationStatus" />
														:
													</td>
													<td width="21%" class="greybox2wk" id="statuslabl">
														<s:textfield name="status" value="%{status}"
															id="negotiationStatus" cssClass="selectwk"
															readonly="true" />
													</td>
													<td width="15%" class="greyboxwk">
														<s:text name="estimate.executing.department" />
														:
													</td>
													<td width="53%" class="greybox2wk">
														<s:select headerKey="-1"
															headerValue="%{getText('estimate.default.select')}"
															name="execDept" id="executingDepartment"
															cssClass="selectwk"
															list="dropdownData.executingDepartmentList" listKey="id"
															listValue="deptName" value="%{execDept}" />
													</td>
													<s:if test="%{negoCreatedBy=='no'}">
														<script>disableDept();</script>
													</s:if>
												</tr>
											</s:elseif>
											<s:else>
												<tr>

													<td width="11%" class="greyboxwk">
														<s:if test="%{source=='searchEstimateForMilestone' || source=='viewMilestone'}">
															<s:text name="estimate.search.projectcode" />
														</s:if>
														<s:else>
															<s:text name="estimate.search.estimateStatus" />
														</s:else>
														:
													</td>
													<td width="21%" class="greybox2wk">
														<s:if test="%{source=='searchEstimateForMilestone' || source=='viewMilestone'}">
															<s:textfield name="projCode"
															value="%{projCode}" id="projCode"
															cssClass="selectwk" />
														</s:if>
														<s:else>
															<s:select id="status" name="status" headerKey="-1"
																headerValue="ALL" cssClass="selectwk"
																list="%{estimateStatuses}" listKey="code"
																listValue="description" />
														</s:else>	
													</td>
													<s:if test="%{source=='createServiceOrderNew' || source=='searchRCEstimate'}"> <script>disableSelect();</script> </s:if>
													<s:if test="%{source=='financialdetail'}">
														<script>disableSelect();</script>
													</s:if>
													<s:if test="%{source=='technical sanction'}">
														<script>disableSelect();</script>
													</s:if>
													<s:if test="%{source=='Financial Sanction'}">
														<script>disableSelect();</script>
													</s:if>
													<s:if test="%{source=='AdministrativeSanction'}">
														<script>disableSelect();</script>
													</s:if>
													<td width="15%" class="greyboxwk"><s:if test="%{source=='createServiceOrderNew' || source=='searchEstimateForMilestone' || source=='viewMilestone'}"> 
													<span class="mandatory">*</span> </s:if>
														<s:text name="estimate.executing.department" />
														:
													</td>
													<td width="53%" class="greybox2wk">
														<s:if test="%{source=='searchEstimateForMilestone' || source=='viewMilestone'}">
																<s:select headerKey="-1"
																	headerValue="%{getText('estimate.default.select')}"
																	name="execDept" id="executingDepartment"
																	cssClass="selectwk"
																	list="dropdownData.executingDepartmentList" listKey="id"
																	listValue="deptName" value="%{execDept}"
																	onChange="populateDesignation1();" />
																<egov:ajaxdropdown id="assignedTo1"fields="['Text','Value']" 
																	dropdownId="assignedTo1" url="workorder/ajaxWorkOrder!getDesignationByDeptId.action" />
																<egov:ajaxdropdown id="assignedTo2"fields="['Text','Value']" 
																	dropdownId="assignedTo2" url="workorder/ajaxWorkOrder!getDesignationByDeptId.action" />
															</s:if>
															<s:else>															
																<s:select headerKey="-1"
																	headerValue="%{getText('estimate.default.select')}"
																	name="execDept" id="executingDepartment"
																	cssClass="selectwk"
																	list="dropdownData.executingDepartmentList" listKey="id"
																	listValue="deptName" value="%{execDept}"
																	onChange="setupPreparedByList(this);" />
																<egov:ajaxdropdown id="preparedBy"
																	fields="['Text','Value','Designation']"
																	dropdownId='preparedBy' optionAttributes='Designation'
																	url='estimate/ajaxEstimate!usersInExecutingDepartment.action' />
															</s:else>
														</td>
													<s:if test="%{source=='wp' || source=='tenderFile'}">
														<script>disableDept();</script>
													</s:if>
												</tr>
											</s:else>
											<s:if test="%{source=='searchRCEstimate'}">
											<tr>
											
												<td width="30%" class="greyboxwk">
													<s:checkbox name="rateContract"	id="rateContract" value="%{rateContract}" cssClass="selectwk" checked="true" disabled="true"/>
													:
												</td>
												
												<td class="greybox2wk">
													<s:text name="Rate Contract" />
													
												</td>
												<td class="greyboxwk"><s:text name="workorder.search.contractor" /> :</td>
												
				 								<td class="greybox2wk" ><s:textfield id="contractor" name="contractor" value="%{contractor}" cssClass="selectwk"/>&nbsp;<a href="javascript:openContractor();"><img src="${pageContext.request.contextPath}/image/searchicon.gif" width="16" height="16" border="0"/></a></td>				
				
												
											</tr>
											</s:if>
											<tr>
												<td width="11%" class="whiteboxwk">
													<s:text name="estimate.search.estimateNo" />
													:
												</td>
												<td width="21%" class="whitebox2wk">
													<s:textfield name="estimatenumber"
														value="%{estimatenumber}" id="location"
														cssClass="selectwk" />
												</td>
												<td width="15%" class="whiteboxwk">
													<s:text name="estimate.work.nature" />
													:
												</td>
												<td width="53%" class="whitebox2wk">
													<s:select headerKey="-1" headerValue="ALL"
														name="expenditureType" id="type" cssClass="selectwk"
														list="dropdownData.typeList" listKey="id" listValue="name"
														value="%{expenditureType}" />
												</td>
												<s:hidden name="expenditureTypeid"
													value="%{expenditureType}"></s:hidden>
											</tr>
											<tr>
												<td class="greyboxwk">
													Estimate Date From:
												</td>
												<td class="greybox2wk">
													<s:date name="fromDate" var="fromDateFormat"
														format="dd/MM/yyyy" />
													<s:textfield name="fromDate" id="fromDate"
														cssClass="selectwk" value="%{fromDateFormat}"
														onfocus="javascript:vDateType='3';"
														onkeyup="DateFormat(this,this.value,event,false,'3')" />
													<a
														href="javascript:show_calendar('forms[0].fromDate',null,null,'DD/MM/YYYY');"
														onmouseover="window.status='Date Picker';return true;"
														onmouseout="window.status='';return true;"> <img
															src="${pageContext.request.contextPath}/image/calendar.png"
															alt="Calendar" width="16" height="16" border="0"
															align="absmiddle" />
													</a>

												</td>
												<td width="17%" class="greyboxwk">
													Estimate Date To:
												</td>
												<td width="17%" class="greybox2wk">
													<s:date name="toDate" var="toDateFormat"
														format="dd/MM/yyyy" />
													<s:textfield name="toDate" id="toDate"
														value="%{toDateFormat}" cssClass="selectwk"
														onfocus="javascript:vDateType='3';"
														onkeyup="DateFormat(this,this.value,event,false,'3')" />
													<a
														href="javascript:show_calendar('forms[0].toDate',null,null,'DD/MM/YYYY');"
														onmouseover="window.status='Date Picker';return true;"
														onmouseout="window.status='';return true;"> <img
															src="${pageContext.request.contextPath}/image/calendar.png"
															alt="Calendar" width="16" height="16" border="0"
															align="absmiddle" />
													</a>
												</td>
											</tr>
											<tr>
												<td class="whiteboxwk">
													<s:text name="estimate.work.type" />
													:
												</td>
												<td class="whitebox2wk">
													<s:select headerKey="-1" headerValue="ALL"
														name="parentCategory" id="parentCategory"
														cssClass="selectwk" list="dropdownData.parentCategoryList"
														listKey="id" listValue="description"
														value="%{parentCategory.id}"
														onChange="setupSubTypes(this);" />
													<egov:ajaxdropdown id="categoryDropdown"
														fields="['Text','Value']" dropdownId='category'
														url='estimate/ajaxEstimate!subcategories.action'
														selectedValue="%{category.id}" />
												</td>

												<td class="whiteboxwk">
													<s:text name="estimate.work.subtype" />
													:
												</td>
												<td class="whitebox2wk">
													<s:select headerKey="-1" headerValue="ALL" name="category"
														value="%{category.id}" id="category" cssClass="selectwk"
														list="dropdownData.categoryList" listKey="id"
														listValue="description" />
												</td>
											</tr>
											<s:hidden name="wpdate" id="wpdate" value="%{wpdate}"></s:hidden>
											<s:hidden name="tenderFileDate" id="tenderFileDate" value="%{tenderFileDate}"></s:hidden>
											<s:if test="%{source!='createNegotiationNew' && source!='SearchEstimateforWO' && source!='searchEstimateForMilestone' && source!='viewMilestone'}">
												<tr>
													<td class="greyboxwk">
														<s:text name="estimate.preparedBy" />
														:
													</td>
													<td class="greybox2wk">
														<s:select headerKey="-1"
															headerValue="%{getText('estimate.default.select')}"
															name="empId" value="%{empId}" id="preparedBy"
															cssClass="selectwk" list="dropdownData.preparedByList"
															listKey="id" listValue="employeeName" />

													</td>

													<td class="greyboxwk">
														<s:text name="estimate.description" />
														:
													</td>
													<td class="greybox2wk">
														<s:textfield name="description" value="%{description}"
															id="description" cssClass="selectwk" />
													</td>
												</tr>
											</s:if>
											<s:elseif test="%{source=='searchEstimateForMilestone' || source=='viewMilestone'}">
    											<tr>
   													<td class="greyboxwk">
   														<s:text name="milestone.search.allocated.to1"/> :
													</td>
													<td class="greybox2wk">
														<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="assignedTo1" 
	         												id="assignedTo1" value="%{assignedTo1}" cssClass="selectwk" 
	         												list="dropdownData.assignedToList" listKey="designationId" listValue="designationName" onchange="populateUser1(this)"/>
	         											<egov:ajaxdropdown id="engineerIncharge" fields="['Text','Value']" dropdownId='engineerIncharge' 
		        	  										url='workorder/ajaxWorkOrder!getUsersForDesg.action'/>
													</td>	
        											<td class="greyboxwk">
        												<s:text name="milestone.search.user1"/> :
        											</td>
        											<td class="greybox2wk">
        												<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="engineerIncharge" 
	         												id="engineerIncharge"  cssClass="selectwk" 
	         												list="dropdownData.assignedUserList1" listKey="id" listValue="employeeName" value="%{engineerIncharge}"/>
        											</td>
   												</tr>
											   <tr>
   													<td class="whiteboxwk">
   			 											<s:text name="milestone.search.allocated.to2"/> :
													</td>
													<td class="whitebox2wk">
														<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="assignedTo2" 
													        id="assignedTo2" value="%{assignedTo2}" cssClass="selectwk" 
	         												list="dropdownData.assignedToList" listKey="designationId" listValue="designationName" onchange="populateUser2(this)"/>
	         											<egov:ajaxdropdown id="engineerIncharge2" fields="['Text','Value']" dropdownId='engineerIncharge2' 
		        	  										url='workorder/ajaxWorkOrder!getUsersForDesg.action'/>
													</td>	
        											<td class="whiteboxwk">
        												<s:text name="milestone.search.user2"/> :
        											</td>
        											<td class="whitebox2wk">
        												<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" name="engineerIncharge2" 
	         												id="engineerIncharge2"  cssClass="selectwk" 
	         												list="dropdownData.assignedUserList2" listKey="id" listValue="employeeName" value="%{engineerIncharge2}"/>
        											</td>

   												</tr>
   												<tr>
   													<td class="greyboxwk">
   															<s:text name="estimate.search.workOrderNo" />:
   													</td>
   													<td class="greybox2wk" colspan="3">
   															<s:textfield name="workOrderNo"
																value="%{workOrderNo}" id="workOrderNo"
																cssClass="selectwk" />
   													
   													</td>
   												</tr>
											</s:elseif>
											<s:if test="%{source=='createNegotiationNew'}">
												<tr>	
													<td width="11%" class="greyboxwk"><s:text name="estimate.search.techSanctionNo" />:</td>
													<td width="21%" class="greybox2wk" colspan="3"><s:textfield name="techSanctionNumber"
														value="%{techSanctionNumber}" id="techSanctionNumber"
														cssClass="selectwk" /></td>
												</tr>
											</s:if>
											<s:elseif test="%{source=='tenderFile'}">
												<tr>
											  		<td width="11%" class="whiteboxwk"><s:text name="estimate.search.techSanctionNo" />:</td>
													<td width="21%" class="whitebox2wk"><s:textfield name="techSanctionNumber"
														value="%{techSanctionNumber}" id="techSanctionNumber"
														cssClass="selectwk" /></td>
											  		<td class="whiteboxwk" ><s:checkbox name="isSpillOverWorks" id="isSpillOverWorks" value="%{isSpillOverWorks}" onclick="checkSpillOverWorks(this)"/></td>
											  		<td class="whitebox2wk"><s:text name="estimate.spillOver.work"/></td>
								              		<s:hidden name="estimateType" value="%{estimateType}"></s:hidden>
														<s:if test="%{estimateType=='spillovertype'}">
															<script>
																document.techSanctionEstimatesForm.isSpillOverWorks.value=true;
																document.techSanctionEstimatesForm.isSpillOverWorks.checked=true;
																document.techSanctionEstimatesForm.isSpillOverWorks.disabled=true;
															</script>
														</s:if>
														<s:elseif test="%{estimateType=='normaltype'}">
															<script>
																document.techSanctionEstimatesForm.isSpillOverWorks.value=false;
																document.techSanctionEstimatesForm.isSpillOverWorks.checked=false;
																document.techSanctionEstimatesForm.isSpillOverWorks.disabled=true;
															</script>
														</s:elseif>
								              	</tr>
											</s:elseif>
											<s:elseif test="%{source=='searchEstimateForMilestone' || source=='viewMilestone'}" >
											</s:elseif>
											<s:else>
												<tr>	
													<td width="11%" class="whiteboxwk"><s:text name="estimate.search.techSanctionNo" />:</td>
													<td width="21%" class="whitebox2wk" colspan="3"><s:textfield name="techSanctionNumber"
														value="%{techSanctionNumber}" id="techSanctionNumber"
														cssClass="selectwk" /></td>
												</tr>
								            </s:else>
											
											<tr>
												<td colspan="4" class="shadowwk"></td>
											</tr>
											
											<tr>
												<td width="30%" colspan="4">
													<div  class="buttonholderwk" align="center">
														<s:submit cssClass="buttonadd" value="SEARCH"
															id="saveButton" name="button"
															onClick="return validateSearch();enableSelect();enableRateContractCheckBox();" method="search"/>
														<input type="button" class="buttonfinal" style="display:none;"
															clcssClass="buttonadd" value="PRINT" id="printButton"
															name="button" onclick="return checkPrint()" />
													</div>
												</td>
											</tr>
											<div class="errorstyle" id="error_search"
												style="display: none;"></div>

											<s:hidden id="estimateId" name="estimateId" />
											<s:hidden id="estimateNumber" name="estimateNumber" />
											<s:hidden id="estimateRcId" name="estimateRcId" />
											<s:hidden id="source" name="source" />
											<table width="100%" border="0" cellspacing="0"
												cellpadding="0">
												<s:if test="%{source=='cancelEstimate'}">
													<tr>
														<td><%@ include file='estimate-list.jsp'%></td>
													</tr>
													</s:if>
													
												<s:elseif test="%{source=='financialdetail'}">
													<tr>
														<td><%@ include file='estimate-list.jsp'%></td>
													</tr>
													<tr>
														<td>
															<div class="buttonholderwk">
																<input type="button" class="buttonadd"
																	value="Update Financial Details " id="addButton"
																	name="updateFinancialDetailButton"
																	onclick="addFinancial();" align="center" />
															</div>
														</td>
													</tr>
												</s:elseif>
												<s:elseif test="%{source=='wp' || source=='tenderFile'}">
													<%@ include file='estimateWP-list.jsp'%>
												</s:elseif>
												<s:elseif
													test="%{source=='createNegotiationNew' || source=='SearchEstimateforWO' || source=='createServiceOrderNew'}">
													<tr>
														<td><%@ include file='estimateNewSearch-list.jsp'%></td>
													</tr>
													<tr>
														<td>
															<div class="buttonholderwk">
																<s:if test="%{source=='createNegotiationNew'}">
																	<input type="button" class="buttonadd"
																		value="Create Negotiation Statement " id="addButton"
																		name="createNegotiationButton"
																		onclick="createNegotiation();" align="center" />
																</s:if>
																<s:elseif test="%{source=='SearchEstimateforWO'}">
																	<input type="button" class="buttonadd"
																		value="Create Work Order " id="addButton"
																		name="createWorkOrderButton" onclick="createWO();"
																		align="center" />
																</s:elseif>
																<s:elseif test="%{source=='createServiceOrderNew'}">
																	<input type="button" class="buttonadd"
																		value="Generate Service Order " id="addButton"
																		name="createServiceOrderButton" onclick="createSO();"
																		align="center" />
																</s:elseif>
																

															</div>
														</td>
													</tr>
												</s:elseif>
												<s:elseif test="%{source=='searchEstimateForMilestone' || source=='viewMilestone'}" >
													<tr>
														<%@ include file='estimateListForMilestone.jsp'%>
													</tr>
													<tr>
														<td>&nbsp;</td>
													</tr>
													<s:if test="%{searchResult.fullListSize != 0 && !hasErrors()}">
														<tr>
															<td>
																<div class="buttonholderwk">
																	<input type="button" class="buttonadd"
																		value="Create Milestone" id="createMilestoneButton"
																		name="createMilestoneButton"
																		onclick="createMilestone();" align="center" />
																	<input type="button" class="buttonadd"
																		value="Track Milestone " id="trackMilestoneButton"
																		name="trackMilestoneButton" onclick="trackMilestone();"
																		align="center" />
																	<input type="button" class="buttonadd"
																		value="View Milestone " id="viewMilestoneButton"
																		name="viewMilestoneButton" onclick="viewMilestone();"
																		align="center" />
																</div>
															</td>
														</tr>
															<s:if test="%{source=='viewMilestone'}">
																<script>disableCreateTrackMilestone();</script>
															</s:if>
															<s:else>
																<script>enableCreateTrackMilestone();</script>
															</s:else>
													</s:if>
												</s:elseif>
												<s:else>
												<script>enableprintButton();</script>
													<s:if test="%{rateContract}" >
														<%@ include file='estimateRateContract-list.jsp'%>
													</s:if>
													<s:else>
												<%@ include file='estimate-list.jsp'%>
												</s:else>
												</s:else>
																																			
											</table>
										</table>
									</td>
								</tr>
							</table>
						</div>
						
						<!-- end of rbroundbox2 -->
						<div class="rbbot2">
							<div></div>
						</div>
					</div>
					<!-- end of insidecontent -->
				</div>
				</div>
				<!-- end of formmainbox -->
		</s:form>
	</body>
</html>