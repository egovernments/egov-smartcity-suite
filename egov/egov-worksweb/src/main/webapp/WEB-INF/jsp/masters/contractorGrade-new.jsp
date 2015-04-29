<%@ taglib prefix="s" uri="/struts-tags" %>
<html>  
<head>  
    <title><s:text name="contractor.grade.master.title" /></title>  
</head>  
<body onload="roundOffMaxMinAmount();">
<script>

function roundOffMaxMinAmount()
{
	document.contractorGrade.minAmount.value=roundTo(document.contractorGrade.minAmount.value);
	document.contractorGrade.maxAmount.value=roundTo(document.contractorGrade.maxAmount.value);
}

</script>
    <s:if test="%{hasErrors()}">
        <div class="errorstyle">
          <s:actionerror/>
          <s:fielderror/>
        </div>
    </s:if>
    
	<s:if test="%{hasActionMessages()}">
        <div class="messagestyle">
        	<s:actionmessage theme="simple"/>
        </div>
    </s:if>
   
<s:form action="contractorGrade" theme="simple" name="contractorGrade" >
<s:token/>
<s:hidden  name="model.id" />
<p>
<%@ include file='contractorGrade-form.jsp'%>
	<s:if test="%{mode!='view'}">
	<s:submit value="SAVE" method="save" cssClass="buttonfinal" id="saveButton" name="button"/>
	</s:if>
	<s:if test="%{model.id==null}" >
	<s:reset value="CLEAR" cssClass="buttonfinal" />
	</s:if>
	<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();" /></p>
	
	 
</s:form> 
</body>
</html>