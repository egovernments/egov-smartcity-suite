package org.egov.pgr.service;

import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.ComplaintTypeBuilder;
import org.egov.pgr.repository.ComplaintTypeRepository;
import org.egov.search.Index;
import org.egov.search.ResourceType;
import org.egov.search.domain.Document;
import org.egov.search.service.IndexService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class ComplaintTypeServiceTest {

    @Mock
    private ComplaintTypeRepository complaintTypeRepository;
    @Mock
    private IndexService indexService;
    private ComplaintTypeService complaintTypeService;

    @Before
    public void before() {
        initMocks(this);

        complaintTypeService = new ComplaintTypeService(complaintTypeRepository, indexService);
    }

    @Test
    public void shouldIndexAfterCreating() {
        ComplaintType complaintType = new ComplaintTypeBuilder().withDefaults().withName("Roads").build();

        complaintTypeService.createComplaintType(complaintType);

        verify(complaintTypeRepository).create(complaintType);
        ArgumentCaptor<Document> argumentCaptor = ArgumentCaptor.forClass(Document.class);
        verify(indexService).index(eq(Index.PGR), eq(ResourceType.COMPLAINT_TYPE), argumentCaptor.capture());

        Document actualDocument = argumentCaptor.getValue();
        assertEquals(complaintType.getId().toString(), actualDocument.getCorrelationId());
    }

}