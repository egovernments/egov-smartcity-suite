<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld" %>
<style type="text/css">
.yui-dt table{
  width:100%;
}
.yui-skin-sam .yui-dt td.align-right  { 
		text-align:right;
}
th.yui-dt-hidden,
tr.yui-dt-odd .yui-dt-hidden,
tr.yui-dt-even .yui-dt-hidden {
display:none;
}
</style>
<script src="<egov:url path='js/works.js'/>"></script>
<div id="header-container">
	<table id="table-header" cellpadding="0" cellspacing="0" align="center">
    	<tr>
       		<td colspan="5" class="headingwk">
       			<div class="arrowiconwk"><image src="<egov:url path='/image/arrow.gif'/>" /></div>
       			<div class="headplacer"><s:text name='page.title.workorder.detail' /></div>
			</td>	
        </tr>
	</table>
</div>
          
<s:if test="%{isRCEstimate}">
<div id="header-container">
	<table id="table-header" cellpadding="0" cellspacing="0" align="center">
		<tr>
			<th width="2%"><s:text name='column.title.SLNo' /></th>	
			<th width="8%"><s:text name='workorder.sch.no' /></th>
			<th width="8%"><s:text name='workorder.quantity' /></th>
			<th width="25%"><s:text name='workorder.description' /></th>
			<th width="8%"><s:text name='workorder.uom' /></th>
			<th width="8%"><s:text name='workorder.workorderrate' /></th>
			<th width="8%"><s:text name='workorder.amount' /></th>
		</tr>
	</table>
</div>
<s:hidden name="workOrderValue" id="workOrderValue"></s:hidden>
<table width="100%" border="0" cellpadding="0" cellspacing="0" id="table-body" >		
	<s:if test="%{abstractEstimate.activities.size != 0}">
		<s:iterator id="estimateActivityIterator" value="abstractEstimate.activities" status="row_status_est">	
			<tr>
				<td width="2%"><s:property value="#row_status.count" /></td>
				<td width="8%"><s:property value='%{schedule.code}' /></td>
				<td width="8%"><div align="right"><s:property value='%{quantity}' /></div></td>
				<s:if test="%{schedule!=null}"> 
					<td width="25%"><s:property value='%{schedule.summaryJS}' /><a href="#" class="hintanchor" onMouseover='showhint("<s:property value='%{schedule.descriptionJS}' />", this, event, "300px")'><img src="${pageContext.request.contextPath}/image/help.png" alt="Help" width="16" height="16" border="0" align="absmiddle"/></a></td>
				</s:if>
				<s:else>
					<td width="25%"><s:property value='%{nonSor.descriptionJS}' /></td>
				</s:else>
				<td width="8%"><s:property value='%{uom.uom}' /></td>
			   	<s:if test="%{schedule!=null && schedule!=''}">
				   	<td width="8%">
				   		<div align="right">
				   			<s:text name="contractor.format.number" >
				   				<s:param value='%{sORCurrentRate.value}' /> 
				   			</s:text>
				   		</div>
				   	</td>
			   	</s:if>
			   	<s:else>
				   	<td width="8%">
				   		<div align="right">
				   			<s:text name="contractor.format.number" >
				   				<s:param value='%{rate.value}' />
				   			</s:text>
				   		</div>
				   	</td>
			   	</s:else>
			    <td width="8%">
			    	<div align="right">
				    	<s:text name="contractor.format.number" >
				        	<s:param value="%{amount.value}"/>
				        </s:text>
				    </div>
			   </td>
			</tr>
		</s:iterator>                  
			 <tr>
             	<td>
                	<td colspan="4"><td><div align="left"><b><s:text name="workorder.total" /></b></div></td></td><td><div align="right"><s:text name="contractor.format.number" ><s:param value='%{workOrder.workOrderAmount}' /></s:text></div></td>
               	</td>
             </tr>                    
	</s:if>
    <s:elseif test="%{abstractEstimate.activities.size != 0}">
    	<tr>
			<td align="center">
				<font color="red"><s:text name="label.no.records.found" /></font>
			</td>
 		</tr>
   	</s:elseif>
</table>
</s:if>           