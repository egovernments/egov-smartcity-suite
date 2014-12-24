<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import=" org.egov.infstr.utils.AppConfigTagUtil" %>
<%@ page language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="/security" %>
<html>

<head>
	<title><s:text name="header.view.complaint" /></title>
</style>

</head>

<body class="yui-skin-sam">
	<s:form theme="simple">
	<s:push value="model">
			<table  width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table  width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img
											src="${pageContext.request.contextPath}/images/arrow.gif" />
									</div>
									<div class="headplacer" align="left">
										<s:text name="subheader.view.complaint.Person.details"></s:text>
										
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr><tr>
				<table width="100%">
							
					<tr>
						<td width="25%" class="whiteboxwk" > <s:text name="view.complaint.number"></s:text> </td>
						<td width="25%" class="whitebox2wk"> <s:property value="complaintNumber" default="N/A"/></td>
						<s:date name="complaintDate" id="complaintDateId" format="dd/MM/yyyy" />
						<td width="25%" class="whiteboxwk"><s:text name="view.complaint.date" ></s:text> </td>
						<td width="25%" class="whitebox2wk"> <s:property default="N/A" value='%{complaintDateId}' /> </td>
					</tr>
					<tr>
						<td width="25%" class="greyboxwk"> <s:text name="view.complaint.name"></s:text> </td>
						<td width="25%" class="greybox2wk"> <s:property value="firstName" default="N/A"/> </td>
						<td width="25%" class="greyboxwk"> <s:text name="view.complaint.email"></s:text> </td>
						<td width="25%" class="greybox2wk"> <s:property value="email" default="N/A"/> </td>
					</tr>
					
					<tr>
						<td width="25%" class="whiteboxwk"> <s:text name="view.complaint.address"></s:text> </td>
						<td width="25%" class="whitebox2wk"> <s:property value="address" default="N/A"/> </td>
						<td width="25%" class="whiteboxwk"> <s:text name="view.complaint.pinCode"></s:text> </td>
						<td width="25%" class="whitebox2wk"><s:property value="pincode" default="N/A" />  </td>
					</tr>
					<tr>
						<td width="25%" class="greyboxwk"> <s:text name="view.complaint.phoneNumber"></s:text> </td>
						<td width="25%" width="25%" class="greybox2wk"> <s:property default="N/A"  value="telePhone" /> </td>
						<td width="25%" class="greyboxwk"> <s:text name="view.complaint.mobileNumber"></s:text> </td>
						<td width="25%" class="greybox2wk"> <s:property value="mobileNumber" default="N/A" /> </td>
					</tr>
				</table></tr>
				</table>
				
				<table  width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table  width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img
											src="${pageContext.request.contextPath}/images/arrow.gif" />
									</div>
									<div class="headplacer" align="left">
										<s:text name="subheader.view.complaint"></s:text>
										
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr><tr>
				<table width="100%">
							
					<tr>
						<td width="25%" class="whiteboxwk" > <s:text name="view.complaint.dept"></s:text> </td>
						<td width="25%" class="whitebox2wk"> <s:property value="department.deptName" default="N/A"/></td>
						
						<td width="25%" class="whiteboxwk"><s:text name="view.complaint.loc" ></s:text> </td>
						<td width="25%" class="whitebox2wk"> <s:property default="N/A" value='complaintlocation' /> </td>
					</tr>
					<tr>
						<td width="25%" class="greyboxwk"> <s:text name="view.complaint.type"></s:text> </td>
						<td width="25%" class="greybox2wk"> <s:property value="complaintType.name" default="N/A"/> </td>
						<td width="25%" class="greyboxwk"> <s:text name="view.complaint.title"></s:text> </td>
						<td width="25%" class="greybox2wk"> <s:property value="title" default="N/A"/> </td>
					</tr>
					
					<tr>
						<td width="25%" class="whiteboxwk"> <s:text name="view.complaint.mode"></s:text> </td>
						<td width="25%" class="whitebox2wk"> <s:property value="compReceivingModes.compMode" default="N/A"/> </td>
						<td width="25%" class="whiteboxwk"> <s:text name="view.complaint.center"></s:text> </td>
						<td width="25%" class="whitebox2wk"><s:property value="receivingCenter.centerName" default="N/A" />  </td>
					</tr>
					<tr>
						<td width="25%" class="greyboxwk"> <s:text name="view.complaint.details"></s:text> </td>
						<td width="25%" width="25%" class="greybox2wk"> <s:property default="N/A"  value="details" /> </td>
						<td width="25%" class="greyboxwk"> <s:text name="view.complaint.viewed.by"></s:text> </td>
						<td width="25%" class="greybox2wk"> <s:property value="%{getViewedBy(complaintNumber)}" default="N/A" /> </td>
					</tr>
					
					<tr>
						<td width="25%" class="whiteboxwk">Document Number </td>
						<td width="25%" class="whitebox2wk">
						<s:if test="imageDocNumber!=null">
						<a href='#' target="_parent" onclick="window.open('/egi/docmgmt/documentManager!viewDocument.action?docNumber=${imageDocNumber}&moduleName=pgr','dataitem','resizable=yes,scrollbars=yes,height=700,width=800,status=yes');">
						${imageDocNumber}</a>
						</s:if><s:else>N/A</s:else>
						 </td>
						
					</tr>
					
				</table></tr>
				</table>
				
				<table  width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table  width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="4" class="headingwk">
									<div class="arrowiconwk">
										<img
											src="${pageContext.request.contextPath}/images/arrow.gif" />
									</div>
									<div class="headplacer" align="left">
										<s:text name="subheader.view.complaint.Person.details"></s:text>
										
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			<table width="100%">		
				
			<sec:authorize access="isFullyAuthenticated()">
			<s:if test="%{null!=mode && mode.equalsIgnoreCase('wf') && !state.value.equalsIgnoreCase('END')}" >
				<jsp:include page="../common/boundary.jsp"/>
				<tr>
						
					<td width="25%" class="greyboxwk"> Priority </td>
						<td width="25%" class="greybox2wk">
						<s:select name="priority" id="priority" list="dropdownData.priorityList" listKey="id" 
						listValue="name" headerKey="-1" headerValue="----Choose----"  value="%{priority.id}" />
							
						</td>
						<td width="25%" class="greyboxwk">Status </td>
						<td width="25%" class="greybox2wk">
						<s:select name="redressal.complaintStatus" id="status" list="dropdownData.statusList" listKey="id" 
						listValue="name" headerKey="-1" headerValue="----Choose----"  value="%{redressal.complaintStatus.id}" />
						</td>
					</tr>
				<jsp:include page="../common/workflow.jsp"/>
				<tr>
				<td  class="whiteboxwk">Comments</td> 
				<td  class="whitebox2wk" ><s:textarea name="comments" id="comments" cols="150" rows="3" onblur="checkLength(this)" value="%{state.text1}"/></td>
			</tr>
			</s:if>
			</sec:authorize>
			<div id="wfHistoryDiv">
	  			<c:import url="/WEB-INF/jsp/workflow/workflowHistory.jsp" context="/egi">
	        		<c:param name="stateId" value="${state.id}"></c:param>
			        </c:import>
	        	</div>
					
				</table>
				
				</table>
			<s:if test="%{null !=geoLocationDetails &&  null!= geoLocationDetails.geoLatLong && null!=  geoLocationDetails.geoLatLong.latitude
          		               && null!=  geoLocationDetails.geoLatLong.longitude}">
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
							<c:param name="latitude"  value="${geoLocationDetails.geoLatLong.latitude}"></c:param>
	       					<c:param name="longitude"  value="${geoLocationDetails.geoLatLong.longitude}"></c:param>
	       					<c:param name="replaceMarker"  value="<%= Boolean.FALSE.toString() %>"></c:param>
	      		 		</c:import>
	      
				</table> 
			
				</table>
				</s:if>
					<table width="100%">
							
							<tr>
								<td colspan="4">
									<div class="buttonholderwk">
									<sec:authorize access="isFullyAuthenticated()">
		<s:if test="%{mode.equalsIgnoreCase('wf') && !state.value.equalsIgnoreCase('END')}" >
									<s:submit type="submit" id="save" value="SAVE" cssClass="buttonfinal" method="saveOrForward"  onClick="setActionName(this);"/>
					<s:submit type="submit" id="forward" value="FORWARD" cssClass="buttonfinal" method="saveOrForward" disabled="true" 
onClick="setActionName(this);"/>
		</s:if></sec:authorize>
										<input type="button" class="buttonfinal" value="CLOSE"
											id="closeButton" name="button" onclick="window.close();" />
									</div>
								</td>
							</tr>
						<s:hidden name="actionName" id="actionName"/>	
						<s:hidden name="model.id" id="model.id"/>
				</table>
				</s:push>
	</s:form>
	

<script>
function checkLength(obj){
	if(obj.value.length>1024)
	{
		alert('Max 1024 characters are allowed for comments. Remaining characters are truncated.')
		obj.value = obj.value.substring(1,1024);
	}
}

function setActionName(obj){

	dom.get('actionName').value = obj.value;
	
}
</script>
</body>
</html>