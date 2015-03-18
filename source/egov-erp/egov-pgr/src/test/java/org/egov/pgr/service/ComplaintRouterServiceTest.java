package org.egov.pgr.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.egov.builder.entities.BoundaryBuilder;
import org.egov.builder.entities.DepartmentBuilder;
import org.egov.eis.entity.PositionBuilder;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.dept.Department;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintBuilder;
import org.egov.pgr.entity.ComplaintRouter;
import org.egov.pgr.entity.ComplaintRouterBuilder;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.ComplaintTypeBuilder;
import org.egov.pgr.repository.ComplaintRouterRepository;
import org.egov.pims.commons.Position;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

public class ComplaintRouterServiceTest {

    @Autowired
    private ComplaintRouterService complaintRouterService;

    @Mock
    private ComplaintRouterRepository complaintRouterRepository;

    private Complaint complaint;

    private ComplaintType complaintType;

    private BoundaryImpl ward;

    private Position wardOfficer;

    private Boundary city;

    private Boundary zone;

    private Position healthInspector;

    private Position zonalOfficer;

    private Position grievanceOfficer;

    @Before
    public void before() {

        initMocks(this);
        setupRoutingMaster();

    }

    private void setupRoutingMaster() {
        complaintRouterService = new ComplaintRouterService(complaintRouterRepository);
        final Department dept = new DepartmentBuilder().withDbDefaults().build();
        complaintType = new ComplaintTypeBuilder().withDepartment(dept).withName("test-ctype").build();
        city = new BoundaryBuilder().withDbDefaults().build();
        zone = new BoundaryBuilder().withDbDefaults().build();
        ward = new BoundaryBuilder().withDbDefaults().build();

        wardOfficer = new PositionBuilder().withName("WardOfficer").build();
        healthInspector = new PositionBuilder().withName("HO").build();
        zonalOfficer = new PositionBuilder().withName("ZonalOfficer").build();
        grievanceOfficer = new PositionBuilder().withName("Grievance Officer").build();

        final ComplaintRouter type_boundary_position = new ComplaintRouterBuilder().withComplaintType(complaintType)
                .withBoundary(ward).withPosition(wardOfficer).build();

        final ComplaintRouter type_position = new ComplaintRouterBuilder().withComplaintType(complaintType)
                .withPosition(healthInspector).build();

        final ComplaintRouter boundary_position = new ComplaintRouterBuilder().withBoundary((BoundaryImpl) zone)
                .withPosition(zonalOfficer).build();

        when(complaintRouterRepository.findByComplaintTypeAndBoundary(complaintType, ward)).thenReturn(
                type_boundary_position);
        when(complaintRouterRepository.findByOnlyComplaintType(complaintType)).thenReturn(type_position);
        when(complaintRouterRepository.findByBoundary((BoundaryImpl) zone)).thenReturn(boundary_position);

    }

    @Test
    public void testGetAssignee_By_Type_Location() {

        complaint = new ComplaintBuilder().withComplaintType(complaintType).withLocation(ward).withDbDefaults().build();
        final Position assignee = complaintRouterService.getAssignee(complaint);
        assertEquals(wardOfficer, assignee);

    }

    @Test
    public void testGetAssignee_By_Type() {
        // this will create a new boundary which is not mapped
        complaint = new ComplaintBuilder().withComplaintType(complaintType).withDbDefaults().build();
        final Position assignee = complaintRouterService.getAssignee(complaint);
        assertEquals(healthInspector, assignee);

    }

    @Test
    public void testGetAssignee_By_Boundary() {
        // this will create a new boundary which is not mapped
        complaintType = new ComplaintTypeBuilder().withDbDefaults().build();

        complaint = new ComplaintBuilder().withComplaintType(complaintType).withLocation((BoundaryImpl) zone).build();
        final Position assignee = complaintRouterService.getAssignee(complaint);
        assertEquals(zonalOfficer, assignee);

    }

    @Test
    public void testGetAssignee_By_Go_without_Go_Insertion() {
        // this will create a new boundary which is not mapped
        complaintType = new ComplaintTypeBuilder().withDbDefaults().build();

        complaint = new ComplaintBuilder().withComplaintType(complaintType).withLocation((BoundaryImpl) city).build();
        final Position assignee = complaintRouterService.getAssignee(complaint);
        assertNull(assignee);

    }

    @Test
    public void testGetAssignee_By_Go_After_Go_Insertion() {
        // this will create a new boundary which is mapped
        complaintType = new ComplaintTypeBuilder().withDbDefaults().build();
        final ComplaintRouter GoPosition = new ComplaintRouterBuilder().withBoundary((BoundaryImpl) city)
                .withPosition(grievanceOfficer).build();
        when(complaintRouterRepository.findGrievanceOfficer()).thenReturn(GoPosition);

        complaint = new ComplaintBuilder().withComplaintType(complaintType).withLocation((BoundaryImpl) city).build();
        final Position assignee = complaintRouterService.getAssignee(complaint);
        assertEquals(grievanceOfficer, assignee);

    }

}