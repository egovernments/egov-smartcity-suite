<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.consumerno" /></label>
	<div class="col-sm-3 add-margin">
		<form:input path="connection.consumerCode"  class="form-control text-left"/>
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.connectiondate" /></label>
	<div class="col-sm-3 add-margin">
		 <input type=text name="executionDate" class="form-control datepicker" data-date-end-date="0d" 
								id="executionDate" data-inputmask="'mask': 'd/m/y'"/>   
	</div>
</div>

<div class="panel-heading custom_form_panel_heading">
	<div class="panel-title">
		<spring:message code="lbl.feedetails" />
	</div>
</div>

<div class="form-group">	
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.monthlyfees" /></label>
	<div class="col-sm-3 add-margin">
		<form:input path="existingConnection.monthlyFee" class="form-control text-right" />  
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.donationcharges" /></label>
	<div class="col-sm-3 add-margin">
		<form:input path="existingConnection.donationCharges" class="form-control text-right" />
	</div>
</div>
<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.arrears" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control text-right" id="existingConnection.arrears"   value="0.00"> 
	</div>	
</div>	

<div class="form-group">	
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.metercost" /></label>
	<div class="col-sm-3 add-margin">
		<form:input path="existingConnection.meterCost" class="form-control text-right" />  
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.metername" /></label>
	<div class="col-sm-3 add-margin">
		<form:input path="existingConnection.meterName" class="form-control text-left" /> 
	</div>
</div>

<div class="form-group">	
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.meterslno" /></label>
	<div class="col-sm-3 add-margin">
		<form:input path="existingConnection.meterNo"  class="form-control text-left"/>   
	</div>
	
</div>

<div class="form-group">	
	<label class="col-sm-3 control-label text-right"><spring:message
			code="lbl.previousreading" /></label>
	<div class="col-sm-3 add-margin">
	<form:input path="existingConnection.previousReading"  class="form-control text-right"/>  
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message
			code="lbl.currentreading" /></label>
	<div class="col-sm-3 add-margin">
		<form:input path="existingConnection.currentReading" class="form-control text-right" />
	</div>
</div>
<div class="buttonbottom" align="center">
	<form:hidden path="" id="workFlowAction" name="workFlowAction"/>
	<table>
		<tr>
			<td>
	
				<form:button type="submit" id="Create" class="btn btn-primary"  value="Create" onclick="validate();">Create </form:button>
				<input type="button" name="button2" id="button2" value="Close" 	class="btn btn-primary" onclick="window.close();" /></td>
		</tr>
	</table>
</div>
				