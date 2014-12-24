<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
	<head>
		<title><s:text name='page.title.search.sampleLetter' /></title>
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
	var sampleLetterId = getControlInBranch(currRow,'sampleLetterHeaderId');
	
	if(dom.get('searchActions')[1]!=null && obj.value==dom.get('searchActions')[1].value)
	{
		window.open("${pageContext.request.contextPath}/qualityControl/sampleLetter!edit.action?id="+sampleLetterId.value+
		"&sourcePage=search",'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes'); 
	}
	if(dom.get('searchActions')[2]!=null && obj.value==dom.get('searchActions')[2].value)
	{
		window.open("${pageContext.request.contextPath}/qualityControl/sampleLetter!generateSampleLetterPdf.action?slId="+sampleLetterId.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
	}
	if(dom.get('searchActions')[3]!=null && obj.value==dom.get('searchActions')[3].value)
	{
		window.open("${pageContext.request.contextPath}/qualityControl/sampleLetter!generateCoveringLetterPdf.action?slId="+sampleLetterId.value,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes'); 
	}
}

function gotoWorkOrderView(obj){
	var currRow=getRow(obj);
	var woId = getControlInBranch(currRow,'workOrderId');
	window.open("${pageContext.request.contextPath}/workorder/workOrder!edit.action?id="+woId.value+
			"&mode=search",'', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
}

function validateAndSubmit(){
	   clearErrorMessage();

	   <s:if test="%{sourcePage=='searchSLForCreateJob'}">
		   if($F('contractorId') == -1 && $F('fromDate').blank() && $F('toDate').blank()){
			   var bool=true;
					if(document.searchSampleLetterForm.testSheetNumber && document.searchSampleLetterForm.workordernumber && document.searchSampleLetterForm.sampleLetterNumber && document.searchSampleLetterForm.coveringLetterNumber){
						if(document.searchSampleLetterForm.testSheetNumber.value=="" && document.searchSampleLetterForm.workordernumber.value=="" && document.searchSampleLetterForm.sampleLetterNumber.value=="" && document.searchSampleLetterForm.coveringLetterNumber.value=="")
							bool=false;
					} 
				if(!bool){
					 	dom.get("searchSampleLetter_error").style.display='';
						document.getElementById("searchSampleLetter_error").innerHTML='<s:text name="createJob.sampleLetter.serach.mandatory.error" />';
					  	return false;
				  	}	
		   }
	   </s:if>
	   <s:else>
		   if($F('fromDate').blank()){
			    dom.get("searchSampleLetter_error").style.display='';
				document.getElementById("searchSampleLetter_error").innerHTML='<s:text name="sampleLetter.search.fromDate.null" />';
		  		return false;
		  	}
		   if($F('toDate').blank()){
			    dom.get("searchSampleLetter_error").style.display='';
				document.getElementById("searchSampleLetter_error").innerHTML='<s:text name="sampleLetter.search.toDate.null" />';
		  		return false;
		  	}
		</s:else>
				 	
		document.searchSampleLetterForm.action='${pageContext.request.contextPath}/qualityControl/searchSampleLetter!search.action';
 		document.searchSampleLetterForm.submit(); 
 		return true;
}

function clearErrorMessage(){
	dom.get("searchSampleLetter_error").style.display='none';
	document.getElementById("searchSampleLetter_error").innerHTML=''; 
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
	alert('<s:text name="sampleLetter.search.testSheetNumber.failure"/>');
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
	alert('<s:text name="sampleLetter.search.workOrderNumber.failure"/>');
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
	alert('<s:text name="sampleLetter.search.sampleLetterNumber.failure"/>');
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
	alert('<s:text name="sampleLetter.search.coveringLetterNumber.failure"/>');
};

function slSearchParameters(){
	return "type=searchSLForCreateJob";
}

function setSampleLetterHeaderId(elem){
	var currRow=getRow(elem);
	dom.get("slHeaderId").value = elem.value; 
}

function gotoCreateJob(){ 
	var id = document.searchSampleLetterForm.slHeaderId.value;
	if(id!='')
		window.open('${pageContext.request.contextPath}/qualityControl/allocateJob!newform.action?sourcePage=allocateJob&sampleLetterId='+id,'_self');
	else{
		dom.get("searchSampleLetter_error").style.display='';
		document.getElementById("searchSampleLetter_error").innerHTML='<s:text name="createJob.sampleLetter.not.selected" />';
		return false;
	  }
	  dom.get("searchSampleLetter_error").style.display='none';
	  document.getElementById("searchSampleLetter_error").innerHTML='';
}


</script>

<body onload="init();"> 
		<div class="errorstyle" id="searchSL_error"
			style="display: none;"></div>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:form name="searchSampleLetterForm" 
			id="searchSampleLetterForm" theme="simple"
			onsubmit="return validateAndSubmit();">
			
			<div class="errorstyle" id="searchSampleLetter_error" style="display: none;"></div>
			<div class="formmainbox">
				<div class="insidecontent">
					<div id="printContent" class="rbroundbox2">
						<div class="rbtop2">
							<div></div>
						</div>
						<div class="rbcontent2">
							<s:hidden id="sourcePage" name="sourcePage" />
							<s:if test="%{sourcePage=='searchSLForCreateJob'}">
									<s:hidden id="slHeaderId" name="slHeaderId" />
							</s:if>
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
														<s:text name="page.subheader.search.sampleLetter" />
													</div>
												</td>
											</tr>
											
											<tr>
												<td class="whiteboxwk">
														<s:text name="testSheet.number" />
														:
													</td>
													<td class="whitebox2wk">
														<div class="yui-skin-sam">
											                <div id="testSheetNoSearch_autocomplete">
											                <div><s:textfield id="testSheetNoSearch" type="text" name="testSheetNumber" value="%{testSheetNumber}" onBlur="clearHiddenTestSheetNumber(this)" class="selectwk"/><s:hidden id="testSheetId" name="testSheetId" value="%{testSheetId}"/></div>
											                <span id="testSheetNoSearchResults"></span>
											                </div>
											             </div>
											             <s:if test="%{sourcePage=='searchSLForCreateJob'}">
	                										<egov:autocomplete name="testSheetNoSearch" width="20" field="testSheetNoSearch" url="ajaxSampleLetter!searchTestSheetNumberAjax.action?"  queryQuestionMark="false" paramsFunction="slSearchParameters" results="testSheetNoSearchResults" handler="testSheetNoSearchSelectionHandler" forceSelectionHandler="testSheetSelectionEnforceHandler" queryLength="3"/>
														</s:if>
														<s:else>
															<egov:autocomplete name="testSheetNoSearch" width="20" field="testSheetNoSearch" url="ajaxSampleLetter!searchTestSheetNumberAjax.action?"  queryQuestionMark="false" results="testSheetNoSearchResults" handler="testSheetNoSearchSelectionHandler" forceSelectionHandler="testSheetSelectionEnforceHandler" queryLength="3"/>
														</s:else>
													</td>
													
												<td class="whiteboxwk">
													<s:text name="sampleLetter.search.workordernumber" />: 
												</td>
												
												<td class="whitebox2wk">
														<div class="yui-skin-sam">
											                <div id="workOrderNoSearch_autocomplete">
											                <div><s:textfield id="workOrderNoSearch" type="text" name="workordernumber" value="%{workordernumber}" onBlur="clearHiddenWONumber(this)" class="selectwk"/><s:hidden id="workOrderId" name="workOrderId" value="%{workOrderId}"/></div>
											                <span id="workOrderNoSearchResults"></span>
											                </div>
											             </div>
											              <s:if test="%{sourcePage=='searchSLForCreateJob'}">
											              	<egov:autocomplete name="workOrderNoSearch" width="20" field="workOrderNoSearch" url="ajaxSampleLetter!searchWONumberAjax.action?"  queryQuestionMark="false" paramsFunction="slSearchParameters" results="workOrderNoSearchResults" handler="workOrderNoSearchSelectionHandler" forceSelectionHandler="workOrderNoSelectionEnforceHandler" queryLength="3"/>
											              </s:if>
											              <s:else>
	                										<egov:autocomplete name="workOrderNoSearch" width="20" field="workOrderNoSearch" url="ajaxSampleLetter!searchWONumberAjax.action?"  queryQuestionMark="false" results="workOrderNoSearchResults" handler="workOrderNoSearchSelectionHandler" forceSelectionHandler="workOrderNoSelectionEnforceHandler" queryLength="3"/>
														  </s:else>
													</td>
											</tr>
											
											<tr>
												<td class="greyboxwk">
													<s:if test="%{sourcePage=='searchSLForCreateJob'}">
														<s:text name="createJob.search.slFromDate" />:
													</s:if>
													<s:else>
															<span class="mandatory">*</span><s:text name="sampleLetter.search.fromdate" />:
													</s:else>
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
													<s:if test="%{sourcePage=='searchSLForCreateJob'}">
														<s:text name="createJob.search.slToDate" />:
													</s:if>
													<s:else>
															<span class="mandatory">*</span><s:text name="sampleLetter.search.todate" />:
													</s:else>
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
													<s:text name="sampleLetter.search.contractor" />:
												</td>
												
													<td colspan="3" class="whitebox2wk">
														<s:select id="contractorId" name="contractorId"
															cssClass="selectwk"
															list="%{contractorForSampleLetter}"  headerKey="-1"
															headerValue="" value="%{contractorId}" />
													</td>
												
											</tr>
											<tr>
												<td class="greyboxwk">
														<s:text name="sampleLetter.number" />
														:
													</td>
													<td class="greybox2wk">
														<div class="yui-skin-sam">
											                <div id="sampleLetterNoSearch_autocomplete">
											                <div><s:textfield id="sampleLetterNoSearch" type="text" name="sampleLetterNumber" value="%{sampleLetterNumber}" onBlur="clearHiddenSLNumber(this)" class="selectwk"/><s:hidden id="sampleLetterId" name="sampleLetterId" value="%{sampleLetterId}"/></div>
											                <span id="sampleLetterNoSearchResults"></span>
											                </div>
											             </div>
											              <s:if test="%{sourcePage=='searchSLForCreateJob'}">
											              	<egov:autocomplete name="sampleLetterNoSearch" width="20" field="sampleLetterNoSearch" url="ajaxSampleLetter!searchSampleLetterNumberAjax.action?"  queryQuestionMark="false"  paramsFunction="slSearchParameters" results="sampleLetterNoSearchResults" handler="sampleLetterNoSearchSelectionHandler" forceSelectionHandler="sampleLetterSelectionEnforceHandler" queryLength="3"/>
											              </s:if>
											              <s:else>
	                										<egov:autocomplete name="sampleLetterNoSearch" width="20" field="sampleLetterNoSearch" url="ajaxSampleLetter!searchSampleLetterNumberAjax.action?"  queryQuestionMark="false" results="sampleLetterNoSearchResults" handler="sampleLetterNoSearchSelectionHandler" forceSelectionHandler="sampleLetterSelectionEnforceHandler" queryLength="3"/>
														   </s:else>
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
											              <s:if test="%{sourcePage=='searchSLForCreateJob'}">
											              	<egov:autocomplete name="coveringLetterNoSearch" width="20" field="coveringLetterNoSearch" url="ajaxSampleLetter!searchCoveringLetterNumberAjax.action?"  queryQuestionMark="false" paramsFunction="slSearchParameters" results="coveringLetterNoSearchResults" handler="coveringLetterNoSearchSelectionHandler" forceSelectionHandler="coveringLetterNoSelectionEnforceHandler" queryLength="3"/>
											              </s:if>
											              <s:else>
	                										<egov:autocomplete name="coveringLetterNoSearch" width="20" field="coveringLetterNoSearch" url="ajaxSampleLetter!searchCoveringLetterNumberAjax.action?"  queryQuestionMark="false" results="coveringLetterNoSearchResults" handler="coveringLetterNoSearchSelectionHandler" forceSelectionHandler="coveringLetterNoSelectionEnforceHandler" queryLength="3"/>
	                									  </s:else>	
													</td>
												
											</tr>  
											<s:if test="%{sourcePage!='searchSLForCreateJob'}">
											<tr>
												<td class="whiteboxwk">
													<s:text name="sampleLetter.executingDepartment" />
													:
												</td>
												<td class="whitebox2wk">
												<s:select id="deptId" name="deptId"
														cssClass="selectwk" list="%{dropdownData.deptListForSearch}"
														listKey="id" listValue="deptName" headerKey="-1"
														headerValue="--- Select ---" />
												</td>
													<td class="whiteboxwk"></td>
													<td class="whitebox2wk"></td>
											</tr>
											</s:if>  
												    
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
														
														<s:if test="%{sourcePage=='searchSLForCreateJob'}">
															<input type="button" class="buttonfinal" value="RESET" id="button" name="button" onclick="window.open('${pageContext.request.contextPath}/qualityControl/searchSampleLetter.action?sourcePage=searchSLForCreateJob','_self');" /> 
														</s:if>
													</div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<s:if test="%{sourcePage=='searchSLForCreateJob'}">
								 	<%@ include file='sampleLetterForCreateJob-list.jsp'%>
								 </s:if>
								 <s:else>
									<%@ include file='sampleLetter-list.jsp'%>
								</s:else>
							</table>
							
							<s:if test="%{searchResult.fullListSize != 0}">
							<P align="center">
							   <s:if test="%{sourcePage=='searchSLForCreateJob'}">
								   <input type="button" class="buttonadd"
										value="Allocate Job Number" id="addButton"
										name="createJob" onclick="gotoCreateJob()"
										align="center" />
							   </s:if>
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

