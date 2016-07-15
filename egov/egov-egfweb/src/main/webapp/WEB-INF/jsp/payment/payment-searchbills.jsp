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


<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<html>
<head>
<link rel="stylesheet"
	href="/EGF/resources/css/tabber.css?rnd=${app_release_no}"
	TYPE="text/css">
<script language="javascript"
	src="../resources/javascript/jsCommonMethods.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/tabber.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/tabber2.js?rnd=${app_release_no}"></script>
</head>
<script>

function validateTab(indexx)
{
	if(indexx==0)
	{
		document.getElementById('buttondiv').style.display='none';
		document.getElementById('paginationdiv').style.display='none';
	}
	else
	{
		document.getElementById('buttondiv').style.display='block';
		document.getElementById('paginationdiv').style.display='block';
	}
	return true;
}

var temp = window.setInterval(load,1);
function load()
{
	try{document.getElementById('tabber1').onclick(); window.clearInterval(temp);}catch(e){}
}

function checkMiscAttributes(obj)
{
	if(obj.checked)
	{
		var id = obj.id.substring(10,obj.id.length);
		var prefix = obj.name.substring(0, obj.name.indexOf("["));
		var fundName = prefix+"["+id+"].fundName";
		var functionName = prefix+"["+id+"].functionName";
		var deptName = prefix+"["+id+"].deptName";
		var functionaryName = prefix+"["+id+"].functionaryName";
		var fundsourceName = prefix+"["+id+"].fundsourceName";
		var schemeName = prefix+"["+id+"].schemeName";
		var subschemeName = prefix+"["+id+"].subschemeName";
		var fieldName = prefix+"["+id+"].fieldName";
		//var schemeName = prefix+"["+id+"].schemeName";
		var mis = '';
		if(document.getElementsByName(fundName) && document.getElementsByName(fundName).item(0) != null )
			mis = ( document.getElementsByName(fundName).item(0)).value;
		/* if(document.getElementsByName(functionName) && document.getElementsByName(functionName).item(0) != null )
			mis = ( document.getElementsByName(functionName).item(0)).value;
		if(document.getElementsByName(deptName) && document.getElementsByName(deptName).item(0) != null )
			mis = mis+'#'+( document.getElementsByName(deptName).item(0)).value; */
		if(document.getElementsByName(functionaryName) && document.getElementsByName(functionaryName).item(0) != null)
			mis = mis+'#'+( document.getElementsByName(functionaryName).item(0)).value;	
		if(document.getElementsByName(fundsourceName) && document.getElementsByName(fundsourceName).item(0) != null)
			mis = mis+'#'+( document.getElementsByName(fundsourceName).item(0)).value;	
		if(document.getElementsByName(schemeName) && document.getElementsByName(schemeName).item(0) != null)
			mis = mis+'#'+( document.getElementsByName(schemeName).item(0)).value;
		if(document.getElementsByName(subschemeName) && document.getElementsByName(subschemeName).item(0) != null)
			mis = mis+'#'+( document.getElementsByName(subschemeName).item(0)).value;
		if(document.getElementsByName(fieldName) && document.getElementsByName(fieldName).item(0) != null)
			mis = mis+'#'+( document.getElementsByName(fieldName).item(0)).value;

		if(document.getElementById('miscattributes').value=='')
			document.getElementById('miscattributes').value = mis;
		
		if(mis!=document.getElementById('miscattributes').value)
		{
			bootbox.alert('Selected bills do not have same attributes. Please select bills with same attributes');
			obj.checked =false;
			return;
			
		}
		document.getElementById('miscount').value=parseInt(document.getElementById('miscount').value)+1;		
	}
	else
	{
		document.getElementById('miscount').value=parseInt(document.getElementById('miscount').value)-1;
		if(document.getElementById('miscount').value==0)
			document.getElementById('miscattributes').value='';
	}
}
function check()                   
{

	
    
   
	var rtgsMode = document.getElementById("rtgsDefaultMode").value;
	//var restrictionDate = document.getElementById("paymentRestrictionDateForCJV").value;
	var restrictionDateForCJV = document.getElementById("rtgsModeRestrictionDateForCJV").value;
	if(rtgsMode =='Y' || rtgsMode=='Yes' || rtgsMode=='YES')
	{
		var length=0;
		var isAnyOneContratorBillChecked = false;
		length = <s:property value="%{contractorList.size()}"/>;
		for ( var i = 0; i < length; i++){
			if(document.getElementsByName('contractorList['+i+'].isSelected')[0].checked)
			{
				
				/*var billdt=document.getElementsByName('contractorList['+i+'].billDate')[0];          
				if( compareDate(restrictionDateForCJV,billdt) == 1){                                 
						isAnyOneContratorBillChecked = true;                    
						break;
				}
				*/
			}
		}             
		
		if(!document.getElementById("paymentModertgs").checked &&  isAnyOneContratorBillChecked)
		{
			bootbox.alert("Mode of payment for contractor bills should only be RTGS");
			return false;
		}	
       addSelectedToForm2();   
		document.form2.action='${pageContext.request.contextPath}/payment/payment-save.action';
		document.form2.submit();
	}	             
	if(document.getElementById('miscount').value==0)
	{
		bootbox.alert('Please select a bill before making the payment');
		return false;
	}
	if(document.getElementById('vouchermis.departmentid'))
		document.getElementById('vouchermis.departmentid').disabled=false;
    addSelectedToForm2();  
	document.form2.action='${pageContext.request.contextPath}/payment/payment-save.action';
	document.form2.submit();
	return true;
}
function loadBank(obj){}
function search()
{

	
	if(document.getElementById('vouchermis.departmentid'))
		document.getElementById('vouchermis.departmentid').disabled=false;
	var fund = document.getElementById('fundId').value;
	if(fund == "-1"){
		bootbox.alert("Please select fund");   
		return false;  
	}else{
		 document.getElementById("search").innerHTML="";
		document.getElementById("search").innerHTML=document.getElementById("searchtab").innerHTML;
		document.form2.action='${pageContext.request.contextPath}/payment/payment-search.action';
		document.form2.submit();
		
	}
	
}
function selectAllContractors(element){
	var length = 0;
	<s:if test="%{contractorList!=null}">
		length = <s:property value="%{contractorList.size()}"/>;
	</s:if>
	
	if(element.checked == true)	{
		var concnt=checkcontractorForSameMisAttribs('contractorList',length);
		if(concnt!=0){
		 bootbox.alert("Selected Bills doesnot have same attributes");
		  document.getElementById('conSelectAll').checked =false; }
		else
		checkAll('contractorList',length);
	}
	else
		uncheckAll('contractorList',length);
}
function selectAllSuppliers(element){
	var length = 0;
	<s:if test="%{supplierList!=null}">
		length = <s:property value="%{supplierList.size()}"/>;
	</s:if>
	if(element.checked == true)
	{
		var supcnt= checkSupplierForSameMisAttribs('supplierList',length);
	    if(supcnt!=0){
	     bootbox.alert("Selected Bills doesnot have same attributes");
	      document.getElementById('suppSelectAll').checked =false; }
	     else
	     checkAll('supplierList',length);
	}
	else
		uncheckAll('supplierList',length);
}
function selectAllContingent(element){
	var length = 0;
	<s:if test="%{contingentList!=null}">
		length = <s:property value="%{contingentList.size()}"/>;
	</s:if>
	
	if(element.checked == true){
		 
		 var expcnt=checkContingentForSameMisAttribs('contingentList',length);
		 if(expcnt!=0){
		  bootbox.alert("Selected Bills doesnot have same attributes" );
		  document.getElementById('expSelectAll').checked =false;    }
		  else
		  checkAll('contingentList',length); 
		}
	else
		uncheckAll('contingentList',length);
}

