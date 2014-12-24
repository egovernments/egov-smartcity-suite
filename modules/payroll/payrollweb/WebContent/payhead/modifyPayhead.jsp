<%@ include file="/includes/taglibs.jsp" %>
<%@ page import="org.egov.payroll.dao.*,java.util.*,
org.egov.infstr.*,org.egov.infstr.commons.dao.*,
org.egov.infstr.commons.*,org.egov.payroll.model.*,
java.text.SimpleDateFormat,
org.egov.payroll.model.* "%>

<html>
<head>
<title>Modify Payhead</title>
<!--<style type="text/css">
	#accountsContainer {left:24.3em;width:270%}
	#accountsContainer .yui-ac-content {position:absolute;width:100%;border:1px solid#404040;background:#fff;overflow:hidden;z-index:9050;}
	#accountsContainer .yui-ac-shadow {position:absolute;margin:.3em;width:100%;background:#a0a0a0;z-index:9049;}
	#accountsContainer ul {padding:5px 0;width:100%;}
	#accountsContainer li {padding:0 5px;cursor:default;white-space:nowrap;}
	#accountsContainer li.yui-ac-highlight {background:#ff0;}
	#accountsContainer li.yui-ac-prehighlight {background:#FFFFCC;}
	</style>	-->
	<style type="text/css">
		#accountsContainer {position:absolute;left:11em;width:9%}
		#accountsContainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
		#accountsContainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
		#accountsContainer ul {padding:5px 0;width:80%;}
		#accountsContainer li {padding:0 5px;cursor:default;white-space:nowrap;}
		#accountsContainer li.yui-ac-highlight {background:#ff0;}
		#accountsContainer li.yui-ac-prehighlight {background:#FFFFCC;}
	</style>

	<%
		List salaryCodes = new ArrayList();
		SalaryCodesDAO salaryCodesDAO = PayrollDAOFactory.getDAOFactory().getSalaryCodesDAO();
		salaryCodes =(List)salaryCodesDAO.findAll();		
		SalaryCodes basePayhead = (SalaryCodes)request.getAttribute("salarycode");
		
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
	   			alert('Please enter Valid name');
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
		if(document.payheadForm.chkGlcode.value == "undefined"){
			alert('<bean:message key="alertCorrectGlcode"/>');
			document.payheadForm.glcode.focus();			
			return false;
		}
		if(document.payheadForm.modifyRemarks.value==""){
			alert('<bean:message key="alertRemarks"/>');
			document.payheadForm.modifyRemarks.focus();
			return false;
		}
		if(document.payheadForm.type.value=="D" && document.payheadForm.categoryE.value != "Deduction-Advance" 
												&& document.payheadForm.recoveryAC.value == ""
												&& document.payheadForm.categoryE.value != "Deduction-Other"){
			alert('<bean:message key="alertRecoveryAccount"/>');
			document.payheadForm.recoveryAC.focus();
			return false;
		}		
		if(document.payheadForm.localLangDesc.value == ""){
			alert('<bean:message key="alertLocalDescription"/>');
			document.payheadForm.localLangDesc.focus();			
			return false;
		}
		if(document.payheadForm.name.value==""){
			alert('<bean:message key="alertPayheadName"/>');
			document.payheadForm.name.focus();
			return false;
		}
		if(document.payheadForm.description.value==""){
			alert('<bean:message key="alertDecsription"/>');
			document.payheadForm.description.focus();
			return false;
		}
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
		if(document.payheadForm.categoryE.value==""){
			alert('<bean:message key="alertCategory"/>');
			document.payheadForm.categoryE.focus();
			return false;
		}
		/*if(document.payheadForm.isInterest.checked)
			document.payheadForm.isInterest.value='Y';
		else
			document.payheadForm.isInterest.value='N'*/
		document.getElementById("isRecomputed").disabled=false;
		document.getElementById("isAttendanceBased").disabled=false;
	}
	
	function callCategory(){		
		if(document.payheadForm.type.value=="E"){			
			document.payheadForm.glcode.disabled=false;
			document.payheadForm.accountName.disabled=false;
			document.getElementById("recovery").style.display="none";
			document.getElementById("recoveryAccountLabelId").style.display="none";
			document.getElementById("recoveryRowId").style.display="none";
			document.getElementById("taxableRowId").style.display="";
			document.getElementById("interestCheckId").style.display="none";
			document.getElementById("interestActId").style.display="none";
			var count = 1;			
			for(var resLen=1;resLen<document.payheadForm.categoryE.length;resLen++){
		      document.payheadForm.categoryE.options[resLen]=null;
    	 	}	
			<c:forEach var="salCategoryObj" items="${salaryCategorysE}">				
				document.payheadForm.categoryE.options[count] = new 
				Option("${salCategoryObj.name}","${salCategoryObj.name}");
				count=count + 1;
			</c:forEach>
		}
		if(document.payheadForm.type.value=="D"){
			document.getElementById("taxableRowId").style.display="none";
			document.getElementById("interestCheckId").style.display="";
			var count = 1;					
			for(var resLen=1; resLen<document.payheadForm.categoryE.length; resLen++){					
		        document.payheadForm.categoryE.options[resLen]=null;
    	 	}	    	 	
			<c:forEach var="salCategoryObj" items="${salaryCategorysD}">
				document.payheadForm.categoryE.options[count] = new 
				Option("${salCategoryObj.name}","${salCategoryObj.name}");
				count = count + 1;
			</c:forEach>
			document.getElementById("slabRowId").style.display="none";		
			callInterestAct(document.payheadForm.isInterest);
		}
		if(document.payheadForm.type.value==""){	
			document.getElementById("interestActId").style.display="none";
			document.getElementById("interestCheckId").style.display="none";
			document.getElementById("taxableRowId").style.display="none";
			document.getElementById("recovery").style.display="none";
			document.getElementById("recoveryAccountLabelId").style.display="none";
			document.getElementById("recoveryRowId").style.display="none";
			document.getElementById("slabRowId").style.display="none";
			document.payheadForm.glcode.disabled=false;
			document.payheadForm.accountName.disabled=false;		
			for(var resLen=document.payheadForm.categoryE.length-1; resLen>0 ;resLen--){				
				document.payheadForm.categoryE.remove(resLen);			
			}	
		}
	}
	
	function callRecoveryAC(){
	//	alert(document.payheadForm.type.value);
	//	alert(document.payheadForm.categoryE.value);	
		document.payheadForm.glcode.disabled=false;	
		if(document.payheadForm.type.value=="D" && document.payheadForm.categoryE.value != "Deduction-Advance" 
												&& document.payheadForm.categoryE.value != "Deduction-BankLoan"
												&& document.payheadForm.categoryE.value != ""){
			for(var resLen=1;resLen<document.payheadForm.recoveryAC.length;resLen+1){
				document.payheadForm.recoveryAC.options[resLen]=null;
			}
			var count = 1;
			<c:forEach var="tdsObj" items="${tdses}">						
				 <c:if test = "${tdsObj.bank == null}">	
					<c:if test = "${tdsObj.isEarning == null || (tdsObj.isEarning != null && tdsObj.isEarning == '0')}">						
						document.payheadForm.recoveryAC.options[count] = new Option("${tdsObj.type}","${tdsObj.id}");	
						count=count+1;
					</c:if>
				</c:if>			
			</c:forEach>																					
			document.getElementById("recovery").style.display="";	
			document.getElementById("recoveryAccountLabelId").style.display="";
			document.getElementById("recoveryRowId").style.display="";
			document.payheadForm.glcode.disabled=true;
			document.payheadForm.accountName.disabled=true;
		}	
		else if(document.payheadForm.categoryE.value == "Deduction-BankLoan"){
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
			document.getElementById("recovery").style.display="";
			document.getElementById("recoveryAccountLabelId").style.display="";		
			document.getElementById("recoveryRowId").style.display="";	
			document.payheadForm.glcode.disabled=true;
			document.payheadForm.accountName.disabled=true;	
		}
		else{
			document.getElementById("recovery").style.display="none";	
			document.getElementById("recoveryAccountLabelId").style.display="none";	
			document.getElementById("recoveryRowId").style.display="none";
		}
		if(document.payheadForm.categoryE.value == "Deduction-Other" ){
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
		if(document.payheadForm.calType.value=="MonthlyFlatRate"){
			document.getElementById("pctBasis").style.display="none";
			document.getElementById("pctBasisRowId").style.display="none";
		}
		if(document.payheadForm.calType.value=="ComputedValue"){
			document.getElementById("pctBasis").style.display="block";
			document.getElementById("pctBasisRowId").style.display="";
		}
		if(document.payheadForm.calType.value=="SlabBased"){
			document.getElementById("pctBasisRowId").style.display="none";
			document.getElementById("pctBasis").style.display="none";
		}
		if(document.payheadForm.calType.value=="RuleBased"){
			document.getElementById("pctBasisRowId").style.display="none";
			document.getElementById("pctBasis").style.display="none";
		}
		if(document.payheadForm.calType.value==""){
			document.getElementById("pctBasis").style.display="none";
			document.getElementById("pctBasisRowId").style.display="none";
		}
	}


	function initiateRequest(){
		if (window.XMLHttpRequest) {
		var req=new XMLHttpRequest();
		if (req.overrideMimeType) req.overrideMimeType("text/html;charset=utf-8");
		return req;
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
// 	if(obj.name=='glcode') target.style.left=posSrc[0]+0;
	target.style.width=300;
 	var currRow=getRow(obj);
 	var coaCodeObj = obj;
	 	//var coaCodeObj = document.getElementById("glcode");
		if(yuiflag1[0] == undefined){
		var Key = window.event ? window.event.keyCode : myEvent.keyCode;  	
			if(Key != 40 ){
				if(Key != 38 ){
					var oAutoComp1 = new YAHOO.widget.AutoComplete(coaCodeObj,'accountsContainer',glcodeObj);
					oAutoComp1.queryDelay = 0;
					oAutoComp1.prehighlightClassName = "yui-ac-prehighlight";
					oAutoComp1.useShadow = true;
					oAutoComp1.maxResultsDisplayed = 100;
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
      	  req.open("GET", url, false);  /* the false here makes it synchronous */
            req.send(null);
	            if (req.status == 200){
                   	var glcodes=req.responseText
                   	var a = glcodes.split("^");
                   	var codes = a[0];
					interestGlcodeArray=codes.split("+");
					interestGlcodeObj = new YAHOO.widget.DS_JSArray(interestGlcodeArray);
	           }
  	}

  	function autocompleteInterestGlcode(obj,myEvent){
	var src = obj;
 	var target = document.getElementById('accountsContainer');
 	var posSrc=findPos(src);	
 	target.style.left=posSrc[0]+6;
 	target.style.top=posSrc[1];
 //	if(obj.name=='interestGlcode') target.style.left=posSrc[0]+0;
	target.style.width=300;
 	var currRow=getRow(obj);
 	var coaCodeObj = obj;
	 	//var coaCodeObj = document.getElementById("interestGlcode");
		if(yuiflag2[0] == undefined){
		var Key = window.event ? window.event.keyCode : myEvent.keyCode;  	
			if(Key != 40 ){
				if(Key != 38 ){
					var oAutoComp1 = new YAHOO.widget.AutoComplete(coaCodeObj,'accountsContainer',interestGlcodeObj);
					oAutoComp1.queryDelay = 0;
					oAutoComp1.prehighlightClassName = "yui-ac-prehighlight";
					oAutoComp1.useShadow = true;
					oAutoComp1.maxResultsDisplayed = 100;
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
	
	function callRecovery(){
		var type = "${salarycode.categoryMaster.catType}";
		var category = "${salarycode.categoryMaster.name}";
	//	alert(type + category);
		if(type == "D" && category != "Deduction-Advance" && category != "Deduction-BankLoan"){			
			for(var resLen=1;resLen<document.payheadForm.recoveryAC.length;resLen+1){
				document.payheadForm.recoveryAC.options[resLen]=null;
			}
			var count = 1;
			<c:forEach var="tdsObj" items="${tdses}">						
				 <c:if test = "${tdsObj.bank == null}">							
	 				document.payheadForm.recoveryAC.options[count] = new 
					Option("${tdsObj.type}","${tdsObj.id}");	
					count=count+1;
				</c:if>			
			</c:forEach>				
			document.getElementById("recovery").style.display="";
			document.getElementById("recoveryAccountLabelId").style.display="";
			document.getElementById("recoveryRowId").style.display="";
			document.payheadForm.recoveryAC.value = "${salarycode.tdsId.id}";
			<c:if test = "${salarycode.tdsId == null}">				
				document.payheadForm.glcode.value = "${salarycode.chartofaccounts.glcode}";
				document.payheadForm.accountName.value = "${salarycode.chartofaccounts.name}";	
				document.payheadForm.glcode.disabled=false;
				document.payheadForm.accountName.disabled=false;		
			</c:if>
			<c:if test = "${salarycode.tdsId != null}">											
				document.payheadForm.glcode.value = "${salarycode.tdsId.chartofaccounts.glcode}";
				document.payheadForm.accountName.value = "${salarycode.tdsId.chartofaccounts.name}";
				document.payheadForm.glcode.disabled=true;
				document.payheadForm.accountName.disabled=true;				
			</c:if>	
			
		}
		else if(document.payheadForm.categoryE.value == "Deduction-BankLoan"){
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
			document.getElementById("recovery").style.display="";
			document.getElementById("recoveryAccountLabelId").style.display="";
			document.getElementById("recoveryRowId").style.display="";
			document.payheadForm.recoveryAC.value = "${salarycode.tdsId.id}";
			document.payheadForm.glcode.value = "${salarycode.tdsId.chartofaccounts.glcode}";
			document.payheadForm.accountName.value = "${salarycode.tdsId.chartofaccounts.name}";
			document.payheadForm.glcode.disabled=true;
			document.payheadForm.accountName.disabled=true;
		}	
		else{
		//	alert("not recovery");
			document.getElementById("recovery").style.display="none";
			document.getElementById("recoveryAccountLabelId").style.display="none";			
			document.getElementById("recoveryRowId").style.display="none";
		}
		
		
	}	

	function callCalType(){		
		if(document.payheadForm.type.value=="E"){			
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
			document.getElementById("slabRowId").style.display="";
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

	function callInterestAct(obj){
		if(obj.checked == true){
			document.getElementById("interestActId").style.display = "";
		}
		if(obj.checked == false){
			document.getElementById("interestActId").style.display = "none";
		}

	}

	function populateFild(){		
		//document.payheadForm.type.value = "${salarycode.categoryMaster.catType}";
		callCategory();		
		callCalType();		
		document.payheadForm.calType.value="${salarycode.calType}";	
		document.payheadForm.categoryE.value = "${salarycode.categoryMaster.name}";
		callPctBasis();
		document.payheadForm.pctBasis.value = "${salarycode.salaryCodes.head}";
		if("${salarycode.isTaxable}" == 'Y'){
			document.getElementById("taxableRowId").style.display="";
			document.payheadForm.isTaxable.checked = true;
		}		
		
		disableChckBoxs();
		if("${salarycode.isAttendanceBased}" == 'Y'){
		  document.getElementById("isAttendanceBased").value='Y';
		  document.getElementById("isAttendanceBased").checked=true;
		 // document.getElementById("isRecomputed").value='Y';
	         // document.getElementById("isRecomputed").checked=true;
	          document.getElementById("isRecomputed").disabled=true;		
		}else
		{
		  document.getElementById("isAttendanceBased").value='N';
		}
		if("${salarycode.isRecomputed}" == 'Y'){
		  document.getElementById("isRecomputed").value='Y';
		  document.getElementById("isRecomputed").checked=true;
		}else
		  document.getElementById("isRecomputed").value='N';
		if("${salarycode.isRecurring}" == 'Y'){
		  document.getElementById("isRecurring").value='Y';
		  document.getElementById("isRecurring").checked=true;
		}
		if("${salarycode.captureRate}" == 'Y'){
		  document.getElementById("captureRate").value='Y';
		  document.getElementById("captureRate").checked=true;
		}
		document.getElementById("isRecurring").value='N';
		callRecoveryAC();			
		callSlab();
		document.payheadForm.slabAC.value="${salarycode.tdsId.id}";
		document.payheadForm.recoveryAC.value="${salarycode.tdsId.id}";
		<c:if test = "${salarycode.tdsId != null}">
			document.payheadForm.glcode.value="${salarycode.tdsId.chartofaccounts.glcode}";
			document.getElementById("accountName").value="${salarycode.tdsId.chartofaccounts.name}";
		</c:if>
		<c:choose>
		<c:when test = "${salarycode.interestAccount != null}">		
			document.getElementById("interestCheckId").style.display = ""
			document.payheadForm.isInterest.checked = true;
		</c:when>
		<c:otherwise>
			document.payheadForm.isInterest.checked = false;
		</c:otherwise>
		</c:choose>
		callInterestAct(document.payheadForm.isInterest);
		var valtemp=document.getElementById("recoveryAC").value;
	        if(valtemp!="")
		{
		    document.getElementById("isAttendanceBased").disabled=false; 
		    document.getElementById("isRecomputed").disabled=false;   
                }
		if(document.payheadForm.categoryE.value == "Deduction-BankLoan" || document.payheadForm.categoryE.value == "Deduction-Tax")
		{
			document.payheadForm.glcode.disabled=true;
			document.payheadForm.accountName.disabled=true;
		}
		if(document.payheadForm.recoveryAC.value != ""){
			document.payheadForm.glcode.disabled=true;
			document.payheadForm.accountName.disabled=true;
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
      }else
      {
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

</script>

</head>

<body onload="populateFild();">
	    
<html:form action="/payhead/modifyPayhead">
	
		<input type="hidden" name="chkGlcode"/>
	
	<table width="95%" cellpadding="0" cellspacing="0" border="0" id="paytable">
	<input type="hidden" name="payheadId" value="${salarycode.id}" />
		<tr>
			<td colspan="4" class="headingwk"><div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
                  <div class="headplacer">Modify Payhead</div></td>
		</tr>
		
		<tr>
			<td class="whiteboxwk" align="right"><bean:message key="PayheadName"/><font color="red">*</font></td>
			<td class="whitebox2wk"><html:text property="name" 
				onblur="trim(this,this.value);" readonly="true" styleClass="selectwk"/></td>
			<td class="whiteboxwk" align="right"><bean:message key="PayheadOrder"/><font color="red">*</font></td>
			<td class="whitebox2wk"><html:text property="orderNo" value="${salarycode.orderId}"
				onblur="trim(this,this.value);checkdecimalval(this,this.value);"  styleClass="selectwk"/></td>	
		</tr>
		<tr>
			<td class="greyboxwk" ><bean:message key="PayheadDescription"/><font color="red">*</font></td>
			<td  class="greybox2wk">
			<%
				String desc = ((SalaryCodes)request.getAttribute("salarycode")).getDescription();
				System.out.println("desc>>>>>>>>>>>>>>>>>>>>>>>>>"+desc);
				byte[] bytes = new byte[desc.length()];
				for (int i = 0; i < desc.length(); i++)
				{
				bytes[i] = (byte) desc.charAt(i);
				}
				desc = new String(bytes,"UTF-8");
			%>
			<html:text property="description"
				onblur="trim(this,this.value);" value="${salarycode.description}" styleClass="selectwk"/>
			</td>
			<td class="greyboxwk" ><bean:message key="LocalDescription"/> <font color="red">*</font></td>
			<td class="greybox2wk">
				<html:text property="localLangDesc"	onblur="trim(this,this.value);" style="font-family:TAB-LT-Lakshman;" value="${salarycode.localLangDesc}" styleClass="selectwk"/>
			</td>
		</tr>
		<tr>
			<td class="whiteboxwk" ><bean:message key="PayheadType"/><font color="red">*</font></td>
			<td  class="whitebox2wk">
			<html:select property="type" onchange="callCategory();callCalType();"  styleClass="selectwk" value="${salarycode.categoryMaster.catType}">
					<html:option value="">-----------Select-----------</html:option>
					<html:option value="D">Deduction</html:option>
					<html:option value="E">Earning</html:option>
				</html:select>
			</td>
			<td class="whiteboxwk" ><bean:message key="PayheadCategory"/><font color="red">*</font></td>
			<td id="earningcategory"  class="whitebox2wk">
				<html:select property="categoryE" value="${salarycode.categoryMaster.name}" onchange="callRecoveryAC();" styleClass="selectwk" >
					<html:option value="">-----------Select------------</html:option>				  	
				</html:select>
			</td>			
		</tr>
		<tr>
			<td class="greyboxwk" ><bean:message key="CalculationType"/> <font color="red">*</font></td>
			<td class="greybox2wk">
				<html:select property="calType" style="width: 150;" onchange="callPctBasis();callSlab();disableChckBoxs();"
					 value="${salarycode.calType}">
					<html:option value="">-----------Select-----------</html:option>				
				</html:select>
			</td>
			<td class="greyboxwk" id="pctBasisRowId"  style="display:none"><bean:message key="%Basis"/><font color="red">*</font></td>
			<td id="pctBasis" class="greybox2wk" >
				<html:select property="pctBasis" >
					<html:option value="">-----------Select------------</html:option>
				  	<%
				  		for(int i=0;i<salaryCodes.size();i++){
							SalaryCodes sc = (SalaryCodes)salaryCodes.get(i);							
							if(sc != basePayhead  &&  sc.getSalaryCodes() != basePayhead && sc.getOrderId().compareTo( basePayhead.getOrderId()) == -1){
					%>
						<html:option value="<%=sc.getHead()%>"><%=sc.getHead()%></html:option>
					<%
							}			
						}
					%>
				</html:select>
			</td>

		</tr>
		<tr id="recoveryRowId" >
		

			<td  class="whiteboxwk" id="recoveryAccountLabelId" ><bean:message key="RecoveryAccount"/> </td>
			<td id="recovery" class="whitebox2wk" >
				<html:select property="recoveryAC"  style="width: 150;" styleId="recoveryAC" onchange="callAfterRecovery(this);disableChckBoxs();" >
					<html:option value="">-----------Select-----------</html:option>
				</html:select>		
				</td>
					
		</tr>
		<tr id="slabRowId" style="display:none;">
			<td class="whiteboxwk" align="right"><bean:message key="SlabDetails"/> <font color="red">*</font></td>
			<td class="whitebox2wk">				
				<html:select property="slabAC" styleId="slabAC" onchange="callAfterSlab(this);" value="${salarycode.tdsId.id}" styleClass="selectwk">
					<html:option value="">-----------Select------------</html:option>		
				</html:select>
			</td>			
		</tr>
 	 	<tr>
			<td class="whiteboxwk" ><bean:message key="AccountCode"/> <font color="red">*</font></td>
			<td class="whitebox2wk"><html:text property="glcode" styleId="glcode" onfocus="loadAllGlcodes('getAllGlcodesFromAccount');"
				onkeyup="autocompleteGlcode(this,event);" onblur="setGlcodeFlagUndefined();setGlcodeAfterSplit(this)" 
				value="${salarycode.chartofaccounts.glcode}" styleClass="selectwk" />
			</td>
			<td class="whiteboxwk"><bean:message key="AccountName"/> </td>
			<td class="whitebox2wk">
				<input type="text" id="accountName" readonly="readonly" value="${salarycode.chartofaccounts.name}" class="selectwk"/>
			 </td>
		</tr>
		<tr>
			<td class="greyboxwk" ><bean:message key="IsAttendanceBased"/></td>
			<td class="greybox2wk">
				<html:checkbox property="isAttendanceBased" styleId="isAttendanceBased" onclick="setCheckBoxValue('isAttendanceBased');"  />
				<!-- /<html:checkbox property="isRecomputed" styleId="isRecomputed" onclick="setCheckBoxValue('isRecomputed');" /> -->
			</td>
			<td class="greyboxwk" ><bean:message key="IsRecomputed"/>&nbsp;&nbsp;&nbsp;</td>
			<td class="greybox2wk"><html:checkbox property="isRecomputed" styleId="isRecomputed" onclick="setCheckBoxValue('isRecomputed');" />
			<a href="#" class="hintanchor" 
				onMouseover="showhint('If Payhead is attendanced based, the recomputed flag is checked by default', this, event, '300px');">
				<img src="../common/image/help.png" alt="Note" width="16" height="16" border="0" align="absmiddle"/></a></td>

			
		</tr>
		<tr>
		<td class="whiteboxwk" ><bean:message key="IsRecurring"/> </td>
			<td class="whitebox2wk" colspan="2"><html:checkbox property="isRecurring" styleId="isRecurring" onclick="setCheckBoxValue('isRecurring');" />
			
		</tr>
			
		<tr id="taxableRowId" style="display:none">
			<td class="whiteboxwk"><bean:message key="Taxable"/></td>
			<td  class="whitebox2wk"><input type="checkbox" name="isTaxable" id="isTaxable" />
			</td>			
		</tr>

		<tr id="interestCheckId" name="interestCheckId" style="display:none">
			<td class="whiteboxwk" >Interest bearing</td>
			<td class="whitebox2wk">
				<html:checkbox property="isInterest" styleId="isInterest"   onclick="callInterestAct(this)"/>
			</td>
		</tr>
		<tr id="interestActId" style="display:none">
			<td class="whiteboxwk">Interest Account Code<font color="red">*</font></td>
			<td class="whitebox2wk">
				<html:text property="interestGlcode" styleId="interestGlcode" onfocus="loadAllIncomeGlcodes('getAllIncomeGlcodesFromAccount');"
				onkeyup="autocompleteInterestGlcode(this,event);" onblur="setInterestGlcodeFlagUndefined();setInterestGlcodeAfterSplit(this)" 
				value="${salarycode.interestAccount.glcode}" styleClass="selectwk"/>
			
			</td>
			
			<td class="whiteboxwk">Account Name</td>
			<td align="left" class="whitebox2wk"><input type="text" id="interestAccountName" readonly="readonly" value="${salarycode.interestAccount.name}"
			class="selectwk"/>
		</tr>
		<tr>
			<td class="greyboxwk"><bean:message key="IsBasicRateCapture"/></td>
			<td class="greybox2wk" >
				<html:checkbox property="captureRate" styleId="captureRate" onclick="setCheckBoxValue('captureRate');" />			
			</td>
			<td class="greyboxwk" align="right">Advance validation rule</td>
			<td class="greyboxwk" >
				<html:select property="ruleScript" styleClass="selectwk" value="${salarycode.validationRuleScript.id}">
		  			<html:option value="">---choose---</html:option>
		  			<c:forEach var="ruleScriptVar" items="${wfActionList}">
		  				<html:option value="${ruleScriptVar.id}" >${ruleScriptVar.description}</html:option>
		  			</c:forEach>
  				</html:select>			
			</td>
		</tr>
		<tr>
			<td class="whiteboxwk"><b><span class="mandatory">*</span>Remarks</b></td>
      <td class="whitebox2wk">
      <input type="text" class="selectwk" name="modifyRemarks" />
      </td>
		</tr>
		
		<tr><td><div id="accountsContainer"></div></td></tr>
		<tr>
                <td colspan="9" class="shadowwk"></td>
              </tr>
	</table>
	
              <tr>
                <td colspan="4"><div align="right" class="mandatory">* Mandatory Fields</div></td>
              </tr>
	<p style="text-align: center"><html:submit value="Submit" onclick="return checkOnSubmit();" styleClass="buttonfinal" />
	
</html:form>


</body>
</html>
