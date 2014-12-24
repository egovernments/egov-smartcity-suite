<script type="text/javascript">
   function viewSearchPage(val){
 
    if(val.value="tender"){
  	 document.forms[0].rcEst.checked=false;
  	  document.forms[0].tenderForEst.checked=true;
    }else if(val.value="estimate"){  
   	 document.forms[0].tenderForEst.checked=false;
   	 document.forms[0].rcEst.checked=true;
    }
     	
   	if(val !='' && val =='estimate'){  
 	window.open('${pageContext.request.contextPath}/estimate/searchEstimate.action?source=searchRCEstimate','_self');
   	}
   		else
   	{
   		window.open('${pageContext.request.contextPath}/tender/searchTenderFile!search.action','_self');
   	}	
   }
 </script>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
 <tr>
			<td width="30%" class="whiteboxwk">			</td>
			<td width="21%" class="whitebox2wk"> <input name=tenderForEst type="radio" id="tenderForEst" value="TenderOrQuotation" onClick="viewSearchPage('tender');"/><s:text name="searchTenderFile.TenderOrQuotation" /></td>
			 <td width="15%" class="whiteboxwk"><input name="rcEst" type="radio" id="rcEst" value="RateContract" onclick="viewSearchPage('estimate');" /><s:text name="searchTenderFile.RateContract" /></td>
			 <td width="53%" class="whitebox2wk"></td>
			</tr>
</table>