function checkAll(field,length){
	for (i = 0; i < length; i++){
		document.getElementsByName(field+'['+i+'].isSelected')[0].checked = true;
		document.getElementById('miscount').value=parseInt(document.getElementById('miscount').value)+1;
	}
}
function uncheckAll(field,length){
	for (i = 0; i < length; i++){
		document.getElementsByName(field+'['+i+'].isSelected')[0].checked = false;
		document.getElementById('miscount').value=parseInt(document.getElementById('miscount').value)-1;
	}
}
function checkcontractorForSameMisAttribs(obj,len)
{
		var fund1=document.getElementsByName(obj+"[0].fundName");
		var dept1=document.getElementsByName(obj+"[0].deptName");
		var function1=document.getElementsByName(obj+"[0].functionName");
		var scheme1=document.getElementsByName(obj+"[0].schemeName");
		var subscheme1=document.getElementsByName(obj+"[0].subschemeName");
		var fundsource1=document.getElementsByName(obj+"[0].fundsourceName");
		var field1=document.getElementsByName(obj+"[0].fieldName");
		var functionaryName1=document.getElementsByName(obj+"[0].functionaryName");
		var concount=0;
		for(i=0;i<len;i++)
		{
			 <s:if test="%{!isFieldMandatory('fund')}">
		   if((document.getElementsByName(obj+"["+i+"].fundName").item(0)).value!=null){
		   if(fund1[0].value != null && fund1[0].value !=(document.getElementsByName(obj+"["+i+"].fundName").item(0)).value) {
		   	document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false;
		  	concount++; break;}}
		  	</s:if>                     
		   
		   <s:if test="%{shouldShowHeaderField('department')}"> 
		   if((document.getElementsByName(obj+"["+i+"].deptName").item(0)).value!=null){  	
		   if(dept1[0].value != null &&  dept1[0].value !=(document.getElementsByName(obj+"["+i+"].deptName").item(0)).value) {
		   document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false;
		   concount++; break;}}
		   </s:if>
		   
		   <s:if test="%{shouldShowHeaderField('function')}"> 
		   if((document.getElementsByName(obj+"["+i+"].functionName").item(0)).value!=null){  	
		   if(function1[0].value != null &&  function1[0].value !=(document.getElementsByName(obj+"["+i+"].functionName").item(0)).value) {
		   document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false;
		   concount++; break;}}
		   </s:if>
		    
		     <s:if test="%{shouldShowHeaderField('functionary')}">
		    if(document.getElementsByName(obj+"["+i+"].functionaryName")!=null){
		    if(functionaryName1[0].value != null && functionaryName1[0].value != (document.getElementsByName(obj+"["+i+"].functionaryName").item(0)).value) {
		    document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false; 
		    concount++; break;}}
		    </s:if>
		   
		   <s:if test="%{shouldShowHeaderField('fundsource')}"> 
		   if((document.getElementsByName(obj+"["+i+"].fundsourceName").item(0)).value!=null){
		   if(fundsource1[0].value != null &&  fundsource1[0].value !=(document.getElementsByName(obj+"["+i+"].fundsourceName").item(0)).value) {
		   document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false; 
		   concount++; break;}}
		   </s:if>		   
		   
		   <s:if test="%{shouldShowHeaderField('scheme')}">
		   if((document.getElementsByName(obj+"["+i+"].schemeName").item(0)).value!=null){
		   if(scheme1[0].value != null  && scheme1[0].value !=( document.getElementsByName(obj+"["+i+"].schemeName").item(0)).value) {
		    document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false;
		   concount++; break;}}
		   </s:if>
		 
		   <s:if test="%{shouldShowHeaderField('subscheme')}">
		   if((document.getElementsByName(obj+"["+i+"].subschemeName").item(0)).value!=null){
		   if(subscheme1[0].value !=  null && subscheme1[0].value!=(document.getElementsByName(obj+"["+i+"].subschemeName").item(0)).value) {
		   document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false;  
		   concount++; break;}}
		   </s:if>		 
		   
		    <s:if test="%{shouldShowHeaderField('field')}">
		    if(document.getElementsByName(obj+"["+i+"].fieldName")!=null){
		    if(field1[0].value != null && field1[0].value != (document.getElementsByName(obj+"["+i+"].fieldName").item(0)).value) {
		    document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false; 
		    concount++; break;}}
		    </s:if>	  		  
		   }
		   return concount;
}

 function addSelectedToForm2()
{
 document.getElementById("exp2").innerHTML="";
var field='contingentList';
var length=<s:property value="%{contingentList.size()}"/>;
for (i = 0; i < length; i++){
  if(document.getElementsByName(field+'['+i+'].isSelected')[0].checked == true)
{
var k=document.getElementsByName(field+'['+i+'].isSelected')[0].parentNode.parentNode.innerHTML;
k="<tr>"+k+"</tr>";
console.log(k);
document.getElementById("exp2").innerHTML=document.getElementById("exp2").innerHTML+k;
console.log(document.getElementById("exp2").innerHTML);
}
}
 document.getElementById("sup2").innerHTML="";
 field='supplierList';
 length=<s:property value="%{supplierList.size()}"/>;
for (i = 0; i < length; i++){
  if(document.getElementsByName(field+'['+i+'].isSelected')[0].checked == true)
{
var k=document.getElementsByName(field+'['+i+'].isSelected')[0].parentNode.parentNode.innerHTML;
k="<tr>"+k+"</tr>"
document.getElementById("sup2").innerHTML=document.getElementById("sup2").innerHTML+k;
//console.log(document.getElementById("exp2").innerHTML);
}
}

 document.getElementById("con2").innerHTML="";
field='contractorList';
 length=<s:property value="%{contractorList.size()}"/>;
for (i = 0; i < length; i++){
  if(document.getElementsByName(field+'['+i+'].isSelected')[0].checked == true)
{
var k=document.getElementsByName(field+'['+i+'].isSelected')[0].parentNode.parentNode.innerHTML;
k="<tr>"+k+"</tr>"
document.getElementById("con2").innerHTML=document.getElementById("con2").innerHTML+k;
//console.log(document.getElementById("con2").innerHTML);
}
}

document.getElementById("search").innerHTML="";
document.getElementById("search").innerHTML=document.getElementById("searchtab").innerHTML;
console.log(document.getElementById("exp2").innerHTML);
} 
 
