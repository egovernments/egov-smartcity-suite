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

<script>
	function onSubmit(obj) {
		var serviceName = jQuery("#serviceName").val();
		if (serviceName != null && serviceName != ''
				&& !serviceName.match(/[a-z]/i)) {
			dom.get("error_area").innerHTML = '<s:text name="masters.serviceCategory.serviceCategoryNameValidation" />'
					+ '<br>';
			dom.get("error_area").style.display = "block";
			dom.get("remerror").style.display = "none";
			return false;
		} else {
			document.forms[0].action = obj;
			document.forms[0].submit;
		}
	}

	function resetValues() {
		jQuery(":text").val("");
		jQuery('input[type=checkbox]').attr('checked', false);
	}
</script>

<fieldset>
	<br />
	<table width="100%" border="0" cellspacing="0" cellpadding="0"
		style="max-width: 960px; margin: 0 auto; border-collapse: separate; border-spacing: 10px;">
		<s:token />
		<tr>
			<td class="greybox">&nbsp;</td>
			<td class="greybox"><label for="serviceCategoryName"><s:text
						name="masters.serviceCategory.serviceCategoryName" /><span
					class="mandatory1">*</span> :</label></td>
			<td class="greybox"><s:textfield label="serviceCategoryName"
					id="serviceName" name="model.name" /></td>
		</tr>
		<tr>
			<td class="greybox">&nbsp;</td>
			<td class="greybox"><label for="serviceCode"><s:text
						name="masters.serviceCategory.servicCategoryeCode" /><span
					class="mandatory1">*</span> :</label></td>
			<td class="greybox"><s:textfield label="servicCategoryeCode"
					cssClass="form-control patternvalidation"
					data-pattern="alphanumericwithspace" name="model.code" /></td>
		</tr>
		<tr>
			<td class="greybox">&nbsp;</td>
			<td class="greybox"><label for="isActive"><s:text
						name="masters.serviceCategory.serviceIsEnable/Disable" /> :</label></td>
			<td class="greybox"><s:checkbox label="ischecked"
					name="model.isActive" id="isActive" /></td>
		</tr>
	</table>
</fieldset>

