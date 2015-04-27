#-------------------------------------------------------------------------------
# /*******************************************************************************
#  *    eGov suite of products aim to improve the internal efficiency,transparency,
#  *    accountability and the service delivery of the government  organizations.
#  *
#  *     Copyright (C) <2015>  eGovernments Foundation
#  *
#  *     The updated version of eGov suite of products as by eGovernments Foundation
#  *     is available at http://www.egovernments.org
#  *
#  *     This program is free software: you can redistribute it and/or modify
#  *     it under the terms of the GNU General Public License as published by
#  *     the Free Software Foundation, either version 3 of the License, or
#  *     any later version.
#  *
#  *     This program is distributed in the hope that it will be useful,
#  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
#  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  *     GNU General Public License for more details.
#  *
#  *     You should have received a copy of the GNU General Public License
#  *     along with this program. If not, see http://www.gnu.org/licenses/ or
#  *     http://www.gnu.org/licenses/gpl.html .
#  *
#  *     In addition to the terms of the GPL license to be adhered to in using this
#  *     program, the following additional terms are to be complied with:
#  *
#  * 	1) All versions of this program, verbatim or modified must carry this
#  * 	   Legal Notice.
#  *
#  * 	2) Any misrepresentation of the origin of the material is prohibited. It
#  * 	   is required that all modified versions of this material be marked in
#  * 	   reasonable ways as different from the original version.
#  *
#  * 	3) This license does not grant any rights to any user of the program
#  * 	   with regards to rights under trademark law for use of the trade names
#  * 	   or trademarks of eGovernments Foundation.
#  *
#  *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#  ******************************************************************************/
#-------------------------------------------------------------------------------
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>


<html>
<title>Month/Registration Unit wise Report - Birth/Death/Still Birth </title>
<head>
    <jsp:include page='/WEB-INF/jsp/reports/report.jsp'/> 
</head>


<script  type="text/javascript">

   function validation(){
   
    if(populate("regUnitId").value=="-1"){
	 warn('<s:text name="regunit.required"/>');
	return false;
	}
	
	if(!validatecommon())
	return false;
	
	return true;
 }
   
   function resetvalues(){
   dom.get("searchRecords_error").style.display = 'none';
    populate("regUnitId").value="-1" ;
    if(populate("resultdiv")!=null)
   populate("resultdiv").style.display='none';
   }


  function openprint(){
 
  var regunit = populate("regUnitId").value;
  var month=populate("month").value;
   window.open("${pageContext.request.contextPath}/reports/report!regUnitWiseReportPrint.action?month="+month+"&regUnitId="+regunit);
  
  }
</script >