function checkSupplierForSameMisAttribs(obj,len)
{
		
		var fund1=document.getElementsByName(obj+"[0].fundName");
		var dept1=document.getElementsByName(obj+"[0].deptName");
		var function1=document.getElementsByName(obj+"[0].functionName");
		var scheme1=document.getElementsByName(obj+"[0].schemeName");
		var subscheme1=document.getElementsByName(obj+"[0].subschemeName");
		var fundsource1=document.getElementsByName(obj+"[0].fundsourceName");
		var field1=document.getElementsByName(obj+"[0].fieldName");
		var functionaryName1=document.getElementsByName(obj+"[0].functionaryName");
		var suppcount=0;
		for(i=0;i<len;i++)
		{
		   	 <s:if test="%{!isFieldMandatory('fund')}">
		     if((document.getElementsByName(obj+"["+i+"].fundName").item(0)).value!=null){
		    if(fund1[0].value != null && fund1[0].value !=(document.getElementsByName(obj+"["+i+"].fundName").item(0)).value) {
		   	document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false;
		  	suppcount++; break;}}
		  	 </s:if>		   
		   
		   <s:if test="%{shouldShowHeaderField('department')}"> 
		    if((document.getElementsByName(obj+"["+i+"].deptName").item(0)).value!=null){ 	
		   if(dept1[0].value != null && dept1[0].value !=(document.getElementsByName(obj+"["+i+"].deptName").item(0)).value) {
		    document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false;
		   suppcount++; break;}}
		   </s:if>
		   
		   <s:if test="%{shouldShowHeaderField('function')}"> 
		    if((document.getElementsByName(obj+"["+i+"].functionName").item(0)).value!=null){   	
		   if(function1[0].value != null && function1[0].value !=(document.getElementsByName(obj+"["+i+"].functionName").item(0)).value) {
		   document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false;
		   suppcount++; break;}}
		   </s:if>
		  
		   <s:if test="%{shouldShowHeaderField('functionary')}">
		   if(document.getElementsByName(obj+"["+i+"].functionaryName")!=null){
		   if(functionaryName1[0].value != null && functionaryName1[0].value != (document.getElementsByName(obj+"["+i+"].functionaryName").item(0)).value) {
		   document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false; 
		   suppcount++; break;}}
		    </s:if>
		   
		   <s:if test="%{shouldShowHeaderField('fundsource')}"> 
		    if((document.getElementsByName(obj+"["+i+"].fundsourceName").item(0)).value!=null){
		   if(fundsource1[0].value != null && fundsource1[0].value !=(document.getElementsByName(obj+"["+i+"].fundsourceName").item(0)).value) {
		   document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false; 
		   suppcount++; break;}}
		   </s:if>
		  
		  	<s:if test="%{shouldShowHeaderField('subscheme')}">
		  	if((document.getElementsByName(obj+"["+i+"].schemeName").item(0)).value!=null){
		    if(scheme1[0].value != null && scheme1[0].value !=( document.getElementsByName(obj+"["+i+"].schemeName").item(0)).value) {
		     document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false;
		    suppcount++; break;}}
		    </s:if>
		 
		 	<s:if test="%{shouldShowHeaderField('subscheme')}">
		 	if((document.getElementsByName(obj+"["+i+"].subschemeName").item(0)).value!=null){	 
		   if(subscheme1[0].value !=  null && subscheme1[0].value!=(document.getElementsByName(obj+"["+i+"].subschemeName").item(0)).value) {
		   document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false;  
		   suppcount++; break;}}
		   </s:if>
		 
		 	<s:if test="%{shouldShowHeaderField('field')}">
		    if(document.getElementsByName(obj+"["+i+"].fieldName")!=null){
		    if(field1[0].value != null && field1[0].value != document.getElementsByName(obj+"["+i+"].fieldName")[0].value) {
		    document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false; 
		    suppcount++; break;}}
		    </s:if>
		   			  
		   }
		   return suppcount;
}
 
