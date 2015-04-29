<%@ taglib prefix="s" uri="/struts-tags" %>  

<style type="text/css">
.yui-dt table{
  width:100%;
}
.yui-dt-col-Add{
  width:5%;
}
.yui-dt-col-Delete{
  width:5%;
}

</style>


<html>  
<head>  
    <title><s:text name="sor.master.title" /></title>  
</head>  
	<body onload="disablePreviousRatesOnLoad();">
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
		<s:form action="scheduleOfRate" theme="simple" name="scheduleOfRate"  onsubmit="return validateSORFormAndSubmit();">

		<s:token/> 
			<s:hidden  name="model.id" id="id" /> 
			<%@ include file='scheduleOfRate-form.jsp'%>
		<p>
		<s:if test="%{mode!='view'}">
			<!-- <input type="button" class="buttonfinal" value="SAVE" id="saveButton" name="button"  onclick="validateSORFormAndSubmit();" />&nbsp;-->
			<s:submit type="submit" cssClass="buttonfinal" value="SAVE" id="saveButton" name="button" method="create" onclick="validateSORFormAndSubmit();"/>&nbsp;
		</s:if>
 		<!--<s:submit name="button" method="create" id="button" value="SAVE" cssClass="buttonfinal" onClick="validateSORFormAndSubmit();"/>   -->
		<s:if test="%{model.id==null}" >
			<input type="button" class="buttonfinal" value="CLEAR" id="button" name="clear" onclick="this.form.reset();">&nbsp;
		</s:if>
		<input type="button" class="buttonfinal" value="CLOSE" id="closeButton" name="closeButton" onclick="window.close();" /></p>
		<s:hidden  name="createdBy" value="%{createdBy.id}"/>
	    <s:hidden  name="createdDate" value="%{createdDate}"/>
	</s:form>    
	</body>  
</html>
