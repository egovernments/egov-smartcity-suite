<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/includes/taglibs.jsp" %>
<fieldset>
	<legend align="left"><b><s:text name="lbl.filedtl"/></b></legend>
	<div id="calender" class="cal"></div>
	<table border="0" cellpadding="4" cellspacing="0" width="100%">
		<tbody>
			<tr class="graybox">
				<td>
					<s:text name="lbl.file"/> #<span class="mandatory">*</span>
				</td>
				<td>
					<s:if test="%{fileNumber == null || fileNumber==''}">
						<s:textfield name="fileNumber" id="fileNumber" tabindex="1"/>
					</s:if>
					<s:else>
						<s:textfield name="fileNumber" id="fileNumber" tabindex="1" readonly="true"/>
					</s:else>
					<s:hidden name="id" id="id" />
				</td>
				<td>
					<s:text name="lbl.filedt"/><span class="mandatory">*</span>
				</td>
				<td>
					<s:date name="fileDate" id="fileDateFmrtd" format="dd/MM/yyyy"/>
					<s:textfield value="%{fileDateFmrtd}" name="fileDate"  id="fileDate" tabindex="1" size="20" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" />
					<img src="../images/calendaricon.gif" onclick="showCalendar(this,event)" alt="Date" width="18" id="fileDateBtn" height="18" align="absmiddle" tabindex="1" onkeypress="showCalendar(this,event)"/>
				</td>
			</tr>

			<tr class="whitebox">
				<td>
					<s:text name="lbl.dept"/>
				</td>
				<td>
					<s:select headerValue="%{getText('sel.dept')}" headerKey=""  name="department" id="department"  list="dropdownData.departmentList" listValue="deptName" listKey="id"  theme="simple" tabindex="1" value="department.id"/>
				</td>
				<td>
					<s:text name="lbl.zone"/>
				</td>
				<td>
					<s:select headerValue="%{getText('sel.zone')}" headerKey="" name="firstLevelBndry" id="firstLevelBndry"  list="firstLevelBoundries" listValue="name" listKey="id"  theme="simple" tabindex="1" onchange="loadSecondLevelBoundary(this)" value="firstLevelBndry.id"/>
					<egovtags:ajaxdropdown id="secondLevelBndryDropdown"  fields="['Text','Value']" dropdownId='secondLevelBndry' url='dms/fileManagement!getSecondLevelBoundries.action' selectedValue="%{secondLevelBndry.id}"/>
				</td>
			</tr>

			<tr class="graybox">
					<td >
						<s:text name="lbl.ward"/>
					</td>
					<td>
						<s:select name="secondLevelBndry" tabindex="1" id="secondLevelBndry" list="secondLevelBndryList" listKey="id" listValue='name' headerKey="-1" headerValue="%{getText('sel.ward')}"  value="secondLevelBndry.id" />

					</td>
					<td >
						<s:text name="lbl.priority"/>
					</td>
					<td >
						<s:select headerValue="%{getText('sel.prity')}" headerKey="" name="filePriority" id="priority"  list="dropdownData.filePriorityList" listValue="name" listKey="id"  theme="simple" tabindex="1" value="filePriority.id"/>
					</td>
			</tr>
		
			<tr class="whitebox">
				<td >
					<s:text name="lbl.filetype"/>
				</td>
				<td>
					<span id="fileType"></span>
				</td>
				<td>
					<s:text name="lbl.filercvdsentdt"/><span class="mandatory">*</span>
				</td>
				<td>
					<s:date name="fileReceivedOrSentDate" id="fileReceivedOrSentDateFrmtd" format="dd/MM/yyyy"/>
					<s:textfield  value="%{fileReceivedOrSentDateFrmtd}" name="fileReceivedOrSentDate" size="20" onkeyup="DateFormat(this,this.value,event,false,'3')" onblur="validateDateFormat(this);" id="fileReceivedOrSentDate" tabindex="1" />
					<img src="../images/calendaricon.gif" onclick="showCalendar(this,event)" alt="Date" width="18" id="fileReceivedOrSentDateBtn" height="18" align="absmiddle" tabindex="1" onkeypress="showCalendar(this,event)"/>
				</td>
			</tr>
		
			<tr class="graybox">
				<td>
					<s:text name="lbl.filecat"/><span class="mandatory">*</span>
				</td>
				<td>
					<s:select headerValue="%{getText('sel.catgry')}" headerKey="" name="fileCategory" id="fileCategory"  list="dropdownData.fileCategoryList" listValue="name" listKey="id"  theme="simple" onchange="loadFileSubcategories(this)" tabindex="1" value="fileCategory.id"/>
					<egovtags:ajaxdropdown id="fileSubcategoryDropdown"  fields="['Text','Value']" dropdownId='fileSubcategory' url='dms/fileManagement!getFileSubcategories.action' selectedValue="%{fileSubcategory.id}"/>

				</td>
				<td>
					<s:text name="lbl.filesubcatgry"/>
				</td>
				<td>
					<s:select name="fileSubcategory" tabindex="1" id="fileSubcategory" list="fileSubCategoriesList" listKey="id" listValue='name' headerKey="-1" headerValue="%{getText('sel.subcatgry')}"  value="fileSubcategory.id" />

				</td>
			</tr>
		
			<tr class="whitebox">
				<td>
					<s:text name="lbl.fileheadng"/><span class="mandatory">*</span>
				</td>
				<td>
					<s:textfield name="fileHeading" id="fileHeading" type="text" tabindex="1" />
				</td>
				<td>
					<s:text name="lbl.filestatus"/><span class="mandatory">*</span>
				</td>
				<td >
					<s:select headerValue="%{getText('sel.status')}" headerKey="" name="fileStatus" id="fileStatus"  list="dropdownData.fileStatusList" listValue="description" listKey="id"  theme="simple"  tabindex="1" value="fileStatus.id"/>
				</td>
			</tr>
		
			<tr class="graybox">
				<td>
					<s:text name="lbl.smmryofdoc"/>
				</td>
				<td>
					<s:textarea name="fileSummary" cols="20" rows="2" tabindex="1"></s:textarea>
				</td>
				<td>
					<s:text name="lbl.searchtag"/>
				</td>
				<td >
					<s:textarea name="fileSearchTag" cols="20" rows="2" tabindex="1"></s:textarea>
				</td>				
			</tr>
		</tbody>
	</table>
