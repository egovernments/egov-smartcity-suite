/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infstr.config.client;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfig;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MasterSetUpAction extends DispatchAction {
	private GenericHibernateDaoFactory genericHibernateDaoFactory;

	private static final Logger LOG = LoggerFactory.getLogger(MasterSetUpAction.class);

	final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	public ActionForward populateExistingDetails(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		final MasterDataForm masterDataForm = (MasterDataForm) form;
		String target = "";
		List appKeyList = null;
		String viewMode = "";

		try {
			if (req.getParameter("mode") != null) {
				final AppConfigValuesDAO appDao = genericHibernateDaoFactory.getAppConfigValuesDAO();
				appKeyList = appDao.getAppConfigKeys(masterDataForm.getModuleName()[0]);
				req.setAttribute("appDataKey", appKeyList);
				viewMode = req.getParameter("mode");
				if (viewMode.equalsIgnoreCase("view")) {
					target = "listOfKeysView";
				} else {
					target = "listOfKeysCreate";
				}
			}
		} catch (final Exception ex) {
			target = "error";
			LOG.error("Error occurred while settingup Master Setup",ex);
			throw ex;
		}
		return mapping.findForward(target);
	}

	public ActionForward createNewValues(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		final MasterDataForm masterDataForm = (MasterDataForm) form;
		AppConfig appCon = null;
		int length = 0;
		final String[] appKeyName = masterDataForm.getKeyName();
		if (!("0").equals(appKeyName[0])) {
			length = appKeyName.length;
		}
		final AppConfigValuesDAO appConDao = genericHibernateDaoFactory.getAppConfigValuesDAO();
		for (int i = 0; i < length; i++) {
			final List appKeyList = appConDao.getAppConfigKeys(masterDataForm.getModuleName()[0]);
			for (final Iterator it = appKeyList.iterator(); it.hasNext();) {
				final AppConfig apc = (AppConfig) it.next();
				if (appKeyName[i].trim().equals(apc.getKeyName())) {
					if (masterDataForm.getValues() != null) {
						final int lenInner = masterDataForm.getValues().length;
						for (int l = 0; l < lenInner; l++) {
							if (masterDataForm.getCount()[l].trim().equals(apc.getKeyName())) {
								if (("").equals(masterDataForm.getKeyId()[l])) {
									final AppConfigValues appConVal = new AppConfigValues();
									appCon = appConDao.getConfigKeyByName(appKeyName[i].trim(), masterDataForm.getModuleName()[0]);
									appConVal.setKey(appCon);
									if (masterDataForm.getValues() != null && !"".equals(masterDataForm.getValues()[l])) {
										appConVal.setValue(masterDataForm.getValues()[l]);
									}
									if (masterDataForm.getEffectiveFrom() != null && !("".equals(masterDataForm.getEffectiveFrom()[l]))) {
										appConVal.setEffectiveFrom(this.formatter.parse(masterDataForm.getEffectiveFrom()[l]));
									}
									if (!"".equals(masterDataForm.getEffectiveFrom()[l]) && !("".equals(masterDataForm.getValues()[l]))) {
										appConDao.create(appConVal);
									} else {
										// target ="error";
										throw new EGOVException("Mandatory data not filled properly");
									}
								}
							}
						}
					}
				}
			}

		}

		return mapping.findForward("afterSave");
	}

	public ActionForward searchModuleForMastersetup(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		final MasterDataForm masterDataForm = (MasterDataForm) form;
		final String target = "listOfModules";
		final Set<String> moduleSet = new HashSet<String>();
		final AppConfigValuesDAO appConDao = genericHibernateDaoFactory.getAppConfigValuesDAO();
		final List<String> appconfigModuleList = appConDao.getAllAppConfigModule();
		for (final String module : appconfigModuleList) {
			moduleSet.add(module);
		}
		masterDataForm.setModuleSet(moduleSet);
		return mapping.findForward(target);
	}

	public void setGenericHibernateDaoFactory(GenericHibernateDaoFactory genericHibernateDaoFactory) {
		this.genericHibernateDaoFactory = genericHibernateDaoFactory;
	}
}
