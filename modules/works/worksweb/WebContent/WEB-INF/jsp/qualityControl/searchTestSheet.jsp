<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
	<head>
		<title><s:text name='page.title.search.testSheet' /></title>
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
	var testSheetHeaderId = getControlInBranch(currRow,'testSheetHeaderId');
	var testSheetStateId = getControlInBranch(currRow,'testSheetStateId');
	
	if(dom.get('searchActions')[1]!=null && obj.value==dom.get('searchActions')[1].value)
	{
		window.open("${pageContext.request.contextPath}/qualityControl/testSheet!edit.action?id="+testSheetHeaderId.value+
		"&sourcePage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(dom.get('searchActions')[2]!=null && obj.value==dom.get("searchActions")[2].value)
	{
		window.open("${pageContext.request.contextPath}/estimate/abstractEstimate!workflowHistory.action?stateValue="+
				testSheetStateId.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes'); 
	}
}


function validateAndSubmit(){
	   if($F('status') == -1 && $F('contractorId') == -1 
			   				&& $F('fromDate').blank()
							&& $F('toDate').blank()){
							var bool=true;
							if(document.searchTestSheetForm.testSheetNumber && document.searchTestSheetForm.workordernumber){
								if(document.searchTestSheetForm.testSheetNumber.value=="" && document.searchTestSheetForm.workordernumber.value=="")
									bool=false;
							}
		if(!bool){
			$('searchTestSheet_error').show();
	  		$('mandatory_error').show();
	  		return false;
	  	}
	  }
		    $('searchTestSheet_error').hide(); 
		    document.searchTestSheetForm.action='${pageContext.request.contextPath}/qualityControl/searchTestSheet!search.action';
	    	document.searchTestSheetForm.submit();
	    	return true;
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
		document.getElementById("workOrderId").value="";
	}	
}

var workOrderNoSearchSelectionHandler = function(sType, arguments) {  
    var oData = arguments[2];
    dom.get("workOrderNoSearch").value=oData[0];
    dom.get("workOrderId").value = oData[1];
};

var workOrderNoSelectionEnforceHandler = function(sType, arguments) {
	alert('<s:text name="testSheet.search.workOrderNumber.failure"/>');
};


</script>

<body onload="init();"> 
		<div class="errorstyle" id="searchTS_error"
			style="display: none;"></div>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:form name="searchTestSheetForm" 
			id="searchTestSheetForm" theme="simple"
			onsubmit="return validateAndSubmit();">
			
			<div class="errorstyle" id="searchTestSheet_error" style="display: none;">
			<span id="mandatory_error" style="display: none;"><s:text name="testSheet.search.mandatory.error" /></span>
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
														<s:text name="page.subheader.search.testSheet" />
													</div>
												</td>
											</tr>
											
											<tr>
												<td class="whiteboxwk">
													<s:text name="testSheet.search.status" />:
												</td>
												<td class="whitebox2wk">
													<s:select id="status" name="status" cssClass="selectwk" 
															list="%{testSheetStatuses}" value="%{status}" headerKey="-1"
															headerValue="%{getText('testSheet.default.select')}"
															listKey="code" listValue="description"
															/>
												</td>
												<td class="whiteboxwk"></td>
												<td class="whitebox2wk"></td>
											</tr>
											<tr>
												<td class="greyboxwk">
													<s:text name="testSheet.search.testSheetFromdate" />:
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
													<s:text name="testSheet.search.testSheetTodate" />:
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
													<s:text name="testSheet.search.contractor" />:
												</td>
												
													<td colspan="3" class="whitebox2wk">
														<s:select id="contractorId" name="contractorId"
															cssClass="selectwk"
															list="%{contractorForTestSheet}"  headerKey="-1"
															headerValue="" value="%{contractorId}" />
													</td>
												
											</tr>
											<tr>
													<td class="greyboxwk">
														<s:text name="testSheet.number" />
														:
													</td>
													<td class="greybox2wk">
														<div class="yui-skin-sam">
											                <div id="testSheetNoSearch_autocomplete">
											                <div><s:textfield id="testSheetNoSearch" type="text" name="testSheetNumber" value="%{testSheetNumber}" onBlur="clearHiddenTestSheetNumber(this)" class="selectwk"/><s:hidden id="testSheetId" name="testSheetId" value="%{testSheetId}"/></div>
											                <span id="testSheetNoSearchResults"></span>
											                </div>
											             </div>
	                										<egov:autocomplete name="testSheetNoSearch" width="20" field="testSheetNoSearch" url="ajaxTestSheet!searchTestSheetNumberAjax.action?"  queryQuestionMark="false" results="testSheetNoSearchResults" handler="testSheetNoSearchSelectionHandler" forceSelectionHandler="testSheetSelectionEnforceHandler"/>
													</td>
													
												<td class="greyboxwk">
													<s:text name="testSheet.search.workordernumber" />: 
												</td>
												
												<td class="greybox2wk">
														<div class="yui-skin-sam">
											                <div id="workOrderNoSearch_autocomplete">
											                <div><s:textfield id="workOrderNoSearch" type="text" name="workordernumber" value="%{workordernumber}" onBlur="clearHiddenWONumber(this)" class="selectwk"/><s:hidden id="workOrderId" name="workOrderId" value="%{workOrderId}"/></div>
											                <span id="workOrderNoSearchResults"></span>
											                </div>
											             </div>
	                										<egov:autocomplete name="workOrderNoSearch" width="20" field="workOrderNoSearch" url="ajaxTestSheet!searchWONumberAjax.action?"  queryQuestionMark="false" results="workOrderNoSearchResults" handler="workOrderNoSearchSelectionHandler" forceSelectionHandler="workOrderNoSelectionEnforceHandler"/>
													</td>
													
													
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
								
								<%@ include file='testSheet-list.jsp'%>
								
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