</fieldset>
<br/>	
<s:if test="%{id !=null}">

<fieldset>
<legend align="left"><b><s:text name="lbl.docattchd"/></b></legend>	
<table width="100%" border="0" cellspacing="0" cellpadding="2" id="fileListTbl"><tbody>
	<tr class="graybox" align="left">
		<th><s:text name="lbl.file"/></th>
		<th><s:text name="lbl.cmmts"/></th>
		<th><s:text name="lbl.cmmthistry"/></th>
		<th>&nbsp;</th>
	</tr>
   <c:forEach items="${fileNames}" var="item" varStatus = "status">
   		<tr class="${status.count%2 == 0 ? 'graybox' : 'whitebox'}" >
           <td style="padding:10px;"><span style="background-color:whitesmoke;border:1 solid gray;padding:3px;"><a href='/egi/docmgmt/ajaxFileDownload.action?docNumber=${fileNumber}&fileName=${item}&moduleName=DMS' target="_parent" >${item}&nbsp;&nbsp;<img src="../images/download.gif"></a></span></td>
           <td style="padding:10px;"><div class='viewdiv'>${fileComment[status.count-1]}</div></td> 
           <td><button onclick="getFileCommentHistory('${fileNumber}','${item}')" type="button"><s:text name="lbl.view"/></button></td>
           <td>
       			<textarea rows="2" cols="20" id="fileComment${status.count}" name="fileComment" style="display:none"></textarea>
       			<input type="hidden" value="${item}" name="fileName">
       			<input type="button"  value="<s:text name="lbl.addcmmt"/>" onclick="javascript:document.getElementById('fileComment${status.count}').style.display='block'">           		
        	</td>            						               						            
       </tr>
       
   </c:forEach>							        											
</tbody>
</table>	
</fieldset>
<br/>
</s:if>
<fieldset>
<legend align="left"><b><s:text name="lbl.attcdoc"/></b></legend>	
<table border="0" cellpadding="4" cellspacing="0" width="100%" id="uploadtbl"><tbody>
	<tr class="graybox">
		<th>
			<s:text name="lbl.doc"/>
		</th>
		<th>
			<s:text name="lbl.cmmts"/>
		</th>
		<th>
			&nbsp;
		</th>
	</tr>
	<tr class="whitebox" align="center">
		<td>
			<input type="file" name="file" tabindex="1"/>
		</td>					
		<td >
			<textarea name="comment" cols="20" rows="2" tabindex="1"></textarea>
		</td>
		<td>
			<img src="../images/add.png"  tabindex="1" onclick="addFileAttachment(this)" title="Add More Files" >
		</td>
	</tr>
</tbody></table>
</fieldset>
<br/>
