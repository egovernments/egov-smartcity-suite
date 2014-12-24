<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<html>  
<head> 
 <script type="text/javascript" src="/EGF/javascript/fundFlow.js"></script>
    <title><s:text name="fundflowreport"/></title>
    
  
    <script type="text/javascript">
    function onloadFundFlow()
    {
    //alert("onload");
	<s:if test="receiptList!=null && receiptList.size()>0">
	var alertMsg='<s:text name="lastReportGenDateAlert"/>'+'  '+'<s:property value="openignBalanceCalculatedDate"/>';    
	alert(alertMsg);
    calculateFunds(document.getElementById('receiptList[0].openingBalance'));
    //alert("onload2");
    </s:if>
    <s:if test="paymentList!=null && paymentList.size()>0">
    calculateFundsForPayment(document.getElementById('paymentList[0].openingBalance'));
    </s:if>
    }
    function validateFundFlow()
    {
    
    /*if(document.getElementById("fund").value=="")
    {
     alert("Select Fund");
     return false;
     }*/
     if(document.getElementById("asOnDate").value=="")
     {
     alert("Select Date");
     return false;
     }
     return true;
    }
function   alertTheMessage()
    {
    var alrtmsg='<s:text name="fundflow.recalculate.alert"/>';
   	return confirm(alrtmsg);
    
 }
   

</script>
    
</head>
<body onload="onloadFundFlow()">  
	<div class="subheadnew">Fund Flow Analysis Report</div>
	<s:form  name="fundFlowReport" action="fundFlow" theme="simple">
	<%@include file="fundFlow-form.jsp" %>	

	<s:if test="(receiptList!=null && receiptList.size()>0) ||( paymentList!=null && paymentList.size()>0) ">


<div class="buttonbottom">
	<s:submit value="Save" method="create" cssClass="buttonsubmit" />
	<s:reset name="button" type="submit" cssClass="button" id="button" value="Cancel"/>
	<s:submit value="Close" onclick="javascript: self.close()" cssClass="button"/>
</div>	
</s:if>	
<s:token/>
 	</s:form>                   
 </body>
 </html>
 
      
      
      