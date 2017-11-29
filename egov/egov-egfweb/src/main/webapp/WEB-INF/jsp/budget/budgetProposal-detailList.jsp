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
<html>
<head>
<title><s:text name="budgetdetail" /></title>
<link rel="stylesheet"
	href="/EGF/resources/css/tabber.css?rnd=${app_release_no}"
	TYPE="text/css">
<script type="text/javascript"
	src="/EGF/resources/javascript/tabber.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/tabber2.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/helper.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/jquery-1.7.2.min.js"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/jquery/jquery.fixheadertable.js"></script>
<link rel="stylesheet" type="text/css"
	href="/EGF/resources/css/jquery/base.css?rnd=${app_release_no}" />
<link rel="stylesheet" type="text/css"
	href="/EGF/resources/css/jquery-ui/css/redmond/jquery-ui-1.8.4.custom.css" />

<jsp:include page="budgetHeader.jsp" />
<SCRIPT type="text/javascript">
  
    
    var dept_callback = {
		success: function(o){
			if(trimStr(o.responseText) != '' && trimStr(o.responseText) != '0'){
				document.getElementById('departmentid').value = trimStr(o.responseText);
				if(document.getElementById('departmentid').value!=-1){
					document.getElementById('departmentid').disabled=true;
					populateDesg();
				}
			}else{
					document.getElementById('departmentid').disabled=false;
				}},
			failure: function(o) {
				document.getElementById('departmentid').disabled=false;
		    }
		}
		
    </SCRIPT>
