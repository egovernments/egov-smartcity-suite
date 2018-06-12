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

<%@ taglib prefix="s" uri="/WEB-INF/taglib/struts-tags.tld" %>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags" %>
<script>
function populateSchemes(fund){
	
	if(null != document.getElementById("schemeId")){
		populateschemeId({fundId:fund.options[fund.selectedIndex].value});
		populatesubschemeId({schemeId:-1});
	}
		
}
function populatesubSchemes(scheme){
	
	populatesubschemeId({schemeId:scheme.options[scheme.selectedIndex].value});	
	
}

function validate(){
	var valid=true;
	dom.get('error_area').innerHTML = '';
	dom.get("error_area").style.display="none"
	if(dom.get('serviceCode').value.trim() == ""){
		dom.get("error_area").innerHTML = '<s:text name="service.code.null" />' + '<br>';
		dom.get("error_area").style.display="block";
		valid=false;
	}
	if(dom.get('name').value.trim()== ""){
		dom.get("error_area").innerHTML = '<s:text name="service.name.null" />' + '<br>';
		dom.get("error_area").style.display="block";
		valid=false;
	}
	if(null!= document.getElementById('serviceType') && document.getElementById('serviceType').value == -1){
		dom.get("error_area").innerHTML = '<s:text name="service.servictype.null" />' + '<br>';
		dom.get("error_area").style.display="block";
		valid=false;
	}
	 <s:if test="%{isFieldMandatory('fund')}"> 
     if(null != document.getElementById('fundId') && document.getElementById('fundId').value == -1){
            document.getElementById("error_area").innerHTML+='<s:text name="miscreceipt.fundcode.errormessage" />'+  "<br>";
            dom.get("error_area").style.display="block";
            valid=false;
     }
     </s:if>
	     <s:if test="%{isFieldMandatory('department')}"> 
	     if(null!= document.getElementById('deptId') && document.getElementById('deptId').value == -1){
	            document.getElementById("error_area").innerHTML+='<s:text name="miscreceipt.deptcode.errormessage" />'+ '<br>';
	            dom.get("error_area").style.display="block";
	            valid=false;
	     }
		</s:if>
		<s:if test="%{isFieldMandatory('scheme')}"> 
		     if(null!=document.getElementById('schemeId') &&  document.getElementById('schemeId').value == -1){
		            document.getElementById("error_area").innerHTML+='<s:text name="miscreceipt.schemeId.errormessage" />'+ '<br>';
		            dom.get("error_area").style.display="block";
		            valid=false;
		     }
		</s:if>
		<s:if test="%{isFieldMandatory('subscheme')}"> 
		     if(null!= document.getElementById('subschemeId') && document.getElementById('subschemeId').value == -1){
		            document.getElementById("error_area").innerHTML+='<s:text name="miscreceipt.subschemeId.errormessage" />'+ '<br>';
		            dom.get("error_area").style.display="block";
		            valid=false;
		     }
		</s:if>
		<s:if test="%{isFieldMandatory('functionary')}"> 
		     if(null!=document.getElementById('receiptMisc.idFunctionary.id') &&  document.getElementById('receiptMisc.idFunctionary.id').value == -1){
		            document.getElementById("error_area").innerHTML+='<s:text name="miscreceipt.functionarycode.errormessage" />'+ '<br>';
		            dom.get("error_area").style.display="block";
		            valid=false;
		     }
		</s:if>
		<s:if test="%{isFieldMandatory('fundsource')}"> 
		     if(null !=document.getElementById('receiptMisc.fundsource.id') &&  document.getElementById('receiptMisc.fundsource.id').value == -1){
		            document.getElementById("error_area").innerHTML+='<s:text name="miscreceipt.fundsourcecode.errormessage" />'+ '<br>';
		            dom.get("error_area").style.display="block";
		            valid=false;
		    }
		</s:if>
		<s:if test="%{isFieldMandatory('function')}">                     
		 if(null!= document.getElementById('functionId') && document.getElementById('functionId').value == -1){
			 document.getElementById("error_area").innerHTML+='<s:text name="miscreceipt.functioncode.errormessage" />'+ '<br>';
			 dom.get("error_area").style.display="block";                                
			 valid=false;
		 }            
		</s:if>
		if(document.getElementById('serviceType').value =="P" ){
	        if(document.getElementById('serviceUrl').value =="" ){       
	        document.getElementById("error_area").innerHTML+='<s:text name="service.serviceurl.null" />'+  "<br>";
	        dom.get("error_area").style.display="block";
	        valid=false;
	    }
	        if(document.getElementById('callBackurl').value ==""){       
	            document.getElementById("error_area").innerHTML+='<s:text name="service.callbackurl.null" />'+  "<br>";
	            dom.get("error_area").style.display="block";
	            valid=false;
	        }
	 }
		 if (valid)
				dom.get('serviceType').disabled = false;
		window.scroll(0,0);
		return valid;
}
function uniqueCheckCode(){
	dom.get('error_area').innerHTML = '';
	dom.get("error_area").style.display="none"
	var serviceCode = dom.get("serviceCode").value.trim();
	 var serviceCodeInitVal ='<s:property value="%{code}" />';
    if(serviceCode !="" && serviceCodeInitVal.trim() != serviceCode){
		populateCodeUnique({code:dom.get('serviceCode').value});
   }
}

