package org.egov.tender.services.common;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Bidder;
import org.egov.commons.EgwStatus;
import org.egov.commons.service.BidderTypeService;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.EGovConfig;
import org.egov.lib.admbndry.CityWebsite;
import org.egov.lib.admbndry.CityWebsiteDAO;
import org.egov.lib.citizen.model.Owner;
import org.egov.lib.rjbac.user.User;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.tender.TenderableGroupType;
import org.egov.tender.model.TenderFileType;
import org.egov.tender.model.TenderNotice;
import org.egov.tender.model.TenderableEntityGroup;
import org.egov.tender.utils.TenderConstants;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * 
 * @author Pradeep
 *
 */

@SuppressWarnings("unchecked")
public class TenderCommonService {

	private static final Logger logger	= Logger.getLogger(TenderCommonService.class);
	public static final String STATUSQUERY = "from EgwStatus where moduletype=? and code=?";
	private EisUtilService eisService;
	private ScriptService scriptService;
	private PersistenceService persistenceService;
	private PersistenceService<Owner, Integer> ownerService;
	private Map<String,BidderTypeService> bidderTypeServiceMap;
	private static final Map<Integer,String> userMap=new HashMap<Integer,String>();
  
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	
	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}
	
	public void setScriptService(ScriptService scriptService) {
		this.scriptService = scriptService;
	}
	
	public void setOwnerService(PersistenceService<Owner, Integer> ownerService) {
		this.ownerService = ownerService;
	}

	public List<String>  getFurtherWorkflowState(String currentStatValue,String mode,StateAware stateAwareItem) 
	{
		logger.info("scriptService--------->"+scriptService);
		return (List<String>)scriptService.executeScript(TenderConstants.TENDERNEXTSTATUSSCRIPT,ScriptService.createContext("wfItem",stateAwareItem,"currentStatValue",currentStatValue,"mode",mode));

	}
	public boolean getAppconfigValue(String module,String key,String defaultValue)
	{
		String appConfigValue= EGovConfig.getAppConfigValue(module, key, defaultValue);
		return (appConfigValue!=null && (appConfigValue.equals("1")|| appConfigValue.equals("Y")|| appConfigValue.equals("y")|| appConfigValue.equals("YES")||appConfigValue.equals("yes")));

	}
	
	public List<TenderableEntityGroup> getGroupListByFileType(Long fileType)
	{
		List<TenderableEntityGroup> groupList=Collections.emptyList();
		if(fileType!=null && fileType != -1){
			TenderFileType tenderType=(TenderFileType)persistenceService.find("from TenderFileType where id=?",fileType);
			TenderableGroupType tType= TenderableGroupType.valueOf(tenderType.getGroupType());
			groupList=(List<TenderableEntityGroup>)persistenceService.findAllBy("from TenderableEntityGroup where tenderableGroupType=?",tType);
		}
		return groupList;
	}

	public Position getPositionForUser(Integer userId)
	{
		return eisService.getPrimaryPositionForUser(userId, new Date());
	}
	
	public void setBidderTypeServiceMap(
			Map<String, BidderTypeService> bidderTypeServiceMap) {
		this.bidderTypeServiceMap = bidderTypeServiceMap;
	}
	
	public Map<String, BidderTypeService> getBidderTypeServiceMap() {
		return bidderTypeServiceMap;
	}

	public BidderTypeService getBidderService(TenderFileType fileType)
	{
		final String KEY=fileType.getBidderType().toLowerCase().concat("Service");
		if(bidderTypeServiceMap.containsKey(KEY))
			return bidderTypeServiceMap.get(KEY);
		else
			throw new EGOVRuntimeException(KEY+" Not found");
	}
	
	public Bidder getBidder(String tenderFileNumber,String bidderCode) {
		Bidder bidder=null;
		TenderFileType tenderFileType=getTenderFileTypeBypassingTenderFileNumber(tenderFileNumber);
		
			if (tenderFileType!=null && tenderFileType.getBidderType()!=null) {
				bidder = getBidderService(tenderFileType).getBidderByCode(bidderCode);
			}
		
	   return bidder;
	}
	
	public TenderFileType getTenderFileTypeBypassingTenderFileNumber(String tenderFileNumber) {
		 Criteria tenderCriteria=persistenceService.getSession().createCriteria(TenderNotice.class,"notice");
			//.createAlias("tenderFileType","tenderFileType");
		// tenderCriteria.setProjection(Projections.property("tenderFileType.bidderType"));
		 tenderCriteria.add(Restrictions.eq("notice.tenderFileRefNumber",tenderFileNumber));
		TenderNotice notice= (TenderNotice) tenderCriteria.uniqueResult();
		if(notice!=null && notice.getTenderFileType()!=null)
			return notice.getTenderFileType();
		else
			return null;
	}
	
	/**
	 * This will save the Owner object
	 * @param owner
	 * @return
	 */
	
	public Owner saveOwner(Owner owner)
	{
		return ownerService.persist(owner);
	}
	
	/**
	 * This will return Owner object
	 */
	
	public Owner getOwnerById(Integer ownerId)
	{
		return ownerService.findById(ownerId, Boolean.TRUE);
	}
	
	 public  String getUsertName(Integer id)
	    {
			
			if(!userMap.containsKey(id))
			{
				User user=eisService.getUserForPosition(id,DateUtils.today());
				if(user!=null)
					userMap.put(id, user.getUserName());
			}
			return userMap.get(id);
		  }
		
		
	
	/**
	 * This method is to check Whether model is modifyable by user or not.
	 * @param model
	 * @return
	 */
	
	public boolean isModifyableByLoginUser(BaseModel model) {
		Integer userId=Integer.valueOf(EGOVThreadLocals.getUserId());
		if(model.getCreatedBy()!=null && userId.equals(model.getCreatedBy().getId()))
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
		
	}
	/*
	 * 
	 * This Api returns logoname. Here the path is appended with city logoname. It refers eg_city_website table, LOGO column.
	 * If the data is missing in this column, then india.png image name will be return back as default value.
	 */
	public String getCityLogoName(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
        ServletContext servletContext=session.getServletContext();
		
		String cityurl=(String)request.getSession().getAttribute("cityurl");
		CityWebsite cityWebsite=null;
		String logoname=servletContext.getRealPath("/")+"images/"+TenderConstants.DEFAULTLOGONAME;
		if(cityurl!=null)
		{
		cityWebsite = new CityWebsiteDAO().getCityWebSiteByURL(cityurl);
		logoname=(cityWebsite==null)?TenderConstants.DEFAULTLOGONAME:cityWebsite.getLogo();
		logoname= servletContext.getRealPath("/")+"images/"+logoname;
		}	
		return logoname;
	}
	
	public EgwStatus getStatusByModuleAndCode(String module,String code)
	{
		return (EgwStatus)persistenceService.find(STATUSQUERY,module,code);
	}
	public String getNameOfHeadOfDepartmentByPassingDeptId(Integer deptId) {
		List<PersonalInformation> personalInfoList = null;
		try{
		
			personalInfoList= eisService.getAllHodEmpByDept(deptId);
		
		}catch(Exception ec){
			logger.error(ec.getMessage());
		}
		
		if(personalInfoList.size()>0){
		    return personalInfoList.get(0).getEmployeeName();
		}
		else {
			return null;
		}	
	}
}
