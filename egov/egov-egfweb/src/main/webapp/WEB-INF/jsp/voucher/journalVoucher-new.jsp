#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ page language="java"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>

<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/voucherHelper.js"></script>
<script type="text/javascript" src="/EGF/commonjs/ajaxCommonFunctions.js"></script>
<script type="text/javascript" src="/EGF/javascript/calender.js"></script>
<script type="text/javascript" src="/EGF/script/calendar.js" ></script>
<script type="text/javascript" src="/EGF/javascript/dateValidation.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Journal voucher Create</title>
</head>
   	
<body onload="loadDropDownCodes();loadDropDownCodesFunction();onloadtask()">

<s:form action="journalVoucher" theme="simple" name="jvcreateform" >
<s:token/>
<div id="loading" style="position:absolute; left:25%; top:70%; padding:2px; z-index:20001; height:auto;width:500px;display: none;">
    <div class="loading-indicator" style="background:white;  color:#444; font:bold 13px tohoma,arial,helvetica; padding:10px; margin:0; height:auto;">
        <img src="/EGF/images/loading.gif" width="32" height="32" style="margin-right:8px;vertical-align:top;"/> Loading...
    </div>
</div>
			<jsp:include page="../budget/budgetHeader.jsp">
        		<jsp:param name="heading" value="Journal voucher Create" />
			</jsp:include>
			
			<span class="mandatory">
			<font  style='color: red ; font-weight:bold '> 
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage /></font>
			</span>
		<div class="formmainbox"><div class="formheading"/><div class="subheadnew">Journal Voucher</div>
		<div id="listid" style="display:block">
		<br/>
<div align="center">
<font  style='color: red ; font-weight:bold '> 
<p class="error-block" id="lblError" ></p></font>

	<table border="0" width="100%">
	<tr>
		<s:if test="%{shouldShowHeaderField('vouchernumber')}">
			<td class="greybox"><s:text name="voucher.number"/><span class="mandatory">*</span></td>
			<td class="greybox"><s:textfield name="voucherNumber" id="voucherNumber" maxlength="30" /></td>
		</s:if>
			<td class="greybox"><s:text name="voucher.date"/><span class="mandatory">*</span></td>
			<td class="greybox"><s:date name="voucherDate" id="voucherDateId" format="dd/MM/yyyy"/>
			<s:textfield name="voucherDate" id="voucherDate" value="%{voucherDateId}"  maxlength="10" onkeyup="DateFormat(this,this.value,event,false,'3')"/>
			<a href="javascript:show_calendar('jvcreateform.voucherDate',null,null,'DD/MM/YYYY');" style="text-decoration:none">&nbsp;<img tabIndex=-1 src="${pageContext.request.contextPath}/image/calendaricon.gif" border="0"/></a>(dd/mm/yyyy)
			</td>
		<s:else>
			<td class="greybox">&nbsp;</td>
			<td class="greybox">&nbsp;</td>
		</s:else>	
	</tr>
	<jsp:include page="loadYIDataTable.jsp"/>
	
	<jsp:include page="voucherSubType.jsp"/>
	<jsp:include page="vouchertrans-filter.jsp"/>
	
		
		<tr>
			<td class="greybox"><s:text name="voucher.narration" /></td>
			<td class="greybox" colspan="3"><s:textarea  id="narration" name="description" style="width:580px" onblur="checkVoucherNarrationLen(this)"/></td>
		</tr>	
	</tr>
	</table>
	</div>
	<br/>
	<div id="labelAD" align="center">
	 		<table width="80%" border=0 id="labelid"><th>Account Details</th></table>
	</div>
	<div class="yui-skin-sam" align="center">
       <div id="billDetailTable"></div>
     </div>
     <script>
		
		makeVoucherDetailTable();
		document.getElementById('billDetailTable').getElementsByTagName('table')[0].width="80%"
	 </script>
	 <div id="codescontainer"></div>
	 <br/>
	 	<div id="labelSL" align="center">
	 		<table width="80%" border=0 id="labelid"><th>Sub-Ledger Details</th></table>
	 	</div>
	 	
		<div class="yui-skin-sam" align="center">
	       <div id="subLedgerTable"></div>
	     </div>
		<script>
			
			makeSubLedgerTable();
			
			document.getElementById('subLedgerTable').getElementsByTagName('table')[0].width="80%"
		</script>
		<br/>
		<div class="subheadsmallnew"/></div>
		<div class="mandatory" align="left">* Mandatory Fields</div>
		<s:if test='%{! wfitemstate.equalsIgnoreCase("END")}'>
			<%@include file="voucherWorkflow.jsp"%>         
		</s:if>	
	  	<div align="center">
		<table border="0" width="100%">
			<tr>
				<td  class="bluebox">Comments</td> 
				<td  class="bluebox" ><s:textarea name="comments" id="comments" cols="150" rows="3" onblur="checkLength(this)"/></td>
				<td>  
			</tr>
			<br/>
		</table> 
		</div> 
		<div  class="buttonbottom" id="buttondiv">                   
			<s:iterator value="%{getValidActions('')}" var="p">
				<s:if test="%{description !='Cancel'}">
					<s:submit type="submit" cssClass="buttonsubmit" value="%{description}" id="%{name}" name="%{name}" method="create" onclick="return validateJV('save','%{name}','%{description}')"/>
				</s:if>
			</s:iterator>	
			<input type="reset" id="Reset" value="Cancel" class="buttonsubmit"/>
			<input type="button" value="Close" onclick="javascript:window.close()" class="button" />        
		</div>
	<!-- 		
	<div class="buttonbottom" style="padding-bottom:10px;">
		<s:submit type="submit" cssClass="buttonsubmit" value="Save & Close" id="save&close" name="save&close" method="create" onclick="return validateJV('close')"/> 
   		<input type="reset" id="Reset" value="Cancel" class="buttonsubmit"/>
		<input type="button" value="Close" onclick="javascript:window.close()" class="button" />
	</div>  -->
	<br/>
	</div>
