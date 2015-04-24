<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>

<s:set name="theme" value="'simple'" scope="page" />
<div align="center"> 
 <div id="regServeyorDetlistdivid" class="formmainbox">
	<div  align="center">
	 <tr>
		     <td class="greybox" width="13%">&nbsp;</td>	
		  	<td class="greybox" width="13%"><s:text name="Surveyor.name" /> :<span class="mandatory" >*</span></td>	
		     <td class="greybox"> <s:select  id="Surveyor" name="Surveyor" value="%{Surveyor}" 
		list="dropdownData.surveyorNameList" listKey="id" listValue="code" headerKey="-1" 
         headerValue="----choose-----" onchange="getSurveyourDetail();" />
		 </td>
		 <td class="greybox">&nbsp;</td>	
		 <td class="greybox">&nbsp;</td>	
		   </tr>	    
	</div> 

	
	
</div>

