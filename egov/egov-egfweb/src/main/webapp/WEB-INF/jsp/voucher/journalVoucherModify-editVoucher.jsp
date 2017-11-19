<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>


<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/tags/cdn.tld" prefix="cdn" %>
<html>

<head>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/calendar.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/dateValidation.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="<cdn:url value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"> </script>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252" />
<title>Journal voucher Modify</title>
</head>

<body
	onload="loadDropDownCodes();loadDropDownCodesFunction();onLoadTask()">

	<s:form theme="simple" name="jvmodifyform">
		<s:push value="model">
			<div id="loading"
				style="position: absolute; left: 25%; top: 70%; padding: 2px; z-index: 20001; height: auto; width: 500px; display: none;">
				<div class="loading-indicator"
					style="background: white; color: #444; font: bold 13px tohoma, arial, helvetica; padding: 10px; margin: 0; height: auto;">
					<img src="/egi/resources/erp2/images/loading.gif" width="32"
						height="32" style="margin-right: 8px; vertical-align: top;" />
					Loading...
				</div>
			</div>
			<jsp:include page="../budget/budgetHeader.jsp">
				<jsp:param name="heading" value="Journal voucher -Modify" />
			</jsp:include>

			<span class="mandatory1"> <font
				style='color: red; font-weight: bold'> <s:actionerror /> <s:fielderror />
					<s:actionmessage /></font>
			</span>
			<div class="formmainbox">
				<div class="subheadnew">Journal Voucher</div>
				<div id="listid" style="display: block">
					<br />
					<div align="center">
						<font style='color: red; font-weight: bold'>
							<p class="error-block" id="lblError"></p>
						</font> <input type="hidden" name="selectedDate" id="selectedDate" />

						<table border="0" width="100%">
							<tr>
								<td class="greybox">&nbsp;</td>
								<s:if test="%{shouldShowHeaderField('vouchernumber')}">
									<td class="greybox"><s:text name="voucher.number" /><span
										class="mandatory1"> *</span></td>
									<td class="greybox"><input type="text"
										name="voucherNumberPrefix" id="voucherNumberPrefix"
										readonly="true" style="width: 100%" /></td>
									<td class="greybox"><s:textfield name="voucherNumber"
											id="voucherNumber" /></td>
								</s:if>
								<s:else>
									<td class="greybox"><s:text name="voucher.number" /><span
										class="mandatory1"> *</span></td>
									<td class="greybox"><s:textfield name="voucherNumber"
											id="voucherNumber" readonly="true" /></td>
								</s:else>
							</tr>
							<tr>
								<td class="greybox">&nbsp;</td>
								<td class="greybox"><s:text name="voucher.date" /><span
									class="mandatory1"> *</span></td>
								<td class="greybox"><s:date name="voucherDate"
										var="voucherDateId" format="dd/MM/yyyy" /> <s:textfield
										name="voucherDate" id="voucherDate" value="%{voucherDateId}"
										maxlength="10"
										onkeyup="DateFormat(this,this.value,event,false,'3')" /> <a
									href="javascript:show_calendar('jvmodifyform.voucherDate');"
									style="text-decoration: none">&nbsp;<img tabIndex=-1
										src="/egi/resources/erp2/images/calendaricon.gif" border="0" /></a>(dd/mm/yyyy)
								</td>
							</tr>
							<jsp:include page="voucherSubType.jsp" />
							<jsp:include page="vouchertrans-filter.jsp" />
							<jsp:include page="loadYIDataTable.jsp" />
							<tr>
								<td class="greybox">&nbsp;</td>
								<td class="greybox"><s:text name="voucher.narration" /></td>
								<td class="greybox" colspan="3"><s:textarea id="narration"
										name="description" style="width:580px"
										onblur="checkVoucherNarrationLen(this)" /></td>
							</tr>
						</table>
					</div>
					<br />
					<div id="labelAD" align="center">
						<table width="80%" border=0 id="labelid">
							<th>Account Details</th>
						</table>
					</div>
					<div class="yui-skin-sam" align="center">
						<div id="billDetailTable"></div>
					</div>
					<script type="text/javascript">
		
		makeVoucherDetailTable();
		document.getElementById('billDetailTable').getElementsByTagName('table')[0].width="90%"
	 </script type="text/javascript" >
					<div id="codescontainer"></div>
					<br />
					<div id="labelSL" align="center">
						<table width="80%" border=0 id="labelid">
							<th>Sub-Ledger Details</th>
						</table>
					</div>

					<div class="yui-skin-sam" align="center">
						<div id="subLedgerTable"></div>
					</div>
					<script type="text/javascript">
			
			makeSubLedgerTable();
			
			document.getElementById('subLedgerTable').getElementsByTagName('table')[0].width="90%"
		</script>

					<br />
					<div class="subheadsmallnew" /></div>
					<div id="wfHistoryDiv">
						<%--   	<c:import url="/WEB-INF/jsp/workflow/workflowHistory.jsp" context="/egi">
	        <c:param name="stateId" value="${voucherHeader.state.id}"></c:param>
        </c:import> --%>
					</div>
					<s:if test='%{! wfitemstate.equalsIgnoreCase("END")}'>
						<%@include file="voucherWorkflow.jsp"%>
					</s:if>
					<div align="center">
						<table border="0" width="100%">
							<tr>
								<td class="bluebox">&nbsp;</td>
								<td class="bluebox">Comments</td>
								<td class="bluebox"><s:textarea name="comments"
										id="comments" cols="150" rows="3" onblur="checkLength(this)" /></td>
								<td><s:hidden id="methodName" name="methodName"
										value="save" /></td>
								<s:hidden id="vhid" name="vhid" value="%{voucherHeader.id}" />
								<s:hidden name="actionName" id="actionName" />
							</tr>
							<br />
						</table>
					</div>

					<br />
				</div>
			</div>
			<div id="codescontainer"></div>
			<%@ include file='../workflow/commonWorkflowMatrix.jsp'%>
			<%@ include file='../workflow/commonWorkflowMatrix-button.jsp'%>
			<div class="buttonbottom" id="buttondiv">
				<input type="button" class="button" id="print" value="Print Preview"
					action="journalVoucherPrint" method="print" onclick="printJV()" />
			</div>
			<s:hidden id="cgn" name="cgn"></s:hidden>
			<s:hidden name="saveMode" id="saveMode" />
			<s:hidden name="actionName" id="actionName" />
			<input type="hidden" id="voucherTypeBean.voucherNumType"
				name="voucherTypeBean.voucherNumType" value="Journal Voucher" />
			<s:hidden id="type" name="type" value="Journal Voucher" />
			<input type="hidden" id="worksVoucherRestrictedDate"
				name="worksVoucherRestrictedDate"
				value="${worksVoucherRestrictedDate}" />
		</s:push>
	</s:form>

	<script type="text/javascript">
