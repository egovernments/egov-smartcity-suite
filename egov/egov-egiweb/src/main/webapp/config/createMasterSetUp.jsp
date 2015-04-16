<%@ include file="/includes/taglibs.jsp"%>
<%@ page
	import="java.util.*,
	org.egov.infstr.utils.HibernateUtil,org.egov.exceptions.EGOVRuntimeException,
	org.apache.log4j.Logger,
	java.text.SimpleDateFormat,
	org.egov.infstr.config.*"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Master Setup</title>

		<script type="text/javascript" src="../javascript/dateValidation.js" type="text/javascript"></script>

		<script language="JavaScript" type="text/JavaScript">
			function validateOnSubmit(){
			   <%	
				AppConfig appObj= new AppConfig();
				String keyName = "";	
				List appConfig= null;
				appConfig  = (List)request.getAttribute("appDataKey");
				for(Iterator it =appConfig.iterator();it.hasNext();) {
				  appObj = (AppConfig)it.next();
				  keyName = appObj.getKeyName();	
			   	%>
				
				var name = '<%=keyName%>';	 
				var len = document.getElementsByName('values').length;			
				if(len==1){			
					if(document.getElementById('values').value ==null || document.getElementById('values').value==""){
					    alert('please enter the value');
					    document.getElementById('values').focus();
					    return false;
					}
					if(document.getElementById('effectiveFrom').value ==null){
						alert('please enter the date');
						document.getElementById('effectiveFrom').focus();
						return false;
					}
				} else {
					for(var j=0;j<=len-1;j++){
					    if(document.getElementsByName('values')[j].value == "") {
							alert('please enter the  value');
							document.getElementsByName('values')[j].focus();
							return false;
						} 
						
						if(document.getElementsByName('effectiveFrom')[j].value =="") {
				      		alert('please enter the  date');
				    		document.getElementsByName('effectiveFrom')[j].focus();
				    		return false;
				    	}
					}
				}  			 
			 <%
			 }
		 	%>	
			
			document.forms[0].action = "${pageContext.request.contextPath}/config/MasterSetUpAction.do?submitType=createNewValues";
			document.forms[0].submit();
			alert('Key value created successfully');
		}
		
		function checkUnique(obj,key){
			var len = document.getElementsByName('count').length;		
			for(var i=0; i<len; i++){
				for(var j=i+1; j<len; j++){				
					if(i != j){
						if(key == document.getElementsByName('count')[i].value && key == document.getElementsByName('count')[j].value){
							if(document.getElementsByName('effectiveFrom')[j].value == document.getElementsByName('effectiveFrom')[i].value){
								alert("Data already there for this key on this date");
								document.getElementsByName('effectiveFrom')[j].value = "";
								document.getElementsByName('effectiveFrom')[j].focus;
							}
						}
					}
				}
			}	
		}
	
		function deleteRow(table,obj){
	    	var tbl = document.getElementById(table);
	    	var rowNumber=getRow(obj).rowIndex;		 
			if(tbl.id !='BankTable'){
			  if(rowNumber >1 && getControlInBranch(tbl.rows[rowNumber],'keyId').value == ""){
			  	tbl.deleteRow(rowNumber);
			  }
			} else {
		  		if(rowNumber >2){
		   			tbl.deleteRow(rowNumber);
		  		}
			}
		}
	
		function whichButtonMaster(tbl,obj,objr) {
	   		if(objr=="config")
	    		addRowToTable(tbl,obj);
	   		if( objr=="bank"){   
	    		addRowToTable(tbl,obj);
	   		}
	   		
	   		if( objr=="configVal"  )
	    		addRowToTable(tbl,obj);
		}
	
		function addRowToTable(tbl,obj){
			<%		
			AppConfig apObj= new AppConfig();
			String key = "";
			List appConfigList = null;
			appConfigList  = (List)request.getAttribute("appDataKey");
			for(Iterator it = appConfigList.iterator();it.hasNext();) {
			  apObj = (AppConfig)it.next();
			  key = apObj.getKeyName();
			%>
			var name = '<%=key%>';
			if(tbl==name) {
				tableObj=document.getElementById(tbl);	     
			    var rowObj1=getRow(obj);
			    var tbody=tableObj.tBodies[0];
				var lastRow = tableObj.rows.length;	   
			   	var rowObj = tableObj.rows[lastRow-1].cloneNode(true);
			    tbody.appendChild(rowObj);
			    var rownum=rowObj1.rowIndex+1;  
			    getControlInBranch(tableObj.rows[rownum],'values').readOnly=false;
		   	   	getControlInBranch(tableObj.rows[rownum],'effectiveFrom').readOnly=false;
		   	   	getControlInBranch(tableObj.rows[rownum],'values').value="";
		   	   	getControlInBranch(tableObj.rows[rownum],'effectiveFrom').value="";
		   	   	getControlInBranch(tableObj.rows[rownum],'keyId').value="";
		  	}
		  	<%
		  	}
		 	%>
		 	
		 	if(tbl=='BankTable' ) {
			   tableObj=document.getElementById(tbl);
			   var rowObj1=getRow(obj);
			   var tbody=tableObj.tBodies[0];
			   var lastRow = tableObj.rows.length;
			   var rowObj = tableObj.rows[lastRow-1].cloneNode(true);
			   tbody.appendChild(rowObj);
			   var rownum=rowObj1.rowIndex+1;  
		       getControlInBranch(tableObj.rows[rownum],'bank').disabled=false;
			   getControlInBranch(tableObj.rows[rownum],'bankBranch').disabled=false;
			   getControlInBranch(tableObj.rows[rownum],'bankAccount').disabled=false;
		  	   getControlInBranch(tableObj.rows[rownum],'bankEffectiveFrom').disabled=false;
		  	   
		  	   getControlInBranch(tableObj.rows[rownum],'bank').value="";
		  	   getControlInBranch(tableObj.rows[rownum],'bankBranch').value="";
		  	   getControlInBranch(tableObj.rows[rownum],'bankAccount').value="";
		  	   getControlInBranch(tableObj.rows[rownum],'bankEffectiveFrom').value="";
		 	}
		}
	</script>

	</head>
	<body>
		<html:form action="/config/MasterSetUpAction.do?submitType=createNewValues">
			<p align="center">
				<font color="#0000A0">Master SetUp Screen for ${masterDataForm.moduleName[0]}</font>
			</p>
			<input type="hidden" name="moduleName" value="${masterDataForm.moduleName[0]}" />
			<table width=90% cellpadding="0" cellspacing="0" border="0" id="mainTable" name="mainTable">
				<c:forEach var="appConfig" items="${appDataKey}">
					<input type="hidden" name="keyName" id="keyName" value="${appConfig.keyName} " />
					<tr>
						<td class="labelcell" colspan="5" height="5px">
					</tr>
					<tr id="keyRowId">
						<td class="fieldcell">
							<label for="description">
								${appConfig.description}
							</label>
						</td>
					</tr>
					<td colspan="7">
						<table id="${appConfig.keyName}" align="left" class="eGovInnerTable" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td class="labelcell" colspan="5" height="5px">
							</tr>
							<tr>
								<td class="labelcell">
									Value
								</td>
								<td class="labelcell">
									Effective From
								</td>
							</tr>
							<c:choose>
								<c:when
									test="${appConfig.appDataValues  != null && fn:length(appConfig.appDataValues) > 0}">
									<c:forEach var="configValues"
										items="${appConfig.appDataValues}">
										<tr>
											<td class="labelcell">
												<input type=hidden name="count" id="count" value="${appConfig.keyName}" />
												<input type=hidden name="keyId" id="keyId"
													value="${configValues.id}" />
												<input type="text" id="values" name="values" value="${configValues.value}" readOnly>
											</td>
											<td class="labelcell">
												<input type="text" name="effectiveFrom" id="effectiveFrom"
													value="<fmt:formatDate  type="date" value="${configValues.effectiveFrom}"  pattern="dd/MM/yyyy"/>"
													onkeyup="DateFormat(this,this.value,event,false,'3')"
													onblur="validateDateFormat(this);"
													readOnly />
											</td>
											<td>
												<input onclick="deleteRow('${appConfig.keyName}',this);"
													style="WIDTH: 20px; HEIGHT: 22px" type="button" value="-"
													name="delF">
											</td>
											<td>
												<input
													onclick="whichButtonMaster('${appConfig.keyName}',this,'configVal');"
													style="WIDTH: 20px; HEIGHT: 22px" type="button" value="+"
													name="addF">
											</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td class="labelcell">
											<input type=hidden name="count" id="count"
												value="${appConfig.keyName}" />
											<input type=hidden name="keyId" id="keyId" value="" />
											<input type="text" id="values" name="values">
										</td>
										<td class="labelcell">
											<input type="text" name="effectiveFrom" id="effectiveFrom"
												onkeyup="DateFormat(this,this.value,event,false,'3')"
												onblur="validateDateFormat(this);" />
										</td>
										<td>
											<input onclick="deleteRow('${appConfig.keyName}',this);"
												style="WIDTH: 20px; HEIGHT: 22px" type="button" value="-"
												name="delF">
										</td>
										<td>
											<input
												onclick="whichButtonMaster('${appConfig.keyName}',this,'config');"
												style="WIDTH: 20px; HEIGHT: 22px" type="button" value="+"
												name="addF">
										</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</table>
				</c:forEach>

				<tr>
					<td colspan="10" height="100px">
						<table id="submit" WIDTH="80%" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td WIDTH="30%" align="center">
									<html:button  value="   Save   " property="b4" onclick="validateOnSubmit();" />&nbsp;&nbsp;
									<html:button  value="   Back   " property="b4" onclick="javascript:	window.history.back()" />
								</td>
							</tr>
						</table>
					</td>
				</tr>

			</table>
		</html:form>
	</body>
</html>