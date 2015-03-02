package org.egov.pgr.service;

import org.egov.config.search.Index;
import org.egov.config.search.IndexType;
import org.egov.infra.search.elastic.aop.IndexingAdvice;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.ComplaintTypeBuilder;
import org.egov.pgr.repository.ComplaintTypeRepository;
import org.egov.search.domain.Document;
import org.egov.search.service.IndexService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@Ignore
public class ComplaintTypeServiceTest {

    @Mock
    private ComplaintTypeRepository complaintTypeRepository;
    @Mock
    private IndexService indexService;
    private ComplaintTypeService complaintTypeService;

    @Before
    public void before() {
        initMocks(this);
           
        complaintTypeService = new ComplaintTypeService(complaintTypeRepository);
        AspectJProxyFactory factory = new AspectJProxyFactory(complaintTypeService);
        IndexingAdvice aspect = new IndexingAdvice();
        factory.addAspect(aspect);
        complaintTypeService = factory.getProxy();
    }

    @Test
    public void shouldIndexAfterCreating() {
        ComplaintType complaintType = new ComplaintTypeBuilder().withDefaults().withName("Roads").build();

        complaintTypeService.createComplaintType(complaintType);

        verify(complaintTypeRepository).save(complaintType);
        ArgumentCaptor<Document> argumentCaptor = ArgumentCaptor.forClass(Document.class);
        verify(indexService).index(argumentCaptor.capture());

        Document actualDocument = argumentCaptor.getValue();
        assertEquals(complaintType.getId().toString(), actualDocument.getCorrelationId());
        assertEquals(Index.PGR.toString(), actualDocument.getIndex());
        assertEquals(IndexType.COMPLAINT_TYPE.toString(), actualDocument.getType());
    }

}
