#-------------------------------------------------------------------------------
# <!-- #-------------------------------------------------------------------------------
# # eGov suite of products aim to improve the internal efficiency,transparency, 
# #    accountability and the service delivery of the government  organizations.
# # 
# #     Copyright (C) <2015>  eGovernments Foundation
# # 
# #     The updated version of eGov suite of products as by eGovernments Foundation 
# #     is available at http://www.egovernments.org
# # 
# #     This program is free software: you can redistribute it and/or modify
# #     it under the terms of the GNU General Public License as published by
# #     the Free Software Foundation, either version 3 of the License, or
# #     any later version.
# # 
# #     This program is distributed in the hope that it will be useful,
# #     but WITHOUT ANY WARRANTY; without even the implied warranty of
# #     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# #     GNU General Public License for more details.
# # 
# #     You should have received a copy of the GNU General Public License
# #     along with this program. If not, see http://www.gnu.org/licenses/ or 
# #     http://www.gnu.org/licenses/gpl.html .
# # 
# #     In addition to the terms of the GPL license to be adhered to in using this
# #     program, the following additional terms are to be complied with:
# # 
# # 	1) All versions of this program, verbatim or modified must carry this 
# # 	   Legal Notice.
# # 
# # 	2) Any misrepresentation of the origin of the material is prohibited. It 
# # 	   is required that all modified versions of this material be marked in 
# # 	   reasonable ways as different from the original version.
# # 
# # 	3) This license does not grant any rights to any user of the program 
# # 	   with regards to rights under trademark law for use of the trade names 
# # 	   or trademarks of eGovernments Foundation.
# # 
# #   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
# #------------------------------------------------------------------------------- -->
#-------------------------------------------------------------------------------
<%@ taglib prefix="s" uri="/WEB-INF/taglibs/struts-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>

<s:set name="theme" value="'simple'" scope="page" />
<s:hidden id="docket" name="docket" value="%{docket.id}"/>
<s:hidden id="docket.createdBy" name="docket.createdBy" value="%{docket.createdBy.id}"/>
<s:hidden id="docket.modifiedBy" name="docket.modifiedBy" value="%{docket.modifiedBy.id}"/>

<div align="center"> 
  <div id="docketSheetDiv" class="formmainbox">
  
    <div align="center" id="checklistdiv">
		
		<%@ include file="../inspection/inspectionExtn-commonDocketSheet.jsp"%>
	
