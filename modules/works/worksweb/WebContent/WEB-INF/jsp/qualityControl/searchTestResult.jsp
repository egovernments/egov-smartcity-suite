<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
	<head>
		<title><s:text name='page.title.search.testresult' /></title>
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
function gotoPage(obj)
{
	var currRow=getRow(obj);
	var testResultHeaderId = getControlInBranch(currRow,'testResultHeaderId');
	var stateId = getControlInBranch(currRow,'stateId');
	
	if(dom.get('searchActions')[1]!=null && obj.value==dom.get('searchActions')[1].value)
	{
		window.open("${pageContext.request.contextPath}/qualityControl/testResult!edit.action?headerId="+testResultHeaderId.value+
		"&sourcePage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes'); 
	}
	if(dom.get('searchActions')[2]!=null && obj.value==dom.get('searchActions')[2].value)
	{
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue="+stateId.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
}

function clearErrorMessage(){
	dom.get("searchTestResult_error").style.display='none';
	document.getElementById("searchTestResult_error").innerHTML=''; 
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
	alert('<s:text name="test.result.search.testSheetNumber.failure"/>');
};


function clearHiddenWONumber(obj)
{
	if(obj.value=="")
	{
		document.getElementById("workOrderId").value="";
	}	
}

var workOrderNoSearchSelectionHandler = function(sType, arguments) {  
    var oData = arguments[2];
    dom.get("workOrderNoSearch").value=oData[0];
    dom.get("workOrderId").value = oData[1];
};

var workOrderNoSelectionEnforceHandler = function(sType, arguments) {
	alert('<s:text name="test.result.search.workOrderNumber.failure"/>');
};

function clearHiddenSLNumber(obj)
{
	if(obj.value=="")
	{
		document.getElementById("sampleLetterId").value="";
	}	
}

var sampleLetterNoSearchSelectionHandler = function(sType, arguments) {  
    var oData = arguments[2];
    dom.get("sampleLetterNoSearch").value=oData[0];
    dom.get("sampleLetterId").value = oData[1];
};

var sampleLetterSelectionEnforceHandler = function(sType, arguments) {
	alert('<s:text name="test.result.search.sampleLetterNumber.failure"/>');
};

function clearHiddenCLNumber(obj)
{
	if(obj.value=="")
	{
		document.getElementById("sampleLetterId1").value="";
	}	
}

var coveringLetterNoSearchSelectionHandler = function(sType, arguments) {  
    var oData = arguments[2];
    dom.get("coveringLetterNoSearch").value=oData[0];
    dom.get("sampleLetterId1").value = oData[1];
};

var coveringLetterNoSelectionEnforceHandler = function(sType, arguments) {
	alert('<s:text name="test.result.search.coveringLetterNumber.failure"/>');
};

function clearHiddenJobNumber(obj)
{
	if(obj.value=="")
	{
		document.getElementById("jobNoId").value="";
	}	
}

var jobNoSearchSelectionHandler = function(sType, arguments) {  
    var oData = arguments[2];
    dom.get("jobNoSearch").value=oData[0];
    dom.get("jobNoId").value = oData[1];
};

var jobNoSelectionEnforceHandler = function(sType, arguments) {
	alert('<s:text name="test.result.search.jobnumber.failure"/>'); 
};

function validateAndSubmit(){
	   clearErrorMessage();
		   if($F('contractorId') == -1 && $F('fromDate').blank() && $F('toDate').blank()){
			   var bool=true;
					if(document.searchTestResultForm.testSheetNumber && document.searchTestResultForm.workordernumber && 
							document.searchTestResultForm.sampleLetterNumber && document.searchTestResultForm.coveringLetterNumber && 
							document.searchTestResultForm.jobNumber){
						if(document.searchTestResultForm.testSheetNumber.value=="" && document.searchTestResultForm.workordernumber.value=="" && 
								document.searchTestResultForm.sampleLetterNumber.value=="" && 
								document.searchTestResultForm.coveringLetterNumber.value=="" && document.searchTestResultForm.jobNumber.value=="")
							bool=false;
					} 
				if(!bool){
					 	dom.get("searchTestResult_error").style.display='';
						document.getElementById("searchTestResult_error").innerHTML='<s:text name="test.result.search.mandatory.error" />';
					  	return false;
				  	}	
		   }
				 	
		document.searchTestResultForm.action='${pageContext.request.contextPath}/qualityControl/searchTestResult!search.action';
		document.searchTestResultForm.submit(); 
		return true;
}


</script>

<body onload="init();"> 
		<div class="errorstyle" id="searchTR_error"
			style="display: none;"></div>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:form name="searchTestResultForm" 
			id="searchTestResultForm" theme="simple"
			onsubmit="return validateAndSubmit();">
			<div class="errorstyle" id="searchTestResult_error" style="display: none;"></div>
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
														<s:text name="page.subheader.search.jobNumber" />
													</div>
												</td>
											</tr>
											
											<tr>
												<td class="whiteboxwk">
														<s:text name="testSheet.number" />:
												</td>
												<td class="whitebox2wk">
													<div class="yui-skin-sam">
											                <div id="testSheetNoSearch_autocomplete">
												                <div><s:textfield id="testSheetNoSearch" type="text" name="testSheetNumber" value="%{testSheetNumber}" onBlur="clearHiddenTestSheetNumber(this)" class="selectwk"/><s:hidden id="testSheetId" name="testSheetId" value="%{testSheetId}"/></div>
												                <span id="testSheetNoSearchResults"></span>
											                </div>
											        </div>
													<egov:autocomplete name="testSheetNoSearch" width="20" field="testSheetNoSearch" url="ajaxJobNumber!searchTestSheetNumberAjax.action?"  queryQuestionMark="false" results="testSheetNoSearchResults" handler="testSheetNoSearchSelectionHandler" forceSelectionHandler="testSheetSelectionEnforceHandler" queryLength="3"/>
												</td>
													
												<td class="whiteboxwk">
													<s:text name="test.result.work.order.number" />: 
												</td>
												
												<td class="whitebox2wk">
														<div class="yui-skin-sam">
											                <div id="workOrderNoSearch_autocomplete">
											                <div><s:textfield id="workOrderNoSearch" type="text" name="workordernumber" value="%{workordernumber}" onBlur="clearHiddenWONumber(this)" class="selectwk"/><s:hidden id="workOrderId" name="workOrderId" value="%{workOrderId}"/></div>
											                <span id="workOrderNoSearchResults"></span>
											                </div>
											             </div>
	                										<egov:autocomplete name="workOrderNoSearch" width="20" field="workOrderNoSearch" url="ajaxJobNumber!searchWONumberAjax.action?"  queryQuestionMark="false" results="workOrderNoSearchResults" handler="workOrderNoSearchSelectionHandler" forceSelectionHandler="workOrderNoSelectionEnforceHandler" queryLength="3"/>
													</td>
											</tr>
											
											<tr>
												<td class="greyboxwk">
														<s:text name="test.result.search.fromdate" />:
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
														<s:text name="test.result.search.todate" />:
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
													<s:text name="test.result.contractor" />:
												</td>
												
												<td colspan="3" class="whitebox2wk">
													<s:select id="contractorId" name="contractorId"
														cssClass="selectwk"
														list="%{contractorForTestResult}"  headerKey="-1"
														headerValue="" value="%{contractorId}" />
												</td>
												
											</tr>
											<tr>
												<td class="greyboxwk">
													<s:text name="sampleLetter.number" />:
												</td>
												<td class="greybox2wk">
													<div class="yui-skin-sam">
											            <div id="sampleLetterNoSearch_autocomplete">
											                <div><s:textfield id="sampleLetterNoSearch" type="text" name="sampleLetterNumber" value="%{sampleLetterNumber}" onBlur="clearHiddenSLNumber(this)" class="selectwk"/><s:hidden id="sampleLetterId" name="sampleLetterId" value="%{sampleLetterId}"/></div>
											                <span id="sampleLetterNoSearchResults"></span>
											            </div>
											        </div>
											        <egov:autocomplete name="sampleLetterNoSearch" width="20" field="sampleLetterNoSearch" url="ajaxJobNumber!searchSampleLetterNumberAjax.action?"  queryQuestionMark="false" results="sampleLetterNoSearchResults" handler="sampleLetterNoSearchSelectionHandler" forceSelectionHandler="sampleLetterSelectionEnforceHandler" queryLength="3"/>
												</td>
												<td class="greyboxwk">
													<s:text name="coveringLetter.number" />: 
												</td>
												<td class="greybox2wk">
													<div class="yui-skin-sam">
											            <div id="coveringLetterNoSearch_autocomplete">
											            	<div><s:textfield id="coveringLetterNoSearch" type="text" name="coveringLetterNumber" value="%{coveringLetterNumber}" onBlur="clearHiddenCLNumber(this)" class="selectwk"/><s:hidden id="sampleLetterId1" name="sampleLetterId1" value="%{sampleLetterId1}"/></div>
											                <span id="coveringLetterNoSearchResults"></span>
											            </div>
										             </div>
                									<egov:autocomplete name="coveringLetterNoSearch" width="20" field="coveringLetterNoSearch" url="ajaxJobNumber!searchCoveringLetterNumberAjax.action?"  queryQuestionMark="false" results="coveringLetterNoSearchResults" handler="coveringLetterNoSearchSelectionHandler" forceSelectionHandler="coveringLetterNoSelectionEnforceHandler" queryLength="3"/>
												</td>
											</tr>  
											<tr>
												<td class="whiteboxwk">
														<s:text name="job.number" />:
												</td>
												<td class="whitebox2wk">
													<div class="yui-skin-sam">
										                <div id="jobNoSearch_autocomplete">
										                <div><s:textfield id="jobNoSearch" type="text" name="jobNumber" value="%{jobNumber}" onBlur="clearHiddenJobNumber(this)" class="selectwk"/><s:hidden id="jobNoId" name="jobNoId" value="%{jobNoId}"/></div>
										                <span id="jobNoSearchResults"></span>
										                </div>
										             </div>
													<egov:autocomplete name="jobNoSearch" width="20" field="jobNoSearch" url="ajaxJobNumber!searchJobNumberAjax.action?"  queryQuestionMark="false" results="jobNoSearchResults" handler="jobNoSearchSelectionHandler" forceSelectionHandler="jobNoSelectionEnforceHandler" queryLength="3"/>
												</td>
													
												<td class="whiteboxwk"></td>
												<td class="whitebox2wk"></td>
											</tr>
											<tr height="40">
												<td class="greyboxwk">
													<s:text name="test.result.search.status" />:
												</td>
												<td class="greybox2wk">
													<s:select id="statusId" name="statusId"
														cssClass="selectwk"
														list="%{testResultStatusList}"  headerKey="-1"
														headerValue="%{getText('test.result.default.select')}" value="%{statusId}" />
												</td>
												<td class="greyboxwk">&nbsp;</td>
												<td class="greybox2wk">&nbsp;</td>
											</tr>	
												
											<tr>
												<td colspan="4" class="shadowwk"></td>
											</tr>
											
											<tr>
												<td colspan="4">
													<div  class="buttonholderwk" align="center">
														<s:submit  cssClass="buttonadd" value="SEARCH"
															id="saveButton" name="button"  
															/>
														<input type="button" class="buttonfinal" value="CLOSE"
															id="closeButton" name="button" onclick="window.close();" />
													</div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
									<%@ include file='testResult-list.jsp'%>
							</table>
							
							<s:if test="%{searchResult.fullListSize != 0}">
								<P align="center">
									<input type="button" class="buttonfinal" value="CLOSE"
										id="closeButton" name="button" onclick="window.close();" />
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


