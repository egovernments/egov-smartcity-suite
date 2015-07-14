<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="ownership.type"></s:text>
			<span class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:select headerKey="-1" headerValue="%{getText('default.select')}" name="propTypeId"
				id="propTypeId" listKey="id" listValue="type" list="dropdownData.PropTypeMaster" value="%{propTypeId}"
				cssClass="selectnew" onchange="populatePropTypeCategory();toggleFloorDetails();enableFieldsForPropType();" /></td>
				
		<td class="greybox" width="25%"><s:text name="property.type"></s:text>
			<span class="mandatory1" id="prntMandatory">*</span> :</td>
		<egov:ajaxdropdown id="propTypeCategoryId" fields="['Text','Value']" dropdownId="propTypeCategoryId"
			url="/common/ajaxCommon-propTypeCategoryByPropType.action" />
		<td class="greybox">
		   <s:select headerKey="-1"	headerValue="%{getText('default.select')}" name="propertyDetail.extra_field5"
				id="propTypeCategoryId" listKey="key" listValue="value" list="propTypeCategoryMap" value="%{propertyDetail.extra_field5}"
				cssClass="selectnew"/>
		</td>
	</tr>
	
	<tr class="vacantAreaRow">
		<td class="greybox" width="5%">&nbsp;</td>
		<td class="greybox" width="25%"><s:text name="PlotArea"></s:text> :</td>
		<td class="greybox" width=""><s:textfield name="areaOfPlot" maxlength="15" value="%{areaOfPlot}"
				onblur="trim(this,this.value);checkForTwoDecimals(this,'Area Of Plot');checkZero(this,'Area Of Plot');" /></td>
		<td class="greybox" width="25%"><s:text name="constCompl.date"></s:text> :</td>
		<td class="greybox">
		   <s:date name="%{dateOfCompletion}" var="occupationDate" format="dd/MM/yyyy" /> <s:textfield
				name="dateOfCompletion" id="basicProperty.propOccupationDate" value="%{#occupationDate}" autocomplete="off" cssClass="datepicker"
				size="10" maxlength="10"></s:textfield>
		</td>
	</tr>
	
	<tr id="apartmentRow">
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="apartcomplex.name"></s:text> :</td>
		<td class="greybox"><s:select headerKey=""
				headerValue="%{getText('default.select')}" 	name="propertyDetail.apartment.id" id="propertyDetail.apartment.id"
				listKey="id" listValue="name" value="%{propertyDetail.apartment.id}"
				list="dropdownData.apartments" cssClass="selectnew"
				onchange="makeMandatory();" /></td>
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
				<span class="bold"><s:text name="assessmentDetails.title"/></span>
			</div>
		</td>
	</tr>
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="rsnForCreatin" /> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox"><s:select headerKey="-1" headerValue="%{getText('default.select')}" name="mutationId"
				id="mutationId" listKey="id" listValue="mutationName" list="dropdownData.MutationList" value="%{mutationId}"
				cssClass="selectnew" onchange="makeMandatory();" /></td>
		<td class="greybox"><s:text name="prntPropAssessmentNum" /> <span
			class="mandatory1" id="prntMandatory">*</span> :</td>
		<td class="greybox"><s:textfield name="parentIndex" id="parentIndex" size="12" maxlength="12"></s:textfield></td>

	</tr>

	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="extent.site"></s:text> <span
			class="mandatory1">*</span> :</td>
		<td class="greybox" width=""><s:textfield
				name="propertyDetail.extentSite" id="extentSite" size="12"
				maxlength="15" value="%{propertyDetail.extentSite}"></s:textfield></td>
		
	</tr>

   <tr>
		<td class="greybox">&nbsp;</td>
		<td class="bluebox"><s:text name="extent.appurtntland" /> : 
		<td class="bluebox"><s:checkbox name="chkIsAppartenauntLand" id="chkIsAppartenauntLand"
				value="%{chkIsAppartenauntLand}" onclick="enableAppartnaumtLandDetails();" />
		</td>
		<td class="greybox"><s:text name="certificationNumber"></s:text>:</td>
		<td class="greybox"><s:textfield maxlength="64" name="certificationNumber" id="certificationNumber"></s:textfield></td>
	</tr>

	<tr id="appartenantRow">
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="extent.appurtntland"></s:text>
			<span class="mandatory1">*</span> :</td>
		<td class="greybox"><s:textfield name="propertyDetail.extentAppartenauntLand" id="propertyDetail.extentAppartenauntLand"
				value="%{propertyDetail.extentAppartenauntLand}" size="12"	maxlength="12" onchange="trim(this,this.value);"
				onblur="validNumber(this);checkZero(this);"></s:textfield></td>
	</tr>
	
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="bluebox"><s:text name="superstructure"></s:text> :</td>
		<td class="bluebox">
		 <s:checkbox name="propertyDetail.structure" id="propertyDetail.structure"
			value="%{propertyDetail.structure}" onclick="enableOrDisableSiteOwnerDetails(this);" />
		</td>
		<td class="greybox siteowner"><s:text name="siteowner"></s:text>:</td>
		<td class="greybox siteowner"><s:textfield maxlength="64" value="%{propertyDetail.siteOwner}"
				name="propertyDetail.siteOwner" id="propertyDetail.siteOwner"></s:textfield></td>
	</tr>
	
	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="bluebox"><s:text name="builidingdetails"></s:text> :</td>
		<td class="bluebox">
		 <s:checkbox name="chkBuildingPlanDetails" id="chkBuildingPlanDetails"
			value="%{chkBuildingPlanDetails}" onclick="enableOrDisableBPADetails(this);" />
		</td>
	</tr>
	
	<tr class="bpddetails">
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="building.permNo"></s:text> :</td>
		<td class="greybox"><s:textfield name="buildingPermissionNo" id="buildingPermissionNo" size="12" maxlength="12"
				onchange="trim(this,this.value);" onblur="checkZero(this);"></s:textfield>
		</td>
		<td class="greybox"><s:text name="buildingpermdate"></s:text> :</td>
		<td class="greybox"><s:date name="buildingPermissionDate" var="buildingPermDate" format="dd/MM/yyyy" /> <s:textfield
				name="buildingPermissionDate" cssClass="datepicker" 	value="%{#buildingPermDate}" autocomplete="off"
				id="buildingPermissionDate" size="12" maxlength="12"></s:textfield>
		</td>

	</tr>

    <tr class="bpddetails">
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="deviationper"></s:text> :</td>
		<td class="greybox"><s:select headerKey="-1" headerValue="%{getText('default.select')}" name="propertyDetail.extra_field5"
				id="propTypeCategoryId" listKey="key" listValue="value" list="propTypeCategoryMap" value="%{propertyDetail.extra_field5}"
				cssClass="selectnew"/>
		</td>
   </tr>

	<tr>
		<td class="greybox">&nbsp;</td>
		<td class="greybox"><s:text name="reg.docno"></s:text> :</td>
		<td class="greybox"><s:textfield name="regdDocNo" id="regdDocNo"
				value="%{regdDocNo}" size="12" maxlength="12"
				onchange="trim(this,this.value);" onblur="checkZero(this);"></s:textfield>
		</td>
		<td class="greybox"><s:text name="reg.docdate"></s:text> :</td>
		<td class="greybox"><s:date name="regdDocDate" var="docDate"
				format="dd/MM/yyyy" /> <s:textfield name="regdDocDate"
				id="regdDocDate" value="%{#docDate}" size="12" autocomplete="off"
				maxlength="12" cssClass="datepicker"></s:textfield></td>
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

	<tr id="amenitiesHeaderRow" class="amenities">
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"> <s:text name="amenities"></s:text>
				</span>
			</div>
		</td>
	</tr>

	<tr class="amenities">
		<td colspan="5">
			<div id="AmenitiesDiv">
				<%@ include file="../common/amenitiesForm.jsp"%>
			</div>
		</td>
	</tr>

	<!-- Floor type details -->

	<tr id="constructionHeaderRow" class="construction">
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"><s:text name="title.constructiontypes" /></span>
			</div>
		</td>
	</tr>
	
	<tr class="construction">
		<td colspan="5">
			<div id="AmenitiesDiv">
				<%@ include file="../common/constructionForm.jsp"%>
			</div>
		</td>
	</tr>

	<tr id="floorHeaderRow" class="floordetails">
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"><s:text name="FloorDetailsHeader" /> </span>
			</div>
		</td>
	</tr>

	<!-- Floor Details Section -->

	<tr class="floordetails">
		<td colspan="5">
			<div align="center">
				<%@ include file="../common/FloorForm.jsp"%>
			</div>
		</td>
	</tr>

	<tr id="vacantLandRow" class="vacantlanddetaills">
		<td colspan="5">
			<div class="headingsmallbg">
				<span class="bold"><s:text name="VacantLandDetailsHeader" /> </span>
			</div>
		</td>
	</tr>

	<tr class="vacantlanddetaills">
		<td colspan="5">
			<div align="center">
				<%@ include file="../common/vacantLandForm.jsp"%>
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
			propTypeId : document.getElementById("propTypeId").value
		});
	}
 	function populateLocationFactors() {
		populatelocationFactor({
			wardId : document.getElementById("wardId").value
		});
	} 

	//hide rows and columns of fields
	jQuery('td.siteowner').hide();
	jQuery('tr.bpddetails').hide();
	jQuery('tr.vacantlanddetaills').hide();
    
</script>
