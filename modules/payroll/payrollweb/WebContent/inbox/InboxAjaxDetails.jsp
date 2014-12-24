

<%@ page language="java" import="java.util.*,java.lang.reflect.Method, org.egov.infstr.workflow.*,org.jbpm.graph.exe.ExecutionContext, org.egov.infstr.workflow.InfraWorkFlowVariables" %>

	<%

	String values = "";
			
	try
	{
		
	if(request.getParameter("taskInstanceId") != "" && request.getParameter("taskInstanceId") != null){
				System.out.println("test------------"+request.getParameter("taskInstanceId"));
			String taskInstanceId[]=request.getParameter("taskInstanceId").split(",");
			String objId[]=request.getParameter("objId").split(",");
			String observation[]=request.getParameter("observation").split(",");
			String transitionName[]=request.getParameter("transitionName").split(",");

			String userId = session.getAttribute("com.egov.user.LoginUserId").toString();
			String userName = session.getAttribute("com.egov.user.LoginUserName").toString();

			String aggregateTxns = "";
			String txnClassName = "";
			String txnMethodName = "";
			String transition = "";

			EgovWorkFlow obj = new EgovWorkFlow();

			TaskInstanceBean beanObj = null;

			ArrayList beanList = new ArrayList();

			for(int i = 0; i <taskInstanceId.length; i++)
			{

				ExecutionContext exeContext = obj.endTaskInstance(taskInstanceId[i], objId[i], observation[i], transitionName[i], userId, userName);

				aggregateTxns = (String) exeContext.getVariable(InfraWorkFlowVariables.AGGREGATE_TXNS);
				txnClassName = (String) exeContext.getVariable(InfraWorkFlowVariables.TXN_CLASSNAME);
				txnMethodName = (String) exeContext.getVariable(InfraWorkFlowVariables.TXN_METHODNAME);
				transition = (String) exeContext.getVariable(InfraWorkFlowVariables.PROCESSING_TRANSITION);

				if(aggregateTxns.equalsIgnoreCase("true") && transition.equalsIgnoreCase(transitionName[i]))
				{
					String status[] = transitionName[i].split("-");	

					beanObj = new TaskInstanceBean();
					beanObj.setObjectId(objId[i]);
					beanObj.setStatus(status[0]);
					beanList.add(beanObj);
				}

			}

			if(beanList.size() > 0 && aggregateTxns.equalsIgnoreCase("true"))
			{
				Class cls = Class.forName(txnClassName);

				Class[] class_name = new Class[1];
				class_name[0] = Class.forName("java.util.ArrayList");

				Object[] obj_name = new Object[1];
				obj_name[0] = (ArrayList) beanList ;

				Method method = cls.getMethod(txnMethodName,class_name);

				method.invoke(cls.newInstance(),obj_name);	
			}

			values = "Executed Successfully ^";		
		}
		else{
			values = "^";		
		}
	}
	catch(Exception ex)
	{
		System.out.println("Exception -----> "+ex.getMessage());
		values = "Error while processing ^";
		throw new Exception("Exception - while processing endTaskInstance");
	}	
	
	
	
	response.setContentType("text/xml");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(values);
	%>
<html>
<body>
</body>
</html>
