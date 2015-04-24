<%@ taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>

<s:set name="theme" value="'simple'" scope="page" />
	<s:if test="%{showActions.size!=0}">
	<s:iterator id="" value="showActions">
	<li><a href="#" onclick="openaction('<s:property/>');" ><s:property/></a></li>	    	
	</s:iterator>
	</s:if>
	<s:else>
	<li><p align="center"> No Actions Available</p></li>	  
	</s:else>
