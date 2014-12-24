<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ page import="org.egov.budget.model.*"%>
<html>  
<head>  
    <title>Create budget proposal (RE)</title>
    <link rel="stylesheet" href="/EGF/cssnew/tabber.css" TYPE="text/css">
	<script type="text/javascript" src="/EGF/javascript/tabber.js"></script>
	<script type="text/javascript" src="/EGF/javascript/tabber2.js"></script>
	<script type="text/javascript" src="/EGF/javascript/helper.js"></script>
    <STYLE type="text/css">
    .yui-dt-liner { 
	    text-align: right; 
	} 
	.tabbertab {
		border:1px solid #CCCCCC;
		height:450px;
		margin-bottom:8px;
		overflow:scroll;
	}
    </STYLE>
    <SCRIPT type="text/javascript">
    function onLoadTask(){
    	showMessage = '<s:property value="showMessage"/>';
    	if(showMessage == 'true' && '<s:property value="actionMessage"/>' != ''){
    		alert('<s:property value="actionMessage"/>');
    		document.getElementById('budgetDetail_executingDepartment').value=-1;
    		document.forms[0].action = "${pageContext.request.contextPath}/budget/budgetDetail!newRe.action?re";
			document.forms[0].submit();
    	}
    	defaultDept();
    	<s:if test="%{referenceBudget != null}">
    		document.getElementById('referenceBudget').innerHTML = '<s:property value="referenceBudget.name"/>';
    	</s:if>
    }
    
    function getActuals(){
   		document.forms[0].action = "${pageContext.request.contextPath}/budget/budgetDetail!loadActualsForRe.action";
		document.forms[0].submit();
    }
     function save(){
   		document.forms[0].action = "${pageContext.request.contextPath}/budget/budgetDetail!createRe.action";
		document.forms[0].submit();
    }
     function forward(){
   		document.forms[0].action = "${pageContext.request.contextPath}/budget/budgetDetail!createReAndForward.action";
		document.forms[0].submit();
    }
    
    var elementId = null;
    function showDocumentManager(obj){
    	if(obj.id == 'budgetDocUploadButton'){
    		elementId = 'budgetDocNumber';
    	}else{
    		var index = (getRow(obj).rowIndex)-2;
    		elementId = "budgetDetailList["+index+"].documentNumber";
    	}
	    docManager(document.getElementById(elementId).value);
	}

    var docNumberUpdater = function (docNumber){
			document.getElementById(elementId).value = docNumber;
		}
    
    </SCRIPT>
