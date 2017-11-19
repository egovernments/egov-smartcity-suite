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


<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>

<html>
<head>
<title><s:text name="masters.loangrant.search.title" /></title>
<script type="text/javascript"
	src="/EGF/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<style type="text/css">
#codescontainer {
	position: absolute;
	left: 11em;
	width: 9%;
	text-align: left;
}

#codescontainer .yui-ac-content {
	position: absolute;
	width: 600px;
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
<script>
	 function checkuniquenesscode(){
   		return; 	
  }
	</script>
</head>

<body>
	<div class="formmainbox">
		<div class="subheadnew">
			<s:text name="masters.loangrant.search.title" />
		</div>
	</div>
	<br />
	<br />

	<s:form name="loanGrantSearchForm" action="loanGrant" theme="simple">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td class="bluebox"><s:text
						name="masters.subscheme.search.fund" /> <s:if
						test="%{defaultFundId==-1}">
						<span class="mandatory">*</span>
					</s:if></td>
				<td class="bluebox"><s:select name="fundId" id="fundId"
						list="dropdownData.fundList" listKey="id" listValue="name"
						headerKey="-1" headerValue="----Choose----"
						onchange="loadChanges(this)" value="%{fundId.id}" /></td>
				<s:if test="%{defaultFundId!=-1}">
					<script>
		document.getElementById("fundId").value='<s:property value="defaultFundId"/>';
		</script>
				</s:if>
			</tr>
			<tr>
				<td class="greybox"><s:text
						name="masters.subscheme.search.scheme" /><span class="mandatory">*</span></td>
				<s:hidden name="schemeId" id="schemeId" />
				<td class="greybox"><s:textfield
						value="%{subScheme.scheme.name}" name="subScheme.scheme.name"
						id="subScheme.scheme.name" autocomplete='off'
						onFocus="autocompleteSchemeBy20LG();"
						onBlur="splitSchemeCode(this)" /></td>
				<td class="greybox"><s:text name="masters.subscheme.search" /></td>
				<s:hidden name="subSchemeId" id="subSchemeId" />
				<td class="greybox"><s:textfield value="%{subScheme.name}"
						name="subScheme.name" id="subScheme.name" autocomplete='off'
						onFocus="autocompleteSubSchemeBy20LG();"
						onBlur="splitSubSchemeCode(this);checkuniquenesscode();" /> <egov:uniquecheck
						id="codeuniquecode" name="codeuniquecode"
						fieldtoreset="subSchemeId" fields="['Value']"
						url='masters/loanGrant!codeUniqueCheckCode.action' /></td>
			</tr>
			<s:hidden name="mode" />
		</table>
		<div id="codescontainer"></div>
		<br />
		<br />

		<div class="buttonbottom">
			<s:submit method="search" value="Search" onclick="return validate()"
				cssClass="buttonsubmit" />
			<input type="button" value="Close"
				onclick="javascript:window.close()" class="button" />
		</div>

		<s:if test="%{loanGrantHeaderList.size!=0}">
			<table width="40%" border="0" align="center" cellpadding="0"
				cellspacing="0" class="tablebottom">

				<tr>

					<th class="bluebgheadtd" style="width: 2%; text-align: center"
						align="center">Sl No.</th>
					<th class="bluebgheadtd" style="width: 4%; text-align: center"
						align="center">Sub Scheme</th>
				</tr>
				<c:set var="trclass" value="greybox" />
				<s:iterator var="hl" value="loanGrantHeaderList" status="f">
					<tr>

						<td class="<c:out value="${trclass}"/>" style="text-align: center"
							align="center"><s:property value="#f.index+1" /></td>
						<td class="<c:out value="${trclass}"/>" style="text-align: center"
							align="center"><a href="#"
							onclick="urlLoad('<s:property value="id"/>');" id="sourceLink" />
							<s:property value="subScheme.name" /> </a></td>

						<c:choose>
							<c:when test="${trclass=='greybox'}">
								<c:set var="trclass" value="bluebox" />
							</c:when>
							<c:when test="${trclass=='bluebox'}">
								<c:set var="trclass" value="greybox" />
							</c:when>
						</c:choose>
					</tr>
				</s:iterator>

			</table>
		</s:if>
		<s:if test="%{loanGrantHeaderList.size==0}">
			<div id="msgdiv" style="display: block">
				<table align="center" class="tablebottom" width="80%">
					<tr>
						<th class="bluebgheadtd" colspan="7">No Records Found
						</td>
					</tr>
				</table>
			</div>
		</s:if>

	</s:form>
	<script>
   var mode='<s:property value="mode"/>';
	function urlLoad(id){
   var url="";
   var windowName="";
     if(mode=='edit')
      {
      		url = "loanGrant!beforeEdit.action?model.id="+id;
            windowName="LoangrantEdit";
      }
      else
      {
      url = "loanGrant!beforeView.action?model.id="+id;
      windowName="LoangrantView";
      }
	 window.open(url,windowName,'resizable=yes,scrollbars=yes,left=300,top=40, width=900, height=700');
	}
	function loadChanges(obj)
{
	//NOTE - In the including jsp, if bankbranch and bankaccount dropdowns are there
	// then give their ids as  bank_branch and bankaccount respectively.
	var bankObj= document.getElementById('bank_branch');
	var bankAccountObj= document.getElementById('bankaccount');
	if(bankObj!=null)
	{
		bankObj.options[0].selected=true;
		if(obj.options[obj.selectedIndex].value!=-1)
			populatebank_branch({fundId:obj.options[obj.selectedIndex].value});
	}
	if(bankAccountObj!=null)
		bankAccountObj.options[0].selected=true;
}
function autocompleteSchemeBy20LG()
{
	     oACDS = new YAHOO.widget.DS_XHR("/EGF/voucher/common!ajaxLoadSchemeBy20.action", [ "~^"]);
	   oACDS.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
	   oACDS.scriptQueryParam = "startsWith";
	  
	   var oAutoComp1 = new YAHOO.widget.AutoComplete('subScheme.scheme.name','codescontainer',oACDS);
	   oAutoComp1.doBeforeSendQuery = function(sQuery){
		   loadWaitingImage(); 
		   return sQuery+"&fundId="+document.getElementById("fundId").value;
	   } 
	   oAutoComp1.queryDelay = 0.5;
	   oAutoComp1.minQueryLength = 3;
	   oAutoComp1.prehighlightClassName = "yui-ac-prehighlight";
	   oAutoComp1.useShadow = true;
	   oAutoComp1.forceSelection = true;
	   oAutoComp1.maxResultsDisplayed = 20;
	   oAutoComp1.useIFrame = true;
	   oAutoComp1.doBeforeExpandContainer = function(oTextbox, oContainer, sQDetauery, aResults) {
		   clearWaitingImage();
	           var pos = YAHOO.util.Dom.getXY(oTextbox);
	           pos[1] += YAHOO.util.Dom.get(oTextbox).offsetHeight + 6;
	           oContainer.style.width=300;
	           YAHOO.util.Dom.setXY(oContainer,pos);
	           return true;
	   };


	
}
function autocompleteSubSchemeBy20LG()
{
	   oACDS = new YAHOO.widget.DS_XHR("/EGF/voucher/common!ajaxLoadSubSchemeBy20.action", [ "~^"]);
	   oACDS.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
	   oACDS.scriptQueryParam = "startsWith";
	   var oAutoComp1 = new YAHOO.widget.AutoComplete('subScheme.name','codescontainer',oACDS);
	   oAutoComp1.doBeforeSendQuery = function(sQuery){
		   loadWaitingImage(); 
		   return sQuery+"&schemeId="+document.getElementById("schemeId").value;
	   } 
	   oAutoComp1.queryDelay = 0.5;
	   oAutoComp1.minQueryLength = 3;
	   oAutoComp1.prehighlightClassName = "yui-ac-prehighlight";
	   oAutoComp1.useShadow = true;
	   oAutoComp1.forceSelection = true;
	   oAutoComp1.maxResultsDisplayed = 20;
	   oAutoComp1.useIFrame = true;
	   oAutoComp1.doBeforeExpandContainer = function(oTextbox, oContainer, sQDetauery, aResults) {
		   clearWaitingImage();
	           var pos = YAHOO.util.Dom.getXY(oTextbox);
	           pos[1] += YAHOO.util.Dom.get(oTextbox).offsetHeight + 6;
	           oContainer.style.width=300;
	           YAHOO.util.Dom.setXY(oContainer,pos);
	           return true;
	   };
}
function validate()
{
	schemeIdObj=document.getElementById('schemeId');
	if(schemeIdObj.value=='')
	{
		bootbox.alert("Please enter scheme");
		return false;
	}
	else
		return true;
}
	</script>
</body>
</html>
