package org.egov.services.recoveries;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.commons.EgPartytype;
import org.egov.commons.EgwTypeOfWork;
import org.egov.dao.recoveries.EgDeductionDetailsHibernateDAO;
import org.egov.dao.recoveries.TdsHibernateDAO;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.recoveries.EgDeductionDetails;
import org.egov.model.recoveries.Recovery;
import org.egov.utils.Constants;


public class RecoveryService extends PersistenceService<Recovery,Long> {
	
	private EgDeductionDetailsHibernateDAO egDeductionDetHibernateDao;
	private TdsHibernateDAO tdsHibernateDAO;
	private static final Logger LOGGER = Logger.getLogger(RecoveryService.class);
	private static final String EMPTY_STRING="";
	
	
    public  Recovery getTdsById(Long tdsId)
    {
		return (Recovery)tdsHibernateDAO.findById(tdsId,false);
    }
    
    public List<Recovery> findByEstDate(String estimateDate)throws EGOVRuntimeException
    {
        try {
			return tdsHibernateDAO.findByEstDate(estimateDate);
		} catch (Exception e) {
			//
			//EgovUtils.rollBackTransaction();
			throw new EGOVRuntimeException("Exception in searching Tds by estimate Date"+ e.getMessage(), e);
		}
    }
    
    public Recovery getTdsByType(String type)
    {
    	return (Recovery)tdsHibernateDAO.getTdsByType(type);
    }

    public List<Recovery> getAllTdsByPartyType(String partyType){
   	 return tdsHibernateDAO.getAllTdsByPartyType(partyType);
    }
    
    public List getAllTds()
    {
   	 return tdsHibernateDAO.getAllTds();
    }

    public void createTds(Recovery tds)
    {
   	 tdsHibernateDAO.create(tds);
    }

    public void updateTds(Recovery tds)
    {
    	tdsHibernateDAO.update(tds);
    }

    public Recovery findById(Long id)
    {
   	 return (Recovery)tdsHibernateDAO.findById(id, false);
    }
    
	public  EgDeductionDetails getEgDeductionDetailsById(Integer deductionId)
    {
		return (EgDeductionDetails)egDeductionDetHibernateDao.findById(deductionId,false);
    }
   public void createEgDeductionDetails(EgDeductionDetails egDeductionDetails)
	{
		    	egDeductionDetHibernateDao.create(egDeductionDetails);
	}

	public void updateEgDeductionDetails(EgDeductionDetails egDeductionDetails)
	{
		    	egDeductionDetHibernateDao.update(egDeductionDetails);
    }
   public void deleteEgDeductionDetails(EgDeductionDetails egDeductionDetails)
	{
			try
			{
				egDeductionDetHibernateDao.delete(egDeductionDetails);
			}
			catch(Exception e)
			{
				//EgovUtils.rollBackTransaction();
				throw new EGOVRuntimeException("Exception in Deleting EgDeductionDetails."+e.getMessage(),e);
			}
	}
   
    public List<EgDeductionDetails> findByTds(Recovery tds)
	{
			 return egDeductionDetHibernateDao.findByTds(tds);
    }
   
   public List<Recovery> getAllActiveTds()
   {
   	return tdsHibernateDAO.getAllActiveTds();
   }
   
   public List<Recovery> getAllActiveAutoRemitTds()
   {
   	return tdsHibernateDAO.getAllActiveAutoRemitTds();
   }
   
   public List<Recovery> getActiveTdsFilterBy(String estimateDate, BigDecimal estCost, EgPartytype egPartytype, EgwTypeOfWork egwTypeOfWork, EgwTypeOfWork egwSubTypeOfWork)
   {
   	return tdsHibernateDAO.getActiveTdsFilterBy(estimateDate, estCost, egPartytype, egwTypeOfWork, egwSubTypeOfWork);
   }
   
   public List<EgDeductionDetails> getEgDeductionDetailsFilterBy(Recovery tds, BigDecimal amount, String date, EgwTypeOfWork egwTypeOfWork, EgwTypeOfWork egwSubTypeOfWork)
   {
		return egDeductionDetHibernateDao.getEgDeductionDetailsFilterBy(tds, amount, date, egwTypeOfWork, egwSubTypeOfWork);
   }
   
   public List<Recovery> recoveryForPartyContractor(Date asOndate)throws ValidationException
   {
			return tdsHibernateDAO.recoveryForPartyContractor(asOndate);
   }

	public void setEgDeductionDetHibernateDao(
			EgDeductionDetailsHibernateDAO egDeductionDetHibernateDao) {
		this.egDeductionDetHibernateDao = egDeductionDetHibernateDao;
	}
	
	public void setTdsHibernateDAO(TdsHibernateDAO tdsHibernateDAO) {
		this.tdsHibernateDAO = tdsHibernateDAO;
	}

