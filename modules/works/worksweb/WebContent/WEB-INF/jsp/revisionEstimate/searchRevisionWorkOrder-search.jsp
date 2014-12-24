<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
	<head>
		<title>Search Revision Work Order</title>
	</head>
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
	if($F('woStatus') == -1 && $F('contractorId') == -1
						&& $F('workordernumber').blank()
						&& $F('fromDate').blank()
						&& $F('toDate').blank()){
						var bool=true;
						if(document.getElementById('estimateNumber')){
							if(document.getElementById('estimateNumber').value=="")
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
}

 function setworkorderId(elem){
	var currRow=getRow(elem);
	dom.get("workOrderId").value = elem.value; 
	dom.get("woNumber").value = getControlInBranch(currRow,'objNo').value; 
}

function disableSelect(){
	document.getElementById('woStatus').disabled=true
}

function enableSelect(){
	document.getElementById('woStatus').disabled=false
}

function setWOId(elem){
	var currRow=getRow(elem);
	dom.get("workOrderId").value = elem.value;	
	dom.get("woNumber").value = getControlInBranch(currRow,'objNo').value; 	
}

function disableSelect(){
	document.getElementById('woStatus').disabled=true
}
   

</script>

	<body onload="init();">
		<div class="errorstyle" id="searchEstimate_error"
			style="display: none;"></div>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:form name="searchRevisionWorkOrderForm" 
			id="searchRevisionWorkOrderForm" theme="simple"
			onsubmit="return validateAndSubmit();enableSelect()">
			<s:push value="model">
			<s:hidden id="source" name="source" value="%{source}" />
			<s:hidden id="woId" name="woId" /> 
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
												<td class="whiteboxwk" width="11%">
													<s:text name="revisionWorkOrder.search.status" />:
												</td>
												<td class="whitebox2wk" width="21%">	
														<s:if test="%{source.equals('cancelRWO') }">												
														 	<s:select id="woStatus" name="woStatus" headerKey="-1"
															headerValue="--- Select ---" cssClass="selectwk"
															list="%{dropdownData.statusList}" value="%{woStatus}" 
															listKey="code" listValue="description" /> </td>
															<script>disableSelect();</script> 
														</s:if>
														<s:else>
															<s:select id="woStatus" name="woStatus" headerKey="-1"
															headerValue="--- Select ---" cssClass="selectwk"
															list="%{dropdownData.statusList}" value="%{woStatus}"
															listKey="code" listValue="description" />
														</s:else>
												</td>
												<td class="whiteboxwk" width="11%">
													<s:text name="revisionWorkOrder.search.workordernumber" />:
												</td>
											        <td class="whitebox2wk" width="21%">
													 <s:textfield name="workOrderNumber"
														value="%{workOrderNumber}" id="workordernumber"
														cssClass="selectwk" />
												</td>
												
											</tr>
											<tr>
												<td class="greyboxwk">
													<s:text name="revisionWorkOrder.search.workorderFromdate" />:
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
													<s:text name="revisionWorkOrder.search.workorderTodate" />:
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
													<s:text name="revisionWorkOrder.search.contractor" />:
												</td>
												
											<td colspan="3" class="whitebox2wk"><s:select
													id="contractorId" name="contractorId" cssClass="selectwk"
													list="%{contractorForApprovedWorkOrder}" headerKey="-1"
													headerValue="" value="%{contractorId}" /></td>
												
											</tr>
											
											<tr>
												<td class="greyboxwk">
												<s:text name="revisionEstimate.number" />:</td>
												<td class="greybox2wk">
												<s:textfield name="estimateNumber" id="estimateNumber" cssClass="selectboldwk" /></td>
												<td class="greyboxwk">
													&nbsp;</td>
											    <td class="greybox2wk">
													&nbsp;</td>
											</tr>
											<tr>
												<td colspan="4" class="shadowwk"></td>
											</tr>
											
											<tr>
												<td colspan="4">
													<div  class="buttonholderwk" align="center">
														<s:submit  cssClass="buttonadd" value="SEARCH"
															id="saveButton" name="button"  onclick="enableSelect();validateAndSubmit();" method="searchWorkOrders" />
														<input type="button" class="buttonfinal" value="CLOSE"
															id="closeButton" name="button" onclick="window.close();" />
													</div>
												</td>
											</tr>
											
											<%@ include file='searchRevisionWorkOrder-list.jsp'%>

										</table>
									</td>
								</tr>
								
								
							</table>
						</div>
						<!-- end of rbroundbox2 -->
						
					</div>
					<!-- end of insidecontent -->
				</div>
				<!-- end of formmainbox -->
			</s:push>
		</s:form>
	</body>
</html>

