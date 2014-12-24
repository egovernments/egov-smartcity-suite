<%@ include file="/includes/taglibs.jsp" %>

<%@ page
	import="org.egov.payroll.dao.*,java.util.*,java.lang.*,org.egov.infstr.*,org.egov.infstr.commons.dao.*,
	org.egov.infstr.commons.*,org.egov.payroll.model.*,
	org.egov.infstr.client.filter.EGOVThreadLocals,
	 java.text.SimpleDateFormat,
	org.egov.infstr.utils.EGovConfig "%>

<html>
<head>
<title>Payhead</title>

<style type="text/css">
		#accountsContainer {position:absolute;left:11em;width:9%}
		#accountsContainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
		#accountsContainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
		#accountsContainer ul {padding:5px 0;width:80%;}
		#accountsContainer li {padding:0 5px;cursor:default;white-space:nowrap;}
		#accountsContainer li.yui-ac-highlight {background:#ff0;}
		#accountsContainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>
<style type="text/css">
		#intGlAccountsContainer {position:absolute;left:11em;width:9%}
		#intGlAccountsContainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
		#intGlAccountsContainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
		#intGlAccountsContainer ul {padding:5px 0;width:80%;}
		#intGlAccountsContainer li {padding:0 5px;cursor:default;white-space:nowrap;}
		#intGlAccountsContainer li.yui-ac-highlight {background:#ff0;}
		#intGlAccountsContainer li.yui-ac-prehighlight {background:#FFFFCC;}
</style>
	<%
		List salaryCodes = new ArrayList();
		SalaryCodesDAO salaryCodesDAO = PayrollDAOFactory.getDAOFactory().getSalaryCodesDAO();
		salaryCodes =(List)salaryCodesDAO.findAll();

		List categoryE = new ArrayList();
		SalaryCategoryMasterDAO salaryCategoryMasterDAO = PayrollDAOFactory.getDAOFactory().getSalaryCategoryMasterDAO();
		categoryE = (List)salaryCategoryMasterDAO.getCategorymasterByType("E");
		Object a[] = categoryE.toArray(); 

		List categoryD = new ArrayList();
		categoryD = (List)salaryCategoryMasterDAO.getCategorymasterByType("D");		

		Long order =  salaryCodesDAO.getMaxOrderSalarycode();
		String orderId = "1";
		if(order != null){
		long o = order.longValue();
		o +=1;
		orderId = String.valueOf(o);
		}
	   // TdsHibernateDAO tdsDAO = CommonsDaoFactory.getDAOFactory().getTdsDAO();
		//List tdses = tdsDAO.getAllTdsByPartyType("Employee");
		System.out.println("domain--------"+EGOVThreadLocals.getDomainName());
		%>


