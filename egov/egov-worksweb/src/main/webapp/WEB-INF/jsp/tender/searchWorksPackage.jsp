<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
	<head>
		<title><s:text name="search.wp" />
		</title>
	</head>
	<body>
		<script src="<egov:url path='js/works.js'/>"></script>
		<script type="text/javascript">
function validateSearch()
{
	var isRetenderingChecked = dom.get("checkRetenderedWP");
	if(isRetenderingChecked)
		isRetenderingChecked = isRetenderingChecked.checked;
	if(dom.get('status').value==-1 && isRetenderingChecked==false &&
	dom.get('wpNumberSearch').value=="" && dom.get('department').value==-1 && 
	dom.get('fromDate').value=="" &&
	dom.get('toDate').value=="" && dom.get('tenderFileNumber').value=="" && dom.get('estimateNumberSearch').value=="")
	{
		dom.get("searchwp_error").innerHTML='Please Select any one of the Search Parameters'; 
        dom.get("searchwp_error").style.display='';
		return false;
	 }

	<s:if test="%{source=='createNegotiationForWP'}">
		dom.get('department').disabled=false;
	</s:if>
	 
	 dom.get("searchwp_error").style.display='none';
	 dom.get("searchwp_error").innerHTML='';
	return true;
}	

function createNegotiation(){
	document.getElementById('status').disabled=false;
	var id = document.workspackageForm.workPackageId.value;
	if(id==null || id==''){
		showMessage('searchwp_error','<s:text name="search.wp.negotiation.create"/>');
		disableSelect();
	}
	else{
	
		clearMessage('searchwp_error')
		window.open('${pageContext.request.contextPath}/tender/tenderNegotiation!newform.action?tenderSource=package&sourcepage=createNegotiationForWP&worksPackageId='+id,'_self');
	}
}

function disableSelect(){
	document.getElementById('status').disabled=true
}
function disableDept(){
	document.getElementById('status').disabled=true
	dom.get('department').disabled=true
}
var wpNumberSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};

var tenderFileNumberSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};
var estimateNumberSearchSelectionHandler = function(sType, arguments){ 
	var oData = arguments[2];
};
function wpParamsFunction()
{
	return "mode=cancelWP";
}
function retenderonclick(obj)
{
	if(obj.checked)
	{
		document.getElementById("fromdatetd").innerHTML='<s:text name="wp.retender.fromdate" />:';
		document.getElementById("todatetd").innerHTML='<s:text name="wp.retender.todate" />:';
		document.getElementById("status").style.display="none";
		document.getElementById("offlinestatus").style.display="";
	}
	else
	{
		document.getElementById("fromdatetd").innerHTML='<s:text name="wp.fromdate" />:';
		document.getElementById("todatetd").innerHTML='<s:text name="wp.todate" />:';
		document.getElementById("status").style.display="";
		document.getElementById("offlinestatus").style.display="none";
	}			
}
</script>
		<div class="errorstyle" id="searchwp_error" style="display: none;"></div>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />
				<s:fielderror />
			</div>
		</s:if>
		<s:form name="workspackageForm"
			action="searchWorksPackage!search.action" theme="simple">
			<div class="formmainbox"></div>
			<div class="insidecontent">
				<div class="rbroundbox2">
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
								test="%{estimateOrWpSearchReq!=null && estimateOrWpSearchReq=='both'}">
								<tr>
									<td>
										<%@ include file='estimateOrWpSearch.jsp'%>
									</td>
									<script>
 				document.forms[0].tenderForWp.checked=true;
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
													<s:text name="search.wp" />
												</div>
											</td>
										</tr>
										<tr>
											<td width="11%" class="whiteboxwk">
												<s:text name="wp.status" />
												:
											</td>
											
												<td width="21%" class="whitebox2wk">
													<s:if test="%{source=='cancelWP'}">
														<s:select id="status" name="status" headerKey="-1"
														cssClass="selectwk"
														list="%{cancelPackageStatuses}" listKey="code"
														listValue="description" />
													</s:if>
													<s:else>
														<s:if test="%{checkRetenderedWP}">
															<s:select id="status" name="status" headerKey="-1" style="display:none"
																headerValue="ALL" cssClass="selectwk"
																list="%{packageStatuses}" listKey="code"
																listValue="description" />
															<s:select id="offlinestatus" name="offlinestatus" headerKey="-1" 
																headerValue="ALL" cssClass="selectwk"
																list="%{allOfflineStatus}" listKey="code"
																listValue="description" />	
														</s:if>
														<s:else>
															<s:select id="status" name="status" headerKey="-1" 
																headerValue="ALL" cssClass="selectwk"
																list="%{packageStatuses}" listKey="code"
																listValue="description" />
															<s:select id="offlinestatus" name="offlinestatus" headerKey="-1" style="display:none"
																headerValue="ALL" cssClass="selectwk"
																list="%{allOfflineStatus}" listKey="code"
																listValue="description" />	
														</s:else>	
													</s:else>
												</td>
											<s:if test="%{source=='createNegotiationForWP'}">
												<script>disableSelect();</script>
											</s:if>
											<s:if test="%{source=='cancelWP'}">
											<script type="text/javascript">
													window.history.forward(1);
											</script>
											</s:if>
											<td width="11%" class="whiteboxwk">
												<s:text name="wp.no" />
												:
											</td>
											<td width="21%" class="whitebox2wk">
												<div class="yui-skin-sam">
       												<div id="wpNumberSearch_autocomplete">
              											<div>
       															<s:textfield id="wpNumberSearch" name="model.wpNumber" value="%{model.wpNumber}" cssClass="selectwk" />
       													</div>
       													<span id="wpNumberSearchResults"></span>
       												</div>	
       											</div>
       											<s:if test="%{source=='cancelWP'}">
       												<egov:autocomplete name="wpNumberSearch" width="20" field="wpNumberSearch" url="ajaxWorksPackage!searchWorksPackageNumber.action?" queryQuestionMark="false" results="wpNumberSearchResults" paramsFunction="wpParamsFunction" handler="wpNumberSearchSelectionHandler" queryLength="3"/>
       											</s:if>
       											<s:else>
       												<egov:autocomplete name="wpNumberSearch" width="20" field="wpNumberSearch" url="ajaxWorksPackage!searchWorksPackageNumber.action?" queryQuestionMark="false" results="wpNumberSearchResults" handler="wpNumberSearchSelectionHandler" queryLength="3"/>
       											</s:else>	
											</td>
										</tr>
										<tr>
											<td width="11%" class="greyboxwk" id="fromdatetd">
												<s:if test="%{checkRetenderedWP}">
													<s:text name="wp.retender.fromdate" />
												</s:if>
												<s:else>
													<s:text name="wp.fromdate" />
												</s:else>	
												:
											</td>
											<td width="21%" class="greybox2wk">
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
											<td width="15%" class="greyboxwk" id="todatetd">
												<s:if test="%{checkRetenderedWP}">
													<s:text name="wp.retender.todate" />
												</s:if>
												<s:else>
													<s:text name="wp.todate" />
												</s:else>											
												:
											</td>
											<s:date name="toDate" var="toDateFormat" format="dd/MM/yyyy" />
											<td width="53%" class="greybox2wk">
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
											<td width="11%" class="whiteboxwk">
												<s:text name="estimate.number" />:
											</td>
											<td width="21%" class="whitebox2wk">
					       						<div class="yui-skin-sam">
					       							<div id="estimateNumberSearch_autocomplete">
					              							<div>
					       									<s:textfield id="estimateNumberSearch" name="estimateNumber" value="%{estimateNumber}" cssClass="selectwk" />
					       								</div>
					       								<span id="estimateNumberSearchResults"></span>
					       							</div>	
					       						</div>
					       						<egov:autocomplete name="estimateNumberSearch" width="20" field="estimateNumberSearch" url="ajaxWorksPackage!searchEstimateNumber.action?" queryQuestionMark="false" results="estimateNumberSearchResults" handler="estimateNumberSearchSelectionHandler" queryLength="3"/>
											</td>
										</tr>
										<tr>
											<s:if test="%{source=='createNegotiationForWP'}">
												<td width="11%" class="whiteboxwk">
													<s:text name="wp.dept" />
													:
												</td>
												<td width="21%" class="whitebox2wk">
													<s:select id="department" name="userDepartment"
														cssClass="selectwk"
														list="%{dropdownData.userDepartmentList}" listKey="id"
														listValue="deptName" value="%{execDept}" />
												</td>
												<s:if test="%{negoCreatedBy=='no' && dropdownData.userDepartmentList.size==1}">
													<script>
											    		disableDept(); 
											    	</script>
												</s:if>
											</s:if>
											<s:else>
												<td width="11%" class="whiteboxwk">
													<s:text name="wp.dept" />
													:
												</td>
												<td width="21%" class="whitebox2wk">
													<s:select id="department" name="userDepartment"
														headerKey="-1" headerValue="---select---"
														cssClass="selectwk"
														list="%{dropdownData.userDepartmentList}" listKey="id"
														listValue="deptName" value="%{userDepartment.id}" />
												</td>
											</s:else>
											<td width="15%" class="whiteboxwk">
												<s:text name="wp.tenderfilenumber" />
												:
											</td>
											<td width="53%" class="whitebox2wk">
												<div class="yui-skin-sam">
       												<div id="tenderFileNumber_autocomplete">
              											<div>
       															<s:textfield id="tenderFileNumber" name="model.tenderFileNumber" value="%{model.tenderFileNumber}" cssClass="selectwk" />
       													</div>
       													<span id="tenderFileNumberResults"></span>
       												</div>	
       											</div>
       											<egov:autocomplete name="tenderFileNumber" width="20" field="tenderFileNumber" url="ajaxWorksPackage!searchTenderFileNumber.action?" queryQuestionMark="false" results="tenderFileNumberResults" handler="tenderFileNumberSelectionHandler" queryLength="3"/>
											</td>
										</tr>
										<s:if test ="%{setStatus=='create' || setStatus=='view'}" >
											<tr>
												<td colspan="1" class="greyboxwk"><s:text name ="wp.retendered.wp" /></td>
												<td colspan="3" class="greybox2wk">
													<s:checkbox name="checkRetenderedWP" id="checkRetenderedWP"  value="%{checkRetenderedWP}" onclick="retenderonclick(this);" />
												</td>
											</tr>
										</s:if>
										<tr>
											<td colspan="4" class="shadowwk"></td>
										</tr>
										<tr>
											<td colspan="4">
												<div class="buttonholdersearch" align="center">
													<s:submit value="Save" cssClass="buttonadd" value="SEARCH"
														id="saveButton" name="button"
														onClick="return validateSearch();" />
												</div>
											</td>
										</tr>

										<s:hidden id="source" name="source" />
										<s:hidden id="workPackageId" name="workPackageId" />
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<s:if test="%{source=='createNegotiationForWP'}">
												<tr>
													<td><%@ include file='wpNewSearch-list.jsp'%></td>
												</tr>
												<tr>
													<td>
														<div class="buttonholderwk">
															<input type="button" class="buttonadd"
																value="Create Negotiation Statement " id="addButton"
																name="createNegotiationButton"
																onclick="createNegotiation();" align="center" />
														</div>
													</td>
												</tr>
											</s:if>
											<s:else>
											<tr>
													<td><%@ include file='wpSearch-list.jsp'%></td>
											</tr>
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
			<!-- end of formmainbox -->
		</s:form>
		<script>
		</script>
	</body>
</html>