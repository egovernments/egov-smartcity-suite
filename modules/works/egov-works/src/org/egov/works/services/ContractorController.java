package org.egov.works.services;


import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.client.filter.SetDomainJndiHibFactNames;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.StringUtils;
import org.egov.lib.rjbac.user.User;
import org.egov.works.models.masters.Contractor;
import org.egov.works.models.masters.ContractorDetail;
import org.egov.works.models.masters.ContractorDetailXml;
import org.egov.works.models.masters.ContractorXml;
import org.egov.works.models.masters.Contractors;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;


@Path("/contractor")
public class ContractorController {

	private String DATE_FORMAT_DDMMYYY = "dd/MM/yyyy";
	private static final Logger LOGGER = Logger.getLogger(ContractorController.class);
	private static String  configLocation[] = {"/WEB-INF/applicationContext.xml"}; 
	private static final String SUPER_USER_NAME = "egovernments";

	protected static ApplicationContext getApplicationContext() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(configLocation);
		return ctx;
	}


	/*public ContractorController()
	{
		System.out.println(" Contractor Controller Initialized ");
	}*/


	@GET
	@Produces(MediaType.TEXT_XML)
	public String generateContractorXML()
	{
		setThreadLocales();
		List<Contractor> contractorList = (List<Contractor>)getContractorBidderService().getAllActiveBidders();
		List<ContractorXml> contractorXMLList = new ArrayList<ContractorXml>();
		Contractors contractors = new Contractors();

		try{
			for(Contractor contractor:contractorList){
				ContractorXml contractorXml = new ContractorXml();
				contractorXml.setName(contractor.getName());
				contractorXml.setCode(contractor.getCode());

				List<ContractorDetailXml> contractorDetailXMlList = new ArrayList<ContractorDetailXml>();

				for(ContractorDetail contractorDetail: contractor.getContractorDetails()){
					ContractorDetailXml contractorDetailXML = new ContractorDetailXml();
					if(contractorDetail!=null){
						contractorDetailXML.setCclass(contractorDetail.getGrade()!=null ? 
								contractorDetail.getGrade().getGrade():null);
						contractorDetailXML.setFromDate(contractorDetail.getValidity()!=null? 
								contractorDetail.getValidity().getStartDate():null);

						contractorDetailXML.setToDate(contractorDetail.getValidity().getEndDate());
						contractorDetailXML.setDeptCode(contractorDetail.getDepartment()!=null?
								contractorDetail.getDepartment().getDeptCode():StringUtils.EMPTY);
						contractorDetailXMlList.add(contractorDetailXML);
					}
				}

				contractorXml.setContractorDetails(contractorDetailXMlList);
				contractorXMLList.add(contractorXml);
			}
			contractors.setContractorList(contractorXMLList);
		}
		catch (Exception e) {
			LOGGER.error("Error occurred while trying to get information from works tender Server {} ", e);
			throw new EGOVRuntimeException(e.getMessage());
		}
		return toXML(contractors);
	}


	public String toXML (Contractors obj) {
		XStream xStream = createXStream();
		String[] array = {DATE_FORMAT_DDMMYYY};
		xStream.registerConverter(new DateConverter(DATE_FORMAT_DDMMYYY,array));
		return xStream.toXML(obj);
	}

	protected XStream createXStream() {
		XStream xstream = new XStream();
		xstream.autodetectAnnotations(true);
		return xstream;
	}

	public ContractorService getContractorBidderService() {
		ContractorService contractorService = new ContractorService();
		contractorService.setSessionFactory(new SessionFactory());
		contractorService.setType(Contractor.class);
		return contractorService;
	}


	private void setThreadLocales() 
	{
		String[] cities = "localhost".split(",");
		String jndiName="";
		String hibFactName ="";
		String cityURL="";

		for(String city:cities)
		{

			jndiName=EGovConfig.getProperty(city, "","JNDIURL");
			hibFactName = EGovConfig.getProperty(city, "","HibernateFactory");
			cityURL="http://" + city;
			LOGGER.debug("Setting thread locals: [" + cityURL + "][" + jndiName + "][" + hibFactName + "]");
			SetDomainJndiHibFactNames.setThreadLocals(cityURL,jndiName,hibFactName);
		}

		User user = null;
		PersistenceService persistenceService =(PersistenceService)getApplicationContext().getBean("persistenceService");
		user = (User)persistenceService.find("from UserImpl where userName=?",SUPER_USER_NAME);
		EGOVThreadLocals.setUserId(user.getId().toString());
	}


}
