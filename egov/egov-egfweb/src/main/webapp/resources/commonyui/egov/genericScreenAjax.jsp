<%@ page errorPage="/error/error.jsp" language="java" import="java.sql.*,org.egov.infstr.utils.HibernateUtil,java.util.*,org.egov.infstr.utils.*,org.hibernate.SQLQuery,org.egov.infstr.commons.Module,org.apache.log4j.Logger,org.egov.EGOVRuntimeException" %>
<%String values = "";
	List list=null;
	String xmlConfigName=null;
	String qryLevel=null;
	String qryName=null;
	String target="";
	Map mapNamedParams=new HashMap();
	
	Map map=request.getParameterMap();
	String queryParamName=null;
	Enumeration enumParamNames=request.getParameterNames();
	while(enumParamNames.hasMoreElements()) {
		queryParamName=(String)enumParamNames.nextElement();
		if(queryParamName.equalsIgnoreCase("xmlConfigName"))
		{
		xmlConfigName=request.getParameter(queryParamName);
		}
		else if(queryParamName.equalsIgnoreCase("qryLevel"))
		{
		qryLevel=request.getParameter(queryParamName);
		}
		else if(queryParamName.equalsIgnoreCase("qryName"))
		{
		qryName=request.getParameter(queryParamName);
		}
		else if(queryParamName.equalsIgnoreCase("text"))
		{
		mapNamedParams.put(queryParamName,request.getParameter(queryParamName).toUpperCase()+"%");	
		}
		else
		mapNamedParams.put(queryParamName,request.getParameter(queryParamName));
	}
	
	AjaxQueryUtil ajaxQueryUtil = new AjaxQueryUtil();
	list=ajaxQueryUtil.executeAjaxQuery(xmlConfigName,qryLevel,qryName,mapNamedParams);
	
	if(list!=null && !list.isEmpty()) {
		Iterator itr=list.iterator();
		StringBuffer result=new StringBuffer();
		StringBuffer id=new StringBuffer();
		StringBuffer name=new StringBuffer();
		StringBuffer narration=new StringBuffer();
		int i = 0;
		
		while(itr.hasNext()){
		 	Object[] rs=(Object[])itr.next();
			if(i > 0) {
				id.append("+");
				id.append(rs[0]);
				name.append("+");
				name.append(rs[1]);
				narration.append("+");
				narration.append(rs[2]);
			} else {
				id.append(rs[0]);
				name.append(rs[1]);
				narration.append(rs[2]);
			}
			i++;	
		}
		result.append(id);
		result.append("^");
		result.append(name);
		result.append("^");
		result.append(narration);
		result.append("^");
		values=result.toString();
	}
	response.setHeader("Cache-Control", "no-cache");
	response.setContentType("text/plain");
	out.print(values);%>