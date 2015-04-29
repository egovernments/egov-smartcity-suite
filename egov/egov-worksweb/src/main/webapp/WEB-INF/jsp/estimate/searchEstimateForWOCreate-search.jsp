<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*" %>
<script src="<egov:url path='js/works.js'/>"></script>
<script src="<egov:url path='js/helper.js'/>"></script>

<html>
	<head>
			<title><s:text name="searchEstimate.createWO.header" /></title>
	</head>
	<script type="text/javascript">
		var rcNumberSearchSelectionHandler = function(sType, arguments){ 
			var oData = arguments[2];
		}
		var estimateNumberSearchSelectionHandler = function(sType, arguments){ 
			var oData = arguments[2];
		}
	</script>

	<body >
		<div class="errorstyle" id="searchEstimate_error"
			style="display: none;"></div>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:form name="searchEstForWOCreation" action="searchEstimateForWOCreate!list.action" id="searchEstForWOCreationForm" theme="simple">
			 
			<div class="formmainbox">
				<div class="insidecontent">
					<div id="printContent" class="rbroundbox2">
						<div class="rbtop2">
							<div></div>
						</div>
						<div class="rbcontent2">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img src="${pageContext.request.contextPath}/image/arrow.gif" />
									</div>
									<div class="headplacer">
										<s:text name="searchEst.criteria" />
									</div>
								</td>
							</tr>
							<tr>
								<td class="whiteboxwk">&nbsp;</td>
								<td class="whitebox2wk" colspan="3" ><s:radio name="indentCategory" id="indentCategory" list="%{indentCategoryMap}" /></td>
							</tr>
							<tr>
								<td class="greyboxwk"><s:text name="searchEstimate.createWO.rc" />:</td>
								<td class="greybox2wk">
									<div class="yui-skin-sam">
	        							<div id="rcNumberSearch_autocomplete">
	               							<div> 
	        									<s:textfield id="rcNumberSearch" name="rcNumber" value="%{rcNumber}" cssClass="selectwk" />
	        								</div>
	        								<span id="rcNumberSearchResults"></span>
	        							</div>		
	        						</div>
	        						<egov:autocomplete name="rcNumberSearch" width="15" field="rcNumberSearch" 
	        							url="../estimate/ajaxSearchRCEstimate!getRCNos.action?" queryQuestionMark="false" 
	        							results="rcNumberSearchResults"  handler="rcNumberSearchSelectionHandler" queryLength="3" />
								        							
								</td>
								<td class="greyboxwk"><s:text name="searchEstimate.createWO.estNo" />:</td>
								<td class="greybox2wk">
									<div class="yui-skin-sam">
	        							<div id="estimateNumberSearch_autocomplete">
	               							<div> 
	        									<s:textfield id="estimateNumberSearch" name="estNo" value="%{estNo}" cssClass="selectwk" />
	        								</div>
	        								<span id="estimateNumberSearchResults"></span>
	        							</div>		
	        						</div>
	        						<egov:autocomplete name="estimateNumberSearch" width="15" field="estimateNumberSearch" 
	        							url="../estimate/ajaxSearchRCEstimate!getEstNos.action?" queryQuestionMark="false" 
	        							results="estimateNumberSearchResults"  handler="estimateNumberSearchSelectionHandler" queryLength="3" />
								</td>
							</tr>
							<tr>
								<td class="whiteboxwk"><s:text name="searchEstimate.createWO.as.fromDate" />:</td>
								<td class="whitebox2wk">
									<s:date name="adminSancFromDate" var="adminSancFromDateFormat" format="dd/MM/yyyy" />
										<s:textfield name="adminSancFromDate" id="adminSancFromDate"
											cssClass="selectwk" value="%{adminSancFromDateFormat}"
											onfocus="javascript:vDateType='3';"
											onkeyup="DateFormat(this,this.value,event,false,'3')" />
										<a
											href="javascript:show_calendar('forms[0].adminSancFromDate',null,null,'DD/MM/YYYY');"
											onmouseover="window.status='Date Picker';return true;"
											onmouseout="window.status='';return true;"> <img
												src="${pageContext.request.contextPath}/image/calendar.png"
												alt="Calendar" width="16" height="16" border="0"
												align="absmiddle" />
										</a>
								</td>
								<td class="whiteboxwk"><s:text name="searchEstimate.createWO.as.toDate" />:</td>
								<td class="whitebox2wk">
									<s:date name="adminSancToDate" var="adminSancToDateFormat" format="dd/MM/yyyy" />
										<s:textfield name="adminSancToDate" id="adminSancToDate"
											cssClass="selectwk" value="%{adminSancToDateFormat}"
											onfocus="javascript:vDateType='3';"
											onkeyup="DateFormat(this,this.value,event,false,'3')" />
										<a
											href="javascript:show_calendar('forms[0].adminSancToDate',null,null,'DD/MM/YYYY');"
											onmouseover="window.status='Date Picker';return true;"
											onmouseout="window.status='';return true;"> <img
												src="${pageContext.request.contextPath}/image/calendar.png"
												alt="Calendar" width="16" height="16" border="0"
												align="absmiddle" />
										</a>
								</td>
							</tr>
							<tr>
								<td class="greyboxwk"><s:text name="searchEstimate.createWO.contractor" />:</td>
								<td class="greybox2wk">
									<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}"
										name="contractorId" id="contractorId"
										cssClass="selectwk" list="dropdownData.contratorList"
										listKey="id" listValue="name"
										value="%{contractorId}" />
								</td>
								<td class="greyboxwk"><s:text name="searchEstimate.createWO.execDept" />:</td>
								<td class="greybox2wk">
									<s:select headerKey="-1" headerValue="%{getText('default.dropdown.select')}"
										name="execDeptId" id="execDeptId"
										cssClass="selectwk" list="dropdownData.execDeptList"
										listKey="id" listValue="deptName"
										value="%{execDeptId}"  />
								</td>
							</tr>
							<tr>
								<td colspan="4" class="shadowwk"></td>
							</tr>
							<tr>
								<td colspan="4">
									<div  class="buttonholderwk" align="center">
										<s:submit cssClass="buttonadd" value="SEARCH" id="saveButton" name="button" method="list"/>
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="4" class="shadowwk"></td>
							</tr>
							<tr>
								<td colspan="4">
									<jsp:include page="searchEstimateForWOCreate-list.jsp" />
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
				<!-- end of formmainbox -->
			</div>	
		</s:form>
	</body>
</html>