function clearCodeIfExists(){
	 if(dom.get("CodeUnique").style.display =="" ){
		 document.getElementById('serviceCode').value="";
	 }	
}

function enableUrl(obj) {
	var selectedStatus = document.getElementById("serviceType").options[obj.selectedIndex].text;
	if (selectedStatus == 'Payment') {
		document.getElementById("urlDetails").style.display = "";
	} else {
		document.getElementById("serviceUrl").value="";
		document.getElementById("callBackurl").value="";
		document.getElementById("urlDetails").style.display = "none";
	}
}
</script>

<div class="errorstyle" id="error_area" style="display:none;"></div>
	 <span align="center" style="display:none" id="CodeUnique">
 		<font  style='color: red ; font-weight:bold '> 
         	<s:text name="service.code.already.exists"/>
 		 </font>
	</span>
		<span class="mandatory1">
			<font  style='color: red ; font-weight:bold '> 
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage /></font>
			</span>

<div class="formmainbox">
	 
	<div class="subheadnew"><s:text name="service.master.create.header"></s:text> </div>

	<div class="subheadsmallnew">
				<span class="subheadnew"> <s:text name="service.create.details.header"></s:text> </span>
			</div>
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
		    <td width="3%" class="bluebox"> &nbsp; </td>
			<td width="20%" class="bluebox"> <s:text name="service.master.search.category"></s:text> </td>
			<td width="25%" class="bluebox"><s:property value="serviceCategory.name"/></td>
			<td width="25%" class="bluebox"> </td><td width="25%" class="bluebox"> </td>
		</tr>
		<s:set var="serviceCodeInitVal" value="%{code}"></s:set>
		<tr>
		    <td></td>
			<egov:uniquecheck id="CodeUnique" fields="['Value']" url='/service/serviceDetails-codeUniqueCheck.action'
			 key='service.code.already.exists' />
			<td class="bluebox"> <s:text name="service.create.code"></s:text><span class="mandatory1">*</span></td>
			<td class="bluebox"><s:textfield name="code" cssClass="form-control patternvalidation" data-pattern="alphanumericwithspace" id="serviceCode" maxlength="12"
			 onblur="uniqueCheckCode();clearCodeIfExists();"></s:textfield> </td>
			<td class="bluebox"> <s:text name="service.create.name"></s:text><span class="mandatory1">*</span></td>
			<td class="bluebox"> <s:textfield name="name" id="name" maxlength="100" ></s:textfield> </td>
		</tr>
		<tr>
            <td></td>
			<td class="bluebox"> <s:text name="service.master.enable"></s:text> </td>
			<td class="bluebox"><s:checkbox name="isEnabled" /></td>
			<td class="bluebox"><s:text name="service.master.classification"/> <span class="mandatory"></td>
			<td class="bluebox"> 
				<s:select list="serviceTypeMap" headerKey="-1" headerValue="%{getText('miscreceipt.select')}"
				name="serviceType" id="serviceType" onchange="return enableUrl(this)"></s:select>
			</td>
		</tr>
		<tr>
		    <td></td>
			<td class="bluebox"> <s:text name="service.master.isvouchertobecreated"></s:text> </td>
			<td class="bluebox" >
			<s:if test="%{isVoucherOnReceiptAndStatusDisplay}">
			<s:checkbox name="voucherCreation" id="voucherCreation" onclick="return EnableVoucherDetails(this)"/>
			</s:if> 
			<s:else>
			   <s:checkbox name="voucherCreation" id="voucherCreation" onclick ="return false;" />
			</s:else>
			</td>
		</tr>
		<tr id="voucherApprovedDetails">
		<td></td>
		<td class="bluebox"><s:text name="service.master.isvouchertobeapproved" ></s:text> </td>
			<td class="bluebox">
			<s:select list="#{'true':'Approved','false':'Pre-Approved'}" 
				name="isVoucherApproved" id="isVoucherApproved"></s:select>
			</td>
			<td class="bluebox"></td>
			<td class="bluebox"></td>
		</tr>
		<tr id="urlDetails">
			<td></td>
			<td class="bluebox"><s:text name="service.create.serviceurl"></s:text><span
				class="mandatory1">*</span></td>
			<td class="bluebox"><s:textarea name="serviceUrl"
					id="serviceUrl" value="%{serviceUrl}" cols="18" rows="1"
					maxlength="255" onkeyup="return ismaxlength(this)" /></td>
			<td class="bluebox"><s:text name="service.create.callbackurl"></s:text><span
				class="mandatory1">*</span></td>
			<td class="bluebox"><s:textarea name="callBackurl"
					id="callBackurl" value="%{callBackurl}" cols="18" rows="1"
					maxlength="255" onkeyup="return ismaxlength(this)" /></td>
		</tr>
		<tr >
			<td colspan="5"><div class="subheadsmallnew"><s:text name="service.create.findetails.header"></s:text></div></td>
		</tr>
		<s:if test="%{shouldShowHeaderField('fund') || shouldShowHeaderField('department')}">
         <tr>
          <td width="4%" class="bluebox">&nbsp;</td>
           <s:if test="%{shouldShowHeaderField('fund')}">
          <td width="21%" class="bluebox"><s:text name="miscreceipt.fund"/><s:if test="%{isFieldMandatory('fund')}"><span class="bluebox"><span class="mandatory"/></s:if></td>
          <td width="24%" class="bluebox"><s:select headerKey="-1" headerValue="%{getText('miscreceipt.select')}" name="fund" id="fundId" cssClass="selectwk" onChange="setFundId();getSchemelist(this);getBankBranchList(this);" list="dropdownData.fundList" listKey="id" listValue="name" value="%{fund.id}" />
          <egov:ajaxdropdown id="bankBranchMasterDropdown" fields="['Text','Value']" dropdownId='bankBranchMaster'
                url='receipts/ajaxBankRemittance-bankBranchList.action' selectedValue="%{bankbranch.id}"/> 
          <egov:ajaxdropdown id="schemeIdDropdown" fields="['Text','Value']" dropdownId='schemeId' url='receipts/ajaxReceiptCreate-ajaxLoadSchemes.action' />
          </td>
          </s:if>
           <s:else>
            <td colspan=2 class="bluebox"></td>
            </s:else>
              <s:if test="%{shouldShowHeaderField('department')}">
           <td width="21%" class="bluebox"><s:text name="miscreceipt.department"/><s:if test="%{isFieldMandatory('department')}"><span class="bluebox"><span class="mandatory"/></s:if></td>
          <td width="24%" class="bluebox"><s:select headerKey="-1" headerValue="%{getText('miscreceipt.select')}" name="deptId" id="deptId" cssClass="selectwk" list="dropdownData.departmentList" listKey="id" listValue="name" value="%{deptId}" /> </td>
            </s:if>
           <s:else>
            <td colspan=2 class="bluebox"></td>
            </s:else>
         </tr>
         </s:if>
          <s:if test="%{shouldShowHeaderField('function')}">
         <tr>
         <s:if test="%{shouldShowHeaderField('function')}">
         <td width="4%" class="bluebox">&nbsp;</td>
           <td width="21%" class="bluebox"><s:text name="miscreceipt.function"/><s:if test="%{isFieldMandatory('function')}"><span class="bluebox"><span class="mandatory"/></s:if></td>
          <td width="24%" class="bluebox"><s:select headerKey="-1" headerValue="%{getText('miscreceipt.select')}" name="function" id="functionId" cssClass="selectwk" list="dropdownData.functionList" listKey="id" listValue="name"  value="%{function.id}"/> </td>
            </s:if>
           <s:else>
            <td colspan=2 class="bluebox"></td>
            </s:else>
         </tr>
         </s:if>
          <s:if test="%{shouldShowHeaderField('fundsource') || shouldShowHeaderField('functionary')}">
        <tr>
          <td width="4%" class="bluebox">&nbsp;</td>
          <s:if test="%{shouldShowHeaderField('fundsource')}">
           <td width="21%" class="bluebox"><s:text name="miscreceipt.fundingsource"/> <s:if test="%{isFieldMandatory('fundsource')}"><span class="bluebox"><span class="mandatory"/></s:if></td>
         <td width="30%" class="bluebox"><s:select headerKey="-1" headerValue="%{getText('miscreceipt.select')}" name="fundSourceId" id="fundSourceId" onclick="checkfund()" onchange="setFundSourceId();" cssClass="selectwk" list="dropdownData.fundsourceList" listKey="id" listValue="name"  /></td>
         <s:hidden label="receiptMisc.fundsource.id" id="receiptMisc.fundsource.id"  name="receiptMisc.fundsource.id"/>
         </s:if>
         <s:else>
        <td colspan=2 class="bluebox"></td>
        </s:else>
        <s:if test="%{shouldShowHeaderField('functionary')}">
          <td width="21%" class="bluebox"><s:text name="miscreceipt.functionary"/>  <s:if test="%{isFieldMandatory('functionary')}"><span class="bluebox"><span class="mandatory"/></s:if> </td>
         <td width="30%" class="bluebox"><s:select headerKey="-1" headerValue="%{getText('miscreceipt.select')}" name="receiptMisc.idFunctionary.id" id="receiptMisc.idFunctionary.id" cssClass="selectwk" list="dropdownData.functionaryList" listKey="id" listValue="name"  /></td>
         
         </s:if>
         <s:else>
        <td colspan=2 class="bluebox"></td>
        </s:else>
        </tr>
        </s:if>
          <s:if test="%{shouldShowHeaderField('scheme')}">
          <tr>
          <td width="4%" class="bluebox">&nbsp;</td>
          <td width="21%" class="bluebox"><s:text name="miscreceipt.scheme"/> <s:if test="%{isFieldMandatory('scheme')}"><span class="mandatory"/></s:if>  </td>
          <td width="24%" class="bluebox">
          <s:select headerKey="-1" headerValue="%{getText('miscreceipt.select')}" name="schemeId" id="schemeId" onclick="checkfund()" onchange="setSchemeId();getSubSchemelist(this)" cssClass="selectwk" list="dropdownData.schemeList" listKey="id" listValue="name"  value="%{scheme.id}" /> 
          <egov:ajaxdropdown id="subschemeId" fields="['Text','Value']" dropdownId='subschemeId' url='receipts/ajaxReceiptCreate-ajaxLoadSubSchemes.action' />
          <s:hidden label="receiptMisc.scheme.id" id="receiptMisc.scheme.id"  name="receiptMisc.scheme.id"/>
          </td>
          
          <td width="21%" class="bluebox"><s:text name="miscreceipt.subscheme"/> <s:if test="%{isFieldMandatory('subscheme')}"><span class="mandatory"/></s:if>  </td>

         <td width="30%" class="bluebox">
          <s:select headerKey="-1" headerValue="%{getText('miscreceipt.select')}" name="subschemeId" id="subschemeId" onchange="setSubSchemeId();getFundSourcelist(this)" onclick="checkscheme()" cssClass="selectwk" list="dropdownData.subschemeList" listKey="id" listValue="name"  /></td>
          <egov:ajaxdropdown id="fundSourceId" fields="['Text','Value']" dropdownId='fundSourceId'
           url='../../EGF/voucher/common-ajaxLoadFundSource.action'  />
           <s:hidden label="receiptMisc.subscheme.id" id="receiptMisc.subscheme.id"  name="receiptMisc.subscheme.id"/>
         
        </tr>
        </s:if>
		<s:hidden name="serviceCategory.id" id="serviceCategory.id"></s:hidden>
		<s:hidden name="id"></s:hidden>
		<s:hidden name="serviceCategory.name" id="serviceCategory.name"></s:hidden>
		<s:hidden name="isVoucherOnReceiptAndStatusDisplay" id="isVoucherOnReceiptAndStatusDisplay"></s:hidden>
	</table>
	<jsp:include page="finAccountsTable.jsp"/>

<div class="subheadsmallnew"><span class="subheadnew">Account Details</span></div>
    
    <div class="yui-skin-sam" align="center">
       <div id="accountsDetailTable"></div>
       
     </div>
     <script>
        
        makeAccountsDetailTable();
        document.getElementById('accountsDetailTable').getElementsByTagName('table')[0].width="100%";
     </script>
     <div id="codescontainer"></div>
     <br/>
    
      <div class="subheadsmallnew"><span class="subheadnew">Subledger Details</span></div>
    
        
        <div class="yui-skin-sam" align="center">
           <div id="subLedgerTable"></div>
         </div>
        <script>
            
            makeSubLedgerTable();
            
            document.getElementById('subLedgerTable').getElementsByTagName('table')[0].width="100%";
        </script>
<div id="subledgercodescontainer"></div> 

<br/>
<div align="left" class="mandatorycoll"> &nbsp;&nbsp;&nbsp;<s:text name="common.mandatoryfields"/></div>
<br/>
</div>

