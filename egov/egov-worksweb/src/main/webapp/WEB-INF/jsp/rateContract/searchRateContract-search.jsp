<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
<head>
<title><s:text name='ratecontract.label' /></title>
</head>
<script src="<egov:url path='js/works.js'/>" type="text/javascript"></script>

<script type="text/javascript">

function rateContractNumberSearchResultsHandler()
{
	warn('improperrateContractorSelection');
}
	
function improperContractorSelectionHandler()
{
	warn('improperContractorSelection');
}

function enableFields() {
	document.getElementsByName("indentCategory")[0].disabled=false;
	<s:if test="%{isBpaCutEstimate}">
		dom.get("executingDepartmentId").disabled = false;
	</s:if>
}

function checkFund(obj)
{
	var isMcFund = document.getElementById("isMunicipalFund").value;
	var indentRadio = document.getElementsByName("indentCategory");
	if(isMcFund=="false" && indentRadio[1].checked)
	{
		dom.get("searchRateContract_error").style.display='';
		document.getElementById("searchRateContract_error").innerHTML='<s:text name="dw.repairs.deposit.code.rc.is.not.municpal.fund" />';
		document.getElementById("saveButton").disabled=true;
	}
	else
	{
		document.getElementById("saveButton").disabled=false;
	}
}

function setDefaultFund()
{
	var indentRadio = document.getElementsByName("indentCategory");
	indentRadio[0].checked=true;
}

function disableExecDept() {
	<s:if test="%{isBpaCutEstimate}">
		dom.get("executingDepartmentId").disabled = true;
	</s:if>
}
</script>

<body onload="setDefaultFund();disableExecDept();">
	<div class="errorstyle" id="searchRateContract_error"
		style="display: none;"></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
	<s:form name="searchRateContract" id="searchRateContract"
		theme="simple">
		<s:hidden name="rowId" id="rowid" />
		<s:hidden name="zoneIdsString" id="zoneIdsString" value="%{zoneIdsString}" />
		<s:hidden name="wardIdsString" id="wardIdsString" value="%{wardIdsString}" />
		<s:hidden name="isMunicipalFund" id="isMunicipalFund" value="%{isMunicipalFund}"/>
		<s:hidden name="repairAndMaintainance" id="repairAndMaintainance" value="%{repairAndMaintainance}"/>
		<s:hidden name="isEmergencyCut" id="isEmergencyCut" value="%{isEmergencyCut}"/>
		<s:hidden name="isBpaCutEstimate" id="isBpaCutEstimate" value="%{isBpaCutEstimate}"/>
		<s:hidden name="bpaAmount" id="bpaAmount" value="%{bpaAmount}"/>
		<s:hidden name="utilizedBpaAmount" id="utilizedBpaAmount" value="%{utilizedBpaAmount}"/>
		<s:hidden name="estimateValue" id="estimateValue" value="%{estimateValue}"/>
		<s:hidden name="connectionType" id="connectionType" value="%{connectionType}"/>
		<s:hidden name="estimateId" id="estimateId" value="%{estimateId}"/>
		<div class="formmainbox">
			<div class="insidecontent">
				<div id="printContent" class="rbroundbox2">
					<div class="rbtop2">
						<div></div>
					</div>
					<div class="rbcontent2">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td>
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td width="100%" colspan="4" class="headingwk">
												<div class="arrowiconwk">
													<img
														src="${pageContext.request.contextPath}/image/arrow.gif"
														alt="" />
												</div>
												<div class="headplacer">
													<s:text name="search.criteria" />
												</div>
											</td>
										</tr>
										<tr>
											<td class="whiteboxwk"></td>
											<s:if test="%{repairAndMaintainance}">
												<td class="whitebox2wk"><s:radio 
													label="Repairs And Maintenance" name="indentCategory" id="indentCategory"
													list="dropdownData.indentCategoryList"
													listValue="label" 
													listKey="value"
													onclick="return checkFund(this);"
													/>
												</td>
											</s:if>
											<s:else>
												<td class="whitebox2wk"><s:radio 
													label="Repairs And Maintenance" name="indentCategory" id="indentCategory"
													list="dropdownData.indentCategoryList"
													listValue="label" 
													listKey="value"
													disabled='true' />
												</td>
											</s:else>

											<td class="whiteboxwk"><s:text
													name="ratecontract.rcNo.label" />:</td>
											<td class="whitebox2wk">
												<div class="yui-skin-sam">
													<div id="rateContractNumberSearch_autocomplete">
														<div>
															<s:textfield id="rateContractNumberSearch"
																name="rcNumber" value="%{rcNumber}" cssClass="selectwk" />
														</div>
														<span id="rateContractNumberSearchResults"></span>
													</div>
												</div> <egov:autocomplete name="rateContractNumberSearch"
													width="20" field="rateContractNumberSearch"
													url="ajaxRateContract!searchRateContractorNumber.action?"
													queryQuestionMark="false"
													results="rateContractNumberSearchResults"
													forceSelectionHandler="rateContractNumberSearchResultsHandler"
													queryLength="3" /> <span class='warning'
												id="improperrateContractorSelectionWarning"></span>
											</td>

										</tr>

										<tr>
											<td class="greyboxwk"><s:text
													name="ratecontract.search.contractornamecode" />:</td>
											<td class="greybox2wk">
												<div class="yui-skin-sam">
													<div id="contractorNameCodeSearch_autocomplete">
														<div>
															<s:textfield id="contractorNameCodeSearch"
																name="contractorNameCode" value="%{contractorNameCode}"
																cssClass="selectwk" />
														</div>
														<span id="contractorNameCodeSearchResults"></span>
													</div>
												</div> <egov:autocomplete name="contractorNameCodeSearch"
													width="20" field="contractorNameCodeSearch"
													url="ajaxRateContract!searchContractor.action?"
													queryQuestionMark="false"
													results="contractorNameCodeSearchResults"
													forceSelectionHandler="contractorNameCodeSearchResultsHandler"
													queryLength="3" /> <span class='warning'
												id="improperContractorSelectionWarning"></span>
											</td>

											<td class="greyboxwk"><s:text
													name="ratecontract.search.executingdepartment" /></td>
											<td class="greybox2wk"><s:select headerKey="-1"
													headerValue="%{getText('list.default.select')}"
													name="executingDepartmentId" id="executingDepartmentId"
													cssClass="selectwk"
													list="dropdownData.executingDepartmentList" listKey="id"
													listValue='deptName' value="%{executingDepartmentId}" /></td>
										</tr>
										<tr>
										</tr>

										<tr>
											<td colspan="4">
												<div class="buttonholderwk" align="center">
													<s:submit cssClass="buttonadd" value="SEARCH"
														id="saveButton" name="button" method="search"
														onclick="return enableFields();" />
													<input type="button" class="buttonfinal" value="CLOSE"
														id="closeButton" name="button" onclick="window.close();" />
												</div>
											</td>
										</tr>
										<%@ include file='searchRateContract-result.jsp'%>
									</table>
								</td>
							</tr>

						</table>

					</div>
					<div class="rbbot2">
						<div></div>
					</div>
					<!-- end of rbroundbox2 -->
				</div>
				<!-- end of insidecontent -->
			</div>
			<!-- end of formmainbox -->
		</div>
	</s:form>
</body>
</html>