<%@ include file="/includes/taglibs.jsp" %>
<%@ page language="java" import="java.util.*,
                java.sql.*,
                org.egov.commons.service.CommonsService,
                org.egov.payroll.utils.PayrollManagersUtill, 
                org.egov.infstr.utils.HibernateUtil,
				java.text.SimpleDateFormat,
				org.egov.infstr.commons.dao.*,
				org.egov.pims.service.*,
                org.egov.infstr.utils.EgovMasterDataCaching,org.egov.payroll.utils.PayrollExternalInterface,org.egov.payroll.utils.PayrollExternalImpl" %>


<html>

<head>


	<title>Regular Payslips </title>
<%
String payslipWfType = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payroll","PayslipWorkflow",new java.util.Date()).getValue();
%>


<script language="JavaScript"  type="text/JavaScript">

function loadDesignationFromMatrix()
{
	if(null != document.getElementById('paySlipCurrentState'))
	{
		<s:if test="%{getNextAction()!='END' && mode!='view'}">	
	      var currentState=document.getElementById('paySlipCurrentState').value;
	      //currentState = currentState.toLowerCase();
	      var amountRule=document.getElementById('amountRule').value;
	      var additionalRule=document.getElementById('additionalRule').value;
	      var pendingAction=document.getElementById('pendingActions').value;
	      var dept="";
	      loadDesignationByDeptAndType('EmpPayroll',dept,currentState,amountRule,additionalRule,pendingAction); 
	    </s:if>
	}   
}

function populateApprover()
{
	getUsersByDesignationAndDept();
}

	

  	





  	
	
	function showPayslipInfo(content){			
	    var url='/payroll/payslip/modifyPaySlips.do?payslipId=' + content ;
		window.open(url,'PayslipInfo','width=800,height=600,top=150,left=80,scrollbars=yes,resizable=yes');
    }
    
  function disableSubmit()
  {
	  /*if(!document.payslipApproveFrom.submitButton.disabled)
	  { 
	  	document.payslipApproveFrom.submitButton.disabled=true; 
	  }	*/ 
 	 refreshInbox();
  }
    
    function refreshInbox()
    {
    
    	if(opener.top.document.getElementById('inboxframe')!=null)
    	{    	
    		if(opener.top.document.getElementById('inboxframe').contentWindow.name!=null && opener.top.document.getElementById('inboxframe').contentWindow.name=="inboxframe")
    		{    		
    		opener.top.document.getElementById('inboxframe').contentWindow.egovInbox.refresh();
    		}
    	
    	}
    }
    
    function validation(){		
    	var anyRowSelection = "N";    	
    	var len = document.getElementsByName("selectedPaySlips").length;
    	if(len > 0){		
	  		for(var i = 0 ; i < len ; i++){
	  			if(document.getElementsByName("selectedPaySlips")[i].checked){	  				
	  				anyRowSelection = "Y";
	  				break;
	  			}	  			
	  		}
	  	}	  	  
	  	if(anyRowSelection == "N"){
	  		alert("Select at least one row to approve/reject");
	  		return false;
	  	}
	  	else
		  	return true;		
    }
    
    function checkAllSelected(obj){    	
    	var len = document.getElementsByName("selectedPaySlips").length;
    	
    	if(len > 0){		
	  		for(var i = 0 ; i < len ; i++){
	  			if(obj.checked){
	  				document.getElementsByName("selectedPaySlips")[i].checked = true;
	  			}
	  			else{
	  				document.getElementsByName("selectedPaySlips")[i].checked = false;
	  			}
	  		}
	  	}
	  	else{
	  		if(obj.checked){
	  			document.getElementById("selectedPaySlips").checked = true;
	  		}
  			else{
  				document.getElementById("selectedPaySlips").checked = false;
  			}  		
	  	}
    	
    }
    
    
    
    function showBillInfo(billId){
		window.open("${pageContext.request.contextPath}/payslip/viewPayslipsForBill.action?billId="+billId,"ViewPayslip","height=900pt, width=900pt,scrollbars=yes,left=30,top=30,status=yes");   
	   	//window.location = "${pageContext.request.contextPath}/payslip/viewPaySlip.do?payslipId="+payslipId 
	}
	
    function validateForm(actionName)
    {		
    	document.getElementById("wf_error").style.display = 'none';
    	document.getElementById("workFlowAction").value   = actionName;
    	if(!validation())
    		return false ;
    	if(actionName == "Forward" || actionName == "Approve")
    	{
	    	if(!validateWorkFlowApprover(actionName))
	    			return false ;
	    }
    	if(actionName=='Reject')
    	{
    		var confirMess;
     		if(document.getElementById('paySlipCurrentState').value=='Created' || document.getElementById('paySlipCurrentState').value=='Rejected')
     		{
     			confirMess="Do you want to cancel the payslip(s)?";
     		}	
     		else
     		{
     			confirMess="Do you want to reject the payslip(s)?";
     		}
    		if(confirm(confirMess))
    		{
    			return true;
    		}
    		else
    		{
    			return false;
    		}	
    	}	
    	
		return true;	    
    }
    
    function showError(msg)
    {
    	document.getElementById("wf_error").style.display = '';	
    	dom.get("wf_error").innerHTML = msg;
    	window.scroll(0,0);
    	return false;
    }
    
