package org.egov.ptis.domain.service.notice;

import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.notice.PtNotice;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;
import org.springframework.beans.factory.annotation.Autowired;

public class NoticeService {
	private static final Logger LOGGER = Logger.getLogger(NoticeService.class);
	PersistenceService<BasicProperty, Long> basicPrpertyService;
	PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();
	@Autowired
	private ModuleDao moduleDao;

	/**
	 * This method populates the <code>PtNotice</code> object along with notice
	 * input stream
	 * 
	 * @param basicProperty
	 *            the <code>BasicProperty</code> object for which the notice is
	 *            generated
	 * @param noticeNo - notice no
	 * @param noticeType - type of notice
	 * @param fileStream - input stream of generated notice.           
	 * 
	 */
	public PtNotice saveNotice(String noticeNo, String noticeType, BasicProperty basicProperty, InputStream fileStream) {
		PtNotice ptNotice = new PtNotice();
		Module module = moduleDao.getModuleByName(PTMODULENAME);
		ptNotice.setModuleId(module.getId());
		ptNotice.setNoticeDate(new Date());
		ptNotice.setNoticeNo(noticeNo);
		ptNotice.setNoticeType(noticeType);
		ptNotice.setUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		ptNotice.setBasicProperty(basicProperty);
		ptNotice.setIsBlob('Y');
		try {
			ptNotice.setNoticeFile(IOUtils.toByteArray(fileStream));
		} catch (IOException e) {
			LOGGER.error("Exception while saving Bill notice.",e);
			throw new EGOVRuntimeException("Exception while saving Bill notice.", e);
		}
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
}
