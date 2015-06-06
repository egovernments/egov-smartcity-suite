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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<div class="row" id="page-content">

	<div class="col-md-12">
		<div class="panel" data-collapsed="0">
			<div class="panel-body">
				 <c:if test="${not empty message}">
                    <div id="message" class="success">${message}</div>
                </c:if>
		<form:form  mothod ="post" class="form-horizontal form-groups-bordered" modelAttribute="appConfig" id="appConfigForm" >
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">
						<strong><spring:message code="lbl.hdr.createAppconfig"/></strong>
					</div>
				</div> 
			
				<div class="panel-body custom-form">
					<div class="form-group">
								<label for="field-1" class="col-sm-3 control-label">Key Name<span class="mandatory"></span></label></label>
								
								<div class="col-sm-6">
							<form:input path="keyName" id="keyName" type="text" class="form-control low-width" placeholder="" autocomplete="off" required="required"/>
                         <form:errors path="keyName" cssClass="add-margin error-msg"/>
						</div>
								
							</div>
							
							<div class="form-group">
								
								<label class="col-sm-3 control-label">Description<span class="mandatory"></span></label>
								<div class="col-sm-6">
							<form:input path="description" id="description" type="text" class="form-control low-width" placeholder="" autocomplete="off" required="required" />
                            <form:errors path="description" cssClass="add-margin error-msg"/>
						</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">Module<small><i
									class="entypo-star error-msg"></i></small></label>
								<div class="col-sm-6">
									<input id="module" name="appConfig.module.name"  type="text" value="${appConfig.module.name}"  class="form-control low-width" placeholder="" autocomplete="off" required="required"/>
									<form:hidden path="module" id="moduleid"/>
									<form:errors path="module" cssClass="add-margin error-msg"/>
									<div class="error-msg positionerror all-errors display-hide"></div>
								</div>
								
								</div>
						
					
					<div class="col-md-12">
					 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table table-bordered"   id="floorDetails" >
      				  <tr>
						<th>Date</th>
							<th>Values</th>
								<th>Operation</th>
							  </tr>
							<tr id="Floorinfo">
										<c:choose>
											<c:when test="${!appConfig.appDataValues.isEmpty()}">
												<c:forEach items="${appConfig.appDataValues}" var="var1" varStatus="counter">
													<td class="blueborderfortd">	
											
											 <input type="text" class="form-control datepicker" value="${var1.value}"  name="appDataValues[${counter.index}].effectiveFrom" 
											 id="appDataValues[${counter.index}].effectiveFrom" required="required" data-inputmask="'mask': 'd/m/y'" >
												</input></td>
											<td class="blueborderfortd">	
											
											 <input type="text" class="form-control low-width"  value="${var1.value}" name="appDataValues[${counter.index}].value" 
											 id="appDataValues[${counter.index}].value" required="required" >
												</input></td>
												
												 	
										<td><img id="addF" name="addF"
														src="${pageContext.request.contextPath}/images/addrow.gif"
														alt="Add" onclick="javascript:addFloor(); return false;"
														width="18" height="18" border="0" /> &nbsp;<img
														id="dDelF" name="dDelF"
														src="${pageContext.request.contextPath}/images/removerow.gif"
														alt="Remove"
														onclick="javascript:delFloor(this);return false;"
														width="18" height="18" border="0" /> 
														</td>
														<input type="hidden"
														id="cmdaddListId" value="appDataValues[${counter.index}].id" />
													
												</c:forEach>
											</c:when>

										</c:choose>
									</tr>
								
			
					  </table>
					
					</div>
					
					<div class="col-md-12 text-center">
						<div class="add-margin">
							<button type="submit" class="btn btn-primary">Submit</button>
							   <button type="reset" class="btn btn-default"><spring:message code="lbl.reset"/></button>
							<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()">Close</a>
						</div>
					</div>
					</div>
					</form:form>
				</div>
				</div>
			</div>
		
		
		
		</div>	
	<script src="<c:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>	
	
	<script src="<c:url value='/resources/global/js/jquery/plugins/exif.js' context='/egi'/>"></script>
	<script src="<c:url value='/resources/global/js/bootstrap/bootstrap.js' context='/egi'/>"></script>
	<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>"></script>
<script src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<script src="<c:url value='/commonjs/ajaxCommonFunctions.js' context='/egi'/>"></script>


<script src="<c:url value='/resources/js/app/appconfig.js' context='/egi'/>"></script>
			<script>
			var cmdaindex=0;
	var moduleid = '${appConfig.module.id}';
	if(moduleid !== ''){
		$("#moduleid").val(moduleid);
	}

	function addFloor()
	{   
			var tableObj=document.getElementById('floorDetails');
			//var	cmdaindex=tableObj.rows.length-1;
			var tbody=tableObj.tBodies[0];
			var lastRow = tableObj.rows.length;
			var rowObj = tableObj.rows[1].cloneNode(true);
			tbody.appendChild(rowObj);
			var rowno = parseInt(tableObj.rows.length)-2;
			   document.forms[0].effectiveFrom[lastRow-1].value="";
			document.forms[0].value[lastRow-1].value="";
		 
			document.forms[0].effectiveFrom[lastRow-1].setAttribute("name","appConfig.appDataValues["+cmdaindex+"].effectiveFrom");
		  	 document.forms[0].value[lastRow-1].setAttribute("name","appConfig.appDataValues["+cmdaindex+"].value");
		document.forms[0].cmdaddListId[lastRow-1].setAttribute("name","appConfig.appDataValues["+cmdaindex+"].id");		    
			cmdaindex++;
			
		 }
	function delFloor(obj)
	{
				var tb1=document.getElementById("floorDetails");
		        var lastRow = (tb1.rows.length)-1;
		        var curRow=getRow(obj).rowIndex;
		         if(lastRow ==1)
		      	{
		     		 alert('you canont delete this row ');
		   			
		      	     return false;
		        }
		      	else
		      	{
		      		// alert(curRow);
		      		var updateserialnumber=curRow;
		 			/* for(updateserialnumber;updateserialnumber<tb1.rows.length-1;updateserialnumber++)
		 			{
		 				if(document.forms[0].srlNo[updateserialnumber]!=null)
		 					document.forms[0].srlNo[updateserialnumber].value=updateserialnumber;
		 			} */
		 			
		 			tb1.deleteRow(curRow);
		 			return true;
		      }
		  
  				
	}
	
	
</script>	
