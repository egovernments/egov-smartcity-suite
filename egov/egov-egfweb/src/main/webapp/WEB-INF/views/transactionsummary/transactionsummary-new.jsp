<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="/WEB-INF/tags/sitemesh-decorator.tld" prefix="decorator"%>
<%@ taglib uri="/WEB-INF/tags/sitemesh-page.tld" prefix="page"%>
<%@ taglib uri="/WEB-INF/tags/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tags/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tags/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tags/struts-nested.tld" prefix="nested"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="egovtags"%>
<%@ taglib uri="/WEB-INF/tags/struts-tags.tld" prefix="s"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sx" uri="/WEB-INF/tags/struts-dojo-tags.tld"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="description" content="eGov System" />
<meta name="author" content="eGovernments Foundation" />

<title></title>
<link rel="icon"
	href="<c:url value='/resources/global/images/favicon.png" sizes="32x32' context='/egi'/>">
<link rel="stylesheet"
	href="<c:url value='/resources/global/css/bootstrap/bootstrap.css' context='/egi'/>">
<link rel="stylesheet"
	href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css'  context='/egi'/>">
<link rel="stylesheet"
	href="<c:url value='/resources/global/css/font-icons/font-awesome-4.3.0/css/font-awesome.min.css' context='/egi'/>">
<link rel="stylesheet"
	href="<c:url value='/resources/global/css/bootstrap/typeahead.css'  context='/egi'/>">
<link rel="stylesheet"
	href="<c:url value='/resources/global/css/egov/custom.css' context='/egi'/>">
<link rel="stylesheet"
	href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css'  context='/egi'/>" />
<script
	src="<c:url value='/resources/global/js/egov/patternvalidation.js' context='/egi'/>"></script>

<script
	src="<c:url value='/resources/global/js/jquery/jquery.js' context='/egi'/>"></script>
<%-- <script type="text/javascript" src="/EGF/resources/javascript/jquery-1.7.2.min.js"></script> --%>
<script
	src="<c:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
<script
	src="<c:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script
	src="<c:url value='/resources/global/js/egov/custom.js' context='/egi'/>"></script>
<script
	src="<c:url value='/resources/global/js/egov/patternvalidation.js' context='/egi'/>"></script>
<script
	src="<c:url value='/resources/global/js/jquery-ui/jquery-ui.js' context='/egi'/>"></script>
<link
	src="<c:url value='/resources/global/js/jquery-ui/jquery-ui.css' context='/egi'/>"></link>
<!--[if lt IE 9]><script src="resources/js/ie8-responsive-file-warning.js"></script><![endif]-->

<link rel="stylesheet" type="text/css"
	href="/egi/commonyui/yui2.8/fonts/fonts-min.css" />
<link rel="stylesheet" type="text/css"
	href="/egi/commonyui/yui2.8/datatable/assets/skins/sam/datatable.css" />
<link rel="stylesheet" type="text/css"
	href="/egi/commonyui/yui2.8/assets/skins/sam/autocomplete.css" />
<!-- <script type="text/javascript" src="/egi/commonyui/yui2.8/animation/animation-min.js"></script> -->
<script type="text/javascript"
	src="/egi/commonyui/yui2.8/yuiloader/yuiloader-min.js"></script>
<script type="text/javascript"
	src="/egi/commonyui/yui2.8/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript"
	src="/egi/commonyui/yui2.8/element/element-min.js"></script>
<script type="text/javascript"
	src="/egi/commonyui/yui2.8/connection/connection-min.js"></script>
<script type="text/javascript"
	src="/egi/commonyui/yui2.8/datasource/datasource-min.js"></script>
<script type="text/javascript"
	src="/egi/commonyui/yui2.8/datatable/datatable-min.js"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/autocomplete.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/helper.js"></script>

<%-- <script type="text/javascript"
			src="${pageContext.request.contextPath}/resources/javascript/voucherHelper.js"></script> --%>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/autocomplete-debug.js"></script>

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
			<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
			<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
		<![endif]-->

</head>
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
	z-index: 9050;
}

