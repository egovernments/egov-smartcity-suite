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
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<script type="text/javascript">

	  function populate(objName)
  {
     return document.getElementById(objName);
  }
  
   function warn(msg)
	  {		 
	      dom.get("searchRecords_error").style.display = '';
	      populate("searchRecords_error").innerHTML = msg;
	      return false;		
	  }
	  
  function validatecommon(){	  
	  
	    var curr_date = new Date();
	    var curr_year = curr_date.getFullYear(); 
	    var curr_month = curr_date.getMonth(); 
	    curr_month=curr_month+1;
	    var todaysDate=getTodayDate();
	    
	   if(document.getElementById('regYear')!=null){
		var selectedYear=document.getElementById('regYear').value;
		
		if(selectedYear!="" && selectedYear!=null){
			if((selectedYear<1900) || (selectedYear>curr_year))
			{		
				warn('Not A Valid Year!! \nPlease Enter the year between 1900 And '+ curr_year);
				document.getElementById('regYear').value="";
				document.getElementById('regYear').focus();
				return false;
			 } 
         }
      }
      
      
      	   if(document.getElementById('month')!=null){
		var selectedMonth=document.getElementById('month').value;

		   if(document.getElementById('regYear')!=null){
		if(selectedMonth!="-1" && selectedMonth!=null){
		      if(selectedYear>=curr_year){
			if((selectedMonth>curr_month))
			{		
				warn('Month cannot be greater than current month');
				document.getElementById('month').focus();
				return false;
			 }
			 } 
          }
         }else{
  
         if(selectedMonth!="-1" && selectedMonth!=null){		  
			if((selectedMonth>curr_month))
			{		
				warn('Month cannot be greater than current month');
				document.getElementById('month').focus();
				return false;
			 }
			 
          }
         
         }
      }
      
       	if(document.getElementById('fromDate')!=null){
    	var fromdate=document.getElementById('fromDate').value;
    	var todate=document.getElementById('toDate').value;
    	
    	if(fromdate!=null && fromdate!="" && fromdate!=undefined && todate!=null && todate!="" && todate!=undefined ){
			if(compareDate(fromdate,todaysDate) == -1)
			{						  	 	
				 warn('<s:text name="fromDate.todaysDate.validate" />');			  					 
				 document.getElementById('fromDate').value=""; 
				 document.getElementById('fromDate').focus();
				 return false;
			}
			if(compareDate(todate,todaysDate) == -1)
			{						 
				  warn('<s:text name="toDate.todaysDate.validate" />');			  								 
				  document.getElementById('toDate').value="";  
				  document.getElementById('toDate').focus();
				  return false;
			}
			if(compareDate(fromdate,todate) == -1)
			{							
				 warn('<s:text name="fromDate.toDate.validate" />');			  
				 document.getElementById('fromDate').value="";
				 document.getElementById('toDate').value="";    
				 document.getElementById('fromDate').focus();
				 return false;
			}
			
       }
    }
    return true;
    }
</script>
<div>
<table width="100%" border="0" cellspacing="0" cellpadding="2">
		<s:if test="%{reportType=='hospitalRegReport' || reportType=='summaryMonthlyReport'|| reportType=='aggregateReport'||reportType=='cancelledReport'}">		
			<tr>
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="8%"><s:text name="recordtype.lbl"/></td> 
				<td class="bluebox"><s:radio list="registrationTypeList" value="%{regType}" name="regType" id="regType" onclick="" /></td>
				<td class="bluebox">&nbsp;</td>
			</tr>
		</s:if>
			
		<s:if test="%{reportType=='certificateReport'}">
			
			<tr>
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="8%"><s:text name="recordtype.lbl"/></td> 
				<td class="bluebox"><s:radio list="registrationReportList" value="%{regType}" name="regType" id="regType" onclick="" /></td>
				<td class="bluebox">&nbsp;</td>
			</tr>
		</s:if>
