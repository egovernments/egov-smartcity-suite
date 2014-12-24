<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
	<head>
	<s:if test="%{sourcePage=='searchTSheetForSampleLetter'}">
		<title><s:text name='page.title.search.testSheet' /></title>
	</s:if>
	<s:else>
		<title><s:text name='page.title.search.workorder' /></title>
	</s:else>	
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

function setworkorderId(elem){
	var currRow=getRow(elem);
	dom.get("workOrderId").value = elem.value; 
	dom.get("woNumber").value = getControlInBranch(currRow,'objNo').value; 
}

function setTestSheetId(elem){
	var currRow=getRow(elem);
	dom.get("testSheetHeaderId").value = elem.value; 
}

function validateAndSubmit(){
	   <s:if test="%{sourcePage=='searchTSheetForSampleLetter'}">
	   		if($F('contractorId') == -1 && $F('deptId') == -1 && 
					$F('fromDate').blank() && $F('toDate').blank()){
	   </s:if>
	   <s:else>
	   		if($F('status') == -1 && $F('contractorId') == -1 && $F('deptId') == -1 && 
			   		$F('workordernumber').blank() && $F('fromDate').blank() && $F('toDate').blank()){
	   </s:else>
							var bool=true;
							<s:if test="%{sourcePage=='searchTSheetForSampleLetter'}">
								if(document.testSheetSearchForm.tSheetNumber && document.testSheetSearchForm.woNumber && document.testSheetSearchForm.estimateNumber){
									if(document.testSheetSearchForm.tSheetNumber.value=="" && document.testSheetSearchForm.woNumber.value=="" && document.testSheetSearchForm.estimateNumber.value=="")
										bool=false;
								} 
							</s:if>
							<s:else>
								if(document.testSheetSearchForm.estimateNumber){
									if(document.testSheetSearchForm.estimateNumber.value=="")
										bool=false;
									}
							 </s:else>
							
		if(!bool){
		$('workOrder_error').show();					
	  	$('mandatory_error').show();
	  	$('mandatory_length_error').hide();
	  	return false;
	  	}
	  }
	  
	   <s:if test="%{sourcePage!='searchTSheetForSampleLetter'}">
		   if(!$F('workordernumber').blank()  && $F('workordernumber').length < 4){
				  	$('workOrder_error').show();
				  	$('mandatory_length_error').show();
				  	$('mandatory_error').hide();
				     return false;
			 }
	  </s:if>
		    $('workOrder_error').hide();  
		    <s:if test="%{sourcePage!='searchTSheetForSampleLetter'}">
		    	 document.testSheetSearchForm.action='${pageContext.request.contextPath}/qualityControl/testSheet!searchWorkOrderDetails.action';
			     document.testSheetSearchForm.submit();
		    </s:if>
		    <s:else>
		    	 document.testSheetSearchForm.action='${pageContext.request.contextPath}/qualityControl/testSheet!searchTestSheetDetails.action';
	    		 document.testSheetSearchForm.submit();
	    	</s:else>
	    }

function clearHiddenEstimateNumber(obj)
{
	if(obj.value=="")
	{
		document.getElementById("estimateId").value="";
	}	
}

var estimateSearchSelectionHandler = function(sType, arguments) {  
    var oData = arguments[2];
    dom.get("estimateSearch").value=oData[0];
    dom.get("estimateId").value = oData[1];
};

var estimateSelectionEnforceHandler = function(sType, arguments) {
	alert('<s:text name="estimate.search.estimateNumber.failure"/>');
};

function gotoTestSheet(){
	var id = document.testSheetSearchForm.workOrderId.value;
	if(id!='')
		window.open('${pageContext.request.contextPath}/qualityControl/testSheet!newform.action?sourcePage=createTestSheet&workOrderId='+id,'_self');
	else{
		dom.get("workOrder_error").style.display='';
		document.getElementById("workOrder_error").innerHTML='<s:text name="testSheet.workorder.not.selected" />';
		return false;
	  }
	  dom.get("workOrder_error").style.display='none';
	  document.getElementById("workOrder_error").innerHTML='';
	}


function clearHiddenTestSheetNumber(obj)
{
	if(obj.value=="")
	{
		document.getElementById("testSheetId").value="";
	}	
}

var testSheetNoSearchSelectionHandler = function(sType, arguments) {  
    var oData = arguments[2];
    dom.get("testSheetNoSearch").value=oData[0];
    dom.get("testSheetId").value = oData[1];
};

var testSheetSelectionEnforceHandler = function(sType, arguments) {
	alert('<s:text name="testSheet.search.testSheetNumber.failure"/>');
};


function clearHiddenWONumber(obj)
{
	if(obj.value=="")
	{
		document.getElementById("tsSearchWorkOrderId").value="";
	}	
}

var workOrderNoSearchSelectionHandler = function(sType, arguments) {  
    var oData = arguments[2];
    dom.get("workOrderNoSearch").value=oData[0];
    dom.get("tsSearchWorkOrderId").value = oData[1];
};

var workOrderNoSelectionEnforceHandler = function(sType, arguments) {
	alert('<s:text name="testSheet.search.workOrderNumber.failure"/>');
};

function tsSearchParameters(){
		return "type=searchWOForSampleLetter";
}

function gotoSampleLetter(){
	var id = document.testSheetSearchForm.testSheetHeaderId.value;
	if(id!='')
		window.open('${pageContext.request.contextPath}/qualityControl/sampleLetter!newform.action?sourcePage=createSampleLetter&testSheetId='+id,'_self');
	else{
		dom.get("workOrder_error").style.display='';
		document.getElementById("workOrder_error").innerHTML='<s:text name="sampleLetter.testSheet.not.selected" />';
		return false;
	  }
	  dom.get("workOrder_error").style.display='none';
	  document.getElementById("workOrder_error").innerHTML='';
	}

</script>

<body onload="init();"> 
		<div class="errorstyle" id="searchWO_error"
			style="display: none;"></div>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:form name="testSheetSearchForm" 
			id="testSheetSearchForm" theme="simple"
			onsubmit="return validateAndSubmit();">
			
			<div class="errorstyle" id="workOrder_error" style="display: none;">
			<span id="mandatory_error" style="display: none;"><s:text name="testSheet.workorder.serach.mandatory.error" /></span>
			<span id="mandatory_length_error" style="display: none;"><s:text name="testSheet.workorder.serach.mandatory.length.error" /></span>
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
								<s:hidden id="sourcePage" name="sourcePage" />
								<s:if test="%{sourcePage!='searchTSheetForSampleLetter'}">
									<s:hidden id="workOrderId" name="workOrderId" />
									<s:hidden id="woNumber" name="woNumber" />	
								</s:if>
								<s:if test="%{sourcePage=='searchTSheetForSampleLetter'}">
									<s:hidden id="testSheetHeaderId" name="testSheetHeaderId" />
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
														<s:text name="page.subheader.search.testSheet.workorder" />
													</div>
												</td>
											</tr>
											
											<s:if test="%{sourcePage!='searchTSheetForSampleLetter'}">
											<tr>
												<td class="whiteboxwk">
													<s:text name="testSheet.workorder.search.status" />:
												</td>
												<td class="whitebox2wk">
													<s:select id="status" name="status" 
															 cssClass="selectwk"
															list="%{workOrderStatuses}" value="%{status}"
															listKey="code" listValue="description"
															/>
												</td>
												<td class="whiteboxwk">
													<s:text name="testSheet.workorder.search.workordernumber" />:
												</td>
											        <td class="whitebox2wk">
													 <s:textfield name="workOrderNumber"
														value="%{workOrderNumber}" id="workordernumber"
														cssClass="selectwk" />
												</td>
											</tr>
											</s:if>
											
											<tr>
												<td class="greyboxwk">
													<s:text name="testSheet.workorder.search.workorderFromdate" />:
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
													<s:text name="testSheet.workorder.search.workorderTodate" />:
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
											
											<tr height="40">
												<td class="whiteboxwk">
													<s:text name="testSheet.workorder.search.contractor" />:
												</td>
												
													<td colspan="3" class="whitebox2wk">
														<s:select id="contractorId" name="contractorId"
															cssClass="selectwk"
															list="%{contractorForApprovedWorkOrder}"  headerKey="-1"
															headerValue="" value="%{contractorId}"/>
													</td>
												
											</tr>
											<tr>
											<td class="greyboxwk">
													<s:text name="testSheet.workOrder.executingDepartment" />
													:
												</td>
												<td class="greybox2wk">
												<s:select id="deptId" name="deptId"
														cssClass="selectwk" list="%{dropdownData.deptListForSearch}"
														listKey="id" listValue="deptName" headerKey="-1"
														headerValue="--- Select ---" />
												</td>
													<td class="greyboxwk">
														<s:text name="estimate.number" />
														:
													</td>
													<td class="greybox2wk">
														<div class="yui-skin-sam">
											                <div id="estimateSearch_autocomplete">
											                <div><s:textfield id="estimateSearch" type="text" name="estimateNumber" value="%{estimateNumber}" onBlur="clearHiddenEstimateNumber(this)" class="selectwk"/><s:hidden id="estimateId" name="estimateId" value="%{estimateId}"/></div>
											                <span id="estimateSearchResults"></span>
											                </div>
											             </div>
											             <s:if test="%{sourcePage=='searchTSheetForSampleLetter'}">
	                										<egov:autocomplete name="estimateSearch" width="20" field="estimateSearch" url="ajaxTestSheet!searchEstimateNumberAjax.action?"  queryQuestionMark="false" paramsFunction="tsSearchParameters" results="estimateSearchResults" handler="estimateSearchSelectionHandler" forceSelectionHandler="estimateSelectionEnforceHandler"/>
														 </s:if>
														 <s:else>
														 	<egov:autocomplete name="estimateSearch" width="20" field="estimateSearch" url="ajaxTestSheet!searchEstimateNumberAjax.action?"  queryQuestionMark="false" results="estimateSearchResults" handler="estimateSearchSelectionHandler" forceSelectionHandler="estimateSelectionEnforceHandler"/>
														 </s:else>
													</td>
												   </tr>  
												   
												   <s:if test="%{sourcePage=='searchTSheetForSampleLetter'}">
												<tr>
													<td class="whiteboxwk">
														<s:text name="testSheet.number" />
														:
													</td>
													<td class="whitebox2wk">
														<div class="yui-skin-sam">
											                <div id="testSheetNoSearch_autocomplete">
											                <div><s:textfield id="testSheetNoSearch" type="text" name="tSheetNumber" value="%{tSheetNumber}" onBlur="clearHiddenTestSheetNumber(this)" class="selectwk"/><s:hidden id="testSheetId" name="testSheetId" value="%{testSheetId}"/></div>
											                <span id="testSheetNoSearchResults"></span>
											                </div>
											             </div>
	                										<egov:autocomplete name="testSheetNoSearch" width="20" field="testSheetNoSearch" url="ajaxTestSheet!searchTestSheetNumberAjax.action?"  queryQuestionMark="false" paramsFunction="tsSearchParameters" results="testSheetNoSearchResults" handler="testSheetNoSearchSelectionHandler" forceSelectionHandler="testSheetSelectionEnforceHandler" queryLength="3"/>
													</td>
													
													<td class="whiteboxwk"> 
														<s:text name="testSheet.search.workordernumber" />: 
													</td>
													<td class="whitebox2wk">
															<div class="yui-skin-sam">
												                <div id="workOrderNoSearch_autocomplete">
												                <div><s:textfield id="workOrderNoSearch" type="text" name="woNumber" value="%{woNumber}" onBlur="clearHiddenWONumber(this)" class="selectwk"/><s:hidden id="tsSearchWorkOrderId" name="tsSearchWorkOrderId" value="%{tsSearchWorkOrderId}"/></div>
												                <span id="workOrderNoSearchResults"></span>
												                </div>
												             </div>
		                										<egov:autocomplete name="workOrderNoSearch" width="20" field="workOrderNoSearch" url="ajaxTestSheet!searchWONumberAjax.action?" queryQuestionMark="false" paramsFunction="tsSearchParameters" results="workOrderNoSearchResults" handler="workOrderNoSearchSelectionHandler" forceSelectionHandler="workOrderNoSelectionEnforceHandler" queryLength="3"/>
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
															id="saveButton" name="button" />
														<input type="button" class="buttonfinal" value="CLOSE"
															id="closeButton" name="button" onclick="window.close();" />
													</div>
												</td>
											</tr>
											
											

										</table>
									</td>
								</tr>
								 <s:if test="%{sourcePage=='searchTSheetForSampleLetter'}">
								 	<%@ include file='testSheetForSampleLetter-list.jsp'%>
								 </s:if>
								 <s:else>
									<%@ include file='woSearchForTestSheet-list.jsp'%>
								</s:else>
							</table>
							
							
							<s:if test="%{searchResult.fullListSize != 0}">
							<P align="center">
							   <s:if test="%{sourcePage=='searchTSheetForSampleLetter'}">
								   <input type="button" class="buttonadd"
										value="Create Sample Letter" id="addButton"
										name="createSampleLetter" onclick="gotoSampleLetter()"
										align="center" />
							   </s:if>
							   <s:else>
									<input type="button" class="buttonadd"
										value="Create Test Sheet" id="addButton"
										name="createTestSheet" onclick="gotoTestSheet()"
										align="center" />
								</s:else>	
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

