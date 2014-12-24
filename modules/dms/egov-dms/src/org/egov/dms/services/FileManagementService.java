/*
 * @(#)FileManagementService.java 3.0, 16 Jul, 2013 11:33:26 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.dms.services;

import java.util.Date;
import java.util.HashMap;

import org.egov.dms.models.ExternalUser;
import org.egov.dms.models.FileCategory;
import org.egov.dms.models.FilePriority;
import org.egov.dms.models.FileSource;
import org.egov.dms.models.Notification;
import org.egov.dms.models.NotificationFile;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.SequenceNumberGenerator;
import org.egov.pims.commons.Position;

/**
 * The Class FileManagementService.
 */
public class FileManagementService {
	
	private transient AppConfigValuesDAO appConfValDao;
	private transient ScriptService scriptExecuter;
	private transient SequenceNumberGenerator sequenceNumberGenerator;
	private transient EISServeable eisService;
	private PersistenceService persistenceService;	
	private transient PersistenceService<NotificationFile, Long> notificationFilePersistenceService;
	private transient PersistenceService<Notification, Long> notificationPersistenceService;
	/**
	 * Sets the AppConfigValuesDAO.
	 * @param appConfValDao the new app config values dao
	 */
	public void setAppConfigValuesDAO(final AppConfigValuesDAO appConfValDao) {
		this.appConfValDao = appConfValDao;
	}
	
	/**
	 * Sets the ScriptService.
	 * @param scriptExecuter the new ScriptService
	 */
	public void setScriptExecuter(final ScriptService scriptExecuter) {
		this.scriptExecuter = scriptExecuter;
	}
	
	/**
	 * Sets the SequenceGenerator.
	 * @param sequenceGenerator the new SequenceGenerator
	 */
	public void setSequenceNumberGenerator(final SequenceNumberGenerator sequenceNumberGenerator) {
		this.sequenceNumberGenerator = sequenceNumberGenerator;
	}
	
	/**
	 * Sets the Employee Information Service
	 * @param eisService;
	 **/
	public void setEisService(final EISServeable eisService) {
		this.eisService = eisService;
	}

	/**
	 * Sets the Persistence Service
	 * @param persistenceService;
	 **/
	public void setPersistenceService(final PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public void setNotificationFilePersistenceService(
			PersistenceService<NotificationFile, Long> notificationFilePersistenceService) {
		this.notificationFilePersistenceService = notificationFilePersistenceService;
	}

	public void setNotificationPersistenceService(
			PersistenceService<Notification, Long> notificationPersistenceService) {
		this.notificationPersistenceService = notificationPersistenceService;
	}

	/**
	 * Checks if is auto generate file number.
	 * @return true, if is auto generate doc number
	 */
	public boolean isAutoGenerateFileNumber() {
		// Getting value to check Auto Document Number Generation is required or not
		final AppConfigValues appConfigVal = this.appConfValDao.getAppConfigValueByDate(DMSConstants.MODULE_NAME, DMSConstants.AUTO_NUM_CONF_NAME, new Date());
		return ((appConfigVal != null) && "Y".equalsIgnoreCase(appConfigVal.getValue()));
	}
	
	/**
	 * Generate the File number.
	 * @return the string
	 */
	public String generateFileNumber() {
		return this.scriptExecuter.executeScript(DMSConstants.AUTO_NUM_SCRIPT_NAME, ScriptService.createContext("sequenceGenerator", this.sequenceNumberGenerator)).toString();
	}
	
	/**
	 * For custom File Notification.
	 * @param fileProperties must contain valid values for the following keys<br/>
	 * <ul>
	 * 	<li>fileCategory<li>
	 * 	<li>filePriority<li>
	 * 	<li>fileHeading<li>
	 * 	<li>fileSummary<li>
	 * 	<li>fileSource<li>
	 *  <li>senderAddress<li>
	 * 	<li>senderName<li>
	 * 	<li>senderPhone<li>
	 *  <li>senderEmail<li>
	 * </ul>
	 * @param userIds, the list of userId's for whom this Notification should send to.  
	 **/
	public String generateFileNotification (final HashMap<String, String> fileProperties,final String...userIds) {
		final NotificationFile notificationFile = new NotificationFile(); 
		final Date currentDate = new Date();
		notificationFile.setFileNumber(this.generateFileNumber());
		notificationFile.setFileReceivedOrSentDate(currentDate);
		notificationFile.setFileCategory((FileCategory)this.persistenceService.find("from org.egov.dms.models.FileCategory where parent is null and name =?",fileProperties.get("fileCategory")));
		notificationFile.setFilePriority((FilePriority)this.persistenceService.find("from org.egov.dms.models.FilePriority where name =?",fileProperties.get("filePriority")));
		notificationFile.setFileHeading(fileProperties.get("fileHeading"));
		notificationFile.setFileDate(currentDate);
		notificationFile.setFileSummary(fileProperties.get("fileSummary"));
		notificationFile.setFileType("NOTIFICATION");
		final ExternalUser users = new ExternalUser();
		users.setUserName(fileProperties.get("senderName"));
		users.setUserAddress(fileProperties.get("senderAddress"));
		users.setUserEmailId(fileProperties.get("senderEmail"));
		users.setUserEmailId(fileProperties.get("senderPhone"));
		users.setUserSource((FileSource)this.persistenceService.find("from org.egov.dms.models.FileSource where name = ?",fileProperties.get("fileSource")));
		notificationFile.setSender(users);
		this.notificationFilePersistenceService.persist(notificationFile);
		this.notificationFilePersistenceService.getSession().flush();
		for (String userId : userIds) {
			final Notification notification = new Notification();
			final Position position = this.eisService.getPrimaryPositionForUser(Integer.valueOf(userId), currentDate);
			notification.setPosition(position);
			notification.setFile(notificationFile);
			this.notificationPersistenceService.persist(notification);
		}
		return notificationFile.getFileNumber();
	}
}
