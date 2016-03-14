<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<form:form method ="post" action="" class="form-horizontal form-groups-bordered"  id="dcbmig-view"
			cssClass="form-horizontal form-groups-bordered"
			enctype="multipart/form-data">
			
					<div class="panel panel-primary" data-collapsed="0">
						<div class="panel-heading">
						</div>
					
					<div class="panel-body custom-form ">
					<div class="form-group" align ="center">
								For Consumer Number :<c:out value="${consumerNumber}" />
								</div>
 					
	              <c:choose>
    <c:when test="${waterChargesReceiptInfo.isEmpty()}">
    <div class="form-group" align ="center">
       No Receipts are available        
     </div>
     </c:when>
    <c:otherwise>
      <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="table table-bordered">
                  <thead>
					<tr>
						<th colspan="6">
                         Receipts
						</th>
					</tr>
					<tr>
						<th colspan="1" >
							<div align="center">
						 <spring:message code="lbl.bookno" /> 
						    </div>
					
							
						</th>
						<th   colspan="1" >
						   <div align="center">
						<spring:message code="lbl.receiptNo" /> 
						    </div>
							
						</th>
						<th   colspan="1" >
						   <div align="center">
						<spring:message code="lbl.receiptDate" />
						   </div>
							
						</th>
						<th  align="center" colspan="1" >
						   <div align="center">
						<spring:message code="lbl.fromDate" />
						    </div>
						
						</th>
						<th   colspan="1" >
						   <div align="center">
						<spring:message code="lbl.toDate" />
						    </div>
					
						</th>
						<th   colspan="1" >
						    <div align="center">
						<spring:message code="lbl.receiptAmt" />
						    </div>
						
						</th>
					</tr>
					</thead>
					<c:forEach var="receipt" items="${waterChargesReceiptInfo}" >
					<tr>
							<td  colspan="1" >
								<div align="center">
								<c:out value="${receipt.bookNumber}" />
								<!-- 	<s:property value="bookNumber" /> -->
								</div>
							</td>
							<td  colspan="1" >
								<div align="center">
								<c:out value="${receipt.receiptNumber}" />
								
								</div>
							</td>
							<td  colspan="1" >
								<div align="center">
								<c:out value="${receipt.receiptDate}" />
									
								</div>
							</td>
							<td  colspan="1" >
								<div align="center">
								<c:out value="${receipt.fromDate}" />
								
								</div>
							</td>
							<td  colspan="1" >
								<div align="center">
								<c:out value="${receipt.toDate}" />
								
								</div>
							</td>
							<td  colspan="1">
								<div align="center">
								<c:out value="${receipt.receiptAmount}" />
								
								</div>
							</td>
						</tr>
						</c:forEach> 	
					</table>
    </c:otherwise>
     </c:choose>
     <div class="form-group text-center" >
								
								<a onclick="self.close()" class="btn btn-default" href="javascript:void(0)"><spring:message code="lbl.close"/></a>
							</div>
     </div>
     </div>
    
       
					
		</form:form>	
<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/entypo/css/entypo.css' context='/egi'/>"/>
<link rel="stylesheet" href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>"/>
<script type="text/javascript" src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.tableTools.js' context='/egi'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/global/js/jquery/plugins/datatables/TableTools.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.columnFilter.js' context='/egi'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/global/js/bootstrap/typeahead.bundle.js' context='/egi'/>"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/jquery.inputmask.bundle.min.js' context='/egi'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/global/js/jquery/plugins/jquery.validate.min.js' context='/egi'/>"></script>
<script	src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>" ></script>
