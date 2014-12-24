package org.egov.payroll.client.payslip;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

public class QuartzServlet extends GenericServlet {
	public static final Logger logger = Logger.getLogger(QuartzServlet.class.getClass());
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		/*
		String city = EGovConfig.getProperty("payroll_egov_config.xml","city", "", "SchedulerJob");
		String cityNames[] = city.split(",");
		for (String cityURL : cityNames) {
			logger.info("Scheduling Job ..");
			logger.info("city URL  >>>>  " + cityURL);
			String jndi = EGovConfig.getProperty("payroll_egov_config.xml",cityURL, "", "JNDIURL");
			logger.info("jndi name  >>>>  " + jndi);
			String hibFactName = EGovConfig.getProperty("payroll_egov_config.xml",cityURL, "",
					"HibernateFactory");
			logger.info("hibernate faCTORY name >>>>> " + hibFactName);
			if (cityURL != null && !cityURL.equals("") && jndi != null
					&& !jndi.equals("") && hibFactName != null
					&& !hibFactName.equals("")) {
				JobDetail jd = new JobDetail("Payslip generation", "Job " + cityURL,
						AutoGenerationPaySlipJob.class);
				// sb: is method required??
				jd.getJobDataMap().put("method", "generateJob");
				// jd.setDurability(true);
				Object[] jdArgs = new Object[3];
				jdArgs[0] = cityURL;
				jdArgs[1] = jndi;
				jdArgs[2] = hibFactName;
				jd.getJobDataMap().put("args", jdArgs);
				
				CronTrigger cronTrigger = new CronTrigger(cityURL,cityURL+" Quartz");
				try {
					String cronExpr = null;
					//Get the cron Expression as an Init parameter
					cronExpr = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","BatchPayslipGenCronExp",new Date()).getValue();
					//cronExpr = EGovConfig.getProperty("payroll_egov_config.xml",cityURL, "", "CronExp");
					if (cronExpr != null && !cronExpr.equals("")) {
						logger.info(cronExpr);
						cronTrigger.setCronExpression(cronExpr);
						Scheduler sched = StdSchedulerFactory
								.getDefaultScheduler();
						sched.scheduleJob(jd, cronTrigger);
						logger.info("Job scheduled now ..");
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		}*/
		
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

}