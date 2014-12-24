	<%@ page import="java.util.*" %>
<script language="JavaScript"  type="text/JavaScript">
<% List mandatoryFields=(List)session.getAttribute("mandatoryFields");
List headerFields=(List)session.getAttribute("headerFields");

%>
	function validateForMandatory(){
	
	<%if( mandatoryFields!=null && mandatoryFields.contains("department") ){%>
		if(document.getElementById('departmentId').value == ""){
			alert("Please Select Department");
			document.getElementById('departmentId').focus();
			return false;
		}	
		<%}%>
		<%if( mandatoryFields!=null && mandatoryFields.contains("functionary") ){%>
		if(document.getElementById('functionaryId').value == ""){
			alert("Please Select Functionary");
			document.getElementById('functionaryId').focus();
			return false;
		}
		<%}%>
	<%if( mandatoryFields!=null && mandatoryFields.contains("function") ){%>
		if(document.getElementById('functionId').value == ""){
			alert("Please Select Function");
			document.getElementById('functionId').focus();
			return false;
		}
		<%}%>
		return true;
		
	}

</script>

<table width="100%" cellpadding="0" cellspacing="0" border="0">
    
   <tr>
    <%if( headerFields!=null && headerFields.contains("department") ){%>
    <td class="whiteboxwk" >Department     
    <%if( mandatoryFields!=null && mandatoryFields.contains("department") ){ %>
      <span class="mandatory">*</span>
    <egovtags:filterByDeptMandatory/>
    <%} %>
    </td>
    <td class="whitebox2wk">
    	    <html:select property="departmentId" styleId="departmentId" styleClass="selectwk" >
			<html:option value="">--------------choose---------------</html:option>
			<c:forEach var="department" items="${deptList}">
			<html:option value="${department.id}">${department.deptName}</html:option>
			</c:forEach>
			</html:select>
	</td>
	<%}
	if( headerFields!=null && headerFields.contains("functionary") ){%>
	 
	<td class="whiteboxwk" >Functionary
		<%if( mandatoryFields!=null && mandatoryFields.contains("functionary") ){%>
		<span class="mandatory">*</span>
		<%} %></td>
    <td class="whitebox2wk">
    	<html:select  property="functionaryId" styleId="functionaryId" styleClass="selectwk" >
			<html:option value="">-------------choose---------------</html:option>
			<c:forEach var="functionary" items="${functionaryList}">
				<html:option value="${functionary.id}" >${functionary.name}</html:option>
			</c:forEach>
		</html:select>
	</td>
	<%} %>
  </tr> 
  <%if( headerFields!=null && headerFields.contains("function") ){%> 
   <tr>
    <td class="whiteboxwk" >Function
    <%if( mandatoryFields!=null && mandatoryFields.contains("function") ){%>
    <span class="mandatory">*</span>
    <%} %>
    </td>
    <td class="whitebox2wk">
		    	
		
    	<html:select  property="functionId" styleId="functionId" styleClass="selectwk" style="width:201px">
			<html:option value="">-------------choose---------------</html:option>
			<c:forEach var="function" items="${functionList}">
				<html:option value="${function.id}" >${function.name}</html:option>
			</c:forEach>
		</html:select>
	</td> 
  </tr>
  <%} %>
  </table>  	