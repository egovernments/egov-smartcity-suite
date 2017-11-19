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
<tr>
	<td class="bluebox"><s:text name="masters.subscheme.search.fund" />
		<s:if test="%{defaultFundId==-1}">
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
	<td class="greybox"><s:text name="masters.subscheme.search.scheme" /><span
		class="mandatory">*</span></td>
	<s:hidden name="schemeId" id="schemeId" />
	<td class="greybox"><s:textfield value="%{subScheme.scheme.name}"
			name="subScheme.scheme.name" id="subScheme.scheme.name"
			autocomplete='off' onFocus="autocompleteSchemeBy20LG();"
			onBlur="splitSchemeCode(this)" /></td>
	<td class="greybox"><s:text name="masters.subscheme.search" /><span
		class="mandatory">*</span></td>
	<s:hidden name="subSchemeId" id="subSchemeId" />
	<td class="greybox"><s:textfield value="%{subScheme.name}"
			name="subScheme.name" id="subScheme.name" autocomplete='off'
			onFocus="autocompleteSubSchemeBy20LG();"
			onBlur="splitSubSchemeCode(this);checkuniquenesscode();" /> <egov:uniquecheck
			id="codeuniquecode" name="codeuniquecode" fieldtoreset="subSchemeId"
			fields="['Value']" url='masters/loanGrant!codeUniqueCheckCode.action' />
	</td>
</tr>
<script>
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
	    // bootbox.alert("helllpo");
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
</script>
