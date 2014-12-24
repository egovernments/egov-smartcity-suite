<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import=" org.egov.infstr.utils.AppConfigTagUtil" %>
<%@ page language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/registerComplaint.js"></script>	
<head>
	<title>Register Complaint</title>
</style>

</head>

<body class="yui-skin-sam">
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />
			<s:fielderror />
		</div>
	</s:if>
<div id="jserrorid" class="errorstyle" style="display:none" >
	<p class="error-block" id="lblError" ></p>
</div>
	<s:if test="%{hasActionMessages()}">
		<div class="messagestyle">
			<s:actionmessage theme="simple" />
		</div>
	</s:if>

	<s:form action="registerComplaint" theme="simple" name="registerComplaintForm" validate="true" enctype="multipart/form-data" method="post">
	
		<s:push value="model">
		

		<table  width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table  width="100%" border="0"
							cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img
											src="${pageContext.request.contextPath}/images/arrow.gif" />
									</div>
									<div class="headplacer" align="left">
										<s:text name="complaintType"/>
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr><tr>
				<table width="100%">
						<jsp:include page="../common/complaintType.jsp"/>
				</table></tr>
				</table>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table  width="100%" border="0"
							cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img
											src="${pageContext.request.contextPath}/images/arrow.gif" />
									</div>
									<div class="headplacer" align="left">
										<s:text name="header.complaintDetails"/>
										
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<table width="100%">
					<tr>
						<td width="25%" class="whiteboxwk"> <s:text name="compReceivingModes" /> <span class="mandatory" >*</span></td>
						<td width="25%" class="whitebox2wk">
						
							<s:radio  list="modeList" listKey="id" listValue="compMode"  name="compReceivingModes.id" 
							id="compReceivingModes" onchange="receivingCenterEvent(this);" value="%{defaultMode}"> 	</s:radio>
						
						</td>
					
					</tr>
					<tr>
						
						<td width="25%" class="greyboxwk"> <s:text name="receivingCenter" /> <span class="mandatory" id="receivingCenterMandate" style="visibility: hidden" >*</span></td>
						<td width="25%" class="greybox2wk">
						<s:select name="receivingCenter" id="receivingCenter" list="dropdownData.receivingCenter" listKey="id" 
						listValue="centerName" headerKey="-1" headerValue="----Choose----"  value="%{receivingCenter.id}" disabled="true"/>
							
						</td>
						<td width="25%" class="greyboxwk"> <s:text name="registrationNum"/>  </td>
						<td width="25%" class="greybox2wk">
						<s:textfield name="complaintNumber" id="complaintNumber"></s:textfield>
						</td>
					</tr>
					<tr>
						<td width="25%" class="whiteboxwk"> <s:text name="title" /> <span class="mandatory">*</span></td>
						<td width="25%" class="whitebox2wk">
						<s:textarea name="title" id="title" rows="1" cols="40"></s:textarea>
						</td>
						
						<td width="25%" class="whiteboxwk"> <s:text name="details" ></s:text><span class="mandatory">*</span></td>
						<td width="25%" class="whitebox2wk">
						<s:textarea name="details" id="details" rows="2" cols="60"></s:textarea>
						</td>
						
					</tr>
					
					<tr>
						
						
						<td width="25%" class="greyboxwk"><s:text name="initials" /> </td>
						<td width="25%" class="greybox2wk">
						<s:textfield name="initials" id="initials"></s:textfield>
						</td>

						<td width="25%" class="greyboxwk"><s:text name="firstName" /> <span class="mandatory" >*</span></td>
						<td width="25%" class="greybox2wk">
						<s:textfield name="firstName" id="firstName"></s:textfield>
						</td>
						
					</tr>
					
						
					
					<tr>
						
						
						<td width="25%" class="whiteboxwk"><s:text name="lastName"/></td>
						<td width="25%" class="whitebox2wk">
						<s:textfield name="lastName" id="lastName"></s:textfield>
						</td>
						<td width="25%" class="whiteboxwk"><s:text name="address" /><span class="mandatory">*</span></td>
						<td width="25%" class="whitebox2wk">
						<s:textarea name="address" id="address" cols="50" rows="3" />
						</td>
					</tr>
					
					
					
					<tr>
						
						<td width="25%" class="greyboxwk"><s:text name="pincode"/></td>
						<td width="25%" class="greybox2wk">
						<s:textfield name="pincode" id="pincode" />
						</td>
						<td width="25%" class="greyboxwk"><s:text name="telePhone"/></td>
						<td width="25%" class="greybox2wk">
						<s:textfield name="telePhone" id="telePhone"  />
						</td>
						
						
					</tr>
					
					
					
					<tr>
						
						<td width="25%" class="whiteboxwk"><s:text name="mobileNumber"/></td>
						<td width="25%" class="whitebox2wk">
						<s:textfield name="mobileNumber" id="mobileNumber" />
						</td>
						<td width="25%" class="whiteboxwk"><s:text name="email"/></td>
						<td width="25%" class="whitebox2wk">
						<s:textfield name="email" id="email"  />
						</td>
						
					</tr>
					
					<tr>
						
					<td  width="25%" class="greyboxwk"><s:text name="header.locationDetails"/> </td>