function validateApproverUser(name,value){
//	bootbox.alert("name value"+name);    
	document.getElementById("actionName").value= name;
	<s:if test='%{! wfitemstate.equalsIgnoreCase("END")}'>
		if(!validateUser(name,value)){
		return false;
		}
	</s:if> return true;
}
function onSubmit()
{
	if(validateAndSubmitJV())
		document.forms[0].action='${pageContext.request.contextPath}/voucher/journalVoucherModify-update.action';
	document.forms[0].submit();
			
}
function validateAndSubmitJV()
{
	if(validateJV()){
		return true;
		}else{
			return false;
			}
}
function validateJV()
{
	document.getElementById('lblError').innerHTML ="";
	//document.getElementById('saveMode').value=saveMode;
	var cDate = new Date();
	var currDate = cDate.getDate()+"/"+(parseInt(cDate.getMonth())+1)+"/"+cDate.getYear();
	var vhDate=document.getElementById('voucherDate').value;
	var VhType= document.getElementById('vType').value;
	/* var typeDate=document.getElementById('worksVoucherRestrictedDate').value;
	var restrictionDate = typeDate.split(",") */

	if(vhDate == '' )	{
		document.getElementById('lblError').innerHTML = "Please enter a voucher date ";
		document.getElementById('voucherDate').focus();
		return false;
	}

	//bootbox.alert("---"+VhType);
	/* if(VhType=='Works'){
		var chkd1=vhDate.split('/');
		var chkd2=restrictionDate[1].split('/');
		var voucherDt=new Date(chkd1[2],chkd1[1]-1,chkd1[0]);
		var restrictionDt=new Date(chkd2[2],chkd2[1]-1,chkd2[0]);

		if(voucherDt>=restrictionDt){
			bootbox.alert(" Cannot Modify Works JV Date to greater than "+restrictionDate[1]);
			return false;
		}
	} */
	
	var varVType = document.getElementById('vType').value;
	if( varVType != 'JVGeneral' && varVType != '-1' )	{
		if(document.getElementById('voucherTypeBean.partyName').value == '' ) {
			document.getElementById('lblError').innerHTML = "Please enter a Party Name ";
			document.getElementById('voucherTypeBean.partyName').focus();
			return false;
		}
	}
		
	if(!validateMIS())	return false;
	//if(!validateApproverUser(name,value)) return false;
	return true;
}
	function onLoadTask()
	{
		//loadSlFunction();
		//getSlAccountCodes();
		// code- JV subtype - starts
		document.getElementById('vType').value='<s:property value="voucherTypeBean.voucherSubType"/>';
		if('<s:property value="voucherTypeBean.voucherSubType"/>' == 'JVGeneral' ){
			document.getElementById('voucherTypeBean.partyBillNum').readOnly=true;
			document.getElementById('voucherTypeBean.partyName').readOnly=true;
			document.getElementById('partyBillDate').readOnly=true;
			document.getElementById('voucherTypeBean.billNum').readOnly=true;
			document.getElementById('billDate').readOnly=true;
		}
		var varVType = document.getElementById('vType').value;
		if(varVType == 'JVGeneral' || varVType == '-1') {
			document.getElementById('partyNameDivId').style.display='none';
		} else {
			document.getElementById('partyNameDivId').style.display='inline';
		}
		document.getElementById('vType').disabled=true; 
		// code- JV subtype - ends
		var target = '<s:property value="target"/>';
		var saveMode='<s:property value="saveMode"/>';
		var voucherNumber = '<s:property value='%{voucherHeader.voucherNumber}'/>' ;
		var cgn = '<s:property value='%{cgn}'/>' ;
		if(target == 'success' ){
			if(saveMode == 'saveclose'){
				bootbox.alert("Voucher modified sucessfully with voucher number =  "+voucherNumber );
				window.close();
			}else if(saveMode == 'saveview'){
				bootbox.alert("Voucher modified sucessfully with voucher number =  "+voucherNumber);
				window.open('preApprovedVoucher-loadvoucherview.action?vhid=<s:property value='%{voucherHeader.id}'/>','Search','resizable=yes,scrollbars=yes,left=300,top=40,width=900, height=700');
			}else if(saveMode == 'saveprint'){
				bootbox.alert("Voucher modified sucessfully with voucher number =  "+voucherNumber);
				window.open('journalVoucherPrint-print.action?id=<s:property value='%{voucherHeader.id}'/>','','resizable=yes,scrollbars=yes,left=300,top=40,width=900, height=700');
			}
		}
		<s:if test="%{shouldShowHeaderField('vouchernumber')}">
			   var tempVoucherNumber='<s:property value="voucherHeader.voucherNumber"/>';
			   var prefixLength='<s:property value="voucherNumberPrefixLength"/>';
			   document.getElementById('voucherNumberPrefix').value=tempVoucherNumber.substring(0,prefixLength);
			   document.getElementById('voucherNumber').value=tempVoucherNumber.substring(prefixLength,tempVoucherNumber.length);
		</s:if>
		populateslDropDown(); // to load the subledger detils when page loads, required when validation fails.
		if(document.getElementById('approverDepartment'))
			document.getElementById('approverDepartment').value = "-1";
	}

	function loadBank(fund){
	}

</script>
</body>

</html>
