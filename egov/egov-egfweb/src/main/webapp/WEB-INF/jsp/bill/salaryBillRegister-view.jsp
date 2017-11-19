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
<title><s:text name="bill.salarybill.register" /></title>
<link href="/EGF/css/commonegov.css?rnd=${app_release_no}" rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/voucherHelper.js?rnd=${app_release_no}"></script>
<script type="text/javascript">
	var path="${pageContext.request.contextPath}";
	function disableControls(frmIndex, isDisable){
		for(var i=0;i<document.forms[frmIndex].length;i++){
			if(document.forms[frmIndex].elements[i].value != "Close" && document.forms[frmIndex].elements[i].value != "Print"){
				document.forms[frmIndex].elements[i].disabled =isDisable;
			}
		}
	}
	
	function populateFunctionName(){
		var funcArray;
		var map = {};
		var url = "/EGF/voucher/common-ajaxGetAllFunctionName.action";
		var req2 = initiateRequest();
		req2.onreadystatechange = function(){
		  if (req2.readyState == 4){
			  if (req2.status == 200){
				var codes2=req2.responseText;
				var a = codes2.split("^");
				var codes = a[0];
				funcArray=codes.split("+");
				for(var i=0;i<funcArray.length;i++){
					value = funcArray[i].split("~")[0].replace("`","");
					key = funcArray[i].split("~")[1].replace("`","");
					map[key] = value;
				}
				for(var i=0;i< <s:property value="earningsList.size"/>;i++){
					if(document.getElementById('earningsList['+i+'].functionid').value != "" && document.getElementById('earningsList['+i+'].functionid').value != 'undefined' && document.getElementById('earningsList['+i+'].functionid').value!=null){
						document.getElementById('earningsList['+i+'].functionDetail').value=map[document.getElementById('earningsList['+i+'].functionid').value];
					}
				}
				for(var i=0;i< <s:property value="deductionsList.size"/>;i++){
					if(document.getElementById('deductionsList['+i+'].functionid').value != "" && document.getElementById('deductionsList['+i+'].functionid').value != 'undefined' && document.getElementById('deductionsList['+i+'].functionid').value!=null){
						document.getElementById('deductionsList['+i+'].functionDetail').value=map[document.getElementById('deductionsList['+i+'].functionid').value];
					}
				}
			  }
		   }
		};
		req2.open("GET", url, true);
		req2.send(null);
	}
	
	function computeTotal(){
		var total = 0;
		var earningsTotal = 0;
		var deductionsTotal = 0;
		for(var i=0;i< <s:property value="earningsList.size"/>;i++){
			if(document.getElementById('earningsList['+i+'].debitamount').value != "" && document.getElementById('earningsList['+i+'].debitamount').value != 'undefined' && document.getElementById('earningsList['+i+'].debitamount').value!=null){
				earningsTotal = earningsTotal + parseInt(document.getElementById('earningsList['+i+'].debitamount').value);
			}
		}
		document.getElementById('totaldramount').value = earningsTotal;
		for(var i=0;i< <s:property value="deductionsList.size"/>;i++){
			if(document.getElementById('deductionsList['+i+'].creditamount').value != "" && document.getElementById('deductionsList['+i+'].creditamount').value != 'undefined' && document.getElementById('deductionsList['+i+'].creditamount').value!=null){
				deductionsTotal = deductionsTotal + parseInt(document.getElementById('deductionsList['+i+'].creditamount').value);
			}
		}
		document.getElementById('totalcramount').value = deductionsTotal;
		document.getElementById('netPayList[0].creditamount').value = earningsTotal - deductionsTotal;
		var subledgerCreditTotal = 0;
		var subledgerDebitTotal = 0;
		for(var i=0;i< <s:property value="subledgerList.size"/>;i++){
			if(document.getElementById('subledgerList['+i+'].creditAmount').value != "" && document.getElementById('subledgerList['+i+'].creditAmount').value != 'undefined' && document.getElementById('subledgerList['+i+'].creditAmount').value!=null){
				subledgerCreditTotal = subledgerCreditTotal + parseInt(document.getElementById('subledgerList['+i+'].creditAmount').value);
			}
			if(document.getElementById('subledgerList['+i+'].debitAmount').value != "" && document.getElementById('subledgerList['+i+'].debitAmount').value != 'undefined' && document.getElementById('subledgerList['+i+'].debitAmount').value!=null){
				subledgerDebitTotal = subledgerDebitTotal + parseInt(document.getElementById('subledgerList['+i+'].debitAmount').value);
			}
		}
		document.getElementById('totalSubLedgerCreditamount').value = subledgerCreditTotal;
		document.getElementById('totalSubLedgerDebitamount').value = subledgerDebitTotal;
	}
	
	function onLoadTask(){
		document.getElementById('month').selectedIndex = <s:property value="billregistermis.month"/>;
		<s:iterator value="subledgerList" status="stat">
			//document.getElementById('subledgerList[<s:property value="#stat.index"/>].glcode.id').selectedIndex = <s:property value="egBilldetailsId.glcodeid"/>;
			//document.getElementById('subledgerList[<s:property value="#stat.index"/>].detailType.id').selectedIndex = ;
			//document.getElementById('subledgerList[<s:property value="#stat.index"/>].detailCode').selectedIndex = ;
			//document.getElementById('subledgerList[<s:property value="#stat.index"/>].detailKey').selectedIndex = <s:property value="debitAmount"/>;
			document.getElementById('subledgerList[<s:property value="#stat.index"/>].debitAmount').value = <s:property value="debitAmount"/>;
			document.getElementById('subledgerList[<s:property value="#stat.index"/>].creditAmount').value = <s:property value="creditAmount"/>;
		</s:iterator>
	}
	</script>
</head>
<body
	onload="disableControls(0, true);onLoadTask();populateFunctionName();computeTotal();">
	<s:form action="salaryBillRegister" theme="simple" name="salaryBill">
		<span class="mandatory"> <s:actionerror /> <s:fielderror /> <s:actionmessage />
		</span>
		<div class="formmainbox">
			<div class="subheadnew">
				<s:text name="bill.salarybill.register" />
			</div>
			<%@ include file='salaryBillRegister-form.jsp'%>
		</div>
		<div class="buttonbottom">
			<s:submit value="Close" onclick="javascript: self.close()"
				cssClass="button" />
		</div>
	</s:form>
</body>
</html>
