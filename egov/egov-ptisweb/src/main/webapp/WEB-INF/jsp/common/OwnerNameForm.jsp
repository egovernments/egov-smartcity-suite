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

<%@ include file="/includes/taglibs.jsp" %>
    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom" id="nameTable" >
    <tr>
    <th class="bluebgheadtd"><s:text name="adharno"/></th>
    <th class="bluebgheadtd"><s:text name="MobileNumber" /> <span class="mandatory1">*</span></th>
    <th class="bluebgheadtd"><s:text name="OwnerName"/><span class="mandatory1">*</span></th>
    <th class="bluebgheadtd"><s:text name="gender"/><span class="mandatory1">*</span></th>
	<th class="bluebgheadtd"><s:text name="EmailAddress"/></th>
	<th class="bluebgheadtd"><s:text name="GuardianRelation"/><span class="mandatory1">*</span></th>
	<th class="bluebgheadtd"><s:text name="Guardian"/><span class="mandatory1">*</span></th>
	<th class="bluebgheadtd"><s:text name="Add/Delete" /></th>
    </tr>
    <s:if test="%{basicProperty.propertyOwnerInfoProxy.size == 0}">
      <tr id="nameRow" >
      <s:hidden name="basicProperty.propertyOwnerInfoProxy[0].owner.type" id="basicProperty.propertyOwnerInfoProxy[0].owner.type"
       value="%{basicProperty.propertyOwnerInfoProxy[0].owner.type}"></s:hidden>
        <td class="blueborderfortd" align="center">
		   <s:textfield name="basicProperty.propertyOwnerInfoProxy[0].owner.aadhaarNumber" value="%{basicProperty.propertyOwnerInfoProxy[0].owner.aadhaarNumber}" id="aadharNo" cssClass="txtaadhar" size="12" maxlength="12"  data-idx="0" onblur="getAadharDetails(this);"></s:textfield>
		</td>
		 <td class="blueborderfortd" align="center">
        	+91 <s:textfield name="basicProperty.propertyOwnerInfoProxy[0].owner.mobileNumber" title="Mobile number of the owner" maxlength="10" size="20" id="mobileNumber"  value="%{basicProperty.propertyOwnerInfoProxy[0].owner.mobileNumber}" 
        		onblur="getUserDetailsForMobileNo(this);validNumber(this);checkZero(this,'Mobile Number');" data-idx="0" data-optional="0" data-errormsg="Mobile no is mandatory!"/>
        		<s:checkbox name="editMobileno[0]" class="mobilecheckbox" id="editMobileno[0]" onclick="enableMobileNumber(this);" data-idx="0" data-toggle="tooltip" data-placement="top" title="Citizen confirmed that his/her mobile no is changed" />
		<td class="blueborderfortd" align="center">
        	<s:textfield name="basicProperty.propertyOwnerInfoProxy[0].owner.name" title="Owner of the Property" maxlength="74" size="20" id="ownerName"  value="%{basicProperty.propertyOwnerInfoProxy[0].owner.name}" 
        		onblur="trim(this,this.value);checkSpecialCharForName(this);" data-optional="0" data-errormsg="Owner name is mandatory!"/>
        </td>
        <td class="blueborderfortd" align="center"><s:select id="gender" name="basicProperty.propertyOwnerInfoProxy[0].owner.gender" value="%{basicProperty.propertyOwnerInfoProxy[0].owner.gender}"
				headerValue="Choose" headerKey="" list="@org.egov.infra.persistence.entity.enums.Gender@values()" cssClass="selectwk" data-optional="0" data-errormsg="Gender is mandatory!">
		</s:select></td>
        <td class="blueborderfortd" align="center">
        	<s:textfield name="basicProperty.propertyOwnerInfoProxy[0].owner.emailId" maxlength="32" size="20" id="emailId"  value="%{basicProperty.propertyOwnerInfoProxy[0].owner.emailId}" 
        		onblur="trim(this,this.value);validateEmail(this);"/>
        </td>
        <td class="blueborderfortd" align="center">
            <s:select id="guardianRelation" name="basicProperty.propertyOwnerInfoProxy[0].owner.guardianRelation" value="%{basicProperty.propertyOwnerInfoProxy[0].owner.guardianRelation}"
				 headerValue="Choose" headerKey="" list="guardianRelations" cssClass="selectwk" data-optional="0" data-errormsg="Guardian relation is mandatory!"/>
		</td>
         <td class="blueborderfortd" align="center">
        	<s:textfield name="basicProperty.propertyOwnerInfoProxy[0].owner.guardian" maxlength="32" size="20" id="guardian"  value="%{basicProperty.propertyOwnerInfoProxy[0].owner.guardian}" 
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
        <s:iterator value="(basicProperty.propertyOwnerInfoProxy.size).{#this}" status="ownerStatus">
			<tr id="nameRow">
			 <s:hidden name="basicProperty.propertyOwnerInfoProxy[%{#ownerStatus.index}].owner.type" id="basicProperty.propertyOwnerInfoProxy[%{#ownerStatus.index}].owner.type"
                       value="%{basicProperty.propertyOwnerInfoProxy[#ownerStatus.index].owner.type}"></s:hidden>
			  <td class="blueborderfortd" align="center">
			  <s:textfield name="basicProperty.propertyOwnerInfoProxy[%{#ownerStatus.index}].owner.aadhaarNumber" id="aadharNo" size="12" maxlength="12" data-optional="1" data-errormsg="Aadhar no is mandatory!"
			  value="%{basicProperty.propertyOwnerInfoProxy[#ownerStatus.index].owner.aadhaarNumber}" data-idx="%{#ownerStatus.index}" onblur="getAadharDetails(this);" cssClass="txtaadhar"></s:textfield>
			  </td>
			  <td class="blueborderfortd" align="center">
        			+91 <s:textfield name="basicProperty.propertyOwnerInfoProxy[%{#ownerStatus.index}].owner.mobileNumber" maxlength="10" size="20" id="mobileNumber" value="%{basicProperty.propertyOwnerInfoProxy[#ownerStatus.index].owner.mobileNumber}" 
        				onblur="getUserDetailsForMobileNo(this);validNumber(this);checkZero(this,'Mobile Number');" data-idx="%{#ownerStatus.index}" data-optional="0" data-errormsg="Mobile no is mandatory!" />
        			<s:checkbox name="editMobileno[%{#ownerStatus.index}]" id="editMobileno[%{#ownerStatus.index}]" onclick="enableMobileNumber(this);" data-idx="%{#ownerStatus.index}" data-toggle="tooltip" data-placement="top" title="Citizen confirmed that his/her mobile no is changed" />
        		</td>
        		<td class="blueborderfortd" align="center">
        			<s:textfield name="basicProperty.propertyOwnerInfoProxy[%{#ownerStatus.index}].owner.name" maxlength="74" size="20" id="ownerName" value="%{basicProperty.propertyOwnerInfoProxy[#ownerStatus.index].owner.name}" 
        				onblur="trim(this,this.value);checkSpecialCharForName(this);" data-optional="0" data-errormsg="Owner name is mandatory!"/>
        		</td>
        		<td class="blueborderfortd" align="center"><s:select id="gender" name="basicProperty.propertyOwnerInfoProxy[%{#ownerStatus.index}].owner.gender" value="%{basicProperty.propertyOwnerInfoProxy[#ownerStatus.index].owner.gender}"
				headerValue="Choose" headerKey="" list="@org.egov.infra.persistence.entity.enums.Gender@values()" data-optional="0" data-errormsg="Gender is mandatory!">
		       </s:select></td>
        		<td class="blueborderfortd" align="center">
        			<s:textfield name="basicProperty.propertyOwnerInfoProxy[%{#ownerStatus.index}].owner.emailId" maxlength="32" size="20" id="emailId" value="%{basicProperty.propertyOwnerInfoProxy[#ownerStatus.index].owner.emailId}" 
        				onblur="trim(this,this.value);validateEmail(this);"/>
        		</td>
        		<td class="blueborderfortd" align="center">
        		    <s:select id="guardianRelation" name="basicProperty.propertyOwnerInfoProxy[%{#ownerStatus.index}].owner.guardianRelation" value="%{basicProperty.propertyOwnerInfoProxy[#ownerStatus.index].owner.guardianRelation}"
				headerValue="Choose" headerKey="" list="guardianRelations" data-optional="0" data-errormsg="Guardian relation is mandatory!"/>
        	    </td>
        		<td class="blueborderfortd" align="center">
        	        <s:textfield name="basicProperty.propertyOwnerInfoProxy[%{#ownerStatus.index}].owner.guardian" maxlength="32" size="20" id="guardian"  value="%{basicProperty.propertyOwnerInfoProxy[#ownerStatus.index].owner.guardian}" 
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
       <script>
       function getAadharDetails(obj) {
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
    					jQuery("input[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.name']").val(userInfoObj.name);
    					jQuery("input[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.name']").attr('readonly', true);
    					if(userInfoObj.gender == 'M' || userInfoObj.gender == 'Male') {
    						jQuery("select[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.gender']").val("MALE");
    					} else if (userInfoObj.gender == 'F' || userInfoObj.gender == 'Female') {
    						jQuery("select[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.gender']").val("FEMALE");
    					} else {
    						jQuery("select[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.gender']").val("OTHERS");
    					} 
    					jQuery("select[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.gender']").attr('disabled','disabled');
    					jQuery("input[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.mobileNumber']").val(userInfoObj.phone);
    					jQuery("input[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.mobileNumber']").attr('readonly', true);
    					jQuery("input[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.emailId']").attr('readonly', true);
    					jQuery("select[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.guardianRelation']").removeAttr('disabled');
    					jQuery("input[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.guardian']").val(userInfoObj.careof);
    					jQuery("input[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.guardian']").attr('readonly', true);
    				} else {
    					jQuery("input[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.aadhaarNumber']").val("");
    					jQuery("input[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.name']").val("");
    					jQuery("input[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.name']").attr('readonly', false);
    					jQuery("select[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.gender']").removeAttr('disabled');
    					jQuery("select[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.gender']").val("");
    					jQuery("input[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.mobileNumber']").val("").attr('readonly', false);
    					jQuery("input[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.emailId']").attr('readonly', false);
    					jQuery("select[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.guardianRelation']").removeAttr('disabled');
    					jQuery("input[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.guardian']").val("");
    					jQuery("input[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.guardian']").attr('readonly', false);
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
    	        	jQuery("input[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.aadhaarNumber']").val("");
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
				data:{"mobileNumber" : mobileNo}
			}).done(function(response) {
				if(response.exists) {
					jQuery("input[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.name']").val(response.name);
					jQuery("select[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.gender']").val(response.gender);
					jQuery("input[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.mobileNumber']").val(response.mobileNumber);
					jQuery("input[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.emailId']").val(response.email);
					jQuery("select[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.guardianRelation']").val(response.guardianRelarion);
					jQuery("input[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.guardian']").val(response.guardian);
			    }
           });
       }
      }

       function enableMobileNumber(obj) { 
    	   var rowidx= jQuery(obj).data('idx');
    	   if(obj.checked == true) {
    		   jQuery("input[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.mobileNumber']").attr('readonly', false);
        	 } else {
        	   jQuery("input[name='basicProperty.propertyOwnerInfoProxy["+ rowidx +"].owner.mobileNumber']").attr('readonly', true);
             }
          }
      </script>