#codescontainer .yui-ac-shadow {
	position: absolute;
	margin: .3em;
	width: 300px;
	background: #a0a0a0;
	z-index: 9049;
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
</style>
<body class="page-body" oncontextmenu="return false;"
	onload="loadDropDownCodesFunction();">
	<div class="page-container">
		<jsp:include page="../template/header.jsp" />
		<div class="main-content">
			<form:form role="form" action="create"
				modelAttribute="transactionSummaryDto" id="transactionSummaryform"
				cssClass="form-horizontal form-groups-bordered"
				enctype="multipart/form-data">
				<jsp:include page="transactionsummary-form.jsp" />
		</div>
	</div>
	</div>
	</div>
	<table class="table table-bordered display-hide" id="result">
		<thead>
			<tr>
				<th width="10%"><spring:message code="hdr.accountcode" /><span
					class="mandatory1">*</span></th>
				<th><spring:message code="hdr.accounthead" /></th>
				<th width="15%"><spring:message code="hdr.subledgertype" /></th>
				<th width="10%"><spring:message code="hdr.entity" /></th>
				<th width="15%"><spring:message code="lbl.openingdebitbalance" /><span
					class="mandatory1">*</span></th>
				<th width="15%"><spring:message code="lbl.openingcreditbalance" /><span
					class="mandatory1">*</span></th>
				<th width="15%"><spring:message code="lbl.narration" /></th>
				<th width="2%"><button type="button" id="add-row"
						class="btn btn-xs btn-secondary add-row">
						<span class="glyphicon glyphicon-plus"></span>
					</button></th>
			</tr>
		</thead>
		<tbody>
			<tr id="resultrow0">
				<form:input path="transactionSummaryList[0].id"
					id="transactionSummaryList[0].id" type="hidden" />
				<td><form:input path="transactionSummaryList[0].glcodeid.id"
						id="transactionSummaryList[0].glcodeid.id" type="hidden" /> <input
					id="transactionSummaryList[0].glcodeDetail"
					name="transactionSummaryList[0].glcodeDetail" type="text"
					class="form-control accountcode mandatory" autocomplete="off"
					onkeyup="autocompletecode(this,event)"
					onblur="fillNeibrAfterSplitGlcode(this);" />
					<div id="codescontainer"></div></td>
				<td id="transactionSummaryList[0].accounthead"></td>
				<td><form:select
						path="transactionSummaryList[0].accountdetailtype.id"
						id="transactionSummaryList[0].accountdetailtype.id"
						onchange="changeaccountdetailkey(this)"
						class="form-control yui-dt-dropdown ">
						<option value="">---Select---</option>
					</form:select></td>
				<td><form:input
						path="transactionSummaryList[0].accountdetailkey"
						id="transactionSummaryList[0].accountdetailkey" type="hidden" />
					<input name="transactionSummaryList[0].accountdetailkeyValue"
					id="transactionSummaryList[0].accountdetailkeyValue" type="text"
					readonly="true" autocomplete="off"
					class="form-control yui-ac-input "
					onfocus="onFocusDetailCode(this);autocompleteEntities(this);"
					onblur="splitEntitiesDetailCode(this)" />
					<img src="/egi/resources/erp2/images/plus1.gif" id="transactionSummaryList[0].search" name="transactionSummaryList[0].search" style="width:15px;" onclick="openSearchWindowFromOB(this)">
					<%-- <div id = "transactionSummaryList[0].search" name = "transactionSummaryList[0].search" onclick="openSearchWindowFromOB(this)"><span 
					class="glyphicon glyphicon-search"></span></div> --%></td>
				<td><form:input path="transactionSummaryList[0].openingdebitbalance"
						type="text" readonly="false" style="text-align:right;"
						class="form-control mandatoryField patternvalidation mandatory "
						data-pattern="decimalvalue"
						id="transactionSummaryList[0].openingdebitbalance"
						onchange="makeMandatory(this)" /></td>
				<td><form:input
						path="transactionSummaryList[0].openingcreditbalance" type="text"
						readonly="false" style="text-align:right;"
						class="form-control mandatoryField patternvalidation mandatory"
						data-pattern="decimalvalue"
						id="transactionSummaryList[0].openingcreditbalance"
						onchange="makeMandatory(this)" /></td>
				<td><form:textarea path="transactionSummaryList[0].narration"
						id="transactionSummaryList[0].narration" class="form-control"></form:textarea></td>
			</tr>
		</tbody>
	</table>
	<div class="form-group display-hide" id="buttonCreate">
		<div class="text-center">
			<button type='button' class='btn btn-primary' id="buttonSubmit">
				<spring:message code='lbl.create' />
			</button>
			<a href='javascript:void(0)' class='btn btn-default'
				onclick='self.close()'><spring:message code='lbl.close' /></a>
		</div>
	</div>
	</form:form>
	</div>
	</div>
</body>
<script type="text/javascript"
	src="/EGF/resources/javascript/transactionSummaryHelper.js"></script>