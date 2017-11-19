<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
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
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
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
  ~
  --%>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<tr>
						<td colspan="5">
							<div class="headingsmallbg">
								<span class="bold"><s:text name="transferDtls" /></span>
							</div>
						</td>
					</tr>

					<tr>
						<td colspan="5">
							<div>
								<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" id="nameTable" >
								    <tr>
								    	<th class="bluebgheadtd"><s:text name="adharno"/></th>
								    	<th class="bluebgheadtd"><s:text name="MobileNumber" />(without +91)<span class="mandatory1">*</span></th>
										<th class="bluebgheadtd"><s:text name="OwnerName"/><span class="mandatory1">*</span></th>
										<th class="bluebgheadtd"><s:text name="gender"/><span class="mandatory1">*</span></th>
										<th class="bluebgheadtd"><s:text name="EmailAddress"/></th>
										<th class="bluebgheadtd"><s:text name="GuardianRelation"/><span class="mandatory1">*</span></th>
										<th class="bluebgheadtd"><s:text name="Guardian"/><span class="mandatory1">*</span></th>
										<th class="bluebgheadtd">Add/Delete</th>
									</tr>
									<s:if test="%{transfereeInfosProxy.size == 0}">
								      <tr id="nameRow" >
								        <td class="blueborderfortd" align="center">
										   <s:textfield name="transfereeInfosProxy[0].transferee.aadhaarNumber" size="12" maxlength="12" value=""  data-idx="0" onblur="getAadharDetailsForTransferee(this);" cssClass="txtaadhar"></s:textfield>
										</td>
										 <td class="blueborderfortd" align="center">
								        	<s:textfield name="transfereeInfosProxy[0].transferee.mobileNumber" maxlength="10" size="20" id="mobileNumber"  value=""  data-idx="0" data-optional="0" data-errormsg="Mobile no is mandatory!"
								        		onblur="getUserDetailsForMobileNo(this);validNumber(this);checkZero(this,'Mobile Number');"/>
								        	<s:checkbox name="editMobileno[0]" id="editMobileno[0]" onclick="enableMobileNumber(this);" data-idx="0" data-toggle="tooltip" data-placement="top" title="Citizen confirmed that his/her mobile no is changed" />
								        </td>
								        <td class="blueborderfortd" align="center">
								        	<s:textfield name="transfereeInfosProxy[0].transferee.name" maxlength="100" size="20" id="ownerName"  value=""  data-optional="0" data-errormsg="Owner name is mandatory!"
								        		onblur="trim(this,this.value);checkSpecialCharForName(this);"/>
								        </td>
								        <td class="blueborderfortd" align="center">
								        	<s:select id="transfereeInfosProxy[0].gender" name="transfereeInfosProxy[0].transferee.gender" value="%{transfereeInfosProxy[0].transferee.gender}"
								        	  headerValue="Choose" headerKey="" list="@org.egov.infra.persistence.entity.enums.Gender@values()"></s:select>
								        </td>
								        <td class="blueborderfortd" align="center">
								        	<s:textfield name="transfereeInfosProxy[0].transferee.emailId" maxlength="64" size="20" id="emailId"  value="" 
								        		onblur="trim(this,this.value);validateEmail(this);"/>
								        	<!-- This hidden field can become dropdown later when transferee become non citizen -->
								        	<s:hidden name="transfereeInfosProxy[0].transferee.type" value="CITIZEN" data-static="true"/>
								        </td>
								        <td class="blueborderfortd" align="center">
								        		 <s:select id="transfereeInfosProxy[0].transferee.guardianRelation" name="transfereeInfosProxy[0].transferee.guardianRelation" value="%{transfereeInfosProxy[0].transferee.guardianRelation}"
				                                     headerValue="Choose" headerKey="" list="guardianRelations" data-optional="0" data-errormsg="Guardian relation is mandatory!"/>
								        </td>
								         <td class="blueborderfortd" align="center">
								        	<s:textfield name="transfereeInfosProxy[0].transferee.guardian" maxlength="100" size="20" 
								        		onblur="trim(this,this.value);checkSpecialCharForName(this);" data-optional="0" data-errormsg="Guardian name is mandatory!"/>
								        </td>
								        <td class="blueborderfortd">
								        	 <span id="addOwnerBtn" name="addOwnerBtn" class="tblactionicon add" alt="addOwnerBtn"
												    onclick="javascript:addOwner(); return false;" >
												       <i class="fa fa-plus-circle"></i>
											  </span>
											  &nbsp;
											  <span id="removeOwnerBtn" name="removeOwnerBtn" class="tblactionicon delete" alt="removeOwnerBtn"
											        onclick="javascript:deleteOwner(this); return false;">
											           <i class="fa fa-minus-circle"></i>
											  </span>
								        </td>
								     </tr>
								     </s:if>
								     <s:else>
								     <s:iterator value="(transfereeInfosProxy.size).{#this}" status="status" >
								      <tr id="nameRow" >
								        <td class="blueborderfortd" align="center">
										   <s:textfield name="transfereeInfosProxy[%{#status.index}].transferee.aadhaarNumber" data-idx="%{#status.index}"  value="%{transfereeInfosProxy[#status.index].transferee.aadhaarNumber}"
										   onblur="getAadharDetailsForTransferee(this);" cssClass="txtaadhar" size="12" maxlength="12"></s:textfield>
										</td>
										<td class="blueborderfortd" align="center">
								        	<s:textfield name="transfereeInfosProxy[%{#status.index}].transferee.mobileNumber" maxlength="10" size="20" data-optional="0" data-errormsg="Mobile no is mandatory!" data-idx="%{#status.index}"
								        		 value="%{transfereeInfosProxy[#status.index].transferee.mobileNumber}" onblur="getUserDetailsForMobileNo(this);validNumber(this);checkZero(this,'Mobile Number');"/>
								        <s:checkbox name="editMobileno[%{#status.index}]" id="editMobileno[%{#status.index}]" onclick="enableMobileNumber(this);" data-idx="%{#status.index}" data-toggle="tooltip" data-placement="top" title="Citizen confirmed that his/her mobile no is changed" />
								        </td>
								        <td class="blueborderfortd" align="center">
								        	<s:textfield name="transfereeInfosProxy[%{#status.index}].transferee.name" maxlength="100" size="20" data-optional="0" data-errormsg="Owner name is mandatory!"
								        		value="%{transfereeInfosProxy[#status.index].transferee.name}" onblur="trim(this,this.value);checkSpecialCharForName(this);"/>
								        </td>
								        <td class="blueborderfortd" align="center">
								        	<s:select id="transfereeInfosProxy[%{#status.index}].transferee.gender" name="transfereeInfosProxy[%{#status.index}].transferee.gender" value="%{transfereeInfosProxy[#status.index].transferee.gender}"
								        	 headerValue="Choose" headerKey="" list="@org.egov.infra.persistence.entity.enums.Gender@values()"></s:select>
								        </td>  
								      
								        <td class="blueborderfortd" align="center">
								        	<s:textfield name="transfereeInfosProxy[%{#status.index}].transferee.emailId" maxlength="64" size="20" 
								        		value="%{transfereeInfosProxy[#status.index].transferee.emailId}" onblur="trim(this,this.value);validateEmail(this);"/>
								        		<!-- This hidden field can become dropdown later when transferee become non citizen -->
								        	<s:hidden name="transfereeInfosProxy[%{#status.index}].transferee.type" value="CITIZEN" data-static="true"/>
								        </td>
								        <td class="blueborderfortd" align="center">
								         <s:select id="transfereeInfosProxy[%{#status.index}].transferee.guardianRelation" name="transfereeInfosProxy[%{#status.index}].transferee.guardianRelation" value="%{transfereeInfosProxy[#status.index].transferee.guardianRelation}"
				                              headerValue="Choose" headerKey="" list="guardianRelations" data-optional="0" data-errormsg="Guardian relation is mandatory!"/>
								
								        </td>
								        <td class="blueborderfortd" align="center">
								        	<s:textfield name="transfereeInfosProxy[%{#status.index}].transferee.guardian" maxlength="100" size="20"
								        		  value="%{transfereeInfosProxy[#status.index].transferee.guardian}"  onblur="trim(this,this.value);checkSpecialCharForName(this);" data-optional="0" data-errormsg="Guardian name is mandatory!"/>
								        </td>
								        
								        <td class="blueborderfortd">
								        	 <span id="addOwnerBtn" name="addOwnerBtn" class="tblactionicon add" alt="addOwnerBtn"
												    onclick="javascript:addOwner(); return false;" >
												       <i class="fa fa-plus-circle"></i>
											  </span>
											  &nbsp;
											  <span id="removeOwnerBtn" name="removeOwnerBtn" class="tblactionicon delete" alt="removeOwnerBtn"
											        onclick="javascript:deleteOwner(this); return false;">
											           <i class="fa fa-minus-circle"></i>
											  </span>
								        </td> 
								     </tr>
								     </s:iterator>
								     </s:else>
								</table>
							</div>
							<br/>
						</td>
					</tr>
