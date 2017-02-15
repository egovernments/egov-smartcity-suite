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

<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<div class="row printable">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0"
			style="text-align: left">
			<div class="panel-heading">
				<div class="panel-title">
					<s:text name="tax.dues"/>
				</div>
			</div>
			<div class="panel-body">
				<div class="row add-border">
					<div class="col-md-3 col-xs-3 add-margin">
						<s:text name="Assesement Number" />
					</div>
					<div class="col-md-3 col-xs-3 add-margin view-content">
						<span class="bold"><s:property default="N/A" value="%{assessmentNo}" /></span>
					</div>
				</div>	
				<div class="row add-border">	
					<div class="col-md-3 col-xs-3 add-margin">
						<s:text name="Owner Name" />
					</div>
					<div class="col-md-3 col-xs-3 add-margin view-content">
						<span class="bold"><s:property default="N/A" value="%{propertyOwner}" /></span>
					</div>				
					<div class="col-md-3 col-xs-3 add-margin">
						<s:text name="Door Number" />
					</div>
					<div class="col-md-3 col-xs-3 add-margin view-content">
						<span class="bold"> <s:property default="N/A" value="%{houseNo}" /></span>
					</div>
				</div>	
				<div class="row add-border">	
					<div class="col-md-3 col-xs-3 add-margin">
						<s:text name="CurrentTax" />
					</div>
					<div class="col-md-3 col-xs-3 add-margin view-content">
						<span class="bold">
						<s:text name="rs"/> <s:property default="N/A" value="%{currentPropertyTax}" /></span>
					</div>
					<div class="col-md-3 col-xs-3 add-margin">
						<s:text name="CurrentTaxDue" />
					</div>
					<div class="col-md-3 col-xs-3 add-margin view-content">
						<span class="bold"><s:text name="rs"/> <s:property default="N/A" value="%{currentPropertyTaxDue}" /></span>
					</div>
				</div>	
				<div class="row add-border">	
					<div class="col-md-3 col-xs-3 add-margin">
						<s:text name="waterTaxDue"/>
					</div>
					<div class="col-md-3 col-xs-3 add-margin view-content">
						<span class="bold"><s:text name="rs"/> <s:property default="N/A" value="%{currentWaterTaxDue}" /></span>
					</div>
					<div class="col-md-3 col-xs-3 add-margin">
						<s:text name="ArrearsDue" />
					</div>
					<div class="col-md-3 col-xs-3 add-margin view-content">
						<s:text name="rs"/>
						<span class="bold"><s:text name="rs"/> <s:property default="N/A" value="%{arrearPropertyTaxDue}" /></span>
					</div>
				</div>
				<div class="mandatory">
					<s:property value="%{taxDueErrorMsg}"/>
				</div>
			</div>
		</div>
	</div>
</div>
<div colspan="3" align="center">
				<input type="button" id="print" class="button printbtn" value="Print" class="print" />
				&nbsp;&nbsp;
				<input type="button" id="close" value="Close" class="button" onclick="javascript:window.close();" />
</div>
<script src="<cdn:url  value='/resources/global/js/jquery/plugins/jQuery.print.js' context='/egi'/>"></script>