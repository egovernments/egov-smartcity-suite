<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="sj" uri="/WEB-INF/struts-jquery-tags.tld"%>
<%@ include file="/includes/taglibs.jsp" %>

<html>

<head>
 <sj:head jqueryui="true" jquerytheme="redmond" />
	<title>View Inspection Schedule</title>
	
<script type="text/javascript">

 
  jQuery.noConflict();
</script>
</head>



<body>

<div align="center"> 
 <div id="dplandetails" class="formmainbox">
  
	<div align="center" id="checklistdiv">
	<s:if test="postponedInspectionDetails.size!=0">
	<div id="inspectdetls" align="center">
	<h1 class="subhead" ><s:text name="Inspection Schedule Details"/></h1>
   <table id="postponetable" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	     
	      <tr>
			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Site Inspection fixed Date</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center"> Site Inspection Postponed Date</div></th>
		     <th  class="bluebgheadtd" width="8%"><div align="center"> PostPone Reason</div></th>
		   
		 </tr>
		  <s:iterator value="postponedInspectionDetails" status="row_status">
		    <tr>
		  	<td  class="blueborderfortd"> <div align="center"><s:text name="#row_status.count" /></div></td>
		    <td class="blueborderfortd"><div align="center"><s:property value="%{inspectionDateString}" /></div></td>
		    <td  class="blueborderfortd"><div align="center"><s:property value="%{postponedDateString}" /></div></td>
		  	<td  class="blueborderfortd"><div align="center"><s:property value="%{postponementReason}" /></div></td>
		  			  
		    </tr>
		    </s:iterator>
	  </table>
		 
    </div>
    </s:if>
    <s:else>
    
    <div align="center">
    <p>No Details Found.</p>
    </div>
    
    </s:else>
	 
	<div class="buttonbottom" align="center">
			<table>
				<tr>
					<td><input type="button" name="back" id="back" class="button" value="Back" onclick="history.back()"/></td>
			  		
			  	</tr>
	        </table>
	   </div>	   	 
    </div>
    
    
  
	</div> 

	
	
</div>



 </body>
</html>