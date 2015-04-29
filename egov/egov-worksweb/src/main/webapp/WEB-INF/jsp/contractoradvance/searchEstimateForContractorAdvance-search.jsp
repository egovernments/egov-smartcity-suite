<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%> 
<html>
	<head>
		<title><s:text name='contractoradvance.title.searchestimate' /></title>
	</head>
<script src="<egov:url path='js/works.js'/>"></script> 

<script type="text/javascript">
function validateAndSubmit(){
	   if(dom.get('estimateNumberSearch').value == "" && dom.get('fromDate').value == "" && dom.get('toDate').value == ""
		   && dom.get('contractorId').value == -1 && dom.get('wpNumberSearch').value == "" && dom.get('tenderNegotiationNumberSearch').value == ""  
		   && dom.get('executingDepartmentId').value == -1 && dom.get('workOrderNumberSearch').value=="") {
	   		dom.get("searchEstimateForAdvance_error").innerHTML='<s:text name="contractoradvance.validation.select.one.search.creteria"/>'
			dom.get("searchEstimateForAdvance_error").style.display='';
        	return false;
	  }
	   else {
			dom.get("searchEstimateForAdvance_error").innerHTML='';
			dom.get("searchEstimateForAdvance_error").style.display="none";
		}
}
function goToCreateAdvanceRequisition(){
	var table=document.getElementById('currentRow');
	var lastRow = table.rows.length-1;
	var woEstimateId="";
	
	if(lastRow==1){
		if(document.getElementById("radio").checked)
			woEstimateId=document.getElementById("woEstimateId").value;		
	}
	else{
		for(i=0;i<lastRow;i++){			
			if(document.forms[0].radio[i].checked){
				woEstimateId=document.forms[0].woEstimateId[i].value;				
			}
		}
	}

	if(woEstimateId!=''){ 
		//alert("Development not yet comepleted for Create Advance requisition form for contractors. It will be re-directed to Create Advance requisition form screen once the feature is ready.");
		window.open('${pageContext.request.contextPath}/contractoradvance/contractorAdvanceRequisition!newform.action?workOrderEstimateId='+woEstimateId,'_self');
		//window.open('${pageContext.request.contextPath}/contractoradvance/contractorAdvanceRequisition!newform.action?workOrderEstimateId='+woEstimateId,'_self');
	}
	else{
		dom.get("searchEstimateForAdvance_error").style.display='';
		document.getElementById("searchEstimateForAdvance_error").innerHTML='<s:text name="contractoradvance.create.select.estimate.validate" />';
		window.scroll(0, 0);
		return false;
	  }
	  dom.get("searchEstimateForAdvance_error").style.display='none';
	  document.getElementById("searchEstimateForAdvance_error").innerHTML=''; 

	  
	}

var estimateNumberSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};

var wpNumberSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};

var tenderNegotiationNumberSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};

var workOrderNumberSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};

</script>

