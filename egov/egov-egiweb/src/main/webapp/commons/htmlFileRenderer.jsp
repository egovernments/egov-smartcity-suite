<%@page import="java.io.InputStream" %>
<%
InputStream in = null;
try {
in = (InputStream)application.getAttribute("fileStream");
application.removeAttribute("fileStream");
int i= 0;
while(i != -1) {
   i = in.read();
   if(i != -1)
   	out.print((char)i) ;
}

} catch (Exception e) {
    System.out.println(e);
}
finally {
    if (in !=null) {
		try { in.close(); } catch(Exception e){}
    }
}
%>
