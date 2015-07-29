<!--
	eGov suite of products aim to improve the internal efficiency,transparency, 
    accountability and the service delivery of the government  organizations.
 
    Copyright (C) <2015>  eGovernments Foundation
 
    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org
 
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.
 
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .
 
    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:
 
 	1) All versions of this program, verbatim or modified must carry this 
 	   Legal Notice.
 
 	2) Any misrepresentation of the origin of the material is prohibited. It 
 	   is required that all modified versions of this material be marked in 
 	   reasonable ways as different from the original version.
 
 	3) This license does not grant any rights to any user of the program 
 	   with regards to rights under trademark law for use of the trade names 
 	   or trademarks of eGovernments Foundation.
 
   	In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
-->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/includes/taglibs.jsp" %>

<html>
	<head>
		<title><s:text name="collectionsummary.pagetitle"></s:text></title>
		<script type="text/javascript">

		function validateFormAndSubmit(){
			if(checkBeforeSubmit()){
				 document.collSumryform.action='${pageContext.request.contextPath}/reports/collectionSummaryReport-list.action';
		    	 document.collSumryform.submit();
			}
			else
				return false;
		}
		
		function checkBeforeSubmit() {
			var fromDate = document.getElementById("fromDate").value;
			var toDate = document.getElementById("toDate").value;
			
			if (fromDate == null || fromDate == "" || fromDate == 'DD/MM/YYYY') {
				alert('From Date is mandatory');
				return false;
			}
			if (toDate == null || toDate == "" || toDate == 'DD/MM/YYYY') {
				alert('To Date is mandatory');
				return false;
			}
			return true;
		} 
		</script>
	</head>
	<body>
		<div class="formmainbox">
		 <s:if test="%{hasErrors()}">
				<div align="left">
					<s:actionerror />
					<s:fielderror/>
				</div>			
			</s:if>
			<table border="0" cellspacing="0" cellpadding="0" width="100%">
			  <tr> 
			    <td>
				  <div class="headingbg">					
					<s:text name="collectionsummary.pagetitle"/>								
				  </div>
				</td>
			  </tr>	
			  <tr>
			    <td>
			    <br/>
			     <table border="0" cellspacing="0" cellpadding="0" width="100%" style="max-width:960px;margin:0 auto;">
			       <s:form name="collSumryform" theme="simple" id="collSumryform">
			       <s:hidden name="mode"/>  
			       <s:hidden name="srchFlag"/>
				       <tr>
				       	<s:if test="%{mode=='zoneWise'}">
					         <td class="bluebox"><s:text name="collectionsummary.zone" /> :</td>
					         <td class="bluebox">
					           <s:select name="boundaryId" id="boundaryId" list="zoneBndryMap" cssStyle="width: 150px;" listKey="key" listValue="value"
									headerKey="-1" headerValue="%{getText('default.all')}" value="%{boundaryId}" />
					         </td>
				         </s:if>
				         <s:elseif test="%{mode=='wardWise'}">
					         <td class="bluebox"><s:text name="collectionsummary.ward" /> :</td>
					         <td class="bluebox">
					           <s:select name="boundaryId" id="boundaryId" list="wardBndryMap" cssStyle="width: 150px;" listKey="key" listValue="value"
									headerKey="-1" headerValue="%{getText('default.all')}" value="%{boundaryId}" />
					         </td>
				         </s:elseif>
				         <s:elseif test="%{mode=='blockWise'}">
					         <td class="bluebox"><s:text name="collectionsummary.block" /> :</td>
					         <td class="bluebox">
					           <s:select name="boundaryId" id="boundaryId" list="blockBndryMap" cssStyle="width: 150px;" listKey="key" listValue="value"
									headerKey="-1" headerValue="%{getText('default.all')}" value="%{boundaryId}" />
					         </td>
				         </s:elseif>
				         <s:elseif test="%{mode=='localityWise'}">
					         <td class="bluebox"><s:text name="collectionsummary.locality" /> :</td>
					         <td class="bluebox">
					           <s:select name="boundaryId" id="boundaryId" list="localityBndryMap" cssStyle="width: 150px;" listKey="key" listValue="value"
									headerKey="-1" headerValue="%{getText('default.all')}" value="%{boundaryId}" />
					         </td>
				         </s:elseif>
				       </tr>
				       
				       <tr>
				         <td class="bluebox"><s:text name="collectionsummary.fromdate" /> <span class="mandatory1">*</span> :</td>
				         <s:date name="fromDate" var="cdFormat" format="dd/MM/yyyy" />
						 <td class="bluebox">
							<s:textfield id="fromDate"	name="fromDate" value="%{fromDate}"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							placeholder="DD/MM/YYYY"
							cssClass="datepicker"
							onblur="validateDateFormat(this);" />
						</td>
				        <td class="bluebox"><s:text name="collectionsummary.todate" /> <span class="mandatory1">*</span> :</td>
				         <s:date name="toDate" var="cdFormat1" format="dd/MM/yyyy" />
				         <td class="bluebox">
							<s:textfield id="toDate" name="toDate" value="%{toDate}"
							onkeyup="DateFormat(this,this.value,event,false,'3')"
							placeholder="DD/MM/YYYY"
							cssClass="datepicker"
							onblur="validateDateFormat(this);" />
						</td>
				       </tr>
				       
				        <tr>
				         <td class="bluebox"><s:text name="collectionsummary.collectionmode" /> :</td>
				         <td class="bluebox">
				         	<s:select name="collMode" id="collMode" list="collectionModesMap" cssStyle="width: 150px;" listKey="key" listValue="value"
								headerKey="-1" headerValue="%{getText('default.all')}" value="%{collMode}" />
				         </td>
				         <td class="bluebox"><s:text name="collectionsummary.transactionmode" /> :</td>
				         <td class="bluebox">
				           <s:select name="transMode" id="transMode" list="dropdownData.instrumentTypeList" cssStyle="width: 150px;" listKey="type" listValue="type"
								headerKey="-1" headerValue="%{getText('default.all')}" value="%{transMode}" />
				         </td>
				       </tr>
				       
				       <tr>
				         <td class="bluebox" colspan="4" align="center">
				           <div class="buttonbottom" align="center">		   
							 <input type="submit" id="btnsearch" name="btnsearch" value="Search" class="buttonsubmit" onclick="return validateFormAndSubmit();" /> 
							 <input type="submit" id="btnclose" name="btnclose" value="Close" class="buttonsubmit normal"onClick="window.close()" />
						   </div>
				         </td>
				       </tr>
				       
				       <tr>
				       <div>
							<s:include value="collectionSummaryReport-results.jsp" />
						</div>
				       </tr>
			       </s:form>
			     </table> 
			    </td>
			  </tr>	
			  <tr>
			 <br/>
			</tr>
		</table>	  
		</div>
	</body>
</html>
