<!-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#     accountability and the service delivery of the government  organizations.
#  
#      Copyright (C) <2015>  eGovernments Foundation
#  
#      The updated version of eGov suite of products as by eGovernments Foundation 
#      is available at http://www.egovernments.org
#  
#      This program is free software: you can redistribute it and/or modify
#      it under the terms of the GNU General Public License as published by
#      the Free Software Foundation, either version 3 of the License, or
#      any later version.
#  
#      This program is distributed in the hope that it will be useful,
#      but WITHOUT ANY WARRANTY; without even the implied warranty of
#      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#      GNU General Public License for more details.
#  
#      You should have received a copy of the GNU General Public License
#      along with this program. If not, see http://www.gnu.org/licenses/ or 
#      http://www.gnu.org/licenses/gpl.html .
#  
#      In addition to the terms of the GPL license to be adhered to in using this
#      program, the following additional terms are to be complied with:
#  
#  	1) All versions of this program, verbatim or modified must carry this 
#  	   Legal Notice.
#  
#  	2) Any misrepresentation of the origin of the material is prohibited. It 
#  	   is required that all modified versions of this material be marked in 
#  	   reasonable ways as different from the original version.
#  
#  	3) This license does not grant any rights to any user of the program 
#  	   with regards to rights under trademark law for use of the trade names 
#  	   or trademarks of eGovernments Foundation.
#  
#    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------->
<%@ include file="/includes/taglibs.jsp"%>
<sx:head/>

