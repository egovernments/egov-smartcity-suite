<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
   <tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%">Application No <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width="">
		   <s:textfield name="applicationno"
				id="applicationno" size="12" maxlength="12"></s:textfield>
		</td>
		<td class="greybox" width="25%">Application Date
			<span class="mandatory1" id="prntMandatory">*</span> :</td>
		<td class="greybox"><s:textfield name="applicationdt"
				id="applicationdt" size="10" maxlength="10"></s:textfield>
		</td>
	</tr>

	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="rsnForCreatin" /> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="mutationId"
				id="mutationId" listKey="idMutation" listValue="mutationName"
				list="dropdownData.MutationList" value="%{mutationId}"
				cssClass="selectnew" onchange="makeMandatory();" />
		</td>
		<td class="greybox" width="25%"><s:text name="prntPropIndexNum" />
			<span class="mandatory1" id="prntMandatory">*</span> :</td>
		<td class="greybox"><s:textfield name="parentIndex"
				id="parentIndex" size="12" maxlength="12"></s:textfield>
		</td>

	</tr>
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%">Extent of Site <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width="">
		   <s:textfield name="extstate" id="extstate" size="12" maxlength="12"></s:textfield>
		</td>
		<td class="greybox" width="25%">Vaccant Land Asmt No
			<span class="mandatory1" id="prntMandatory">*</span> :</td>
		<td class="greybox">
		  <s:textfield name="vaccantasmtno" id="vaccantasmtno" size="12" maxlength="12"></s:textfield>
		</td>

	</tr>
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%">Extend of Appurtenant Land <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width="">
		   <s:textfield name="extapptland" id="extapptland" size="12" maxlength="12"></s:textfield>
		</td>
		<td class="greybox" width="25%">Building plan Approval :</td>
		<td class="greybox">
		  <s:checkbox name="cbbuildplanapr" id="cbbuildplanapr" />
		</td>

	</tr>
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%">Building Permission no :</td>
		<td class="greybox" width="">
		   <s:textfield name="buildpermno" id="buildpermno" size="12" maxlength="12"></s:textfield>
		</td>
		<td class="greybox" width="25%">Building Permission Date :</td>
		<td class="greybox">
		  <s:textfield name="buildpermdate" id="buildpermdate" size="12" maxlength="12"></s:textfield>
		</td>

	</tr>
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%">Regd.Doc No :</td>
		<td class="greybox" width="">
		   <s:textfield name="regdocno" id="regdocno" size="12" maxlength="12"></s:textfield>
		</td>
		<td class="greybox" width="25%">Regd.Doc Date :</td>
		<td class="greybox">
		  <s:textfield name="regdocdate" id="regdocdate" size="12" maxlength="12"></s:textfield>
		</td>
	</tr>
	
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%">Construction Completed Date :</td>
		<td class="greybox" width="">
		   <s:textfield name="concompletedt" id="concompletedt" size="10" maxlength="10"></s:textfield>
		</td>
	</tr>
	
	<!-- Owner details section -->
	
	<tr>
		<td colspan="5"><div class="headingsmallbg"><span class="bold">Owner Details</span></div></td>
	</tr>
	
	<tr>
		<td colspan="5">
			<div id="OwnerNameDiv">
				<%@ include file="../common/OwnerNameForm.jsp"%>
			</div>
		</td>
	</tr>
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%">Aadhar No :</td>
		<td class="greybox" width="">
		   <s:textfield name="aadharno" id="aadharno" size="12" maxlength="12"></s:textfield>
		</td>
		<td class="greybox" width="25%">Father/Husband Name :</td>
		<td class="greybox">
		  <s:textfield name="guardname" id="guardname" maxlength="30"></s:textfield>
		</td>
	</tr>
	
	<tr>
		<td class="bluebox2">&nbsp;</td>
		<td class="bluebox"><s:text name="MobileNumber" /> : <span style="
    float: right;
