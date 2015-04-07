package org.egov.pgr.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.exceptions.TooManyValuesException;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.services.EISServeable;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.admbndry.BoundaryTypeDAO;
import org.egov.lib.admbndry.HeirarchyTypeDAO;
import org.egov.pims.commons.DesignationMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

    private static Logger LOG = Logger.getLogger(CommonService.class);

    @Autowired
    private HeirarchyTypeDAO hierHeirarchyTypeDAO;
    @Autowired
    private BoundaryTypeDAO boundaryTypeDAO;
    @Autowired
    private BoundaryDAO boundaryDAO;
    @Autowired
    private EISServeable eisService;

    @SuppressWarnings("unchecked")
    public List<BoundaryImpl> getZones() {
        List<BoundaryImpl> zones = new ArrayList<BoundaryImpl>();
        try {
            HierarchyType hierarchyTypeByName = hierHeirarchyTypeDAO.getHierarchyTypeByName("ADMINISTRATION");
            BoundaryType boundaryType = boundaryTypeDAO.getBoundaryType("ZONE", hierarchyTypeByName);
            zones = (List<BoundaryImpl>) boundaryDAO.getAllBoundariesByBndryTypeId(boundaryType.getId());
        } catch (NoSuchObjectException e) {
            LOG.error(e.getMessage());
        } catch (TooManyValuesException e) {
            LOG.error(e.getMessage());
        }
        return zones;
    }

    public List<BoundaryImpl> getWards(Integer zoneId) {

        if (zoneId == null || zoneId <= 0) {
            throw new EGOVRuntimeException("Zone or Zoneid is not passed");
        }
        List<BoundaryImpl> wards = new ArrayList<BoundaryImpl>();
        try {
            wards = boundaryDAO.getChildBoundaries(zoneId);
        } catch (Exception e) {
            LOG.error(e.getMessage());

        }
        return wards;
    }

    public List<DesignationMaster> getDesignations(Integer departmentId) {
        return eisService.getAllDesignationByDept(departmentId, new Date());

    }

    public List<User> getPosistions(Integer departmentId, Integer designationId) {
        return eisService.getUsersByDeptAndDesig(departmentId, designationId, new Date());
    }

}
