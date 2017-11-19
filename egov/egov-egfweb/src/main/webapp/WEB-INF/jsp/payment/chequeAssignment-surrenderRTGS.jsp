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


<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<html>
<head>
<link rel="stylesheet" type="text/css" 
href="/EGF/resources/css/ccMenu.css?rnd=${app_release_no}" />
<title><s:text name="surrender.rtgs" /></title>
</head>
<body>

	<s:form action="chequeAssignment" theme="simple">
		<s:token />
		<jsp:include page="../budget/budgetHeader.jsp">
			<jsp:param name="heading" value="Surrender RTGS" />
		</jsp:include>
		<span class="mandatory1"> <s:actionerror /> <s:fielderror /> 
		<s:actionmessage />
		</span>
			<div class="subheadnew"></div>
			<div class="formmainbox">
				<div class="subheadnew">
					<s:property value="bank_account_dept" />
				</div>
				<!--<s:hidden name="department" />
			-->
				<s:hidden name="bankaccount" id="bankaccount" />
				<s:hidden name="bank_branch" />
				<s:hidden name="fromDate" />
				<s:hidden name="toDate" />
				<s:hidden name="voucherNumber" />
				<s:hidden name="instrumentNumber" />
				<table align="center" width="100%" cellpadding="0" cellspacing="0">
					<tr>
						<th class="bluebgheadtdnew"><s:text name="Sl No." /></th>
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.transacton.no" /></th>
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.rtgs.amount" /></th>
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.transaction.date" /></th>
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.paymentvoucherno" /></th>
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.surrender" /></th>
						<th class="bluebgheadtdnew"><s:text
								name="chq.assignment.surrendarreason" /></th>
					</tr>
					<s:if test="%{instrumentHeaderList.size()>0 }">
						<s:iterator var="p" value="instrumentHeaderList" status="stat">
							<tr>
								<s:hidden name="instrumentHeaderId" value="%{id}" />
								<s:hidden name="paymentVoucherNumber"
									value="%{voucherHeaderId.id}" />
									
									
								<td style="text-align: center" class="blueborderfortdnew" />
								<s:property value="#stat.index+1" />
								</td>
								<td style="text-align: center" class="blueborderfortdnew"><s:property
										value="%{transactionNumber}" /><s:hidden name="transactionNumber"
								value="%{transactionNumber}" /></td>
										
										
								<td style="text-align: right" class="blueborderfortdnew"><s:text
										name="format.number">
										<s:param value="%{instrumentAmount}" />
									</s:text></td>
							
									
								<td style="text-align: center" class="blueborderfortdnew"><s:date
										name="%{transactionDate}" format="dd/MM/yyyy" /> <s:hidden name="transactionDate"
								value="%{transactionDate}" />
								</td>
								
								<td style="text-align: center" class="blueborderfortdnew">
									<s:iterator var="v" value="instrumentVouchers" status="st">
										<A href="#"
											onclick='openDetails(<s:property value="%{voucherHeaderId.id}"/>);'>
											<s:property value="%{voucherHeaderId.voucherNumber}" />
										</A>
									</s:iterator>
								</td>
								<td style="text-align: center" class="blueborderfortdnew">
								<s:checkbox
										name="surrender"
										value='%{surrender[#stat.index]!=null?true:false}'
										fieldValue="%{id}" id="surrender%{#stat.index}"/></td>
								<td style="text-align: center" class="blueborderfortdnew">
									<s:select name="surrendarReasons" id="surrendarReasons"
										list="surrendarReasonMap" headerKey="-1"
										headerValue="----Choose----"
										value='%{surrendarReasons[#stat.index]}' />
							</tr>
						</s:iterator>
						<tr>
							<td class="greybox"><s:text name="chq.issued.department" /><span
								class="mandatory1">*</span> <s:select name="department"
									id="department" list="dropdownData.departmentList" listKey="id"
									listValue="name" headerKey="-1"
									headerValue="----Choose----" value="%{department}" /></td>
						</tr>
					</s:if>


				</table>
				<br />

				<s:if test="%{instrumentHeaderList.size()>0}">
					<div class="buttonbottom">
						<s:hidden name="button" id="button" />
						
					
					 <s:hidden name="selectedRowsId" id="selectedRowsId"
						value="%{selectedRowsId}" />
						 
					
						<input type="button" Class="buttonsubmit" name="Surrender"
							value="Surrender" onclick="return surrenderChq();" method="save" />
							
						
					
					
						<input type="button" value="Close"
							onclick="javascript:window.close()" class="button" />
					</div>
				</s:if>
				<s:else>
					<div class="subheadsmallnew" id="noRecordsDiv">No Records
						Found</div>
				</s:else>
			</div>


			<s:token />
	</s:form>
	<script>
		function validatechequeno(obj)
			{
				if(isNaN(obj.value))
				{
					bootbox.alert('Cheque number contains alpha characters.');
					obj.value='';
					return false;
				}
				var index = obj.id.substring(19,obj.id.length);
				if(obj.value=='')
					return true;
				//bootbox.alert(index);		
				if(document.getElementById('department') && document.getElementById('department').options[document.getElementById('department').selectedIndex].value==-1)
				{
					bootbox.alert('Select Cheque Issued From');
					obj.value='';
					return false;
				}
				var name=obj.id;
				name=name.replace("InstrumentNumber","SerialNo");
			   var slObj=	document.getElementById(name);
				var dept = document.getElementById('department').options[dom.get('department').selectedIndex].value;
				var slNo = slObj.options[slObj.selectedIndex].value;
				var url = '${pageContext.request.contextPath}/voucher/common-ajaxValidateChequeNumber.action?bankaccountId='+document.getElementById('bankaccount').value+'&chequeNumber='+obj.value+'&index='+index+'&departmentId='+dept+"&serialNo="+slNo;
				var transaction = YAHOO.util.Connect.asyncRequest('POST', url,callback , null);
			}
			
			
			var callback = {
				success: function(o) {
					var res=o.responseText;
					res = res.split('~');
					if(res[1]=='false')
					{
						bootbox.alert('Enter valid cheque number or This Cheque number has been already used');
						document.getElementById('newInstrumentNumber['+parseInt(res[0])+']').value='';
					}
			    },
			    failure: function(o) {
			    	bootbox.alert('failure');
			    }
			}
			
		
	function openDetails(val)
	{
	var mode="view";
 	window.open("/EGF/voucher/preApprovedVoucher-loadvoucherview.action?vhid="+val+"&showMode="+mode,"","height=650,width=900,scrollbars=yes,left=30,top=30,status=yes");
 	}
 	function surrenderChq(){
 		resetSelectedRowsId();
 		disableAll();
 		document.getElementById('button').value='surrender';
 		document.chequeAssignment.action = '/EGF/payment/chequeAssignment-save.action?containsRTGS=true';
		document.chequeAssignment.submit();
		return true;
 	}
  	function Reassign()
 	{
		resetSelectedRowsId();
	 	document.getElementById('button').value='surrenderAndReassign';
	 	var chqGenMode='<s:property value="isChequeNoGenerationAuto()"/>';
	 	var alertNumber='<s:text name="chq.number.missing.alert"/>';
	 	var alertOnlyNumber='<s:text name="chq.number.should.numeric"/>';
	 	var alertDate='<s:text name="chq.date.missing.alert"/>';
	 	if(chqGenMode=='false')
	 	{
	 		var surrenderObj=document.getElementsByName('surrender');
	 		var newChqNoObj=document.getElementsByName('newInstrumentNumber');
	 		var newChqDateObj=document.getElementsByName('newInstrumentDate');
			var i;
	 		for(i=0;i<surrenderObj.length;i++)
	 		{
	 		 if(surrenderObj[i].checked==true)
	 			{
	 				if(newChqNoObj[i].value==""||newChqNoObj[i].value==undefined)
					{
						bootbox.alert(alertNumber);
						newChqNoObj[i].focus();
						return false;
					}
					else
					{
					if(isNaN(newChqNoObj[i].value))
					{
					bootbox.alert(alertOnlyNumber);
					}
					}
					
					if(newChqDateObj[i].value=="" || newChqDateObj[i].value==undefined)
					{
					bootbox.alert(alertDate);
					newChqDateObj[i].focus();
					return false;
	 				}
	 			}
	  	
	 		}
	 	
 		}
	 	disableAll();
	 	document.chequeAssignment.action = '/EGF/payment/chequeAssignment-save.action?containsRTGS='+document.getElementById('containsRTGS').value;
		document.chequeAssignment.submit();
		
 	}
 	var selectedRowsId = new Array();
  	function resetSelectedRowsId(){
  		
  		var newSurrendarReasonsObj=document.getElementsByName('surrendarReasons');
 		//var newSerialNoObj=document.getElementsByName('newSerialNo');
 		var newChqNoObj=document.getElementsByName('transactionNumber');
 		var newChqDateObj=document.getElementsByName('transactionDate');
		var chequeSize='<s:property value ="%{instrumentHeaderList.size()}"/>';
		   selectedRowsId = new Array();
			for(var index=0;index<chequeSize;index++){
				var obj = document.getElementById('surrender'+index);
				if(obj.checked == true){
				selectedRowsId.push(document.getElementsByName("instrumentHeaderId")[index].value+"~"+
							newChqNoObj[index].value+"~"+
							newChqDateObj[index].value+"~"+"~"+
							//newSerialNoObj[index].value+";"+
							newSurrendarReasonsObj[index].value+";"				
							);
					
					
				}
			}
			document.getElementById('selectedRowsId').value = selectedRowsId;
	}
	
  	function disableAll()
	{
		var frmIndex=0;
		for(var i=0;i<document.forms[frmIndex].length;i++)
			{
				for(var i=0;i<document.forms[0].length;i++)
					{
						if(document.forms[0].elements[i].name != 'bankaccount' && document.forms[0].elements[i].name != 'bank_branch'
							&& document.forms[0].elements[i].name != 'fromDate' && document.forms[0].elements[i].name != 'toDate' &&
							document.forms[0].elements[i].name != 'button' && document.forms[0].elements[i].name != 'selectedRowsId'
							&& document.forms[0].elements[i].name != 'containsRTGS' && document.forms[0].elements[i].name != 'voucherNumber'
							&& document.forms[0].elements[i].name != 'instrumentNumber' && document.forms[0].elements[i].name != 'surrender'
							&& document.forms[0].elements[i].name != 'department' && document.forms[0].elements[i].name != 'newInstrumentNumber' 
							&& document.forms[0].elements[i].name != 'newInstrumentDate' && document.forms[0].elements[i].name != 'surrendarReasons' 
							&& document.forms[0].elements[i].name != 'newInstrumentDate'){
							document.forms[frmIndex].elements[i].disabled =true;
						}						
					}	
			}
	}
 	
 		
	</script>
</body>

</html>
 