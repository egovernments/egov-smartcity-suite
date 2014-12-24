package org.egov.works.web.actions.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.utils.WorksConstants;
import org.hibernate.Query;

/**
 * @author vikas
 * 
 */

@ParentPackage("egov")
@SuppressWarnings("serial")
public class AjaxGisReportAction extends BaseFormAction {
	private List<Map<String, String>> deptWiseEstimateList = new ArrayList<Map<String, String>>();
	private BoundaryDAO boundaryDao = new BoundaryDAO();
	private Integer boundaryId;
	private CommonsService commonsService;

	@Override
	public Object getModel() {
		return null;
	}

	public String getDeptWiseEstimateCounts() {
		List<Boundary> boundaries = null;
		try {
			boundaries = boundaryDao.getChildBoundaries(boundaryId.toString());
			boundaries.add(boundaryDao.getBoundary(boundaryId));
		} catch (Exception exp) {
			throw new EGOVRuntimeException("Error occurred in populateDeptWiseEstimateList() for getChildBoundaries()",exp);
		}
		populateEstimateCountsForBoundaries(boundaries);
		return "deptWiseEstimateList";
	}

	private void populateEstimateCountsForBoundaries(List<Boundary> boundaries) {
		List<Integer> boundaryIds = new ArrayList<Integer>();
		deptWiseEstimateList.clear();
		for (Boundary boundary : boundaries) {
			boundaryIds.add(boundary.getId());
		}

		Query query = HibernateUtil.getCurrentSession().createQuery(
								"select ae.executingDepartment.deptName,count(ae.id),sum(ae.workValue) from AbstractEstimate ae "
								+ "where ae.ward.id in (:boundaries) and "
								+ "ae.egwStatus.code=:code and "
								+ "ae.estimateNumber in (select referenceNumber from org.egov.model.budget.BudgetUsage where financialYearId=:finyearid) "
								+ "group by ae.executingDepartment.deptName");

		query.setParameterList("boundaries", boundaryIds);
		query.setParameter("code", WorksConstants.BUDGET_CONSUMPTION_STATUS);
		query.setParameter("finyearid", Integer.parseInt(commonsService.getCurrYearFiscalId()));

		List<Object> results = query.list();
		if (results.size() > 0) {
			Iterator iter = results.iterator();
			while (iter.hasNext()) {
				Object[] row = (Object[]) iter.next();
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("department", (String) row[0]);
				data.put("NoOfEstimates", ((Long) row[1]).toString());
				data.put("amount", ((Double) row[2]).toString());
				deptWiseEstimateList.add(data);
			}
		}
	}

	public List<Map<String, String>> getDeptWiseEstimateList() {
		return deptWiseEstimateList;
	}

	public void setBoundaryId(Integer boundaryId) {
		this.boundaryId = boundaryId;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

}
