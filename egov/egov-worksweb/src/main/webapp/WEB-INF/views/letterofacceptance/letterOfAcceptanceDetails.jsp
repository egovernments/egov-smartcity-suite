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
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.file.no" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control">
		
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message code="lbl.file.date" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control"> 
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.estimateamount" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control" id="estimateAmount" disabled>
	</div>
	
	<label class="col-sm-2 control-label text-right"><spring:message code="lbl.tender.finalized.percentage" /></label><div class="col-sm-3">
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
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.agreement.amount" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control">
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message code="lbl.dateofagreement" /></label>
	<div class="col-sm-3 add-margin">
		<input id="workOrderDate" name="workOrderDate" type="text" class="form-control" maxlength="12" disabled/>
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.nameofagency" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin" style="margin-bottom: 0;">
		<div class="input-group">
			<input id="contractor" name="workOrder.contractor" data-pattern="number" min="3" class="form-control patternvalidation" required="required" type="text" maxlength="15"> 
			<span class="input-group-addon"> <i class="fa fa-search specific"></i></span>
		</div>
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message code="lbl.contractor.code" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control" id="contractorCode" disabled>
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.preparedby" /></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control" id="preparedBy" disabled>
	</div>
</div>

<div class="form-group" style="display: block;">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.additional.security.deposit" /></label>
	<div class="col-sm-3 add-margin">
<input type="text" class="form-control">		
	</div>	
    <label class="col-sm-2 control-label text-right"><spring:message code="lbl.bank.guarantee" /></label>
    <div class="col-sm-3 add-margin">
		<textarea class="form-control"></textarea>
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.emd.amount" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin"><input type="text" class="form-control"></div>
	<label class="col-sm-2 control-label text-right"><spring:message code="lbl.contract.period" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<input type="text" class="form-control"> 
	</div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.dlp" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin"><input type="text" class="form-control"></div>
</div>

<div class="form-group">
	<label class="col-sm-3 control-label text-right"><spring:message code="lbl.work.allocated.to" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<select id="" name="waterSource" class="form-control" data-first-option="false" required="required">
			<option value="">
				Select from below
			</option>
  		<!-- 	<option selected="">Assistant Engineer</option> -->
		</select>		
	</div>
	<label class="col-sm-2 control-label text-right"><spring:message code="lbl.engineer.incharge" /><span class="mandatory"></span></label>
	<div class="col-sm-3 add-margin">
		<select id="" name="waterSource" class="form-control" data-first-option="false" required="required">
			<option value="">
				Select from below
			</option>
		</select> 
	</div>
</div>		