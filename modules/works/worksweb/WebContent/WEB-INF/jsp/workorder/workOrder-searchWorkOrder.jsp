<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
	<head>
		<title><s:text name='page.title.search.workorder' /></title>
		<script src="<egov:url path='js/works.js'/>"></script>	
		<style>
		.ui-button
		{
			position: absolute;
			height: 2.0em;
		}
		</style>
		<script type="text/javascript">
		
		function validateAndSubmit(){
		   if($F('status') == -1 && $F('contractorId') == -1 && $F('deptId') == -1
								&& $F('workordernumber').blank()
								&& $F('fromDate').blank()
								&& $F('toDate').blank()){
								var bool=true;
								if(document.getElementById('estimateNumber')){
									if(document.getElementById('estimateNumber').value=="")
										bool=false;
									}
								if (document.getElementById('wpNumber')){
								 if( document.getElementById('wpNumber').value=="")
								 		bool=false;
								}
								if (document.getElementById('tenderFileNumber')){
								 if( document.getElementById('tenderFileNumber').value=="")
								 		bool=false;
								}
			if(!bool){
			$('workOrder_error').show();					
		  	$('mandatory_error').show();
		  	$('mandatory_length_error').hide();
		  	return false;
		  	}
		  }
		  
		 
		   if(!$F('workordernumber').blank()  && $F('workordernumber').length < 4){
				  	$('workOrder_error').show();
				  	$('mandatory_length_error').show();
				  	$('mandatory_error').hide();
				     return false;
			 }
			    $('workOrder_error').hide();
			    document.workOrderForm.action='${pageContext.request.contextPath}/workorder/workOrder!searchWorkOrderDetails.action';
		    	document.workOrderForm.submit();
		    }
		 function gotoPage(obj){
			var currRow=getRow(obj);
			var id = getControlInBranch(currRow,'workorderId');
		    var workOrderStateId=getControlInBranch(currRow,'workOrderStateId');	
			var docNumber = getControlInBranch(currRow,'docNumber');
			var appDate = getControlInBranch(currRow,'appDate');
			var objNo = getControlInBranch(currRow,'objNo');
			var showActions = getControlInBranch(currRow,'showActions');
			if(showActions[1]!=null && obj.value==showActions[1].value){	
				window.open("${pageContext.request.contextPath}/workorder/workOrder!edit.action?id="+id.value+
				"&mode=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
			}
			if(showActions[2]!=null && obj.value==showActions[2].value){	
			
			window.open("${pageContext.request.contextPath}/workorder/workOrder!viewWorkOrderPdf.action?id="+id.value+
				"&mode=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');	
			}
			if(showActions[3]!=null && obj.value==showActions[3].value){
				window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue="+
				workOrderStateId.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
			}
			if(showActions[4]!=null && obj.value==showActions[4].value){ 
				viewDocumentManager(docNumber.value);return false;
			}
			if(showActions[5]!=null && obj.value==showActions[5].value){ 
				window.open("${pageContext.request.contextPath}/workorder/setStatus!edit.action?objectType=WorkOrder&objId="+
				id.value+"&setStatus="+dom.get('setStatus').value+"&appDate="+appDate.value+"&objNo="+objNo.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
			}
			if(showActions[6]!=null && obj.value==showActions[6].value){ 
				window.open("${pageContext.request.contextPath}/workorder/workCompletionDetail!newform.action?mode=new&workOrderId="+
				id.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
			}		
		}	
		 function setworkorderId(elem){
			var currRow=getRow(elem);
			dom.get("workOrderId").value = elem.value; 
			dom.get("woNumber").value = getControlInBranch(currRow,'objNo').value; 
		}
		
		function gotoMB(){
		var id = document.workOrderForm.workOrderId.value;
		if(id!='')
			window.open('${pageContext.request.contextPath}/measurementbook/measurementBook!newform.action?workOrderId='+id,'_self');
		else{
			dom.get("workOrder_error").style.display='';
			document.getElementById("workOrder_error").innerHTML='<s:text name="workorder.not.selected" />';
			return false;
		  }
		  dom.get("workOrder_error").style.display='none';
		  document.getElementById("workOrder_error").innerHTML='';
		}
		
		function gotoBill(){
		var id = document.workOrderForm.workOrderId.value;
		if(id!='')
			window.open('${pageContext.request.contextPath}/contractorBill/contractorBill!newform.action?workOrderId='+id	,'_self');
		else{
			dom.get("workOrder_error").style.display='';
			document.getElementById("workOrder_error").innerHTML='<s:text name="contractorBill.workorder.not.selected" />';
			return false;
		  }
		  dom.get("workOrder_error").style.display='none';
		  document.getElementById("workOrder_error").innerHTML='';
		}
		function gotoReturnSecurityDeposit(){
		var id = document.workOrderForm.workOrderId.value;
		if(id!='')
			window.open('${pageContext.request.contextPath}/securityDeposit/returnSecurityDeposit!newform.action?workOrderId='+id	,'_self');
		else{
			dom.get("workOrder_error").style.display='';
			document.getElementById("workOrder_error").innerHTML='<s:text name="contractorBill.workorder.not.selected" />';
			return false;
		  }
		  dom.get("workOrder_error").style.display='none';
		  document.getElementById("workOrder_error").innerHTML='';
		}
		function gotoRefundRetentionMoney(){
			var id = document.workOrderForm.workOrderId.value;
			if(id!='')
				window.open('${pageContext.request.contextPath}/retentionMoney/retentionMoneyRefund!newform.action?workOrderId='+id	,'_self');
			else{
				dom.get("workOrder_error").style.display='';
				document.getElementById("workOrder_error").innerHTML='<s:text name="contractorBill.workorder.not.selected" />';
				return false;
			  }
			  dom.get("workOrder_error").style.display='none';
			  document.getElementById("workOrder_error").innerHTML='';
			}
		
		function disableSelect(){
			document.getElementById('status').disabled=true
		}
		
		function enableSelect(){
			document.getElementById('status').disabled=false
		}
		
		function setWOId(elem){
			var currRow=getRow(elem);
			dom.get("workOrderId").value = elem.value;	
			dom.get("woNumber").value = getControlInBranch(currRow,'objNo').value; 	
		}
		
		function getApprovedMBs(workOrderId){
		    makeJSONCall(["mbRefNo"],'${pageContext.request.contextPath}/workorder/ajaxWorkOrder!getApprovedMBsForWorkOrder.action',{workOrderId:workOrderId},mbLoadHandler,mbLoadFailureHandler) ;
		}
		
		mbLoadHandler = function(req,res){
		  results=res.results;
		  var mbRefNo=''
		  if(results != '') {
		  	for(var i=0; i<results.length;i++) {
		  	if(mbRefNo!='')
				mbRefNo=mbRefNo+', MB#:'+results[i].mbRefNo;
			else
				mbRefNo=results[i].mbRefNo;
			}
		  }
		  	
			if(mbRefNo != ''){
				dom.get("workOrder_error").style.display='';
				document.getElementById("workOrder_error").innerHTML='<s:text name="cencelWO.bill.created.message"/>'+mbRefNo;
				window.scroll(0,0);
				return false;
			}	
			var id = document.workOrderForm.workOrderId.value; 	
			var cancelRemarks = document.workOrderForm.cancelRemarks.value; 
			if(validateCancel())
				window.open('${pageContext.request.contextPath}/workorder/workOrder!cancelApprovedWO.action?workOrderId='+id+'&sourcepage=cancelWO&cancelRemarks='+cancelRemarks,'_self');
			else
				return false;
		}
		
		mbLoadFailureHandler= function(){
		    dom.get("errorstyle").style.display='';
			document.getElementById("errorstyle").innerHTML='Unable to get MBs for Work Order';
		}
		
		function cancelWorkOrder(){
			var id = document.workOrderForm.workOrderId.value; 	
			if(id!=''){		
				getApprovedMBs(id);			
				}		
			else{
				dom.get("workOrder_error").style.display='';
				document.getElementById("workOrder_error").innerHTML='<s:text name="wo.cancel.not.selected" />';
				window.scroll(0,0);
				return false;
			  }
			  dom.get("workOrder_error").style.display='none';
			  document.getElementById("workOrder_error").innerHTML='';
			  if(dom.get("errorstyle")){
			  	dom.get("errorstyle").style.display='none';
			 	dom.get("errorstyle").innerHTML='';
			}
		}	
		
		function validateCancel() {
			var msg='<s:text name="wo.cancel.confirm"/>';
			var woNo=dom.get("woNumber").value; 
			if(!confirmCancel(msg,woNo)) {
				return false;
			}
			else {
				return true;
			}
		}
		
		</script>
	</head>


	<body onload="init();"> 
		<div class="errorstyle" id="searchEstimate_error"
			style="display: none;"></div>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:form name="workOrderForm" 
			id="workOrderForm" theme="simple"
			onsubmit="return validateAndSubmit();enableSelect()">
			
			<div class="errorstyle" id="workOrder_error" style="display: none;">
			<span id="mandatory_error" style="display: none;"><s:text name="workorder.serach.mandatory.error" /></span>
			<span id="mandatory_length_error" style="display: none;"><s:text name="workorder.serach.mandatory.length.error" /></span>
		    </div>
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
								<s:hidden name="setStatus" id="setStatus" value="%{setStatus}" />
								<s:hidden name="sourcepage" id="sourcepage" value="%{sourcepage}" />
								<s:hidden id="workOrderId" name="workOrderId" />
								<s:hidden id="woNumber" name="woNumber" />	
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
														<s:text name="page.subheader.search.workorder" />
													</div>
												</td>
											</tr>
											
											<tr>
												<td class="whiteboxwk">
													<s:text name="workorder.search.status" />:
												</td>
												<td class="whitebox2wk">
												<s:if test="%{sourcepage.equals('searchWOForMBCreation') || sourcepage.equals('searchWOForBillCreation') || sourcepage.equals('searchWOForReturnSD')  || sourcepage.equals('searchWOForRetentionMR')}">
													<s:select id="status" name="status" 
															cssClass="selectwk"
															list="%{workOrderStatusesForMBCreation}" value="%{status}"
															listKey="code" listValue="description"
															/>
												</s:if>
												<s:elseif test="%{sourcepage.equals('cancelWO') }">												
													<s:select id="status" name="status" headerKey="-1"
															headerValue="--- Select ---" cssClass="selectwk"
															list="%{workOrderStatuses}" value="%{woStatus}"
															listKey="code" listValue="description"
															/>
															<script>disableSelect();</script>
												</s:elseif>
												<s:else>
													<s:select id="status" name="status" headerKey="-1"
															headerValue="--- Select ---" cssClass="selectwk"
															list="%{workOrderStatuses}" value="%{status}"
															listKey="code" listValue="description"
															/>
												</s:else>
												</td>
												<td class="whiteboxwk">
													<s:text name="workorder.search.workordernumber" />:
												</td>
											        <td class="whitebox2wk">
													 <s:textfield name="workOrderNumber"
														value="%{workOrderNumber}" id="workordernumber"
														cssClass="selectwk" />
												</td>
												
											</tr>
											<tr>
												<td class="greyboxwk">
													<s:text name="workorder.search.workorderFromdate" />:
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
												<td class="greyboxwk">
													<s:text name="workorder.search.workorderTodate" />:
												</td>
												<td class="greybox2wk">
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
													<s:text name="workorder.search.contractor" />:
												</td>
												
													<td colspan="3" class="whitebox2wk">
														<s:select id="contractorId" name="contractor"
															cssClass="selectwk"
															list="%{contractorForApprovedWorkOrder}"  headerKey="-1"
															headerValue="" value="%{contractor.id}" />
													</td>
												
											</tr>
											<s:if
													test="%{wOCreationForEstimateOrWP.toLowerCase().equals('workspackage') 
													|| wOCreationForEstimateOrWP.toLowerCase().equals('both')}">
												
											<tr>
													<td class="greyboxwk">
														<s:text name="tenderNegotiation.worksPackageNo" />
														:
													</td>

													<td class="greybox2wk">
														<s:textfield name="wpNumber" id="wpNumber"
															cssClass="selectboldwk" />
													</td>

													<td class="greyboxwk">
														<s:text name="estimate.tenderfilenumber" />
														:
													</td>
													<td class="greybox2wk">
														<s:textfield name="tenderFileNumber" id="tenderFileNumber"
															cssClass="selectboldwk" />
													</td>
											</tr>
											</s:if>
											<tr>
											<td class="whiteboxwk">
													<s:text name="workOrder.executingDepartment" />
													:
												</td>
												<td class="whitebox2wk">
												<s:if test="%{sourcepage.equals('searchWOForMBCreation') || sourcepage.equals('searchWOForBillCreation')}">
												<s:if test="%{dropdownData.deptListForMB.size()==1}">
												<s:select id="deptId" name="deptId"
														cssClass="selectwk" list="%{dropdownData.deptListForMB}"
														listKey="id" listValue="deptName"  />
												</s:if>
												<s:else>
												<s:select id="deptId" name="deptId"
														cssClass="selectwk" list="%{dropdownData.deptListForMB}"
														listKey="id" listValue="deptName" headerKey="-1"
														headerValue="--- Select ---" />
												</s:else>
												</s:if>
												<s:else>
												<s:select id="deptId" name="deptId"
														cssClass="selectwk" list="%{dropdownData.deptListForSearch}"
														listKey="id" listValue="deptName" headerKey="-1"
														headerValue="--- Select ---" />
												</s:else>
												</td>
													<s:if
													test="%{wOCreationForEstimateOrWP.toLowerCase().equals('estimate') 
													|| wOCreationForEstimateOrWP.toLowerCase().equals('both')}">
													<td class="whiteboxwk">
														<s:text name="estimate.number" />
														:
													</td>
													<td class="whitebox2wk">
														<s:textfield name="estimateNumber" id="estimateNumber"
															cssClass="selectboldwk" />
													</td>
												</s:if>
													<s:else>
												<td class="whiteboxwk">
													&nbsp;
												</td>
											        <td class="whitebox2wk">
													&nbsp; 
												   </td>
												</s:else>
												   </tr>
												   <s:if test="%{sourcepage.equals('searchWOForBillCreation')}">
													   <tr>
													   		<td class="greyboxwk"><s:text name="contractorBill.mb.preparedby" />:</td>
															<td class="greybox2wk">
																<s:select headerKey="-1" headerValue="%{getText('estimate.default.select')}" 
																	name="mbPreparedBy" value="%{mbPreparedBy}" 
																	id="mbPreparedBy" cssClass="selectwk" list="dropdownData.preparedByListForMB" listKey="id" 
																	listValue="employeeName"/>
															</td>
															<td class="greyboxwk">
																<s:text name="measurementbook.mbref" />
																:
															</td>
															<td class="greybox2wk">
																<s:textfield name="mbRefNo" id="mbRefNo"
																	cssClass="selectboldwk" />
															</td>
													   </tr>
													</s:if>
											<tr>
												<td colspan="4" class="shadowwk"></td>
											</tr>
											
											<tr>
												<td colspan="4">
													<div  class="buttonholderwk" align="center">
														<s:submit  cssClass="buttonadd" value="SEARCH"
															id="saveButton" name="button"  onclick="enableSelect()"
															/>
														<input type="button" class="buttonfinal" value="CLOSE"
															id="closeButton" name="button" onclick="window.close();" />
													</div>
												</td>
											</tr>
											
											

										</table>
									</td>
								</tr>
								
								<%@ include file='woSearch-list.jsp'%>
							</table>
							
							<s:if test="%{sourcepage.equals('searchWOForMBCreation')}">
							<P align="center">
								<input type="button" class="buttonadd"
									value="Create Abstract M Book" id="addButton"
									name="createWorkOrderButton" onclick="gotoMB()"
									align="center" />
							</P>
							</s:if>
							<s:if test="%{sourcepage.equals('searchWOForBillCreation')}">
							<P align="center">
								<input type="button" class="buttonadd"
									value="Create Contractor Bill" id="addButton"
									name="createContractorBillButton" onclick="gotoBill()"
									align="center" />
							</P>
							</s:if>
							<s:if test="%{sourcepage.equals('searchWOForReturnSD')}">
							<P align="center">
								<input type="button" class="buttonadd"
									value="Return Security Deposit" id="addButton"
									name="returnSecurityDepositButton" onclick="gotoReturnSecurityDeposit()"
									align="center" />
							</P>
							</s:if>
							<s:if test="%{sourcepage.equals('searchWOForRetentionMR')}">
							<P align="center">
								<input type="button" class="buttonadd"
									value="Refund Retention Money" id="addButton"
									name="refundRetentionMoneyButton" onclick="gotoRefundRetentionMoney()"
									align="center" />
							</P>
							</s:if>
							<s:if test="%{sourcepage.equals('cancelWO')}">
								 <P align="left">
								 <tr>
								  <td colspan="2" class="whiteboxwk">
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="mandatory">*</span><s:text name="cancel.remarks" />:
								 </td>
								 <td colspan="2"  class="whitebox2wk">
									&nbsp;&nbsp;<s:select id="cancelRemarks" name="cancelRemarks" cssClass="selectwk" list="#{'DATA ENTRY MISTAKE':'DATA ENTRY MISTAKE','OTHER':'OTHER'}" />
								</td>	
								</tr>
								</P>
								<P align="center">
									<input type="button" class="buttonadd"
										value="Cancel Work Order" id="addButton"
										name="cancelWO" onclick="cancelWorkOrder()"
										align="center" />
								</P>
							</s:if>
							
							
						</div>
						<!-- end of rbroundbox2 -->
						
					</div>
					<!-- end of insidecontent -->
				</div>
				<!-- end of formmainbox -->
		</s:form>
	</body>
</html>

