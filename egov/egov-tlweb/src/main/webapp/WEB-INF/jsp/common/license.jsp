<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2018  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script>
    $(document).ready(function () {
        $('#subCategory').change(function () {
            getUom();
        });
        <s:if test="%{hasErrors() || mode=='view' || mode=='edit'}">
        if ($('#subCategory').val() != '-1') {
            getUom();
        }
        </s:if>
    });

</script>
<div class="panel-heading custom_form_panel_heading">
    <div class="panel-title"><s:text name='license.details.lbl'/></div>
</div>
<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='license.establishmentname'/><span
            class="mandatory"></span></label>
    <div class="col-sm-3 add-margin">
        <s:textfield name="nameOfEstablishment" cssClass="form-control newpatternvalidation" id="nameOfEstablishment"
                     value="%{nameOfEstablishment}" maxlength="250" onBlur="checkLength(this,250)" required="true"/>
        <div id="nameOfEstablishment_error" class="error-msg" style="display:none;" align="left"></div>
        <s:fielderror fieldName="model.nameOfEstablishment" cssClass="error-msg"/>
    </div>

    <label class="col-sm-2 control-label text-right"><s:text name='license.tradeType.lbl'/><span
            class="mandatory"></span></label>
    <div class="col-sm-3 add-margin">
        <s:select name="natureOfBusiness" id="buildingType" list="dropdownData.tradeTypeList"
                  listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('default.select')}"
                  value="%{natureOfBusiness.id}"
                  required="true" class="form-control"/>
        <div id="natureOfBusiness_error" class="error-msg" style="display:none;" align="left"></div>
        <s:fielderror fieldName="model.natureOfBusiness" cssClass="error-msg"/>
    </div>
</div>
<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='license.category.lbl'/><span
            class="mandatory"></span></label>
    <div class="col-sm-3 add-margin">
        <s:select name="category" id="category" list="dropdownData.categoryList" listKey="id" listValue="name"
                  headerKey="-1" headerValue="%{getText('default.select')}" value="%{category.id}" required="true"
                  class="form-control"/>
        <div id="category_error" class="error-msg" style="display:none;" align="left"></div>
        <s:fielderror fieldName="model.category" cssClass="error-msg"/>
    </div>

    <label class="col-sm-2 control-label text-right"><s:text name='license.subCategory.lbl'/><span
            class="mandatory"></span></label>
    <div class="col-sm-3 add-margin">
        <s:select name="tradeName" id="subCategory" list="dropdownData.subCategoryList" listKey="id" listValue="name"
                  required="true"
                  headerKey="-1" headerValue="%{getText('default.select')}" value="%{tradeName.id}"
                  class="form-control select2"/>
        <div id="subCategory_error" class="error-msg" style="display:none;" align="left"></div>
        <s:fielderror fieldName="model.tradeName" cssClass="error-msg"/>
    </div>
</div>

<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='license.premises.lbl'/><span
            class="mandatory"></span></label>
    <div class="col-sm-3 add-margin">
        <s:textfield name="tradeArea_weight" maxlength="8" id="tradeArea_weight" value="%{tradeArea_weight}"
                     cssClass="form-control patternvalidation"
                     required="true" data-pattern="number"/>
        <div id="tradeArea_weight_error" class="error-msg" style="display:none;" align="left"></div>
        <s:fielderror fieldName="model.tradeArea_weight" cssClass="error-msg"/>
    </div>
    <label class="col-sm-2 control-label text-right"><s:text name='license.uom.lbl'/><span
            class="mandatory"></span></label>
    <div class="col-sm-3 add-margin">
        <s:textfield name="uom" maxlength="20" id="uom"
                     value="%{tradeName.licenseSubCategoryDetails.iterator.next.uom.name}" readOnly="true"
                     required="true" class="form-control"/>
        <div id="uom_error" class="error-msg" style="display:none;" align="left"></div>
        <s:fielderror fieldName="model.uom" cssClass="error-msg"/>
    </div>
</div>

