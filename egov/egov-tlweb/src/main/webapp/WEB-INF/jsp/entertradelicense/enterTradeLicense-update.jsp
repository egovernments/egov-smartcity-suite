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

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<html>
	<head>
		<title><s:text name="page.title.entertrade.update" /></title>
 	</head>
	<body onload="showHideAgreement()">
		<div id="enterLicense_error" class="error-msg" style="display:none;"></div>
                <div class="row">
                    <div class="col-md-12">
                     <div class="text-right error-msg" style="font-size:14px;"><s:text name="dateofapplication.lbl" /> : <s:date name="model.applicationDate"  format="dd/MM/yyyy"/></div>
                     <s:if test="%{model.applicationNumber!=null}">
                    	 <div class="text-right error-msg" style="font-size:14px;"><s:text name="application.num" /> : <s:property value="%{model.applicationNumber}" /></div>
                 	</s:if>
               		<s:if test="%{hasErrors()}">
						<div align="center" class="error-msg" >
							<s:actionerror />
							<s:fielderror/>
						</div>			 
					</s:if>
					<s:if test="%{hasActionMessages()}">
						<div class="messagestyle">
							<s:actionmessage theme="simple" />
						</div>
					</s:if>
                 	
                 	<s:form name="registrationForm" action="update" theme="css_xhtml"  enctype="multipart/form-data" 
					cssClass="form-horizontal form-groups-bordered" validate="true" >    
					<s:push value="model"> 
							<s:token/>
							<s:hidden id="detailChanged" name="detailChanged" />
							<s:hidden id="applicationDate" name="applicationDate" />
							<s:hidden name="id" id="id" />
							<s:hidden name="licenseNumber"/>
							<s:hidden name="feeTypeId" id="feeTypeId" />
                        <div class="panel panel-primary" data-collapsed="0">
                            <div class="panel-heading">
								<div class="panel-title" style="text-align:center">
									<s:text name="page.title.entertrade.update" />
								</div>
                                <ul class="nav nav-tabs" id="settingstab">
                                    <li class="active"><a data-toggle="tab" href="#tradedetails" data-tabidx="0" aria-expanded="true"><s:text name="license.tradedetail"/></a></li>
                                    <li class=""><a data-toggle="tab" href="#tradeattachments" data-tabidx="1" aria-expanded="false"><s:text name="license.support.docs"/></a></li>
                                </ul>
                            </div>
                            
                             <div class="panel-body custom-form">
                                <div class="tab-content">
                                    <div class="tab-pane fade active in" id="tradedetails">
													
											<div class="form-group">
											    <label class="col-sm-3 control-label text-right"><s:text name='license.oldlicensenum' /><span class="mandatory"></span></label>
											    <div class="col-sm-3 add-margin">
											           <s:textfield name="oldLicenseNumber"  id="oldLicenseNumber" onBlur="checkLength(this,50)"  maxlength="50" cssClass="form-control patternvalidation"  data-pattern="alphanumerichyphenbackslash" />
											    </div>
											    <%-- <label class="col-sm-2 control-label text-right"><s:text name='license.enter.issuedate' /><span class="mandatory"></span></label>
											     <div class="col-sm-3 add-margin">
											      	<s:date name="dateOfCreation" id="dateOfCreationformat" format="dd/MM/yyyy" />
													<s:textfield  name="dateOfCreation" id="dateOfCreation" class="form-control datepicker" data-date-end-date="0d" maxlength="10" size="10" value="%{dateOfCreationformat}" />
											   </div>  --%>
											</div>		
                                             <%@ include file='../common/licensee.jsp'%>
	                                         <%@ include file='../common/address.jsp'%>
	                                         <%@ include file='../common/license.jsp'%>
	                                         
	                                         <div class="panel-heading custom_form_panel_heading">
											    <div class="panel-title"><s:text name='license.title.feedetail' /></div>
											</div>
											
											<div class="col-md-12">
											<table class="table table-bordered feedetails">
												<thead>
													<tr>
														<th><s:text name='license.fin.year'/></th>
														<th><s:text name='license.fee.amount'/></th>
														<th class="text-center"><s:text name='license.fee.paid.y.n'/></th>
													</tr>
												</thead>
												<tbody>
												<c:set value="" var="startfinyear"/>
												<s:iterator value="legacyInstallmentwiseFees" var="LIFee" status="stat">
													<tr>
														<c:set value="${fn:substring(LIFee.key,0, 4)}-${fn:substring(LIFee.key,2, 4)+1}" var="finyear"/>
														<s:if test="#stat.index == 0">
															<c:set value="${finyear}" var="startfinyear"/>
														</s:if>
														<td><input type="text"  name="" class="form-control feeyear" readonly="readonly" value="${finyear}" tabindex="-1"/></td>
														<td><input type="text" name="legacyInstallmentwiseFees[${LIFee.key}]" class="form-control patternvalidation feeamount"  value="${LIFee.value}" data-pattern="number"/> </td>
														<td class="text-center">
														<s:checkbox name="legacyFeePayStatus[%{#attr.LIFee.key}]" class="case"></s:checkbox>
														</td>
													</tr>
												</s:iterator>
												</tbody>
												<tfoot>
													<tr>
														<td class="error-msg" colspan="3">
															<s:text  name="license.legacy.info">
																<s:param>${startfinyear}</s:param>
															</s:text>
														</td>
													</tr>
												</tfoot>
											</table>
											</div>
											
                                    </div>
                                    <div class="tab-pane fade" id="tradeattachments"> 
                                        <div>
											<%@include file="../common/supportdocs-update.jsp" %>
										</div>
                                    </div>
                            	</div>
                            </div>
                        </div> 
                        <div class="row">
							<div class="text-center">
								<button type="submit" id="btnsave" class="btn btn-primary" onclick="return validateForm();">
									Save</button>
								<button type="button" id="btnclose" class="btn btn-default" onclick="window.close();">
									Close</button>
							</div>
						</div>
                        </s:push>  
                    </s:form> 
                    </div>
                </div>
                <script>
					jQuery(".datepicker").datepicker({
						format: "dd/mm/yyyy",
						autoclose: true 
					}); 
				</script>
        <script src="../resources/js/app/newtrade.js?rnd=${app_release_no}"></script>
        <script>
	
			function validateForm() {
				if (document.getElementById("oldLicenseNumber").value == '' || document.getElementById("oldLicenseNumber").value == null){
					showMessage('enterLicense_error', '<s:text name="newlicense.oldlicensenumber.null" />');
					document.getElementById("oldLicenseNumber").focus();
					return false;
				}/*  else if (document.getElementById("dateOfCreation").value == '' || document.getElementById("dateOfCreation").value == null){
					showMessage('enterLicense_error', '<s:text name="newlicense.dateofcreation.null" />');
					document.getElementById("dateOfCreation").focus();
					return false;
				} */ else if (document.getElementById("mobilePhoneNumber").value == '' || document.getElementById("mobilePhoneNumber").value == null){
					showMessage('enterLicense_error', '<s:text name="newlicense.mobilephonenumber.null" />');
					document.getElementById("mobilePhoneNumber").focus();
					return false;
				} else if (document.getElementById("applicantName").value == '' || document.getElementById("applicantName").value == null){
					showMessage('enterLicense_error', '<s:text name="newlicense.applicantname.null" />');
					document.getElementById("applicantName").focus();
					return false;
				} else if (document.getElementById("fatherOrSpouseName").value == '' || document.getElementById("fatherOrSpouseName").value == null){
					showMessage('enterLicense_error', '<s:text name="newlicense.fatherorspousename.null" />');
					document.getElementById("fatherOrSpouseName").focus();
					return false;
				} else if (document.getElementById("emailId").value == '' || document.getElementById("emailId").value == null){
					showMessage('enterLicense_error', '<s:text name="newlicense.email.null" />');
					document.getElementById("emailId").focus();
					return false;
				} else if (document.getElementById("licenseeAddress").value == '' || document.getElementById("licenseeAddress").value == null){
					showMessage('enterLicense_error', '<s:text name="newlicense.licenseeaddress.null" />');
					document.getElementById("licenseeAddress").focus();
					return false;
				} else if (document.getElementById("boundary").value == '-1'){
					showMessage('enterLicense_error', '<s:text name="newlicense.locality.null" />');
					document.getElementById("boundary").focus();
					return false;
				} else if (document.getElementById("ownershipType").value == '-1'){
					showMessage('enterLicense_error', '<s:text name="newlicense.ownershiptype.null" />');
					document.getElementById("ownershipType").focus();
					return false;
				} else if (document.getElementById("address").value == '' || document.getElementById("address").value == null){
					showMessage('enterLicense_error', '<s:text name="newlicense.licenseaddress.null" />');
					document.getElementById("address").focus();
					return false;
				} else if (document.getElementById("buildingType").value == '-1'){
					showMessage('enterLicense_error', '<s:text name="newlicense.buildingtype.null" />');
					document.getElementById("buildingType").focus();
					return false;
				} else if (document.getElementById("category").value == '-1'){
					showMessage('enterLicense_error', '<s:text name="newlicense.category.null" />');
					document.getElementById("category").focus();
					return false;
				}  else if (document.getElementById("subCategory").value == '-1'){
					showMessage('enterLicense_error', '<s:text name="newlicense.subcategory.null" />');
					document.getElementById("subCategory").focus();
					return false;
				}	else if (document.getElementById("tradeArea_weight").value == '' || document.getElementById("tradeArea_weight").value == null){
					showMessage('enterLicense_error', '<s:text name="newlicense.tradeareaweight.null" />');
					document.getElementById("tradeArea_weight").focus();
					return false;
				}	else if (document.getElementById("startDate").value == '' || document.getElementById("startDate").value == null){
					showMessage('enterLicense_error', '<s:text name="newlicense.startDate.null" />');
					window.scroll(0, 0);  
					return false;
				}  else if(document.getElementById("showAgreementDtl").checked){
					 if (document.getElementById("agreementDate").value == '' || document.getElementById("agreementDate").value == null){
							showMessage('enterLicense_error', '<s:text name="newlicense.agreementDate.null" />');
							window.scroll(0, 0);  
							return false;
					 } else if(document.getElementById("agreementDocNo").value == '' || document.getElementById("agreementDocNo").value == null){
						 	showMessage('enterLicense_error', '<s:text name="newlicense.agreementDocNo.null" />');
							window.scroll(0, 0);  
							return false;
					}else{
						/*validate fee details*/
						if(validate_feedetails()){
							//checkbox checked
							if(feedetails_checked()){
								formsubmit();
							}else{
								return false;
							}
						}else{
							return false;
						}
					}
				} else{
					/*validate fee details*/
					if(validate_feedetails()){
						//checkbox checked
						if(feedetails_checked()){
							formsubmit();
						}else{
							return false;
						}
					}else{
						return false;
					}
				}
  			}

			function validate_feedetails(){
				
				var validated = false;
				var globalindex;
				
				jQuery("table.feedetails tbody tr").each(function (index) {
					var rowval = jQuery(this).find("input.feeamount").val();
					if(parseFloat(rowval) > 0){
						globalindex = index;
						validated = true;					
					}else{
						if(index == (globalindex+1)){
							bootbox.alert(jQuery(this).find("input.feeyear").val()+' financial year fee details amount is missing!');
							validated = false;
							return false;
						}else{
							if(jQuery(this).is(":last-child")){
								bootbox.alert(jQuery(this).find("input.feeyear").val()+' financial year fee details amount is mandatory!');
								validated = false;
								return false;
							}
						}
					}
				});
				return validated;
			}

			function feedetails_checked(){
				
				var checkindex;
				var validated = false;
				
				jQuery('.case:checked').each(function () {
			        checkindex = jQuery(this).closest('tr').index();
			    });

				if(checkindex != undefined){
					jQuery("table.feedetails tbody tr").each(function (index) {
						if(index > checkindex){
							validated = true;
							return;
						}else{
							var rowval = jQuery(this).find("input.feeamount").val();	
							if(parseFloat(rowval) > 0){
								if(jQuery(this).is(":last-child")){
									//leave it
									validated = true;
								}else{
									if(jQuery(this).find('input[type=checkbox]:checked').val() == undefined){
										bootbox.alert(jQuery(this).find("input.feeyear").val()+' financial year fee details paid should be checked!');
										validated = false;
										return false;
									}
								}
							}
						}
					});
				}else{
					validated = true;
				}
				return validated;
			}

  			function formsubmit(){
  				/*submit the form*/
				clearMessage('enterLicense_error');
				toggleFields(false,"");
				document.registrationForm.action='${pageContext.request.contextPath}/entertradelicense/update.action'
				document.registrationForm.submit();
  			}

 		</script>
    </body>
</html>
