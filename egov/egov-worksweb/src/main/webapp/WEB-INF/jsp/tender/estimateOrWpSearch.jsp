<script type="text/javascript">
   function viewSearchPage(val){
 
    if(val.value="wp"){
  	 document.forms[0].tenderForEst.checked=false;
  	  document.forms[0].tenderForWp.checked=true;
    }else if(val.value="estimate"){  
   	 document.forms[0].tenderForWp.checked=false;
   	 document.forms[0].tenderForEst.checked=true;
    }
     	
   	if(val !='' && val =='estimate'){  
 	window.open('${pageContext.request.contextPath}/estimate/searchEstimate.action?source=createNegotiationNew&option=menu','_self');
   	}
   	else
   	{
   		window.open('${pageContext.request.contextPath}/tender/searchWorksPackage.action?source=createNegotiationForWP&option=menu','_self');
   	}		
   }
 </script>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
 <tr>
    <td width="11%" class="whiteboxwk">Select Estimate:</td>
	<td width="21%" class="whitebox2wk">
	   <input name="tenderForEst" type="radio" id="tenderForEst" onClick="viewSearchPage('estimate');"/>
	</td>
	<td width="11%" class="whiteboxwk">Select  Works package:</td>
	<td width="21%" class="whitebox2wk">
	  <input name="tenderForWp" type="radio" id="tenderForWp" onClick="viewSearchPage('wp');"/>
	</td>  
 </tr>	
</table>