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

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<div class="panel-heading " align="text-center">
				<div class="panel-title">
					Assigned Department
				</div>					
			</div>
			<table class="table table-striped table-bordered" id="estimateDetails">
					<thead>
					      <tr>
					     <th class="text-center">Is Primary Dept</th>
							<th class="text-center">Department :<span class="mandatory"></span></th>
							<th class="text-center">Assigned To :<span class="mandatory"></span></th>
							<th class="text-center">Date Of Receipt Of PWR (DD/MM/YYYY)<span class="mandatory"></span></th>
							<th class="text-center">Add/Delete Department</th>
					      </tr>
				         </thead>
					<tbody>
					      <tr class="">
					      	<td class="text-center"><input type ="checkbox" id="activeid" name="legalcaseDepartment[0].isPrimaryDepartment" id="legalcaseDepartment[0].isPrimaryDepartment"  /></td>
							<td class="text-right"><input type="text" class="form-control table-input text-right" data-pattern="alphanumerichyphenbackslash" name="legalcaseDepartment[0].department.name" id="legalcaseDepartment[0].department.name" maxlength="50" ></td>
							<td class="text-right"><input type="text" class="form-control table-input" name="legalcaseDepartment[0].positionAndEmpName" id="legalcaseDepartment[0].positionAndEmpName" maxlength="256"></td>
							<td class="text-right"><input type="text"  name="legalcaseDepartment[0].receiptOfPwr" id="legalcaseDepartment[0].receiptOfPwr"
							class="form-control datepicker"
			title="Please enter a valid date" pattern="\d{1,2}/\d{1,2}/\d{4}"
			data-date-end-date="-1d" 
			data-inputmask="'mask': 'd/m/y'"  ></td>
							<td class="text-center"><span style="cursor:pointer;" id="addRowId"><i class="fa fa-plus"></i></span></td>
					      </tr>
					     
					     
					</tbody>
				</table>
				
	
	
	
	
	