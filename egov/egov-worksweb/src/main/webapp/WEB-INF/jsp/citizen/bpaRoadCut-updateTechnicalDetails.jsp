<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="egov"%>
<%@ taglib uri="/WEB-INF/struts-tags.tld" prefix="s"%>
<html>
	<head>
		<title>
			<s:if test="%{mode == @org.egov.works.utils.DepositWorksConstants@UPDATE_TECHNICAL_DETAILS}">
				<s:text name="dw.bpa.update.tech.details.title"/>
			</s:if>
			<s:elseif test="%{mode == @org.egov.works.utils.DepositWorksConstants@VIEW_TECHNICAL_DETAILS}">
				<s:text name="dw.bpa.view.tech.details.title"/>
			</s:elseif>
			<s:else>
				<s:text name="dw.bpa.modify.tech.details.title"/>
			</s:else>
		</title>
	</head>
	<script type="text/javascript">

		var zoneIdVar = <s:property value = "%{zoneId}"/>;
		var wardIdVar = <s:property value = "%{wardId}"/>;
		var areaIdVar = null;
		var localityIdVar = null;
		var streetIdVar = null;
		<s:if test = "%{areaId != null}">
			areaIdVar = <s:property value = "%{areaId}"/>;
		</s:if>
		<s:if test = "%{localityId != null}">
			localityIdVar = <s:property value = "%{localityId}"/>;
		</s:if>
		<s:if test = "%{streetId != null}">
			streetIdVar = <s:property value = "%{streetId}"/>;
		</s:if>

		function setDefaultPurposeOfCut() {
			<s:if test = "%{applicationDetails.applicationRequest.purposeOfRoadCut == null}">
				jQuery('#purposeOfRoadCut option:contains("SERVICE")').attr('selected', 'selected');
				defaultDepthOfCut('SERVICE');
			</s:if>
			<s:else>
				defaultDepthOfCut('<s:property value="%{applicationDetails.applicationRequest.purposeOfRoadCut}"/>');
			</s:else>
		}

		function defaultDepthOfCut(purposeOfCut) {
			if(dom.get("depth") != null) {
		    	if(purposeOfCut=='<s:property value="@org.egov.works.models.depositWorks.PurposeOfRoadCut@SERVICE"/>') {
					dom.get("depth").value = '<s:property value="@org.egov.works.utils.DepositWorksConstants@DEPTH_OF_CUT_TWO"/>';
			     	dom.get("depth").disabled=true;
		    	} else { 
			    	<s:if test="%{applicationDetails.applicationRequest.roadCutDetailsList[0].roadBreadth == '-1'}">
			    		dom.get("depth").value = -1;						
			    	</s:if>
			     	dom.get("depth").disabled=false;
		        }
		    }        	
		}

		function populateJurisdictionDetails() {
			var zoneId = jQuery("#zoneId").val();
			if (zoneId != null && zoneId != -1) {
				jQuery("#zoneId").attr('disabled', 'disabled');
				populateWards();
			} else {
				jQuery("#zoneId").attr('disabled',false);
			}
		}

		function populateRoadDepth() {
			<s:if test = "%{applicationDetails.applicationRequest.roadCutDetailsList[0].roadDepth != null && applicationDetails.applicationRequest.roadCutDetailsList[0].roadDepth != 0.0}">
				jQuery("#depth").val(roundTo("<s:property value='applicationDetails.applicationRequest.roadCutDetailsList[0].roadDepth'/>",2));
			</s:if>
		}
		
		function populateWards() {
			var zoneId = jQuery("#zoneId").val();
			populatewardId({zoneId:zoneId, dwCategory:"BPA"});
		}

		function populateAreas() {
			var wardId = jQuery("#wardId").val();
			populateareaId({wardId:wardId, dwCategory:"BPA"});			
		}

		function populateLocalities() {
			var areaId = jQuery("#areaId").val();
			populatelocalityId({areaId:areaId, dwCategory:"BPA"});
		}

		function populateStreets() {
			var locationId = jQuery("#localityId").val();
			populatestreetId({locationId:locationId, dwCategory:"BPA"});			
		}

		function setWardId() {
			if (wardIdVar != null && wardIdVar != -1) {
				jQuery('#wardId').val(wardIdVar);
			}
			<s:if test = "%{disableWard}">	
				jQuery("#wardId").attr('disabled', 'disabled');
			</s:if>
			<s:else>
				jQuery("#wardId").attr('disabled',false);
			</s:else>
		}

		function setAreaId() {
			if (areaIdVar != null && areaIdVar != -1) {
				jQuery('#areaId').val(areaIdVar);
			}
			<s:if test = "%{disableArea}">	
				jQuery("#areaId").attr('disabled', 'disabled');
			</s:if>
			<s:else>
				jQuery("#areaId").attr('disabled',false);
			</s:else>
		}

		function setLocalityId() {
			if (localityIdVar != null && localityIdVar != -1) {
				jQuery('#localityId').val(localityIdVar);
			}
			<s:if test = "%{disableLocality}">	
				jQuery("#localityId").attr('disabled', 'disabled');
			</s:if>
			<s:else>
				jQuery("#localityId").attr('disabled',false);
			</s:else>
		}

		function setStreetId() {
			if (streetIdVar != null && streetIdVar != -1) {
				jQuery('#streetId').val(streetIdVar);
			}
			<s:if test = "%{disableStreet}">	
				jQuery("#streetId").attr('disabled', 'disabled');
			</s:if>
			<s:else>
				jQuery("#streetId").attr('disabled',false);
			</s:else>
		}

		function validateNumber(obj) {
			var objt = obj;
			var value = obj.value;
			if (value != null && value !="") {
			   if(isNaN(value) || getNumber(value)<0) {
				   dom.get("citizenPortal_error").style.display = '';
				   dom.get("citizenPortal_error").innerHTML = 'Please enter a numeric value';
				   objt.value = "";
				   return false;
				} else {
					dom.get("citizenPortal_error").style.display = 'none';
				}
			}
		}

		function showBPAViewPage(bpaNumber) {
            window.open("/bpa/register/registerBpa!viewRegForm.action?planSubmissionNum="+bpaNumber,"","height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");
        }

		function validateDataBeforeSubmit() {
			dom.get("citizenPortal_error").style.display = "";
			if (dom.get("bpaNumber").value == null || dom.get("bpaNumber").value == "") {
				dom.get("citizenPortal_error").innerHTML = '<s:text name="dw.bpa.bpaNumber.mandatory"/>';
				return false;
			}
			if (dom.get("appRequestDate").value == null || dom.get("appRequestDate").value == "") {
				dom.get("citizenPortal_error").innerHTML = '<s:text name="dw.bpa.applicationDate.mandatory"/>';
				return false;
			}
			if (dom.get("typeOfCut").value == null || dom.get("typeOfCut").value == "-1") {
				dom.get("citizenPortal_error").innerHTML = '<s:text name="depositworks.roadcut.enter.typeofcut"/>';
				return false;
			}
			if (dom.get("purposeOfRoadCut").value == null || dom.get("purposeOfRoadCut").value == "") {
				dom.get("citizenPortal_error").innerHTML = '<s:text name="dw.roadcut.select.purposeofcut"/>';
				return false;
			}
			if (dom.get("address").value == null || dom.get("address").value == "") {
				dom.get("citizenPortal_error").innerHTML = '<s:text name="dw.bpacut.address.mandatory"/>';
				return false;
			}
			if (dom.get("zoneId").value == null || dom.get("zoneId").value == "-1") {
				dom.get("citizenPortal_error").innerHTML = '<s:text name="dw.bpacut.zone.mandatory"/>';
				return false;
			}
			if (dom.get("wardId").value == null || dom.get("wardId").value == "-1") {
				dom.get("citizenPortal_error").innerHTML = '<s:text name="dw.bpacut.ward.mandatory"/>';
				return false;
			}
			if (dom.get("areaId").value == null || dom.get("areaId").value == "-1") {
				dom.get("citizenPortal_error").innerHTML = '<s:text name="dw.bpacut.area.mandatory"/>';
				return false;
			}
			if (dom.get("localityId").value == null || dom.get("localityId").value == "-1") {
				dom.get("citizenPortal_error").innerHTML = '<s:text name="dw.bpacut.locality.mandatory"/>';
				return false;
			}
			if (dom.get("streetId").value == null || dom.get("streetId").value == "-1") {
				dom.get("citizenPortal_error").innerHTML = '<s:text name="dw.bpacut.street.mandatory"/>';
				return false;
			}
			if (dom.get("roadLength").value == null || dom.get("roadLength").value == "" || dom.get("roadLength").value == "0.0") {
				dom.get("citizenPortal_error").innerHTML = '<s:text name="dw.bpacut.length.mandatory"/>';
				return false;
			}
			if (dom.get("breadth").value == null || dom.get("breadth").value == "" || dom.get("breadth").value == "0.0") {
				dom.get("citizenPortal_error").innerHTML = '<s:text name="dw.bpacut.breadth.mandatory"/>';
				return false;
			}
			if (dom.get("depth").value == null || dom.get("depth").value == "-1") {
				dom.get("citizenPortal_error").innerHTML = '<s:text name="dw.bpacut.depth.mandatory"/>';
				return false;
			}
			if (dom.get("docNumber").value == null || dom.get("docNumber").value == "") {
				dom.get("citizenPortal_error").innerHTML = '<s:text name="dw.bpacut.document.upload.mandatory"/>';
				return false;
			}
			dom.get("citizenPortal_error").style.display = "none";
			dom.get("depth").disabled = false;
			return true;
		}

		function viewDocument(){
		  viewDocumentManager(dom.get("docNumber").value); 
		}

		function disableElements() {
			<s:if test="%{mode == @org.egov.works.utils.DepositWorksConstants@VIEW_TECHNICAL_DETAILS}">
				for(var i=0;i<document.technicalDetailsForm.length;i++) {
		      		document.technicalDetailsForm.elements[i].disabled =true;
		      	}
				if (document.technicalDetailsForm.docViewButton != null) {
					document.technicalDetailsForm.docViewButton.readonly = false;
					document.technicalDetailsForm.docViewButton.disabled = false;
				}
				if (document.technicalDetailsForm.closeButton != null) {
					document.technicalDetailsForm.closeButton.readonly = false;
					document.technicalDetailsForm.closeButton.disabled = false;
				}
			</s:if>
			<s:if test="%{mode == @org.egov.works.utils.DepositWorksConstants@MODIFY_TECHNICAL_DETAILS}">
				if (document.technicalDetailsForm.resubmitRemarks != null) {
					document.technicalDetailsForm.resubmitRemarks.readonly = true;
					document.technicalDetailsForm.resubmitRemarks.disabled = true;
				}
			</s:if>
		}
        
	</script>
	<script src="<egov:url path='js/works.js'/>"></script>
	<body onload="setDefaultPurposeOfCut();populateJurisdictionDetails();populateRoadDepth();disableElements();">
		<s:if test="%{hasErrors()}">
	        <div id="errorstyle" class="errorstyle" >
	          <s:actionerror/>
	          <s:fielderror/>
	        </div>
	    </s:if>
	    <div class="errorstyle" id="citizenPortal_error" style="display: none;"></div>
		<s:form name="technicalDetailsForm" action="bpaRoadCut" theme="simple">
		<s:token/>
		<s:push value="model">
			<div class="formmainbox">
			<div class="insidecontent">
			<div class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			<div class="rbcontent2">
			<s:hidden name="applicationDetails.applicationRequest.documentNumber" id="docNumber" />
			<s:hidden name="id" id="id" value="%{id}" />
			<s:hidden name="appDetailsId" id="appDetailsId" />
			<s:hidden name="mode" value="%{mode}" id="mode"/>
			<s:hidden id="citizenId" name="citizenId" value="%{citizenId}"/>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						&nbsp;
					</tr>
					<tr>
						<td colspan="4" class="headingwk">
							<div class="arrowiconwk">
								<img src="${pageContext.request.contextPath}/image/arrow.gif" />
							</div>
							<div class="headplacer"><s:text name="depositworks.roadcut.app.request.details" /></div>
						</td>
					</tr>
					<tr>
						<td class="greyboxwk" width="20%">
							<span class="mandatory">*</span><s:text name="depositworks.roadcut.typeofcut" />:
						</td>
						<td class="greybox2wk">
							<s:select id="typeOfCut" name="applicationDetails.applicationRequest.depositWorksType.id" cssClass="selectwk" 
								list="dropdownData.typesOfRoadCut" headerKey="-1" headerValue="--- Select ---"
								listKey="id" listValue="code" value="%{applicationDetails.applicationRequest.depositWorksType.id}" disabled="true" />
						</td>
						<td class="greyboxwk">
							<span class="mandatory">*</span><s:text name="depositworks.aplication.date">:</s:text>
						</td>
						<td class="greybox2wk">
							<s:date name="applicationDetails.applicationRequest.applicationDate" var="appRequestDateFormat" format="dd/MM/yyyy"/>
					        <s:textfield name="applicationDetails.applicationRequest.applicationDate" value="%{appRequestDateFormat}" id="appRequestDate" cssClass="selectboldwk" onfocus="javascript:vDateType='3';" onkeyup="DateFormat(this,this.value,event,false,'3')" disabled="true" />			         	
						</td>
					</tr>
					<tr>
						<td class="whiteboxwk">
			            	<span class="mandatory">*</span><s:text name="dw.roadcut.details.purposeofcut" />:
			        	</td>
			        	<td class="whitebox2wk">
			            	<s:select id="purposeOfRoadCut" name="applicationDetails.applicationRequest.purposeOfRoadCut" cssClass="selectwk" 
			            	    list="dropdownData.purposeOfRoadCutList" value="%{applicationDetails.applicationRequest.purposeOfRoadCut}"
			            	    onchange="defaultDepthOfCut(this.value);"/>
			        	</td>
			        	<s:if test="%{applicationDetails.applicationRequest.referenceNumber != null}">
							<td class="whiteboxwk">
			            		<s:text name="depositworks.roadcut.refNumber" />:
			        		</td>
			        		<td class="whitebox2wk">
			        			<s:textfield name="applicationDetails.applicationRequest.referenceNumber" value="%{applicationDetails.applicationRequest.referenceNumber}" id="refNumber" disabled="true"/>
			        		</td>			        	
			        	</s:if>
			        	<s:else>
			        		<td class="whiteboxwk" colspan="2">
			        		&nbsp;
			        		</td>
			        	</s:else>
					</tr>
					<tr>
						<td colspan="4" class="greyboxwk">&nbsp;</td>
					</tr>
				</table>
				<div style="width:100%;overflow:auto; ">
					<table width="100%" cellpadding="0" cellspacing="0">
						<tr>
							<td class="headingwk"><s:text name="dw.bpacut.sno"/></td>
							<td class="headingwk"><span class="mandatory">*</span><s:text name="dw.bpacut.bpaNo"/></td>
							<td class="headingwk"><span class="mandatory">*</span><s:text name="dw.bpacut.address"/></td>
							<td class="headingwk"><span class="mandatory">*</span><s:text name="dw.bpacut.zone"/></td>
							<td class="headingwk"><span class="mandatory">*</span><s:text name="dw.bpacut.ward"/></td>
							<td class="headingwk"><span class="mandatory">*</span><s:text name="dw.bpacut.area"/></td>
							<td class="headingwk"><span class="mandatory">*</span><s:text name="dw.bpacut.locality"/></td>
							<td class="headingwk"><span class="mandatory">*</span><s:text name="dw.bpacut.street"/></td>
							<td class="headingwk"><span class="mandatory">*</span><s:text name="dw.bpacut.length"/><br/>(meters)</td>
							<td class="headingwk"><span class="mandatory">*</span><s:text name="dw.bpacut.breadth"/><br/>(meters)</td>
							<td class="headingwk"><span class="mandatory">*</span><s:text name="dw.bpacut.depth"/><br/>(meters)</td>
							<td class="headingwk"><s:text name="dw.bpacut.remarks"/></td>
						</tr>
						<tr>
							<td class="whiteboxwk">
								1
							</td>
							<td class="whiteboxwk">
								<s:hidden name="applicationDetails.applicationRequest.roadCutDetailsList[0].bpaNumber" value="%{applicationDetails.applicationRequest.roadCutDetailsList[0].bpaNumber}" id="bpaNumber"/>
								<a href='#' onclick="javascript:showBPAViewPage('<s:property value="%{applicationDetails.applicationRequest.roadCutDetailsList[0].bpaNumber}"/>')">
									<span id="bpaNumberSpanId"><s:property value="%{applicationDetails.applicationRequest.roadCutDetailsList[0].bpaNumber}"/></span>
								</a>
							</td>
							<td class="whiteboxwk"> 
								<s:textarea name="applicationDetails.applicationRequest.roadCutDetailsList[0].locationName" rows="2" cols="50" id="address"
									value="%{applicationDetails.applicationRequest.roadCutDetailsList[0].locationNameJS}" style="width:160px"  cssClass="selectmultilinewk"/>
							</td>
							<td class="whiteboxwk">
								<s:select list="dropdownData.zones" name="zoneId" 
								value="%{zoneId}" listKey="id" listValue="name" id="zoneId"
								headerKey="-1" headerValue="--Select--" style="width:60px" onchange="populateWards();"/>
								<egov:ajaxdropdown fields="['Text', 'Value']" url="citizen/ajaxDepositWorks!populateWard.action" dropdownId="wardId" id="wardId" afterSuccess="setWardId(); populateAreas();"/>
							</td>
							<td class="whiteboxwk">
								<s:select list="dropdownData.wards" name="wardId" 
								value="%{wardId}" listKey="id" listValue="name" id="wardId"
								headerKey="-1" headerValue="--Select--" style="width:60px" onchange="populateAreas();"/>
								<egov:ajaxdropdown fields="['Text', 'Value']" url="citizen/ajaxDepositWorks!populateArea.action" dropdownId="areaId" id="areaId" afterSuccess="setAreaId(); populateLocalities();"/>
							</td>
							<td class="whiteboxwk">
								<s:select list="dropdownData.areas" name="areaId" 
								value="{areaId}" listKey="id" listValue="name" id="areaId"
								headerKey="-1" headerValue="--Select--" style="width:150px" onchange="populateLocalities();"/>
								<egov:ajaxdropdown fields="['Text', 'Value']" url="citizen/ajaxDepositWorks!populateLocality.action" dropdownId="localityId" id="localityId" afterSuccess="setLocalityId(); populateStreets();"/>
							</td>
							<td class="whiteboxwk">
								<s:select list="dropdownData.localities" name="localityId" 
								value="%{localityId}" listKey="id" listValue="name" id="localityId"
								headerKey="-1" headerValue="--Select--" style="width:150px" onchange="populateStreets();"/>
								<egov:ajaxdropdown fields="['Text', 'Value']" url="citizen/ajaxDepositWorks!populateStreets.action" dropdownId="streetId" id="streetId" afterSuccess="setStreetId();"/>
							</td>
							<td class="whiteboxwk">
								<s:select list="dropdownData.streets" name="streetId" 
								value="%{streetId}" listKey="id" listValue="name" id="streetId"
								headerKey="-1" headerValue="--Select--" style="width:250px"/>
							</td>
							<td class="whiteboxwk">
								<s:textfield id="roadLength" name="applicationDetails.applicationRequest.roadCutDetailsList[0].roadLength" cssClass="selectmultilinewk" 
									value="%{applicationDetails.applicationRequest.roadCutDetailsList[0].roadLength}" onblur="validateNumber(this);" />
							</td>
							<td class="whiteboxwk">
								<s:textfield id="breadth" name="applicationDetails.applicationRequest.roadCutDetailsList[0].roadBreadth" cssClass="selectmultilinewk" 
									value="%{applicationDetails.applicationRequest.roadCutDetailsList[0].roadBreadth}" onblur="validateNumber(this);" />
							</td>
							<td class="whiteboxwk">
								<s:select list="dropdownData.depthOfCutList" name="applicationDetails.applicationRequest.roadCutDetailsList[0].roadDepth" cssClass="selectmultilinewk"
									value="%{applicationDetails.applicationRequest.roadCutDetailsList[0].roadDepth}" headerKey="-1" headerValue="--Select--" id = "depth"
									listKey="value" listValue="label" style="width:70px"/>
							</td>
							<td class="whiteboxwk">
								<s:textarea name="applicationDetails.applicationRequest.roadCutDetailsList[0].remarks" rows="2" cols="50" id="remarks" 
									value="%{applicationDetails.applicationRequest.roadCutDetailsList[0].remarks}" style="width:160px"  cssClass="selectmultilinewk"/>
							</td>
						</tr>
					</table>							
				</div>
				<s:if test="%{applicationDetails.applicationRequest.egwStatus.code == @org.egov.works.utils.DepositWorksConstants@APPLICATIONREQUEST_STATUS_REJECTED_FOR_RESUBMISSION}">
					<table width="100%" cellpadding="0" cellspacing="0">
						<tr>
							<td colspan="4" class="whileboxwk">&nbsp;</td>
						</tr>
						<tr>
						    <td class="greyboxwk" width="20%"><span class="mandatory">*</span><s:text name="dw.bpacut.rejection.remarks"/>:</td>
							<td class="greybox2wk">
								<s:textarea name="applicationDetails.rejectionRemarks" cols="40" rows="3" cssClass="selectwk" id="resubmitRemarks"
												value="%{applicationDetails.rejectionRemarks}" />
							</td>
							<td colspan="2" class="greybox2wk">&nbsp;</td>
						</tr>
					</table>
				</s:if>
				<br/>			
				<div class="buttonholdersearch" align = "center">
					<s:if test="%{mode == @org.egov.works.utils.DepositWorksConstants@VIEW_TECHNICAL_DETAILS}">
						<input type="button" class="buttonadd" value="View Document" id="docViewButton" onclick="viewDocument();return false;" />
					</s:if>
					<s:else>
			      		<input type="button" class="buttonadd" value="Attach Document" id="docUploadButton" onclick="showDocumentManager();return false;" />
			      	</s:else>
			      	<s:if test="%{mode == @org.egov.works.utils.DepositWorksConstants@UPDATE_TECHNICAL_DETAILS}">
			      		<s:submit value="Update Technical Details" cssClass="buttonfinal" id="saveButton" method="updateTechnicalDetails" name="button" onclick="return validateDataBeforeSubmit();" />
			      	</s:if>
			      	<s:elseif test="%{mode == @org.egov.works.utils.DepositWorksConstants@MODIFY_TECHNICAL_DETAILS}">
			      		<s:submit value="Modify Technical Details" cssClass="buttonfinal" id="saveButton" method="updateTechnicalDetails" name="button" onclick="return validateDataBeforeSubmit();" />
			      	</s:elseif>
			      	<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
				</div>
				</div>
				</div>
				</div>
				</div>
			</s:push>
		</s:form>		
	</body>
</html>