<td width="25%" class="greybox2wk"><s:textarea rows="3" cols="50" name="complaintlocation" id="complaintlocation"></s:textarea></td>
					
						<td width="25%" class="greyboxwk"><s:text name="fileUpload.importFile"/> </td>
						<td width="25%" class="greybox2wk">
						<input type="button" class="button" value="Upload Document" id="docUploadButton" onclick="showDocumentManager();" /></td>
					<s:hidden name="imageDocNumber" id="docNumber" />
						
						
						<td width="25%" class="whiteboxwk"></td><td width="25%" class="whitebox2wk"></td>
					
					</tr>
				</table>
				</table>


				
				
				<table  width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table  width="100%" border="0"
							cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img
											src="${pageContext.request.contextPath}/images/arrow.gif" />
									</div>
									<div class="headplacer" align="left">
										<s:text name="header.location"/>
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<table width="100%">
							
					<jsp:include page="../common/boundary.jsp"/>
					
				</table>
				</table>
				
				
			<table  width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table  width="100%" border="0"
							cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img
											src="${pageContext.request.contextPath}/images/arrow.gif" />
									</div>
									<div class="headplacer" align="left">
										<s:text name="header.map"/>
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<table border="1" width="870" align="center" bgcolor="#EFEFEF"  cellspacing="0"  height="5"><tr>
					<TD width="100" height="300"><div id="map_canvas" style="width:100%; height:100%"></div> </TD></tr>
         	 			<c:import url="/commons/googleMap.jsp" context="/egi">
							<c:param name="latitude"  value='<%= AppConfigTagUtil.getAppConfigValue("CITY_LATITUDE","egi",this.getServletContext()) %>'></c:param>
	      		 			<c:param name="longitude"  value='<%= AppConfigTagUtil.getAppConfigValue("CITY_LONGITUDE","egi",this.getServletContext()) %>'></c:param>
	      		 			<c:param name="replaceMarker"  value='<%= Boolean.TRUE.toString() %>'></c:param>
	      		 		</c:import>
	      		 		<s:hidden name="geoLocationDetails.geoLatLong.latitude" id="latitude" ></s:hidden>
	      		 		<s:hidden name="geoLocationDetails.geoLatLong.longitude" id="longitude" ></s:hidden>
	      		 		
				
				</table> 
			
				</table>
				
			
				
				<br><br>
				 <div class="rbroundbox2">
				
					
				</div>
			
			<table width="100%">
							
							<tr>
								<td colspan="4">
									<div class="buttonholderwk">
									<s:submit type="submit" id="save" value="SAVE" cssClass="buttonfinal" method="saveAndView"  />
										
										<input type="button" class="buttonfinal" value="CLOSE"
											id="closeButton" name="button" onclick="window.close();" />
									</div>
								</td>
							</tr>
							
				</table>
				</s:push>
	</s:form>
<script>

function showDocumentManager(){
		var docNum= document.getElementById("docNumber").value;
  		var url;
  		if(docNum==null||docNum==''||docNum=='To be assigned'){
       			 url="/egi/docmgmt/basicDocumentManager.action?moduleName=pgr";
  		} else {
       			 url = "/egi/docmgmt/basicDocumentManager!editDocument.action?docNumber="+docNum+"&moduleName=pgr";
 		 }
     		 window.open(url,'docupload','width=1000,height=400');
}
</script>


</body>
</html>