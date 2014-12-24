<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
<head>
<title>Vacant Position Report</title>
<script type="text/javascript">

var designationSelectionHandler = function(sType, arguments) {
	var oData = arguments[2];
	var empDetails = oData[0];
    var splitResult = empDetails.split("~");
	document.getElementById('designation').value = splitResult[0];
    document.getElementById('designationId').value = splitResult[1];
}

var designationSelectionEnforceHandler = function(sType, arguments) {
	warn('impropercodeSelection');
}
function validateForm()
{
	if (document.getElementById("department").value == 0) {
		showError("Please select Deparment");
		return false;
	}
	if (document.getElementById("primaryAssignmentDate").value == "") {
		showError("Please enter As on Date");
		return false;
	}
	if(document.getElementById('billNumberId').value !=0)
	   document.getElementById('billNumber').value = document.getElementById('billNumberId').options[document.getElementById('billNumberId').selectedIndex].text;
	return true;
	
}
function showError(msg) {
	document.getElementById("report_error").style.display = 'none';
	if (document.getElementById("fieldError") != null)
		document.getElementById("fieldError").style.display = 'none';
	dom.get("report_error").style.display = '';
	document.getElementById("report_error").innerHTML = msg;
}


function getReport(format) {

	if(!validateForm())
	{
		return false;
	}	
	
	document.getElementById("fileFormat").value=format;
	if(format != 'HTM')
	{	
		document.getElementById("searchResults").style.display	 = 'none';
	}	
	return true;
}
function populatebillno(obj)
{
	var deptid = document.getElementById("department").value;
	populatebillNumberId({departmentId :deptid});
}

