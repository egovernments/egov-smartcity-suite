package org.egov.ptis.actions.citizen.search;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.CITIZENUSER;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.utils.StringUtils;
import org.egov.lib.citizen.model.Owner;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.dao.UserDAO;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;

import com.opensymphony.xwork2.validator.annotations.Validations;

@SuppressWarnings("serial")
@ParentPackage("egov")
@Validations
public class SearchAction extends BaseFormAction implements ServletRequestAware {
	private final Logger LOGGER = Logger.getLogger(getClass());

	private String indexNum;
	private String mode;
	private List<Map<String, String>> searchResultList;
	private String searchUri;
	private String searchCreteria;
	private String searchValue;
	private HttpSession session = null;
	private HttpServletRequest request;
	private Integer userId;
	List<Map<String, String>> searchList = new ArrayList<Map<String, String>>();
	String target = "failure";

	@Override
	public Object getModel() {
		return null;
	}

	public String searchForm() {
		return "new";
	}

	@SuppressWarnings("unchecked")
	public void prepare() {
		LOGGER.debug("Entered into prepare method");
		session = request.getSession();
		UserDAO userDao = new UserDAO();
		User user = userDao.getUserByUserName(CITIZENUSER);
		userId = user.getId();
		EGOVThreadLocals.setUserId(userId.toString());
		session.setAttribute("com.egov.user.LoginUserName", user.getUserName());
		LOGGER.debug("Exit from prepare method");
	}

	@ValidationErrorPage(value = "new")
	public String srchByIndex() {
		LOGGER.info("Entered into srchByIndex  method");
		LOGGER.info("Index Number : " + indexNum);
		try {
			BasicPropertyDAO basicPropertyDAO = PropertyDAOFactory.getDAOFactory().getBasicPropertyDAO();
			BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(indexNum);
			LOGGER.debug("srchByIndex : BasicProperty : " + basicProperty);
			if (basicProperty != null) {
				setSearchResultList(getSearchResults(basicProperty.getUpicNo()));
			}
			if (indexNum != null && !indexNum.equals("")) {
				setSearchValue("Index Number : " + indexNum);
			} else if (indexNum != null && !indexNum.equals("")) {
				setSearchValue("Index Number : " + indexNum);
			}
			setSearchCreteria("Search By Index number");
			target = "result";
		} catch (Exception e) {
			LOGGER.error("Exception in Search Property By Index ", e);
			throw new EGOVRuntimeException("Exception : " + e);
		}
		LOGGER.debug("Exit from srchByIndex method ");
		return target;
	}

	@Override
	public void validate() {
		LOGGER.info("Entered into validate method");
		if (StringUtils.equals(mode, "index")) {
			if ((StringUtils.isEmpty(indexNum) || StringUtils.isBlank(indexNum))) {
				addActionError(getText("mandatory.indexNumber"));
			}
		}
		LOGGER.debug("Exit from validate method");
	}

	private List<Map<String, String>> getSearchResults(String indexNumber) {
		LOGGER.debug("Entered into getSearchResults method");
		LOGGER.debug("Index Number : " + indexNumber);
		PTISCacheManagerInteface ptisCachMgr = new PTISCacheManager();
		BasicPropertyDAO basicPropertyDAO = PropertyDAOFactory.getDAOFactory().getBasicPropertyDAO();
		PtDemandDao ptDemandDao = PropertyDAOFactory.getDAOFactory().getPtDemandDao();

		if (indexNumber != null || StringUtils.isNotEmpty(indexNumber)) {

			BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(indexNumber);
			LOGGER.debug("BasicProperty : " + basicProperty);
			if (basicProperty != null) {
				Property property = basicProperty.getProperty();
				LOGGER.debug("Property : " + property);
				Set<Owner> ownerSet = property.getPropertyOwnerSet();

				Map<String, String> searchResultMap = new HashMap<String, String>();
				searchResultMap.put("indexNum", indexNumber);
				searchResultMap.put("ownerName", ptisCachMgr.buildOwnerFullName(ownerSet));
				searchResultMap.put("parcelId", basicProperty.getGisReferenceNo());
				searchResultMap.put("address", ptisCachMgr.buildAddressByImplemetation(basicProperty.getAddress()));
				Map<String, BigDecimal> demandCollMap = ptDemandDao.getDemandCollMap(property);
				searchResultMap.put("currDemand", demandCollMap.get(CURR_DMD_STR).toString());
				searchResultMap.put("arrDemandDue", (demandCollMap.get(ARR_DMD_STR).subtract(demandCollMap
						.get(ARR_COLL_STR))).toString());
				searchResultMap.put("currDemandDue", (demandCollMap.get(CURR_DMD_STR).subtract(demandCollMap
						.get(CURR_COLL_STR))).toString());
				searchList.add(searchResultMap);
			}
		}
		LOGGER.debug("Search list : " + (searchList != null ? searchList : ZERO));
		LOGGER.debug("Exit from getSearchResults method");
		return searchList;
	}

	public String getIndexNum() {
		return indexNum;
	}

	public void setIndexNum(String indexNum) {
		this.indexNum = indexNum;
	}

	public List<Map<String, String>> getSearchResultList() {
		return searchResultList;
	}

	public void setSearchResultList(List<Map<String, String>> searchResultList) {
		this.searchResultList = searchResultList;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getSearchUri() {
		return searchUri;
	}

	public void setSearchUri(String searchUri) {
		this.searchUri = searchUri;
	}

	public String getSearchCreteria() {
		return searchCreteria;
	}

	public void setSearchCreteria(String searchCreteria) {
		this.searchCreteria = searchCreteria;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
    @Override
    @SkipValidation
    public void setServletRequest(HttpServletRequest arg0) {
        this.request = arg0;
    }

}