</head>
<body>
	<s:form action="budgetProposal" theme="simple">
		<s:token />
		<div style="color: red">
			<s:actionmessage theme="simple" />
			<s:actionerror />
			<s:fielderror />
		</div>
		<div align="left">
			<br />
			<table border="0" cellspacing="0" cellpadding="0" >
				<tr>
					<td>
						<div class="tabber">
							<div class="tabbertab" style="height: 500px; width: 1200px;">
								<h2>Budget Details</h2>

								<script>
						var callback = {
							     success: function(o) {

								if(o.responseText=='successful')
								{
								     bootbox.alert("Deletion success");
								}else
								{
								     bootbox.alert("Deletion failed");
								}


								 },
							     failure: function(o) {
								     bootbox.alert("Deletion failed");
							     }
						} 
						function deleteBudgetDetail(re, be,obj,bename,rename){
							var rownum=getRow(obj);
							var table=document.getElementById("detailsTable");
							var beamount=document.getElementById(bename).value;
							var reamount=document.getElementById(rename).value;
							if(beamount==0 && reamount==0){
							if(table)
								{
								table.deleteRow(rownum.rowIndex);
								} 
							bootbox.alert("Sending Request to server Please wait for Confirmation");
							var transaction = YAHOO.util.Connect.asyncRequest('POST', 'budgetProposal-ajaxDeleteBudgetDetail.action?bpBean.id='+re+'&bpBean.nextYrId='+be, callback, null);
						}else{
							bootbox.alert("This Budget detail cannot be deleted ");
						}  
						}
						</script>
								<s:set var="validButtons" value="%{validActions}" />
								<jsp:include page="budgetHeader.jsp" />
								<s:if test="%{isConsolidatedScreen()}">
									<div align="right" class="extracontent">Amount in
										Thousands</div>
								</s:if>
								<s:else>
									<div align="right" class="extracontent">Amount in Rupees
									</div>
								</s:else>

								<s:hidden name="topBudget.id" value="%{getTopBudget().getId()}" />
								<s:hidden name="consolidatedScreen"
									value="%{consolidatedScreen}" />
								<s:hidden name="budgetDetail.id" id="budgetDetail.id" />
								<s:hidden name="budgetDetail.budget.id"
									id="budgetDetail.budget.id" />

								<s:if test="%{!bpBeanList.isEmpty()}">
									<div id="detail">
										<%@ include file="budgetProposal-modifyList.jsp"%>

									</div>
								</s:if>
								<br /> <br />
								<script>
								function validateAmounts(){
									var len = <s:property value="savedbudgetDetailList.size"/>;
									for(i=0;i<len;i++){
										if(document.getElementById('savedbudgetDetailList['+i+'].approvedAmount') && document.getElementById('savedbudgetDetailList['+i+'].approvedAmount').value == ''){
											bootbox.alert("Enter approved amount");
											return false;
										}
									}
									return true;
								}
								function validateAppoveUser(value){
									document.forms[0].action = "${pageContext.request.contextPath}/budget/budgetProposal-update.action";
									document.forms[0].submit();
								}
							</script>
								<s:hidden id="scriptName" value="BudgetDetail.nextDesg" />

							</div>
							<!-- Individual tab -->
							<div class="tabbertab" id="approvalDetails"
								style="height: 500px; width: 1200px;">
								<h2>Approval Details</h2>

								<table align="center" border="0" cellpadding="0" cellspacing="0"
									width="100%" class="tablebottom"
									style="border-right: 0px solid #C5C5C5;">
									<tr>
										<td width="5%"></td>
										<td class="blueborderfortd" width="5%"><b>Budget:</b></td>
										<td class="blueborderfortd"><s:property
												value="%{getTopBudget().getName()}" /></td>
										<td class="blueborderfortd" width="5%"><b>Remarks:</b></td>
										<td class="blueborderfortd"><textarea cols="50" rows="3"
												name='comments'><s:property value="comments" /></textarea>
										</td>
										<s:if test="%{isConsolidatedScreen()}">
											<td class="blueborderfortd" width="5%"><b><s:text
														name="As On Date" />:</b></td>
											<td class="blueborderfortd" width="5%"><s:textfield
													name="asOndate" id="asOndate" cssStyle="width:100px" /><a
												href="javascript:show_calendar('budgetProposal.asOndate');"
												style="text-decoration: none">&nbsp;<img
													src="/egi/resources/erp2/images/calendaricon.gif"
													border="0" /></a>(dd/mm/yyyy)</td>
											<td width="5%"></td>
											<td><input type="button" class="buttonsubmit"
												value="Refresh" id="refresh" name="refresh"
												onclick="updateNew()" /></td>
											<td width="5%"></td>
											<td width="5%"></td>
											<td width="5%"></td>
											<td width="5%"></td>
											<td width="5%"></td>
											<td width="5%"></td>
											<td width="5%"></td>
											<td width="5%"></td>
										</s:if>
									</tr>
								</table>


								<div class="buttonholderwk" id="buttonsDiv">
									<s:hidden name="actionName" />
									<s:hidden name="mode" />
									<centre>
									<div class="buttonbottom" id="sbuttons"
										style="text-align: center">
										<s:iterator value="%{getValidActions()}" var="validAction">
											<s:if test="%{validAction!=''}">
												<s:submit type="submit" cssClass="buttonsubmit"
													value="%{validAction}" id="%{validAction}"
													name="%{validAction}" method="update"
													onclick="document.budgetProposal.actionName.value='%{validAction}';return validateAppoveUser('%{validAction}');" />
											</s:if>
										</s:iterator>


										<input type="button" value="Close"
											onclick="javascript:window.close()" class="button" />
									</div>

									<div id="exportButton" class="buttonbottom"
										style="text-align: center"></div>
									</centre>
								</div>

							</div>
							<!-- Individual tab -->
						</div>
					</td>
				</tr>
			</table>
		</div>



		<script>
	if(document.getElementById("approve")){
		//bootbox.alert("-----"+document.getElementById("approve").value);
		document.getElementById("approvalDetails").style.display = 'none';
	}
	<s:if test='%{isHod()}'>
		<s:if test="%{!isAllfunctionsArrived()}">
		   bootbox.alert("Not All function Centers Received , Forward Not allowed ");
		   bootbox.alert('List of functions not yet Received are : <s:property value="functionsNotYetReceiced"/>');
		   if(document.getElementById("forward"))
			document.getElementById("forward").style.display = 'none';
		</s:if>
	</s:if>   
	


		 function exportPDF() {
		 	 var budgetId=document.getElementById("budgetDetail.budget.id").value;
			 var url="${pageContext.request.contextPath}/budget/budgetProposal-generatePdf.action?budgetDetail.budget.id="+budgetId;
			 window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
			 }

		 function exportExcel() {
		 	 var budgetId=document.getElementById("budgetDetail.budget.id").value;
			 var url="${pageContext.request.contextPath}/budget/budgetProposal-generateXls.action?budgetDetail.budget.id="+budgetId;
			 window.open(url,'','height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');
			 }

	function updateNew(){
		var asOndate = document.getElementById("asOndate").value;
		var budid = document.getElementById("budgetDetail.budget.id").value;
		window.location = "/EGF/budget/budgetProposal-modifyBudgetDetailList.action?budgetDetail.budget.id="+budid+"&asOndate="+asOndate;
		return true;
	}

	var elementId = null;
    function showDocumentManager(obj){
        
    	if(obj.id == 'budgetDocUploadButton'){
            elementId = 'budgetDocNumber';
        }else{
            elementId = "bpBeanList["+obj+"].documentNumber";
        }
        docManager(document.getElementById(elementId).value);
    }
 var docNumberUpdater = function (docNumber){
            document.getElementById(elementId).value = docNumber;   
        }	
 var checkDatatableLoaded = window.setInterval(checkDatatable,1);
 function checkDatatable() {
	  if (jQuery('.t_fixed_header_main_wrapper_child')) {
		  jQuery('.t_fixed_header_main_wrapper_child').css('width','918px');
		  window.clearInterval(checkDatatableLoaded);
	  }
 } 			 			
	</script>
	</s:form>
</body>
</html>
