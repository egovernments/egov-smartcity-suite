package org.egov.works.web.actions.report;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.BoundaryTypeDAO;
import org.egov.lib.admbndry.HeirarchyType;
import org.egov.lib.admbndry.HeirarchyTypeDAO;
import org.egov.web.actions.BaseFormAction;
import org.egov.works.services.AbstractEstimateService;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.exceptions.TooManyValuesException;

/**
 * @author vikas
 * 
 */
@ParentPackage("egov")
@SuppressWarnings("serial")
public class GisReportAction extends BaseFormAction {
	private BoundaryDAO boundaryDao = new BoundaryDAO();
	private BoundaryTypeDAO boundaryTypeDao = new BoundaryTypeDAO();
	private HeirarchyTypeDAO heirarchTypeDao = new HeirarchyTypeDAO();
	private List<GisReportBean> GisReportBeans = new ArrayList<GisReportBean>();
	private AbstractEstimateService abstractEstimateService;

	public void prepare() {
		super.prepare();

	}

	public String execute() throws Exception {
		populateZonalBudgetInfo();
		return "view";

	}

	private void populateZonalBudgetInfo() {
		HeirarchyType heirarchy = null;

		try {
			heirarchy = heirarchTypeDao.getHierarchyTypeByName("ADMINISTRATION");
		} catch (NoSuchObjectException noObjExp) {
			throw new EGOVRuntimeException("Error occurred in populateZonalBudgetInfo() for getHierarchyTypeByName(): No heirarchy type exists for ADMINISTRATION",noObjExp);
		} catch (TooManyValuesException exp) {
			throw new EGOVRuntimeException("Error occurred in populateZonalBudgetInfo() for getHierarchyTypeByName() :Many heirarchy type exists for ADMINISTRATION",exp);
		}

		BoundaryType boundaryType = boundaryTypeDao.getBoundaryType("Zone",heirarchy);
		List<Boundary> boundaries = (List<Boundary>) boundaryDao.getAllBoundariesByBndryTypeId(boundaryType.getId());
		GisReportBeans.clear();
		GisReportBean gisBean = null;
		for (Boundary boundary : boundaries) {
			gisBean = new GisReportBean();
			gisBean.setBoundary(boundary);
			// budgetAvailable is hardcoded because API to get available zonal
			// budget is not ready
			gisBean.setBudgetAvailable(500000D);
			gisBean.setBudgetConsumed(abstractEstimateService.getConsumedBudgetForBoundary(boundary));
			if (!(boundary.getLat() == null || boundary.getLng() == null)) {
				GisReportBeans.add(gisBean);
			}
		}
	}

	@Override
	public Object getModel() {
		return null;
	}

	public List<GisReportBean> getGisReportBeans() {
		return GisReportBeans;
	}

	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}
}
