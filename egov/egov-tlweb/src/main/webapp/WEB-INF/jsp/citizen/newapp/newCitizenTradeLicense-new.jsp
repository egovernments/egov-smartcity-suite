<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>
<%@ include file="/includes/taglibs.jsp"%>
<html>
	<head>
		<title><s:text name="page.title.newtrade" /></title>
		<sx:head />
		<script>
	
			function validateForm(obj) {
			clearWaterMark();
    			if(validateForm_newTradeLicense()==false) {
    				return false;
    			} else {
      				return true;
    			}
  			}
  
			function clearWaterMark(){
				if(document.getElementById('applicationDate') && document.getElementById('applicationDate').value=='dd/mm/yyyy') {
					document.getElementById('applicationDate').value = '';
				}				
			}
			
			function closethis() {
			   if(confirm("Do you want to close this window ?")) {
			       window.close();
			   }			   
			}
				
			function onBodyLoad(){
  				if (document.getElementById("motorInstalled").checked==true) {
					document.getElementById("hpheader").style.display='';
				} else {
					document.getElementById("hpheader").style.display='none';
				}
			}

			var motorcnt = 0;
			var rowcnt = 1; // special counter to keep track of the number of Rows for id & name values
			var labelCnt = 1; // special counter to keep track of the number of Rows for id & name values
					
			function totalHP() { 
				var tot = 0;
				for( var i=0; i<=motorcnt ;i++) {			  		
			  		if(document.getElementById('installedMotorList['+i+'].hp')!=null && document.getElementById('installedMotorList['+i+'].noOfMachines')) {
			 			chkDecimal(document.getElementById('installedMotorList['+i+'].hp'));
			  			chkNum(document.getElementById('installedMotorList['+i+'].noOfMachines'));
			  			if(document.getElementById('installedMotorList['+i+'].hp') && document.getElementById('installedMotorList['+i+'].hp').value != '' && document.getElementById('installedMotorList['+i+'].noOfMachines') && document.getElementById('installedMotorList['+i+'].noOfMachines').value != '') {
			  				tot +=parseFloat(document.getElementById('installedMotorList['+i+'].hp').value)*parseInt(document.getElementById('installedMotorList['+i+'].noOfMachines').value);
			  	 		}
			  		}
			  	}
			  	document.getElementById("totalHP").value = tot;
			}
	
			function showhide(id) {
				if (document.getElementById("motorInstalled").checked) {
					if(motorcnt<1){
						addMotorRowToTable(false);
					}
					document.getElementById('tb2Create').style.display='';
					document.getElementById("addmoremotor").style.display='';
					document.getElementById("hpheader").style.display='';
					document.getElementById('totalHP').value=0;
				} else {
					for( var i=0; i<=motorcnt ;i++){
						if(document.getElementById('installedMotorList['+i+'].noOfMachines') && document.getElementById('installedMotorList['+i+'].hp')){
							document.getElementById('installedMotorList['+i+'].noOfMachines').style.display='none';
							document.getElementById('installedMotorList['+i+'].noOfMachines').value='';
							document.getElementById('installedMotorList['+i+'].hp').value='';
							document.getElementById('installedMotorList['+i+'].hp').style.display='none';
							document.getElementById('addImg'+i).style.display='none';
							document.getElementById('delImg'+i).style.display='none';
							document.getElementById('totalHP').value=0;
						}
					}
					addMotorRowToTable(false);
					document.getElementById('totalHP').value=0;
					document.getElementById('tb2Create').style.display='none';
					document.getElementById("totalHP").value="";
					document.getElementById("addmoremotor").style.display='none';
					document.getElementById("hpheader").style.display='none';
				}
			}
			
			function detailchange(){
				document.getElementById("detailChanged").value = 'true';
			}
	
			function addMotorRowToTable(populateslab,key,motorhpvalue,machines){
				if(!populateslab){
					detailchange();
				}
	
				if (document.getElementById('tb2Create').style.display=='none') {
				  	document.getElementById('tb2Create').style.display='';
				}
				  
			  	var browser=navigator.appName;
			  	var tbl = document.getElementById('tb2Create');
			  	var lastRow = tbl.rows.length;
			  	// if there's no header row in the table, then iteration = lastRow + 1
				if (lastRow==1) {
				  	var row = tbl.insertRow(lastRow);
					var labelnoofmac;
					var cellRight = row.insertCell(0);
					cellRight.setAttribute("align","left");
					labelnoofmac = document.createElement('label');
					labelnoofmac.appendChild(document.createTextNode('No. of Machines'))
					cellRight.appendChild(labelnoofmac);
					var hspower;
					var cellRight = row.insertCell(1);
					cellRight.setAttribute("align","left");
					hspower = document.createElement('label');
					hspower.appendChild(document.createTextNode('Horse Power'))
					cellRight.appendChild(hspower);
					
				  	<s:if test="%{!sControlDisabled}">
						var adddel;
						var cellRight = row.insertCell(2);
						cellRight.setAttribute("align","left");
						adddel = document.createElement('label');
						adddel.appendChild(document.createTextNode('Add/Del'))
						cellRight.appendChild(adddel);
					</s:if>
				}
				 			  
				var lastRow = tbl.rows.length;
				var iteration = lastRow;
				var row = tbl.insertRow(lastRow);
				//1st column
				var noOfMachines;  
				var cellRight = row.insertCell(0);
				cellRight.setAttribute("align","left");
				noOfMachines = document.createElement('input');
				noOfMachines.type = 'text';
				noOfMachines.size = '8';
				noOfMachines.onBlur= 'checkLength(this,6)';
				<s:if test="%{sControlDisabled}">
				  noOfMachines.disabled="<s:property value='%{sControlDisabled}' />";
				</s:if>
				noOfMachines.name = 'installedMotorList['+motorcnt+'].noOfMachines' ;
				noOfMachines.id = 'installedMotorList['+motorcnt+'].noOfMachines' ;
				if(machines!=null && machines!='') {
				  	noOfMachines.value = machines;
				}
				
				if(browser=='Microsoft Internet Explorer'){
				    noOfMachines.onblur=totalHP;
				} else {
				    noOfMachines.setAttribute('onBlur', 'checkLength(this,6);totalHP(this);' );
				}
				cellRight.appendChild(noOfMachines);
				    
				//2nd column
				var cellRight = row.insertCell(1);
				cellRight.setAttribute("align","left");
				horsepower = document.createElement('input');
				horsepower.type = 'text';
				horsepower.size = '12';
				horsepower.onBlur = 'checkLength(this,6)';
				<s:if test="%{sControlDisabled}">
				  	horsepower.disabled="<s:property value='%{sControlDisabled}' />";
				</s:if>
				  
				horsepower.name = 'installedMotorList['+motorcnt+'].hp' ;
				horsepower.id = 'installedMotorList['+motorcnt+'].hp' ;
				
				if(motorhpvalue!=null && motorhpvalue!=''){	
				  	horsepower.value = motorhpvalue;
				}
				
				if(browser=='Microsoft Internet Explorer'){
				    horsepower.onblur=totalHP;
				} else{
				    horsepower.setAttribute('onBlur', 'checkLength(this,6);totalHP(this);' );
				}
				  
				 cellRight.appendChild(horsepower);
				
				  //3rd column
				<s:if test="%{!sControlDisabled}">  
					var oCell = row.insertCell(2);
					oCell.innerHTML = "<img src='${pageContext.request.contextPath}/images/addrow.gif' alt='Add' width='18' height='18' border='0' id='addImg"+motorcnt+"' onclick='addMotorRowToTable(false);'/> <img src='${pageContext.request.contextPath}/images/removerow.gif' alt='Remove' id='delImg"+motorcnt+"' width='18' height='18' border='0' onclick='removeRow1(this);'/>";
				</s:if>
				 motorcnt++;  
			}
			
			function removeRow1(src){  
				var tbl = document.getElementById('tb2Create');
				var lastRow = tbl.rows.length;
				if(lastRow>=3) {
 					var oRow = src.parentNode.parentNode;
 					if (oRow.rowIndex == 2) 
 					{
 						bootbox.alert("Can not delete the first row!");
 						return;
 					}
 					else
 					{
 					document.all('tb2Create').deleteRow(oRow.rowIndex);
 					}
 					totalHP();  
				 	detailchange();
				}
			}
			
			function checkLength(obj,val){
				if(obj.value.length>val) {
					bootbox.alert('Max '+val+' digits allowed')
					obj.value = obj.value.substring(0,val);
				}
			}	
	
			function formatCurrency(obj) {
       			if(obj.value=="") {
        			return;
        		} else {
            		obj.value=(parseFloat(obj.value)).toFixed(2);
       			}
    		}
 		</script>

	</head>
	<body onload="onBodyLoad()">
		<table align="center" width="100%">
			<tbody>
				<tr>
					<td>
						<div align="center">
							<center>
								<div class="formmainbox">
									<div class="headingbg">
										<s:text name="page.title.newtrade" />
									</div>
									<table>
										<tr>
											<td align="left" style="color: #FF0000">
												<s:actionerror cssStyle="color: #FF0000" />
												<s:actionmessage />
											</td>
										</tr>
									</table>
									<s:form action="newCitizenTradeLicense" theme="css_xhtml" name="registrationForm" validate="true">
									<s:token/>
										<s:hidden name="actionName" value="create" />
										<s:hidden id="detailChanged" name="detailChanged" />
										<s:hidden name="docNumber" id="docNumber" />
										<c:set var="trclass" value="greybox" />
										<table border="0" cellpadding="0" cellspacing="0" width="100%">
											<tbody>
												<tr>
													<td colspan="5" class="headingwk">
														<div class="arrowiconwk">
															<img src="${pageContext.request.contextPath}/images/arrow.gif" height="20"/>
														</div>
														<div class="headplacer">
															<s:text name='license.title.applicantiondetails' />
														</div>
													</td>
												</tr>
												<%@ include file='../../common/license.jsp'%>
												<%@ include file='../../common/address.jsp'%>
												<tr>
													<td colspan="5" class="headingwk">
														<div class="arrowiconwk">
															<img src="${pageContext.request.contextPath}/images/arrow.gif" height="20"/>
														</div>
														<div class="headplacer">
															<s:text name='license.title.applicantdetails' />
														</div>
													</td>
												</tr>
												<%@ include file='../../common/licensee.jsp'%>
												<%@ include file='../../common/licenseeAddress.jsp'%>
												
												<c:choose>
													<c:when test="${trclass=='greybox'}">
														<c:set var="trclass" value="bluebox" />
													</c:when>
													<c:when test="${trclass=='bluebox'}">
														<c:set var="trclass" value="greybox" />
													</c:when>
												</c:choose>
												<tr>
													<td class="<c:out value="${trclass}"/> width="5%"></td>
													<td class="<c:out value="${trclass}"/>">
														<s:text name='license.othercharges' />
													</td>
													<td class="<c:out value="${trclass}"/>">
														<s:textfield name="otherCharges" maxlength="8" onKeyPress="return numbersforamount(this, event)" onBlur="checkLength(this,8),formatCurrency(otherCharges)" />
													</td>
													<td class="<c:out value="${trclass}"/>">
														<s:text name='license.deduction' />
													</td>
													<td class="<c:out value="${trclass}"/>"> <s:textfield name="deduction"  maxlength="8" onKeyPress="return numbersforamount(this, event)" onBlur="checkLength(this,8),formatCurrency(deduction)" /></td>
												</tr>
												<c:choose>
													<c:when test="${trclass=='greybox'}">
														<c:set var="trclass" value="bluebox" />
													</c:when>
													<c:when test="${trclass=='bluebox'}">
														<c:set var="trclass" value="greybox" />
													</c:when>
												</c:choose>
												<tr>
												<td class="<c:out value="${trclass}"/> width="5%"></td>
													<td class="<c:out value="${trclass}"/>">
														<s:text name='license.swmfee' />
													</td>
													<td class="<c:out value="${trclass}"/>">
														<s:textfield name="swmFee" maxlength="8" onKeyPress="return numbersforamount(this, event)" onBlur="checkLength(this,8),formatCurrency(swmFee)" />
													</td>
													<td colspan="4" class="<c:out value="${trclass}"/> width="5%"></td>
												</tr>
												<c:choose>
													<c:when test="${trclass=='greybox'}">
														<c:set var="trclass" value="bluebox" />
													</c:when>
													<c:when test="${trclass=='bluebox'}">
														<c:set var="trclass" value="greybox" />
													</c:when>
												</c:choose>
												<tr align="center">
													<td class="<c:out value="${trclass}"/> width="5%"></td>
													<td class="<c:out value="${trclass}"/> width="5%"></td>
													<td class="<c:out value="${trclass}"/>" colspan="3">
														<input type="button" class="button" value="Upload Document" id="docUploadButton" onclick="showDocumentManagerForDoc('docNumber');updateCurrentDocId('docNumber')" tabindex="1" />
													</td>
												</tr>
												<c:choose>
													<c:when test="${trclass=='greybox'}">
														<c:set var="trclass" value="bluebox" />
													</c:when>
													<c:when test="${trclass=='bluebox'}">
														<c:set var="trclass" value="greybox" />
													</c:when>
												</c:choose>
												<tr>
													<td colspan="5" class="headingwk">
														<div class="arrowiconwk">
															<img src="${pageContext.request.contextPath}/images/arrow.gif" height="20"/>
														</div>
														<div class="headplacer">
															<s:text name='license.title.motordetail' />
														</div>
													</td>
												</tr>
												<tr>
													<td class="<c:out value="${trclass}"/>" width="5%">
													</td>
													<td class="<c:out value="${trclass}"/>">
														<s:text name="license.motor.installed" />
													</td>
													<td class="<c:out value="${trclass}"/>">
														<s:checkbox theme="simple" key="motorInstalled" tabindex="17" onclick="showhide('addmoremotor')" label="motorInstalled" id="motorInstalled" disabled="%{sDisabled}" />
													</td>
													<td class="<c:out value="${trclass}"/>" colspan="2"></td>
												</tr>
												<c:choose>
													<c:when test="${trclass=='greybox'}">
														<c:set var="trclass" value="bluebox" />
													</c:when>
													<c:when test="${trclass=='bluebox'}">
														<c:set var="trclass" value="greybox" />
													</c:when>
												</c:choose>
												<tr>
													<td colspan="2" class="<c:out value="${trclass}"/>">
													</td>
													<td colspan="3" class="<c:out value="${trclass}"/>">
														<table width="47%" border="0" cellspacing="1" cellpadding="0" id="tb2Create" align="left">
															<th id="hpheader" style="display: none;" colspan="3" class="bluebgheadtd" align="center">
																<b><s:text name="license.horsepower" /><span class="mandatory1">*</span> </b>
															</th>
														</table>
													</td>
												</tr>
												<script>
												<s:iterator var="p" value="installedMotorList">
													addMotorRowToTable(true,'<s:property value="key"/>',  '<s:property value="#p.hp"/>', '<s:property value="#p.noOfMachines"/>');
												</s:iterator>
												</script>
												<c:choose>
													<c:when test="${trclass=='greybox'}">
														<c:set var="trclass" value="bluebox" />
													</c:when>
													<c:when test="${trclass=='bluebox'}">
														<c:set var="trclass" value="greybox" />
													</c:when>
												</c:choose>
												<tr id="addmoremotor">
													<td colspan="2" class="<c:out value="${trclass}"/>" />
													<td colspan="2" class="<c:out value="${trclass}"/>" align="center" class="bluebox1">
														<s:text name="license.total.horsepower" />
														:
														<span class="mandatory1">*</span>
														<span class="greybox"> 
															<s:textfield readonly="true" disabled="%{sDisabled}" name="totalHP" size="12" onBlur="trimAll(this.value);" id="totalHP" /> 
														</span>
													</td>
													<td colspan="1" class="<c:out value="${trclass}"/>"></td>
												</tr>
												<script>
														if(document.getElementById("motorInstalled").checked){															
															document.getElementById("addmoremotor").style.display='';
														}else{
															document.getElementById("addmoremotor").style.display='none';
														}														
														totalHP();
												</script>
											</tbody>
										</table>

										<div>
											<table>
												<tr class="buttonbottom" id="buttondiv" style="align: middle">
													<td>
														<s:submit type="submit" cssClass="buttonsubmit" value="Save" id="Save" method="create" onclick="return validateForm(this);" />
													</td>
													<td>
														<input type="button" value="Close" onclick="javascript:window.close()" class="button" />
													</td>
												</tr>
											</table>
										</div>
										<div class="mandatory1" style="font-size: 11px;" align="left">
											* Mandatory Fields
										</div>
									</s:form>
								</div>
							</center>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</body>
</html>