<SCRIPT type="text/javascript" src="<%=request.getContextPath()%>/common/js/eispayroll.js" ></SCRIPT>
<script language="JavaScript" type="text/JavaScript">

	var glcodeArray = new Array();
	var glcodeObj;
	var yuiflag1 = new Array();
	var interestGlcodeArray = new Array();
	var interestGlcodeObj;
	var yuiflag2 = new Array();
	
	

	function checkAlphaNumeric(obj){
   		if(obj.value!=""){
   			var num=obj.value;
   			var objRegExp  = /^([a-zA-Z0-9.()\ ]+)$/i;
    		if(!objRegExp.test(num)){
	   			alert('Please enter valid name');
    			obj.value="";
    			obj.focus();
    		}
    	}
   }
   
   function checkdecimalval(obj,amount){		
	    var objt = obj;
	    var amt = amount;
	    if(amt != null && amt != "")
	    {
	      if(amt < 0 )
	        {
	            alert("Please enter positive value for the amount");
	            obj.value="";
	            objt.focus();
	            return false;
	
	        }
	        if(isNaN(amt))
	        {
	            alert("Please enter a numeric value for the amount");
	            obj.value="";
	            objt.focus();
	            return false;	
	        }	           
	    }
	}

	function checkOnSubmit()
	{		
		if(document.payheadForm.isInterest.checked == true && document.payheadForm.interestGlcode.value == "" ){
			alert("Enter interest account");
			document.payheadForm.interestGlcode.focus();
			return false;
		}
		if(document.payheadForm.calType.value=="SlabBased" && document.payheadForm.slabAC.value==""){
			alert('<bean:message key="alertSelectslab"/>');
			document.payheadForm.slabAC.focus();
			return false;
		}
		if(!validatePayheadForm(document.payheadForm))
			return false;
		if(document.payheadForm.type.value=="D" && document.payheadForm.categoryD.value != "Deduction-Advance"
												&& document.payheadForm.recoveryAC.value == ""
												&& document.payheadForm.categoryD.value != "Deduction-Other"){
			alert('<bean:message key="alertRecoveryAccount"/>');
			document.payheadForm.recoveryAC.focus();
			return false;
		}		
		if(document.payheadForm.localLangDesc.value == ""){
			alert('<bean:message key="alertLocalDescription"/>');
			document.payheadForm.localLangDesc.focus();			
			return false;
		}
		if(document.payheadForm.chkGlcode.value == "undefined"){
			alert('<bean:message key="alertCorrectGlcode"/>');
			document.payheadForm.glcode.focus();			
			return false;
		}
		if(document.payheadForm.name.value==""){
			alert('<bean:message key="alertPayheadName"/>');
			document.payheadForm.name.focus();
			return false;
		}
		if(uniqueCheckPayhead(document.payheadForm.name.value)=="false"){
			alert('<bean:message key="alertPayheadnameExists"/>');
			document.payheadForm.name.focus();
			return false;
		}
	/*	if(document.payheadForm.description.value==""){
			alert('<bean:message key="alertDecsription"/>');
			document.payheadForm.description.focus();
			return false;
		}	*/
		if(document.payheadForm.calType.value==""){
			alert('<bean:message key="alertCalculationType"/>');
			document.payheadForm.calType.focus();
			return false;
		}
		if(document.payheadForm.calType.value=="ComputedValue" && document.payheadForm.pctBasis.value==""){
			alert('<bean:message key="alertPayheadFor%Basis"/>');
			document.payheadForm.calType.focus();
			return false;
		}
		if(document.payheadForm.glcode.value==""){
			alert('<bean:message key="alertGlcode"/>');
			document.payheadForm.glcode.focus();
			return false;
		}
		if(document.payheadForm.type.value==""){
			alert('<bean:message key="alertType"/>');
			document.payheadForm.type.focus();
			return false;
		}
		if(document.payheadForm.type.value=="D" && document.payheadForm.categoryD.value==""){
			alert('<bean:message key="alertCategory"/>');
			document.payheadForm.categoryD.focus();
			return false;
		}
		if(document.payheadForm.type.value=="E" && document.payheadForm.categoryE.value==""){
			alert('<bean:message key="alertCategory"/>');
			document.payheadForm.categoryE.focus();
			return false;
		}
		document.getElementById("isRecomputed").disabled=false;
		document.getElementById("isAttendanceBased").disabled=false;
		

	}

	function callCategory(){
		if(document.payheadForm.type.value=="E"){
			document.getElementById("categoryRowId").style.display="block";			
			document.getElementById("earningCategory").style.display="block";
			document.getElementById("deductionCategory").style.display="none";
			document.payheadForm.glcode.disabled=false;
			document.payheadForm.accountName.disabled=false;
			//document.getElementById("recovery").style.display="none";
			document.getElementById("recoveryRowLbl").style.display="none";
			document.getElementById("recoveryRowId").style.display="none";
			document.getElementById("taxableId").style.display="block";
			document.getElementById("taxableLbl").style.display="block";
			document.getElementById("interestcheckId").style.display="none";
			document.getElementById("interestcheckLbl").style.display="none";
			document.getElementById("interestActLbl").style.display = "none";
			document.getElementById("interestActId").style.display = "none";
			document.getElementById("interestActNameLbl").style.display = "none";
			document.getElementById("interestActNameId").style.display = "none";
		}
		if(document.payheadForm.type.value=="D"){
			document.getElementById("categoryRowId").style.display="block";	
			document.getElementById("deductionCategory").style.display="block";
			document.getElementById("earningCategory").style.display="none";		
			document.getElementById("taxableId").style.display="none";
			document.getElementById("taxableLbl").style.display="none";
			document.getElementById("slabRowLbl").style.display="none";
			document.getElementById("slabRowId").style.display="none";
			document.getElementById("interestcheckId").style.display="block";
			document.getElementById("interestcheckLbl").style.display="block";
			callInterestAct(document.payheadForm.isInterest);
		}
		if(document.payheadForm.type.value==""){	
			document.getElementById("categoryRowId").style.display="none";		
			document.getElementById("deductionCategory").style.display="none";
			document.getElementById("earningCategory").style.display="none";
			document.payheadForm.glcode.disabled=false;
			document.payheadForm.accountName.disabled=false;
			//document.getElementById("recovery").style.display="none";
			document.getElementById("recoveryRowLbl").style.display="none";
			document.getElementById("recoveryRowId").style.display="none";
			document.getElementById("taxableId").style.display="none";
			document.getElementById("taxableLbl").style.display="none";
			document.getElementById("slabRowLbl").style.display="none";
			document.getElementById("slabRowId").style.display="none";
			document.getElementById("interestcheckLbl").style.display="none";
			document.getElementById("interestcheckId").style.display="none";
			document.getElementById("interestActLbl").style.display = "none";
			document.getElementById("interestActId").style.display = "none";
			document.getElementById("interestActNameLbl").style.display = "none";
			document.getElementById("interestActNameId").style.display = "none";
		}		
	}

	function callCalType(obj){
		if(document.payheadForm.type.value=="E"){
			//document.getElementById("salRowId").style.display="block";
			for(var resLen=1;resLen<document.payheadForm.calType.length;resLen+1){
				document.payheadForm.calType.options[resLen]=null;
			}
			var count = 1;
			document.payheadForm.calType.options[count] = new Option("Monthly Flat Rate ","MonthlyFlatRate");
			count=count+1;
			document.payheadForm.calType.options[count] = new Option("Computed Value ","ComputedValue");
			count=count+1;
			document.payheadForm.calType.options[count] = new Option("Slab Based ","SlabBased");
			count=count+1;
			document.payheadForm.calType.options[count] = new Option("Rule Based ","RuleBased");
		}
		if(document.payheadForm.type.value=="D"){
			for(var resLen=1;resLen<document.payheadForm.calType.length;resLen+1){
				document.payheadForm.calType.options[resLen]=null;
			}
			var count = 1;
			document.payheadForm.calType.options[count] = new Option("Monthly Flat Rate ","MonthlyFlatRate");
			count=count+1;
			document.payheadForm.calType.options[count] = new Option("Slab Based ","SlabBased");
			count=count+1;
			document.payheadForm.calType.options[count] = new Option("Rule Based ","RuleBased");
		}
		if(document.payheadForm.type.value==""){
			for(var resLen=1;resLen<document.payheadForm.calType.length;resLen+1){
				document.payheadForm.calType.options[resLen]=null;
			}
		}	

	}

	function callSlab(obj){
		if(document.payheadForm.calType.value == "SlabBased"){
			document.payheadForm.glcode.disabled=true;
			document.payheadForm.accountName.disabled=true;
			document.getElementById("slabRowLbl").style.display="block";
			document.getElementById("slabRowId").style.display="block";
			for(var resLen=1;resLen<document.payheadForm.slabAC.length;resLen+1){
				document.payheadForm.slabAC.options[resLen]=null;
			}
			var count = 1;
			<c:forEach var="tdsObj" items="${tdses}">						
				 <c:if test = "${tdsObj.isEarning == '1'}">							
	 				document.payheadForm.slabAC.options[count] = new 
					Option("${tdsObj.type}","${tdsObj.id}");	
					count=count+1;
				</c:if>			
			</c:forEach>
			
		}
		else{
			document.getElementById("slabRowLbl").style.display="none";
			document.getElementById("slabRowId").style.display="none";
			document.payheadForm.glcode.disabled=false;
			document.payheadForm.accountName.disabled=false;
		}	
	}
	
	function callAfterSlab(obj){
		if(document.payheadForm.slabAC.value == ""){
			document.payheadForm.glcode.value = "";
			document.payheadForm.accountName.value = "";
		}
		<c:forEach var="tdsObj" items="${tdses}">
			if(obj.value == "${tdsObj.id}"){
				document.payheadForm.glcode.value = "${tdsObj.chartofaccounts.glcode}";
				document.payheadForm.accountName.value = "${tdsObj.chartofaccounts.name}";
			}	
		</c:forEach>
	}	

	function callRecoveryAC(obj){
		//alert(document.payheadForm.categoryD.value);
		document.payheadForm.glcode.disabled=false;
		if(document.payheadForm.categoryD.value != "Deduction-Advance" && document.payheadForm.categoryD.value != "Deduction-BankLoan" && document.payheadForm.categoryD.value != ""){
			
			for(var resLen=1;resLen<document.payheadForm.recoveryAC.length;resLen+1){
				document.payheadForm.recoveryAC.options[resLen]=null;
			}
			var count = 1;
			<c:forEach var="tdsObj" items="${tdses}">					
				 <c:if test = "${tdsObj.bank == null}">	
					<c:if test = "${tdsObj.isEarning == null || (tdsObj.isEarning != null && tdsObj.isEarning == '0')}">						document.payheadForm.recoveryAC.options[count] = new 
						Option("${tdsObj.type}","${tdsObj.id}");	
						count=count+1;
					</c:if>
				</c:if>			
			</c:forEach>				
			document.getElementById("recoveryRowLbl").style.display="block";
			document.getElementById("recoveryRowId").style.display="block";
			document.payheadForm.glcode.disabled=true;
			document.payheadForm.accountName.disabled=true;
		}
		else if(document.payheadForm.categoryD.value == "Deduction-BankLoan"){			
			for(var resLen=1;resLen<document.payheadForm.recoveryAC.length;resLen+1){
				document.payheadForm.recoveryAC.options[resLen]=null;
			}
			var count = 1;
			<c:forEach var="tdsObj" items="${tdses}">					
				 <c:if test = "${tdsObj.bank != null}">							
	 				document.payheadForm.recoveryAC.options[count] = new 
					Option("${tdsObj.type}","${tdsObj.id}");	
					count=count+1;
				</c:if>			
			</c:forEach>				
			document.getElementById("recoveryRowLbl").style.display="block";
			document.getElementById("recoveryRowId").style.display="block";
			document.payheadForm.glcode.disabled=true;
			document.payheadForm.accountName.disabled=true;	
		}
		else{
			document.getElementById("recoveryRowLbl").style.display="none";	
			document.getElementById("recoveryRowId").style.display="none";	
		}
		if(document.payheadForm.categoryD.value == "Deduction-Other" ){
			document.payheadForm.glcode.disabled=false;
		}
	}
	
	function callAfterRecovery(obj){
		if(document.payheadForm.recoveryAC.value != ""){
			document.payheadForm.glcode.disabled=true;
			document.payheadForm.accountName.disabled=true;
		}
		else{
			document.payheadForm.glcode.value="";
			document.payheadForm.accountName.value="";
			document.payheadForm.glcode.disabled=false;
			document.payheadForm.accountName.disabled=false;			
		}	
	//	alert(document.payheadForm.glcode.value);		
		<c:forEach var="tdsObj" items="${tdses}">
			if(obj.value == "${tdsObj.id}"){
			//	alert("${tdsObj.chartofaccounts.glcode}");
				document.payheadForm.glcode.value = "${tdsObj.chartofaccounts.glcode}";
				document.payheadForm.accountName.value = "${tdsObj.chartofaccounts.name}";
			}	
		</c:forEach>
	}
	
	function callPctBasis(){		
		if(document.payheadForm.calType.value=="MonthlyFlatRate" || document.payheadForm.calType.value=="SlabBased" || document.payheadForm.calType.value=="" ){
			document.getElementById("pctBasisRowId").style.display="none";
			document.getElementById("pctBasisRowLbl").style.display="none";
		}
		else if(document.payheadForm.calType.value=="ComputedValue"){
			document.getElementById("pctBasisRowId").style.display="block";
			document.getElementById("pctBasisRowLbl").style.display="block";
		}
	}

	function initiateRequest() {
		if (window.XMLHttpRequest) {
			return new XMLHttpRequest();
		} else if (window.ActiveXObject) {
			isIE = true;
			return new ActiveXObject("Microsoft.XMLHTTP");
		}
   }

	function uniqueCheckPayhead(name){
		var action = "getPayheadByName";
		var url = "<%=request.getContextPath()%>"+"/commons/uniquePayhead.jsp?action=" +action+ "&name="+name ;
		var isUnique;
		var req = initiateRequest();
      	req.onreadystatechange = function(){
	      if (req.readyState == 4){
	            if (req.status == 200){
                   	var payheads = req.responseText
                   	var a = payheads.split("^");
                   	var codes = a[0];
                   	if(codes=="true"){
                   		isUnique = "true";
                   	}
                   	else if(codes=="false"){
         				isUnique = "false";
                   	}
	       		}
	       	}
        };
		req.open("GET", url, false);
		req.send(null);		
		return isUnique;
	}

    function loadAllGlcodes(action){
        var url = "<%=request.getContextPath()%>"+"/commons/process.jsp?type=" +action+ " ";
      	var req = initiateRequest();
      	req.onreadystatechange = function(){
	      if (req.readyState == 4){
	            if (req.status == 200){
                   	var glcodes=req.responseText
                   	var a = glcodes.split("^");
                   	var codes = a[0];
					glcodeArray=codes.split("+");
					glcodeObj = new YAHOO.widget.DS_JSArray(glcodeArray);
	           }
	       }
        };
	   req.open("GET", url, true);
	   req.send(null);
  	}

  	function autocompleteGlcode(obj,myEvent){
		var src = obj;
	 	var target = document.getElementById('accountsContainer');
	 	var posSrc=findPos(src);
	 	target.style.left=posSrc[0]+6;
	 	target.style.top=posSrc[1];
		target.style.width=300;
	 	var currRow=getRow(obj);
	 	var coaCodeObj = obj;
		if(yuiflag1[0] == undefined){
			var Key = window.event ? window.event.keyCode : myEvent.keyCode;  	
			if(Key != 40 ){
				if(Key != 38 ){
					var oAutoComp1 = new YAHOO.widget.AutoComplete(coaCodeObj,'accountsContainer',glcodeObj);
					oAutoComp1.queryDelay = 0;
	  				oAutoComp1.prehighlightClassName = "yui-ac-prehighlight";
	  				oAutoComp1.useShadow = true;
	  				oAutoComp1.forceSelection = true; 
	  				oAutoComp1.maxResultsDisplayed = 15;
	    			oAutoComp1.useIFrame = true;
				}
			}
		yuiflag1[0] = 1;
		}
	}

  	function setGlcodeAfterSplit(obj){
  		var temp = obj.value;
  		var temp1 = temp.split("`-`");
  		document.payheadForm.glcode.value = temp1[0];
  		document.payheadForm.chkGlcode.value = temp1[1];  		
  		if(temp1[1]!=null)
	  		document.getElementById("accountName").value = temp1[1];
		else
			getGlcodeByEnteringCode(temp1[0]);		
  	}

	function getGlcodeByEnteringCode(code){
		var url = "<%=request.getContextPath()%>"+"/commons/uniquePayhead.jsp?action=getAllGlcodesByCode&code="+code;
      	var req = initiateRequest();
      	req.onreadystatechange = function(){
	      if (req.readyState == 4){
	            if (req.status == 200){
                   	var glcodes=req.responseText
                   	var a = glcodes.split("^");
                   	var codes = a[0];					
					var glcode = codes.split("`-`");
					if(codes == "false"){
						//alert("Enter correct glcode");
						//document.payheadForm.glcode.focus();
					}
					else{
						document.payheadForm.glcode.value = glcode[0];
						document.payheadForm.chkGlcode.value = glcode[1];  	
				  		document.getElementById("accountName").value = glcode[1];
					}
	           }
	       }
        };
	   req.open("GET", url, true);
	   req.send(null);
  }
  

  	function setGlcodeFlagUndefined(){
 		yuiflag1[0] = undefined;
	}

	function loadAllIncomeGlcodes(action){
	
        var url = "<%=request.getContextPath()%>"+"/commons/process.jsp?type=" +action+ " ";
      	var req = initiateRequest();
      	req.onreadystatechange = function(){
	      if (req.readyState == 4){
	            if (req.status == 200){
                   	var glcodes=req.responseText;
                   	var a = glcodes.split("^");
                   	var codes = a[0];
					interestGlcodeArray=codes.split("+");
					interestGlcodeObj = new YAHOO.widget.DS_JSArray(interestGlcodeArray);
	           }
	       }
        };
	   req.open("GET", url, true);
	   req.send(null);
  	}

  	function autocompleteInterestGlcode(obj,myEvent){
		var src = obj;
	 	var target = document.getElementById('intGlAccountsContainer');
	 	var posSrc=findPos(src);	
	 	target.style.left=posSrc[0]+6;
	 	target.style.top=posSrc[1];
		target.style.width=300;
	 	var currRow=getRow(obj);
	 	var coaCodeObj = obj;
		if(yuiflag2[0] == undefined){
			var Key = window.event ? window.event.keyCode : myEvent.keyCode;  	
			if(Key != 40 ){
				if(Key != 38 ){
					var oAutoComp1 = new YAHOO.widget.AutoComplete(coaCodeObj,'intGlAccountsContainer',interestGlcodeObj);
					oAutoComp1.queryDelay = 0;
	  				oAutoComp1.prehighlightClassName = "yui-ac-prehighlight";
	  				oAutoComp1.useShadow = true;
	  				oAutoComp1.forceSelection = true; 
	  				oAutoComp1.maxResultsDisplayed = 15;
	    			oAutoComp1.useIFrame = true;
				}
			}
			yuiflag2[0] = 1;
		}
	}

  	function setInterestGlcodeAfterSplit(obj){
  		var temp = obj.value;
  		var temp1 = temp.split("`-`");
  		document.payheadForm.interestGlcode.value = temp1[0];
  		//document.payheadForm.chkGlcode.value = temp1[1];  		
  		if(temp1[1]!=null)
	  		document.getElementById("interestAccountName").value = temp1[1];
  	}

  	function setInterestGlcodeFlagUndefined(){
 		yuiflag2[0] = undefined;
	}
   
   function trim1(obj,value)	{
    value = value;    
    while (value.charAt(value.length-1) == " ")
    {
     value = value.substring(0,value.length-1);
    } 
    while(value.substring(0,1) ==" ")
    {
     value = value.substring(1,value.length);
    }
    obj.value = value;
   return value ;
}

	function callInterestAct(obj){
	//	alert(obj.checked);
		document.getElementById("interestGlcode").value = "";
		document.getElementById("interestAccountName").value = "";
		if(obj.checked == true){
			document.getElementById("interestActLbl").style.display = "block";
			document.getElementById("interestActId").style.display = "block";
			document.getElementById("interestActNameLbl").style.display = "block";
			document.getElementById("interestActNameId").style.display = "block";
		}
		if(obj.checked == false){
			document.getElementById("interestActLbl").style.display = "none";
			document.getElementById("interestActId").style.display = "none";
			document.getElementById("interestActNameLbl").style.display = "none";
			document.getElementById("interestActNameId").style.display = "none";
		}

	}