</script>
</head>
<body >
	<div class="errorstyle" id="report_error" style="display: none;"></div>
	</div>
	<s:if test="%{hasErrors()}">
		<div class="errorMessage" id="fieldError">
			<s:actionerror cssClass="errorMessage" />
			<s:fielderror cssClass="errorMessage" />
		</div>
	</s:if>

	<s:if test="%{hasActionMessages()}">
		<div class="errorMessage">
			<s:actionmessage />
		</div>
	</s:if>
	<s:form theme="simple">
		<s:token />
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tbody>
				<tr>
					&nbsp;
					<td colspan="20" class="headingwk"><div class="arrowiconwk">
							<img src="../common/image/arrow.gif" />
						</div>
						<p>
						<div class="headplacer">
							<s:text name="search.vacant.pos.report" />
						</div>
						</p></td>
					<td></td>
				</tr>
			</tbody>
		</table>
		<s:hidden id="fileFormat" name="fileFormat" value="%{fileFormat}"/>	
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tbody>
			   <tr>
						<td width="11%" class=""whiteboxwk""><font color="red">*</font><s:text name="department.lbl"/></td>
						<td width="21%" class="whitebox2wk">
							<s:select id="department" name="department" headerKey="0"
								headerValue="--Choose--" cssClass="selectwk"
								list="dropdownData.departmentList" listKey="id"
								listValue="deptName" onchange="populatebillno(this);" />
							 	<egov:ajaxdropdown id="billNumberId" fields="['Text','Value']" dropdownId="billNumberId" url="report/vacantPositionReport!getBillNumberListByDepartment.action" />
				
					
						</td>
							<td class="whiteboxwk" ><s:text name="asDate.lbl"/><font color="red">*</font></td>
					  	<td class="whitebox2wk">
							<s:date name="primaryAssignmentDate" var="primaryAssignmentDateValue" format="dd/MM/yyyy"/>
							<s:textfield id="primaryAssignmentDate"  name="primaryAssignmentDate" value="%{primaryAssignmentDateValue}" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);"/>
							<a name="dateFromAnchor" id="dateFromAnchor" href="javascript:show_calendar('forms[0].primaryAssignmentDate');"	onmouseover="window.status='Date Picker';return true;" 
										onmouseout="window.status='';return true;"><img src="<%=request.getContextPath()%>/common/image/calendar.png"  border="0">
							</a>	
							
					   	</td>	
						
						
				</tr>
					
				<tr>
					    <td class="whiteboxwk"><s:text name="billNo.lbl"/></td>
					    <td>
					    <s:select id="billNumberId"  name="billNumberId" cssClass="selectwk" 
							headerValue="Choose" headerKey="0" list="dropdownData.billNoList" cssStyle="width:100%"
							listKey="id" listValue="billNumber" value="%{billNumberId}" />
							<s:hidden id="billNumber" name="billNumber" value="%{billNumber}"/>
						</td>
						
					
					   	<td class="greyboxwk"><s:text name="designation.lbl"/></td>
							 <td class="greybox2wk">
							 <div class="yui-skin-sam">
							 <div id="designation_autocomplete" class="yui-ac">
								<s:textfield id="designation"  name="designation" value="%{designation}" cssClass="selectwk"/>
							<div id="designationSearchResults"></div>
							</div>
							</div> 
							<egovtags:autocomplete name="designation" field="designation"
														url="${pageContext.request.contextPath}/report/vacantPositionReport!getDesignations.action" 
														queryQuestionMark="true" results="designationSearchResults"
														handler="designationSelectionHandler"
														forceSelectionHandler="designationSelectionEnforceHandler" /> <span
													class='warning' id="impropercodeSelectionWarning"></span>
						</td>
						<s:hidden name="designationId" id="designationId"></s:hidden>		

			</tr>
			<tr>
			<td>
			&nbsp;
			</td>
			
			</tr>
			
			</tbody>
    </table>
    
    <table align="center">
			<tr>
			<td>
			     <s:submit id="submit"  cssClass="buttonfinal" name="submit" value="Show HTML"  method="exportToRequestedFile" onclick="return getReport('HTM');"  />
			</td>
			<td>
			     	<s:submit   id="submit" cssClass="buttonfinal"  name="submit" value="Show PDF" method="exportToRequestedFile" onclick="return getReport('PDF');"/>
			</td>
			<td>
			     	<s:submit   id="submit" cssClass="buttonfinal"  name="submit" value="Show EXCEL" method="exportToRequestedFile" onclick="return getReport('XLS');"/>
			</td>
			<td>
			        <input type="button" name="closeBut" id="closeBut" value="CLOSE" onclick="window.close();" class="buttonfinal"/> 
			</td>
			</tr>
	</table>
    
   <div  id="searchResults">
        <table width="80%" align="center" border="0" cellspacing="0" cellpadding="0">
			<s:if test="%{searchResult.fullListSize != 0}">
           				<tr align="center">
					<td>
				   <display:table name="searchResult" export="false"  id="searchResultid" uid="currentRowObject" cellpadding="0" cellspacing="0"
				    requestURI="" sort="external"  class="its" style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
				
				    <display:caption class="headerbold"> Vacant Postion Report
				    <p align="left"> ${billNumber}</p>
										</display:caption>
					<display:column style="width:2%" title=" Sl No">
							<s:property value="%{#attr.currentRowObject_rowNum+ (page-1)*pageSize}"/>
					</display:column>
					<display:column   title="Designation" style="width:5%;" property = "designationName" />
					<display:column  title="ST" style="width:2%;" property = "sanctionPosts" />
					<display:column  title="W" style="width:5%" property = "workingPosts" />	
					<display:column  title="V" style="width:2%;"   property = "vacantPosts"/>
					</display:table>	
			          </td>
			    </tr>
			</s:if>
			  <s:elseif test="%{searchResult.fullListSize == 0}">
								<div>
									<table width="100%" border="0" cellpadding="0"
										cellspacing="0">
										<tr>
											<td align="center">
												<font color="red">No record Found.</font>
											</td>
										</tr>
									</table>
								</div>
		 		</s:elseif>  
			</table> 
    </div>
    
  
    	
</s:form>
</body>		
</html>