<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>

<%@ include file="/includes/taglibs.jsp" %>
<link rel="stylesheet" type="text/css" href="<egov:url path='/yui/assets/skins/sam/autocomplete.css'/>" />
<head>
	<title><s:text name="searchOnlineReceipts.title"/></title>
<script>

function onBodyLoad(){
	var cnt=document.getElementsByName('selectedReceipts');
	var paymentstatus=document.getElementsByName('paymentstatus');
	var statuscode=document.getElementsByName('statusCode');
	var txId=document.getElementsByName('transactionId');
	var txDate=document.getElementsByName('transactionDate');
	var remarks=document.getElementsByName('remarks');
	var el = document.forms[0].elements;
	
	for (i = 0; i < cnt.length; i++){
		if(paymentstatus[i].value=='Success' || paymentstatus[i].value=='Failure' || paymentstatus[i].value=='Refunded'){  
		    statuscode[i].setAttribute('disabled',true);
		    txId[i].setAttribute('disabled',true);
		    txDate[i].setAttribute('disabled',true);
		    remarks[i].setAttribute('disabled',true);
		}
		else if(paymentstatus[i].value=='Failure' || paymentstatus[i].value=='Pending'){
		txDate[i].value="";
		}
	}
}

function validate()
{
	var fromdate=document.getElementById('fromDate').value;
	var todate=document.getElementById('toDate').value;
	var valSuccess = true;
	if(fromdate!="" && todate!="" && fromdate!=todate)
	{
		if(!checkFdateTdate(fromdate,todate))
		{
			//dom.get("comparedatemessage").style.display="block";
			document.getElementById("comparedatemessage").style.display = 'block';
			window.scroll(0,0);
			valSuccess= false;
		}
	}
	else
	{
		document.getElementById("comparedatemessage").style.display = 'none';
		valSuccess= true;
	}
	return valSuccess;
}

