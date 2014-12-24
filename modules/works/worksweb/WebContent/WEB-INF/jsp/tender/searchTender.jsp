 <%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
 
 <table width="100%" border="0" cellspacing="0" cellpadding="0">
 			<tr>
				 <td width="30%" class="greyboxwk"><s:text name="TenderFile.status" />:</td>
		         <td width="21%" class="greybox2wk"><s:select id="status" name="status" cssClass="selectwk" list="%{tenderFileStatuses}"  listKey="code" listValue="description" /></td>
		         <td width="10%" class="greyboxwk"><s:text name="TendeFile.FileNo" />:</td>
		         <td width="53%" class="greybox2wk"><s:textfield name="fileNum" value="%{fileNum}" id="fileNum" cssClass="selectwk" /></td>
			</tr>
			
		    <tr>
		         <td width="30%" class="whiteboxwk"><s:text name="TenderFile.NoticeNo" />:</td>
		         <td width="21%" class="whitebox2wk"><s:textfield id="noticeNumber" name="noticeNumber" value="%{noticeNumber}"  cssClass="selectwk" /></td>
		         <td width="10%" class="whiteboxwk"><!-- <s:text name="TenderFile.estimates" />: --> &nbsp;</td>
		         <td width="53%" class="whitebox2wk">
		         	<!--<s:checkbox id="pullDataForeTenderingFlag" name="pullDataForeTenderingFlag"/><s:text name="pullDataFromeTendering.lbl"/>-->
		         	<s:select id="estimateId" name="estimateId" headerKey="-1" headerValue="Select" cssClass="selectwk" list="%{estimatesList}"  listKey="id" listValue="estimateNumber" />
		         </td>
		   </tr>
		  
		   <tr>
	       		<td colspan="4" class="shadowwk"></td>
	       </tr>
	       <tr>
	       		<td colspan="4">
	       			<div class="buttonholdersearch" align = "center">
	            	  <s:submit value="Save" cssClass="buttonfinal" value="SEARCH" id="saveButton" name="button" onClick="return validateSearch();"/>
	          		</div>
	            </td>
	      	</tr>
	
			<div class="errorstyle" id="error_search" style="display: none;"></div>
	
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			
					<tr><td><div class="buttonholderwk">
							</div>
						</td>
					</tr>
				</table>	
</table>

   <script>
	  if(dom.get("estimateId")!=null) {
				dom.get("estimateId").style.visibility='hidden';
	  }
  </script>
		