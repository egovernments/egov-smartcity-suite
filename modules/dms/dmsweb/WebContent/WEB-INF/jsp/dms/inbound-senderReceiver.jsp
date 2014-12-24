<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/includes/taglibs.jsp" %>

	<fieldset id="inbound">
	<legend align="left" ><b><s:text name='lbl.sndrrcvrinfo'/></b></legend>	
	<fieldset>
	<legend align="left"><b><s:text name='lbl.senderdtl'/></b></legend>	
	<table border="0" cellpadding="4" cellspacing="0" width="100%">
		<tbody>
			

			<tr class="whitebox">
				<td>
					<s:text name='lbl.sentfrm'/>
				</td>
				<td>
					<span>${sender.userSource.name}</span>
				</td>
				<td>
					<s:text name='lbl.nameofsndr'/>
				</td>
				<td>
					<span>${sender.userName}</span>
				</td>
			</tr>
			<tr>		
				<td>
					<s:text name='lbl.fileaddrto'/>
				</td>
				<td >
					<span>${sender.addressedTo}</span>
				</td>							
				<td>
					<s:text name='lbl.addr'/>
				</td>
				<td>
					<div class='viewdiv'>${sender.userAddress}</div>
				</td> 
			</tr>

			<tr class="graybox">
				<td >
					<s:text name='lbl.phno'/>
				</td>
				<td>
					<span>${sender.userPhNumber}</span>
				</td>
				<td>
					<s:text name='lbl.email'/>
				</td>
				<td>
					<span>${sender.userEmailId}</span>
				</td>
			</tr>
		</tbody>
	</table>
	</fieldset>
	<fieldset>
	<legend align="left"><b><s:text name='lbl.recvrdtl'/></b></legend>	
	<table border="0" cellpadding="4" cellspacing="0" width="100%">
		<tbody>
			<tr class="graybox">
				<td>
					<s:text name='lbl.recvrdept'/>
				</td>
				<td>
					<span>${receiver.department.deptName}</span>
				</td>
				<td>
					<s:text name='lbl.recvrdesig'/>
				</td>
				<td >
					<span>${receiver.designation.designationName}</span>
				</td>
			</tr>

			<tr class="whitebox">
				<td>
					<s:text name='lbl.recvr'/>
				</td>
				<td>
					<span>${receiver.employeeInfo.employeeName}/${receiver.position.name}</span>
				</td>
				<td>
					<s:text name='lbl.date'/>
				</td>
				<td >
					<span><s:date name="sysDate" format="dd/MM/yyyy hh:mm a" /></span>
				</td>
			</tr>
		</tbody>
	</table>
	</fieldset>
	</fieldset>	
	<br/>