function checkContingentForSameMisAttribs(obj,len)
{
   
		var fund1=document.getElementsByName(obj+"[0].fundName");
		var dept1=document.getElementsByName(obj+"[0].deptName");
		var function1=document.getElementsByName(obj+"[0].functionName");
		var scheme1=document.getElementsByName(obj+"[0].schemeName");
		var subscheme1=document.getElementsByName(obj+"[0].subschemeName");
		var fundsource1=document.getElementsByName(obj+"[0].fundsourceName");
		var field1=document.getElementsByName(obj+"[0].fieldName");
		var functionaryName1=document.getElementsByName(obj+"[0].functionaryName");
		var expcount=0;
		for(i=0;i<len;i++)
		{
		 <s:if test="%{!isFieldMandatory('fund')}">
		 if((document.getElementsByName(obj+"["+i+"].fundName").item(0)).value!=null){
		    if(fund1[0].value != null && fund1[0].value !=(document.getElementsByName(obj+"["+i+"].fundName").item(0)).value) {
		   	document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false;
		  	expcount++; break;}}
		  	</s:if>
		   
		   <s:if test="%{shouldShowHeaderField('department')}"> 
		    if((document.getElementsByName(obj+"["+i+"].deptName").item(0)).value!=null){   	
		   if(dept1[0].value != null && dept1[0].value !=(document.getElementsByName(obj+"["+i+"].deptName").item(0)).value) {
		   document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false;
		   expcount++; break;}}
		   </s:if>

		   <s:if test="%{shouldShowHeaderField('function')}"> 
		    if((document.getElementsByName(obj+"["+i+"].functionName").item(0)).value!=null){   	
		   if(function1[0].value != null && function1[0].value !=(document.getElementsByName(obj+"["+i+"].functionName").item(0)).value) {
		   document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false;
		   expcount++; break;}}
		   </s:if>
		   
		    <s:if test="%{shouldShowHeaderField('functionary')}">
		   if(document.getElementsByName(obj+"["+i+"].functionaryName")!=null){	  
		   if(functionaryName1[0].value != null && functionaryName1[0].value != (document.getElementsByName(obj+"["+i+"].functionaryName").item(0)).value) {
		    document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false; 
		   expcount++; break; }}
		   </s:if>
		   
		   <s:if test="%{shouldShowHeaderField('fundsource')}">
		   if((document.getElementsByName(obj+"["+i+"].fundsourceName").item(0)).value!=null){		 
		   if(fundsource1[0].value != null && fundsource1[0].value !=(document.getElementsByName(obj+"["+i+"].fundsourceName").item(0)).value) {
		   document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false; 
		   expcount++; break;}}
		   </s:if>
		   
		   <s:if test="%{shouldShowHeaderField('scheme')}">
		   if((document.getElementsByName(obj+"["+i+"].schemeName").item(0)).value!=null){
		   if(scheme1[0].value != null && scheme1[0].value !=( document.getElementsByName(obj+"["+i+"].schemeName").item(0)).value) {
		   document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false;
		   expcount++; break;}}
		   </s:if>
		 
		   <s:if test="%{shouldShowHeaderField('subscheme')}">
		   if((document.getElementsByName(obj+"["+i+"].subschemeName").item(0)).value!=null){
		   if(subscheme1[0].value !=  null && subscheme1[0].value!=(document.getElementsByName(obj+"["+i+"].subschemeName").item(0)).value) {
		   document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false;  
		   expcount++; break;}}
		    </s:if>
		 
		   <s:if test="%{shouldShowHeaderField('field')}">
		    if(document.getElementsByName(obj+"["+i+"].fieldName")!=null){
		    if(field1[0].value != null && field1[0].value != (document.getElementsByName(obj+"["+i+"].fieldName").item(0)).value) {
		    document.getElementsByName(obj+"["+i+"].isSelected")[0].checked = false; 
		    expcount++; break;}}
		     </s:if>	  
		   }
		   return expcount;
}
/*function defaultModeOfPayment()
{
	var rtgsMode = document.getElementById("rtgsDefaultMode").value;
	if(rtgsMode =='Y' || rtgsMode=='Yes' || rtgsMode=='YES')
	{
		document.getElementById("paymentModertgs").checked=true;
		document.getElementById("paymentModecheque").checked=false;
	}
}*/

</script>

