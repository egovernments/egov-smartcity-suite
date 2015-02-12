package org.egov.pgr.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.exceptions.TooManyValuesException;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.BoundaryTypeDAO;
import org.egov.lib.admbndry.HeirarchyType;
import org.egov.lib.admbndry.HeirarchyTypeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonService {
	
	    private static Logger LOG=Logger.getLogger(CommonService.class);
	
	 	@Autowired
		private HeirarchyTypeDAO hierHeirarchyTypeDAO;
	    @Autowired
		private BoundaryTypeDAO boundaryTypeDAO;
	    @Autowired
		private BoundaryDAO boundaryDAO;

	@SuppressWarnings("unchecked")
	public List<BoundaryImpl> getZones()
	{
		List<BoundaryImpl> zones=new ArrayList<BoundaryImpl>();
		try {
			HeirarchyType hierarchyTypeByName = hierHeirarchyTypeDAO.getHierarchyTypeByName("ADMINISTRATION");
			BoundaryType boundaryType = boundaryTypeDAO.getBoundaryType("ZONE", hierarchyTypeByName);
			zones = (List<BoundaryImpl>) boundaryDAO.getAllBoundariesByBndryTypeId(boundaryType.getId());
		} catch (NoSuchObjectException e) {
			LOG.error(e.getMessage());
		} catch (TooManyValuesException e) {
			LOG.error(e.getMessage());
		}
		return zones;
	}

	
	public List<BoundaryImpl> getWards(Boundary zone)
	{

		if(zone==null || zone.getId()==null)
		{
			throw new EGOVRuntimeException("Zone or Zoneid is not passed");
		}
		List<BoundaryImpl> wards=new ArrayList<BoundaryImpl>();
		wards = boundaryDAO.getAllchildBoundaries(zone.getId());
		return wards;
	}
	
	
}
