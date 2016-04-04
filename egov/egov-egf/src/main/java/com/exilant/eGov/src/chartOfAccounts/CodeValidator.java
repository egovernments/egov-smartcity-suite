/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package com.exilant.eGov.src.chartOfAccounts;

import org.springframework.transaction.annotation.Transactional;

import com.exilant.GLEngine.ChartOfAccounts;
import com.exilant.GLEngine.GLAccount;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.dataservice.DataExtractor;

@Transactional(readOnly = true)
public class CodeValidator {
    // private static HashMap glFilterList;
    // public static HashMap glAccountCodes;
    static CodeValidator singletonInstance = new CodeValidator();;
    static
    {// This fix is for Phoenix Migration.
        /*
         * try { cache=(TreeCacheMBean)MBeanProxyExt.create(TreeCacheMBean.class, "jboss.cache:service=TreeCache", server); }
         * catch (MalformedObjectNameException e) { // TODO Auto-generated catch block if(LOGGER.isDebugEnabled())
         * LOGGER.debug("Exp="+e.getMessage()); throw new ApplicationRuntimeException(e.getMessage()); }
         */
    }

    private CodeValidator() {

    }

    public static CodeValidator getInstance() throws TaskFailedException {
        // This fix is for Phoenix Migration.
        /*
         * if(LOGGER.isInfoEnabled())
         * LOGGER.info("CodeValidator getInstance() called for "+EgovThreadLocals.getDomainName()+"and singletonInstance == null"
         * +singletonInstance == null); try { //if(LOGGER.isInfoEnabled())
         * LOGGER.info("cache.get(rootNode/"+EgovThreadLocals.getDomainName
         * ()+"/"+gLFilterCode+"::::::"+cache.get(rootNode+"/"+EgovThreadLocals.getDomainName(),gLFilterCode));
         * if(cache.get(ROOTNODE+"/"+EgovThreadLocals.getDomainName(),GLFILTERCODE) == null) { if(LOGGER.isInfoEnabled())
         * LOGGER.info("calling loadFilterData::for "+EgovThreadLocals.getDomainName()); loadFilterData(); } } catch (Exception e)
         * { // TODO Auto-generated catch block if(LOGGER.isDebugEnabled()) LOGGER.debug("Exp="+e.getMessage()); throw new
         * TaskFailedException(); }
         */

        return singletonInstance;
    }

    private static void loadFilterData() throws TaskFailedException {
        DataExtractor.getExtractor();
    }

    public boolean isValidGLCode(final String pServiceID, final String pGlCode) {
        final boolean valid = false;
        // Iterator it=glFilterList.keySet().iterator();

        /*
         * try { hm = (HashMap)cache.get(ROOTNODE+"/"+EgovThreadLocals.getDomainName(),GLFILTERCODE); } catch (Exception e) { //
         * TODO Auto-generated catch block if(LOGGER.isDebugEnabled()) LOGGER.debug("Exp="+e.getMessage()); throw new
         * ApplicationRuntimeException(e.getMessage()); } //if(LOGGER.isInfoEnabled()) LOGGER.info("glcode:"+pGlCode);
         * //if(LOGGER.isInfoEnabled()) LOGGER.info("looked up str:"+rootNode+"/"+FilterName.get()+gLFilterCode); //
         * if(LOGGER.isInfoEnabled()) LOGGER.info("hashmap is:"+hm); // if(LOGGER.isInfoEnabled())
         * LOGGER.info("hashmap keyset is:"+hm.keySet()); Iterator it=hm.keySet().iterator(); while(it.hasNext()){ FilterCodeList
         * fList=(FilterCodeList)hm.get(it.next()); if(fList.getServiceID().equalsIgnoreCase(pServiceID) &&
         * fList.getGLCode().equalsIgnoreCase(pGlCode)){ valid=true; break; } }
         */
        return valid;
    }

    public boolean isValidCode(final String pGlCode) {
        boolean valid = false;
        // GLAccount fList=(GLAccount)glAccountCodes.get(pGlCode);
        final GLAccount fList = (GLAccount) ChartOfAccounts.getGlAccountCodes().get(pGlCode);
        if (fList != null)
            valid = true;
        return valid;
    }

    public void reloadMappedCodes() throws TaskFailedException {
        // glFilterList.clear();
        // glAccountCodes.clear();
        loadFilterData();
    }
}
