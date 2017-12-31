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

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title><s:text name='transOwnAck' />
		</title>
		<script type="text/javascript">
		  function printAcknowledgement() {
			  var mutationId = document.getElementById("mutationId").value;
			  window.open('printAck.action?mutationId='+mutationId,
						'_blank', 'width=650, height=500, scrollbars=yes', false);
		  }
          function payment() {
        	  var applicationNo = '<s:property value="%{model.applicationNo}"/>';
        	  window.open('collect-fee.action?applicationNo='+applicationNo,
						'_blank', 'width=650, height=500, scrollbars=yes', false); 
          }
		</script>
	</head>
	<body onload=" refreshParentInbox(); ">
		<s:form name="transPropAckForm" theme="simple">
			<s:push value="model">
			<s:hidden name="mutationId" id="mutationId" value="%{id}"></s:hidden>
			<s:hidden name="applicationSource" value="%{applicationSource}" />
			<s:token/>
				<div class="col-md-12">	
					<div class="panel panel-primary" data-collapsed="0"
			style="text-align: left">
			<div class="panel-heading">
				<div class="panel-title">
					<s:text name="title.transfer.fee.payment"/>
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
					<div class="col-md-3 col-xs-3 add-margin">
						<s:text name="Door Number" />
					</div>
					<div class="col-md-3 col-xs-3 add-margin view-content">
						<span class="bold"> <s:property default="N/A" value="%{model.basicProperty.address.houseNoBldgApt}" /></span>
					</div>
				</div>	
				<div class="row add-border">	
					<div class="col-md-3 col-xs-3 add-margin">
						<s:text name="PropertyAddress" />
					</div>
					<div class="col-md-3 col-xs-3 add-margin view-content">
						<span class="bold"> <s:property default="N/A" value="%{model.basicProperty.address.toString()}" /></span>
					</div>
					<div class="col-md-3 col-xs-3 add-margin">
						<s:text name="applNumber" />
					</div>
					<div class="col-md-3 col-xs-3 add-margin view-content">
						<span class="bold"> <s:property default="N/A" value="%{model.applicationNo}" /> </span>
					</div>	
				</div>	
				<div class="row add-border">	
					<div class="col-md-3 col-xs-3 add-margin">
						<s:text name="applicant.name" />
					</div>
					<div class="col-md-3 col-xs-3 add-margin view-content">
						<span class="bold"> <s:property default="N/A" value="%{propertyOwner}" /> </span>
					</div>
					<div class="col-md-3 col-xs-3 add-margin">
						<s:text name="payablefee"/>
					</div>
					<div class="col-md-3 col-xs-3 add-margin view-content">
						<span class="bold"><s:text name="rs"/> <s:property default="N/A" value="%{model.mutationFee}" /></span>
					</div>	
				</div>	

				<div class="buttonbottom" align="center">
						<input type="button" name="button2" id="button2" value="Download Acknowledgement" class="buttonsubmit" onclick="printAcknowledgement()" />
                        <input type="button" name="button2" id="button2" value="Pay Fee Online" class="buttonsubmit" onclick="payment()" />
						<input type="button" name="button2" id="button2" value="Close" class="button" onclick="window.close();" />
				</div>
					
			</div>
		</div>
				</div>
			</s:push>
		</s:form>
	</body>
</html>