	public EgPartytype getPartytypeByCode(String code) {
		return tdsHibernateDAO.getPartytypeByCode(code);
	}

	public EgwTypeOfWork getTypeOfWorkByCode(String code) {
		return tdsHibernateDAO.getTypeOfWorkByCode(code);
	}
	
	public EgPartytype getSubPartytypeByCode(String code) {
		return tdsHibernateDAO.getSubPartytypeByCode(code);
	}
	
	public Recovery getTdsByTypeAndPartyType(String type, EgPartytype egPartytype) {
		return tdsHibernateDAO.getTdsByTypeAndPartyType(type, egPartytype);
	}
	 
	public BigDecimal getDeductionAmount(String recoveryCode,String partyType,String subPartyType,String docType,
			BigDecimal grossAmount,Date asOnDate) throws Exception  {
		
		SimpleDateFormat dateFormatter =new SimpleDateFormat("dd/MM/yyyy",Constants.LOCALE);
		BigDecimal incomeTax = new BigDecimal(0);
		BigDecimal surcharge= new BigDecimal(0);
		BigDecimal education= new BigDecimal(0);
		BigDecimal total = new BigDecimal(0);
		BigDecimal deductionAmt = new BigDecimal(0);
		EgDeductionDetails egDeductionDetails = null;
		
		if(null == recoveryCode || recoveryCode.trim().equals("")) {
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Recovery Code is missing");
			throw new ValidationException(EMPTY_STRING,"Recovery Code is missing");
		}
		
		if(null == partyType || partyType.trim().equals("")) {
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Party Type is missing");
			throw new ValidationException(EMPTY_STRING,"Party Type is missing");
		}
		
		if(null == grossAmount ) {
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Gross Amount is missing");
			throw new ValidationException(EMPTY_STRING,"Gross Amount is missing");
		}
		
		if(null == asOnDate ) {
			if(LOGGER.isDebugEnabled())     LOGGER.debug("AsOnDate is missing");
			throw new ValidationException(EMPTY_STRING,"AsOnDate is missing");
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("getDeductionAmount() -> recoveryCode :"+recoveryCode+" | partyType :"+partyType+" | grossAmount :"+grossAmount+" | asOnDate :"+dateFormatter.format(asOnDate)
				+" | docType :"+docType);
		
		EgwTypeOfWork egwTypeOfWork = null;
		EgPartytype egSubPartytype = null;
		
		EgPartytype egPartytype = getPartytypeByCode(partyType);
		Recovery recovery = getTdsByTypeAndPartyType(recoveryCode, egPartytype);

		if(recovery==null)
			throw new ValidationException(EMPTY_STRING, "Recovery with "+recoveryCode+" code  and "+egPartytype +" party type is invalid.");
		if(recovery.getRecoveryMode()=='M')	
			return BigDecimal.valueOf(-1);		
		
		if(null != docType)
			egwTypeOfWork = getTypeOfWorkByCode(docType);
		
		if(null != subPartyType)
			egSubPartytype = getSubPartytypeByCode(subPartyType);
		
		try {
			egDeductionDetails = egDeductionDetHibernateDao.findEgDeductionDetailsForDeduAmt(recovery,
												egPartytype, egSubPartytype, egwTypeOfWork, asOnDate);
		} catch(Exception e) {
			LOGGER.error("Exception in egDeductionDetails fetching :"+e);
			throw new ValidationException(EMPTY_STRING, "Error while fetching the date for this "+recoveryCode+" code for this "+dateFormatter.format(asOnDate) +" date. "+ e.getMessage());
		}
		
		if(null == egDeductionDetails) {
			throw new ValidationException(EMPTY_STRING, "There is no data for this "+recoveryCode+" code for this "+dateFormatter.format(asOnDate) +" date.");
		}
		
		if(null != recovery.getCalculationType() && recovery.getCalculationType().equalsIgnoreCase("flat")) {
			if(null != egDeductionDetails.getFlatAmount()) deductionAmt = egDeductionDetails.getFlatAmount();
		} else {
			if(null != egDeductionDetails.getIncometax()) incomeTax = egDeductionDetails.getIncometax();
			if(null != egDeductionDetails.getSurcharge()) surcharge = egDeductionDetails.getSurcharge();
			if(null != egDeductionDetails.getEducation()) education = egDeductionDetails.getEducation();
			total = incomeTax.add(surcharge).add(education);
			if(LOGGER.isDebugEnabled())     LOGGER.debug("total IT/SC/EC "+total);
			deductionAmt = grossAmount.multiply(total.divide(new BigDecimal(100)));
		}
		if(LOGGER.isDebugEnabled())     LOGGER.debug("deductionAmt :"+deductionAmt);
		return deductionAmt = deductionAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
}