</head>  
	<body>  
	<s:form name="budgetDetailForm" action="budgetDetail" theme="simple" >
	<div align="left"><br/>
         	<div class="tabber">
           		<div class="tabbertab" >
					<h2>Budget Details</h2>
					<span>
						<div style="height:440px">
						<input type="hidden" id="bere" value="re"/>
						<jsp:include page="budgetHeader.jsp"/>
						<%@ include file='budgetDetailSetUp-re.jsp'%>
						<script>
							function validate(){
								anticipatory = false;
								estimate = false;
								for(i=0;i<budgetDetailsTable.getRecordSet().getLength();i++){
									if(isNaN(document.getElementById('budgetDetailList['+i+'].anticipatoryAmount').value))
										anticipatory = true;
								}				
								for(i=0;i<budgetDetailsTable.getRecordSet().getLength();i++){
									if(isNaN(document.getElementById('budgetDetailList['+i+'].originalAmount').value))
										estimate = true;
								}				
								if(estimate && anticipatory){
									alert('Estimate amount and Anticipatory amount must be a number');
									return false;
								}else if(estimate){
									alert('Estimate amount must be a number');
									return false;
								}else if(anticipatory){
									alert('Anticipatory amount must be a number');
									return false;
								}
								document.budgetDetailForm.submit();
								return;
							}
							function validateForApproval()
							{
								if(null != document.getElementById("approverUserId") && document.getElementById("approverUserId").value == -1){
									alert("Please select User");
									return false;
								}
								forward();
								validate();
							}
						</script>
						<div class="formmainbox"><div class="subheadnew">Create budget proposal</div>
						<div align="center" style="color: red;"> 
						<s:actionmessage theme="simple"/>
						<s:actionerror/>  
						<s:fielderror />  
						</div>
						<%@ include file='budgetDetail-form.jsp'%>
						<s:hidden name="budgetDocumentNumber" id="budgetDocNumber"/>
						<input type="hidden" id="re" value='<s:property value="re"/>'/>
						<table width="100%" border="0" cellspacing="0" cellpadding="0" id="budgetDetailFormTable">
							<tr><td>&nbsp;</td></tr>
							<tr> 
								<td width="25%" class="bluebox">&nbsp;</td>
						 		<td width="5%" class="bluebox"><s:text name="budgetdetail.budget.asOnDate"/></td>
								<td class="bluebox">
									<input type="text"  id="asOnDate" name="asOnDate" style="width:100px" value='<s:date name="asOnDate" format="dd/MM/yyyy"/>'/><a href="javascript:show_calendar('budgetDetailForm.asOnDate');" style="text-decoration:none">&nbsp;<img src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)
									<input type="submit" id="budgetDetail__loadActualsForRe" onclick="getActuals()" value="Get Actuals" class="buttonsubmit"/>
								</td>
							</tr>
							<tr><td>&nbsp;</td></tr>
							</table>
							<br/>
								 <div class="yui-skin-sam" style="width:100%;overflow-x:auto; overflow-y:hidden;">
							       <div id="budgetDetailTable"></div>
							     </div>
							<script>
								makeBudgetDetailTable();
								document.getElementById('budgetDetailTable').getElementsByTagName('table')[0].width = "100%";
								addGridRows();
								hideColumns();
								updateAllGridValues()
								<s:if test="%{getActionErrors().size()>0 || getFieldErrors().size()>0}">
									setValues();
								</s:if>
							</script>
							<br/><br/>
						<s:hidden name="budget" id="hidden_budget"/>
						<div id="savedDataGrid">
							<s:if test="%{savedbudgetDetailList.size()>0}">
								<%@ include file='budgetDetailList.jsp'%>
							</s:if>
						</div>
						<script>
								document.getElementById('hidden_budget').value = '<s:property value="budgetDetail.budget.id"/>'
						</script>
						</div>
					</span>
				</div> <!-- Individual tab -->
				<div class="tabbertab">
                  	<h2>Approval Details</h2>
	                <span>
	                	<input type="hidden" name="scriptName" id="scriptName" value="BudgetDetail.nextDesg"/>
	               		<%@include file="../voucher/workflowApproval.jsp"%>
	                </span>
				</div> <!-- Individual tab -->
			</div>
	</div>
	<div class="buttonbottom" style="padding-bottom:10px;position:relative"> 
		<s:hidden  name="actionName" value="forward"/>
		<input type="submit" value="Save" id="budgetDetail__createRe" name="method:createRe" onClick="javascript: return validate();" class="buttonsubmit"/>
		<input type="submit" value="Forward" id="budgetDetail__createReAndForward" name="method:createReAndForward" onClick="javascript: return validateForApproval();" class="buttonsubmit"/>
		<!-- <input type="submit" class="buttonsubmit" value="Upload Document" id="budgetDocUploadButton" onclick="showDocumentManager(this);return false;" /> -->
		<s:submit value="Close" onclick="javascript: self.close()" cssClass="button"/>
	</div>
	<script>
		onLoadTask();
		var dept_callback = {
		success: function(o){
			if(o.responseText != '')
				document.getElementById('departmentid').value = o.responseText;
				document.getElementById('departmentid').disabled=true;
			},
			failure: function(o) {
		    }
		}
		
		function updateApproverDepartment(obj){
			document.getElementById('departmentid').value = document.getElementById('budgetDetail_executingDepartment').value;
			populateDesg();
		}
		function defaultDept(){
			var url = '/EGF/voucher/common!ajaxLoadDefaultDepartment.action';
			YAHOO.util.Connect.asyncRequest('POST', url, dept_callback, null);
		}
		document.getElementById('hidden_budget').value = '<s:property value="budgetDetail.budget.id"/>'
	</script>
	</s:form>
	</body>  
</html>