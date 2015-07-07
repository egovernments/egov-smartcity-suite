<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="ownership.type"></s:text> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="propTypeId"
				id="propTypeId" listKey="id" listValue="type"
				list="dropdownData.PropTypeMaster" value="%{propertyDetail.propertyTypeMaster.id}"
				cssClass="selectnew" onchange="toggleFloorDetails();" />
		</td>
	</tr>
   <tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="application.no"></s:text><span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width="">
		   <s:textfield name="applicationNo"
				id="applicationNo" value="%{applicationNo}" autocomplete="off" size="12" maxlength="12"></s:textfield>
		</td>
		<td class="greybox" width="25%"><s:text name="application.date"></s:text>
			<span class="mandatory1" id="prntMandatory">*</span> :</td>
		<td class="greybox">
		<s:if test="basicProperty.createdDate != null">
		<s:date name="%{basicProperty.createdDate}" var="createdDate" format="dd/MM/yyyy" />
		<s:textfield name="applicationDate" id="createdDate" value="%{#createdDate}" readOnly="true" size="10" maxlength="10"></s:textfield>
		</s:if>
		<s:else>
		<s:date name="currDate" var="todaysDate" format="dd/MM/yyyy" />
	    <s:textfield name="applicationDate" id="createdDate" value="%{#todaysDate}" readOnly="true" size="10" maxlength="10"></s:textfield>
	    </s:else>
		</td>
	</tr>

	<tr>
	<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="siteowner"></s:text>:</td>
		<td class="greybox"><s:textfield maxlength="64"
				name="propertyDetail.siteOwner" id="propertyDetail.siteOwner"></s:textfield></td>
	</tr>
	<!-- Owner details section -->
	<tr>
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"><s:text name="ownerdetails.title"></s:text></span>
			</div>
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
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"> Heading Not decided</span>
			</div>
		</td>
	</tr>
    <tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="rsnForCreatin" /> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox"><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="mutationId"
				id="mutationId" listKey="id" listValue="mutationName"
				list="dropdownData.MutationList" value="%{propertyDetail.propertyMutationMaster.id}"
				cssClass="selectnew" onchange="makeMandatory();" />
		</td>
		<td class="greybox"><s:text name="prntPropAssessmentNum" />
			<span class="mandatory1" id="prntMandatory">*</span> :</td>
		<td class="greybox"><s:textfield name="parentIndex"
				id="parentIndex" size="12" maxlength="12"></s:textfield>
		</td>

	</tr> 
	
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="extent.site"></s:text> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width="">
		   <s:textfield name="propertyDetail.extentSite" id="extentSite" size="12" maxlength="15" value="%{propertyDetail.extentSite}"></s:textfield>
		</td>
		<td class="greybox"><s:text name="vacantland.assmtno"></s:text>
			<span class="mandatory1" id="prntMandatory">*</span> :</td>
		<td class="greybox">
		  <s:textfield name="vacantLandNo" id="vacantLandNo" value="%{vacantLandNo}" size="12" maxlength="15" onchange="trim(this,this.value);" onblur = "validNumber(this);checkZero(this);"></s:textfield>
		</td>

	</tr>
	
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="extent.appurtntland"></s:text> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox">
		   <s:textfield name="propertyDetail.extentAppartenauntLand" id="propertyDetail.extentAppartenauntLand" value="%{propertyDetail.extentAppartenauntLand}" size="12" maxlength="12" onchange="trim(this,this.value);" onblur = "validNumber(this);checkZero(this);"></s:textfield>
		</td>

	</tr>
	
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="building.permNo"></s:text> :</td>
		<td class="greybox">
		   <s:textfield name="buildingPermissionNo" id="buildingPermissionNo" size="12" maxlength="12" onchange="trim(this,this.value);" onblur = "validNumber(this);checkZero(this);"></s:textfield>
		</td>
		<td class="greybox"><s:text name="buildingpermdate"></s:text> :</td>
		<td class="greybox">
		  <s:textfield name="buildingPermissionDate"  cssClass="datepicker" autocomplete="off" id="buildingPermissionDate" size="12" maxlength="12"></s:textfield>
		</td>

	</tr>
	
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="reg.docno"></s:text> :</td>
		<td class="greybox">
		   <s:textfield name="regdDocNo" id="regdDocNo" value="%{regdDocNo}" size="12" maxlength="12" onchange="trim(this,this.value);" onblur = "validNumber(this);checkZero(this);"></s:textfield>
		</td>
		<td class="greybox"><s:text name="reg.docdate"></s:text> :</td>
		<td class="greybox">
		<s:date name="regdDocDate" var="docDate" format="dd/MM/yyyy" />
		  <s:textfield name="regdDocDate" id="regdDocDate" value="%{#docDate}" size="12" autocomplete="off" maxlength="12" cssClass="datepicker"></s:textfield>
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
		<td colspan="5">
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
				<td width="20%" align="right">
				  <label><s:text name="lift"></s:text> <s:checkbox name="propertyDetail.lift" id="propertyDetail.lift"/></label>
				  <br/><label><s:text name="drainage"></s:text> <s:checkbox name="propertyDetail.drainage" id="propertyDetail.drainage"/></label>
				  <br/> <label><s:text name="cableconnection"></s:text> <s:checkbox name="propertyDetail.cable" id="propertyDetail.cable"/></label> 
				</td>
				<td width="20%" align="right">
				  <label><s:text name="toilets"></s:text> <s:checkbox name="propertyDetail.toilets" id="propertyDetail.toilets"/> </label>
				  <br/> <label><s:text name="electricity"></s:text>  <s:checkbox name="propertyDetail.electricity" id="propertyDetail.electricity"/> </label>
				</td>
				<td width="20%" align="right">
				  <label><s:text name="watertap"></s:text> <s:checkbox name="propertyDetail.waterTap" id="propertyDetail.waterTap" value="%{propertyDetail.waterTap}"/></label> 
				   <br/><label><s:text name="attachbathroom"></s:text> <s:checkbox name="propertyDetail.attachedBathRoom" id="propertyDetail.attachedBathRoom"/> </label>
			    </td>
			    <td width="20%" align="right">
				  <label><s:text name="superstructure"></s:text> <s:checkbox name="propertyDetail.structure" id="propertyDetail.structure" value="%{propertyDetail.structure}"/></label>
				  <br/> <label><s:text name="waterharvesting"></s:text> <s:checkbox name="propertyDetail.waterHarvesting" id="propertyDetail.waterHarvesting"/></label>
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
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="floortype"></s:text> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="floorTypeId"
				id="floorTypeId" listKey="id" listValue="name"
				list="dropdownData.floorType" value="%{propertyDetail.floorType.id}"
				cssClass="selectnew" />
		</td>
		<td class="greybox"><s:text name="rooftype"></s:text> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox"><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="roofTypeId"
				id="roofTypeId" listKey="id" listValue="name"
				list="dropdownData.roofType" value="%{propertyDetail.roofType.id}"
				cssClass="selectnew"/>
		</td>
	</tr>
	
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="walltype"></s:text> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="wallTypeId"
				id="wallTypeId" listKey="id" listValue="name"
				list="dropdownData.wallType"  value="%{propertyDetail.wallType.id}"
				cssClass="selectnew"/>
		</td>
		<td class="greybox"><s:text name="woodtype"></s:text> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select headerKey="-1"
				headerValue="%{getText('default.select')}" name="woodTypeId"
				id="woodTypeId" listKey="id" listValue="name"
				list="dropdownData.woodType" value="%{propertyDetail.woodType.id}"
				cssClass="selectnew"/>
		</td>
	</tr>
	
	
	<!-- Ownership section -->
	
	<tr id="ownerShipRow">
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"><s:text name="title.ownership"/></span>
			</div>
		</td>
	</tr>
	
	<tr id="vacantAreaRow">
		<td class="greybox">&nbsp;</td>
		<td class="bluebox">
			<div id="plotArea">
				<s:text name="PlotArea"/>
				<span class="mandatory1">*</span> :
			</div>
		</td>
		<td class="bluebox" colspan="2"><s:textfield name="areaOfPlot" maxlength="15" value="%{propertyDetail.sitalArea.area}"
				onblur="trim(this,this.value);checkForTwoDecimals(this,'Area Of Plot');checkZero(this,'Area Of Plot');" />
			<span class="highlight2"><s:text
					name="msgForCompulsionOfOpenPlot" /> </span>
		</td>
	</tr>
	
	<tr id="appartmentRow">
	    <td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="apartcomplex.name"></s:text> :</td>
		<td class="greybox"><s:select headerKey="" headerValue="%{getText('default.select')}" name="propertyDetail.apartment.id"
				id="propertyDetail.apartment.id" listKey="id" listValue="name" value="%{propertyDetail.apartment.id}"
				list="dropdownData.apartments" cssClass="selectnew" onchange="makeMandatory();" />
		</td>
	</tr>
	
	<tr id="floorHeaderRow">
			<td colspan="5">
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
				<br/>
			</div>
		</td>
	</tr>

	<tr id="completionDate">
		<td class="greybox">&nbsp;</td>
		<td class="bluebox"><s:text name="constCompl.date"></s:text> :</td>
		<td class="greybox">
		<s:date name="%{basicProperty.propOccupationDate}" var="occupationDate" format="dd/MM/yyyy" />
		   <s:textfield name="dateOfCompletion" id="basicProperty.propOccupationDate" value="%{#occupationDate}" autocomplete="off" cssClass="datepicker" size="10" maxlength="10"></s:textfield>
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
