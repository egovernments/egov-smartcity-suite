<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
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
  --%>

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/taglib/cdn.tld" prefix="cdn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="row" id="page-content">
	<div class="errorstyle" id="egi_error_area" style="display: none;"></div>

	<div class="errorstyle"></div>

	<div class="col-md-12">
				<c:if test="${not empty message}">
					<div id="message" class="success"><spring:message code="${message}"/></div>
				</c:if>
				<form:form mothod="post"
					class="form-horizontal form-groups-bordered"
					modelAttribute="appConfig" id="appConfigForm">
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading">
							<div class="panel-title">
								<strong><spring:message code="lbl.hdr.createAppconfig" /></strong>
							</div>
						</div>
						<div class="panel-body custom-form">
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.module"/><span class="mandatory"></span></label>
								<div class="col-sm-6 add-margin">
									<form:select path="module" id="appModuleName"
										cssClass="form-control" cssErrorClass="form-control error"
										required="required">
										<form:option value="">
											<spring:message code="lbl.select" />
										</form:option>
										<form:options items="${modulesList}" itemValue="id"
											itemLabel="name" />
									</form:select>
									<form:errors path="module" cssClass="add-margin error-msg" />
									<div class="error-msg positionerror all-errors display-hide"></div>
								</div>
							</div>
							<div class="form-group">
								<label for="field-1" class="col-sm-3 control-label"><spring:message code="lbl.AppconfigKeyName"/><span class="mandatory"></span>
								</label>

								<div class="col-sm-6">
									<form:input path="keyName" id="keyName" type="text"
										class="form-control low-width" placeholder=""
										autocomplete="off" required="required" />
									<form:errors path="keyName" cssClass="add-margin error-msg" />
								</div>

							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label"><spring:message code="lbl.description"/><span class="mandatory"></span></label>
								<div class="col-sm-6">
									<form:input path="description" id="description" type="text"
										class="form-control low-width" placeholder=""
										autocomplete="off" required="required" />
									<form:errors path="description" cssClass="add-margin error-msg" />
								</div>
							</div>
							<div class="col-md-12">
								<table class="table table-bordered" id="floorDetails">
									<tr>
										<th><spring:message code="lbl.date"/><span class="mandatory"></span></th>
										<th><spring:message code="lbl.values"/><span class="mandatory"></span></th>
										<th><spring:message code="lbl.operation"/></th>
									</tr>
									<c:choose>
										<c:when test="${!appConfig.appDataValues.isEmpty()}">
											<c:forEach items="${appConfig.appDataValues}" var="var1"
												varStatus="counter">
												<tr id="Floorinfo">
													<td class="blueborderfortd"><fmt:formatDate
															value="${var1.effectiveFrom}" var="historyDate"
															pattern="dd/MM/yyyy" /> <input type="text"
														class="form-control datepicker" value="${historyDate}"
														name="appDataValues[${counter.index}].effectiveFrom"
														name="appDataValues[${counter.index}].effectiveFrom"
														required="required" onblur="validateDate(this);" /></td>
													<td class="blueborderfortd"><input type="text"
														class="form-control low-width" value="${var1.value}"
														name="appDataValues[${counter.index}].value"
														id="appDataValues[${counter.index}].value"
														required="required"
														onblur="checkSplCharIncludingFewSplchar(this)"> <input
														type="hidden" id="cmdaddListId"
														value="appDataValues[${counter.index}].id" /></td>
													<td id="rowadddelete"><input type="button"
														class="btn btn-primary" value="Add" name="Add" id="add"
														onclick="javascript:addRow1(); return false;"> <input
														type="button" class="btn btn-primary" name="Delete"
														value="Delete" id="delete"
														onclick="javascript:delFloor(this);return false;"></td>
												</tr>
											</c:forEach>
										</c:when>
									</c:choose>
								</table>
							</div>
							<div class="col-md-12 text-center">
								<div class="add-margin">
									<button type="submit" class="btn btn-primary"><spring:message code="lbl.submit"/></button>
									<button type="reset" class="btn btn-default">
										<spring:message code="lbl.reset" />
									</button>
									<button type="button" class="btn btn-default"
										data-dismiss="modal" onclick="self.close()">
										<spring:message code="lbl.close" />
									</button>
								</div>
							</div>
						</div>
					</div>
				</form:form>
			</div>