function transitionStates(){
    document.getElementById("onlinepayt_error_area").innerHTML="";
    document.getElementById("invaliddateformat").style.display = "none";
    var cnt=document.getElementsByName('selectedReceipts');
	var paymentstatus=document.getElementsByName('paymentstatus');
	var statuscode=document.getElementsByName('statusCode');
	var txId=document.getElementsByName('transactionId');
	var txDate=document.getElementsByName('transactionDate');
	var valErrorMsg1="";
	var valErrorMsg2="";
	var valErrorMsg3="";
	var valErrorMsg4="";
	var valErrorMsg5="";
	var validation=true;
	var selected=false;
	
	for (i = 0; i < cnt.length; i++){
	    if(paymentstatus[i].value=='To Be Refunded'){
	    	if(!(statuscode[i].value==-1 || statuscode[i].value=='ONLINE_STATUS_SUCCESS' || statuscode[i].value=='ONLINE_STATUS_REFUNDED')){
	    		if(valErrorMsg1==""){
	    		    valErrorMsg1='<s:text name="onlinereceipt.manualrecon.toberefunded.errormsg" />' + '<br>';
	    		}
	    	    // dom.get("stateerror"+(i+1)).style.display='';
	    	    document.getElementById("stateerror"+(i+1)).style.display = "block";
	    	    validation=false;
	    	}
	    	else{
	    		document.getElementById("stateerror"+(i+1)).style.display = "none";
	    	}
	    }
	    if(paymentstatus[i].value=='Refunded'){
	    	if(statuscode[i].value!=-1){
	    		if(valErrorMsg2==""){
	    		    valErrorMsg2='<s:text name="onlinereceipt.manualrecon.refunded.errormsg" />' + '<br>';
	    		}
	    		//dom.get("stateerror"+(i+1)).style.display='';
	    		document.getElementById("stateerror"+(i+1)).style.display = "block";
	    		validation=false;
	       }
	       else{
	       		document.getElementById("stateerror"+(i+1)).style.display = "none";
	       }
	    }
	    if(paymentstatus[i].value=='Pending'){
	    	if(!(statuscode[i].value==-1 || statuscode[i].value=='ONLINE_STATUS_SUCCESS' || 
	    	    statuscode[i].value=='ONLINE_STATUS_REFUNDED' || 
	    	    statuscode[i].value=='TO_BE_REFUNDED')){
	    			if(valErrorMsg3==""){ 
	    		    	valErrorMsg3='<s:text name="onlinereceipt.manualrecon.pending.errormsg" />' + '<br>';
	    			}
	    			//dom.get("stateerror"+(i+1)).style.display='';
	    			document.getElementById("stateerror"+(i+1)).style.display = "block";
	    			validation=false;
	    	}
	    	else{
	       		document.getElementById("stateerror"+(i+1)).style.display = "none";
	       }
	    }
	    
	    //validations on moving a payment to Success state.
	    if(statuscode[i].value=='ONLINE_STATUS_SUCCESS'){
	        if(txId[i].value==null || txId[i].value==""){
	            if(valErrorMsg4==""){ 
	                valErrorMsg4='<s:text name="onlinereceipt.manualrecon.transactionid.mandatory.errormsg" />' + '<br>';
	            }
	        	//dom.get("transactionIdError"+(i+1)).style.display=''; 
	        	document.getElementById("transactionIdError"+(i+1)).style.display = "block";
	            validation=false;
	        }
	        else{
	            document.getElementById("transactionIdError"+(i+1)).style.display = "none";
	        }
	        if(txDate[i].value==null || txDate[i].value==""){
	            if(valErrorMsg5==""){ 
	                valErrorMsg5='<s:text name="onlinereceipt.manualrecon.transactiondate.mandatory.errormsg" />' + '<br>';
	            }
	        	//dom.get("transactionDateError"+(i+1)).style.display=''; 
	        	document.getElementById("transactionDateError"+(i+1)).style.display = "block";
	            validation=false;
	        }
	        else{
	            document.getElementById("transactionDateError"+(i+1)).style.display = "none";
	        }
	    }
	    
	    if(statuscode[i].value!=-1){
	        selected=true;
	    }
	}
	
	document.getElementById("onlinepayt_error_area").innerHTML+=valErrorMsg1 + valErrorMsg2 + valErrorMsg3 + valErrorMsg4 + valErrorMsg5;
	
	if(selected==false){ 
	    document.getElementById("onlinepayt_error_area").innerHTML+='<s:text name="onlinereceipt.manualrecon.no.transaction.selected.errormsg" />' + '<br>';
	    validation=false;
	}
	
	if(validation==false){
	    /**
	    Internet Explorer 7- (and some minor browsers) cannot set values for style, class or event handlers. - 
	    through dom
	    **/
		//dom.get('onlinepayt_error_area').style.display="block";
		document.getElementById("onlinepayt_error_area").setAttribute('style','block');
		window.scroll(0,0);
		return validation;
	}
	
	var remarks=document.getElementsByName('remarks');
	
	//if validation is true, enable the disabled fields so that they are available in the action
	for (i = 0; i < cnt.length; i++){
		    txId[i].disabled=false;
		    txDate[i].disabled=false;
		    statuscode[i].disabled=false;
		    remarks[i].disabled=false;
	}
	
	document.searchOnlineReceiptForm.action="onlineReceipt-reconcileOnlinePayment.action";
	document.searchOnlineReceiptForm.submit();
}

</script> 
</head>
<body onLoad="onBodyLoad();" >

<span align="center" style="display:none" id="comparedatemessage">
  <li>
     <font size="2" color="red"><b>
		<s:text name="common.comparedate.errormessage"/>
	</b></font>
  </li>