</script>    





</head>


<body onload="disableSubmit();populateDesignation();">
<div class="errorcss" id="wf_error" style="display:none;">
</div>
<s:form name="payslipApproveFrom" action ="payslipWorkflow"  theme="simple"><!-- /payslipapprove/approvePayslip-->
<div  align="center"><s:actionerror cssStyle="color:red;size:12;align:center" /></div>
 
<c:set var="payslipWfType" value="<%= payslipWfType %>" />
<c:if test="${billList != null}">
	<b>Bill Number :- </b>
</c:if>
<br/>
<c:forEach var="bill" items="${billList}">
	<a href="#" onclick='showBillInfo("${bill.id}")'>
		<c:out value="${bill.billnumber}" />
	</a>	
	<br>
</c:forEach>

<s:if test="%{hasErrors()}">
		<div class="errorcss" id="fieldError">
			<s:actionerror cssClass="errorcss"/>
			<s:fielderror cssClass="errorcss"/>
		</div>
</s:if>

<s:if test="%{hasActionMessages()}">
		<div class="errorcss">
				<s:actionmessage />
		</div>
</s:if>	

<s:hidden name="actionName" id="actionName" />
<s:hidden name="nextAction" id="nextAction" value="%{getNextAction()}"/>	
<s:hidden name="paySlipCurrentState" id="paySlipCurrentState" value="%{paySlipCurrentState}"/>
<div class="simple" style="font-size:13">
 <s:if test="%{groupedDeptName != null }">
     	<b>Department :</b> <s:property  value='%{groupedDeptName}' />
     	</s:if>
     	 <s:if test="%{groupedFundName != null }">
     	<b>Fund : </b><s:property  value='%{groupedFundName}' />
     	</s:if>
     	 <s:if test="%{groupedFunctionName != null }">
     	<b>Function : </b><s:property   value='%{groupedFunctionName}'  />
     	</s:if>
     	<s:if test="%{billNumber != null }">
     	<b>Bill Number : </b><s:property   value='%{billNumber}'  />
     	</s:if>
