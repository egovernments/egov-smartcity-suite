<%@page contentType="text/html" %>
<%@page contentType="text/html" %>
<%@page import="java.util.*,org.egov.lib.rjbac.user.ejb.server.UserServiceImpl,org.egov.lib.rjbac.user.UserImpl" %>
<%@page import="org.egov.lib.rjbac.user.dao.UserDAO,org.egov.infstr.security.utils.CryptoHelper,org.egov.lib.rjbac.user.User"%>
<%@page import="java.text.SimpleDateFormat.*"%>
<%@ page import=" org.w3c.dom.html.*,java.util.*,org.egov.infstr.utils.HibernateUtil,org.egov.infstr.utils.EGovConfig,org.egov.infstr.client.filter.EGOVThreadLocals,org.egov.lib.rjbac.user.User,org.egov.lib.rjbac.user.ejb.api.UserService,
 		java.text.ParsePosition"
%>

<%@ page import="org.egov.lib.rjbac.user.dao.UserDAO" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-nested" prefix="nested" %>
 <%@ include file="/staff/egovHeader.jsp" %>
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Encoding Password</title>
	<LINK REL=stylesheet HREF="/egov.css" TYPE="text/css">
	<LINK REL=stylesheet HREF="/ccMenu.css" TYPE="text/css">

 <script language="JavaScript" src="/dhtml.js" type="text/JavaScript"></script>
</head>

<%
try{
String factoryName = EGovConfig.getProperty("HIBFACTORYNAME","", "HibernateFactory");
System.out.println("factoryName"+factoryName);
if(factoryName!=null&&!factoryName.equals(""))			
{
EGOVThreadLocals.setHibFactName(factoryName);	
HibernateUtil.getCurrentSession();
HibernateUtil.beginTransaction();
}
HibernateUtil.getSessionFactory();
UserDAO userDao=new UserDAO(); 
List passwords=userDao.getAllPasswords();

System.out.println("LLLLLLLLLLLLLLLLLLLLLLL:::::"+passwords.size());


          for(Iterator it=passwords.iterator();it.hasNext();)
             {
             User usr =(User)it.next();
             if(usr.getPwd() ==null || usr.getPwd().equals("")  )
             {
             
             CryptoHelper helper = new CryptoHelper();
             String encpassword = helper.encrypt(usr.getPwdReminder());
             usr.setPwd(encpassword);
             userDao.createOrUpdateUserWithPwdEncryption(usr);
             
             }
             
             }
          
        
 HibernateUtil.commitTransaction(); 
    
  }
  catch(Exception e)
  {
    e.printStackTrace();
    System.out.println("Error occurred::::"+e);
    HibernateUtil.rollbackTransaction();
    HibernateUtil.closeSession(); 
  }  
HibernateUtil.closeSession(); %>


</html>