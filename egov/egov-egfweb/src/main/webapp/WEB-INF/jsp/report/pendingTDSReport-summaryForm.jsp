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


<%@ taglib prefix="s" uri="/WEB-INF/tags/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<head>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js?rnd=${app_release_no}"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/tdsReportHelper.js?rnd=${app_release_no}"></script>
<script type="text/javascript" src="/EGF/resources/javascript/helper.js?rnd=${app_release_no}"></script>
<style type="text/css">
#codescontainer {
	position: absolute;
	left: 11em;
	width: 9%;
	text-align: left;
}

#codescontainer .yui-ac-content {
	position: absolute;
	width: 350px;
	border: 1px solid #404040;
	background: #fff;
	overflow: hidden;
	z-index: 20000;
}

#codescontainer .yui-ac-shadow {
	position: absolute;
	margin: .3em;
	width: 300px;
	background: #a0a0a0;
	z-index: 19999;
}

.yui-skin-sam .yui-ac-input {
	width: 100%;
}

#codescontainer ul {
	padding: 5px 0;
	width: 100%;
}

#codescontainer li {
	padding: 0 5px;
	cursor: default;
	white-space: nowrap;
}

#codescontainer li.yui-ac-highlight {
	background: #ff0;
}

#codescontainer li.yui-ac-prehighlight {
	background: #FFFFCC;
}

.yui-skin-sam tr.yui-dt-odd {
	background-color: #FFF;
}

#detailcodescontainer {
	position: absolute;
	left: 11em;
	width: 9%;
	text-align: left;
}

#detailcodescontainer .yui-ac-content {
	position: absolute;
	width: 350px;
	border: 1px solid #404040;
	background: #fff;
	overflow: hidden;
	z-index: 20000;
}

#detailcodescontainer .yui-ac-shadow {
	position: absolute;
	margin: .3em;
	width: 300px;
	background: #a0a0a0;
	z-index: 19999;
}

#detailcodescontainer ul {
	padding: 5px 0;
	width: 100%;
}

#detailcodescontainer li {
	padding: 0 5px;
	cursor: default;
	white-space: nowrap;
}

#detailcodescontainer li.yui-ac-highlight {
	background: #ff0;
}

#detailcodescontainer li.yui-ac-prehighlight {
	background: #FFFFCC;
}
</style>
</head>
<script>
var callback = {
		success: function(o){
			document.getElementById('results').innerHTML=o.responseText;
			//clearWaitingImage();
			undoLoadingMask();
			},
			failure: function(o) {
		    }
		}
function getData(){
	var asOnDate =  document.getElementById('asOnDate').value;
	var department =  document.getElementById('department').value;
	var fund =  document.getElementById('fund').value;
	var recovery =  document.getElementById('recovery').value;
	var detailKey =  document.getElementById('detailKey').value;
	var partyName =  document.getElementById('partyName').value;
	if(detailKey == 'undefined' && document.getElementById('partyName').value!='')
		detailKey = 0;
	if(detailKey == 'undefined' && document.getElementById('partyName').value=='')
		detailKey = -1;
	isValid = validateData();
	if(isValid == false)
		return false;
	var url = '/EGF/report/pendingTDSReport-ajaxLoadSummaryData.action?skipPrepare=true&asOnDate='+asOnDate+'&department.id='+
							department+'&fund.id='+fund+'&partyName='+partyName+'&recovery.id='+recovery+'&detailKey='+detailKey;
	YAHOO.util.Connect.asyncRequest('POST', url, callback, null);
	//loadWaitingImage();
	doLoadingMask();
}

function exportXls(){
	var asOnDate =  document.getElementById('asOnDate').value;
	var department =  document.getElementById('department').value;
	var fund =  document.getElementById('fund').value;
	var recovery =  document.getElementById('recovery').value;
	var detailKey =  document.getElementById('detailKey').value;
	var partyName =  document.getElementById('partyName').value;
	window.open('/EGF/report/pendingTDSReport-exportSummaryXls.action?skipPrepare=true&asOnDate='+asOnDate+'&department.id='+department+'&fund.id='+fund+
	'&recovery.id='+recovery+'&detailKey='+detailKey+'&partyName='+partyName,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
}

function exportPdf(){
	var asOnDate =  document.getElementById('asOnDate').value;
	var department =  document.getElementById('department').value;
	var fund =  document.getElementById('fund').value;
	var recovery =  document.getElementById('recovery').value;
	var detailKey =  document.getElementById('detailKey').value;
	var partyName =  document.getElementById('partyName').value;
	window.open('/EGF/report/pendingTDSReport-exportSummaryPdf.action?skipPrepare=true&asOnDate='+asOnDate+'&department.id='+department+'&fund.id='+
	fund+'&recovery.id='+recovery+'&detailKey='+detailKey+'&partyName='+partyName,'','resizable=yes,height=650,width=900,scrollbars=yes,left=30,top=30,status=no');
}
function hideIncludeRemittance(){
	document.getElementById('showRemittedEntries').style.display = "none";
	document.getElementById('showRemittedEntrieslabel').style.display = "none";
}
</script>
<body onload="hideIncludeRemittance();loadEntities();">

	<jsp:include page="pendingTDSReport-form.jsp"></jsp:include>
</body>
</html>
