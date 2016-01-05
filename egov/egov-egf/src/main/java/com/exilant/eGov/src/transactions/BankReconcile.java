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
/*
 * Created on Mar 24, 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.transactions;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.exilant.eGov.src.domain.BankReconciliation;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;

public class BankReconcile extends AbstractTask {
    private static final Logger LOGGER = Logger.getLogger(BankReconcile.class);
    private TaskFailedException taskExc;

    public BankReconcile() {
    }

    @Override
    public void execute(final String taskName,
            final String gridName,
            final DataCollection dc,
            final Connection conn,
            final boolean erroOrNoData,
            final boolean gridHasColumnHeading, final String prefix) throws TaskFailedException {
        taskExc = new TaskFailedException();
        final String reconcileGrid[][] = dc.getGrid("gridBankReconciliation");
        final BankReconciliation br = new BankReconciliation();
        for (int i = 0; i < reconcileGrid.length; i++) {
            if (reconcileGrid[i][1].equalsIgnoreCase("") || reconcileGrid[i][1] == null)
                continue;
            br.setId(reconcileGrid[i][0]);
            br.setIsReconciled("1");
            try
            {
                // if(LOGGER.isDebugEnabled()) LOGGER.debug("reconcileGrid[i][1]"+reconcileGrid[i][1]);
                final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                final String vdt = reconcileGrid[i][1];
                reconcileGrid[i][1] = formatter.format(sdf.parse(vdt));
                br.setReconciliationDate(reconcileGrid[i][1]);
                // br.setReconciliationDate(ReconciliationDate[i]);
            } catch (final Exception e) {
                LOGGER.error("Exp in execute:" + e.getMessage());
                throw taskExc;
            }
            try {
                br.update();
            } catch (final SQLException sqlEx) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("SQLEx BankReconciliation : " + sqlEx.toString());
                dc.addMessage("exilRPError", "bankReconciliation Updation Failed");
                throw taskExc;
            }
        }
    }
}
