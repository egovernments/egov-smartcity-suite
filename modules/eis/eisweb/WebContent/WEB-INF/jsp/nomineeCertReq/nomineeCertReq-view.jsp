<!--
	Program Name : nomineeCertReq-view.jsp
	Author		: jagadeesan
	Created	on	: 18-03-2010
	Purpose 	: Nominee certification required view.
 -->
<%@ include file="/includes/taglibs.jsp" %>
<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>  
	<head>  
	    <title>View Nominee Certification Required</title>
	    <script language="JavaScript"  type="text/JavaScript">
	    	
	    </script>
	</head>

	<body>  
		<s:form action="nomineeCertReq" theme="simple" >  
			<center>
			
			<div class="navibarshadowwk"></div>
				<div class="formmainbox">
					<div class="insidecontent">
				  		<div class="rbroundbox2">
							<div class="rbtop2">
								<div>
								</div>
							</div>
							<div class="rbcontent2">
								<table width="95%" cellpadding="0" cellspacing="0" border="0" id="nomineeCertReqTable">
									<span id="msg">
										<s:actionerror cssClass="mandatory"/>  
										<s:fielderror cssClass="mandatory"/>
										<s:actionmessage cssClass="actionmessage"/>
									</span>
									<tr>
										<td colspan="2" class="headingwk">
											<div class="arrowiconwk"><img src="../common/image/arrow.gif" /></div>
							               	<div class="headplacer"><s:text name="NomineeCertReq.Heading"/></div>
							            </td>
							        </tr>
									
									<tr>
										<td class="whiteboxwk"><s:text name="NomineeCertReq.RelationType"/> </td>
										<td class="whitebox2wk"><input type="text" readonly="true" value="${relationType.nomineeType}"/></td>
									</tr>
									
									<tr>
										<td  class="greyboxwk"><s:text name="NomineeCertReq.CertType"/> </td>
							   			<td class="greybox2wk">
											<s:select name="certTypeIds" id="certTypeIds" multiple="true" list="dropdownData.certReqListForRightSelect" listKey="id" 
											listValue="type"  size="4" cssStyle="width:200px" />
										</td>
									</tr>
									<tr>
						               	<td colspan="2" class="shadowwk"></td>
						            </tr>
									<tr>
								       <td align="center" colspan="2"></td>
						           </tr>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div class="buttonholderwk">
					<input type="button" value="Close" onclick="javascript:window.close()" class="buttonfinal"/> 
				</div>
			</center>
		</s:form>
	</body>
</html>