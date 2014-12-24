<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/includes/taglibs.jsp" %>

<fieldset id="outbound">
<legend align="left"><b><s:text name="lbl.filemovdtl"/></b></legend>
<fieldset>
<legend align="left"><b><s:text name="lbl.recvrdtl"/></b></legend>
<table border="0" cellpadding="4" cellspacing="0" width="100%">
	<tbody>
		<tr class="graybox">
			<td>
				<s:text name="lbl.sentto"/>
			</td>
			<td>
				<span>${receiver.userSource.name}</span>
			</td>
			<td>
				<s:text name="lbl.nameofrcvr"/>
			</td>
			<td>
				<span>${receiver.userName}</span>
			</td>							
		</tr>

		<tr class="whitebox">
			<td>
				<s:text name="lbl.addr"/>
			</td>
			<td>
				<div class='viewdiv'>${receiver.userAddress}</div>
			</td>
			<td >
				<s:text name="lbl.phno"/>
			</td>
			<td>
				<span>${receiver.userPhNumber}</span>
			</td>
		</tr>

		<tr class="graybox">							
			<td>
				<s:text name="lbl.email"/>
			</td>
			<td>
				<span>${receiver.userEmailId}</span>
			</td>
			<td>
				<s:text name="lbl.outboundfileno"/>
			</td>
			<td>
				<span>${receiver.outboundFileNumber}</span>
			</td>
		</tr>
	</tbody>
</table>
</fieldset>
</fieldset>