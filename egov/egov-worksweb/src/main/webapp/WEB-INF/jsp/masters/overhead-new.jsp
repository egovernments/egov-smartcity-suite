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
    <s:if test="%{not model.id}">
    <title><s:text name="title.overhead.add" /></title>
    </s:if>
    <s:else>
    <title><s:text name="title.overhead.edit" /></title>
    </s:else>  
</head>  
	<body> 
	 <script>
	 function enableFields()
	 {
 		<s:iterator id="overheadRateIterator" value="model.overheadRates" status="rate_row_status">
		var record = overheadRateDataTable.getRecord(parseInt('<s:property value="#rate_row_status.index"/>'));
		
		var column = overheadRateDataTable.getColumn('startDate');  
        dom.get(column.getKey()+record.getId()).disabled = false;
      
     			var column = overheadRateDataTable.getColumn('endDate');  
        dom.get(column.getKey()+record.getId()).disabled = false;
	
		</s:iterator>  
		validateOverheadFormAndSubmit();
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
   
		<s:form action="overhead" theme="simple" name="overhead" >  
		<s:token/> 
		 <div class="errorstyle" id="overheads_error" style="display:none;"></div>
		<s:push value="model">
		    <!-- <s:hidden  name="model.id" /> -->
			<s:hidden name="id" />
		    <%@ include file='overhead-form.jsp'%>
			<div class="buttonholderwk">
			<input type="button" class="buttonfinal" value="SAVE" id="saveButton" name="button"  onclick="enableFields();" />
			 &nbsp;
			 <input type="button" name="addOverheadButton" id="buttonfinal" class="buttonadd" value="Add a New Overhead"
			 onclick="window.open('${pageContext.request.contextPath}/masters/overhead!newform.action','_self');" />
			  &nbsp;
			  
			<input type="button" name="listOverheadsButton" id="listOverheadsButton" class="buttonadd" value="Overhead Listing" 
			onclick="window.open('${pageContext.request.contextPath}/masters/overhead.action','_self');"/>
			
			</div>
			</s:push>
		</s:form>  
	    
	    <script>
	     </script>
	</body>  
</html>
