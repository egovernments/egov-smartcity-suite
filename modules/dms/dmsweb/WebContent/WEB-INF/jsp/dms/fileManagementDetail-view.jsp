<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/includes/taglibs.jsp" %>
<style>
span{
color:navy;
}
</style>
<fieldset>
	<legend align="left"><b><s:text name="lbl.filedtl"/></b></legend>
	<div id="calender" class="cal"></div>
	<table border="0" cellpadding="4" cellspacing="0" width="100%">
		<tbody>
			<tr class="graybox">
				<td>
					<s:text name="lbl.file"/> # 
				</td>
				<td>
					<s:hidden id="id" name="id"/>
					<span>${fileNumber}</span>
				</td>
				<td>
					<s:text name="lbl.filedt"/>
				</td>
				<td>
					<span><s:date name="fileDate" format="dd/MM/yyyy"/></span>
				</td>
			</tr>

			<tr class="whitebox">
				<td>
					<s:text name="lbl.dept"/>
				</td>
				<td>
					<span>${department.deptName}</span>
				</td>
				<td>
					<s:text name="lbl.zone"/>
				</td>
				<td>
					<span>${firstLevelBndry.name}</span>
				</td>
			</tr>
		
			<tr class="graybox">
				<td >
					<s:text name="lbl.ward"/>
				</td>
				<td>
					<span>${secondLevelBndry.name}</span>
				</td>
				<td >
					<s:text name="lbl.priority"/>
				</td>
				<td >
					<span>${filePriority.name}</span>
				</td>
			</tr>
		
			<tr class="whitebox">
				<td >
					<s:text name="lbl.filetype"/>
				</td>
				<td>
					<span>${fileType}</span>
				</td>
				<td>
					<s:text name="lbl.filercvdsentdt"/>
				</td>
				<td>
					<span><s:date name="fileReceivedOrSentDate" format="dd/MM/yyyy" /></span>
				</td>
			</tr>
		
			<tr class="graybox">
				<td>
					<s:text name="lbl.filecat"/>
				</td>
				<td>
					<span>${fileCategory.name}</span>
				</td>
				<td>
					<s:text name="lbl.filesubcatgry"/>
				</td>
				<td>
					<span>${fileSubcategory.name}</span>
				</td>
			</tr>
		
			<tr class="whitebox">
				<td>
					<s:text name="lbl.fileheadng"/>
				</td>
				<td>
					<span>${fileHeading}</span>
				</td>
				<td>
					<s:text name="lbl.filestatus"/>
				</td>
				<td >
					<span>${fileStatus.description}</span>
				</td>
			</tr>
		
			<tr class="graybox">
				<td>
					<s:text name="lbl.smmryofdoc"/>
				</td>
				<td>
					<div class='viewdiv'>${fileSummary}</div>
				</td>
				<td>
					<s:text name="lbl.searchtag"/>
				</td>
				<td >
					<div class='viewdiv'>${fileSearchTag}</div>
				</td>				
			</tr>
		</tbody>
	</table>
</fieldset>
<br/>
<fieldset>
<legend align="left"><b><s:text name="lbl.docattchd"/></b></legend>	
<table width="100%" border="0" cellspacing="0" cellpadding="2" id="fileListTbl"><tbody>
	<tr class="graybox" align="left">
		<th><s:text name="lbl.file"/></th>
		<th><s:text name="lbl.cmmt"/></th>
		<th><s:text name="lbl.cmmthistry"/></th>
	</tr>
   <c:forEach items="${fileNames}" var="item" varStatus = "status">
   		<tr class="${status.count%2 == 0 ? 'graybox' : 'whitebox'}" >
   		   <td style="padding:10px;"><span style="background-color:whitesmoke;border:1 solid gray;padding:3px;"><a href='/egi/docmgmt/ajaxFileDownload.action?docNumber=${fileNumber}&fileName=${item}&moduleName=DMS' target="_parent" >${item}&nbsp;&nbsp;<img src="../images/download.gif"></a></span></td>
           <td style="padding:10px;"><div class='viewdiv'>${fileComment[status.count-1]}</div></td>
           <td><button type="button" onclick="getFileCommentHistory('${fileNumber}','${item}')"><s:text name="lbl.view"/></button></td>           						            
       </tr>
   </c:forEach>							        											
</tbody>
</table>	
</fieldset>
<br/>
