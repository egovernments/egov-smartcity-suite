<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<tr>
						<td colspan="5">
							<div class="headingsmallbg">
								<s:text name="transferDtls" />
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
									<s:if test="%{transfereeInfos.size == 0}">
								      <tr id="nameRow" >
								        <td class="blueborderfortd" align="center">
										   <s:textfield name="transfereeInfos[0].aadhaarNumber" size="12" maxlength="12" value=""  data-idx="0" onblur="getAadharDetailsForTransferee(this);" cssClass="txtaadhar"></s:textfield>
										</td>
										 <td class="blueborderfortd" align="center">
								        	<s:textfield name="transfereeInfos[0].mobileNumber" maxlength="10" size="20" id="mobileNumber"  value=""  data-idx="0" data-optional="0" data-errormsg="Mobile no is mandatory!"
								        		onblur="getUserDetailsForMobileNo(this);validNumber(this);checkZero(this,'Mobile Number');"/>
								        	<s:checkbox name="editMobileno[0]" id="editMobileno[0]" onclick="enableMobileNumber(this);" data-idx="0" data-toggle="tooltip" data-placement="top" title="Citizen confirmed that his/her mobile no is changed" />
								        </td>
								        <td class="blueborderfortd" align="center">
								        	<s:textfield name="transfereeInfos[0].name" maxlength="100" size="20" id="ownerName"  value=""  data-optional="0" data-errormsg="Owner name is mandatory!"
								        		onblur="trim(this,this.value);checkSpecialCharForName(this);"/>
								        </td>
								        <td class="blueborderfortd" align="center">
								        	<s:select id="transfereeInfos[0].gender" name="transfereeInfos[0].gender" value="%{transfereeInfos[0].gender}"
								        	  headerValue="Choose" headerKey="" list="@org.egov.infra.persistence.entity.enums.Gender@values()"></s:select>
								        </td>
								        <td class="blueborderfortd" align="center">
								        	<s:textfield name="transfereeInfos[0].emailId" maxlength="64" size="20" id="emailId"  value="" 
								        		onblur="trim(this,this.value);validateEmail(this);"/>
								        	<!-- This hidden field can become dropdown later when transferee become non citizen -->
								        	<s:hidden name="transfereeInfos[0].type" value="CITIZEN" data-static="true"/>
								        </td>
								        <td class="blueborderfortd" align="center">
								        		 <s:select id="transfereeInfos[0].guardianRelation" name="transfereeInfos[0].guardianRelation" value="%{transfereeInfos[0].guardianRelation}"
				                                     headerValue="Choose" headerKey="" list="guardianRelationMap" data-optional="0" data-errormsg="Guardian relation is mandatory!"/>
								        </td>
								         <td class="blueborderfortd" align="center">
								        	<s:textfield name="transfereeInfos[0].guardian" maxlength="100" size="20" 
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
								     <s:iterator value="transfereeInfos" status="status" >
								      <tr id="nameRow" >
								        <td class="blueborderfortd" align="center">
										   <s:textfield name="transfereeInfos[%{#status.index}].aadhaarNumber" data-idx="%{#status.index}" onblur="getAadharDetailsForTransferee(this);" cssClass="txtaadhar" size="12" maxlength="12"></s:textfield>
										</td>
										<td class="blueborderfortd" align="center">
								        	<s:textfield name="transfereeInfos[%{#status.index}].mobileNumber" maxlength="10" size="20" data-optional="0" data-errormsg="Mobile no is mandatory!" data-idx="%{#status.index}"
								        		onblur="getUserDetailsForMobileNo(this);validNumber(this);checkZero(this,'Mobile Number');"/>
								        <s:checkbox name="editMobileno[%{#ownerStatus.index}]" id="editMobileno[%{#ownerStatus.index}]" onclick="enableMobileNumber(this);" data-idx="%{#status.index}" data-toggle="tooltip" data-placement="top" title="Citizen confirmed that his/her mobile no is changed" />
								        </td>
								        <td class="blueborderfortd" align="center">
								        	<s:textfield name="transfereeInfos[%{#status.index}].name" maxlength="100" size="20" data-optional="0" data-errormsg="Owner name is mandatory!"
								        		onblur="trim(this,this.value);checkSpecialCharForName(this);"/>
								        </td>
								        <td class="blueborderfortd" align="center">
								        	<s:select id="transfereeInfos[%{#status.index}].gender" name="transfereeInfos[%{#status.index}].gender" value="%{transfereeInfos[#status.index].gender}"
								        	 headerValue="Choose" headerKey="" list="@org.egov.infra.persistence.entity.enums.Gender@values()"></s:select>
								        </td>
								      
								        <td class="blueborderfortd" align="center">
								        	<s:textfield name="transfereeInfos[%{#status.index}].emailId" maxlength="64" size="20" 
								        		onblur="trim(this,this.value);validateEmail(this);"/>
								        		<!-- This hidden field can become dropdown later when transferee become non citizen -->
								        	<s:hidden name="transfereeInfos[%{#status.index}].type" value="CITIZEN" data-static="true"/>
								        </td>
								        <td class="blueborderfortd" align="center">
								         <s:select id="transfereeInfos[%{#status.index}].guardianRelation" name="transfereeInfos[%{#status.index}].guardianRelation" value="%{transfereeInfos[#status.index].guardianRelation}"
				                                     headerValue="Choose" headerKey="" list="guardianRelationMap" data-optional="0" data-errormsg="Guardian relation is mandatory!"/>
								
								        </td>
								        <td class="blueborderfortd" align="center">
								        	<s:textfield name="transfereeInfos[%{#status.index}].guardian" maxlength="100" size="20"
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
								     </s:iterator>
								     </s:else>
								</table>
							</div>
							<br/>
						</td>
					</tr>