">+91</span></td>
		<td class="bluebox">
				<s:textfield name="mobileNo" maxlength="10"
					onblur="validNumber(this);checkZero(this,'Mobile Number');" />
		</td>
		<td class="bluebox"><s:text name="EmailAddress" /> :</td>
		<td class="bluebox"><s:textfield name="email" maxlength="64"
				onblur="trim(this,this.value);validateEmail(this);" />
		</td>
	</tr>
	
	
	<!-- property address section -->
	
	<tr>
		<td>
			<div id="PropAddrDiv">
				<%@ include file="../common/PropAddressForm.jsp"%>
			</div>
		</td>
	</tr>
	
	<tr>
		<td>
			<div id="CorrAddrDiv">
				<%@ include file="../common/CorrAddressForm.jsp"%>
			</div>
		</td>
	</tr>
	
	
	<!-- Amenities section -->
	
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"> Amenities </span>
			</div>
		</td>
	</tr>
	
	<tr>
		<td colspan="5">
		  <table width="100%">
	        <tr>
	            <td width="10%"></td>
				<td width="20%" align="right">
				  <label> Lift <s:checkbox name="cblift" id="cblift"/></label>
				  <br/> <label> Drainage <s:checkbox name="cbdrainage" id="cbdrainage"/></label>
				  <br/> <label>Cable Connection <s:checkbox name="cbcablecon" id="cbcablecon"/></label> 
				</td>
				<td width="20%" align="right">
				  <label>Toilets <s:checkbox name="cbtoilet" id="cbtoilet"/> </label>
				  <br/> <label>Electricity <s:checkbox name="cbelectric" id="cbelectric"/> </label>
				</td>
				<td width="20%" align="right">
				  <label>Water Tap <s:checkbox name="cbwtap" id="cbwtap"/></label> 
				   <br/> <label>Attached Bathroom <s:checkbox name="cbattachbath" id="cbattachbath"/> </label>
			    </td>
			    <td width="20%" align="right">
				  <label>Super Structure <s:checkbox name="cbsupstru" id="cbsupstru"/></label>
				  <br/>  <label>Water Harvesting <s:checkbox name="cbwaterharve" id="cbwaterharve"/></label>
			    </td>
			    <td width="10%"></td>
			</tr>
			
		   </table>
		</td>
	</tr>
	
	<!-- Floor type details -->
	
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold">Floor Type Details</span>
			</div>
		</td>
	</tr>
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%">Floor Type <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="floortype"
				id="floortype" listKey="idMutation" listValue="mutationName"
				list="dropdownData.MutationList" value="%{mutationId}"
				cssClass="selectnew" onchange="makeMandatory();" />
		</td>
		<td class="greybox" width="25%">Roof Type <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="rooftype"
				id="rooftype" listKey="idMutation" listValue="mutationName"
				list="dropdownData.MutationList" value="%{mutationId}"
				cssClass="selectnew" onchange="makeMandatory();" />
		</td>
	</tr>
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%">Wall Type <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="walltype"
				id="walltype" listKey="idMutation" listValue="mutationName"
				list="dropdownData.MutationList" value="%{mutationId}"
				cssClass="selectnew" onchange="makeMandatory();" />
		</td>
		<td class="greybox" width="25%">Wood Type <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="woodtype"
				id="woodtype" listKey="idMutation" listValue="mutationName"
				list="dropdownData.MutationList" value="%{mutationId}"
				cssClass="selectnew" onchange="makeMandatory();" />
		</td>
	</tr>
	
	
	<!-- Ownership section -->
	
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold">Ownership Details</span>
			</div>
		</td>
	</tr>
	
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%">Ownership Type <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="sbownershiptype"
				id="sbownershiptype" listKey="idMutation" listValue="mutationName"
				list="dropdownData.MutationList" value="%{mutationId}"
				cssClass="selectnew" onchange="makeMandatory();" />
		</td>
		
	</tr>
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%">Service Charges :</td>
		<td class="greybox" width="">
		  <s:checkbox name="cbservicecharge" id="cbservicecharge"/> 
		</td>
		<td class="greybox" width="25%">Service Charges Category :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="sbservchargetype"
				id="sbservchargetype" listKey="idMutation" listValue="mutationName"
				list="dropdownData.MutationList" value="%{mutationId}"
				cssClass="selectnew" onchange="makeMandatory();" />
		</td>
	</tr>
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%">Apartment/Complex :</td>
		<td class="greybox" width="">
		  <s:checkbox name="cbapartcomplex" id="cbapartcomplex"/> 
		</td>
		<td class="greybox" width="25%">Apartment/Complex Name :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="sbapartcomplex"
				id="sbapartcomplex" listKey="idMutation" listValue="mutationName"
				list="dropdownData.MutationList" value="%{mutationId}"
				cssClass="selectnew" onchange="makeMandatory();" />
		</td>
	</tr>
	
	
	<!-- property type section -->
	
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"><s:text name="PropertyType" /> </span>
			</div>
		</td>
	</tr>
	<tr>
		<td class="bluebox2">&nbsp;</td>
		<td class="bluebox"><s:text name="PropertyType" /> <span
			class="mandatory1">*</span> :</td>
		<td class="bluebox"><s:select name="propTypeId"
				id="propTypeMaster" list="dropdownData.PropTypeMaster" listKey="id"
				listValue="type" headerKey="-1"
				headerValue="%{getText('default.select')}" value="%{propTypeId}"
				onchange="enableFieldsForPropType(); populateUsg(); populatePropTypeCategory(); toggleForResNonRes();toggleFloorDetails(); " />
		</td>
		<td class="bluebox">&nbsp;</td>
		<td class="bluebox">&nbsp;</td>
	</tr>
	
	<tr>
		<td class="bluebox2">&nbsp;</td>
		<td class="bluebox">
			<div id="plotArea">
				<s:text name="PlotArea"/>
				<span class="mandatory1">*</span> :
			</div>
			<div id="undivArea">
				<s:text name="undivArea"/>
				<span class="mandatory1">*</span> :
			</div>
		</td>
		<td class="bluebox" colspan="2"><s:textfield name="areaOfPlot"
				maxlength="15"
				onblur="trim(this,this.value);checkForTwoDecimals(this,'Area Of Plot');checkZero(this,'Area Of Plot');" />
			<span class="highlight2"><s:text
					name="msgForCompulsionOfOpenPlot" /> </span>
		</td>
		<td class="bluebox">&nbsp;</td>
	</tr>
	
	<tr id="floorHeaderRow">
			<td colspan="5" width="5%">
				<div class="headingsmallbg">
					<span class="bold"><s:text name="FloorDetailsHeader" />
					</span>
				</div>
			</td>
	 </tr>
	
	<!-- Floor Details Section -->
		
	<tr>
		<td colspan="5">
			<div align="center">
				<%@ include file="../common/FloorForm.jsp"%>
			</div>
		</td>
	</tr>
	

