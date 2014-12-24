package org.egov.ptis.domain.service.notice;

import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;

import java.io.InputStream;
import java.util.Date;

import javax.jcr.RepositoryException;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.docmgmt.AssociatedFile;
import org.egov.infstr.docmgmt.DocumentManagerService;
import org.egov.infstr.docmgmt.DocumentObject;
import org.egov.infstr.docmgmt.documents.Notice;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.notice.PtNotice;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;

public class NoticeService {
	private static final String MOD_NAME = "PT";
	PersistenceService<BasicProperty, Long> basicPrpertyService;
	private DocumentManagerService<DocumentObject> documentManagerService;
	PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();

	/**
	 * This method populates the <code>Notice</code> object and saves it into
	 * the Document Management system(DMS)
	 * 
	 * @param property
	 *            the <code>Property</code> object for which the notice is
	 *            generated
	 * 
	 */
	public PtNotice saveNoticeToDMS(Integer userId, InputStream fileStream, String noticeNo, String noticeType,
			BasicProperty basicProperty) {
		// saving notice to PTIS System
		PtNotice ptNotice = saveNotice(noticeNo, noticeType, basicProperty);

		Notice notice = new Notice();
		notice.setAddress(ptisCacheMgr.buildAddressByImplemetation(basicProperty.getAddress()));
		notice.setAddressedTo(ptisCacheMgr.buildOwnerFullName(basicProperty.getProperty().getPropertyOwnerSet()));
		notice.setDocumentNumber(noticeNo);
		notice.setDomainName(EGOVThreadLocals.getDomainName());
		notice.setModuleName(MOD_NAME);
		notice.setNoticeType(noticeType);
		notice.setNoticeDate(new Date());
		notice.setCreatedBy(userId);
		notice.setModifiedBy(userId);
		notice.setCreatedDate(new Date());
		notice.setModifiedDate(new Date());

		AssociatedFile noticeReport = new AssociatedFile();
		noticeReport.setFileInputStream(fileStream);
		noticeReport.setFileName(noticeNo + ".pdf");
		noticeReport.setMimeType("application/pdf");
		noticeReport.setRemarks("Uploaded " + noticeType + " with no " + noticeNo + " to DMS");
		noticeReport.setCreatedBy(userId);
		noticeReport.setModifiedBy(userId);
		noticeReport.setCreatedDate(new Date());
		noticeReport.setModifiedDate(new Date());
		notice.getAssociatedFiles().add(noticeReport);
		try {
			documentManagerService.addDocumentObject(notice);
		} catch (IllegalAccessException e) {
			throw new EGOVRuntimeException("Error occurred while saving Notice in Document Manager", e);
		} catch (RepositoryException e) {
			throw new EGOVRuntimeException("Error occurred while saving Notice in Document Manager", e);
		}
		return ptNotice;
	}

	/**
	 * This method persists the notice details into PTIS System.
	 * 
	 * @param property
	 * @return
	 */
	public PtNotice saveNotice(String noticeNo, String noticeType, BasicProperty basicProperty) {
		PtNotice ptNotice = new PtNotice();
		Module module = GenericDaoFactory.getDAOFactory().getModuleDao().getModuleByName(PTMODULENAME);
		ptNotice.setModuleId(module.getId());
		ptNotice.setNoticeDate(new Date());
		ptNotice.setNoticeNo(noticeNo);
		ptNotice.setNoticeType(noticeType);
		ptNotice.setUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		ptNotice.setBasicProperty(basicProperty);

		basicProperty.addNotice(ptNotice);
		basicPrpertyService.update(basicProperty);
		return ptNotice;
	}

	public PersistenceService<BasicProperty, Long> getBasicPrpertyService() {
		return basicPrpertyService;
	}

	public void setBasicPrpertyService(PersistenceService<BasicProperty, Long> basicPrpertyService) {
		this.basicPrpertyService = basicPrpertyService;
	}

	public void setDocumentManagerService(DocumentManagerService<DocumentObject> documentManagerService) {
		this.documentManagerService = documentManagerService;
	}

}
