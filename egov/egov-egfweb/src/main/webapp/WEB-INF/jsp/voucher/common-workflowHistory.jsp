<%@ taglib prefix="egov" tagdir="/WEB-INF/tags"%>
<%@ page language="java"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-nested" prefix="nested"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
 <span>
	               
						  	<div id="wfHistoryDiv">
						  	<h2> Workflow History</h2>
							  	<c:import url="/WEB-INF/jsp/workflow/workflowHistory.jsp" context="/egi">
							        <c:param name="stateId" value="${stateId}"></c:param>
							    </c:import>
						  	</div>   
 </span>
 <br/>
 <br/>
 <input type="button" id="button2" value="Close" onclick="javascript:window.close()" class="button"/>

