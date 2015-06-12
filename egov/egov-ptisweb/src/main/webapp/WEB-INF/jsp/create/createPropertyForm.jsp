<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
   <tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="20%"><s:text name="application.no"></s:text><span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width="">
		   <s:textfield name="property.basicProperty.applicationNo"
				id="property.basicProperty.applicationNo" size="12" maxlength="12"></s:textfield>
		</td>
		<td class="greybox" width="25%"><s:text name="application.date"></s:text>
			<span class="mandatory1" id="prntMandatory">*</span> :</td>
			
		<td class="greybox">
		<s:date name="currDate" var="todaysDate" format="dd/MM/yyyy" />
	    <s:textfield name="applicationDate" id="createdDate" value="%{#todaysDate}" readOnly="true" size="10" maxlength="10"></s:textfield>
		</td>
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
		<td class="greybox" width="25%"><s:text name="adharno"></s:text> :</td>
		<td class="greybox" width="">
		   <s:textfield name="aadharNo" id="aadharNo" size="12" maxlength="12"></s:textfield>
		</td>
		<td class="greybox" width="25%">Father/Husband Name :</td>
		<td class="greybox">
		  <s:textfield name="guardname" id="guardname" maxlength="30"></s:textfield>
		</td>
	</tr>
	
	<tr>
	   <td class="greybox" width="5%">&nbsp;</td>
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
	
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"> Heading Not decided</span>
			</div>
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
		<td class="greybox" width="25%"><s:text name="extent.site"></s:text> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width="">
		   <s:textfield name="propertyDetail.extentSite" id="extentSite" size="12" maxlength="12"></s:textfield>
		</td>
		<td class="greybox" width="25%"><s:text name="vacantland.assmtno"></s:text>
			<span class="mandatory1" id="prntMandatory">*</span> :</td>
		<td class="greybox">
		  <s:textfield name="vacantLandNo" id="vacantLandNo" size="12" maxlength="12"></s:textfield>
		</td>

	</tr>
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="extent.appurtntland"></s:text> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width="">
		   <s:textfield name="extentAppartenauntLand" id="propertyDetail.extentAppartenauntLand" size="12" maxlength="12"></s:textfield>
		</td>

	</tr>
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="building.permNo"></s:text> :</td>
		<td class="greybox" width="">
		   <s:textfield name="basicProperty.buildingPermissionNo" id="basicProperty.buildingPermissionNo" size="12" maxlength="12"></s:textfield>
		</td>
		<td class="greybox" width="25%">Building Permission Date :</td>
		<td class="greybox">
		  <s:textfield name="basicProperty.buildingPermissionDate"  class="form-control datepicker" id="basicProperty.buildingPermissionDate" size="12" maxlength="12"></s:textfield>
		</td>

	</tr>
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="reg.docno"></s:text> :</td>
		<td class="greybox" width="">
		   <s:textfield name="basicProperty.regDocNo" id="basicProperty.regDocNo" size="12" maxlength="12"></s:textfield>
		</td>
		<td class="greybox" width="25%"><s:text name="reg.docdate"></s:text> :</td>
		<td class="greybox">
		  <s:textfield name="basicProperty.regDocDate" id="basicProperty.regDocDate" size="12" maxlength="12"></s:textfield>
		</td>
	</tr>
	
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="constCompl.date"></s:text> :</td>
		<td class="greybox" width="">
		   <s:textfield name="dateOfCompletion" id="basicProperty.propOccupationDate" size="10" maxlength="10"></s:textfield>
		</td>
	</tr>
	
	<!-- Owner details section -->
	
	
	
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
				<span class="bold"> <s:text name="amenities"></s:text> </span>
			</div>
		</td>
	</tr>
	
	<tr>
		<td colspan="5">
		  <table width="100%" class="checkbox-section">
	        <tr>
	            <td width="10%"></td>
				<td width="20%" align="right">
				  <label> Lift <s:checkbox name="propertyDetail.lift" id="propertyDetail.lift"/></label>
				  <br/> <label> Drainage <s:checkbox name="propertyDetail.drainage" id="propertyDetail.drainage"/></label>
				  <br/> <label>Cable Connection <s:checkbox name="propertyDetail.cable" id="propertyDetail.cable"/></label> 
				</td>
				<td width="20%" align="right">
				  <label>Toilets <s:checkbox name="propertyDetail.toilets" id="propertyDetail.toilets"/> </label>
				  <br/> <label>Electricity <s:checkbox name="propertyDetail.electricity" id="propertyDetail.electricity"/> </label>
				</td>
				<td width="20%" align="right">
				  <label>Water Tap <s:checkbox name="propertyDetail.waterTap" id="propertyDetail.waterTap"/></label> 
				   <br/> <label>Attached Bathroom <s:checkbox name="propertyDetail.attachedBathRoom" id="propertyDetail.attachedBathRoom"/> </label>
			    </td>
			    <td width="20%" align="right">
				  <label>Super Structure <s:checkbox name="propertyDetail.structure" id="propertyDetail.structure"/></label>
				  <br/>  <label>Water Harvesting <s:checkbox name="propertyDetail.waterHarvesting" id="propertyDetail.waterHarvesting"/></label>
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
				<span class="bold"><s:text name="title.constructiontypes"/></span>
			</div>
		</td>
	</tr>
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="floortype"></s:text> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="floorTypeId"
				id="floorTypeId" listKey="id" listValue="name"
				list="dropdownData.floorType" value="%{floorTypeId}"
				cssClass="selectnew" onchange="makeMandatory();" />
		</td>
		<td class="greybox" width="25%"><s:text name="rooftype"></s:text> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="roofTypeId"
				id="roofTypeId" listKey="id" listValue="name"
				list="dropdownData.roofType" value="%{roofTypeId}"
				cssClass="selectnew" onchange="makeMandatory();" />
		</td>
	</tr>
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="walltype"></s:text> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="wallTypeId"
				id="wallTypeId" listKey="id" listValue="name"
				list="dropdownData.wallType" value="%{wallTypeId}"
				cssClass="selectnew" onchange="makeMandatory();" />
		</td>
		<td class="greybox" width="25%"><s:text name="woodtype"></s:text> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="woodTypeId"
				id="woodTypeId" listKey="id" listValue="name"
				list="dropdownData.woodType" value="%{woodTypeId}"
				cssClass="selectnew" onchange="makeMandatory();" />
		</td>
	</tr>
	
	
	<!-- Ownership section -->
	
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"><s:text name="title.ownership"/></span>
			</div>
		</td>
	</tr>
	
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="ownership.type"></s:text> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="propTypeId"
				id="ownershipType" listKey="id" listValue="type"
				list="dropdownData.PropTypeMaster" value="%{mutationId}"
				cssClass="selectnew" onchange="makeMandatory();" />
		</td>
		
	</tr>
	
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
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
	
	
	<tr>
	<td class="bluebox">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="servicecharg.category"></s:text> :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="sbservchargetype"
				id="sbservchargetype" listKey="idMutation" listValue="mutationName"
				list="dropdownData.MutationList" value="%{mutationId}"
				cssClass="selectnew" onchange="makeMandatory();" />
		</td>
	
		<td class="greybox" width="25%"><s:text name="apartcomplex.name"></s:text> :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="sbapartcomplex"
				id="sbapartcomplex" listKey="idMutation" listValue="mutationName"
				list="dropdownData.MutationList" value="%{mutationId}"
				cssClass="selectnew" onchange="makeMandatory();" />
		</td>
	</tr>
	
	
	<!-- property type section -->
	
	<%-- <tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"><s:text name="PropertyType" /> </span>
			</div>
		</td>
	</tr>
	<tr>
		<td class="bluebox">&nbsp;</td>
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
	 --%>
	
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
