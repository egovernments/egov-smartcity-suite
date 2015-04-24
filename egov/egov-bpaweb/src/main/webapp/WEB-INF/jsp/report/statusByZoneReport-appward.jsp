<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@	taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ include file="/includes/taglibs.jsp" %>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>

<%@ include file="/includes/taglibs.jsp" %>
<html>

<head>
<title>Appliaction Status Report By Zone</title>
<script type="text/javascript">

function  showDetail(wardcode,statecode)
{
	
	document.searchForm.action =('${pageContext.request.contextPath}/report/statusByZoneReport!showdetail.action?wardcode='+wardcode+'&statecode='+statecode);	
document.searchForm.submit();           
return true;
	}
</script>
</head>


<body  >
<div class="errorstyle" id="searchRecords_error" style="display: none;"></div>
	<div class="errorstyle" style="display: none" ></div>
	<s:if test="%{hasErrors()}">
		<div class="errorstyle">
			<s:actionerror />	
			<s:fielderror />
		</div>
	</s:if>
<s:form action="statusByZoneReport" name="searchForm" theme="simple">
<div id="searchdetails" >
<table width="100%" class="its" border="0" cellspacing="0" cellpadding="2">	

<tr>
<div align="center">
<th class="bluebgheadtd" width="10%"><s:text name="Status" />
</div></th>
<s:iterator value="appstatusmap" status="headermapstatusward">
 <s:if test="#headermapstatusward.index ==0">
     <s:iterator value="value">
  
     <th class="bluebgheadtd" width="10%">
     <div align="center">
     <s:property value="key"/>
     </div></th>  
   
   </s:iterator>
 </s:if>
</s:iterator>

</tr>

  <s:iterator value="appstatusmap" status="outerstatus">
  
 <tr>
     <td class="blueborderfortd">
  <s:property value="key" /> 
   <s:set var="count" value="key"/>

  
 </td>
    <s:iterator value="value">
 <td class="blueborderfortd">
 <div align="center">
   <s:if test="%{value>0}">
<a href="#" onclick="showDetail('<s:property value="key"/>','<s:property value="#count"/>')"> </s:if>
       <s:property value="value"/>
</a>
 </div>
     </td>
  </s:iterator>
</tr>
 
</s:iterator>


  </table>
</div>

	   <div id="tableData">
 
 
	  
          		 <div id="displaytbl">	
          		     	 <display:table  name="regList" export="true" requestURI="" id="registrationId"  class="its" uid="currentRowObject" >
          			 	 <div STYLE="display: table-header-group" align="center">
          			 	  
 						 	<display:column title=" Sl No" style="text-align:center;"  >
 						 	 <s:property value="#attr.currentRowObject_rowNum"/></display:column>
 						 	 
 						<display:column title="Plan Submission Number " style="text-align:center;" property="planSubmissionNum"/>
 						 <display:column title="Application Date" style=" text-align:center" property="planSubmissionDate"	/>
							<display:column title="Applicant Name " style="text-align:center;" property="owner.firstName" />	 
							<display:column title="Applicant Address " style="text-align:center;" property="bpaOwnerAddress" />	
						
						<display:column title="Current Owner" style="text-align:center;" >
          			        	<s:property value="%{getUsertName(#attr.currentRowObject.state.owner.id)}" />
          			   </display:column>
						<display:column title="Service Type" style="text-align:center;" property="serviceType.description" />		
						<display:column title="Surveyor Name"	 style="text-align:center" property="surveyorName.name"	/>
						<display:column title="Status " style="text-align:center;" property="egwStatus.code" />	
						<display:column title="Last Transaction Date " style="text-align:center;"  >	
								<s:date name="#attr.currentRowObject.modifiedDate" format="dd/MM/yyyy" />
							</display:column> 
 						 
 						 	
 						 	</div>
 						 	</display:table>
 						 	</div>
 						 	</div>
 						<div>
	   
	   </div>
	   </table> 
	   
	   
	  
	   </s:form>
	   </body>
	   </html>
	   
	   