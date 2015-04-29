<%@ include file="/includes/taglibs.jsp"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="egov"%>
<%@ taglib uri="/WEB-INF/struts-tags.tld" prefix="s"%>
<html>
	<head>
		<title>
			<s:text name="dw.bpa.view.tech.details.title"/>
		</title>
	</head>
	<script src="<egov:url path='js/works.js'/>"></script>
	<script src="<egov:url path='js/helper.js'/>"></script>
	<script type="text/javascript">
		function disableElements() {
			dom.get("zoneId").value = '<s:property value="%{zoneId}"/>';
			dom.get("wardId").value = '<s:property value="%{wardId}"/>';
			dom.get("areaId").value = '<s:property value="%{areaId}"/>';
			dom.get("localityId").value = '<s:property value="%{localityId}"/>';
			dom.get("streetId").value = '<s:property value="%{streetId}"/>';
			dom.get("depth").value = roundTo('<s:property value="%{applicationDetails.applicationRequest.roadCutDetailsList[0].roadDepth}"/>',2);
			for(var i=0;i<document.forms[0].length;i++) {
	      		document.forms[0].elements[i].disabled =true;
	      	}
			
			if (document.viewtechnicalDetailsForm.docViewButton != null) {
				document.viewtechnicalDetailsForm.docViewButton.readonly = false;
				document.viewtechnicalDetailsForm.docViewButton.disabled = false;
			}
			if (document.viewtechnicalDetailsForm.closeButton != null) {
				document.viewtechnicalDetailsForm.closeButton.readonly = false;
				document.viewtechnicalDetailsForm.closeButton.disabled = false;
			}
		}

		function showBPAViewPage(bpaNumber) {
			window.open("/bpa/register/registerBpa!viewRegForm.action?planSubmissionNum=" + bpaNumber, "",
							"height=600,width=1200,scrollbars=yes,left=0,top=0,status=yes");
		}

		function viewDocument(){
			  viewDocumentManager(dom.get("docNumber").value); 
		}
	</script>
	<script src="<egov:url path='js/works.js'/>"></script>
	<body onload="disableElements();">
		<s:if test="%{hasErrors()}">
	        <div id="errorstyle" class="errorstyle" >
	          <s:actionerror/>
	          <s:fielderror/>
	        </div>
	    </s:if>
	    <div class="errorstyle" id="citizenPortal_error" style="display: none;"></div>
		<s:form name="viewtechnicalDetailsForm" action="bpaRoadCut" theme="simple">
			<div class="formmainbox">
			<div class="insidecontent">
			<div class="rbroundbox2">
			<div class="rbtop2"><div></div></div>
			<div class="rbcontent2">
			<s:hidden name="applicationDetails.applicationRequest.documentNumber" value="%{applicationDetails.applicationRequest.documentNumber}" id="docNumber" />
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
			            	    list="dropdownData.purposeOfRoadCutList" value="%{applicationDetails.applicationRequest.purposeOfRoadCut}"/>
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
								headerKey="-1" headerValue="--Select--" style="width:60px"/>
							</td>
							<td class="whiteboxwk">
								<s:select list="dropdownData.wards" name="wardId" 
								value="%{wardId}" listKey="id" listValue="name" id="wardId"
								headerKey="-1" headerValue="--Select--" style="width:60px"/>
							</td>
							<td class="whiteboxwk">
								<s:select list="dropdownData.areas" name="areaId" 
								value="{areaId}" listKey="id" listValue="name" id="areaId"
								headerKey="-1" headerValue="--Select--" style="width:150px"/>
							</td>
							<td class="whiteboxwk">
								<s:select list="dropdownData.localities" name="localityId" 
								value="%{localityId}" listKey="id" listValue="name" id="localityId"
								headerKey="-1" headerValue="--Select--" style="width:150px"/>
							</td>
							<td class="whiteboxwk">
								<s:select list="dropdownData.streets" name="streetId" 
								value="%{streetId}" listKey="id" listValue="name" id="streetId"
								headerKey="-1" headerValue="--Select--" style="width:250px"/>
							</td>
							<td class="whiteboxwk">
								<s:textfield id="roadLength" name="applicationDetails.applicationRequest.roadCutDetailsList[0].roadLength" cssClass="selectmultilinewk" 
									value="%{applicationDetails.applicationRequest.roadCutDetailsList[0].roadLength}"/>
							</td>
							<td class="whiteboxwk">
								<s:textfield id="breadth" name="applicationDetails.applicationRequest.roadCutDetailsList[0].roadBreadth" cssClass="selectmultilinewk" 
									value="%{applicationDetails.applicationRequest.roadCutDetailsList[0].roadBreadth}"/>
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
				<br/>			
				<div class="buttonholdersearch" align = "center">
			      	<input type="button" class="buttonadd" value="View Document" id="docViewButton" onclick="viewDocument();return false;" />
			      	<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();"/>
				</div>
				</div>
				</div>
				</div>
				</div>
		</s:form>		
	</body>
</html>