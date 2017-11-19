<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
<div class="panel-heading" style="text-align: center">
	<div class="panel-title" style="text-align: center">
		<s:text name="lbl.amalgamation.title" />
	</div>
</div>
<div class="panel-body">
	<div class="form-group">
		<label class="col-sm-3 control-label text-right"> <s:text
				name="prop.Id" />
		</label>
		<div class="col-sm-3 add-margin">
			<s:property value="%{basicProp.upicNo}" />
			<s:hidden id="retainerPropertyId" name="retainerPropertyId" value="%{basicProp.upicNo}" />
			<s:hidden id="instStartDtId" name="instStartDtId" value="%{instStartDt}" />
		</div>

		<label class="col-sm-2 control-label text-right"> <s:text
				name="PropertyAddress" />
		</label>
		<div class="col-sm-3 add-margin">
			<s:property value="%{propAddress}" />
		</div>
	</div>
</div>

<%@ include file="amalgamatedPropsForm.jsp"%>
<%@ include file="amalgamatedOwnersForm.jsp"%>

<div class="panel-heading" style="text-align: left">
	<div class="panel-title">
		<s:text name="assessmentDetails.title" />
	</div>
</div>


<div class="panel-body">
	<div class="form-group">
		<label class="col-sm-3 control-label text-right"> <s:text
				name="ownership.type" /> :
		</label>
		<div class="col-sm-3 add-margin">
			<s:property value="%{propertyDetail.propertyTypeMaster.type}"
				default="N/A" />
		</div>
		<label class="col-sm-2 control-label text-right"> <s:text
				name="property.type" /> :
		</label>
		<div class="col-sm-3 add-margin">
			<s:select headerKey="-1" headerValue="%{getText('default.select')}"
				name="propertyDetail.categoryType" id="propertyCategory"
				listKey="key" listValue="value" list="propTypeCategoryMap"
				value="%{propertyDetail.categoryType}"
				cssClass="form-control proptypecategory" />
		</div>

	</div>

	<div class="form-group">
		<label class="col-sm-3 control-label text-right"> <s:text
				name="apartcomplex.name" /> :
		</label>
		<div class="col-sm-3 add-margin">
			<s:property default="N/A" value="%{propertyDetail.apartment.name}" />
		</div>
		<label class="col-sm-2 control-label text-right"> <s:text
				name="extent.site" /> <span class="mandatory" /> :
		</label>
		<div class="col-sm-3 add-margin">
			<s:textfield name="areaOfPlot" id="areaOfPlot" size="12"
				maxlength="15" value="%{areaOfPlot}"
				cssClass="form-control patternvalidation"
				data-pattern="decimalvalue" />
		</div>
		
		
	</div>
	<div class="form-group">
		
		<label class="col-sm-3 control-label text-right"> <s:text
				name="certificationNumber" /> :
		</label>
		<div class="col-sm-3 add-margin">
			<s:textfield name="propertyDetail.occupancyCertificationNo"
				cssClass="form-control"
				value="%{propertyDetail.occupancyCertificationNo}"
				cssStyle="width:100%" />
		</div>
		<label class="col-sm-2 control-label text-right"> <s:text
				name="certificationDate" /> :
		</label>
		<div class="col-sm-3 add-margin">
			<s:date
				name="propertyDetail.occupancyCertificationDate"
				format="dd/MM/yyyy" var="occDate"/> 
				<s:textfield
				name="propertyDetail.occupancyCertificationDate"
				value="%{occDate}"
				cssClass="form-control datepicker" cssStyle="width:100"></s:textfield>
		</div>
	</div>
</div>

<div class="panel-body">
	<div class="form-group">
		<%@ include file="../common/addressView.jsp"%>
	</div>
</div>
<s:if test="%{oldPropertyTypeCode!=@org.egov.ptis.constants.PropertyTaxConstants@OWNERSHIP_TYPE_VAC_LAND}">
	<div class="overflow-x-scroll floors-tbl-freeze-column-div">
	<%@ include file="builtupPropDetailsForm.jsp"%>
	</div>
</s:if>

<s:if test="%{!documentTypes.isEmpty()}">
	<%@ include file="../common/DocumentUploadForm_new.jsp"%>
</s:if>