<script type="text/javascript">
          function getAadharDetailsForTransferee(obj) {
	    	   var aadharNo = jQuery(obj).val();
	    	   var rowidx= jQuery(obj).data('idx');
	    	   console.log('calling :) ->'+rowidx + ' ->'+ aadharNo);
	    	   jQuery.ajax({
					type: "GET",
					url: "/egi/aadhaar/"+aadharNo,
					cache: true,
				}).done(function(value) {
					console.log('response received!')
					var userInfoObj = jQuery.parseJSON(value);
					if(userInfoObj.valid == true) {
						jQuery("input[name='transfereeInfos["+ rowidx +"].name']").val(userInfoObj.name);
						jQuery("input[name='transfereeInfos["+ rowidx +"].name']").attr('readonly', true);
						if(userInfoObj.gender == 'M' || userInfoObj.gender == 'Male') {
							jQuery("select[name='transfereeInfos["+ rowidx +"].gender']").val("MALE");
						} else if (userInfoObj.gender == 'F' || userInfoObj.gender == 'Female') {
							jQuery("select[name='transfereeInfos["+ rowidx +"].gender']").val("FEMALE");
						} else {
							jQuery("select[name='transfereeInfos["+ rowidx +"].gender']").val("OTHERS");
						} 
						jQuery("select[name='transfereeInfos["+ rowidx +"].gender']").attr('disabled','disabled');
						jQuery("input[name='transfereeInfos["+ rowidx +"].mobileNumber']").val(userInfoObj.phone);
						jQuery("input[name='transfereeInfos["+ rowidx +"].mobileNumber']").attr('readonly', true);
						jQuery("input[name='transfereeInfos["+ rowidx +"].emailId").attr('readonly', true);
						jQuery("select[name='transfereeInfos["+ rowidx +"].guardianRelation']").attr('disabled', 'disabled');
						jQuery("input[name='transfereeInfos["+ rowidx +"].guardian']").val(userInfoObj.careof);
						jQuery("input[name='transfereeInfos["+ rowidx +"].guardian']").attr('readonly', true);
					} else if(userInfoObj.valid == false) {
						jQuery("input[name='transfereeInfos["+ rowidx +"].aadhaarNumber']").val("");
						jQuery("input[name='transfereeInfos["+ rowidx +"].name']").val("");
						jQuery("input[name='transfereeInfos["+ rowidx +"].name']").attr('readonly', false);
						jQuery("select[name='transfereeInfos["+ rowidx +"].gender").removeAttr('disabled');
						jQuery("select[name='transfereeInfos["+ rowidx +"].gender']").val("");
						jQuery("input[name='transfereeInfos["+ rowidx +"].mobileNumber']").val("").attr('readonly', false);
						//jQuery("input[name='transfereeInfos["+ rowidx +"].mobileNumber']").attr('readonly', true);
						jQuery("input[name='transfereeInfos["+ rowidx +"].emailId").attr('readonly', false);
						jQuery("select[name='transfereeInfos["+ rowidx +"].guardianRelation']").removeAttr('disabled');
						jQuery("input[name='transfereeInfos["+ rowidx +"].guardian']").attr('readonly', false);
						if(aadharNo != "NaN") {
						bootbox.alert("Aadhar number is not valid");
						}
				   }
				});
	       }

          function getUserDetailsForMobileNo(obj) {
       	   var mobileNo = jQuery(obj).val();
       	   var rowidx= jQuery(obj).data('idx');
       	   console.log('calling :) ->'+rowidx + ' ->'+ mobileNo);
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
   					jQuery("input[name='transfereeInfos["+ rowidx +"].name']").val(response.name);
   					jQuery("select[name='transfereeInfos["+ rowidx +"].gender']").val(response.gender);
   					jQuery("input[name='transfereeInfos["+ rowidx +"].mobileNumber']").val(response.mobileNumber);
   					jQuery("input[name='transfereeInfos["+ rowidx +"].emailId").val(response.email);
   					jQuery("select[name='transfereeInfos["+ rowidx +"].guardianRelation']").val(response.guardianRelarion);
   					jQuery("input[name='transfereeInfos["+ rowidx +"].guardian']").val(response.guardian);
   			    }
              });
          }
        }

          function enableMobileNumber(obj) { 
       	   var rowidx= jQuery(obj).data('idx');
       	   if(obj.checked == true) {
       		   jQuery("input[name='transfereeInfos["+ rowidx +"].mobileNumber']").attr('readonly', false);
           	   } else {
           		jQuery("input[name='transfereeInfos["+ rowidx +"].mobileNumber']").attr('readonly', true);
               }
            }
	</script>