<body>
	<div class="errorstyle" id="searchEstimateForAdvance_error" style="display: none;"></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:form name="searchEstimateForContractorAdvanceForm" id="searchEstimateForContractorAdvanceForm" theme="simple" onsubmit="return validateAndSubmit(); ">		
		<div class="formmainbox">
			<div class="insidecontent">
				<div id="printContent" class="rbroundbox2">
					<div class="rbtop2"><div></div></div>
					<div class="rbcontent2">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr><td>&nbsp;</td></tr>
							<tr>
								<td>
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td width="100%" colspan="4" class="headingwk">
												<div class="arrowiconwk">
													<img src="${pageContext.request.contextPath}/image/arrow.gif" />
												</div>
												<div class="headplacer">
													<s:text name="search.criteria" />
												</div>
											</td>
										</tr>
										
										<tr>	
											<td class="whiteboxwk"><s:text name="contractoradvance.estimatestatus" />:</td>
											<td class="whitebox2wk">
												<s:textfield name="estimateStatus" value="%{estimateStatus}" id="estimateStatus" cssClass="selectwk" readonly="true" />
											</td>	
											<td class="whiteboxwk"><s:text name="contractoradvance.estimatenumber" />:</td>
											<td class="whitebox2wk" >
				        						<div class="yui-skin-sam">
				        							<div id="estimateNumberSearch_autocomplete">
			                							<div>
				        									<s:textfield id="estimateNumberSearch" name="estimateNumber" value="%{estimateNumber}" cssClass="selectwk" />
				        								</div>
				        								<span id="estimateNumberSearchResults"></span>
				        							</div>	
				        						</div>
				        						<egov:autocomplete name="estimateNumberSearch" width="20" field="estimateNumberSearch" url="ajaxContractorAdvance!searchEstimateNumber.action?" queryQuestionMark="false" results="estimateNumberSearchResults" handler="estimateNumberSearchSelectionHandler" queryLength="3"/>
					         				</td>										
										</tr>									
									
										<tr>
											<td class="greyboxwk"><s:text name="contractoradvance.workorder.approved.fromdate" />:</td>
											<td class="greybox2wk">											
												<s:date name="fromDate" var="fromDateFormat" format="dd/MM/yyyy" />
												<s:textfield name="fromDate" id="fromDate" cssClass="selectwk" value="%{fromDateFormat}"
													onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')" />
												<a	href="javascript:show_calendar('forms[0].fromDate',null,null,'DD/MM/YYYY');"
													onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> 
													<img src="${pageContext.request.contextPath}/image/calendar.png"
														alt="Calendar" width="16" height="16" border="0" align="absmiddle" />
												</a>
											</td>
											<td class="greyboxwk"><s:text name="contractoradvance.workorder.approved.todate" />:</td>
											<td class="greybox2wk">
												<s:date name="toDate" var="toDateFormat" format="dd/MM/yyyy" />
												<s:textfield name="toDate" id="toDate" value="%{toDateFormat}" cssClass="selectwk"
													onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')" />
												<a href="javascript:show_calendar('forms[0].toDate',null,null,'DD/MM/YYYY');"
													onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"> <img
														src="${pageContext.request.contextPath}/image/calendar.png"
														alt="Calendar" width="16" height="16" border="0" align="absmiddle" />
												</a>
											</td>
										</tr>
										<tr>
											<td class="whiteboxwk"><s:text name="contractoradvance.contractor" />:</td>
											<td class="whitebox2wk">
												<s:select id="contractorId" name="contractorId" cssClass="selectwk"
													list="%{contractorForApprovedWorkOrder}"  headerKey="-1"
													headerValue="--- Select ---" value="%{contractorId}" />
											</td>
											<td class="whiteboxwk"> <s:text name="contractoradvance.workspackagenumber" /></td>
											<td class="whitebox2wk" >
												<div class="yui-skin-sam">
				        							<div id="wpNumberSearch_autocomplete">
			                							<div>
				        									<s:textfield id="wpNumberSearch" name="wpNumber" value="%{wpNumber}" cssClass="selectwk" />
				        								</div>
				        								<span id="wpNumberSearchResults"></span>
				        							</div>	
				        						</div>
				        						<egov:autocomplete name="wpNumberSearch" width="20" field="wpNumberSearch" url="ajaxContractorAdvance!searchWPNumber.action?" queryQuestionMark="false" results="wpNumberSearchResults" handler="wpNumberSearchSelectionHandler" queryLength="3"/>
											</td>
										</tr>
										
										<tr>
											<td class="greyboxwk"><s:text name="contractoradvance.tendernegotiationnumber" />:</td>
											<td class="greybox2wk">
												<div class="yui-skin-sam">
				        							<div id="tenderNegotiationNumberSearch_autocomplete">
			                							<div>
				        									<s:textfield id="tenderNegotiationNumberSearch" name="tenderNegotiationNumber" value="%{tenderNegotiationNumber}" cssClass="selectwk" />
				        								</div>
				        								<span id="tenderNegotiationNumberSearchResults"></span>
				        							</div>	
				        						</div>
				        						<egov:autocomplete name="tenderNegotiationNumberSearch" width="20" field="tenderNegotiationNumberSearch" url="ajaxContractorAdvance!searchTNNumber.action?" queryQuestionMark="false" results="tenderNegotiationNumberSearchResults" handler="tenderNegotiationNumberSearchSelectionHandler" queryLength="3"/>
											</td>
											<td class="greyboxwk"> <s:text name="contractoradvance.executingdepartment" /></td>
											<td class="greybox2wk" >
												<s:select headerKey="-1" headerValue="%{getText('list.default.select')}" name="executingDepartmentId" 
													id="executingDepartmentId" cssClass="selectwk" list="dropdownData.executingDepartmentList" listKey="id"
													listValue='deptName' value="%{executingDepartmentId}" />
											</td>
										</tr>
									<tr>
										<td class="whiteboxwk"><s:text name="contractoradvance.workordernumber" />:</td>
										<td class="whitebox2wk" > 
			        						<div class="yui-skin-sam">
			        							<div id="workOrderNumberSearch_autocomplete">
		                							<div> 
			        									<s:textfield id="workOrderNumberSearch" name="workOrderNumber" value="%{workOrderNumber}" cssClass="selectwk" />
			        								</div>
			        								<span id="workOrderNumberSearchResults"></span>
			        							</div>		
			        						</div>
			        						<egov:autocomplete name="workOrderNumberSearch" width="20" field="workOrderNumberSearch" url="ajaxContractorAdvance!searchWorkOrderNumber.action?" queryQuestionMark="false" results="workOrderNumberSearchResults" handler="workOrderNumberSearchSelectionHandler" queryLength="3" />
				         				</td>
				         			</tr>
									<tr>
											<td colspan="4" class="shadowwk"></td>
										</tr>
										
										<tr>
											<td colspan="4">
												<div  class="buttonholderwk" align="center">
													<s:submit  cssClass="buttonadd" value="SEARCH"
														id="saveButton" name="button"  onclick="return validateAndSubmit();" method="searchList" />
													<input type="button" class="buttonfinal" value="CLOSE"
														id="closeButton" name="button" onclick="window.close();" />
										</td>
										</tr>
										<%@ include file='searchEstimateForContractorAdvance-estimateList.jsp'%>
									</table>
								<P align="center">
									<input type="button" class="buttonadd" value="Create Advance Requisition" id="addButton"
										name="createAdvanceRequisitionButton" onclick="goToCreateAdvanceRequisition();"	align="center" />
								</P>
							</td>
							</tr>
							
						</table>
						
					</div>
					<!-- end of rbroundbox2 -->
				</div>
				<!-- end of insidecontent -->
			</div>
			<!-- end of formmainbox -->
	</s:form>
</body>
</html>