<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<html>  
<head>  
    <title><s:text name="budgetdetail"/></title>
    <link rel="stylesheet" href="/EGF/cssnew/tabber.css" TYPE="text/css">
	<script type="text/javascript" src="/EGF/javascript/tabber.js"></script>
	<script type="text/javascript" src="/EGF/javascript/tabber2.js"></script>
	<script type="text/javascript" src="/EGF/javascript/helper.js"></script>
    <jsp:include page="budgetHeader.jsp"/>
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
		
		function defaultDept(){
			var url = '/EGF/voucher/common!ajaxLoadDefaultDepartment.action';
			YAHOO.util.Connect.asyncRequest('POST', url, dept_callback, null);
		}
    </SCRIPT>
</head>  
	<body>  
	<s:form action="budgetSearchAndModify" theme="simple" >
	<div style="color: red">
		<s:actionmessage theme="simple"/>
		<s:actionerror/>  
		<s:fielderror />
	</div>
		<div align="left"><br/>
    	<table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr>
        <td> 
         	<div class="tabber">
           		<div class="tabbertab">
					<h2>Budget Details</h2>
					<span>
						<script>
							var budgetDetailsTable = null;
							var callback = {
								     success: function(o) {
							     			document.getElementById('detail').innerHTML = o.responseText;
									var length = document.getElementById('detailsLength').value;
									if(length==0)
									{
										document.getElementById('buttonsDiv').style.display="none";
									}
								        },
								     failure: function(o) {
								     }
							} 
							function deleteBudgetDetail(elem){
								var transaction = YAHOO.util.Connect.asyncRequest('POST', 'budgetSearchAndModify!ajaxDeleteBudgetDetail.action?id='+elem.id+'&action=modify', callback, null);
							}
						</script>
						<s:set var="validButtons" value="%{validActions}" />
						<jsp:include page="budgetHeader.jsp"/>
						<div class="formmainbox"><div class="subheadnew"><s:text name="budgetdetail"/></div>
							<s:hidden name="budget.id" value="%{getTopBudget().getId()}"/>
							<s:if test="%{showApprovalDetails()}"> 
								<table align="center" border="0" cellpadding="0" cellspacing="0"
									width="100%" class="tablebottom"
									style="border-right: 0px solid #C5C5C5;">
									<tr>
										<td class="blueborderfortd" width="5%"><b>Budget:</b></td>
										<td class="blueborderfortd">
											<s:property value="%{getTopBudget().getName()}" />
										</td>
										<td class="blueborderfortd" width="5%"><b>Remarks:</b></td>
										<td class="blueborderfortd">
											<textarea cols="50" rows="3" name='comments' ><s:property value="comments"/></textarea>
										</td>
									</tr>
								</table>
							</s:if>
							<s:hidden name="budget" id="hidden_budget"/>
							<input type='hidden' name='action' value='modify'/>
							<s:if test="%{!savedbudgetDetailList.isEmpty()}">
				               	<div id="detail" width="600">
									<%@ include file="budgetSearchAndModify-modifyList.jsp" %>
								</div>
							</s:if>
							<br/><br/>
							<script>
								document.getElementById('hidden_budget').value = '<s:property value="budgetDetail.budget.id"/>';
								function validateAmounts(){
									var len = <s:property value="savedbudgetDetailList.size"/>;
									for(i=0;i<len;i++){
										if(document.getElementById('savedbudgetDetailList['+i+'].approvedAmount') && document.getElementById('savedbudgetDetailList['+i+'].approvedAmount').value == ''){
											alert("Enter approved amount");
											return false;
										}
									}
									return true;
								}
								function validateAppoveUser(name,value){
									<s:if test="%{wfitemstate =='END'}">
										if(value == 'Approve' || value == 'Reject') {
											document.getElementById("approverUserId").value=-1;
											return true;
										}
									</s:if>
									<s:else>
										if( (value == 'forward' || value == 'Forward') && null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
											alert("Please select User");
											return false;
										}
									</s:else>
									return validateAmounts();
								}
							</script>
							<s:hidden  id="scriptName" value="BudgetDetail.nextDesg"/>
						</div>
					</span>
				</div> <!-- Individual tab -->
				<div class="tabbertab" id="approvalDetails">
					<h2>Approval Details</h2>
					<span>
						<s:if test="%{!savedbudgetDetailList.isEmpty()}">
							<s:if test='%{! "END".equalsIgnoreCase(wfitemstate)}'>
									<%@include file="../voucher/workflowApproval.jsp"%>
									<script>
										document.getElementById('departmentid').value='<s:property value="savedbudgetDetailList[0].executingDepartment.id"/>';
										populateDesg();
										defaultDept();
									</script>
							</s:if>
						</s:if>
					</span>
				</div><!-- Individual tab -->
			</div>
		</td>
		</tr>
		</table>
		</div>
		<div class="buttonholderwk" id="buttonsDiv">
		<s:hidden  name="actionName" />
		<s:hidden name="mode"/>
			<div class="buttonbottom">
				<s:if test="%{showbuttons()}">
					<s:iterator value="%{validButtons}">
					  	<s:submit type="submit" cssClass="buttonsubmit" value="%{capitalize(description)}" id="%{name}" name="%{name}" method="update" onclick=" document.budgetSearchAndModify.actionName.value='%{name}';return validateAppoveUser('%{name}','%{description}')"/>
					</s:iterator>
				</s:if>
				<input type="submit" value="Close" onclick="javascript:window.close()" class="button"/>
			</div>
		</div>
		
	</s:form>
	<script type="text/javascript">
	if(document.getElementById("approve")){
		document.getElementById("approvalDetails").style.display = 'none';
	}
	</script>
	</body>  
</html>
