<%@ page import="java.util.*,
	java.text.*,
	org.egov.lib.rjbac.jurisdiction.*,org.egov.lib.rjbac.user.*,
	org.egov.lib.rjbac.jurisdiction.dao.*"%>

<%@include file="/includes/taglibs.jsp" %>
<%
	Set jurObjSet = new HashSet();
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	
	if(request.getAttribute("jurObj1")!=null) {
		jurObjSet = (Set)request.getAttribute("jurObj1");
	}
	
	String userIdStr="";
	User user = null;
	
	if(request.getAttribute("user")!=null) {
		user = (User)request.getAttribute("user");
		userIdStr = (String)user.getId().toString();
	}
%>

<html>
	<head>
		<title>Jurisdiction</title>
		<script>
			function getRow(obj) {
				if(!obj){
					return null;
				}
				tag = obj.nodeName.toUpperCase();
				while(tag != 'BODY'){
					if (tag == 'TR'){ 
						return obj;
					}
					obj=obj.parentNode;
					tag = obj.nodeName.toUpperCase();
				}
				return null;
			}
			
			function getControlInBranch(obj,controlName) {
				if (!obj || !(obj.getAttribute)) {
					return null;
				}
				// check if the object itself has the name
				if (obj.getAttribute('name') == controlName) {
					return obj;
				}
				// try its children
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

			var idValue = ""; var nameValue = ""; var descValue = "";
			var objct = null;
			//All you need to do his to pass the category name which you have mentioned in the applname-config.xml.
			function callGenericScreenDept(obj) {
				//Need to pass the application config file name
				var xmlconfigname = "egov_config.xml";
				var categoryname = "SelectBndry";
				// Opens the Generic Search Screen. We need to send the name of the Category (we have to write a category in the applname-config.xml file)
				if(document.all) {
					window.showModalDialog("/egi/commonyui/egov/genericScreenWithLeaf.jsp?xmlconfigname="+xmlconfigname+"&categoryname="+categoryname,window,'dialogHeight:500px;dialogWidth:600px');
				} else {
					window.open("/egi/commonyui/egov/genericScreenWithLeaf.jsp?xmlconfigname="+xmlconfigname+"&categoryname="+categoryname,'_blank',"height=500,width=600px,scrollbars=yes,left=30,top=30,status=yes");
				}
				objct = obj;
			}

			function assignValues(obj){
				var rowObj=getRow(objct);
				var table= document.getElementById("reason_table");
				getControlInBranch(table.rows[rowObj.rowIndex],'bndryValue').value = nameValue;
				getControlInBranch(table.rows[rowObj.rowIndex],'bndryID').value= idValue;
				getControlInBranch(table.rows[rowObj.rowIndex],'bndryTypeID').value= descValue;
				nameValue="";idValue="", descValue = "";
				objct=null;
			} 
			
			function addJur() {
				var correctData =checkData();
				if(correctData){
					var tbl = document.getElementById('reason_table');
					var tbody=tbl.tBodies[0];
					var lastRow = tbl.rows.length;
					var rowObj = document.getElementById('reasonrow').cloneNode(true);
					tbody.appendChild(rowObj);
					document.forms[0].bndryValue[lastRow].value="";
					document.forms[0].fromDate1[lastRow].value="";
					document.forms[0].fromDate1[lastRow].disabled=false;
					document.forms[0].toDate1[lastRow].value="";
					document.forms[0].toDate1[lastRow].disabled=false;
					document.forms[0].modifyJur[lastRow].style.visibility="hidden";
					document.forms[0].boundaryTreeMenu[lastRow].style.visibility="visible";
					document.forms[0].delRow[lastRow].disabled=false;
				}
			}
			
			var noDateOverLap= true;
			function checkjurisdictionDates(fDate,tDate,Id){
				var url = "/egi/commons/Process.jsp?type=checkJurisdictionDates&fDate="+fDate+"&tDate="+tDate+"&Id="+Id;
				var req2 = initiateRequest();
				req2.open("GET", url, false);
				req2.send(null);
				if (req2.status == 200) {
					var result=req2.responseText;
					result = result.split("/");
					if(result[0]!= null && result[0]!= ""){
						if(noDateOverLap == true) {
							noDateOverLap = eval(result[0]);
						}						
					}
				}
				return noDateOverLap;
			}
			
			function validate(obj){
				var rowObj=getRow(obj);
				var table = document.getElementById('reason_table');
				var len = table.rows.length;
				if(len>0) {
					for(var i=0;i<len;i++) {
						if(getControlInBranch(table.rows[rowObj.rowIndex],'modifyJur').checked)	{
							getControlInBranch(table.rows[rowObj.rowIndex],'fromDate1').disabled=false;
							getControlInBranch(table.rows[rowObj.rowIndex],'toDate1').disabled=false;
							getControlInBranch(table.rows[rowObj.rowIndex],'delRow').disabled=false;
						} else {
							getControlInBranch(table.rows[rowObj.rowIndex],'fromDate1').disabled=true;
							getControlInBranch(table.rows[rowObj.rowIndex],'toDate1').disabled=true;
							getControlInBranch(table.rows[rowObj.rowIndex],'delRow').disabled=true;
						}
	   				}
				} else {
					if(document.forms[0].modifyJur.checked) {
						document.forms[0].fromDate1.disabled=false;
						document.forms[0].toDate1.disabled=false;
					} else {
						document.forms[0].fromDate1.disabled=true;
						document.forms[0].toDate1.disabled=true;
					}
				}

			}
			
			function checkData() {
				var table = document.getElementById('reason_table');
				for(var i=0;i<table.rows.length;i++) {
					if(getControlInBranch(table.rows[i],'fromDate1').value==""){
			 			alert("Please enter From Date");
			 			getControlInBranch(table.rows[i],'fromDate1').focus();
			 			return false;
					}
					if(getControlInBranch(table.rows[i],'bndryValue').value==""){
			 			alert("Please select a Boundary Value");
			 			getControlInBranch(table.rows[i],'bndryValue').focus();
			 			return false;
					}
					if(getControlInBranch(table.rows[i],'fromDate1').value!="" && getControlInBranch(table.rows[i],'toDate1').value!="") {
						rTF=checkFdateTdate(getControlInBranch(table.rows[i],'fromDate1').value,getControlInBranch(table.rows[i],'toDate1').value);
						if(rTF==false) {
							alert('From date should be less than or equal to To Date');
							getControlInBranch(table.rows[i],'fromDate1').focus();
							return false;
						}
					}
					if(getControlInBranch(table.rows[i],'fromDate1').value!="") {
						if(checkjurisdictionDates(getControlInBranch(table.rows[i],'fromDate1').value,getControlInBranch(table.rows[i],'toDate1').value,getControlInBranch(table.rows[i],'bndryID').value) == false) {
							alert(getControlInBranch(table.rows[i],'bndryValue').value +" is not valid in this Jurisdiction Dates");
							return false;
		 				}
					}
					if(getControlInBranch(table.rows[i],'modifyJur').checked) {
						getControlInBranch(table.rows[i],'selCheck').value="yes";
					} else {
						getControlInBranch(table.rows[i],'selCheck').value="no";
					}
				}
				return true;
			}
			
			function goTo() {
				var correctData =checkData();
				if(correctData){
					var len = document.forms[0].fromDate1.length;
					if(len>0){
						for(var i=0;i<len;i++){
							document.forms[0].fromDate1[i].disabled=false;
							document.forms[0].toDate1[i].disabled=false;
							for(var j=i+1;j<len;j++) {
								if(i!=j) {
									if(document.forms[0].bndryValue[i].value==document.forms[0].bndryValue[j].value) {
										alert("Duplicate selection of Boundary Value...!");
										document.forms[0].bndryValue[j].focus();
										return false;
				 					}
								}
							}
						}
					}
					<c:set var="userid">
					<%=user.getId()%>
					</c:set>
					var submitType="saveJurisdiction";
					document.forms[0].action = "<c:url value='/CreateJurisdiction.do?submitType="+submitType+"&userid=${userid}'/>";
					document.forms[0].submit();
				}
			}

			function deleteJur(obj) {
				var tbl=document.getElementById('reason_table');
				var rowNumber=getRow(obj).rowIndex;
				var rowo=tbl.rows.length;
				if(rowo<=1){
					alert("This Row can not be deleted,Atleast One Row Should be there");
					return false;
				} else {
					tbl.deleteRow(rowNumber)
					return true;
				}
			}
			
			//Check whether the values entered in the date fields are in dd/MM/yyyy format and whether the dd, MM and yyyy values are valid
			function validateDateFormat(obj) {
 				var dtStr=obj.value;
 				var year;
 				var day;
 				var month;
 				var leap=0;
 				var valid=true;
 				var oth_valid=true;
 				var feb=false;
 				var validDate=true;

    			if(dtStr!="" && dtStr!=null){
    				year=dtStr.substr(6,4);
    				month=dtStr.substr(3,2);
    				day=dtStr.substr(0,2);

    				if(year=="0000" || year<1900 || month=="00" || day=="00" || dtStr.length<10) {
    					validDate=false;
    				}
    				if(validDate)	{
    					leap=year%4;
 						if(leap==0) {
    						if(month=="2"){
    							valid=false;
    							feb=true;
    						} else if(month=="02") {
    							if(day>29) {
    								valid=false;
    							}
    							feb=true;
    						}
    					} else {
    						if(month=="2") {
    							valid=false;
    							feb=true;
    						} else if(month=="02") {
    							if(day>28) {
    								valid=false;
    							}
    							feb=true;
    						}
    					}

    					if(!feb) {
    						if(month=="03" || month=="01" || month=="05" || month=="07" || month=="08" || month=="10" || month=="12") {
    							if(day>31) {
    								oth_valid=false;
    							}
    						} else if(month=="04" || month=="06" || month=="09" || month=="11"){
    							if(day>30) {
    								oth_valid=false;
    							}
    						}else {
    							oth_valid=false;
    						}
						}
    				}
    			}

    			if(!valid || !oth_valid || !validDate){
    				alert("Please enter the valid date in the dd/MM/yyyy Format only");
    				obj.value="";
    				obj.focus();
    			}
    			return;
			}
			
			function checkFdateTdate(fromDate1,toDate1) {
				if(fromDate1.substr(6,4) > toDate1.substr(6,4)){
					return false;
				} else if(fromDate1.substr(6,4) == toDate1.substr(6,4)){
					if(fromDate1.substr(3,2) > toDate1.substr(3,2)){
						return false;
					} else if(fromDate1.substr(3,2) == toDate1.substr(3,2)) {
						if(fromDate1.substr(0,2) > toDate1.substr(0,2)){
							return false;
						} else {
							return true;
						}
					} else {
						return true;
					}
				} else {
					return true;
				}
			}
		</script>
	</head>
	<body>
		<html:form method="post" action="/CreateJurisdiction.do?submitType=saveJurisdiction">
			<table align="center" class="tableStyle">
				<tr>
					<td  align="middle" colSpan="2" width="100%"/>				
				</tr>
			</table>
			
			<table  border="1" width="810"  class="tableStyle">
				<tr>
					<td class="tableheader" vAlign="center" align="middle" width="100%" colSpan="6" height="30">
						<p align="center" ><b>User Jurisdiction</b>
					</td>
				</tr>
				<tr>
					<td  align="middle"  ><p align="right" class="labelcell"><b>User Name</b></td>
					<td class="labelcell" align="middle" width="47%" height="56"><p  >
						<%if(user!=null)
						{%>
						<b><%=user.getUserName()%></b>
						<%}
						else
						{%>
						<b>&nbsp;</b>
						<%}%>
					</td>
				</tr>
			</table>		
			
			<table  border="1" width="810"   class="tableStyle">
				<tr >
					<td class="labelcell" vAlign="bottom" align="left" width="50%" colSpan="6" height="30">
						<p align="left" ><b>Boundary Type Values</b>
						<input class="button2" type=button name ="addRow" value="Add Row" onClick="addJur();"/>
					</td>
				</tr>
				<tr >
					<td  vAlign="bottom" align="center" width="7%" height="20">
						<b><font size="2">Modify</font></b>
					</td>
					<td class="labelcellforsingletd" vAlign="bottom" align="left" Style="width:300px" height="20">
						<b><font size="2">Boundary Type Value</font></b>
					</td>
					<td class="labelcell" vAlign="bottom" align="left" Style="width:250px" height="20">
						<b><font size="2">From Date(dd/mm/yyyy)</font></b>
					</td>
					<td class="labelcell" vAlign="bottom" align="left" Style="width:250px"  height="20">
						<b><font size="2">To Date(dd/mm/yyyy)</font></b>
					</td>
					<td class="labelcell" vAlign="bottom" align="left" width="20%" height="20">
						<b><font size="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Delete</font></b>
					</td>
				</tr>
			</table>
			
			<table  border="1" width="810"  id="reason_table"  class="tableStyle">
				<%
				if(jurObjSet!=null && !jurObjSet.isEmpty()){
						for (Iterator iter = jurObjSet.iterator(); iter.hasNext();) {
							JurisdictionValues jur = (JurisdictionValues)iter.next();
				%>
				<tr  id="reasonrow" >
					<td  vAlign="middle" align="center" width="7%" height="20">
						<input type="checkbox" name="modifyJur" id="modifyJur" value="ON" onclick="validate(this)">
						<input type="hidden" name="selCheck" id="selCheck">
					</td>
					<td class="labelcellforsingletd" Style="width:220px" vAlign="bottom" align="left" width="33%" height="20">
						<html:hidden property="bndryTypeID" value="<%=jur.getBoundary().getBoundaryType().getId().toString()%>"/>
						<html:hidden property="bndryID" value="<%=jur.getBoundary().getId().toString()%>"/>
						<html:text property="bndryValue" size="32" value="<%=jur.getBoundary().getName().toString()%>" readonly="true"/>&nbsp; <input type="button" class="button2" value="..." id="boundaryTreeMenu" onclick="callGenericScreenDept(this);" style="visibility:hidden">	
					</td>
					<td class="labelcell" vAlign="bottom" Style="width:200px" align="left" width="20%" height="20">
						<b><html:text property="fromDate1"  value="<%=formatter.format(jur.getFromDate())%>"  disabled="true" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3');" onblur="return validateDateFormat(this);"/>
					</td>
					<%if(jur.getToDate()!=null){%>
					<td class="labelcell" Style="width:163px" vAlign="bottom" align="left" width="20%"  height="20">
						<b><html:text property="toDate1"  value="<%=formatter.format(jur.getToDate())%>"  size="19"  disabled="true" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="return validateDateFormat(this)"/>
					</td>
					<%}
					else
					{%>	
					<td class="labelcell" Style="width:163px" vAlign="bottom" align="left" width="20%"  height="20">
						<b><html:text property="toDate1"  value=""  size="19" onfocus="javascript:vDateType='3'"  disabled="true" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="return validateDateFormat(this)"/>
					</td>
					<%}%>
					<td  vAlign="middle" align="left"  height="10">
						<p align="center">&nbsp;<input type=button name ="delRow" class="button2" value="Delete Row" disabled=true onclick="deleteJur(this)"/>
					</td>
				</tr>
				<%}
				} else {%>
				<tr  id="reasonrow" >
					<td  vAlign="bottom" align="left" width="7%" height="20"><input type="checkbox" name="modifyJur" id="modifyJur" value="ON" style="visibility:hidden" onclick="validate(this)">
						<input type="hidden" name="selCheck" id="selCheck">
					</td>
					<td class="labelcellforbg" vAlign="bottom" align="left" Style="width:220px" height="20">
						<html:hidden property="bndryTypeID"/>
						<html:hidden property="bndryID" />
						<html:text property="bndryValue" size="32"  readonly="true"/>&nbsp; <input class="button2" type="button" value="..." id="boundaryTreeMenu" onclick="callGenericScreenDept(this);">	
					</td>
					<td class="labelcell" Style="width:200px" vAlign="bottom" align="left" width="20%" height="20">
						<b><html:text property="fromDate1" size="26" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3');" onblur="return validateDateFormat(this);"/>
					</td>
					<td class="labelcell"  Style="width:163px" vAlign="bottom" align="left" width="20%" colSpan="2" height="20">
						<b><html:text property="toDate1" size="26" onfocus="javascript:vDateType='3'" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="return validateDateFormat(this)"/>&nbsp;
					</td>
					<td  vAlign="middle" align="left"  height="10">
						<p align="center">&nbsp;<input type=button class="button2" name ="delRow" value="Delete Row" onclick="deleteJur(this)"/>
					</td>
				</tr>
				<%}%>
			</table>
 			<table align="center" class="tableStyle">
				<tr >
					<td vAlign="bottom" align="left" width="20%" colSpan="6" height="23">
						<p align="center">
						<input type="button" class="button2"  value="Save" onclick="goTo()">
					</td>
				</tr>
			</table>
		</html:form>
	</body>
</html>