</head>
<body>


	<s:form action="payment" theme="simple">

		<div class="formmainbox">
			<s:token />
			<jsp:include page="../budget/budgetHeader.jsp">
				<jsp:param name="heading" value="Bill Payment Search" />
			</jsp:include>
			<span class="mandatory1"> <s:actionerror /> <s:fielderror />
				<s:actionmessage />
			</span>
			<div class="subheadnew">Bill Payment</div>
			<div id="budgetSearchGrid" style="display: block; width: 100%;">
				<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td>
							<div align="left">
								<br />
								<table border="0" cellspacing="0" cellpadding="0" width="100%">
									<tr>
										<td>
											<div class="tabber">
												<div class="tabbertab" id="searchtab">
													<h2>Search Bill</h2>
													<span>
														<table width="100%" border="0" cellspacing="0"
															cellpadding="0">
															<tr>
																<td colspan="6">
																	<div class="subheadsmallnew" style="border: 0;">Search
																		Bill</div>
																</td>
															</tr>
															<tr>
																<td class="bluebox"></td>
																<td class="bluebox"><s:text
																		name="payment.billnumber" /></td>
																<td class="bluebox"><s:textfield name="billNumber"
																		id="billNumber" maxlength="25" value="%{billNumber}" /></td>
																<td class="bluebox"></td>
																<td class="bluebox"></td>
															</tr>
															<tr>
																<td class="bluebox"></td>
																<td class="greybox"><s:text
																		name="payment.billdatefrom" /></td>
																<td class="greybox"><s:textfield id="fromDate"
																		name="fromDate" value="%{fromDate}"
																		data-date-end-date="0d"
																		onkeyup="DateFormat(this,this.value,event,false,'3')"
																		placeholder="DD/MM/YYYY"
																		class="form-control datepicker"
																		data-inputmask="'mask': 'd/m/y'" /></td>
																<td class="greybox"><s:text
																		name="payment.billdateto" /></td>
																<td class="greybox"><s:textfield id="toDate"
																		name="toDate" value="%{toDate}"
																		data-date-end-date="0d"
																		onkeyup="DateFormat(this,this.value,event,false,'3')"
																		placeholder="DD/MM/YYYY"
																		class="form-control datepicker"
																		data-inputmask="'mask': 'd/m/y'" /></td>
															</tr>
															<tr>
																<td class="bluebox"></td>
																<td class="bluebox"><s:text
																		name="payment.expendituretype" /></td>
																<td class="bluebox"><s:select name="expType"
																		id="expType"
																		list="#{'-1':'---Select---','Purchase':'Purchase','Works':'Works', 'Expense':'Expense'}"
																		value="%{expType}" /></td>
																<td class="bluebox"></td>
																<td class="bluebox"></td>
															</tr>
															<jsp:include page="../payment/paymenttrans-filter.jsp" />
															<tr>
																<td align="center" colspan="5">
																	<div class="buttonbottom">
																		<input type="button" method="search" value="Search"
																			Class="button" onclick="return search()" />
																		<input type="button" value="Close"
																			onclick="javascript:window.close()" class="button" />
																		<s:hidden name="miscount" id="miscount" />
																		<s:hidden name="miscattributes" id="miscattributes"
																			value="" />
																		<s:hidden name="rtgsDefaultMode" id="rtgsDefaultMode" />
																		<s:hidden name="rtgsModeRestrictionDateForCJV"
																			id="rtgsModeRestrictionDateForCJV" />
																		<s:hidden name="paymentRestrictionDateForCJV"
																			id="paymentRestrictionDateForCJV" />
																	</div>
																</td>
															</tr>
														</table>
													</span>
												</div>
												<div class="tabbertab" id="contractortab">
													<h2>Contractor Bill</h2>
													<span>
														<table name="contractortable" align="center" border="0"
															cellpadding="0" cellspacing="0" class="newtable">
															<tr>
																<td colspan="6"><div class="subheadsmallnew">Contractor
																		Bill</div></td>
															</tr>
															<tr>
																<td colspan="6">
																	<div style="float: left; width: 100%;">
																		<table align="left" border="0" cellpadding="0"
																			cellspacing="0" width="100%">
																			<tr>
																				<th class="bluebgheadtdnew">Select<input
																					type="checkbox" name="conSelectAll"
																					id="conSelectAll"
																					onclick="selectAllContractors(this)" /> </checkbox></th>

																				<jsp:include page="billdetails-header.jsp" />
																				<s:iterator var="p" value="contractorList"
																					status="s">
																			</tr>
																			<tr>

																				<td class="blueborderfortdnew"><s:hidden
																						name="contractorList[%{#s.index}].csBillId"
																						id="csBillId%{#s.index}" value="%{csBillId}" /> <s:checkbox
																						name="contractorList[%{#s.index}].isSelected"
																						id="isSelected%{#s.index}"
																						onclick="checkMiscAttributes(this)"></s:checkbox></td>
																				<td align="left" class="blueborderfortdnew" />
																				<s:property value="#s.index+1" />
																				</td>
																				<td align="left" class="blueborderfortdnew"><s:hidden
																						name="contractorList[%{#s.index}].expType"
																						id="expType%{#s.index}" value="%{expType}" /> <s:hidden
																						name="contractorList[%{#s.index}].billNumber"
																						id="billNumber%{#s.index}" value="%{billNumber}" />
																					<s:property value="%{billNumber}" /></td>
																				<td class="blueborderfortdnew"><s:hidden
																						name="contractorList[%{#s.index}].billDate"
																						id="billDate%{#s.index}" value="%{billDate}" /> <s:date
																						name="%{billDate}" format="dd/MM/yyyy" /></td>

																				<td align="left" class="blueborderfortdnew"><s:hidden
																						name="contractorList[%{#s.index}].billVoucherNumber"
																						id="billVoucherNumber%{#s.index}"
																						value="%{billVoucherNumber}" /> <s:property
																						value="%{billVoucherNumber}" /></td>
																				<td style="text-align: left"
																					class="blueborderfortdnew"><s:hidden
																						name="contractorList[%{#s.index}].billVoucherDate"
																						id="billVoucherDate%{#s.index}"
																						value="%{billVoucherDate}" /> <s:date
																						name="%{billVoucherDate}" format="dd/MM/yyyy" /></td>

																				<td align="left" class="blueborderfortdnew"><s:hidden
																						name="contractorList[%{#s.index}].payTo"
																						id="payTo%{#s.index}" value="%{payTo}" /> <s:property
																						value="%{payTo}" /></td>
																				<td style="text-align: right"
																					class="blueborderfortdnew"><s:hidden
																						name="contractorList[%{#s.index}].netAmt"
																						id="netAmt%{#s.index}" value="%{netAmt}" /> <s:text
																						name="payment.format.number">
																						<s:param value="%{netAmt}" />
																					</s:text></td>
																				<td style="text-align: right"
																					class="blueborderfortdnew"><s:hidden
																						name="contractorList[%{#s.index}].earlierPaymentAmt"
																						id="earlierPaymentAmt%{#s.index}"
																						value="%{earlierPaymentAmt}" /> <s:text
																						name="payment.format.number">
																						<s:param value="%{earlierPaymentAmt}" />
																					</s:text></td>
																				<td style="text-align: right"
																					class="blueborderfortdnew"><s:hidden
																						name="contractorList[%{#s.index}].payableAmt"
																						id="payableAmt%{#s.index}" value="%{payableAmt}" />
																					<s:hidden
																						name="contractorList[%{#s.index}].paymentAmt"
																						id="paymentAmt%{#s.index}" value="%{paymentAmt}" />
																					<s:text name="payment.format.number">
																						<s:param value="%{payableAmt}" />
																					</s:text></td>
																				<s:if test="%{!isFieldMandatory('fund')}">
																					<td class="blueborderfortdnew"
																						id="fund<s:property value="#s.index"/>"><s:hidden
																							name="contractorList[%{#s.index}].fundName"
																							id="fundName%{#s.index}" value="%{fundName}" />
																						<s:property value="%{fundName}" /></td>
																				</s:if>
																				<s:if test="%{shouldShowHeaderField('department')}">
																					<td class="blueborderfortdnew"
																						id="dept<s:property value="#s.index"/>"><s:hidden
																							name="contractorList[%{#s.index}].deptName"
																							id="deptName%{#s.index}" value="%{deptName}" />
																						<s:property value="%{deptName}" /></td>
																				</s:if>
																				<s:if test="%{shouldShowHeaderField('function')}">
																					<td class="blueborderfortdnew"
																						id="function<s:property value="#s.index"/>"><s:hidden
																							name="contractorList[%{#s.index}].functionName"
																							id="functionName%{#s.index}"
																							value="%{functionName}" /> <s:property
																							value="%{functionName}" /></td>
																				</s:if>
																				<s:if test="%{shouldShowHeaderField('functionary')}">
																					<td class="blueborderfortdnew"
																						id="functionary<s:property value="#s.index"/>"><s:hidden
																							name="contractorList[%{#s.index}].functionaryName"
																							id="functionaryName%{#s.index}"
																							value="%{functionaryName}" /> <s:property
																							value="%{functionaryName}" /></td>
																				</s:if>
																				<s:if test="%{shouldShowHeaderField('fundsource')}">
																					<td class="blueborderfortdnew"
																						id="fundsource<s:property value="#s.index"/>"><s:hidden
																							name="contractorList[%{#s.index}].fundsourceName"
																							id="fundsourceName%{#s.index}"
																							value="%{fundsourceName}" /> <s:property
																							value="%{fundsourceName}" /></td>
																				</s:if>
																				<s:if test="%{shouldShowHeaderField('scheme')}">
																					<td class="blueborderfortdnew"
																						id="scheme<s:property value="#s.index"/>"><s:hidden
																							name="contractorList[%{#s.index}].schemeName"
																							id="schemeName%{#s.index}" value="%{schemeName}" />
																						<s:property value="%{schemeName}" /></td>
																				</s:if>
																				<s:if test="%{shouldShowHeaderField('subscheme')}">
																					<td class="blueborderfortdnew"
																						id="subscheme<s:property value="#s.index"/>"><s:hidden
																							name="contractorList[%{#s.index}].subschemeName"
																							id="subschemeName%{#s.index}"
																							value="%{subschemeName}" /> <s:property
																							value="%{subschemeName}" /></td>
																				</s:if>
																				<s:if test="%{shouldShowHeaderField('field')}">
																					<td class="blueborderfortdnew"
																						id="field<s:property value="#s.index"/>"><s:hidden
																							name="contractorList[%{#s.index}].fieldName"
																							id="fieldName%{#s.index}" value="%{fieldName}" />
																						<s:property value="%{fieldName}" /></td>
																				</s:if>
																			</tr>
																			</s:iterator>
																		</table>
																		<s:if
																			test="contractorList == null || contractorList.size==0">
																			<div class="subheadsmallnew" style="border: 0;">No
																				Records Found</div>
																		</s:if>
																	</div>
																</td>
															</tr>
														</table>
													</span>
												</div>
												<div class="tabbertab" id="suppliertab">
													<h2>Supplier Bill</h2>
													<span>
														<table align="center" border="0" cellpadding="0"
															cellspacing="0" class="newtable" name="supSelectAll"
															id="supSelectAll">
															<tr>
																<td colspan="6"><div class="subheadsmallnew">Supplier
																		Bill</div></td>
															</tr>
															<tr>
																<td colspan="6">
																	<div style="float: left; width: 100%;">
																		<table align="center" border="0" cellpadding="0"
																			cellspacing="0" width="100%">
																			<tr>
																				<th class="bluebgheadtdnew">Select<input
																					type="checkbox" onclick="selectAllSuppliers(this)" />
																					</checkbox></th>

																				<jsp:include page="billdetails-header.jsp" />
																				<s:iterator var="p" value="supplierList" status="s">
																			</tr>
																			<tr>
																				<td class="blueborderfortdnew"><s:hidden
																						name="supplierList[%{#s.index}].csBillId"
																						id="csBillId%{#s.index}" value="%{csBillId}" /> <s:checkbox
																						name="supplierList[%{#s.index}].isSelected"
																						id="isSelected%{#s.index}"
																						onclick="checkMiscAttributes(this)"></s:checkbox></td>
																				<td align="left" class="blueborderfortdnew" />
																				<s:property value="#s.index+1" />
																				</td>
																				<td align="left" class="blueborderfortdnew"><s:hidden
																						name="supplierList[%{#s.index}].expType"
																						id="expType%{#s.index}" value="%{expType}" /> <s:hidden
																						name="supplierList[%{#s.index}].billNumber"
																						id="billNumber%{#s.index}" value="%{billNumber}" />
																					<s:property value="%{billNumber}" /></td>
																				<td class="blueborderfortdnew"><s:hidden
																						name="supplierList[%{#s.index}].billDate"
																						id="billDate%{#s.index}" value="%{billDate}" /> <s:date
																						name="%{billDate}" format="dd/MM/yyyy" /></td>

																				<td align="left" class="blueborderfortdnew"><s:hidden
																						name="supplierList[%{#s.index}].billVoucherNumber"
																						id="billVoucherNumber%{#s.index}"
																						value="%{billVoucherNumber}" /> <s:property
																						value="%{billVoucherNumber}" /></td>
																				<td style="text-align: left"
																					class="blueborderfortdnew"><s:hidden
																						name="supplierList[%{#s.index}].billVoucherDate"
																						id="billVoucherDate%{#s.index}"
																						value="%{billVoucherDate}" /> <s:date
																						name="%{billVoucherDate}" format="dd/MM/yyyy" /></td>

																				<td align="left" class="blueborderfortdnew"><s:hidden
																						name="supplierList[%{#s.index}].payTo"
																						id="payTo%{#s.index}" value="%{payTo}" /> <s:property
																						value="%{payTo}" /></td>
																				<td style="text-align: right"
																					class="blueborderfortdnew"><s:hidden
																						name="supplierList[%{#s.index}].netAmt"
																						id="netAmt%{#s.index}" value="%{netAmt}" /> <s:text
																						name="payment.format.number">
																						<s:param value="%{netAmt}" />
																					</s:text></td>
																				<td style="text-align: right"
																					class="blueborderfortdnew"><s:hidden
																						name="supplierList[%{#s.index}].earlierPaymentAmt"
																						id="earlierPaymentAmt%{#s.index}"
																						value="%{earlierPaymentAmt}" /> <s:text
																						name="payment.format.number">
																						<s:param value="%{earlierPaymentAmt}" />
																					</s:text></td>
																				<td style="text-align: right"
																					class="blueborderfortdnew"><s:hidden
																						name="supplierList[%{#s.index}].payableAmt"
																						id="payableAmt%{#s.index}" value="%{payableAmt}" />
																					<s:hidden
																						name="supplierList[%{#s.index}].paymentAmt"
																						id="paymentAmt%{#s.index}" value="%{paymentAmt}" />
																					<s:text name="payment.format.number">
																						<s:param value="%{payableAmt}" />
																					</s:text></td>
																				<s:if test="%{!isFieldMandatory('fund')}">
																					<td class="blueborderfortdnew"
																						id="fund<s:property value="#s.index"/>"><s:hidden
																							name="supplierList[%{#s.index}].fundName"
																							id="fundName%{#s.index}" value="%{fundName}" />
																						<s:property value="%{fundName}" /></td>
																				</s:if>
																				<s:if test="%{shouldShowHeaderField('department')}">
																					<td class="blueborderfortdnew"
																						id="dept<s:property value="#s.index"/>"><s:hidden
																							name="supplierList[%{#s.index}].deptName"
																							id="deptName%{#s.index}" value="%{deptName}" />
																						<s:property value="%{deptName}" /></td>
																				</s:if>
																				<s:if test="%{shouldShowHeaderField('function')}">
																					<td class="blueborderfortdnew"
																						id="function<s:property value="#s.index"/>"><s:hidden
																							name="supplierList[%{#s.index}].functionName"
																							id="functionName%{#s.index}"
																							value="%{functionName}" /> <s:property
																							value="%{functionName}" /></td>
																				</s:if>
																				<s:if test="%{shouldShowHeaderField('functionary')}">
																					<td class="blueborderfortdnew"
																						id="functionary<s:property value="#s.index"/>"><s:hidden
																							name="supplierList[%{#s.index}].functionaryName"
																							id="functionaryName%{#s.index}"
																							value="%{functionaryName}" /> <s:property
																							value="%{functionaryName}" /></td>
																				</s:if>
																				<s:if test="%{shouldShowHeaderField('fundsource')}">
																					<td class="blueborderfortdnew"
																						id="fundsource<s:property value="#s.index"/>"><s:hidden
																							name="supplierList[%{#s.index}].fundsourceName"
																							id="fundsourceName%{#s.index}"
																							value="%{fundsourceName}" /> <s:property
																							value="%{fundsourceName}" /></td>
																				</s:if>
																				<s:if test="%{shouldShowHeaderField('scheme')}">
																					<td class="blueborderfortdnew"
																						id="scheme<s:property value="#s.index"/>"><s:hidden
																							name="supplierList[%{#s.index}].schemeName"
																							id="schemeName%{#s.index}" value="%{schemeName}" />
																						<s:property value="%{schemeName}" /></td>
																				</s:if>
																				<s:if test="%{shouldShowHeaderField('subscheme')}">
																					<td class="blueborderfortdnew"
																						id="subscheme<s:property value="#s.index"/>"><s:hidden
																							name="supplierList[%{#s.index}].subschemeName"
																							id="subschemeName%{#s.index}"
																							value="%{subschemeName}" /> <s:property
																							value="%{subschemeName}" /></td>
																				</s:if>
																				<s:if test="%{shouldShowHeaderField('field')}">
																					<td class="blueborderfortdnew"
																						id="field<s:property value="#s.index"/>"><s:hidden
																							name="supplierList[%{#s.index}].fieldName"
																							id="fieldName%{#s.index}" value="%{fieldName}" />
																						<s:property value="%{fieldName}" /></td>
																				</s:if>
																			</tr>
																			</s:iterator>
																		</table>
																		<s:if
																			test="supplierList == null || supplierList.size==0">
																			<div class="subheadsmallnew" style="border: 0;">No
																				Records Found</div>
																		</s:if>
																	</div>
																</td>
															</tr>
														</table>
													</span>
												</div>
												<div class="tabbertab" id="cbilltab">
													<h2>Expense Bill</h2>
													<span>
														<table align="center" border="0" cellpadding="0"
															cellspacing="0" class="newtable" name="expSelectAll">
															<tr>
																<td colspan="6"><div class="subheadsmallnew">Expense
																		Bill</div></td>
															</tr>
															<tr>
																<td colspan="6">
																	<div style="float: left; width: 100%;">
																		<table align="center" border="0" cellpadding="0"
																			cellspacing="0" width="100%">
																			<tr>
																				<th class="bluebgheadtdnew">Select<input
																					type="checkbox" id="expSelectAll"
																					onclick="selectAllContingent(this)" /> </checkbox></th>
																				<jsp:include page="billdetails-header.jsp" />
																				<s:iterator var="p" value="contingentList"
																					status="s">
																			</tr>
																			<tr>

																				<td class="blueborderfortdnew"><s:hidden
																						name="contingentList[%{#s.index}].csBillId"
																						id="csBillId%{#s.index}" value="%{csBillId}" /> <s:checkbox
																						name="contingentList[%{#s.index}].isSelected"
																						id="isSelected%{#s.index}"
																						onclick="checkMiscAttributes(this)"></s:checkbox></td>
																				<td align="left" class="blueborderfortdnew" />
																				<s:property value="#s.index+1" />
																				</td>
																				<!-- <td align="left"  class="blueborderfortdnew"/><s:hidden  name="contingentList[%{#s.index}].csBillId" id="csBillId%{#s.index}" value="%{csBillId}"/><s:property value="#s.index" /> </td> -->
																				<td align="left" class="blueborderfortdnew"><s:hidden
																						name="contingentList[%{#s.index}].expType"
																						id="expType%{#s.index}" value="%{expType}" /> <s:hidden
																						name="contingentList[%{#s.index}].billNumber"
																						id="billNumber%{#s.index}" value="%{billNumber}" />
																					<s:property value="%{billNumber}" /></td>
																				<td class="blueborderfortdnew"><s:hidden
																						name="contingentList[%{#s.index}].billDate"
																						id="billDate%{#s.index}" value="%{billDate}" /> <s:date
																						name="%{billDate}" format="dd/MM/yyyy" /></td>

																				<td align="left" class="blueborderfortdnew"><s:hidden
																						name="contingentList[%{#s.index}].billVoucherNumber"
																						id="billVoucherNumber%{#s.index}"
																						value="%{billVoucherNumber}" /> <s:property
																						value="%{billVoucherNumber}" /></td>
																				<td style="text-align: left"
																					class="blueborderfortdnew"><s:hidden
																						name="contingentList[%{#s.index}].billVoucherDate"
																						id="billVoucherDate%{#s.index}"
																						value="%{billVoucherDate}" /> <s:date
																						name="%{billVoucherDate}" format="dd/MM/yyyy" /></td>

																				<td align="left" class="blueborderfortdnew"><s:hidden
																						name="contingentList[%{#s.index}].payTo"
																						id="payTo%{#s.index}" value="%{payTo}" /> <s:property
																						value="%{payTo}" /></td>
																				<td style="text-align: right"
																					class="blueborderfortdnew"><s:hidden
																						name="contingentList[%{#s.index}].netAmt"
																						id="netAmt%{#s.index}" value="%{netAmt}" /> <s:text
																						name="payment.format.number">
																						<s:param value="%{netAmt}" />
																					</s:text></td>
																				<td style="text-align: right"
																					class="blueborderfortdnew"><s:hidden
																						name="contingentList[%{#s.index}].earlierPaymentAmt"
																						id="earlierPaymentAmt%{#s.index}"
																						value="%{earlierPaymentAmt}" /> <s:text
																						name="payment.format.number">
																						<s:param value="%{earlierPaymentAmt}" />
																					</s:text></td>
																				<td style="text-align: right"
																					class="blueborderfortdnew"><s:hidden
																						name="contingentList[%{#s.index}].payableAmt"
																						id="payableAmt%{#s.index}" value="%{payableAmt}" />
																					<s:hidden
																						name="contingentList[%{#s.index}].paymentAmt"
																						id="paymentAmt%{#s.index}" value="%{paymentAmt}" />
																					<s:text name="payment.format.number">
																						<s:param value="%{payableAmt}" />
																					</s:text></td>
																				<s:if test="%{!isFieldMandatory('fund')}">
																					<td class="blueborderfortdnew"
																						id="fund<s:property value="#s.index"/>"><s:hidden
																							name="contingentList[%{#s.index}].fundName"
																							id="fundName%{#s.index}" value="%{fundName}" />
																						<s:property value="%{fundName}" /></td>
																				</s:if>
																				<s:if test="%{shouldShowHeaderField('department')}">
																					<td class="blueborderfortdnew"
																						id="dept<s:property value="#s.index"/>"><s:hidden
																							name="contingentList[%{#s.index}].deptName"
																							id="deptName%{#s.index}" value="%{deptName}" />
																						<s:property value="%{deptName}" /></td>
																				</s:if>
																				<s:if test="%{shouldShowHeaderField('functionary')}">
																					<td class="blueborderfortdnew"
																						id="functionary<s:property value="#s.index"/>"><s:hidden
																							name="contingentList[%{#s.index}].functionaryName"
																							id="functionaryName%{#s.index}"
																							value="%{functionaryName}" /> <s:property
																							value="%{functionaryName}" /></td>
																				</s:if>
																				<s:if test="%{shouldShowHeaderField('function')}">
																					<td class="blueborderfortdnew"
																						id="function<s:property value="#s.index"/>"><s:hidden
																							name="contingentList[%{#s.index}].functionName"
																							id="functionName%{#s.index}"
																							value="%{functionName}" /> <s:property
																							value="%{functionName}" /></td>
																				</s:if>
																				<s:if test="%{shouldShowHeaderField('fundsource')}">
																					<td class="blueborderfortdnew"
																						id="fundsource<s:property value="#s.index"/>"><s:hidden
																							name="contingentList[%{#s.index}].fundsourceName"
																							id="fundsourceName%{#s.index}"
																							value="%{fundsourceName}" /> <s:property
																							value="%{fundsourceName}" /></td>
																				</s:if>
																				<s:if test="%{shouldShowHeaderField('scheme')}">
																					<td class="blueborderfortdnew"
																						id="scheme<s:property value="#s.index"/>"><s:hidden
																							name="contingentList[%{#s.index}].schemeName"
																							id="schemeName%{#s.index}" value="%{schemeName}" />
																						<s:property value="%{schemeName}" /></td>
																				</s:if>
																				<s:if test="%{shouldShowHeaderField('subscheme')}">
																					<td class="blueborderfortdnew"
																						id="subscheme<s:property value="#s.index"/>"><s:hidden
																							name="contingentList[%{#s.index}].subschemeName"
																							id="subschemeName%{#s.index}"
																							value="%{subschemeName}" /> <s:property
																							value="%{subschemeName}" /></td>
																				</s:if>
																				<s:if test="%{shouldShowHeaderField('field')}">
																					<td class="blueborderfortdnew"
																						id="field<s:property value="#s.index"/>"><s:hidden
																							name="contingentList[%{#s.index}].fieldName"
																							id="fieldName%{#s.index}" value="%{fieldName}" />
																						<s:property value="%{fieldName}" /></td>
																				</s:if>
																			</tr>
																			</s:iterator>
																		</table>
																		<s:if
																			test="contingentList == null || contingentList.size==0">
																			<div class="subheadsmallnew" style="border: 0;">No
																				Records Found</div>
																		</s:if>
																	</div>
																</td>
															</tr>
														</table>
													</span>
												</div>
												<!-- individual tab div -->
											</div> <!-- tabber div -->
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
				</table>
			</div>
			<div id="paginationdiv" align="center" style="padding-top: 10px;">
				<!-- <a href="#"><<</a> <a href="#">1</a> <a href="#">2</a> <a href="#">3</a> <a href="#">>></a>&nbsp;&nbsp;&nbsp;
		<select name="select">
			<option>Show 50</option>
			<option>Show 100</option>
			<option>Show All</option>
		</select> -->
			</div>
			<s:if test="%{!validateUser('createpayment')}">
				<script>
			document.getElementById('searchBtn').disabled=true;
			document.getElementById('errorSpan').innerHTML='<s:text name="payment.invalid.user"/>';
			if(document.getElementById('vouchermis.departmentid'))
			{
				var d = document.getElementById('vouchermis.departmentid');
				d.options[d.selectedIndex].text='----Choose----';
				d.options[d.selectedIndex].text.value=-1;
			}
		</script>
			</s:if>
			<s:if test="%{validateUser('deptcheck')}">
				<script>
				if(document.getElementById('vouchermis.departmentid'))
				{
					document.getElementById('vouchermis.departmentid').disabled=true;
				}
			</script>
			</s:if>
		</div>
	</s:form>