<body onload="">

	<div class="errorstyle" id="searchRecords_error" style="display: none;"></div>
		<div class="errorstyle" style="display: none" >			
		</div>
		<s:if test="%{hasErrors()}">
			<div class="errorstyle">
				<s:actionerror />	
				<s:fielderror />
			</div>
		</s:if>
		<s:if test="%{hasActionMessages()}">
			<div class="errorstyle">
				<s:actionmessage />
			</div>
		</s:if>

	<s:form action="report" theme="simple" name="reportForm">
	
	<jsp:include page='/WEB-INF/jsp/reports/report-common.jsp'/> 
	  		
		<div class="buttonbottom" align="center">
			<table>
				<tr>			
			  		 <td><s:submit cssClass="buttonsubmit" id="submit" name="submit" value="Submit" onclick="return validation();" method="regUnitWiseReportResult"  /></td>
			  		 <td><input type="button" id="reset" name="reset" class="button" value="Reset" onclick="resetvalues();" /></td>
			  		 <td><input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();"/></td>
			  	</tr>
	        </table>
	   </div>
	   
	 	<s:if test="%{searchMode=='result'}">
	    <s:if test="%{reportdetailsList!=null}">
	   <div id="resultdiv">
	 <table width="50%" border="1" align="center" 
						 class="tablebottom">

						<tr id="1">
							<td class="blueborderfortd" width="10%" >
								
							</td>
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
							
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:text name="Monthly.lbl" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:text name="Progressive.lbl" />
							</td>  
	 					</tr>
	 					<tr id="2">
							<td class="blueborderfortd" width="10%" align="center">
								<s:text name="A)" />
							</td>
							<td class="blueborderfortd" width="40%" align="left" colspan="2">
								<s:text name="No. of Birth Registration during the month" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="%{totalBirthRegforMonth}" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
							<s:property  value="%{totalBirthRegforMonthprogressive}" />
							</td>  
	 					</tr>
	 					<tr id="3">
							<td class="blueborderfortd" width="10%" align="center">
								<s:text name="HB" />
							</td>
							<td class="blueborderfortd" width="10%" align="left" colspan="2">
								<s:text name="1. Registration within one year" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="hospitalreportdetail.birthTotal" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="hospitalreportdetail.birthTotalprogressive" />
							</td>  
	 					</tr>
	 					<tr id="4">
							<td class="blueborderfortd" width="10%" align="center">
								<s:text name="" />
							</td>
							<td class="blueborderfortd" width="10%" align="left" colspan="2">
								<s:text name="2. No. of Male" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="hospitalreportdetail.birthMale" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
							<s:property  value="hospitalreportdetail.birthMaleprogressive" />
							</td>  
	 					</tr>
	 					<tr id="5">
							<td class="blueborderfortd" width="10%" align="center">
								<s:text name="" />
							</td>
							<td class="blueborderfortd" width="10%" align="left" colspan="2">
								<s:text name="3. No. of Female" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="hospitalreportdetail.birthFemale" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="hospitalreportdetail.birthFemaleprogressive" />
							</td>  
	 					</tr>
	 					<tr id="6">
							<td class="blueborderfortd" width="10%" align="center">
								<s:text name="RB" />
							</td>
							<td class="blueborderfortd" width="10%" align="left" colspan="2">
								<s:text name="1. Registration within one year" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="nonhospitalreportdetail.birthTotal" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="nonhospitalreportdetail.birthTotalprogressive" />
							</td>  
	 					</tr>
	 					<tr id="7">
							<td class="blueborderfortd" width="10%" align="center">
								<s:text name="" />
							</td>
							<td class="blueborderfortd" width="10%" align="left" colspan="2">
								<s:text name="2. No. of Male" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="nonhospitalreportdetail.birthMale" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="nonhospitalreportdetail.birthMaleprogressive" />
							</td>  
	 					</tr>
	 					<tr id="8">
							<td class="blueborderfortd" width="10%" align="center">
								<s:text name="" />
							</td>
							<td class="blueborderfortd" width="10%" align="left" colspan="2">
								<s:text name="3. No. of Female" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="nonhospitalreportdetail.birthFemale" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="nonhospitalreportdetail.birthFemaleprogressive" />
							</td>  
	 					</tr>
	 					<tr id="9">
							<td class="blueborderfortd" width="10%" align="center">
								<s:text name="B)" />
							</td>
							<td class="blueborderfortd" width="10%" align="left" colspan="2">
								<s:text name="No. of Death Registration during the month" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
							<s:property  value="%{totalDeathRegforMonth}" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="%{totalDeathRegforMonthprogressive}" />
							</td>  
	 					</tr>
	 					<tr id="10">
							<td class="blueborderfortd" width="10%" align="center">
								<s:text name="HD" />
							</td>
							<td class="blueborderfortd" width="10%" align="left" colspan="2">
								<s:text name="1. Registration within one year" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="hospitalreportdetail.deathTotal" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="hospitalreportdetail.deathTotalprogressive" />
							</td>  
	 					</tr>
	 					<tr id="11">
							<td class="blueborderfortd" width="10%" align="center">
								<s:text name="" />
							</td>
							<td class="blueborderfortd" width="10%" align="left" colspan="2">
								<s:text name="2. No. of Male" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="hospitalreportdetail.deathMale" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
							<s:property  value="hospitalreportdetail.deathMaleprogressive" />
							</td>  
	 					</tr>
	 					<tr id="12">
							<td class="blueborderfortd" width="10%" align="center">
								<s:text name="" />
							</td>
							<td class="blueborderfortd" width="10%" align="left" colspan="2">
								<s:text name="3. No. of Female" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="hospitalreportdetail.deathFemale" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
							<s:property  value="hospitalreportdetail.deathFemaleprogressive" />
							</td>  
	 					</tr>
	 					<tr id="13">
							<td class="blueborderfortd" width="10%" align="center">
								<s:text name="RD" />
							</td>
							<td class="blueborderfortd" width="10%" align="left" colspan="2">
								<s:text name="1. Registration within one year" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="nonhospitalreportdetail.deathTotal" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="nonhospitalreportdetail.deathTotalprogressive" />
							</td>  
	 					</tr>
	 					<tr id="14">
							<td class="blueborderfortd" width="10%" align="center">
								<s:text name="" />
							</td>
							<td class="blueborderfortd" width="10%" align="left" colspan="2">
								<s:text name="2. No. of Male" />
							</td>  
						<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="nonhospitalreportdetail.deathMale" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
							<s:property  value="nonhospitalreportdetail.deathMaleprogressive" />
							</td>   
	 					</tr>
	 					<tr id="15">
							<td class="blueborderfortd" width="10%" align="center">
								<s:text name="" />
							</td>
							<td class="blueborderfortd" width="10%" align="left" colspan="2">
								<s:text name="3. No. of Female" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="nonhospitalreportdetail.deathFemale" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
							<s:property  value="nonhospitalreportdetail.deathFemaleprogressive" />
							</td>   
	 					</tr>
	 					<tr id="16">
							<td class="blueborderfortd" width="10%" align="center">
								<s:text name="C)" />
							</td>
							<td class="blueborderfortd" width="10%" align="left" colspan="2">
								<s:text name="No. of Form No. 4 received during the month" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="totalCertifiedforMonth" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="totalCertifiedforMonthprogressive" />
							</td>  
	 					</tr>
	 					<tr id="17">
							<td class="blueborderfortd" width="10%" align="center">
								<s:text name="D)" />
							</td>
							<td class="blueborderfortd" width="10%" align="left" colspan="2">
								<s:text name="No. of the Still Birth during the month" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="hospitalreportdetail.stillbirth" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="hospitalreportdetail.stillbirthprogressive" />
							</td>  
	 					</tr>
	 					<tr id="18">
							<td class="blueborderfortd" width="10%" align="center">
								<s:text name="" />
							</td>
							<td class="blueborderfortd" width="10%" align="left" colspan="2">
								<s:text name="1. No. of Male" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="hospitalreportdetail.stillbirthmale" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="hospitalreportdetail.stillbirthmaleprogressive" />
							</td>  
	 					</tr>
	 					<tr id="19">
							<td class="blueborderfortd" width="10%" align="center">
								<s:text name="" />
							</td>
							<td class="blueborderfortd" width="10%" align="left" colspan="2">
								<s:text name="2. No. of Female" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="hospitalreportdetail.stillbirthfemale" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
								<s:property  value="hospitalreportdetail.stillbirthfemaleprogressive" />
							</td>  
	 					</tr>
	 					<tr id="20">
							<td class="blueborderfortd" width="10%" align="center">
								<s:text name="E)" />
							</td>
							<td class="blueborderfortd" width="10%" align="left" colspan="2">
								<s:text name="No. of the Infant Death during the month" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
									<s:property  value="totalInfantforMonth" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
									<s:property  value="totalInfantforMonthprogressive" />
							</td>  
	 					</tr>
	 					<tr id="21">
							<td class="blueborderfortd" width="10%" align="center">
								<s:text name="F)" />
							</td>
							<td class="blueborderfortd" width="10%" align="left" colspan="2">
								<s:text name="No. of the Maternal Death during the month" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
									<s:property  value="totalDeliverydeathforMonth" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="3">
									<s:property  value="totalDeliverydeathforMonthprogressive" />
							</td>  
	 					</tr>
	 					
							
	 				
	 	</table>
	  
	   			<tr>			
			  		 
			  		 <td><input type="button" id="print" name="print" class="button" value="Print" onclick="openprint();" /></td>
			  		 <td><input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();"/></td>
			  	</tr>
			  	</div>
	   </s:if>
	   </s:if>
	   
	 
	   
	</s:form>
</body>
</html>
