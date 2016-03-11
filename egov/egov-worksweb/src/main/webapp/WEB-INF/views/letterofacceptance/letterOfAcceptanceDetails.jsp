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

<div class="form-group">
	<label class="col-sm-3 control-label text-right">File No</label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control">
		
	</div>
	<label class="col-sm-2 control-label text-right">File Date</label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control"> 
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right">Estimate Amount</label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control" id="estimateAmount" disabled>
	</div>
	
	<label class="col-sm-2 control-label text-right">Tender Finalized Percentage</label><div class="col-sm-3">
		<div class="input-group" style="margin-bottom: 0;">
            <div class="input-group-btn">
               <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">+ &nbsp;<span class="caret"></span></button>
               <ul class="dropdown-menu">
                  <li><a href="#">+</a></li>
                  <li><a href="#">-</a></li>
                  
               </ul>
            </div>
           <input type="text" class="form-control">
        </div> 
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right">Agreement Amount</label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control">
	</div>
	<label class="col-sm-2 control-label text-right">Date of Agreement</label>
	<div class="col-sm-3 add-margin">
		<form:input path="workOrderDate" name="workOrderDate" type="text" class="form-control" value="${workOrderDate}" maxlength="12" readonly="true" disabled/>
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right">Name of the Agency</label>
	<div class="col-sm-3 add-margin" style="margin-bottom: 0;">
		<div class="input-group">
			<input id="contractor" name="workOrder.contractor" data-pattern="number" min="3" class="form-control patternvalidation" required="required" type="text" maxlength="15"> 
			<span class="input-group-addon"> <i class="fa fa-search specific"></i></span>
		</div>
	</div>
	<label class="col-sm-2 control-label text-right">Contractor Code</label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control" id="contractorCode" disabled>
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right">Prepared By</label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control" id="preparedBy" disabled>
	</div>
</div>

<div class="form-group" style="display: block;">
	<label class="col-sm-3 control-label text-right">Additional Security Deposit</label>
	<div class="col-sm-3 add-margin">
<input type="text" class="form-control">		
	</div>	
    <label class="col-sm-2 control-label text-right">Bank Guarantee</label>
    <div class="col-sm-3 add-margin">
		<textarea class="form-control"></textarea>
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right">EMD Amount<span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin"><input type="text" class="form-control"></div>
	<label class="col-sm-2 control-label text-right">Contract Period (in days)<span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control"> 
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right">Defect Liability Period (in Years)<span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin"><input type="text" class="form-control"></div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right">Work allocated to<span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<select id="" name="waterSource" class="form-control" data-first-option="false" required="required">
			<option value="">
				Select from below
			</option>
  		<!-- 	<option selected="">Assistant Engineer</option> -->
		</select>		
	</div>
	<label class="col-sm-2 control-label text-right">Engineer in charge<span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<select id="" name="waterSource" class="form-control" data-first-option="false" required="required">
			<option value="">
				Select from below
			</option>
		</select> 
	</div>
</div>		