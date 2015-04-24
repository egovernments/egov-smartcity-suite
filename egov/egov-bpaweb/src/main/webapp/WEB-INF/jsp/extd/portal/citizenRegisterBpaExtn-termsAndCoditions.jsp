<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<s:set name="theme" value="'simple'" scope="page" />
 <SCRIPT>
</SCRIPT>
 <div id="termsConditiondetail" align="center"> 
	 <div class="formmainbox">
		<div align="center" > 
			<h1 class="subhead"><u>Terms And Conditions</u></h1>
			<s:if test="%{!isUserMappedToSurveyorRole()}">
				<table  width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
					<tr>
						<td class="bluebox"><span class="bold">1. I here with submit that all the details and particulars furnished are true to my best of the knowledge.</span></td> 
					 </tr>
					 <tr>
						<td class="bluebox"><span class="bold">2. If anything is found later that the document is forged or any facts are suppressed by me, I may be held responsible.</span></td> 
					 </tr>
					 <tr>
						<td class="bluebox"><span class="bold">3. Corporation of Chennai shall initiate any action if the documents are forged by me or any facts are suppressed even at later date.</span></td> 
					 </tr>
	 			</table>
	 		</s:if>
	 	
		 	<s:else>
			 	<table  width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebottom">
						<tr>
							<td class="bluebox"><span class="bold">1. I here with undertake that it has been inspected by me and the details furnished in this regard are true to the best of my knowledge.</span></td> 
						 </tr>
						 <tr>
							<td class="bluebox"><span class="bold">2. The document have been verified by me and submitted as per the document history enclosed.</span></td> 
						 </tr>
						 <tr>
							<td class="bluebox"><span class="bold">3. Corporation of Chennai shall initiates any action and suspend my license if it is found that the document are forged or any facts are suppressed even at later date.</span></td> 
						 </tr>
		 			</table>
		 	</s:else>
		</div>
	</div>
</div>