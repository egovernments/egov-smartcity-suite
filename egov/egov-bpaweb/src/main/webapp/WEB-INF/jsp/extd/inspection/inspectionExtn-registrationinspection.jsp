<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>

<s:set name="theme" value="'simple'" scope="page" />
<div align="center"> 
 <div id="dplandetails" class="formmainbox">
  
	<div align="center" id="checklistdiv">
	<s:if test="postponedInspectionDetails.size!=0">
	<div id="inspectdetls" align="center">
	<h1 class="subhead" ><s:text name="inspectionlbl.dtls.hdr"/></h1>
   <table id="postponetable" width="80%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
	     
	      <tr>
			<th  class="bluebgheadtd" width="3%"><div align="center">Sl No</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center">Site Inspection fixed Date</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center"> Site Inspection Postponed Date</div></th>
		    <th  class="bluebgheadtd" width="8%"><div align="center"> Site Inspection Scheduled By</div></th>
		     <th  class="bluebgheadtd" width="8%"><div align="center"> PostPone Reason</div></th>
		   
		 </tr>
		  <s:iterator value="postponedInspectionDetails" status="row_status">
		    <tr>
		  	<td  class="blueborderfortd"> <div align="center"><s:text name="#row_status.count" /></div></td>
		    <td class="blueborderfortd"><div align="center"><s:property value="%{inspectionDateString}" /></div></td>
		    <td  class="blueborderfortd"><div align="center"><s:property value="%{postponedDateString}" /></div></td>
		        <td  class="blueborderfortd"><div align="center"><s:property value="%{inspectedBy.firstName}" /></div></td>
		  	<td  class="blueborderfortd"><div align="center"><s:property value="%{postponementReason}" /></div></td>
		  			  
		    </tr>
		    </s:iterator>
	  </table>
		 
    </div>
    </s:if>
	 
		 
    </div>
    
    
  
	</div> 

	
	
</div>

