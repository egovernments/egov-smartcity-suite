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
<title>Certificates Generated Report</title>
<head>
    <jsp:include page='/WEB-INF/jsp/reports/report.jsp'/> 
</head>


<script  type="text/javascript">

   function validation(){
   
   
	 if(populate("regNo").value==""){
	 warn('<s:text name="regno.required"/>');
	return false;
	}
	
	 if(populate("regYear").value==""){
    warn('<s:text name="regYear.required"/>');
	return false;
	}
	
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
 	populate("regUnitId").value="-1"
 	populate("regNo").value=""
 	populate("regYear").value=""
    if(populate("resultdiv")!=null)
   populate("resultdiv").style.display='none';
   }
   
     function openprint(){
     var regtype;
     var year = populate("regYear").value;
     var regno = populate("regNo").value;
     var regunit = populate("regUnitId").value;
     var placeType = document.getElementsByName("regType");     
	 var count;
	 var regTypeValue ="";
		for(count=0;count<placeType.length;count++)
		{
		    if(placeType[count].checked )
		    {
		        regTypeValue = placeType[count].value;
		        break;
		    }
    }
    
   window.open("${pageContext.request.contextPath}/reports/report!certificateReportPrint.action?regYear="+year+"&regNo="+regno+"&regUnitId="+regunit+"&regType="+regTypeValue);
  
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
			  		 <td><s:submit cssClass="buttonsubmit" id="submit" name="submit" value="Submit" onclick="return validation();" method="certificateReportResult"  /></td>
			  		 <td><input type="button" id="reset" name="reset" class="button" value="Reset" onclick="resetvalues();" /></td>
			  		 <td><input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();"/></td>
			  	</tr>
	        </table>
	   </div>
	   
	  <s:if test="%{searchMode=='result'}">
	  <div class="blueshadow" align="center" id="resultdiv">
						<table width="100%" border="0" class="tableStyle"
							style="width: 40%; border: 1px solid;">

							<tr>
								<td class="greybox" width="40%" align="center" />
								<td class="greybox"><b><p><s:property value="%{regType}" />Records</p></b></td>
								<td class="greybox">&nbsp;</td>
							</tr>
							</table>
							
							<table width="100%" border="0" class="tableStyle"
							style="width: 40%;">
							
							<tr>
								<td class="bluebox" width="27%" align="center" />								
								<td class="bluebox">&nbsp;</td>
								<td class="bluebox">&nbsp;</td>
				
							</tr>
							
							<tr>
								<td class="bluebox" width="27%" align="center" />
								<td class="bluebox"><p><s:text name="Registration Number"/>:<s:property value="%{regNo}" /></p></td>
								<td class="bluebox">&nbsp;</td>
							</tr>
							
							<tr>
								<td class="bluebox" width="27%" align="center" />									
								<td class="bluebox"><p><s:text name="Registration Unit"/>:<s:property value="%{regUnit.regUnitDesc}" /></p></td>
								<td class="bluebox">&nbsp;</td>
							</tr>
							
							<tr>
								<td class="bluebox" width="27%" align="center" />								
								<td class="bluebox"><p><s:text name="Registration Year"/>:<s:property value="%{regYear}" /></p></td>
								<td class="bluebox">&nbsp;</td>
							</tr>
							
								<tr>
								<td class="bluebox" width="27%" align="center" />								
								<td class="bluebox"><p><s:text name="Total Certificates"/>:<s:property value="%{noofcopy}" /></p></td>
								<td class="bluebox">&nbsp;</td>
							</tr>
							<tr>
								<td class="bluebox" width="27%" align="center" />								
								<td class="bluebox">&nbsp;</td>
								<td class="bluebox">&nbsp;</td>
								
							</tr>
	  </table>  
	  
	          <div>
	 				 <tr>						  		 
			  		 <td><input type="button" id="print" name="print" class="button" value="Print" onclick="openprint();" /></td>
			  		 <td><input type="button" name="close" id="close" class="button" value="Close" onclick="window.close();"/></td>
			  	     </tr>
	         </div> 
	 </div>
	 </s:if>
	   
	</s:form>
</body>
</html>