</table>
<script type="text/javascript">
	function showDocumentManager() {
			var docNum = document.getElementById("docNumber").value;
			var url;
			if (docNum == null || docNum == '' || docNum == 'To be assigned') {
				url = "/egi/docmgmt/basicDocumentManager.action?moduleName=ptis";
			} else {
				url = "/egi/docmgmt/basicDocumentManager!editDocument.action?docNumber="
						+ docNum + "&moduleName=ptis";
			}
			window.open(url, 'docupload', 'width=1000,height=400');
		}
		
	function populateWard() {
		populatewardId({
			zoneId : document.getElementById("zoneId").value
		});
		document.getElementById("areaId").options.length = 0;
		document.getElementById("areaId").value = "select";
	}
	function populateArea() {
		populateareaId({
			wardId : document.getElementById("wardId").value
		});
	}
	function populatePropTypeCategory() {
		populatepropTypeCategoryId({
			propTypeId : document.getElementById("propTypeMaster").value
		});
	}
 	function populateLocationFactors() {
		populatelocationFactor({
			wardId : document.getElementById("wardId").value
		});
	} 
	function setLocationFactor() {
		<s:if test="%{propertyDetail.extra_field6 != null}">
			document.getElementById('locationFactor').value = <s:property value="%{propertyDetail.extra_field6}"/>;			
		</s:if>
	}
</script>
