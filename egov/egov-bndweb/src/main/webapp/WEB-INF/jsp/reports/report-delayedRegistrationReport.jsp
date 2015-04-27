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
<title>Delayed Registration Report</title>
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
   
   function resetValues(){
   dom.get("searchRecords_error").style.display = 'none';
   populate("regYear").value="";
    if(populate("tableData")!=null)
   populate("tableData").style.display='none';
   }
   
   function openprint(){
 
  var year = populate("regYear").value;
   window.open("${pageContext.request.contextPath}/reports/report!delayedRegistrationReportPrint.action?regYear="+year);
  
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
			  		 <td><s:submit cssClass="buttonsubmit" id="submit" name="submit" value="Submit" onclick="return validation();" method="delayedRegistrationResult"  /></td>
			  		 <td><input type="button" id="reset" name="reset" class="button" value="Reset" onclick="resetValues();" /></td>
			  		 <td><input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();"/></td>
			  	</tr>
	        </table>
	   </div>
	 
	 
	  <s:if test="%{searchMode=='result'}">
	  <div id="tableData">
	 <table width="50%" border="1" align="center" 
						 class="tablebottom">

						<tr>
							<th class="bluebgheadtd" width="10%">
								<s:text name="Sr.No" />
							</th>
							<th class="bluebgheadtd" width="10%" align="center" colspan="2">
								<s:text name="month.lbl" />
							</th>  
							<th class="bluebgheadtd" width="10%" align="center" colspan="3">
								<s:text name="birth.lbl" />
							</th>  
							<th class="bluebgheadtd" width="10%" align="center" colspan="3">
								<s:text name="death.lbl" />
							</th>  
	 					</tr>
	 					<tr>
							<th class="bluebgheadtd" width="10%">
								<s:text name="" />
							</th>
							<th class="bluebgheadtd" width="10%" align="center" colspan="2">
								<s:text name="" />
							</th>  
							<th class="bluebgheadtd" width="10%" align="center" colspan="1">
								<s:text name="Male.lbl"/>
							</th>  
							<th class="bluebgheadtd" width="10%" align="center" colspan="1">
								<s:text name="FeMale.lbl" />
							</th>  
							<th class="bluebgheadtd" width="10%" align="center" colspan="1">
								<s:text name="Total.lbl" />
							</th> 
							<th class="bluebgheadtd" width="10%" align="center" colspan="1">
								<s:text name="Male.lbl" />
							</th> 
							<th class="bluebgheadtd" width="10%" align="center" colspan="1">
								<s:text name="FeMale.lbl" />
							</th> 
							<th class="bluebgheadtd" width="10%" align="center" colspan="1">
								<s:text name="Total.lbl" />
							</th>  
	 					</tr>
	 						<s:iterator value="reportdetailsList" var="report">
	 						<tr>
	 						<td class="blueborderfortd" width="10%">
								<s:property value="slNo" />
							</td>
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:property value="month" />
							</td>
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
								<s:property value="birthMale" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
								<s:property value="birthFemale" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
								<s:property value="birthTotal" />
							</td> 
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
								<s:property value="deathMale" />
							</td> 
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
								<s:property value="deathFemale" />
							</td> 
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
								<s:property value="deathTotal" />
							</td>   
							</tr>
	 						</s:iterator>
	 							<tr>
	 						<td class="" width="10%">
							
							</td>
							<td class="blueborderfortd" width="10%" align="center" colspan="2">
								<s:text name="Total.lbl" />
							</td>
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
								<s:property value="totalReportdetail.birthMale" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
								<s:property value="totalReportdetail.birthFemale" />
							</td>  
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
								<s:property value="totalReportdetail.birthTotal" />
							</td> 
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
								<s:property value="totalReportdetail.deathMale" />
							</td> 
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
								<s:property value="totalReportdetail.deathFemale" />
							</td> 
							<td class="blueborderfortd" width="10%" align="center" colspan="1">
								<s:property value="totalReportdetail.deathTotal" />
							</td>   
							</tr>
	 	</table>
	 		<tr>			
			  		 
			  		 <td><input type="button" id="print" name="print" class="button" value="Print" onclick="openprint();" /></td>
			  		 <td><input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();"/></td>
			  	</tr>
			  	</div>
	   
	   </s:if>
	</s:form>
</body>
</html>
