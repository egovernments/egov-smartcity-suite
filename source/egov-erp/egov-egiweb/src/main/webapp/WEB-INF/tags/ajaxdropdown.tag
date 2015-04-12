<%@ tag body-content="empty" dynamic-attributes="true" isELIgnored="false" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="fields" required="true"%>
<%@ attribute name="dropdownId" required="true"%>
<%@ attribute name="url" required="true"%>
<%@ attribute name="optionAttributes" required="false"%>
<%@ attribute name="selectedValue" required="false" %>
<%@ attribute name="afterSuccess" required="false" %>
<%@ attribute name="contextToBeUsed" required="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script>

${id}SuccessHandler=function(data, textStatus, xhr){

  $('#${dropdownId} option[value != ""]').remove();
  
  $.each(data, function (i, item) {
	  $('#${dropdownId}').append($('<option>', { 
	        value: item.Value,
	        text : item.Text 
	  }));
   });

  $('#${dropdownId}').prop('selectedIndex', 0);

  <% if(afterSuccess != null && !afterSuccess.trim().equals("")) {%>
     ${afterSuccess}(req,res)
  <%}%>
}
${id}FailureHandler=function(){
  console.log('Unable to load ${dropdownId}');
}

function populate${dropdownId}(params){
   <% if(contextToBeUsed != null && !contextToBeUsed.trim().equals("")) { %>
   		<c:set var="contextRoot" value="${contextToBeUsed}" />
   <% } else  {%>
   		<c:set var="contextRoot" value="${pageContext.request.contextPath}" />
   <% } %>
   
   $.ajax({
		type: "GET",
		url: '${contextRoot}/${url}',
		data: params,
		dataType: "json",
		success: ${id}SuccessHandler, 
		error: ${id}FailureHandler
	});
}
</script>