</div>
</br>
<s:if test='payslipSet!=null'>
 <div id="leftNaviProposal"   style="overflow-y:auto;overflow-x:auto  "> 	
 
  <display:table name="payslipSet" uid="currentRowObject"  class="simple"  export="false"  sort="list" requestURI="" >
  <display:column style="text-align:left" title="SlNo"><s:property value="#attr.currentRowObject_rowNum + (page-1)*pageSize" />
							</display:column>
     	<display:column  property="paySlip.employee.employeeCode" title="Employee Code " />
     	<display:column  title="Employee Name "  >
     		 <c:out value="${currentRowObject.paySlip.employee.employeeFirstName}"/>
     		 <c:out value="${currentRowObject.paySlip.employee.employeeMiddleName}"/>
     		 <c:out value="${currentRowObject.paySlip.employee.employeeLastName}"/>
     	 </display:column>
     	<display:column  property="paySlip.empAssignment.desigId.designationName" title="Designation "  />
     	
     	 <s:if test="%{groupedDeptName == null }">
     	<display:column  property="paySlip.empAssignment.deptId.deptName" title="Department "  />
     	</s:if>
     	 <s:if test="%{groupedFundName == null }">
     	<display:column  property="paySlip.empAssignment.fundId.name"  title="Fund " />
     	</s:if>
     	 <s:if test="%{groupedFunctionName == null }">
     	<display:column  property="paySlip.empAssignment.functionId.name" title="Function "  />
     	</s:if>
     	
     	<display:column style="text-align:right" format="{0,number,0}" property="paySlip.grossPay"  title="Gross pay &nbsp; " />
     	<display:column style="text-align:right" format="{0,number,0}" property="paySlip.totalDeductions" title=" Total Deduction &nbsp;"  />
     	<display:column style="text-align:right" format="{0,number,0}" property="paySlip.netPay"  title=" Net pay &nbsp;" />     	
     	<display:column style="text-align:center" title=" modify Payslips &nbsp; &nbsp;" >
     	<a href="#" onclick="showPayslipInfo(<s:property value='#attr.currentRowObject.paySlip.id' />)">modify payslip </a>
  		</display:column>
  	
		<display:column  title="Select All<input type='checkbox' id='selectAll' name='selectAll' onclick='checkAllSelected(this);' />">
  			<input type="checkbox" name="selectedPaySlips" value='<s:property  value="#attr.currentRowObject.paySlip.id"/>' />
		</display:column>  
		
		<display:column style="text-align:center" title=" Comments " >
     	<input type="textarea"  name="comments" cols="10" rows="2"  />
     	</display:column>
     	
     	
  		<display:footer >
			<tr>
				<td colspan="4" align="right"><b><bean:message key="grand.total"/></b></td>
				<td align="right">
					<b><s:property value="%{grossPayTotal}"/></b>
				</td>
				<td align="right">	      												
						<b><s:property value='%{deductionsTotal}'/></b>
				</td>
				<td align="right">
						<b><s:property value='%{netPayTotal}'/></b>
				</td>
			</tr>
	    </display:footer>
	    
   	</display:table>	
   		
  </div>  		
   		<%@ include file="payslipWorkflow-history.jsp" %>
   		
   	
   	<br/>
				<!--<s:submit name="submitButton" id="submitButton" value="submit" method="approveOrRejectPayslip" onclick="return validation();" /> onclick="return checkOnSubmit();" -->
	<div id="buttonDiv" align="center">		
	<s:if test="%{mode!='view'}">
	   	<div id="approverInfo">
	        <c:set var="approverHeadTDCSS" value="headingwk" scope="request" />
	        <c:set var="approverHeaderCss" value="headplacerlbl" scope="request"/>
	        <c:set var="headerImgCss" value="arrowiconwk" scope="request"/>
	        <c:set var="headerImgUrl" value="../common/image/arrow.gif" scope="request"/>
	        <c:set var="approverOddCSS" value="whiteboxwk" scope="request" />
	        <c:set var="approverOddTextCss" value="whitebox2wk" scope="request" />
	        <c:set var="approverEvenCSS" value="greyboxwk" scope="request" />
	        <c:set var="approverEvenTextCSS" value="greybox2wk" scope="request" />
	        <s:if test='%{getNextAction()!="Pending Approval By BC" || paySlipCurrentState=="Rejected"}'>
	   			<c:import url="/commons/commonWorkflow-payroll.jsp" context="/payroll" />
	   		</s:if>
		</div>
	</s:if>	
  	</div> 
   
   <br/>
   <div class="buttondiv" align="center">
   	<table cellpadding="0" cellspacing="0" border="0">
	    <tr>	      	
  			<s:if test="%{mode!='view'}">	             	 	  			
	  					<s:iterator value="%{getValidActions()}" var="p">
		  				<td><s:submit type="submit" cssClass="buttonfinal" value="%{p}" id="%{p}" name="%{p}" method="approveOrRejectPayslip" onclick=" return validateForm('%{p}');"/></td>
						</s:iterator>					
			</s:if>
			<td>&nbsp;&nbsp;</td>
	  			<td>	  			
	  				<input type="button" name="close" id="close" class="submitButton"  value="  Close  " onclick="window.close();"/>
	  			</td>  	
	   </tr>
    </table>
    </div>  
    
 </s:if>
 <s:if test="%{getNextAction()!='END'}">
	<div align="right" class="mandatory">* Mandatory Fields</div>
</s:if>	
</s:form>
</body>
</html>