</div>
</div>

<div id="codescontainer"></div>
<s:hidden  name="actionName" id="actionName"/>
<input type="hidden" id="voucherTypeBean.voucherName" name="voucherTypeBean.voucherName" value="${voucherTypeBean.voucherName}"/>
<input type="hidden" id="voucherTypeBean.voucherType" name="voucherTypeBean.voucherType" value="Journal Voucher"/>
<input type="hidden" id="voucherTypeBean.voucherNumType" name="voucherTypeBean.voucherNumType" value="${voucherTypeBean.voucherNumType}" />
<input type="hidden" id="voucherTypeBean.cgnType" name="voucherTypeBean.cgnType" value="JVG"/>
<input type="hidden" id="worksVoucherRestrictedDate" name="worksVoucherRestrictedDate" value="${worksVoucherRestrictedDate}"/>

<input type="hidden" id="buttonValue" name="buttonValue" />

</s:form>

<s:hidden name="targetvalue" value="%{target}" id="targetvalue"/>
<s:hidden name="functionValue"  id="functionValue"/>
<s:hidden name="functionId"  id="functionId"/>
		<script>
		
			if(dom.get('targetvalue').value=='success')
			{
				document.getElementById('voucherDate').value=""; 
				if(document.getElementById('voucherNumber')){
					document.getElementById('voucherNumber').value="";
				}
				document.getElementById('narration').value="";
				if(document.getElementById('fundId')){
					document.getElementById('fundId').value=-1;
				}
				if(document.getElementById('vouchermis.function')){   
					document.getElementById('vouchermis.function').value=-1;
				}
				if(document.getElementById('vouchermis.departmentid')){
					document.getElementById('vouchermis.departmentid').value=-1;
				}   
				if(document.getElementById('schemeid')){
					document.getElementById('schemeid').value=-1;
				}
				if(document.getElementById('subschemeid')){
					document.getElementById('subschemeid').value=-1;
				}
				if(document.getElementById('vouchermis.functionary')){
					document.getElementById('vouchermis.functionary').value=-1;
				}
				if(document.getElementById('fundsourceId')){
					document.getElementById('fundsourceId').value=-1;
				}
				if(document.getElementById('vouchermis.divisionid')){
					document.getElementById('vouchermis.divisionid').value=-1;
				}
			}	
			function validateApproverUser(name,value){
				//alert("action name"+name);     
				document.getElementById("actionName").value= name;
				//alert("button value"+value);  
				<s:if test='%{! wfitemstate.equalsIgnoreCase("END")}'>
					if(!validateUser(name,value)){ return false; }
				</s:if>
				return true;
			}
	function validateJV(btnval,name,value)
	{
	 // alert("inside validate jv");  
	   document.getElementById("buttonValue").value=btnval;
		document.getElementById('lblError').innerHTML ="";
		var cDate = new Date();
		
		var currDate = cDate.getDate()+"/"+(parseInt(cDate.getMonth())+1)+"/"+cDate.getYear();
		var vhDate=document.getElementById('voucherDate').value;
		var VhType= document.getElementById('vType').value;
		var typeDate=document.getElementById('worksVoucherRestrictedDate').value;
		var restrictionDate = typeDate.split(",");  //worksVoucherRestrictedDate have  billtype and restrictiondate as comma separated values.
			//alert("Hi vType->"+VhType);

		if(vhDate == '' )	{
			document.getElementById('lblError').innerHTML = "Please enter a voucher date ";
			document.getElementById('voucherDate').focus();
			return false;
		}
	
		var vVoucherSubType = document.getElementById('vType').value;
		if(vVoucherSubType != 'JVGeneral' && vVoucherSubType != '-1' )	{
			if(document.getElementById('voucherTypeBean.partyName').value == '' ) {
				document.getElementById('lblError').innerHTML = "Please enter a Party Name ";
				document.getElementById('voucherTypeBean.partyName').focus();
				return false;
			}
		}
		if(VhType=='Works'){
			var chkd1=vhDate.split('/');
			var chkd2=restrictionDate[1].split('/');
			var voucherDt=new Date(chkd1[2],chkd1[1]-1,chkd1[0]);
			var restrictionDt=new Date(chkd2[2],chkd2[1]-1,chkd2[0]);

			//alert("-----restrictionDt--------"+restrictionDt);
			//alert("-----voucherDt--------"+voucherDt);
			if(voucherDt>=restrictionDt){
				alert(" Cannot Create Works JV for Date to greater than "+restrictionDate[1]);
				return false;
			}
		}
		
	// Javascript validation of the MIS Manadate attributes.
		<s:if test="%{isFieldMandatory('vouchernumber')}"> 
			 if(null != document.getElementById('voucherNumber') && document.getElementById('voucherNumber').value.trim().length == 0 ){

				document.getElementById('lblError').innerHTML = "Please enter a voucher number";
				return false;
			 }
		 </s:if>
		 <s:if test="%{isFieldMandatory('voucherdate')}"> 
				 if(null != document.getElementById('voucherDate') && document.getElementById('voucherDate').value.trim().length == 0){

					document.getElementById('lblError').innerHTML = "Please enter a voucher date";
					return false;
				 }
			 </s:if>    
		 	<s:if test="%{isFieldMandatory('fund')}"> 
				 if(null != document.getElementById('fundId') && document.getElementById('fundId').value == -1){
					document.getElementById('lblError').innerHTML = "Please Select a fund";
					return false;
				 }    
			 </s:if>   
			 <s:if test="%{isFieldMandatory('function')}">                        
			 if(null != document.getElementById('vouchermis.function') && document.getElementById('vouchermis.function').value == -1){

				document.getElementById('lblError').innerHTML = "Please Select a function";
				return false;
			 }
		 </s:if>
			<s:if test="%{isFieldMandatory('department')}"> 
				 if(null!= document.getElementById('vouchermis.departmentid') && document.getElementById('vouchermis.departmentid').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a department";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('scheme')}"> 
				 if(null!=document.getElementById('schemeid') &&  document.getElementById('schemeid').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a scheme";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('subscheme')}"> 
				 if(null!= document.getElementById('subschemeid') && document.getElementById('subschemeid').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a subscheme";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('functionary')}"> 
				 if(null!=document.getElementById('vouchermis.functionary') &&  document.getElementById('vouchermis.functionary').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a functionary";
					return false;
				 }
			</s:if>
			<s:if test="%{isFieldMandatory('fundsource')}"> 
				 if(null !=document.getElementById('fundsourceId') &&  document.getElementById('fundsourceId').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a fundsource";
					return false;
				}
			</s:if>
			<s:if test="%{isFieldMandatory('field')}"> 
				 if(null!= document.getElementById('vouchermis.divisionid') && document.getElementById('vouchermis.divisionid').value == -1){

					document.getElementById('lblError').innerHTML = "Please select a field";
					return false;
				 }
			</s:if>
			result =validateApproverUser(name,value);
	return result;
}function loadBank(fund){
	}
function onloadtask(){
//autocompleteEntities1By20();

	var currentTime = new Date()
	var month = currentTime.getMonth() + 1
	var day = currentTime.getDate()
	var year = currentTime.getFullYear()
	if(document.getElementById('voucherDate').value  =="")  
		document.getElementById('voucherDate').value = day + "/" + month + "/" + year ;
	var VTypeFromBean = '<s:property value="voucherTypeBean.voucherSubType"/>';
	if(VTypeFromBean == "") 
		VTypeFromBean = '-1';
	document.getElementById('vType').value = VTypeFromBean;
	if('<s:property value="voucherTypeBean.voucherSubType"/>' == 'JVGeneral' || '<s:property value="voucherTypeBean.voucherSubType"/>'== ""){
		document.getElementById('voucherTypeBean.partyBillNum').readOnly=true;
		document.getElementById('voucherTypeBean.partyName').readOnly=true;
		document.getElementById('partyBillDate').readOnly=true;
		document.getElementById('voucherTypeBean.billNum').readOnly=true;
		document.getElementById('billDate').readOnly=true;
	}
	document.getElementById('vouchermis.function').style.display="none";
	document.getElementById('functionnametext').style.display="none";
	var message = '<s:property value="message"/>';
	if(message != null && message != '')
		showMessage(message);
	<s:if test="%{voucherTypeBean.voucherNumType == null}">
		document.getElementById('voucherTypeBean.voucherNumType').value ="Journal";
	</s:if>
	<s:if test="%{voucherTypeBean.voucherName == null}">
		document.getElementById('voucherTypeBean.voucherName').value ="JVGeneral";
	</s:if>
	<s:if test="%{voucherTypeBean.voucherSubType == null}">
		document.getElementById('voucherTypeBean.voucherSubType').value = "JVGeneral";
	</s:if>
	if(message == null || message == '')
		populateslDropDown(); // to load the subledger detils when page loads, required when validation fails.	
  }
function showMessage(message){
	var buttonValue = '<s:property value="buttonValue"/>';
	for(var i=0;i<document.forms[0].length;i++)
	{
		if( document.forms[0].elements[i].id!='Close')
		document.forms[0].elements[i].disabled =true;
	} 
	alert(message);
	var voucherHeaderId = '<s:property value="voucherHeader.id"/>';
	document.forms[0].action = "${pageContext.request.contextPath}/voucher/preApprovedVoucher!loadvoucherview.action?vhid="+voucherHeaderId;
	document.forms[0].submit();      
	
}

function printJV()
{		
		var voucherHeaderId = '<s:property value="voucherHeader.id"/>';
		window.location="${pageContext.request.contextPath}/voucher/journalVoucherPrint!print.action?id="+voucherHeaderId;		
		//document.forms[0].submit();
}

</script>
</body>

</html>