<form action="payment" id="form2" name="form2" method="POST" >
<div id="search" style="display:visible"></div>
<table id="con2" style="display:visible">
</table>
<table id="sup2" style="display:hidden">
</table>

<table id="exp2" style="display:hidden" >
</table>
<div id="buttondiv" align="center" style="display: visible">
				<table align="center" width="100%">
					<tr>
						<font size="small" color="red">*Maximum of 500 records are
							displayed here<br>*You can select Maximum of 125 bills for single payment</font>
					</tr>
					<tr>
						<td class="modeofpayment"><strong><s:text
									name="payment.mode" /><span class="mandatory1">*</span></strong> <input
							name="paymentMode" id="paymentModecheque" checked="checked"
							value="cheque" type="radio"><label
							for="paymentModecheque">Cheque</label> <input name="paymentMode"
							id="paymentModecash" value="cash" type="radio"><label
							for="paymentModecash"><s:text
									name="cash.consolidated.cheque" /></label> <input name="paymentMode"
							id="paymentModertgs" value="rtgs" type="radio"><label
							for="paymentModertgs">RTGS</label></td>
					</tr>

			
<tr>
						<td class="buttonbottomnew" align="center"><br> <input
							type="button" class="buttonsubmit" value="Generate Payment"
							id="generatePayment" onclick="return check();" /></td>
					</tr>
</table>
</div>
</form>

</body>
</html>