<div class="form-group">
    <label class="col-sm-3 control-label text-right"><s:text name='license.startdate'/><span
            class="mandatory"></span></label>
    <div class="col-sm-3 add-margin">
        <s:date name="commencementDate" format="dd/MM/yyyy" var="commencementDateFrmttd"/>
        <s:textfield name="commencementDate" cssClass="form-control datepicker" required="true" id="startDate"
                     maxlength="10" value="%{commencementDateFrmttd}"/>
        <s:fielderror fieldName="model.commencementDate" cssClass="error-msg"/>
    </div>
    <label class="col-sm-2 control-label text-right"><s:text name='license.remarks'/></label>
    <div class="col-sm-3 add-margin">
        <s:textarea name="remarks" id="remarks" value="%{remarks}" maxlength="512" class="form-control" rows="3"/>
        <s:fielderror fieldName="model.remarks" cssClass="error-msg"/>
    </div>
</div>

<!-- Labour-Details -->
<div id="labourDetailFields">
	<div class="panel-heading custom_form_panel_heading">
	    <div class="panel-title"><s:text name='license.labour.details.lbl'/></div>
	</div>
	<div class="form-group">
	    <label class="col-sm-3 control-label text-right"><s:text name='license.labour.classification.lbl'/><span
	            class="mandatory"></span></label>
	    <div class="col-sm-3 add-margin">
	        <s:select name="classificationType" id="classificationType" list="dropdownData.classificationList"
	                  listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('default.select')}"
	                  value="%{classificationType.id}"
	                  class="form-control"/>
	        <div id="classificationType_error" class="error-msg" style="display:none;" align="left"></div>
	    </div>
	    <label class="col-sm-2 control-label text-right"><s:text name='license.labour.employers.lbl'/><span
	            class="mandatory"></span></label>
	    <div class="col-sm-3 add-margin">
	        <s:select name="employersType" id="employersType" list="dropdownData.employersList"
	                  listKey="id" listValue="name" headerKey="-1" headerValue="%{getText('default.select')}"
	                  value="%{employersType.id}" class="form-control"/>
	        <div id="employersType_error" class="error-msg" style="display:none;" align="left"></div>
	    </div>
	</div>
	<div class="form-group">
	    <label class="col-sm-3 control-label text-right"><s:text name='license.labour.mandal.lbl'/><span
	            class="mandatory"></span></label>
	    <div class="col-sm-3 add-margin">
	        <s:textfield name="mandalName" cssClass="form-control newpatternvalidation" id="mandalName"
	                     value="%{mandalName}" maxlength="250" onBlur="checkLength(this,250)"/>
	        <div id="mandalName_error" class="error-msg" style="display:none;" align="left"></div>
	    </div>
	
	    <label class="col-sm-2 control-label text-right"><s:text name='license.labour.doorno.lbl'/><span
	            class="mandatory"></span></label>
	    <div class="col-sm-3 add-margin">
	        <s:textfield name="doorNo" cssClass="form-control newpatternvalidation" id="doorNo"
	                     value="%{doorNo}" maxlength="250" onBlur="checkLength(this,250)"/>
	        <div id="doorNo_error" class="error-msg" style="display:none;" align="left"></div>
	    </div>
	</div>
	<div class="form-group">
	    <label class="col-sm-3 control-label text-right"><s:text name='license.labour.direct.workers.lbl'/><span
	            class=""></span></label>
	</div>
	<div class="form-group">
	    <label class="col-sm-3 control-label text-right"><s:text name='license.labour.male.lbl'/><span
	            class="mandatory"></span></label>
	    <div class="col-sm-3 add-margin">
	        <s:textfield name="directWorkerMale" cssClass="form-control newpatternvalidation" id="directWorkerMale"
	                     value="%{directWorkerMale}" maxlength="5" onBlur="checkLength(this,250);" 
	                     onchange="calculateTotalWorkers();" type="text" 
	                     onkeypress='return event.charCode >= 48 && event.charCode <= 57'/>
	        <div id="directWorkerMale_error" class="error-msg" style="display:none;" align="left"></div>
	    </div>
	
	    <label class="col-sm-2 control-label text-right"><s:text name='license.labour.female.lbl'/><span
	            class="mandatory"></span></label>
	    <div class="col-sm-3 add-margin">
	        <s:textfield name="directWorkerFemale" cssClass="form-control newpatternvalidation" id="directWorkerFemale"
	                     value="%{directWorkerFemale}" maxlength="5" onBlur="checkLength(this,250);" 
	                     onchange="calculateTotalWorkers();" type="text" 
	                     onkeypress='return event.charCode >= 48 && event.charCode <= 57'/>
	        <div id="directWorkerFemale_error" class="error-msg" style="display:none;" align="left"></div>
	    </div>
	</div>
	<div class="form-group">
	    <label class="col-sm-3 control-label text-right"><s:text name='license.labour.contract.workers.lbl'/><span
	            class=""></span></label>
	</div>
	<div class="form-group">
	    <label class="col-sm-3 control-label text-right"><s:text name='license.labour.male.lbl'/><span
	            class="mandatory"></span></label>
	    <div class="col-sm-3 add-margin">
	        <s:textfield name="contractWorkerMale" cssClass="form-control newpatternvalidation" id="contractWorkerMale"
	                     value="%{contractWorkerMale}" maxlength="5" onBlur="checkLength(this,250);" 
	                     onchange="calculateTotalWorkers();" type="text" 
	                     onkeypress='return event.charCode >= 48 && event.charCode <= 57'/>
	        <div id="contractWorkerMale_error" class="error-msg" style="display:none;" align="left"></div>
	    </div>
	
	    <label class="col-sm-2 control-label text-right"><s:text name='license.labour.female.lbl'/><span
	            class="mandatory"></span></label>
	    <div class="col-sm-3 add-margin">
	        <s:textfield name="contractWorkerFemale" cssClass="form-control newpatternvalidation" id="contractWorkerFemale"
	                     value="%{contractWorkerFemale}" maxlength="5" onBlur="checkLength(this,250);" 
	                     onchange="calculateTotalWorkers();" type="text" 
	                     onkeypress='return event.charCode >= 48 && event.charCode <= 57'/>
	        <div id="contractWorkerFemale_error" class="error-msg" style="display:none;" align="left"></div>
	    </div>
	</div>
	<div class="form-group">
	    <label class="col-sm-3 control-label text-right"><s:text name='license.labour.daily.wages.lbl'/><span
	            class=""></span></label>
	</div>
	<div class="form-group">
	    <label class="col-sm-3 control-label text-right"><s:text name='license.labour.male.lbl'/><span
	            class="mandatory"></span></label>
	    <div class="col-sm-3 add-margin">
	        <s:textfield name="dailyWagesMale" cssClass="form-control newpatternvalidation" id="dailyWagesMale"
	                     value="%{dailyWagesMale}" maxlength="5" onBlur="checkLength(this,250);" 
	                     onchange="calculateTotalWorkers();" type="text" 
	                     onkeypress='return event.charCode >= 48 && event.charCode <= 57'/>
	        <div id="dailyWagesMale_error" class="error-msg" style="display:none;" align="left"></div>
	    </div>
	
	    <label class="col-sm-2 control-label text-right"><s:text name='license.labour.female.lbl'/><span
	            class="mandatory"></span></label>
	    <div class="col-sm-3 add-margin">
	        <s:textfield name="dailyWagesFemale" cssClass="form-control newpatternvalidation" id="dailyWagesFemale"
	                     value="%{dailyWagesFemale}" maxlength="5" onBlur="checkLength(this,250);" 
	                     onchange="calculateTotalWorkers();" type="text" 
	                     onkeypress='return event.charCode >= 48 && event.charCode <= 57'/>
	        <div id="dailyWagesFemale_error" class="error-msg" style="display:none;" align="left"></div>
	    </div>
	</div>
	<div class="form-group">
	    <label class="col-sm-3 control-label text-right"><s:text name='license.labour.total.lbl'/><span
	            class="mandatory"></span></label>
	    <div class="col-sm-3 add-margin">
	        <s:textfield name="totalWorkers" cssClass="form-control newpatternvalidation" id="totalWorkers"
	                     value="%{totalWorkers}" maxlength="25" onBlur="checkLength(this,25)" readonly="true"/>
	        <div id="totalWorkers_error" class="error-msg" style="display:none;" align="left"></div>
	    </div>
	</div>
</div>