</div>
<script	src="<cdn:url cdn='${applicationScope.cdn}'  value='/resources/js/app/appconfig.js?rnd=${app_release_no}'/>"></script>

<script>
			var cmdaindex=0;
			var count=0;
	var moduleid = '${appConfig.module.id}';
	if(moduleid !== ''){
		$("#moduleid").val(moduleid);
	}

	
	 function addRow1() 
     {
	   
             var table = document.getElementById('floorDetails');

             var rowCount = table.rows.length;
             var row = table.insertRow(rowCount);
             var counts = rowCount - 1;
             var newRow = document.createElement("tr");
             
           /*   var newCol = document.createElement("td");
             newRow.appendChild(newCol);
       		var cell1 = row.insertCell(0);
       		cell1.innerHTML = counts+1; */
             
         	var newCol = document.createElement("td");
			newRow.appendChild(newCol);
			 
             var cell1 = row.insertCell(0);
			
             var houseNo = document.createElement("input");
             var att = document.createAttribute("class");
      		att.value="form-control datepicker";
      		houseNo.setAttributeNode(att); 
            // houseNo.setAttribute("class","form-control datepicker");
            houseNo.type = "text";
            houseNo.setAttribute("required", "required");
          //  houseNo.className = "form-control datepicker";
          	houseNo.setAttribute("maxlength", "10");
        	houseNo.setAttribute("data-inputmask","'mask': 'd/M/y'");
             houseNo.setAttribute("dateFormat", "dd/MM/yyyy");
             houseNo.setAttribute("onblur", "validateDate(this);");
            houseNo.name = "appDataValues[" + counts + "].effectiveFrom";
            cell1.appendChild(houseNo);
             
             var newCol = document.createElement("td");
 			newRow.appendChild(newCol);
             var cell2 = row.insertCell(1);
             var street = document.createElement("input");
             street.setAttribute("class","form-control low-width");
             street.type = "text";
             street.setAttribute("required", "required");
             street.name = "appDataValues[" + counts + "].value";
             street.setAttribute("onblur", "checkSplCharIncludingFewSplchar(this);");
            cell2.appendChild(street);
             
             var newCol = document.createElement("td");
  			newRow.appendChild(newCol);
              var cell3 = row.insertCell(2);
         
             var addButton = document.createElement("input");
             addButton.type = "button";
             addButton.setAttribute("class", "btn btn-primary");
             addButton.setAttribute("onclick", "return addRow1();");
             addButton.setAttribute("value", "Add");
             cell3.appendChild(addButton);
            
             
              var x = document.createElement("LABEL");
              var t = document.createTextNode(" ");
                 cell3.appendChild(t);
                     
             var addButton = document.createElement("input");
             addButton.type = "button";
             addButton.setAttribute("class", "btn btn-primary");
             addButton.setAttribute("onclick", "return delFloor(this);");
             addButton.setAttribute("value", "Delete");
             cell3.appendChild(addButton);

             var hiddenId = document.createElement("input");
             hiddenId.type = "hidden";
             hiddenId.id = "appDataValues[" + counts + "].id";
            // hiddenId.name = "appDataValues[" + counts + "].id";
             hiddenId.setAttribute("value", "${appDataValues[" + counts + "].id}");
             cell3.appendChild(hiddenId);
             
             $(".datepicker").datepicker({
     			format: "dd/mm/yyyy"
     		}); 

     }

	
     
	function delFloor(obj)
	{
				var tb1=document.getElementById("floorDetails");
		        var lastRow = (tb1.rows.length)-1;
		        var curRow=getRow(obj).rowIndex;
		        var counts = lastRow - 1;
		         if(lastRow ==1)
		      	{
		        	 bootbox.alert('Cannot delete this row');
		   			
		      	     return false;
		        }
		      	else
		      	{
		      		var updateserialnumber=curRow;
		      		for(updateserialnumber;updateserialnumber<tb1.rows.length-1;updateserialnumber++)
		 			{
		 				if(counts!=null)
		 					counts=updateserialnumber;
		 			}
		 			
		      		tb1.deleteRow(curRow);
		 			return true;
		      }
		  
  				
	}
	
	
</script>