</span>
<s:form theme="simple" name="searchOnlineReceiptForm" action="searchOnlineReceipt-search">
<div class="formmainbox"><div class="subheadnew"><s:text name="searchOnlineReceipts.title"/>
</div>
<div class="subheadsmallnew"><span class="subheadnew"><s:text name="searchOnlineReceipts.criteria"/></span></div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">

	    <tr>
	      <td width="4%" class="bluebox2">&nbsp;</td>
	      <td width="21%" class="bluebox2"><s:text name="searchOnlineReceipts.criteria.servicetype"/></td>
	      <td width="24%" class="bluebox2"><s:select headerKey="-1" headerValue="%{getText('searchOnlineReceipts.servicetype.select')}" name="serviceTypeId" id="serviceType" cssClass="selectwk" list="dropdownData.serviceTypeList" listKey="id" listValue="name" value="%{serviceTypeId}" /> </td>
	      <td width="21%" class="bluebox2">&nbsp;</td>
	      <td width="30%" class="bluebox2">&nbsp;</td>
	    </tr>
	     <tr>
	      <td width="4%" class="bluebox">&nbsp;</td>
	      <td width="21%" class="bluebox"><s:text name="searchOnlineReceipts.criteria.fromdate"/></td>
		  <s:date name="fromDate" var="cdFormat" format="dd/MM/yyyy"/>
		  <td width="24%" class="bluebox"><s:textfield id="fromDate" name="fromDate" value="%{cdFormat}" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('forms[0].fromDate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"  ><img src="/collection/resources/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" /></a><div class="highlight2" style="width:80px">DD/MM/YYYY</div></td>
	      <td width="21%" class="bluebox"><s:text name="searchOnlineReceipts.criteria.todate"/></td>
	      <s:date name="toDate" var="cdFormat1" format="dd/MM/yyyy"/>
		  <td width="30%" class="bluebox"><s:textfield id="toDate" name="toDate" value="%{cdFormat1}" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')"/><a href="javascript:show_calendar('forms[0].toDate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true;"  ><img src="/collection/resources/images/calendaricon.gif" alt="Date" width="18" height="18" border="0" align="absmiddle" /></a><div class="highlight2" style="width:80px">DD/MM/YYYY</div></td>
	    </tr>
	    <tr>
	      <td width="4%" class="bluebox2">&nbsp;</td>
	      <td width="21%" class="bluebox2"><s:text name="searchOnlineReceipts.criteria.referenceid"/></td>
	      <td width="24%" class="bluebox2"><s:textfield id="referenceId" type="text" name="referenceId"/></td>
	      <td width="21%" class="bluebox2"><s:text name="searchOnlineReceipts.transaction.status"/></td>
	      <td width="30%" class="bluebox2"><s:select id="searchTransactionStatus" name="searchTransactionStatus" headerKey="-1" headerValue="%{getText('searchOnlineReceipts.default.transaction.status')}" cssClass="selectwk" list="%{onlineReceiptStatuses}" value="%{searchTransactionStatus}" listKey="id" listValue="description" /> </td>
	    </tr>	    
	    </table>
	    <br/>
</div>
    <div class="buttonbottom">
      <label><s:submit type="submit" cssClass="buttonsubmit" id="button" value="Search" onclick="return validate();"/></label>&nbsp;
      <label><s:submit type="submit" cssClass="button" value="Reset" onclick="document.searchOnlineReceiptForm.action='searchOnlineReceipt-reset.action'"/></label>&nbsp;
      <s:if test="%{results.isEmpty()}">
      	<input name="closebutton" type="button" class="button" id="closebutton" value="Close" onclick="window.close();"/>
      </s:if>
      
</div>

<div class="errorstyle" id="onlinepayt_error_area" name="onlinepayt_error_area" style="display:none;"></div>
<span align="center" style="display:none" id="invaliddateformat">
  <li>
     <font size="2" color="red"><b>
		<s:text name="common.dateformat.errormessage"/>
	</b></font>
  </li>
</span>
<s:if test="%{!results.isEmpty()}">
<div style="overflow:auto; margin-left: 8px;margin-right: 8px;">
<div align="center">		

<display:table name="results" uid="currentRow" pagesize = "30" style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;" cellpadding="0" cellspacing="0" export="false" requestURI="">

	<display:caption media="pdf">&nbsp;</display:caption>
	
	<display:column headerClass="bluebgheadtd"  class="blueborderfortd" style="width:3%">
		<input name="selectedReceipts" type="hidden" id="selectedReceipts" value="${currentRow.receiptHeader.id}"/>
		<input type="hidden" name="paymentstatus" id="paymentstatus" value="${currentRow.status.description}" />
	</display:column>
	<display:column headerClass="bluebgheadtd"  class="blueborderfortd" title="SI.No" style="width:3%;text-align: center"> <c:out value="${currentRow_rowNum}"/> </display:column> 
	<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Bill Number" property="receiptHeader.referencenumber"  format="{0,date,dd/MM/yyyy}" style="width:6%;text-align: center" />
	<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Reference ID" property="receiptHeader.id" style="width:8%;text-align:center"  />
	
	<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Department" property="receiptHeader.receiptMisc.department.name" style="width:8%;text-align:center"  />
	<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Amount (Rs.)" property="receiptHeader.totalAmount" style="width:4%;text-align: center" format="{0, number, #,##0.00}" />
	<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Service Type" property="receiptHeader.service.name" style="width:10%; text-align: right" />
	<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Status" property="status.description" style="width:8%" />  
	<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Change Status" style="width:8%">
		<s:select name="statusCode" id="statusCode" cssClass="selectwk" headerKey="-1" headerValue="%{getText('searchOnlineReceipts.default.transition.transaction.status')}" 
     		 list="%{onlineReceiptTransitionStatuses}" listKey="code" listValue="description" value="%{code}" />
     	<span id="stateerror${currentRow_rowNum}" style='display:none;color:red;font-weight:bold'>&nbsp;x</span>     		
	</display:column> 
	<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Transaction ID" style="width:5%">
		<input type="text" name="transactionId" id="transactionId" style='width:100px;' value="${currentRow.transactionNumber}" />
		<span id="transactionIdError${currentRow_rowNum}" style='display:none;color:red;font-weight:bold'>&nbsp;x</span> 
	</display:column>
	<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Transaction Date(dd/mm/yyyy)" style="width:5%;text-align:center" >
	
	
	<fmt:formatDate value="${currentRow.transactionDate}" pattern="dd/MM/yyyy" var="trnDate"/>
		<input type="text" name="transactionDate" id="transactionDate" styleId="transactionDate" style='width:100px;text-align:center;'  value='<c:out value="${trnDate}"/> ' onblur="waterMarkTextOut('transactionDate','DD/MM/YYYY');validateDateFormat(this)" onkeyup="DateFormat(this,this.value,event,false,'3')" onfocus = "waterMarkTextIn('transactionDate','DD/MM/YYYY');" />
		<span id="transactionDateError${currentRow_rowNum}" style='display:none;color:red;font-weight:bold'>&nbsp;x</span>
	</display:column>
	<display:column headerClass="bluebgheadtd" class="blueborderfortd" title="Remarks" style="width:5%">
	   <input type="text" name="remarks" id="remarks" value="${currentRow.remarks}" />
	</display:column>
	
</display:table>	
</div>
<br/>
<div class="buttonbottom">
  <input name="button32" type="button" class="buttonsubmit" id="button32" value="Submit" onclick="return transitionStates()" />
  <input name="button32" type="button" class="button" id="button32" value="Close" onclick="window.close();"/>
</div>
</div>	
				
</s:if>
    <s:if test="%{results.isEmpty()}">
	<s:if test="target=='searchresult'">
	
		<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="tablebottom">
		<tr> 
			<div>&nbsp;</div>
			<div class="subheadnew"><s:text name="searchresult.norecord"/></div>
		</tr>
		</table>
	
	</s:if>
</s:if>
</s:form>
<script type="text/javascript">
onBodyLoad();
</script>
</body>

	
