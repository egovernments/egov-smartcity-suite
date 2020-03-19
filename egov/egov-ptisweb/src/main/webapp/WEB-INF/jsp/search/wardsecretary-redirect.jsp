<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2020  eGovernments Foundation
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

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
<script type="text/javascript">
	jQuery(document).ready(
			function() {
				var applicationType = '${applicationType}';
				var applicationSource = '${applicationSource}';
				var transactionId = '${transactionId}';
				var wsPortalRequest = '${wsPortalRequest}';
				if (applicationType == 'Transfer_of_Ownership') {
					var type = 'REGISTERED TRANSFER';
					var namespace = '/ptis'.concat('${actionNamespace}')
							.concat('/new.action');
					var assessmentNo = '${assessmentNum}';
					jQuery('<form>.').attr({
						method : 'post',
						action : namespace,
						target : '_self'
					}).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'assessmentNo',
						name : 'assessmentNo',
						value : assessmentNo
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'applicationSource',
						name : 'applicationSource',
						value : applicationSource
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'type',
						name : 'type',
						value : type
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'transactionId',
						name : 'transactionId',
						value : transactionId
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'wsPortalRequest',
						name : 'wsPortalRequest',
						value : wsPortalRequest
					})).appendTo(document.body).submit();
				}
				if (applicationType == 'Alter_Assessment') {
					var indexNumber = '${assessmentNum}';
					var namespace = '/ptis'.concat('${actionNamespace}')
							.concat('/modifyProperty-modifyForm.action');
					var modifyRsn = 'ADD_OR_ALTER';
					jQuery('<form>.').attr({
						method : 'post',
						action : namespace,
						target : '_self'
					}).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'indexNumber',
						name : 'indexNumber',
						value : indexNumber
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'applicationSource',
						name : 'applicationSource',
						value : applicationSource
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'modifyRsn',
						name : 'modifyRsn',
						value : modifyRsn
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'transactionId',
						name : 'transactionId',
						value : transactionId
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'wsPortalRequest',
						name : 'wsPortalRequest',
						value : wsPortalRequest
					})).appendTo(document.body).submit();
				}
				if (applicationType == 'Bifuracate_Assessment') {
					var indexNumber = '${assessmentNum}';
					var namespace = '/ptis'.concat('${actionNamespace}')
							.concat('/modifyProperty-modifyForm.action');
					var modifyRsn = 'BIFURCATE';
					jQuery('<form>.').attr({
						method : 'post',
						action : namespace,
						target : '_self'
					}).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'indexNumber',
						name : 'indexNumber',
						value : indexNumber
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'applicationSource',
						name : 'applicationSource',
						value : applicationSource
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'modifyRsn',
						name : 'modifyRsn',
						value : modifyRsn
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'transactionId',
						name : 'transactionId',
						value : transactionId
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'wsPortalRequest',
						name : 'wsPortalRequest',
						value : wsPortalRequest
					})).appendTo(document.body).submit();
				}
				if (applicationType == 'Amalgamation') {
					var indexNumber = '${assessmentNum}';
					var namespace = '/ptis'.concat('${actionNamespace}')
							.concat('/amalgamation-newForm.action');
					var modifyRsn = 'AMALG';
					jQuery('<form>.').attr({
						method : 'post',
						action : namespace,
						target : '_self'
					}).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'indexNumber',
						name : 'indexNumber',
						value : indexNumber
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'applicationSource',
						name : 'applicationSource',
						value : applicationSource
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'modifyRsn',
						name : 'modifyRsn',
						value : modifyRsn
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'transactionId',
						name : 'transactionId',
						value : transactionId
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'wsPortalRequest',
						name : 'wsPortalRequest',
						value : wsPortalRequest
					})).appendTo(document.body).submit();
				}
				if (applicationType == 'Revision_Petition') {
					var propertyId = '${assessmentNum}';
					var namespace = '/ptis'.concat('${actionNamespace}')
							.concat('/revPetition-newForm.action');
					var wfType = 'RP';
					jQuery('<form>.').attr({
						method : 'post',
						action : namespace,
						target : '_self'
					}).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'propertyId',
						name : 'propertyId',
						value : propertyId
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'applicationSource',
						name : 'applicationSource',
						value : applicationSource
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'wfType',
						name : 'wfType',
						value : wfType
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'transactionId',
						name : 'transactionId',
						value : transactionId
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'wsPortalRequest',
						name : 'wsPortalRequest',
						value : wsPortalRequest
					})).appendTo(document.body).submit();
				}
				if (applicationType == 'General_Revision_Petition') {
					var propertyId = '${assessmentNum}';
					var namespace = '/ptis'.concat('${actionNamespace}')
							.concat('/genRevPetition-newForm.action');
					var wfType = 'GRP';
					jQuery('<form>.').attr({
						method : 'post',
						action : namespace,
						target : '_self'
					}).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'propertyId',
						name : 'propertyId',
						value : propertyId
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'applicationSource',
						name : 'applicationSource',
						value : applicationSource
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'wfType',
						name : 'wfType',
						value : wfType
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'transactionId',
						name : 'transactionId',
						value : transactionId
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'wsPortalRequest',
						name : 'wsPortalRequest',
						value : wsPortalRequest
					})).appendTo(document.body).submit();
				}
				if (applicationType == 'Tax_Exemption') {
					var assessmentNo = '${assessmentNum}';
					var namespace = '/ptis/exemption/form/wardsecretary/'.concat(assessmentNo);
					jQuery('<form>.').attr({
						method : 'post',
						action : namespace,
						target : '_self'
					}).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'assessmentNo',
						name : 'assessmentNo',
						value : assessmentNo
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'applicationSource',
						name : 'applicationSource',
						value : applicationSource
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'transactionId',
						name : 'transactionId',
						value : transactionId
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'wsPortalRequest',
						name : 'wsPortalRequest',
						value : wsPortalRequest
					})).appendTo(document.body).submit();
				}
				if (applicationType == 'Demolition') {
					var assessmentNo = '${assessmentNum}';
					var namespace = '/ptis/property/demolition/form/'.concat(assessmentNo).concat('/').concat(applicationSource);
					jQuery('<form>.').attr({
						method : 'post',
						action : namespace,
						target : '_self'
					}).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'assessmentNo',
						name : 'assessmentNo',
						value : assessmentNo
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'applicationSource',
						name : 'applicationSource',
						value : applicationSource
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'transactionId',
						name : 'transactionId',
						value : transactionId
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'wsPortalRequest',
						name : 'wsPortalRequest',
						value : wsPortalRequest
					})).appendTo(document.body).submit();
				}
				if (applicationType == 'Vacancy_Remission') {
					var assessmentNo = '${assessmentNum}';
					var mode = '${mode}';
					var namespace = '/ptis/vacancyremission/create/form/'.concat(assessmentNo).concat(',').concat(mode);
					jQuery('<form>.').attr({
						method : 'post',
						action : namespace,
						target : '_self'
					}).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'assessmentNo',
						name : 'assessmentNo',
						value : assessmentNo
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'applicationSource',
						name : 'applicationSource',
						value : applicationSource
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'mode',
						name : 'mode',
						value : mode
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'transactionId',
						name : 'transactionId',
						value : transactionId
					})).append(jQuery('<input>').attr({
						type : 'hidden',
						id : 'wsPortalRequest',
						name : 'wsPortalRequest',
						value : wsPortalRequest
					})).appendTo(document.body).submit();
				}
			});
</script>
</head>
<body>
</body>
</html>