<script type="text/javascript">
          function getAadharDetailsForTransferee(obj) {
        	  var rowidx=jQuery(obj).data('idx');
      		var aadharNo = jQuery(obj).val();
      		if(aadharNo.length<12){
      			jQuery(obj).val("");
      			bootbox.alert("Invalid Aadhar number !");
      			return false;
      		}
      	    jQuery.ajax({
      	    	url: "/egi/aadhaar/"+aadharNo,
      	        type: "GET",
      	        beforeSend:function()
      	        {
      	        	jQuery('.loader-class').modal('show', {backdrop: 'static'});
      	        },
      	        success: function(response){
      	        	var userInfoObj = jQuery.parseJSON(response);
      				if(userInfoObj.uid == aadharNo) {
      					jQuery("input[name='transfereeInfosProxy["+ rowidx +"].transferee.name']").val(userInfoObj.name);
      					jQuery("input[name='transfereeInfosProxy["+ rowidx +"].transferee.name']").attr('readonly', true);
      					if(userInfoObj.gender == 'M' || userInfoObj.gender == 'Male') {
      						jQuery("select[name='transfereeInfosProxy["+ rowidx +"].transferee.gender']").val("MALE");
      					} else if (userInfoObj.gender == 'F' || userInfoObj.gender == 'Female') {
      						jQuery("select[name='transfereeInfosProxy["+ rowidx +"].transferee.gender']").val("FEMALE");
      					} else {
      						jQuery("select[name='transfereeInfosProxy["+ rowidx +"].transferee.gender']").val("OTHERS");
      					} 
      					jQuery("select[name='transfereeInfosProxy["+ rowidx +"].transferee.gender']").attr('disabled','disabled');
      					jQuery("input[name='transfereeInfosProxy["+ rowidx +"].transferee.mobileNumber']").val(userInfoObj.phone);
      					jQuery("input[name='transfereeInfosProxy["+ rowidx +"].transferee.mobileNumber']").attr('readonly', true);
      					jQuery("input[name='transfereeInfosProxy["+ rowidx +"].transferee.emailId']").attr('readonly', true);
      					jQuery("select[name='transfereeInfosProxy["+ rowidx +"].transferee.guardianRelation']").removeAttr('disabled');
      					jQuery("input[name='transfereeInfosProxy["+ rowidx +"].transferee.guardian']").val(userInfoObj.careof);
      					jQuery("input[name='transfereeInfosProxy["+ rowidx +"].transferee.guardian']").attr('readonly', true);
      				} else {
      					jQuery("input[name='transfereeInfosProxy["+ rowidx +"].transferee.aadhaarNumber']").val("");
      					jQuery("input[name='transfereeInfosProxy["+ rowidx +"].transferee.name']").val("");
      					jQuery("input[name='transfereeInfosProxy["+ rowidx +"].transferee.name']").attr('readonly', false);
      					jQuery("select[name='transfereeInfosProxy["+ rowidx +"].transferee.gender']").removeAttr('disabled');
      					jQuery("select[name='transfereeInfosProxy["+ rowidx +"].transferee.gender']").val("");
      					jQuery("input[name='transfereeInfosProxy["+ rowidx +"].transferee.mobileNumber']").val("").attr('readonly', false);
      					jQuery("input[name='transfereeInfosProxy["+ rowidx +"].transferee.emailId']").attr('readonly', false);
      					jQuery("select[name='transfereeInfosProxy["+ rowidx +"].transferee.guardianRelation']").removeAttr('disabled');
      					jQuery("input[name='transfereeInfosProxy["+ rowidx +"].transferee.guardian']").val("");
      					jQuery("input[name='transfereeInfosProxy["+ rowidx +"].transferee.guardian']").attr('readonly', false);
      					if(aadharNo != "NaN") {
      						bootbox.alert("Aadhar number is not valid");
      					}
      			   }
      	        	
      	        },
      	        complete:function()
      	        {
      	        	jQuery('.loader-class').modal('hide');
      	        },
      	        error:function()
      	        {
      	        	jQuery("input[name='transfereeInfosProxy["+ rowidx +"].transferee.aadhaarNumber']").val("");
      	        	alert("Invalid Aadhar number or no details available with this Aadhar number!");
      	        	return false;
      	        }
      	    });
	       }

          function getUserDetailsForMobileNo(obj) {
       	   var mobileNo = jQuery(obj).val();
       	   var rowidx= jQuery(obj).data('idx');
        if(jQuery("input[name='editMobileno["+ rowidx +"]']").is(':checked') ==  true) {
        	jQuery("input[name='editMobileno["+ rowidx +"]']").prop('checked', false);
        } else {
               jQuery.ajax({
   				type: "GET",
   				url: "/ptis/common/ajaxCommon-getUserByMobileNo.action",
   				cache: true,
   				dataType: "json",
   				data:{"mobileNumber" : mobileNo},
   			}).done(function(response) {
   				if(response.exists) {
   					jQuery("input[name='transfereeInfosProxy["+ rowidx +"].transferee.name']").val(response.name);
   					jQuery("select[name='transfereeInfosProxy["+ rowidx +"].transferee.gender']").val(response.gender);
   					jQuery("input[name='transfereeInfosProxy["+ rowidx +"].transferee.mobileNumber']").val(response.mobileNumber);
   					jQuery("input[name='transfereeInfosProxy["+ rowidx +"].transferee.emailId']").val(response.email);
   					jQuery("select[name='transfereeInfosProxy["+ rowidx +"].transferee.guardianRelation']").val(response.guardianRelarion);
   					jQuery("input[name='transfereeInfosProxy["+ rowidx +"].transferee.guardian']").val(response.guardian);
   			    }
              });
          }
        }

          function enableMobileNumber(obj) { 
       	   var rowidx= jQuery(obj).data('idx');
       	   if(obj.checked == true) {
       		   jQuery("input[name='transfereeInfosProxy["+ rowidx +"].transferee.mobileNumber']").attr('readonly', false);
           	   } else {
           		jQuery("input[name='transfereeInfosProxy["+ rowidx +"].transferee.mobileNumber']").attr('readonly', true); 
               }
            }   
	</script>