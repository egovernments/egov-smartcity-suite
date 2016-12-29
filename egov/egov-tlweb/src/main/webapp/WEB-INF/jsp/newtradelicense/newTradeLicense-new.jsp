<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~      accountability and the service delivery of the government  organizations.
  ~
  ~       Copyright (C) 2016  eGovernments Foundation
  ~
  ~       The updated version of eGov suite of products as by eGovernments Foundation
  ~       is available at http://www.egovernments.org
  ~
  ~       This program is free software: you can redistribute it and/or modify
  ~       it under the terms of the GNU General Public License as published by
  ~       the Free Software Foundation, either version 3 of the License, or
  ~       any later version.
  ~
  ~       This program is distributed in the hope that it will be useful,
  ~       but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~       GNU General Public License for more details.
  ~
  ~       You should have received a copy of the GNU General Public License
  ~       along with this program. If not, see http://www.gnu.org/licenses/ or
  ~       http://www.gnu.org/licenses/gpl.html .
  ~
  ~       In addition to the terms of the GPL license to be adhered to in using this
  ~       program, the following additional terms are to be complied with:
  ~
  ~           1) All versions of this program, verbatim or modified must carry this
  ~              Legal Notice.
  ~
  ~           2) Any misrepresentation of the origin of the material is prohibited. It
  ~              is required that all modified versions of this material be marked in
  ~              reasonable ways as different from the original version.
  ~
  ~           3) This license does not grant any rights to any user of the program
  ~              with regards to rights under trademark law for use of the trade names
  ~              or trademarks of eGovernments Foundation.
  ~
  ~     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<html>
	<head>
		<title><s:text name="page.title.newtrade" /></title>
		
		<script>
		
		function validateLicenseForm(obj) {
				var adhaar = document.getElementById('adhaarId').value;
				if (document.getElementById("mobilePhoneNumber").value == '' || document.getElementById("mobilePhoneNumber").value == null){
					showMessage('newLicense_error', '<s:text name="newlicense.mobilephonenumber.null" />');
					window.scroll(0, 0); 
					return false;
				} else if (document.getElementById("applicantName").value == '' || document.getElementById("applicantName").value == null){
					showMessage('newLicense_error', '<s:text name="newlicense.applicantname.null" />');
					window.scroll(0, 0); 
					return false;
				} else if (document.getElementById("fatherOrSpouseName").value == '' || document.getElementById("fatherOrSpouseName").value == null){
					showMessage('newLicense_error', '<s:text name="newlicense.fatherorspousename.null" />');
					window.scroll(0, 0); 
					return false;
				} else if (document.getElementById("emailId").value == '' || document.getElementById("emailId").value == null){
					showMessage('newLicense_error', '<s:text name="newlicense.email.null" />');
					window.scroll(0, 0);
					return false;
				} else if (document.getElementById("licenseeAddress").value == '' || document.getElementById("licenseeAddress").value == null){
					showMessage('newLicense_error', '<s:text name="newlicense.licenseeaddress.null" />');
					window.scroll(0, 0); 
					return false;
				} else if (document.getElementById("boundary").value == '-1'){
					showMessage('newLicense_error', '<s:text name="newlicense.locality.null" />');
					window.scroll(0, 0); 
					return false;
				} else if (document.getElementById("ownershipType").value == '-1'){
					showMessage('newLicense_error', '<s:text name="newlicense.ownershiptype.null" />');
					window.scroll(0, 0); 
					return false;
				}else if (document.getElementById("nameOfEstablishment").value == '' || document.getElementById("nameOfEstablishment").value == null){
					showMessage('newLicense_error', '<s:text name="newlicense.tradeTitle.null" />');
					window.scroll(0, 0); 
					return false;
				}  
				else if (document.getElementById("address").value == '' || document.getElementById("address").value == null){
					showMessage('newLicense_error', '<s:text name="newlicense.licenseaddress.null" />');
					window.scroll(0, 0);
					return false;
				} else if (document.getElementById("buildingType").value == '-1'){
					showMessage('newLicense_error', '<s:text name="newlicense.buildingtype.null" />');
					window.scroll(0, 0); 
					return false;
				} else if (document.getElementById("category").value == '-1'){
					showMessage('newLicense_error', '<s:text name="newlicense.category.null" />');
					window.scroll(0, 0); 
					return false;
				}  else if (document.getElementById("subCategory").value == '-1'){
					showMessage('newLicense_error', '<s:text name="newlicense.subcategory.null" />');
					window.scroll(0, 0); 
					return false;
				}	else if (document.getElementById("tradeArea_weight").value == '' || document.getElementById("tradeArea_weight").value == null){
					showMessage('newLicense_error', '<s:text name="newlicense.tradeareaweight.null" />');
					window.scroll(0, 0);
					return false;
				}	else if (document.getElementById("uom").value == ""){
					showMessage('newLicense_error', '<s:text name="newlicense.uom.null" />');
					window.scroll(0, 0); 
					return false;
				}	else if (document.getElementById("startDate").value == '' || document.getElementById("startDate").value == null){
					showMessage('newLicense_error', '<s:text name="newlicense.startDate.null" />');
					window.scroll(0, 0);  
					return false;
				}	else if(document.getElementById("showAgreementDtl").checked){
					 if (document.getElementById("agreementDate").value == '' || document.getElementById("agreementDate").value == null){
							showMessage('newLicense_error', '<s:text name="newlicense.agreementDate.null" />');
							window.scroll(0, 0);  
							return false;
					 } else if(document.getElementById("agreementDocNo").value == '' || document.getElementById("agreementDocNo").value == null){
						 	showMessage('newLicense_error', '<s:text name="newlicense.agreementDocNo.null" />');
							window.scroll(0, 0);  
							return false;
					}
				} 

				var adhaar = document.getElementById('adhaarId').value;
				var mobileno= document.getElementById('mobilePhoneNumber').value;
				if(adhaar.length > 0 && adhaar.length < 12){
					jQuery('#adhaarError').removeClass("hide");
					document.getElementById("adhaarId").focus();
					return false;
				}
				if(mobileno.length > 0 && mobileno.length < 10){
					jQuery('#mobileError').removeClass("hide");
					document.getElementById("mobilePhoneNumber").focus();
					return false;
				}
				else { 
					return true;    	              
    			 } 
  			}

			function onBodyLoad(){
  				var currentState=document.getElementById("currentWfstate").value;
  				showHideAgreement();
  				if (currentState=='Second level fee collected') {
  			    	jQuery("span").remove(".mandatory");
  			    }
				if(document.getElementById("mode").value=='disableApprover')	
					{
					toggleFields(true,['Submit','Reject','button2','Approve','approverComments','Sign','Preview','Generate Certificate']); 
					jQuery(".show-row").hide(); 
					jQuery('#approverComments').removeAttr('<span class="mandatory"></span>');
					jQuery('#approverDepartment').removeAttr('<span class="mandatory"></span>');
					jQuery('#approverDesignation').removeAttr('<span class="mandatory"></span>');
					jQuery('#approverPositionId').removeAttr('<span class="mandatory"></span>');
					jQuery('#workflowCommentsDiv label').text('<s:text name="newlicense.fieldInspection.label" />');
					}	
				
				if(document.getElementById("mode").value=='ACK' ){
					 
					   toggleFields(true,['approverDepartment','approverDesignation','approverPositionId','approverComments','Generate Certificate',
					                     'Forward','Reject','button2','Approve','Sign','Preview','closeBtn','closeDiv','currentWfstate']);  
	                  //remove onclick event for propertyno search button
					  jQuery("#searchImg").removeAttr("onclick");
					  // remove onclick event for add and delete button having class = add-padding
					  jQuery('.add-padding').attr('onclick','').unbind('click');
					  // renaming approver remarks label for second level of workflow
					  <s:if test="%{getNextAction()!='END'}">
					  	jQuery('#workflowCommentsDiv label').text('<s:text name="newlicense.fieldInspection.label" />');
					 	 jQuery('#workflowCommentsDiv label').append('<span class="mandatory"></span>');
					</s:if>
					document.getElementById('workflowDiv').style.visibility = 'hidden';
					
				} 
				if(document.getElementById("mode").value=='view'|| document.getElementById("mode").value=='editForReject'){
					 
					   toggleFields(true,['approverDepartment','approverDesignation','approverPositionId','approverComments','Generate Certificate',
					                     'Forward','Reject','button2','Approve','Sign','Preview','currentWfstate']);  
	                  //remove onclick event for propertyno search button
					  jQuery("#searchImg").removeAttr("onclick");
					  // remove onclick event for add and delete button having class = add-padding
					  jQuery('.add-padding').attr('onclick','').unbind('click');
					  // renaming approver remarks label for second level of workflow
					  <s:if test="%{getNextAction()!='END'}">
					  	jQuery('#workflowCommentsDiv label').text('<s:text name="newlicense.fieldInspection.label" />');
					 	 jQuery('#workflowCommentsDiv label').append('<span class="mandatory"></span>');
					  </s:if>
					  if(currentState == 'SI/MHO approved'){
						  document.getElementById('approverDetailHeading').hidden = true
					  }
					
				}
				if(document.getElementById("mode").value=='editForApproval'){
					  toggleFields(true,['approverDepartment','approverDesignation','approverPositionId','approverComments','Generate Certificate',
					                     'Forward','Reject','button2','Approve']); 
	                  //remove onclick event for propertyno search button
	                  document.getElementById("tradeArea_weight").disabled=false;
					  jQuery("#searchImg").removeAttr("onclick");
					  // remove onclick event for add and delete button having class = add-padding
					  jQuery('.add-padding').attr('onclick','').unbind('click');
					  // renaming approver remarks label for second level of workflow
					  <s:if test="%{getNextAction()!='END'}">
					 	 jQuery('#workflowCommentsDiv label').text('<s:text name="newlicense.fieldInspection.label" />');
					 	 jQuery('#workflowCommentsDiv label').append('<span class="mandatory"></span>');
					</s:if>
				} 
				if(currentState == 'First level fee collected')
					document.getElementById('certificateDiv').hidden = false;  
				if(currentState == 'License Created' || currentState=='Commissioner approved')
					document.getElementById('closeDiv').hidden = false;
			}

			function onSubmitValidations() {
    			return validateLicenseForm(this);
        	}

			function formsubmit(){
				if(!validateLicenseForm(this)){
					return false;
				}
  			}

    		function onSubmit() {
        		var mode=document.getElementById("mode").value;
        		var workflowaction = document.getElementById("workFlowAction").value;
        		<s:if test="%{workflowaction!=null && workflowaction='Generate Provisional Certificate'}">
        			window.open("/tl/viewtradelicense/viewTradeLicense-generateCertificate.action?model.id="+jQuery('#id').val(),  'gc'+jQuery('#id').val(), 'scrollbars=yes,width=1000,height=700,status=yes');
        		</s:if>
    			<s:if test="%{mode!=null && ((mode=='view' || mode=='editForApproval' || mode== 'disableApprover') &&  mode!='editForReject' )}">
					clearMessage('newLicense_error');
					toggleFields(false,"");
					document.newTradeLicense.action='${pageContext.request.contextPath}/newtradelicense/newTradeLicense-approve.action';
				</s:if>
				<s:elseif  test="%{mode!=null && mode=='editForReject'}">
				clearMessage('newLicense_error');
				toggleFields(false,"");
				document.newTradeLicense.action='${pageContext.request.contextPath}/newtradelicense/newTradeLicense-approve.action';
			</s:elseif>
				<s:elseif test="%{mode!=null && mode=='edit'}">
					clearMessage('newLicense_error');
					toggleFields(false,"");
					document.newTradeLicense.action = '${pageContext.request.contextPath}/newtradelicense/editTradeLicense-edit.action';
				</s:elseif>
				<s:else>   
					clearMessage('newLicense_error'); 
					toggleFields(false,"");
	    			document.newTradeLicense.action='${pageContext.request.contextPath}/newtradelicense/newTradeLicense-create.action';
				</s:else>

				return true;
        	} 


        	
 		</script>
 		
	</head>
	<body onload="onBodyLoad()">
		<div id="newLicense_error" class="error-msg" style="display:none;" align="center"></div> 
                <div class="row">
                    <div class="col-md-12">
                     <div class="text-right error-msg" style="font-size:14px;"><s:text name="dateofapplication.lbl" /> : <s:date name="applicationDate"  format="dd/MM/yyyy"/></div>
                     <s:if test="%{applicationNumber!=null}">
                    	 <div class="text-right error-msg" style="font-size:14px;"><s:text name="application.num" /> : <s:property value="%{applicationNumber}" /></div>
                 	</s:if>
                 		<s:if test="%{hasErrors()}">
							<div align="center">
								<s:actionerror />
								<s:fielderror/>
							</div>			 
						</s:if>
						<s:if test="%{hasActionMessages()}">
						<div class="messagestyle">
							<s:actionmessage theme="simple" />
						</div>
						</s:if>
                 		<s:if test="%{getMessage() != null}">
                 			<div class="alert alert-danger view-content" style="font-size: 18px;">
							  <s:text name="%{getMessage()}" />
							</div>
                 		</s:if>
                 		
                 	<s:form name="newTradeLicense" action="newTradeLicense-create" theme="simple"  enctype="multipart/form-data" 
					cssClass="form-horizontal form-groups-bordered" validate="true" >    
					<s:push value="model"> 
							<s:token/>
							<s:if test="%{state==null}">
								<s:hidden id="additionalRule" name="additionalRule" value="%{additionalRule}"/>
							</s:if>
							<s:hidden name="actionName" value="create" />
							<s:hidden id="detailChanged" name="detailChanged" />
							<s:hidden id="applicationDate" name="applicationDate" />
							<s:hidden id="mode" name="mode" value="%{mode}" />
							<s:hidden id="currentWfstate" name="currentWfstate" value="%{state.value}" />
							<s:hidden name="id" id="id" />
							<s:hidden name="feeTypeId" id="feeTypeId" />
							
                        <div class="panel panel-primary">
                            <div class="panel-heading">
                            <s:if test="%{mode=='edit'}">
								<div class="panel-title" style="text-align:center">
										<s:text name='page.title.edittrade' /> 
								</div>
							</s:if>
							<s:else>
								<div class="panel-title" style="text-align:center">
										<s:text name='newtradeLicense.heading' /> 
								</div>
							</s:else>
                            
                                 <ul class="nav nav-tabs" id="settingstab">
                                    <li class="active"><a data-toggle="tab" href="#tradedetails" data-tabidx="0" aria-expanded="true"><s:text name="license.tradedetail"/></a></li>
                                    <li class=""><a data-toggle="tab" href="#tradeattachments" data-tabidx="1" aria-expanded="false"><s:text name="license.support.docs"/></a></li>
                                </ul>
                            </div>
                            
                             <div class="panel-body">
                                <div class="tab-content">
                                    <div class="tab-pane fade active in" id="tradedetails">
	                                         <%@ include file='../common/licensee.jsp'%>
	                                         <%@ include file='../common/address.jsp'%>
	                                         <%@ include file='../common/license.jsp'%>
									</div>
                                    <div class="tab-pane fade" id="tradeattachments">
                                    	<%@include file="../common/supportdocs-new.jsp" %>
                                    </div>
                                </div>
                            </div>
                        </div> 
                        <div style="text-align: center;" hidden="true" id="closeDiv">
	                    <input type="button" name="closeBtn" id="closeBtn" value="Close" 
							class="button" onclick="window.close();" style="margin:0 5px"/>
						</div>
						<s:if test="%{state!=null}">
	                        <div class="panel panel-primary" id="workflowDiv" >
	                        	<%@ include file='../common/commonWorkflowMatrix.jsp'%>
								<%@ include file='../common/commonWorkflowMatrix-button.jsp'%>
							</div>
						</s:if>
						<s:else>
							<div class="row">
								<div class="text-center">
									<button type="submit" id="btnsave" class="btn btn-primary" onclick="return formsubmit();">
										Save</button>
									<button type="button" id="btnclose" class="btn btn-default" onclick="window.close();">
										Close</button>
								</div>
							</div>
						</s:else>
                        </s:push>  
                    </s:form> 
                    <div style="text-align: center;" hidden="true" id="certificateDiv">
							<input type="button" class="btn btn-primary" id="GenerateProvisionalCertificate"
							value="Generate Provisional Certificate"  
							onclick="window.open('/tl/viewtradelicense/viewTradeLicense-generateCertificate.action?model.id=<s:property value="%{id}" />', '_blank', 'height=650,width=980,scrollbars=yes,left=0,top=0,status=yes');"/> &nbsp;
					</div>
                    </div>
                </div>
        <script src="<cdn:url  value='/resources/global/js/egov/inbox.js?rnd=${app_release_no}' context='/egi'/>"></script>
        <script src="<cdn:url  value='/resources/js/app/newtrade.js?rnd=${app_release_no}'/>"></script>
        <script src="<cdn:url  value='/resources/js/app/searchTrade.js?rnd=${app_release_no}'/>"></script>
    </body>
</html>
