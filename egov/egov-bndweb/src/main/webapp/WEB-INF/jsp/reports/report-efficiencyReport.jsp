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
<title>Birth and Death Efficiency Report </title>
<head>
    <jsp:include page='/WEB-INF/jsp/reports/report.jsp'/> 
</head>

<script  type="text/javascript">

   function validation(){
   
    if(populate("regYear").value==""){
	 warn('<s:text name="regYear.required"/>');
	return false;
	}	
	if(!validatecommon())
	return false;
	
	return true;
 }
   
   function resetvalues(){
   dom.get("searchRecords_error").style.display = 'none';
    populate("regYear").value="" ;
    if(populate("resultdiv")!=null)
   populate("resultdiv").style.display='none';
   }
   
   
  function openprint(){
 
  var year = populate("regYear").value;
   window.open("${pageContext.request.contextPath}/reports/report!efficiencyReportPrint.action?regYear="+year);
  
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
			  		 <td><s:submit cssClass="buttonsubmit" id="submit" name="submit" value="Submit" onclick="return validation();" method="efficiencyReportResult"  /></td>
			  		 <td><input type="button" id="reset" name="reset" class="button" value="Reset" onclick="resetvalues();" /></td>
			  		 <td><input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();"/></td>
			  	</tr>
	        </table>
	   </div>
	   
	  <s:if test="%{searchMode=='result'}">
	   <div id="resultdiv">
	 <table width="50%" border="1" align="center" 
						 class="tablebottom">

						<tr id="1">
							
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
								<s:text name="No" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:text name="Name" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">Year-
								<s:text name="%{regYear}" />
							</td>  
	 					</tr>
	 					<tr id="2">
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
							
							</td>
							<td class="blueborderfortd" width="40%" align="left" colspan="2">
								<s:text name="" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:property  value="%{}" />
							</td>  
							
	 					</tr>
	 					<tr id="3">
							
							<td class="blueborderfortd" width="10%" align="left" colspan="1">
								<s:text name="1" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:text  name="Birth Rate" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:property  value="resultMap.birthrate" />
							</td>  
	 					</tr>
	 					<tr id="4">
								<td class="blueborderfortd" width="10%" align="left" colspan="1">
								<s:text name="2" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:text  name="Death Rate" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:property  value="resultMap.deathrate" />
							</td>  
	 					</tr>
	 					<tr id="5">
								<td class="blueborderfortd" width="10%" align="left" colspan="1">
								<s:text name="3" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:text  name="Still Birth" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:property  value="resultMap.stillbirthrate" />
							</td>  
	 					</tr>
	 					<tr id="6">
										<td class="blueborderfortd" width="10%" align="left" colspan="1">
								<s:text name="4" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:text  name="Infant Mortality Rate" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:property  value="resultMap.infantmortalityrate" />
							</td>  
	 					</tr>
	 					<tr id="7">
							<td class="blueborderfortd" width="10%" align="left" colspan="1">
								<s:text name="5" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:text  name="Maternal Mortality Rate" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:property  value="resultMap.meternalmortalityrate" />
							</td>  
	 					</tr>
	 					<tr id="8">
							<td class="blueborderfortd" width="10%" align="left" colspan="1">
								<s:text name="6" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:text  name="Growth Rate" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:property  value="resultMap.growthrate" />
							</td>  
	 					</tr>
	 					</table>
	 				<tr>			
			  		 
			  		 <td><input type="button" name="print" id="print" class="button"  value="Print" onclick="openprint();"/></td>
			  		 <td><input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();"/></td>
			  	</tr>
			  	</div>
	  
	   </s:if>
	   
	</s:form>
</body>
</html>