/**
* this api will set the values for check box property based on form check box 'checked' value
*/
function setCheckBoxValue(propertyname)
{
	if(document.getElementById(propertyname).checked)
		document.getElementById(propertyname).value='Y';
	else
		document.getElementById(propertyname).value='N';
	if(propertyname=='isAttendanceBased')
	{
	  	if(document.getElementById(propertyname).checked)
	  	{
	     	document.getElementById("isRecomputed").value='Y';
	     	document.getElementById("isRecomputed").checked=true;
	     	document.getElementById("isRecomputed").disabled=true;
	 	}else
	  	{
	  		document.getElementById("isRecomputed").value='N';
	  		document.getElementById("isRecomputed").checked=false;
	     	document.getElementById("isRecomputed").disabled=false;
	  	}
	}
}
/**
* this API will disable the isAttendence,isRecomputed check boxes ,based on calculation type
*/
function disableChckBoxs()
{
   if(document.payheadForm.calType.value=="MonthlyFlatRate"){
	   document.getElementById("captureRate").disabled=false;
		//document.getElementById("isRecomputed").value='N';
		//document.getElementById("isRecomputed").checked=false;
		//document.getElementById("isRecomputed").disabled=true;
		//document.getElementById("isAttendanceBased").value='N';
		//document.getElementById("isAttendanceBased").checked=false;
		//document.getElementById("isAttendanceBased").disabled=true;
	 
   }else{
	  document.getElementById("captureRate").disabled=true;	
      document.getElementById("isAttendanceBased").disabled=false; 
      document.getElementById("isRecomputed").disabled=false;
      }
      
   var valtemp=document.getElementById("recoveryAC").value;
   if(valtemp!="")
   {
       document.getElementById("isAttendanceBased").disabled=false; 
       document.getElementById("isRecomputed").disabled=false;   
   }
}
function stripe(id) {
    var even = false;
    var evenColor = "#fff";
    var oddColor = "#eee";
  
    var table = document.getElementById(id);
   
    var tbodies = table.getElementsByTagName("body");
    for (var j = 0; j < tbodies.length; j++) {
      var trs = tbodies[j].getElementsByTagName("tr").style.visibility="block";
      for (var i = 0; i < trs.length; i++) {
      if (!hasClass(trs[i]) && ! trs[i].style.backgroundColor) {
          var tds = trs[i].getElementsByTagName("td");
          for (var j = 0; j < tds.length; j++) {
            var mytd = tds[j];
          if (! hasClass(mytd) && ! mytd.style.backgroundColor) {
          mytd.style.backgroundColor = even ? evenColor : oddColor;
            }
          }
        }
        even =  ! even;
      }
    }
  }
  
  function callMethodsOnLoad()
  {
  	stripe('paytable');
  	loadAllIncomeGlcodes('getAllIncomeGlcodesFromAccount');
  	loadAllGlcodes('getAllGlcodesFromAccount');
  }
