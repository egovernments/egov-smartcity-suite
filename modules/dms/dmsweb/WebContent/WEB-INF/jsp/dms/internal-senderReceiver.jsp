<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/includes/taglibs.jsp" %>
<fieldset id="internal">
	<legend align="left"><b><s:text name="lbl.filemovdtl"/></b></legend>	
	<fieldset>
	<legend align="left"><b><s:text name="lbl.senderdtl"/></b></legend>	
	<table border="0" cellpadding="4" cellspacing="0" width="100%">
		<tbody>
			<tr class="graybox">
				<td>
					<s:text name="lbl.senderdept"/>
				</td>
				<td>
					<span>${sender.department.deptName}</span>
				</td>
				<td>
					<s:text name="lbl.senderdesig"/>
				</td>
				<td>
					<span>${sender.designation.designationName}</span>
				</td>
			</tr>
	
			<tr class="whitebox">
				<td>
					<s:text name="lbl.sender"/>
				</td>
				<td>
					<span>${sender.employeeInfo.employeeName} / ${sender.position.name}</span>
				</td>
				<td>
					<s:text name="lbl.date"/>
				</td>
				<td>
					<span><s:date name="sysDate" format="dd/MM/yyyy hh:mm a" /></span>
				</td>
			</tr>
		</tbody>
	</table>
	</fieldset>
	<fieldset>
	<legend align="left"><b><s:text name="lbl.recvrdtl"/></b></legend>	
	<table border="0" cellpadding="4" cellspacing="0" width="100%">
		<tbody>
			<tr class="graybox">
				<td>
					<s:text name="lbl.recvrdept"/>
				</td>
				<td>
					<span>${receiver.department.deptName}</span>
				</td>
				<td>
					<s:text name="lbl.recvrdesig"/>
				</td>
				<td>
					<span>${receiver.designation.designationName}</span>
				</td>
			</tr>
	
			<tr class="whitebox">
				<td>
					<s:text name="lbl.recvr"/>
				</td>
				<td>
					<span>${receiver.employeeInfo.employeeName}/${receiver.position.name}</span>
				</td>
				<td>
					<s:text name="lbl.date"/>
				</td>
				<td>
					<span><s:date name="sysDate" format="dd/MM/yyyy hh:mm a" /></span>
				</td>
			</tr>
		</tbody>
	</table>
	</fieldset>
	</fieldset>