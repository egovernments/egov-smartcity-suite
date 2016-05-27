package org.egov.infra.utils.autonumber;

import java.util.Map;
import java.util.Set;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class AutonumberServiceBeanResolver { 

	@Autowired
	private ApplicationContext context;

	public Object getBean(Class  autonumberInterface)
	{

		Object autonumberBean=null;

		Map beansOfType = context.getBeansOfType(autonumberInterface);
		if(beansOfType.isEmpty())
		{
			throw new ApplicationRuntimeException("Could not find any implementation bean for interface "+autonumberInterface.getSimpleName());
		}
		if(beansOfType.size()==1)
		{
			autonumberBean=beansOfType.get(beansOfType.keySet().toArray()[0]);
		}else
		{
			Object tempBean;
			Set<String> keySet = beansOfType.keySet();
			for(String s:keySet )
			{
				tempBean=beansOfType.get(s);
				if(tempBean.getClass().isAnnotationPresent(org.egov.infra.utils.autonumber.OverrideImpl.class))
				{
				autonumberBean=tempBean;
				break;
				}
			}
			if(autonumberBean==null)
			{
				autonumberBean=beansOfType.get(beansOfType.keySet().toArray()[0]);
			}

		}

		return autonumberBean;		
	}
}
