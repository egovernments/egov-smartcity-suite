package org.egov.ptis.actions.reports;

import static java.math.BigDecimal.ZERO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.dao.property.PropertyTypeMasterDAO;
import org.egov.ptis.domain.dao.property.PropertyUsageDAO;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.web.actions.BaseFormAction;

/**
 * This Action class is used to generate the a report called Zone wise Property
 * Usage Demand
 * 
 * @author Sathish Reddy
 * @version 2.00
 */

public class BoundaryWisePropUsgeDemandAction extends BaseFormAction {

	// private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(BoundaryWisePropUsgeDemandAction.class);
	private final PropertyUsageDAO propUsgeDao = PropertyDAOFactory.getDAOFactory().getPropertyUsageDAO();
	List bndryPropUseList = new ArrayList();
	private Map<Integer, TreeMap<Integer, BoundryWisePropUsgeBean>> bndryMap;
	private Map<Integer, Map<String, BoundryWisePropUsgeBean>> zoneMap;
	private transient BoundaryWisePropUsgeDelegate bndryDel;
	private Map parameters;
	private String bndryParamStr = null;
	private String wardParam = null;
	private String isPrint = null;

	@SuppressWarnings("unchecked")
	public String execute() throws EGOVException {
		LOGGER.debug("Entered into execute method");
		String target = "failure";
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			PropertyTypeMasterDAO propTypeMstrDao = PropertyDAOFactory.getDAOFactory().getPropertyTypeMasterDAO();
			bndryParamStr = getParameterValue("bndryParam");
			isPrint = getParameterValue("isPrint");
			LOGGER.debug("Boundary Param String : " + bndryParamStr + ", " + "Is Print ? : " + isPrint);
			bndryDel = new BoundaryWisePropUsgeDelegate();
			List<PropertyTypeMaster> propMstrList = propTypeMstrDao.findAll();
			// If bndryParamStr is null then the list we get is zoneList
			// Any value i.e zone number then the list we get is ward list
			if (bndryParamStr == null) {
				List zoneList = bndryDel.getZoneList();
				bndryMap = (Map<Integer, TreeMap<Integer, BoundryWisePropUsgeBean>>) bndryDel.getBoundaryWiseList(
						zoneList, null, propMstrList);
				if(bndryMap != null){
					zoneMap = (Map<Integer, Map<String, BoundryWisePropUsgeBean>>) bndryDel
						.getZoneAndPropertyTypeWiseList(bndryMap);
				}else{
					LOGGER.debug("Error in getting boundary wise list");
				}

			} else {
				List wardList = bndryDel.getWardList(Integer.parseInt(bndryParamStr));
				bndryMap = (Map<Integer, TreeMap<Integer, BoundryWisePropUsgeBean>>) bndryDel.getBoundaryWiseList(
						wardList, Integer.parseInt(bndryParamStr),propMstrList);
				if(bndryMap != null){
					zoneMap = (Map<Integer, Map<String, BoundryWisePropUsgeBean>>) bndryDel
						.getZoneAndPropertyTypeWiseList(bndryMap);
				}else{
					LOGGER.debug("Error in getting boundary wise list");
				}
				request.setAttribute("bndryParamStr", bndryParamStr);
			}
			LOGGER.debug("Boundary Map size : " + (bndryMap != null ? bndryMap.size() : ZERO) + ", " +
					"Zone Map size : " + (zoneMap != null ? zoneMap.size() : ZERO));
			if (isPrint != null && isPrint.equals("TRUE")) {
				target = "printResult";
			} else {
				target = INDEX;
			}
			LOGGER.info("zoneMap========="+zoneMap);
			request.setAttribute("zoneMap", zoneMap);
			request.setAttribute("bndryPropUsgeDelgte", bndryDel);

		} catch (Exception e) {
			target = "failure";
			LOGGER.error("--error in BoundaryWisePropUsgeDemand-in execute() method--" + e.getMessage());
			throw new EGOVRuntimeException("Error in ZoneWiseDemandAction---------------", e);
		}
		LOGGER.debug("Exit from execute method");
		return target;
	}

	public Map<Integer, TreeMap<Integer, BoundryWisePropUsgeBean>> getBndryMap() {
		return bndryMap;
	}

	public void setBndryMap(Map<Integer, TreeMap<Integer, BoundryWisePropUsgeBean>> bndryMap) {
		this.bndryMap = bndryMap;
	}

	public List getBndryPropUseList() {
		return bndryPropUseList;
	}

	public void setBndryPropUseList(List bndryPropUseList) {
		this.bndryPropUseList = bndryPropUseList;
	}

	public Map<Integer, Map<String, BoundryWisePropUsgeBean>> getZoneMap() {
		return zoneMap;
	}

	public void setZoneMap(Map<Integer, Map<String, BoundryWisePropUsgeBean>> zoneMap) {
		this.zoneMap = zoneMap;
	}

	public PropertyUsageDAO getPropUsgeDao() {
		return propUsgeDao;
	}

	public String getParameterValue(String param) {
		Object varr = getParameters().get(param);
		if (varr == null)
			return null;
		return ((String[]) varr)[0];
	}

	public Map getParameters() {
		return parameters;
	}

	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}

	public BoundaryWisePropUsgeDelegate getBndryPropUsgeDelgte() {
		return bndryDel;
	}

	public void setBndryPropUsgeDelgte(BoundaryWisePropUsgeDelegate bndryPropUsgeDelgte) {
		this.bndryDel = bndryPropUsgeDelgte;
	}

	public String getBndryParamStr() {
		return bndryParamStr;
	}

	public void setBndryParamStr(String bndryParamStr) {
		this.bndryParamStr = bndryParamStr;
	}

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getWardParam() {
		return wardParam;
	}

	public void setWardParam(String wardParam) {
		this.wardParam = wardParam;
	}

	public String getIsPrint() {
		return isPrint;
	}

	public void setIsPrint(String isPrint) {
		this.isPrint = isPrint;
	}
}