</table>
		
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
			
			<s:if test="%{reportType=='summaryMonthlyReport' || reportType=='yearWiseReport'}">
			
			<tr>
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="8%"><s:text name="month.lbl"/></td>
				<td class="bluebox" ><s:select  id="month" name="month"  value="%{month}" list="monthMap"/></td>
				<td class="bluebox" width="8%"><s:text name="year.lbl"/><span class="mandatory">*</span> </td>
				<td class="bluebox" ><s:textfield id="regYear" name="regYear" value="%{regYear}" maxlength="4"  onblur="return validateYear(this);"/></td>
				<td class="bluebox">&nbsp;</td>
			</tr>	
			
			</s:if>
			
			
				
			
			<s:if test="%{reportType=='regUnitWiseReport'}">					
			<tr>
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="8%"><s:text name="month.lbl"/></td>
				<td class="bluebox" ><s:select  id="month" name="month"  value="%{month}" list="monthMap"/></td>
				<td class="bluebox" width="8%"><s:text name="regunit.lbl"/><span class="mandatory">*</span> </td>
				<td class="bluebox" ><s:select name="regUnitId" id="regUnitId" list="dropdownData.registrationUnitList" value="%{regUnitId}" listKey="id" listValue="regUnitDesc" headerKey="-1"  headerValue="---choose---" /></td>
				<td class="bluebox">&nbsp;</td>
			</tr>
			</s:if>	
			
			<s:if test="%{reportType=='delayedRegistrationReport'||reportType=='efficiencyReport'}">
			<tr>
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="8%"><s:text name="year.lbl"/><span class="mandatory">*</span></td>
				<td class="bluebox" ><s:textfield id="regYear" name="regYear" value="%{regYear}" maxlength="4"  onblur="return validateYear(this);"/></td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
			</tr>
			</s:if>
			
			<s:if test="%{reportType=='hospitalRegReport'|| reportType=='aggregateReport'}">
				<tr>
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="8%"><s:text name="from.date.lbl"/><span class="mandatory">*</span></td>
				<td class="bluebox">
					<s:date format="dd/MM/yyyy" name="fromDate" var="TempDate"/>
					<s:textfield name="fromDate" id="fromDate"  maxlength="20" value="%{TempDate}"  onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" />
					<a href="javascript:show_calendar('forms[0].fromDate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true; "><img src="${pageContext.request.contextPath}/common/image/calendaricon.gif" border="0"/></a>				
				</td>  
				<td class="bluebox"><s:text name="to.date.lbl"/><span class="mandatory">*</span></td>
				<td class="bluebox">				
					<s:date name="toDate" format="dd/MM/yyyy" var="dateTemp"/>
					<s:textfield name="toDate" id="toDate" maxlength="20" value="%{dateTemp}" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" />
					<a href="javascript:show_calendar('forms[0].toDate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true; "><img src="${pageContext.request.contextPath}/common/image/calendaricon.gif" border="0"/></a>			
					</td>			
				<td class="bluebox">&nbsp;</td>
			</tr>
			</s:if>
		 	<s:if test="%{reportType=='hospitalRegReport'}">
			 <tr>
           			<td class="bluebox">&nbsp;</td>
           			<td class="bluebox"><s:text name="hospital.name"/><span class="mandatory">* </span></td>
            		<td class="bluebox"><s:select name="establishment" id="establishment" list="dropdownData.establishmentList" value="%{establishment}" listKey="id" listValue="name" headerKey="0" headerValue="---choose---" />
           			</td>
           			<td class="bluebox" ><s:text name="status.lbl"/> </td>
           			<td class="bluebox" ><s:select  id="status" name="status" headerKey="0" headerValue="---choose---"  value="%{status}" list="statusMap"/></td>
           			<td class="bluebox">&nbsp;</td>
           	</tr>
           	</s:if>
           	
           	<s:if test="%{reportType=='certificateReport'}">
           	<tr>
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="8%"><s:text name="registration.number.lbl"/><span class="mandatory">* </span></td>
				<td class="bluebox" ><s:textfield id="regNo" name="regNo" value="%{regNo}" /></td>
				<td class="bluebox" width="8%"><s:text name="year.lbl"/><span class="mandatory">* </span> </td>
				<td class="bluebox" ><s:textfield id="regYear" name="regYear" value="%{regYear}" maxlength="4"  onblur="return validateYear(this);"/></td>
				<td class="bluebox">&nbsp;</td>
			</tr>
			
			<tr>
			    <td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="8%"><s:text name="regunit.lbl"/><span class="mandatory">* </span> </td>
				<td class="bluebox" ><s:select name="regUnitId" id="regUnitId" list="dropdownData.registrationUnitList" value="%{regUnitId}" listKey="id" listValue="regUnitDesc" headerKey="-1"  headerValue="---choose---" /></td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
			    <td class="bluebox">&nbsp;</td>
			</tr>
			</s:if>
			
			<s:if test="%{reportType=='summaryMonthlyReport'}">
			<tr>
			    <td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="8%"><s:text name="regunit.lbl"/></td>
				<td class="bluebox" ><s:select name="regUnitId" id="regUnitId" list="dropdownData.registrationUnitList" value="%{regUnitId}" listKey="id" listValue="regUnitDesc" headerKey="-1"  headerValue="---choose---" /></td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
			    <td class="bluebox">&nbsp;</td>
			</tr>
			</s:if>
			
				<s:if test="%{reportType=='cancelledReport'}">
				
				
				<tr>
				<td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="8%"><s:text name="from.date.lbl"/><span class="mandatory">*</span></td>
				<td class="bluebox">
					<s:date format="dd/MM/yyyy" name="fromDate" var="TempDate"/>
					<s:textfield name="fromDate" id="fromDate"  maxlength="20" value="%{TempDate}"  onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" />
					<a href="javascript:show_calendar('forms[0].fromDate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true; "><img src="${pageContext.request.contextPath}/common/image/calendaricon.gif" border="0"/></a>				
				</td>  
				<td class="bluebox"><s:text name="to.date.lbl"/><span class="mandatory">*</span></td>
				<td class="bluebox">				
					<s:date name="toDate" format="dd/MM/yyyy" var="dateTemp"/>
					<s:textfield name="toDate" id="toDate" maxlength="20" value="%{dateTemp}" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" />
					<a href="javascript:show_calendar('forms[0].toDate');" onmouseover="window.status='Date Picker';return true;"  onmouseout="window.status='';return true; "><img src="${pageContext.request.contextPath}/common/image/calendaricon.gif" border="0"/></a>			
					</td>			
				<td class="bluebox">&nbsp;</td>
			</tr>
			
			<tr>
			    <td class="bluebox" width="20%">&nbsp;</td>
				<td class="bluebox" width="8%"><s:text name="regunit.lbl"/></td>
				<td class="bluebox" ><s:select name="regUnitId" id="regUnitId" list="dropdownData.registrationUnitList" value="%{regUnitId}" listKey="id" listValue="regUnitDesc" headerKey="-1"  headerValue="---choose---" /></td>
				<td class="bluebox">&nbsp;</td>
				<td class="bluebox">&nbsp;</td>
			    <td class="bluebox">&nbsp;</td>
			</tr>
			</s:if>
			
		</table>
</div>
