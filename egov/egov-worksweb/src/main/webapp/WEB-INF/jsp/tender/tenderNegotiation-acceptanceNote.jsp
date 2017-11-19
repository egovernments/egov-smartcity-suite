<%--
  ~    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) 2017  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~            Further, all user interfaces, including but not limited to citizen facing interfaces,
  ~            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
  ~            derived works should carry eGovernments Foundation logo on the top right corner.
  ~
  ~            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
  ~            For any further queries on attribution, including queries on brand guidelines,
  ~            please contact contact@egovernments.org
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  ~
  --%>

<%@ include file="/includes/taglibs.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
	<META HTTP-EQUIV="CONTENT-TYPE" CONTENT="text/html; charset=utf-8">
	<TITLE>Authority for Tender Acceptance</TITLE>
	<meta http-equiv="Pragma" content="no-cache"/>
	
<link href="<c:url value='/resources/css/works.css'/>" rel="stylesheet" type="text/css" />
<%-- <link href="<c:url value='/css/commonegov.css' context='/egi'/>" rel="stylesheet" type="text/css" /> --%>

</HEAD>

<BODY>
<script>
	function closePage(){
		var wind;
		var data = new Array();
		wind=window.dialogArguments;
		if(wind==undefined){
			wind=window.opener;
			data='success';
			window.opener.update(data);
		}
		else{
			wind=window.dialogArguments;
			data='success';
			wind.result=data;
		}
		
		window.close();
	}
	</script>

<div class="navibarshadowwk"></div>
<div class="formmainbox">
	<div class="insidecontent">
		<div class="rbroundbox2"><div class="rbtop2"><div></div></div>
			<div class="rbcontent2">

				<div id="header-container">
				<table id="table-header" cellpadding="0" cellspacing="0"
					align="center">
					<tr>
						<th width="2%">Sl No:</th>
						<th width="3%">Authority</th>
						<th width="3%">Power</th>
					</tr>
				</table>
				</div>
						
				<table width="100%" border="0" cellpadding="0"	cellspacing="0" id="table-body">
					<tr>
						<td width="2%">1</td>
						<td width="3%"><P STYLE="margin-bottom: 0.35cm">Executive Engineer</P>
							<P STYLE="margin-bottom: 0.35cm">(Zones/Departments)</P>
							<P>Divisional Electrical engineer</P></td>
						<td width="3%"><P STYLE="margin-bottom: 0.35cm">Upto Rs. 10 lakhs</P>
							<P>( Upto 3% excess ) 
							</P></td>
					</tr>
					
					<tr>
						<td width="2%">2</td>
						<td width="3%"><P>Zonal Officer</P></td>
						<td width="3%"><P STYLE="margin-bottom: 0.35cm">Upto Rs. 10 lakhs</P>
								<P STYLE="margin-bottom: 0.35cm">( Upto 6% excess )</P>
								<P STYLE="margin-bottom: 0.35cm">Upto Rs. 15 lakhs</P>
								<P STYLE="margin-bottom: 0.35cm">( Upto 3 % excess )</P>
						</td>
					</tr>
					
					<tr>
						<td width="2%">3</td>
						<td width="3%"><P STYLE="margin-bottom: 0.35cm">Superintending Engineer</P>
										<P>City Engineer</P></td>
						<td width="3%"><P STYLE="margin-bottom: 0.35cm">Upto Rs. 15 lakhs</P>
										<P STYLE="margin-bottom: 0.35cm">( Upto 6 % excess )</P>
										<P STYLE="margin-bottom: 0.35cm">Upto Rs. 25 lakhs</P>
										<P STYLE="margin-bottom: 0.35cm">( Upto 3 % excess )</P>
						</td>
					</tr>
					
					<tr>
						<td width="2%">4</td>
						<td width="3%"><P STYLE="margin-bottom: 0.35cm">Chief Engineer(GI)</P>
										<P>Chief Engineer(B &amp; Br)</P></td>
						<td width="3%"><P STYLE="margin-bottom: 0.35cm">Upto Rs. 25 lakhs</P>
										<P STYLE="margin-bottom: 0.35cm">( Upto 6 % excess )</P>
										<P STYLE="margin-bottom: 0.35cm">Upto Rs. 30 lakhs</P>
										<P STYLE="margin-bottom: 0.35cm">( Upto 3 % excess )</P>
						</td>
					</tr>
					
					<tr>
						<td width="2%">5</td>
						<td width="3%"><P STYLE="margin-bottom: 0.35cm">Chief Engineer(GI)</P>
										<P STYLE="margin-bottom: 0.35cm">Chief Engineer(B &amp; Br)</P>
										<P STYLE="margin-bottom: 0.35cm">Financial Advisor</P>
										<P>Any two superintending engineers nominated by the engineer from
										time to time</P></td>
						<td width="3%"><P STYLE="margin-bottom: 0.35cm">Upto Rs. 35 lakhs</P>
										<P STYLE="margin-bottom: 0.35cm">( Irrespective of tender excess )</P>
						</td>
					</tr>
					
					<tr>
						<td width="2%">6</td>
						<td width="3%"><P>Committee</P></td>
						<td width="3%"><P>Greater than 35 lakhs</P></td>
					</tr>
				</table>
			</div>
			<div class="rbbot2"><div></div></div>
		</div>
	</div>
</div>

<div class="buttonholderwk" id="buttons">
	<input type="button" class="buttonfinal" value="APPROVE" id="approveButton" name="approveButton" onclick="closePage();" />
	<input type="button" class="buttonfinal" value="CANCEL" id="cancelButton" name="cancelButton" onclick="self.close();" />
</div>

</BODY>
</HTML>
