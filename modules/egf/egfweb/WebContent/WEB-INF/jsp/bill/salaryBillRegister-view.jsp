<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<html>  
<head>  
    <title><s:text name="bill.salarybill.register"/></title>
    <link href="common/css/budget.css" rel="stylesheet" type="text/css" />
	<link href="common/css/commonegov.css" rel="stylesheet" type="text/css" />	
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/payment.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/voucherHelper.js"></script>
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
		var url = path+"/commons/Process.jsp?type=getAllFunctionName";
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
	<body onload="disableControls(0, true);onLoadTask();populateFunctionName();computeTotal();">  
		<s:form action="salaryBillRegister" theme="simple" name="salaryBill">  
			<span class="mandatory">
				<s:actionerror/>  
				<s:fielderror />
				<s:actionmessage />
			</span>
			<div class="formmainbox"><div class="subheadnew"><s:text name="bill.salarybill.register"/></div>
				<%@ include file='salaryBillRegister-form.jsp'%>
			</div>
			<div class="buttonbottom">
				<s:submit value="Close" onclick="javascript: self.close()" cssClass="button"/>
			</div>
		</s:form>  
	</body>  
</html>