</script>

</head>

<body onload="callMethodsOnLoad()">


	    
<html:javascript formName="payheadForm" />
<html:form action="/payhead/showPayhead">

	<input type="hidden" name="chkGlcode"/>


	<table width="95%"  cellpadding="0" cellspacing="0" border="0" id="paytable">
		<tr>
                <td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">Define Payhead</div></td>
              </tr>
		
		<tr>
			<td class="whiteboxwk"><span class="mandatory">*</span><bean:message key="PayheadName"/>:</td>
			<td class="whitebox2wk">
			<html:text property="name" onblur="trim1(this,this.value);checkAlphaNumeric(this);" styleClass="selectwk"/></td>
			<td class="whiteboxwk"><span class="mandatory">*</span><bean:message key="PayheadOrder"/>:</td>
			<td class="whitebox2wk"><html:text property="orderNo" value="<%= orderId %>"
				onblur="trim1(this,this.value);checkdecimalval(this,this.value);" styleClass="selectwk" /></td>	
		</tr>
		<tr>
			<td class="greyboxwk" ><span class="mandatory">*</span><bean:message key="PayheadDescription"/></td>
			<td class="greybox2wk" >
				<html:text property="description"	onblur="trim1(this,this.value);" styleClass="selectwk" />
			</td>
			<td class="greyboxwk" ><span class="mandatory">*</span><bean:message key="LocalDescription"/> </td>
			<td class="greybox2wk">
				<html:text property="localLangDesc"	onblur="trim1(this,this.value);" styleClass="selectwk" />
			</td>
		</tr>
		<tr>
			<td class="whiteboxwk"><span class="mandatory">*</span><bean:message key="PayheadType"/></td>
			<td class="whitebox2wk">
				<html:select property="type" onchange="callCategory();callCalType(this);" styleClass="selectwk">
					<html:option value="">-----------Select-----------</html:option>
					<html:option value="D">Deduction</html:option>
					<html:option value="E">Earning</html:option>
				</html:select>
			</td>
			<td class="whiteboxwk" id="categoryRowId" style="display: none" ><span class="mandatory">*</span><bean:message key="PayheadCategory"/></td>
			<td class="whitebox2wk"  >
				<span id="earningCategory" style="display: none">
					<html:select property="categoryE">
						<html:option value="">-----------Select------------</html:option>
					  	<%
					  		for(int i=0;i<categoryE.size();i++){
							SalaryCategoryMaster scm = (SalaryCategoryMaster)categoryE.get(i);
						%>
							<html:option value="<%=scm.getName()%>"><%=scm.getName()%></html:option>
						<%
							}
						%>
					</html:select>
				</span>
				<span class="whitebox2wk" id="deductionCategory" style="display: none" align="left">
					<html:select property="categoryD" onchange="callRecoveryAC(this);">
						<html:option value="">-----------Select------------</html:option>
					  	<%
					  		for(int i=0;i<categoryD.size();i++){
							SalaryCategoryMaster scm = (SalaryCategoryMaster)categoryD.get(i);
						%>
							<html:option value="<%=scm.getName()%>"><%=scm.getName()%></html:option>
						<%
							}
						%>
					</html:select>
				</span>
			</td>
		</tr>
		<tr>
			<td class="greyboxwk" ><span class="mandatory">*</span><bean:message key="CalculationType"/></td>
			<td class="greybox2wk" > 
				<html:select property="calType" onchange="callPctBasis();callSlab(this);disableChckBoxs();" styleClass="selectwk">
					<html:option value="">-----------Select-----------</html:option>
		<!--			<html:option value="MonthlyFlatRate">Monthly Flat Rate</html:option>
					<html:option value="ComputedValue">Computed Value(%)</html:option>	-->
				</html:select>
			</td>
			<td class="greyboxwk">
				<span  id="pctBasisRowLbl" style="color:#000000;font-size:11px;display:none">
					 <span class="mandatory">*</span><bean:message key="%Basis"/>
				</span>
			</td>
			<td class="greybox2wk">
				<span  id="pctBasisRowId" style="display:none;">
					<html:select property="pctBasis">
						<html:option value="">-----------Select------------</html:option>
					  	<%
					  		for(int i=0;i<salaryCodes.size();i++){
							SalaryCodes sc = (SalaryCodes)salaryCodes.get(i);
						%>
							<html:option value="<%=sc.getHead()%>"><%=sc.getHead()%></html:option>
	
						<%
						   }
						%>
					</html:select>
				</span>
			</td>

		</tr>
		  
		<tr >
			<td class="whiteboxwk" >
				<span id="recoveryRowLbl" style="color:#000000;font-size:11px;display:none">
					<bean:message key="RecoveryAccount"/>
		    	</span>
		    	<span id="slabRowLbl" style="color: #000000;font-size:11px;display:none">
		    		<span class="mandatory">*</span><bean:message key="SlabDetails"/>
		    	</span>
		    </td>
			<td  class="whitebox2wk" colspan="3">				
				<span id="recoveryRowId" style="display:none;">
					<html:select styleId="recoveryAC" property="recoveryAC" onchange="callAfterRecovery(this);disableChckBoxs()" styleClass="selectwk">
						<html:option value="">-----------Select------------</html:option>		
					</html:select>
				</span>
				<span id="slabRowId" style="display:none;">
					<html:select property="slabAC" onchange="callAfterSlab(this);" styleClass="selectwk">
						<html:option value="">-----------Select------------</html:option>		
					</html:select>
				</span>
			</td>			
		</tr>
		
 	 	<tr>
			<td class="whiteboxwk" align="right"><span class="mandatory">*</span><bean:message key="AccountCode"/></td>
			<td align="left" class="whitebox2wk">
				<html:text property="glcode" 
				onkeyup="autocompleteGlcode(this,event);" onblur="setGlcodeFlagUndefined();setGlcodeAfterSplit(this)" styleClass="selectwk"/>
			
			</td>
			
			<td class="whiteboxwk" align="right"><bean:message key="AccountName"/> </td>
			<td align="left" class="whitebox2wk"><input type="text" id="accountName" readonly="readonly" class="selectwk"/>
		</tr>
		<tr >
			<td class="greyboxwk" align="right"><bean:message key="IsAttendanceBased"/>
			</td>
			<td class="greybox2wk"  align="left">
			<html:checkbox property="isAttendanceBased" styleId="isAttendanceBased" onclick="setCheckBoxValue('isAttendanceBased');" />
			</td>
			<td class="greyboxwk" ><bean:message key="IsRecomputed"/>&nbsp;&nbsp;&nbsp;</td>
			<td class="greybox2wk" >
			<html:checkbox property="isRecomputed"  styleId="isRecomputed" onclick="setCheckBoxValue('isRecomputed');" />
			
				<a href="#" class="hintanchor" 
				onMouseover="showhint('If Payhead is attendanced based, the recomputed flag is checked by default', this, event, '300px');">
				<img src="../common/image/help.png" alt="Note" width="16" height="16" border="0" align="absmiddle"/></a>
			</td>
			</td>
		</tr>
		<tr>
			<td class="whiteboxwk" align="right" ><bean:message key="IsRecurring"/> </td>
			<td align="left" class="whitebox2wk" colspan="3"><html:checkbox property="isRecurring" styleId="isRecurring" onclick="setCheckBoxValue('isRecurring');" /></td>
		</tr>
		
		<tr>
			<td class="greyboxwk" align="right">
				<span id="taxableLbl" style="color: #000000;font-size:11px;display:none">
					<bean:message key="Taxable"/>
				</span>
				<span id="interestcheckLbl" style="color: #000000;font-size:11px;display:none">
					<bean:message key="IntBearing"/>
				</span>
			</td>
			<td align="left" class="greybox2wk" colspan="3">
				<span id="taxableId" style="display:none">
					<input type="checkbox" name="isTaxable"/>
				</span>
				<span id="interestcheckId" style="display:none">
					<input type="checkbox" name="isInterest" onclick="callInterestAct(this)"/>
				</span>
			</td>
		</tr>	

		<tr>
			<td class="greyboxwk" align="right">
				<span id="interestActLbl" style="color: #000000;font-size:11px;display:none">
					<span class="mandatory">*</span><bean:message key="IntAccCode"/>
				</span>
			</td>
			<td align="left" class="greybox2wk">
				<span id="interestActId" style="display:none">
					<input type="text" name="interestGlcode" id="interestGlcode" 
					onkeyPress="autocompleteInterestGlcode(this,event);" onblur="setInterestGlcodeFlagUndefined();setInterestGlcodeAfterSplit(this)" >
				</span>
			</td>
			<td class="greyboxwk" align="right">
				<span id="interestActNameLbl" style="color: #000000;font-size:11px;display:none">
					<bean:message key="AccountName"/>
				</span>
			</td>
			<td align="left" class="greybox2wk">
				<span id="interestActNameId" style="display:none">
					<input type="text" id="interestAccountName" readonly="readonly"/>
				</span>
			</td>
		</tr>
		
		<tr id="isBasicRateCaptureId">
			<td class="whiteboxwk" align="right"><bean:message key="IsBasicRateCapture"/></td>
			<td align="left" class="whitebox2wk" >
				<html:checkbox styleId="captureRate" property="captureRate" onclick="setCheckBoxValue('captureRate');" />			
			</td>
			<td class="whiteboxwk" align="right">Advance validation rule</td>
			<td align="left" class="whitebox2wk" >
				<html:select property="ruleScript" styleClass="selectwk">
		  			<html:option value="">---choose---</html:option>
		  			<c:forEach var="ruleScriptVar" items="${wfActionList}">
		  				<html:option value="${ruleScriptVar.id}" >${ruleScriptVar.description}</html:option>
		  			</c:forEach>
  				</html:select>			
			</td>
		</tr>
		<tr>
        	<td colspan="4" class="shadowwk"></td>
        </tr>
        <tr>
          	<td colspan="4"><div align="right" class="mandatory">* Mandatory Fields</div></td>
        </tr>
		<tr>
			<td colspan="2" align="right">
				<html:submit value="Submit" onclick="return checkOnSubmit();" styleClass="buttonfinal" />	
			</td>
			<td colspan="2" align="left">
				<input type="button" name="cancel" value="Cancel" onclick="history.go(0)" class="buttonfinal"/>	
			</td>
		</tr>
		<tr><td><div id="accountsContainer"></div></td></tr>
		<tr><td><div id="intGlAccountsContainer"></div></td></tr>
	</table>

	
</html:form>


       
     
</body>

</html>
