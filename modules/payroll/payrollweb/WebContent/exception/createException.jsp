<%@ include file="/includes/taglibs.jsp" %>
<%@page import="java.util.*" %>
<html>
<head>
	<title>eGov EIS Payroll</title>
	<style type="text/css">
		.yui-skin-sam .yui-ac-container {
			position:absolute;
			top:1.6em;
			width:250px;
		}
		#codescontainer {position:absolute;left:11em;width:9%}
		#codescontainer .yui-ac-content {position:absolute;width:80%;border:1px solid #404040;background:#fff;overflow:hidden;z-index:9050;}
		#codescontainer .yui-ac-shadow {position:absolute;margin:.3em;width:40%;background:#a0a0a0;z-index:9049;}
		#codescontainer ul {padding:5px 0;width:80%;}
		#codescontainer li {padding:0 5px;cursor:default;white-space:nowrap;}
		#codescontainer li.yui-ac-highlight {background:#ff0;}
		#codescontainer li.yui-ac-prehighlight {background:#FFFFCC;}		

</style>
<%
String currDatetemp="" ;

java.util.Date date = new java.util.Date();
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");

String disId=null;
if(request.getAttribute("disciplinaryId")!=null)
{
	disId = (String) request.getAttribute("disciplinaryId");
}
System.out.println("dis id==="+disId);
Map<String,String> disEmpDetails = new HashMap<String,String>();
if(request.getAttribute("disEmpDetails")!=null){
	disEmpDetails = (HashMap<String,String>)request.getAttribute("disEmpDetails");
	System.out.println("1==="+disEmpDetails.get("disEmpId"));
	System.out.println("2==="+disEmpDetails.get("disEmpCode"));
	System.out.println("3==="+disEmpDetails.get("disEmpName"));
}
%>
<script language="JavaScript"  type="text/JavaScript">

	var year;
	var month;
	
	function deleteSpecificRow(obj,tableId)
	{
		if(document.getElementById('disciplinaryId').value!=null && document.getElementById('disciplinaryId').value!=''){
			return false;
		}
		else{
			var delRow = obj.parentNode.parentNode.parentNode.parentNode;
			var rIndex = delRow.rowIndex;
			var tbl=document.getElementById(tableId);
			var rowt=tbl.rows.length;
			if(rowt>2)//row1 is header and row2 on wards values
			{
			  tbl.deleteRow(rIndex);;
			  return true;
			}
			else
			alert("This Row can not be deleted");
		}
	
	}
		
	/* if addrow will be invoke onclicking of button */
	function addExceptionRow(tbl,objr){
		
		if(document.getElementById('disciplinaryId').value!=null && document.getElementById('disciplinaryId').value!='')
		{
			return false;
		}
		else{
			var obj = document.getElementById(objr);
			
			if(checkEmpCode() && objr=="earnings")
			    addRowToTable(tbl,obj);
		}
	}
	function deleteExceptionRow(tbl,objr){
		var obj = document.getElementById(objr);
		var table = document.getElementById(tbl);	
		var row = table.rows[table.rows.length-1] 
		deleteRow(tbl,row);
	}
		
	function deleteRow(table,obj){
		if(table=='paytable'){
			var tbl = document.getElementById(table);		
			var rowNumber=getRow(obj).rowIndex;
			if(tbl.rows.length == 2){
				if(${fn:length(exceptionForm.empCode)}<(eval(rowNumber)-1))
				   tbl.deleteRow(rowNumber)
				else{
		    		alert("You cannot delete this row");
		    		return false;
				}
			}
			else
				tbl.deleteRow(rowNumber)		
		}
	}
	
	function addRowToTable(tbl,obj)
	{
	  	var rowObj1=getRow(obj);
	  	var rowIndex=rowObj1.rowIndex;
	   	if(tbl=='paytable' )// && lastRow<getControlInBranch(tableObj.rows[rowObj1.rowIndex],'empCode').length)
	  	{
	  		var isAddRowValid=validateOnAddRow(document.getElementById(tbl).rows.length);
	  		if (!isAddRowValid)
	  		{
	  			return false;
	  		}
		  	else
		  	{
	      		var tableObj=document.getElementById(tbl);
	  	   		var rowObj = tableObj.rows[rowIndex].cloneNode(true);
	  	  		tableObj=document.getElementById(tbl);
	  			var tbody=tableObj.tBodies[0];  
	       		tbody.appendChild(rowObj);
	       		var lastRow = tableObj.rows.length;
	  	  		document.exceptionForm.type[(tableObj.rows.length-2)].value=document.exceptionForm.type[(rowIndex-1)].value;
	  	   		document.exceptionForm.reason[(tableObj.rows.length-2)].value=document.exceptionForm.reason[(rowIndex-1)].value;  	  
	  	   		var remlen1=document.exceptionForm.empCode.length;
	  	   		document.exceptionForm.empCode[0].value = "" ;
	  	   		document.exceptionForm.empCode1[0].value = "" ;
	       		document.exceptionForm.empName[0].value="";
	 	   		document.exceptionForm.type[0].value="";
	 	  		document.exceptionForm.reason[0].value=""; 	 
	 	   		document.exceptionForm.comments[0].value="";
	 		} 	 
	  	}
	}

	function validation(arg){	
		var table = document.getElementById("paytable");
		var tbl = table.rows.length;
		year= document.exceptionForm.financialYear.value;
    	month = document.exceptionForm.month.value;
    	if(document.exceptionForm.financialYear.value=="")
		{
			 alert("Please select the financial year from the drop down");
			 document.exceptionForm.financialYear.focus();
			 return false;
		}
		if(document.exceptionForm.month.value=="")
		{
			 alert("Please select the month from the drop down");
			 document.exceptionForm.month.focus();
			 return false;
		}
		
		var expdate=document.getElementById('createdOn').value;
		if(expdate=="")
		{
			 alert("Please Enter createdOn date");
			 document.exceptionForm.createdOn.focus();
			 return false;
		}
		else if(expdate=="dd/mm/yyyy")
		{
			alert("Please Enter correct date");
			 document.exceptionForm.createdOn.focus();
			 return false;
		}
		else
		{
			validateDateFormat(document.getElementById('createdOn'));
		}
		<% 	
		try{
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
			java.util.Date currDate=  new java.util.Date();
			currDatetemp = formatter.format(currDate);
		}
		catch(Exception e){
			e.printStackTrace();	
		}

		%>
		var currentdate = "<%=currDatetemp%>"; 
		var today=currentdate;
		
		if(compareDate(expdate,currentdate)== -1)
		{
			alert('Date should be less than or equal to '+today);			
			document.getElementById('createdOn').value="";
			document.getElementById('createdOn').focus();
			return false;
		}
		if(tbl==2 && checkExistingExceptionForEmp(document.exceptionForm.empCode.value,year,month) == "false"){
			alert("Exception  for "+document.exceptionForm.empName.value+" already created for this month and year");
			document.exceptionForm.empCode1.focus();
     		return false;	
		}
		else
		{
			if(!validateOnAddRow(tbl))
			{
				return false;
			}
		}
		
     	document.exceptionForm.action = "${pageContext.request.contextPath}/exception/beforeException.do?submitType=createException";
  	}
  	function validateOnAddRow(tbl)
  	{
 		 if(tbl == 2){			
		     if(document.exceptionForm.empCode1.value=="" || document.exceptionForm.empCode.value=="")
			 {
				 alert("Please enter the Employee Code ");
				 document.exceptionForm.empCode1.focus();
				 return false;
			 }
			 if(document.exceptionForm.type.value=="")
			 {
				 alert("Please select the Action from the drop down");
				 document.exceptionForm.type.focus();
				 return false;
			 }
			 if(document.exceptionForm.reason.value=="")
			 {
				 alert("Please select the reason from the drop down");
				 document.exceptionForm.reason.focus();
				 return false;
			 }
			 if(document.exceptionForm.comments.value=="")
			 {
				 alert("Please enter the comments ");
				 document.exceptionForm.comments.focus();
				 return false;
			 }
		}
	    else{
			 var empIdObj = document.exceptionForm.empCode;
		
			 for(var i=0; i<empIdObj.length; i++){
			 	if(document.exceptionForm.empCode[i].value=="" || document.exceptionForm.empCode1[i].value==""){			 			
					 alert("Please select the Employee Code from the drop down");
					 document.exceptionForm.empCode1[i].focus();
					 return false;
				 }
				if(document.exceptionForm.type[i].value==""){
					 alert("Please select the Action from the drop down");
					 document.exceptionForm.type[i].focus();
					 return false;
				}
			    if(document.exceptionForm.reason[i].value==""){
					 alert("Please select the reason from the drop down");
					 document.exceptionForm.reason[i].focus();
					 return false;
				}
				if(document.exceptionForm.comments[i].value==""){
					 alert("Please enter the comments for the row "+ (i+1));
					 document.exceptionForm.comments[i].focus();
					 return false;
				}
			 	if(checkExistingExceptionForEmp(empIdObj[i].value,year,month) == "false"){
			 		alert("Exception  for "+document.exceptionForm.empName[i].value+" already created for this month and year");
			 		document.exceptionForm.empCode1[i].focus();
     				return false;	
			 	}
			 }
	   	}
  	   	return true;
  	}
 
	function checkExistingExceptionForEmp(empCode,year,month){
		
		var action = "getExceptionForEmp";	
		var year = document.exceptionForm.financialYear.value;
		var month = document.exceptionForm.month.value;
		var url = "<%=request.getContextPath()%>"+"/commons/checkException.jsp?action=" +action+ "&empId="+empCode+ "&year="+year+ "&month="+month ;
		var isUnique;
		var req = initiateRequest();
		req.open("GET", url, false);
		req.send(null);	
	    if (req.status == 200){
			var payheads = req.responseText;
			var a = payheads.split("^");
			var codes = a[0];                   	
			if(codes=="true"){
				isUnique = "true";
			}
			else if(codes=="false"){
				isUnique = "false";
			}
	   	}

		return isUnique;
	 }
  
  	function callReasons(obj){
	  	var table = document.getElementById('paytable');
		var rCount=table.rows.length-1;
		if(table.rows.length == 2){
			for(var resLen=1;resLen<document.exceptionForm.reason.length;resLen++){
				document.exceptionForm.reason.options[resLen]=null;
			}
			var count = 1;
			<c:forEach var="exceptionObj" items="${exceptionMstrs}">			
				if(obj.value == "${exceptionObj.type}"){							
	 				document.exceptionForm.reason.options[count] = new 
					Option("${exceptionObj.reason}","${exceptionObj.id}");	
					count=count+1;
				}				
			</c:forEach>
			if(obj.value==""){			
				for(var resLen=document.exceptionForm.reason.length-1; resLen>0 ;resLen--){				
					document.exceptionForm.reason.remove(resLen);			
				}	
			}		
		}
		if(table.rows.length > 2){
			var rowObj=getRow(obj);
			var hit=0;
			var caught=0;
			var type = getControlInBranch(table.rows[rowObj.rowIndex],'type').value;		
			for(var resLen=1;resLen<getControlInBranch(table.rows[rowObj.rowIndex],'reason').length;resLen++){
				getControlInBranch(table.rows[rowObj.rowIndex],'reason').options[resLen]=null;
			}
			var count = 1;
			<c:forEach var="exceptionObj" items="${exceptionMstrs}">
				if(type == "${exceptionObj.type}"){					
	 				getControlInBranch(table.rows[rowObj.rowIndex],'reason').options[count] = new 
					Option("${exceptionObj.reason}","${exceptionObj.id}");	
					count=count+1;
				}				
			</c:forEach>
			if(type == ""){					
				for(var resLen=getControlInBranch(table.rows[rowObj.rowIndex],'reason').length; resLen>0 ;resLen--){				
					getControlInBranch(table.rows[rowObj.rowIndex],'reason').options[resLen]=null;			
				}	
			}	
		}
 	}	

	function checkEmpCode(){
		var tbl = document.getElementById('paytable');	
		var rCount=tbl.rows.length-1;

		if(tbl.rows.length == 2)
	  	{		  		
			if(document.exceptionForm.empCode1.value =="" || document.exceptionForm.empCode.value =="")
			{
				alert("Please select the Employe code!!!");
				document.exceptionForm.empCode1.focus();
				return false;
			}
	
			if(document.exceptionForm.action.value=="")
			{
				alert("Please Enter the Action for the Selected Employe!!!");
				document.salaryPaySlipForm.payHeadAmount.focus();
				return false;
			}
			if(document.exceptionForm.reason.value=="")
			{
				alert("Please Enter the Reason for the Selected Employe code!!!");
				document.exceptionForm.reason.focus();
				return false;
			}
		}
		else
		{
			if(tbl.rows.length>2)
			{
				if(document.exceptionForm.empCode1[rCount-1].value =="" || document.exceptionForm.empCode[rCount-1].value =="")
				{
					alert("Please select Employee code!!!");
					document.exceptionForm.empCode1[rCount-1].focus();
					return false;
				 }
				 if(document.exceptionForm.type[rCount-1].value=="")
				 {
					alert("Please Enter the Action for the Selected Employee!!!");
					document.exceptionForm.type[rCount-1].focus();
					return false;
				 }
				 if(document.exceptionForm.reason[rCount-1].value=="")
				 {
					alert("Please Enter the Reason for the Selected Employe!!!");
					document.exceptionForm.reason[rCount-1].focus();
					return false;
				 }
			 }
		 }
		 return true;
     }
   
	 function setFild(obj){  	
	 	var rowObj = getRow(obj);
	    var table = document.getElementById("paytable");
	    var tbl = table.rows.length;
		<c:forEach var="empObj" items="${employees}" >
		  if(getControlInBranch(table.rows[rowObj.rowIndex],'empCode').value=="${empObj.idPersonalInformation}"){
				getControlInBranch(table.rows[rowObj.rowIndex],'empName').value = "${empObj.employeeFirstName}";
				getControlInBranch(table.rows[rowObj.rowIndex],'srEntry').value = "${empObj.idPersonalInformation}";
		  }
		</c:forEach>
	 }

  	function checkDuplicate(obj){

	 	var rowObj=getRow(obj);
	 	var hit=0;
	 	var caught=0;
	 	var table= document.getElementById("paytable");
	 	var tbl = table.rows.length;
	 	var curRowIndex = rowObj.rowIndex-1;
	 
		if(obj.value=="")
	 	{
	 		document.exceptionForm.empCode[curRowIndex].value="";
			document.exceptionForm.empCode1[curRowIndex].value="";
			document.exceptionForm.empName[curRowIndex].value="";
	 		return false;
	 	}
		
		if(tbl>2)
		{
			for(var i=0;i<tbl;i++)
			{
				for(var j=i+1;j<tbl-1;j++)
				{
					if(i!=j)
					{
						if(document.exceptionForm.empCode[i].value==document.exceptionForm.empCode[j].value)
						{
							alert("Duplicate Selection of Employee code!!!");
							document.exceptionForm.empCode[curRowIndex].value="";
							document.exceptionForm.empCode1[curRowIndex].value="";
							document.exceptionForm.empName[curRowIndex].value="";
							document.exceptionForm.empCode1[curRowIndex].focus();
							return false;
						 }
					}
				}
			 }
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
	/*
	* This function returns absolue left and top position of the object
	*/
	function findPos(obj)
	{
		var curleft = curtop = 0;
		if (obj.offsetParent)
		{
			curleft = obj.offsetLeft;
			curtop = obj.offsetTop;
			while (obj = obj.offsetParent)
			{	//alert(obj.nodeName);
				curleft =curleft + obj.offsetLeft;
				curtop =curtop + obj.offsetTop; //alert(curtop);
			}
		}
		return [curleft,curtop];
	}
	
	var a = new Array();
	var i=0;
	function getRow(obj)
	 {
	 	if(!obj)return null;
	 	tag = obj.nodeName.toUpperCase(); 	
	 	while(tag != 'BODY'){
		 a[i]=tag.nodeName;
		 i++;
	 		if (tag == 'TR') return obj;
	 		obj=obj.parentNode;
	 		
	 		tag = obj.nodeName.toUpperCase();
	 	}
	 	return null;
	 }

	function getControlInBranch(obj,controlName)
	{
		if (!obj || !(obj.getAttribute)) return null;
		if (obj.getAttribute('name') == controlName) return obj;
	
		var children = obj.childNodes;
		var child;
		if (children && children.length > 0){
			for(var i=0; i<children.length; i++){
				child=this.getControlInBranch(children[i],controlName);
				if(child) return child;
			}
		}
		return null;
	}
 
 	function updateAndDisableFields()
 	{
 		<%
 		if(request.getAttribute("disciplinaryId")!=null)
 		{
 		%>
 			document.getElementById('disciplinaryId').value='<%=request.getAttribute("disciplinaryId").toString()%>';
 			if(document.getElementById('disciplinaryId').value!=null && document.getElementById('disciplinaryId').value!='')
 			{
 				document.getElementById('empCode').value='<%=disEmpDetails.get("disEmpId").toString()%>';
 				document.getElementById('empCode1').value='<%=disEmpDetails.get("disEmpCode").toString()%>';
 				document.getElementById('empName').value='<%=disEmpDetails.get("disEmpName").toString()%>';
 				document.getElementById('empCode1').disabled=true;
 				document.getElementById('empName').disabled=true;
			}
 		<%
 		}
 		%>
 	
 	}
 	

	var empCodeSelectionHandler = function(sType, arguments)
    { 
        var oData = arguments[2];
	 	var empDetails = oData[0];
	 	var empCode = empDetails.split(EMPCODE_SEP)[0];
	 	var empName = empDetails.split(EMPCODE_SEP)[1];
	 	
	 	document.getElementById("empCode").value = oData[1];
	 	document.getElementById("empCode1").value = empCode;
		document.getElementById("empName").value	=empName;
	 	empcode=empCode;
 	}
    var empCodeSelectionEnforceHandler = function(sType, arguments) {
      		warn('improperEmpCodeSelection');
  	}

   	function paramsFunction()
   	{ 
   		return "type=AllEmployeeCodes";
   	}
   	
   	
   	//Creates autocompletes for the Workflow Admin screen, To populate User, Workflow Type			
	var autoComplete = function () {
	    // Use an XHRDataSource
		var sUrl1 = "/commons/employeeSearch!getEmpListByEmpCodeLike";
		var oDS1 = new YAHOO.util.XHRDataSource(sUrl1);
	   
	     // Set the responseType
		oDS1.responseType =  YAHOO.util.XHRDataSource.TYPE_JSON;
	    // Define the schema of the delimited results
		oDS1.responseSchema = {
	        resultsList : "ResultSet.Result",
	        fields : ["value","key"]
	    };
	    // Enable caching
		oDS1.maxCacheEntries =  5;
	
	    // Instantiate the AutoComplete
		var oAC1 = new YAHOO.widget.AutoComplete("wfType", "docTypeAC", oDS1);
		oAC1.useShadow = true;
		oAC1.allowBrowserAutocomplete =false;
		return {oDS1:oDS1, oAC1:oAC1};
	}();
</script>
</head>

<body onload="waterMarkInitialize('createdOn','dd/mm/yyyy');updateAndDisableFields();">
  	<center>
		<html:form method="POST" action="exception/beforeException" >
			<div class="navibarshadowwk"></div>
			<div class="formmainbox">
				<div class="insidecontent">
					<div class="rbroundbox2">
						<div class="rbtop2">
							<div></div>
						</div>
						<div class="rbcontent2">
							<div class="datewk"><span class="bold">Today : </span><egovtags:now/></div>
							  	<table  width="100%" colspan="6" cellpadding ="0" cellspacing ="0" border = "0" id="employee">
									<tr>
								    	<td colspan="6"   class="headingwk">
								    	<div class="arrowiconwk"><img src="${pageContext.request.contextPath}/common/image/arrow.gif" /></div>
								    	<div class="headplacer">Generate Exception </div></td>
								    </tr>
	
									<tr>
										<td class="whiteboxwk" ><span class="mandatory">*</span>Year:</td>
										<td class="whitebox2wk" ><span class="greybox2wk">		
											<html:select property="financialYear">
												<html:option value="">-------Select----------</html:option>
												<c:forEach var="yearObj" items="${financialYears}">
												 <html:option value="${yearObj.id}">${yearObj.finYearRange}</html:option>
												</c:forEach>
											</html:select>
											</span>
										</td>
										<td class="whiteboxwk"><span class="mandatory">*</span>Month:</td>
										<td class="whitebox2wk"><span class="greybox2wk">
											<html:select property="month">
												<html:option value="">-----------Select------------</html:option>
												<html:option value="1">Jan</html:option>
												<html:option value="2">Feb</html:option>				
												<html:option value="3">March</html:option>
												<html:option value="4">April</html:option>
												<html:option value="5">May</html:option>
												<html:option value="6">June</html:option>
												<html:option value="7">July</html:option>
												<html:option value="8">Aug</html:option>
												<html:option value="9">Sept</html:option>
												<html:option value="10">Oct</html:option>
												<html:option value="11">Nov</html:option>
												<html:option value="12">Dec</html:option>
											</html:select>
											</span>
										</td>		
									</tr>
									<tr>
										<td class="greyboxwk"> <span class="mandatory">*</span>Created On:</td>
										<td class="greybox2wk">
											<input type="text" name="createdOn" maxlength="10" id="createdOn" class="datefieldinput" size="20" onkeyup="DateFormat(this,this.value,event,false,'3')" 
											onFocus="waterMarkTextIn('createdOn','dd/mm/yyyy');" onblur="waterMarkTextOut('createdOn','dd/mm/yyyy');"  />
										
											<a href="javascript:show_calendar('exceptionForm.createdOn');"
												onmouseover="window.status='Date Picker';return true;" 
											onmouseout="window.status='';return true;"><img src="<c:url value='../common/image/calendar.png'  />" onclick="document.getElementById('createdOn').style.color='black';" border="0"></a>
										</td>
										<td class="greyboxwk" colspan="2"><input type="hidden" name="disciplinaryId" id="disciplinaryId" value=""></td>
									</tr>
									<tr>
									<td colspan="4">	
									 	<table  width="100%" border="1" cellpadding="0" cellspacing="0" colspan="6"   id="paytable" class="tablebottom">
									 
										 <tr>
										      <td class="tablesubheadwk" width="20%" ><span class="mandatory">*</span>Code</td>
										      <td class="tablesubheadwk" width="10%">Name</td>
										      <td class="tablesubheadwk" width="10%"><span class="mandatory">*</span>Action</td>
										      <td class="tablesubheadwk" width="10%"><span class="mandatory">*</span>Reason</td>
										      <td class="tablesubheadwk" width="10%"><span class="mandatory">*</span>Comments</td>
										      <td class="tablesubheadwk" width="10%">SR Entry</td>
										      <td class="tablesubheadwk" width="10%">Add/ Del</td>
										            
										  </tr>
									
									   	  <tr id="earnings">
									
									   	 	<td class="whitebox3wk"  valign="top" >	
									   	 		<input type="hidden" class="fieldcell" name ="empCode" id="empCode" />		
												<div class="yui-skin-sam" >
									    			<div id="empSearch_autocomplete" width="100%"> 
									    				<div >
									    	    			<input type="text"  class="selectwk" name="empCode1" id="empCode1" size="10"  onblur="checkDuplicate(this);trim(this,this.value)"/>		    	    
									    				</div>
									   	    			<span id="empCodeSearchResults"></span>
									    			</div>
									    		</div>
									
												<egovtags:autocomplete name="empCode1"  field="empCode1" 
									   	    		url="${pageContext.request.contextPath}/common/employeeSearch!getEmpListByEmpCodeLike.action" queryQuestionMark="true" paramsFunction="paramsFunction" results="empCodeSearchResults" 
									   	    		handler="empCodeSelectionHandler" forceSelectionHandler="empCodeSelectionEnforceHandler"/>
									   	    	<span class='warning' id="improperempCodeSelectionWarning"></span>
									         </td>
									   	 	<td class="whitebox3wk" >
									   	 		<html:text styleClass="fieldcell" property="empName" styleId="empName" style="width:120px" readonly="true"/>
									  	 	</td>
									   	 	<td class="whitebox3wk">
									  			<html:select styleClass="fieldcell" property="type" style="width:120px;" styleId="type" onchange="callReasons(this);">
									       		<html:option value="">------------Select-------------</html:option>
												<c:forEach var="actionObj" items="${exceptionTypes}" >
									 			<html:option value="${actionObj}">${actionObj}</html:option>
												</c:forEach>
									   	    	</html:select>
									   	 	</td>
									   	 	<td class="whitebox3wk">
									  			<html:select styleClass="fieldcell" property="reason" style="width:120px" styleId="reason">
									       		<html:option value="">------------Select-------------</html:option>
									   	    	</html:select>
									  	 	</td>
									   	 	<td class="whitebox3wk">
									      		<html:text property="comments" style="width:100px" />
									     	</td>
									     	<td class="whitebox3wk">
									      		<input type="checkbox" name="srEntry" >
									     	</td>
									      	<td class="whitebox3wk"><div align="center"><a href="#"><img src="${pageContext.request.contextPath}/common/image/add.png" alt="Add" width="16" height="16" border="0" onclick="addExceptionRow('paytable','earnings');"/></a> 
									      	<a href="#"><img src="${pageContext.request.contextPath}/common/image/cancel.png" alt="Del" width="16" height="16" border="0" onclick="deleteSpecificRow(this,'paytable');"/></a></div></td><!-- deleteExceptionRow('paytable','earnings'); -->
									     
									  	</tr>
							 		</table>
 								</td>
 							</tr>
		 					<tr>
								<td class="shadowwk" colspan="4"/>
							</tr>
		
							<tr>
								<td colspan="4">
									<div class="mandatory" align="right">* Mandatory Fields</div>
								</td>
							</tr>
   
       						<c:if test="${exceptionWf=='Manual'}">
							<%@ include file="/payslip/manualWfApproverSelection.jsp" %>
							</c:if>
   
  
							<tr>
								<td colspan="4" > 
									<div class="buttonholderwk">
										<html:submit  onclick="return validation('save');" styleClass="buttonfinal"><bean:message key="create"/></html:submit>
										<html:button property="cancel" onclick="history.go(0)" styleClass="buttonfinal"><bean:message key="cancel"/></html:button>
									</div>
								</td>
							</tr>
						</table>
					</div>
				<div class="rbbot2">
					<div></div>
				</div>
			</div>
		</div>
	</div>
	<div class="buttonholderwk">
		<html:button property="close"  styleId="button"  styleClass="buttonfinal"  onclick="window.close()"  ><bean:message key="close"/></html:button>
	</div>
	<div class="urlwk">City Administration System Designed and Implemented by 
	<a href="http://www.egovernments.org/">eGovernments Foundation</a> All Rights Reserved 
	</div>
</html:form>
</center>
</body>
</html>
