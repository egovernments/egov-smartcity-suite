<!-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#     accountability and the service delivery of the government  organizations.
#  
#      Copyright (C) <2015>  eGovernments Foundation
#  
#      The updated version of eGov suite of products as by eGovernments Foundation 
#      is available at http://www.egovernments.org
#  
#      This program is free software: you can redistribute it and/or modify
#      it under the terms of the GNU General Public License as published by
#      the Free Software Foundation, either version 3 of the License, or
#      any later version.
#  
#      This program is distributed in the hope that it will be useful,
#      but WITHOUT ANY WARRANTY; without even the implied warranty of
#      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#      GNU General Public License for more details.
#  
#      You should have received a copy of the GNU General Public License
#      along with this program. If not, see http://www.gnu.org/licenses/ or 
#      http://www.gnu.org/licenses/gpl.html .
#  
#      In addition to the terms of the GPL license to be adhered to in using this
#      program, the following additional terms are to be complied with:
#  
#  	1) All versions of this program, verbatim or modified must carry this 
#  	   Legal Notice.
#  
#  	2) Any misrepresentation of the origin of the material is prohibited. It 
#  	   is required that all modified versions of this material be marked in 
#  	   reasonable ways as different from the original version.
#  
#  	3) This license does not grant any rights to any user of the program 
#  	   with regards to rights under trademark law for use of the trade names 
#  	   or trademarks of eGovernments Foundation.
#  
#    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div class="panel-heading custom_form_panel_heading">
    <div class="panel-title">Trade Details</div>
</div>

<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='licensee.aadhaarNo' /></label>
    <div class="col-sm-3 add-margin">
    	<s:property value="%{licensee.uid}" />
    </div>
    <label class="col-sm-2 control-label text-right"><s:text name='licensee.mobileNo' /></label>
    <div class="col-sm-3 add-margin">
    	<s:property value="%{licensee.mobilePhoneNumber}" />
    </div>
</div>

<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='licensee.applicantName' /></label>
    <div class="col-sm-3 add-margin">
   	 <s:property value="%{licensee.applicantName}" />
    </div>
    <label class="col-sm-2 control-label text-right"><s:text name='licensee.father/spouse' /></label>
    <div class="col-sm-3 add-margin">
    <s:property value="%{licensee.fatherOrSpouseName}" />
    </div>
</div>
<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='licensee.emailId' /></label>
    <div class="col-sm-3 add-margin">
     <s:property value="%{licensee.emailId}" />
    </div>
    <label class="col-sm-2 control-label text-right"><s:text name='licensee.applicantAddress' /></label>
    <div class="col-sm-3 add-margin">
     <s:property value="%{licensee.address}" />
    </div>
</div>


<div class="panel-heading custom_form_panel_heading">
    <div class="panel-title"><s:text name='license.location.lbl' /></div>
</div>
<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='license.propertyNo.lbl' /></label>
    <div class="col-sm-3 add-margin">
          <s:property value="%{propertyNo}" />
    </div>
  
    <label class="col-sm-2 control-label text-right"><s:text name='license.locality.lbl' /></label>
    <div class="col-sm-3 add-margin">
    	<s:property value="%{boundary.name}" />
    </div>
</div>

<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='license.zone.lbl' /></label>
    <div class="col-sm-3 add-margin">
     	<s:property value="%{zoneName}" />
    </div>
    
    <label class="col-sm-2 control-label text-right"><s:text name='license.ward.lbl' /></label>
    <div class="col-sm-3 add-margin">
    	<s:property value="%{wardName}" />
    </div>
</div>

<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='license.ownerShipType.lbl' /></label>
    <div class="col-sm-3 add-margin">
    	<s:property value="%{ownershipType}" />
    </div>
    
    <label class="col-sm-2 control-label text-right"><s:text name='license.address.lbl' /></label>
    <div class="col-sm-3 add-margin">
      <s:property value="%{address}" />
    </div>
</div>


<div class="panel-heading custom_form_panel_heading">
    <div class="panel-title"><s:text name='license.details.lbl' /></div>
</div>

<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='license.nameOfEst.lbl' /></label>
    <div class="col-sm-3 add-margin">
     	<s:property value="%{nameOfEstablishment}" />
    </div>
  
    <label class="col-sm-2 control-label text-right"><s:text name='license.tradeType.lbl' /></label>
    <div class="col-sm-3 add-margin">
    	<s:property value="%{buildingType.name}" />
    </div>
</div>

<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='license.category.lbl' /></label>
    <div class="col-sm-3 add-margin">
    	<s:property value="%{category.name}" />
    </div>
    
    <label class="col-sm-2 control-label text-right"><s:text name='license.subCategory.lbl' /></label>
    <div class="col-sm-3 add-margin">
    	<s:property value="%{tradeName.name}" />
    </div>
</div>

<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='license.premises.lbl' /></label>
    <div class="col-sm-3 add-margin">
    	<s:property value="%{tradeArea_weight}" />
    </div>
    <label class="col-sm-2 control-label text-right"><s:text name='license.uom.lbl' /></label>
    <div class="col-sm-3 add-margin">
    	<s:property value="%{uom.name}" />
    </div>
</div>

<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='license.remarks.lbl' /></label>
    <div class="col-sm-3 add-margin">
   		 <s:property value="%{remarks}" />
    </div>
</div>