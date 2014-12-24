<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>  
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%> 
<script>
function setJobNumberId(elem){
	dom.get("jobNumberId").value = elem.value;
}
function gotoPage(){
	var id = document.getElementById("jobNumberId").value;
	if(id!='')
		window.open('${pageContext.request.contextPath}/qualityControl/testResult!newform.action?sourcePage=createTestResult&jobHeaderId='+id,'_self');
	else{
		dom.get("searchTestResult_error").style.display='';
		document.getElementById("searchTestResult_error").innerHTML='<s:text name="test.result.job.not.selected" />';
		return false;
	}
	clearErrorMessage();
}
</script>
<div>
	<table width="100%" border="0" cellspacing="0"
		cellpadding="0">
		<tr>
			<td colspan="7" class="headingwk">
				<div class="arrowiconwk">
					<img
						src="${pageContext.request.contextPath}/image/arrow.gif" />
				</div>
				<div class="headplacer">
					<s:text name="title.search.result" />
				</div>
			</td>
		</tr>
	</table>

	<s:if test="%{searchResult.fullListSize != 0}">
		<s:text id="select" name="%{getText('column.title.select')}"></s:text>
		<s:text id="slNo" name="%{getText('column.title.SLNo')}"></s:text>
		<s:text id="jobNumber" name="%{getText('test.result.job.number')}"></s:text>
		<s:text id="jobDate" name="%{getText('test.result.job.date')}"></s:text>
		<s:text id="woNumber" name="%{getText('test.result.work.order.number')}"></s:text>
		<s:text id="woDate" name="%{getText('test.result.work.order.date')}"></s:text>
		<s:text id="contractor" name="%{getText('test.result.contractor')}"></s:text>
		<s:text id="samplelNo" name="%{getText('test.result.sample.letter.no')}"></s:text>
		<s:text id="slDate" name="%{getText('test.result.sample.letter.date')}"></s:text>
		<s:text id="clNo" name="%{getText('test.result.covering.letter.no')}"></s:text>
		<s:text id="totalCharges" name="%{getText('test.result.total.charges')}"></s:text>
		
		<s:hidden id="jobNumberId" name="jobNumberId" value="%{jobNumberId}"/>
		
       <display:table name="searchResult" pagesize="30"
			uid="currentRow" cellpadding="0" cellspacing="0"
			requestURI=""
			style="border:1px;width:100%;empty-cells:show;border-collapse:collapse;">
             
             <display:column headerClass="pagetableth" class="pagetabletd"
				title="${select}" titleKey="column.title.select"
				style="width:2%;text-align:left">					
				<s:hidden name="applRequestStatus" id="applRequestStatus" value="%{#attr.currentRow.applicationRequest.egwStatus.code}" />
				<input name="radio" type="radio" id="radio"
					value="<s:property value='%{#attr.currentRow.id}'/>"
					onClick="setJobNumberId(this);" />
			 </display:column>    
             <display:column headerClass="pagetableth"
				class="pagetabletd" title="${slNo}"
				titleKey="column.title.SLNo"
				style="width:3%;text-align:right" >
				<s:property value="#attr.currentRow_rowNum + (page-1)*pageSize"/>
			</display:column>

			<display:column headerClass="pagetableth"
				class="pagetabletd" title="Job Number"
				titleKey="test.result.job.number"
				style="width:10%;text-align:left" property="jobNumber" />
	         	 
			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${jobDate}"
				titleKey="test.result.job.date"
				style="width:10%;text-align:left">
				<s:date name="#attr.currentRow.jobDate" format="dd/MM/yyyy" />
         	</display:column>	

         	<display:column headerClass="pagetableth"
				class="pagetabletd" title="${woNumber}"
				titleKey="test.result.work.order.number"
				style="width:15%;text-align:left" property="sampleLetterHeader.testSheetHeader.workOrder.workOrderNumber" />
           
            <display:column headerClass="pagetableth"
				class="pagetabletd" title="${woDate}"
				titleKey="test.result.work.order.date"
				style="width:10%;text-align:left" >
				<s:date name="#attr.currentRow.sampleLetterHeader.testSheetHeader.workOrder.workOrderDate" format="dd/MM/yyyy" />
			</display:column>	
         
          <display:column headerClass="pagetableth"
				class="pagetabletd" title="${contractor}"
				titleKey="test.result.contractor"
				style="width:10%;text-align:left" property="sampleLetterHeader.testSheetHeader.workOrder.contractor.name" />
         
         <display:column headerClass="pagetableth"
				class="pagetabletd" title="${samplelNo}"
				titleKey="test.result.sample.letter.no"
				style="width:10%;text-align:left" property="sampleLetterHeader.sampleLetterNumber">
         	 </display:column>
         	 
         	 <display:column headerClass="pagetableth"
				class="pagetabletd" title="${slDate}"
				titleKey="test.result.sample.letter.date"
				style="width:10%;text-align:left" >
				<s:date name="#attr.currentRow.sampleLetterHeader.sampleLetterDate" format="dd/MM/yyyy" />
         	 </display:column>
         	 
         	 <display:column headerClass="pagetableth"
				class="pagetabletd" title="${clNo}"
				titleKey="test.result.covering.letter.no"
				style="width:10%;text-align:left" property="sampleLetterHeader.coveringLetterNumber">
         	 </display:column>

			<display:column headerClass="pagetableth"
				class="pagetabletd" title="${totalCharges}"
				titleKey="test.result.total.charges"
				style="width:10%;text-align:right" >
             	<s:text name="contractor.format.number" >
					<s:param name="rate" value='%{#attr.currentRow.sampleLetterHeader.slTotalAmount}' />
				</s:text>
         	</display:column>

		</display:table>
		<P align="center">
		   <input type="button" class="buttonadd"
				value="Create Test Result" id="addButton"
				name="createTestResult" onclick="gotoPage();"
				align="center" />
		</P>
	</s:if>
	<s:elseif test="%{searchResult.fullListSize == 0}">
		<div>
			<table width="100%" border="0" cellpadding="0"
				cellspacing="0">
				<tr>
					<td align="center">
						<font color="red">No record Found.</font>
					</td>
				</tr>
			</table>
		</div>
	</s:elseif>  
</div> 
                       		