<h1 class="subhead" ><s:text name="inspectionlbl.docket.constructionstageHeader"/></h1>
	   <s:if test="%{constructionStages.size!=0}">
	   	  <s:iterator value="constructionStages" status="row_status1">
		   <table id="constStagechecklists" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		    <tr>
			    <td class="greybox" width="10%">&nbsp;</td>	 		
			   	<td class="greybox" width="50%"><s:text name="constructionStages[%{#row_status1.index}].checkListDetails.description"  /></td>		   	
		     	 <td class="blueborderfortd" >
			     <s:select id="constStage" name="constructionStages[%{#row_status1.index}].value"  list="#{'NA':'NA','YES':'YES','NO':'NO'}"       />
			     </td>
			 	<s:hidden name="constructionStages[%{#row_status1.index}].checkListDetails.id"/>
				<s:hidden name="constructionStages[%{#row_status1.index}].checkListDetails.checkList.id"/>
				<s:hidden name="constructionStages[%{#row_status1.index}].checkListDetails.description"/>
				<s:hidden name="constructionStages[%{#row_status1.index}].checkListDetails.docket.id"/>
				<s:hidden name="constructionStages[%{#row_status1.index}].id"/>
			    	
		    </tr>   
		   </table>
		  </s:iterator>
	  </s:if>
	
	   <h1 class="subhead" ><s:text name="inspectionlbl.docket.natuveorViolationLbl"/></h1>
	
	  <s:if test="%{devContrlList.size!=0}">
		  
		   <table id="generalViolationlists" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		    <tr>		   
				<td class="greybox" width="30%"><s:text name="inspectionlbl.docket.devrule"/>  </td>
				<td class="greybox" width="10%"><s:text name="inspectionlbl.docket.required"/>  </td>
				<td class="greybox" width="20%"><s:text name="inspectionlbl.docket.provided"/>  </td>
				<td class="greybox" width="15%"><s:text name="inspectionlbl.docket.extofviolation"/>  </td>
				<td class="greybox" width="10%"><s:text name="inspectionlbl.docket.percOfviolation"/>  </td>
				<td class="greybox" width="15%"><s:text name="inspectionlbl.docket.remarks"/>  </td>
		    </tr>
		    <s:iterator value="devContrlList" status="row_status">
		    <tr>
			    <td class="greybox" width="30%"><s:text name="devContrlList[%{#row_status.index}].checkListDetails.description"/></td>			
			   
			   
		     	<s:if  test="%{checkListDetails.description=='No. of Floors'}">
		     	<td class="greybox" width="20%"><s:textfield  name="devContrlList[%{#row_status.index}].required" maxlength="20"/>  	</td>		   	
			   	<td class="greybox" width="10%"><s:textfield  name="devContrlList[%{#row_status.index}].provided" maxlength="20"/>  	</td>
			   	<td class="greybox" width="15%"><s:textfield  name="devContrlList[%{#row_status.index}].extentOfViolation" maxlength="20"/>  	</td>
			   	<td class="greybox" width="10%"><s:textfield  name="devContrlList[%{#row_status.index}].percentageOfViolation" maxlength="5"/>  	</td>
			   	<td class="greybox" width="10%"><s:textfield  name="devContrlList[%{#row_status.index}].remarks" maxlength="250"/>  	</td>
			   
			    </s:if>
			    <s:else>
			   	<td class="greybox"  id="numbers" width="20%"><s:textfield  name="devContrlList[%{#row_status.index}].required" maxlength="20"/>  	</td>		   	
			   	<td class="greybox"  id="numbers" width="10%"><s:textfield  name="devContrlList[%{#row_status.index}].provided" maxlength="20"/>  	</td>
			   	<td class="greybox"  id="numbers" width="15%"><s:textfield  name="devContrlList[%{#row_status.index}].extentOfViolation" maxlength="20"/>  	</td>
			   	<td class="greybox"  id="numbers" width="10%"><s:textfield  name="devContrlList[%{#row_status.index}].percentageOfViolation" maxlength="5"/>  	</td>
			   	<td class="greybox"   width="10%"><s:textfield  name="devContrlList[%{#row_status.index}].remarks" maxlength="250"/>  	</td>
			    
			     </s:else>
			    </td>
			 	<s:hidden name="devContrlList[%{#row_status.index}].checkListDetails.id"/>
				<s:hidden name="devContrlList[%{#row_status.index}].checkListDetails.checkList.id"/>
				<s:hidden name="devContrlList[%{#row_status.index}].checkListDetails.description"/>
				<s:hidden name="devContrlList[%{#row_status.index}].checkListDetails.docket.id"/>
				<s:hidden name="devContrlList[%{#row_status.index}].id"/>
		    </tr>
		    </s:iterator>   
		   </table>
		  
	  </s:if>
	  
	    <h1 class="subhead" ><s:text name="inspectionlbl.docket.setBack"/></h1>
	
	  <s:if test="%{setBackList.size!=0}">
		  
		   <table id="generalsetbacklists" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		    <tr>		   
				<td class="greybox" width="30%">&nbsp;  </td>
				<td class="greybox" width="10%"><s:text name="inspectionlbl.docket.required"/>  </td>
				<td class="greybox" width="20%"><s:text name="inspectionlbl.docket.provided"/>  </td>
				<td class="greybox" width="15%"><s:text name="inspectionlbl.docket.extofviolation"/>  </td>
				<td class="greybox" width="10%"><s:text name="inspectionlbl.docket.percOfviolation"/>  </td>
				<td class="greybox" width="15%"><s:text name="inspectionlbl.docket.remarks"/>  </td>
		    </tr>
		    <s:iterator value="setBackList" status="row_status">
		    <tr>
			    <td class="greybox" width="30%"><s:text name="setBackList[%{#row_status.index}].checkListDetails.description"/></td>			
			   
			   
		     	<s:if  test="%{checkListDetails.description=='Plot Coverage'}">
		     	<td class="greybox" width="20%"><s:textfield  name="setBackList[%{#row_status.index}].required" maxlength="20"/>  	</td>		   	
			   	<td class="greybox" width="10%"><s:textfield  name="setBackList[%{#row_status.index}].provided" maxlength="20"/>  	</td>
			   	<td class="greybox" width="15%"><s:textfield  name="setBackList[%{#row_status.index}].extentOfViolation" maxlength="20"/>  	</td>
			   	<td class="greybox" width="10%"><s:textfield  name="setBackList[%{#row_status.index}].percentageOfViolation" maxlength="5"/>  	</td>
			   	<td class="greybox" width="10%"><s:textfield  name="setBackList[%{#row_status.index}].remarks" maxlength="250"/>  	</td>
			   
			    </s:if>
			    <s:else>
			   	<td class="greybox"  id="numbers" width="20%"><s:textfield  name="setBackList[%{#row_status.index}].required" maxlength="20"/>  	</td>		   	
			   	<td class="greybox"  id="numbers" width="10%"><s:textfield  name="setBackList[%{#row_status.index}].provided" maxlength="20"/>  	</td>
			   	<td class="greybox"  id="numbers" width="15%"><s:textfield  name="setBackList[%{#row_status.index}].extentOfViolation" maxlength="20"/>  	</td>
			   	<td class="greybox"  id="numbers" width="10%"><s:textfield  name="setBackList[%{#row_status.index}].percentageOfViolation" maxlength="5"/>  	</td>
			   	<td class="greybox"   width="10%"><s:textfield  name="setBackList[%{#row_status.index}].remarks" maxlength="250"/>  	</td>
			    
			     </s:else>
			    </td>
			 	<s:hidden name="setBackList[%{#row_status.index}].checkListDetails.id"/>
				<s:hidden name="setBackList[%{#row_status.index}].checkListDetails.checkList.id"/>
				<s:hidden name="setBackList[%{#row_status.index}].checkListDetails.description"/>
				<s:hidden name="setBackList[%{#row_status.index}].checkListDetails.docket.id"/>
				<s:hidden name="setBackList[%{#row_status.index}].id"/>
		    </tr>
		    </s:iterator>   
		   </table>
		  
	  </s:if>
	      <h1 class="subhead" ><s:text name="inspectionlbl.docket.parkingLbl"/></h1>
	      
	    <table  width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" id="abbutingRoad_Tbl">
	    <tr>
 			<td class="bluebox">&nbsp;</td>
			<td class="bluebox" width="13%"><s:text name="inspectionlbl.docket.parkingGeneralVoilation.plotWidthRear"/></td> 
   			<td class="bluebox"  id="numbers"><s:textfield id="plotWidthRear" name="docket.plotWidthRear"  value="%{docket.plotWidthRear}"/></td>
   			
   			<td class="bluebox">&nbsp;</td>
			<td class="bluebox" width="13%"><s:text name="inspectionlbl.docket.parkingGeneralVoilation.constructionWidthRear"/></td> 
   			<td class="bluebox"  id="numbers"><s:textfield id="constructionWidthRear" name="docket.constructionWidthRear"  value="%{docket.constructionWidthRear}"/></td>
		 </tr>
		 
		  <tr>
	 		<td class="greybox">&nbsp;</td>
			<td class="greybox" width="13%"><s:text name="inspectionlbl.docket.parkingGeneralVoilation.constructionHeightRear"/></td>  
   			<td class="greybox"  id="numbers"><s:textfield id="constructionHeightRear" name="docket.constructionHeightRear"  value="%{docket.constructionHeightRear}"/></td>
   			
   			<td class="greybox">&nbsp;</td>
   			<td class="greybox">&nbsp;</td>
   			<td class="greybox">&nbsp;</td>
		 </tr>
		 </table>
	
	  <s:if test="%{parkingList.size!=0}">
		  
		   <table id="generalparkinglists" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		    <tr>		   
				<td class="greybox" width="30%">&nbsp;  </td>
				<td class="greybox" width="10%"><s:text name="inspectionlbl.docket.required"/>  </td>
				<td class="greybox" width="20%"><s:text name="inspectionlbl.docket.provided"/>  </td>
				<td class="greybox" width="15%"><s:text name="inspectionlbl.docket.extofviolation"/>  </td>
				<td class="greybox" width="10%"><s:text name="inspectionlbl.docket.percOfviolation"/>  </td>
				<td class="greybox" width="15%"><s:text name="inspectionlbl.docket.remarks"/>  </td>
		    </tr>
		    <s:iterator value="parkingList" status="row_status">
		    <tr>
			    <td class="greybox" width="30%"><s:text name="parkingList[%{#row_status.index}].checkListDetails.description"/></td>			
			   	<td class="greybox"  id="numbers" width="20%"><s:textfield  name="parkingList[%{#row_status.index}].required" maxlength="20"/>  	</td>		   	
			   	<td class="greybox"  id="numbers" width="10%"><s:textfield  name="parkingList[%{#row_status.index}].provided" maxlength="20"/>  	</td>
			   	<td class="greybox"  id="numbers" width="15%"><s:textfield  name="parkingList[%{#row_status.index}].extentOfViolation" maxlength="20"/>  	</td>
			   	<td class="greybox"  id="numbers" width="10%"><s:textfield  name="parkingList[%{#row_status.index}].percentageOfViolation" maxlength="5"/>  	</td>
			   	<td class="greybox"   width="10%"><s:textfield  name="parkingList[%{#row_status.index}].remarks" maxlength="250"/>  	</td>
			 	<s:hidden name="parkingList[%{#row_status.index}].checkListDetails.id"/>
				<s:hidden name="parkingList[%{#row_status.index}].checkListDetails.checkList.id"/>
				<s:hidden name="parkingList[%{#row_status.index}].checkListDetails.description"/>
				<s:hidden name="parkingList[%{#row_status.index}].checkListDetails.docket.id"/>
				<s:hidden name="parkingList[%{#row_status.index}].id"/>
		    </tr>
		    </s:iterator>   
		   </table>
		  
	  </s:if>
	  <s:if test="%{generalList.size!=0}">
		  <s:iterator value="generalList" status="row_status">
		   <table id="generalViolationlists" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		    <tr>
			    <td class="greybox" width="10%">&nbsp;</td>			
			   	<td class="greybox" width="50%"><s:text name="generalList[%{#row_status.index}].checkListDetails.description"/></td>		   	
		     	 <td class="blueborderfortd">
		     	  <s:if test="%{checkListDetails.description!=@org.egov.bpa.constants.BpaConstants@DOCKET_PARKINGGNRLVOILATION_CONSTRUCTIONSTAGEREAR}">
			      	<s:select id="genviolist" name="generalList[%{#row_status.index}].value"  list="#{'NA':'NA','YES':'YES','NO':'NO'}"       />
			      </s:if>
			      <s:else>
			      	<s:select id="genviolist" name="generalList[%{#row_status.index}].value"  list="dropdownData.usageOfConstructionList" headerKey="NA" headerValue="NA"/>
			      </s:else>
			     </td>
			 	<s:hidden name="generalList[%{#row_status.index}].checkListDetails.id"/>
				<s:hidden name="generalList[%{#row_status.index}].checkListDetails.checkList.id"/>
				<s:hidden name="generalList[%{#row_status.index}].checkListDetails.description" />
				<s:hidden name="generalList[%{#row_status.index}].checkListDetails.docket.id"/>
				<s:hidden name="generalList[%{#row_status.index}].id"/>
		    </tr>   
		   </table>
		  </s:iterator>
	  </s:if>
		
		<h1 class="subhead" ><s:text name="inspectionlbl.docket.minDisFromPowerLineLbl"/></h1>
	  <s:if test="%{minDistancePowerLineList.size!=0}">
		  <s:iterator value="minDistancePowerLineList" status="row_status">
		   <table id="generalViolationlists" width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
		    <tr>
			    <td class="greybox" width="10%">&nbsp;</td>			
			   	<td class="greybox" width="50%"><s:text name="minDistancePowerLineList[%{#row_status.index}].checkListDetails.description"/>
			   	</td>		   	
			    <td class="blueborderfortd">	
		     	  <s:select id="genviolist" name="minDistancePowerLineList[%{#row_status.index}].value"  list="#{'NA':'NA','YES':'YES','NO':'NO'}"      />
			    </td>
			 	<s:hidden name="minDistancePowerLineList[%{#row_status.index}].checkListDetails.id"/>
				<s:hidden name="minDistancePowerLineList[%{#row_status.index}].checkListDetails.checkList.id"/>
				<s:hidden name="minDistancePowerLineList[%{#row_status.index}].checkListDetails.description" />
				<s:hidden name="minDistancePowerLineList[%{#row_status.index}].checkListDetails.docket.id"/>
				<s:hidden name="minDistancePowerLineList[%{#row_status.index}].id"/>
		    </tr>   
		   </table>
		  </s:iterator>
	  </s:if> 
		
	    <table  width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" id="otherinfo_Tbl">
	       <tr>
	 			<td class="greybox" width="20%">&nbsp;</td>
				<td class="greybox" width="13%"><s:text name="inspectionlbl.docket.aeereport"/></td> 
	   			<td class="greybox"><s:textarea id="aeeInspectionReport" name="docket.aeeInspectionReport"  value="%{docket.aeeInspectionReport}"  cols="20" rows="2"/></td>
				
				<td class="greybox" width="13%" ><s:text name="inspectionlbl.docket.otherDetails"/></td> 
	   			<td class="greybox" >  <s:textarea id="remarks" name="docket.remarks"  value="%{docket.remarks}" cols="20" rows="2"/></td>
		 </tr>
	    </table> 
	</div>
  
   </div> 
</div>