<html>
	<head>
		<title><s:text name="page.title.newtrade" /></title>
		<script>
	
			function validateForm(obj) {
				if (document.getElementById("mobilePhoneNumber").value == '' || document.getElementById("mobilePhoneNumber").value == null){
					showMessage('newLicense_error', '<s:text name="newlicense.mobilephonenumber.null" />');
					document.getElementById("mobilePhoneNumber").focus();
					return false;
				} else if (document.getElementById("applicantName").value == '' || document.getElementById("applicantName").value == null){
					showMessage('newLicense_error', '<s:text name="newlicense.applicantname.null" />');
					document.getElementById("applicantName").focus();
					return false;
				} else if (document.getElementById("fatherOrSpouseName").value == '' || document.getElementById("fatherOrSpouseName").value == null){
					showMessage('newLicense_error', '<s:text name="newlicense.fatherorspousename.null" />');
					document.getElementById("fatherOrSpouseName").focus();
					return false;
				} else if (document.getElementById("emailId").value == '' || document.getElementById("emailId").value == null){
					showMessage('newLicense_error', '<s:text name="newlicense.email.null" />');
					document.getElementById("emailId").focus();
					return false;
				} else if (document.getElementById("licenseeAddress").value == '' || document.getElementById("licenseeAddress").value == null){
					showMessage('newLicense_error', '<s:text name="newlicense.licenseeaddress.null" />');
					document.getElementById("licenseeAddress").focus();
					return false;
				} else if (document.getElementById("boundary").value == '-1'){
					showMessage('newLicense_error', '<s:text name="newlicense.locality.null" />');
					document.getElementById("boundary").focus();
					return false;
				} else if (document.getElementById("ownershipType").value == '-1'){
					showMessage('newLicense_error', '<s:text name="newlicense.ownershiptype.null" />');
					document.getElementById("ownershipType").focus();
					return false;
				} else if (document.getElementById("address").value == '' || document.getElementById("address").value == null){
					showMessage('newLicense_error', '<s:text name="newlicense.licenseaddress.null" />');
					document.getElementById("address").focus();
					return false;
				} else if (document.getElementById("buildingType").value == '-1'){
					showMessage('newLicense_error', '<s:text name="newlicense.buildingtype.null" />');
					document.getElementById("buildingType").focus();
					return false;
				} else if (document.getElementById("category").value == '-1'){
					showMessage('newLicense_error', '<s:text name="newlicense.category.null" />');
					document.getElementById("category").focus();
					return false;
				}  else if (document.getElementById("subCategory").value == '-1'){
					showMessage('newLicense_error', '<s:text name="newlicense.subcategory.null" />');
					document.getElementById("subCategory").focus();
					return false;
				}	else if (document.getElementById("tradeArea_weight").value == '' || document.getElementById("tradeArea_weight").value == null){
					showMessage('newLicense_error', '<s:text name="newlicense.tradeareaweight.null" />');
					document.getElementById("tradeArea_weight").focus();
					return false;
				}	else if (document.getElementById("uom").value == '-1'){
					showMessage('newLicense_error', '<s:text name="newlicense.uom.null" />');
					document.getElementById("uom").focus();
					return false;
				} else if (document.getElementById("workersCapacity").value == '' ||  document.getElementById("workersCapacity").value == null ||
						 document.getElementById("workersCapacity").value == 0) {
					showMessage('newLicense_error', '<s:text name="newlicense.workerscapacity.null" />');
					document.getElementById("workersCapacity").focus();
					return false;
				}
    			if(validateForm_newTradeLicense()==false) { 
    				return false;
    			} else { 
    				clearMessage('newLicense_error')
        			document.newTradeLicense.action='${pageContext.request.contextPath}/newtradelicense/newTradeLicense-create.action';
    		    	document.newTradeLicense.submit();
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
						adddel.appendChild(document.createTextNode('Actions'))
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
				noOfMachines.size = '10';
				noOfMachines.onBlur= 'checkLength(this,3)';
				noOfMachines.className = "form-control";
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
				    noOfMachines.setAttribute('onBlur', 'checkLength(this,3);totalHP(this);' );
				}
				cellRight.appendChild(noOfMachines);
				    
				//2nd column
				var cellRight = row.insertCell(1);
				cellRight.setAttribute("align","left");
				horsepower = document.createElement('input');
				horsepower.type = 'text';
				horsepower.size = '10';
				horsepower.onBlur = 'checkLength(this,6)';
				horsepower.className = "form-control";
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
					oCell.innerHTML = "<span class='add-padding' style='cursor:pointer;' id='addImg"+motorcnt+"' onclick='addMotorRowToTable(false);'><i class='fa fa-plus'></i></span><span class='add-padding' style='cursor:pointer;' id='delImg"+motorcnt+"'  onclick='removeRow1(this);'><i class='fa fa-trash'></i></span>";
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
 						alert("Can not delete the first row!");
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
					alert('Max '+val+' digits allowed')
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

    		function onSubmit() {
    			return validateForm(this);
        	}

			// Calls propertytax REST api to retrieve property details for an assessment no
			// url : contextpath/ptis/rest/property/assessmentno (ex: contextpath/ptis/rest/property/1085000001)
    		function callPropertyTaxRest(){
               	var propertyNo = jQuery("#propertyNo").val();
            	if(propertyNo!="" && propertyNo!=null){
					console.log(propertyNo); 
					jQuery.ajax({
						url: "/ptis/rest/property/" + propertyNo,
						type:"GET",
						contentType:"application/x-www-form-urlencoded",
						success:function(data){
							if(data.errorDetails.errorCode != null && data.errorDetails.errorCode != ''){
								alert(data.errorDetails.errorMessage);
							} else{
								if(data.boundaryDetails!=null){
									jQuery("#zoneName").val(data.boundaryDetails.zoneName);
									jQuery("#wardName").val(data.boundaryDetails.wardName);
									jQuery("#address").val(data.propertyAddress);
								}
							}
						},
						error:function(e){
							console.log('error:'+e.message);
							document.getElementById("propertyNo").value="";
							resetOnPropertyNumChange();
							alert("Error getting property details");
						}
					});
            	} else{
					showMessage('newLicense_error', '<s:text name="newlicense.propertyNo.null" />');
            		document.getElementById("propertyNo").focus();
                }
            }

            function resetOnPropertyNumChange(){
            	var propertyNo = jQuery("#propertyNo").val();
               	if(propertyNo!="" && propertyNo!=null){
            		document.getElementById("address").disabled="true";
	            	document.getElementById("boundary").disabled="true"; 
            	} else {
                    document.getElementById("address").disabled=false;
	            	document.getElementById("boundary").disabled=false;  
                }
            	document.getElementById("boundary").value='-1';
            	document.getElementById("zoneName").value="";
            	document.getElementById("wardName").value="";
            	document.getElementById("address").value="";
            }
        	
 		</script>
 		
 			</head>
	<body onload="onBodyLoad()">
		<div id="newLicense_error" class="errorstyle" style="display:none;"></div> 
                <div class="row">
                    <div class="col-md-12">
                     <div class="text-right error-msg" style="font-size:14px;"><s:text name="dateofapplication.lbl" /> : <s:date name="applicationDate"  format="dd/MM/yyyy"/></div>
                 		<s:if test="%{hasErrors()}">
							<div align="left">
								<s:actionerror />
								<s:fielderror/>
							</div>			 
						</s:if>
						<s:if test="%{hasActionMessages()}">
						<div class="messagestyle">
							<s:actionmessage theme="simple" />
						</div>
						</s:if>
                 	
                 	<s:form name="newTradeLicense" action="newTradeLicense" theme="css_xhtml"  enctype="multipart/form-data" 
					cssClass="form-horizontal form-groups-bordered" validate="true" >    
					<s:push value="model"> 
							<s:token/>
							<s:hidden name="actionName" value="create" />
							<s:hidden id="detailChanged" name="detailChanged" />
							<s:hidden id="applicationDate" name="applicationDate" />
                        <div class="panel panel-primary" data-collapsed="0">
                            <div class="panel-heading">
                            
								<div class="panel-title" style="text-align:center">
										<s:text name='newtradeLicense.heading' /> 
								</div>
                            
                                <ul class="nav nav-tabs" id="settingstab">
                                    <li class="active"><a data-toggle="tab" href="#tradedetails" data-tabidx="0" aria-expanded="true">Trade Details</a></li>
                                    <li class=""><a data-toggle="tab" href="#tradeattachments" data-tabidx="1" aria-expanded="false">Enclosed Documents</a></li>
                                </ul>
                            </div>
                            
                             <div class="panel-body custom-form">
                                <div class="tab-content">
                                    <div class="tab-pane fade active in" id="tradedetails">
                                        <form role="form" class="form-horizontal form-groups-bordered">
	                                         <%@ include file='../common/licensee.jsp'%>
	                                          <%@ include file='../common/address.jsp'%>
	                                         <%@ include file='../common/license.jsp'%>
	                                         
	                                         
	                                         <div class="panel-heading custom_form_panel_heading">
											    <div class="panel-title"><s:text name='license.title.feedetail' /></div>
											</div>
											
											<div class="form-group">
											    <label class="col-sm-3 control-label text-right"><s:text name="license.motor.installed" /></label>
											    <div class="col-sm-3 add-margin text-left">
											         	<s:checkbox theme="simple" key="motorInstalled" tabindex="17" onclick="showhide('addmoremotor')" label="motorInstalled" id="motorInstalled" disabled="%{sDisabled}" />
											    </div>
											</div>
											<div class="form-group">
											   <!-- <table  width="47%" border="0" cellspacing="1" cellpadding="0" id="tb2Create" align="left"> -->
											   <table class="table table-bordered" style="width:80%;margin:10px auto" id="tb2Create">
															<th id="hpheader" style="display: none;" colspan="3" class="bluebgheadtd" align="center">
																<b><s:text name="license.horsepower" /></b>
															</th>
												</table>			
											</div>
											<script>
												<s:iterator var="p" value="installedMotorList">
													addMotorRowToTable(true,'<s:property value="key"/>',  '<s:property value="#p.hp"/>', '<s:property value="#p.noOfMachines"/>');
												</s:iterator>
											</script>
											
											<div class="form-group" id="addmoremotor">
											    <label class="col-sm-3 control-label text-right"><s:text name="license.total.horsepower" /><span class="mandatory"></span></label>
											    <div class="col-sm-3 add-margin">	
											    	<s:textfield name="totalHP" readonly="true" disabled="%{sDisabled}"  onBlur="trimAll(this.value);" id="totalHP" cssClass="form-control" />
											    </div>		
											</div>
											
											
											<div class="form-group">
											    <label class="col-sm-3 control-label text-right"><s:text name="license.total.workersCapacity" /><span class="mandatory"></span></label>
											    <div class="col-sm-3 add-margin">	
											    	<s:textfield name="workersCapacity" size="8" maxlength="8" onBlur="trimAll(this.value);" id="workersCapacity" cssClass="form-control patternvalidation" data-pattern="number" />
											    </div>		
											</div>
											
											<script>
														 if(document.getElementById("motorInstalled").checked){															
															document.getElementById("addmoremotor").style.display='';
														}else{
															document.getElementById("addmoremotor").style.display='none';
														} 														
														totalHP();
												</script>
                             			</form>
                                    </div>
                                    
                                    <div class="tab-pane fade" id="tradeattachments">
                                        <form role="form" class="form-horizontal form-groups-bordered">
                                        <div>
												<%@include file="../common/documentUpload.jsp" %>
										</div>
                                            
                                        </form>
                                    </div>
                                    
                            	</div>
                            </div>
                        </div> 
                        </s:push>  
                    </s:form> 
                        
						<%@ include file='../common/commonWorkflowMatrix.jsp'%>
						<%@ include file='../common/commonWorkflowMatrix-button.jsp'%>
                        
                            <!-- <div class="row">
                            <div class="text-center">
                                <button type="submit" class="btn btn-primary">Submit</button>
                                <button class="btn btn-default">Close</button>
                            </div>
                        </div> -->
                    </div>
                </div>
                  
        
        <script src="../resources/app/js/newtrade.js"></script>
       
    </body>
</html>
