/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package com.exilant.exility.pagemanager;

import com.exilant.exility.common.DataCollection;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * @author raghu.bhandi, Exilant Consulting
 */
public class GridMap {
    private static final Logger LOGGER = Logger.getLogger(GridMap.class);
    public String name;
    public String serverFieldName; // defaults to name if omitted
    public boolean containsNames; // whether the first row of the grid contains name.
    // if field map is not provided, entire grid is assumed to be required
    // if first row contians field names, then the field map is used to map names to cleint
    // columns are sent in the order in which the names are specified
    public FieldMap[] columnMaps; // if first row contains name, it is used, else columns are mapped on that order

    public GridMap() {
        super();
    }

    public String toJavaScript(final DataCollection dc, final String dcName) {
        String[][] grid;
        final String nam = serverFieldName == null ? name : serverFieldName;
        grid = dc.getGrid(nam);
        // ensure that there is some data, and some mapping to do before going furter
        if (grid == null || grid.length == 0 || grid[0].length == 0 || columnMaps == null || columnMaps.length == 0)
            return "";

        final int serverCols = grid[0].length;
        final int clientCols = columnMaps.length;

        // maps client column number to server column
        final int[] indexes = new int[clientCols];

        if (containsNames)
            for (int i = 0; i < clientCols; i++) {
                indexes[i] = 0;
                for (int j = 0; j < serverCols; j++) {
                    final String n = columnMaps[i].serverFieldName == null ? columnMaps[i].name : columnMaps[i].serverFieldName;
                    if (grid[0][j].equalsIgnoreCase(n)) {
                        indexes[i] = j;
                        break;
                    }
                }
            }
        else
            for (int i = 0; i < clientCols; i++)
                try {
                    indexes[i] = Integer.parseInt(columnMaps[i].serverFieldName);
                    if (indexes[i] >= serverCols)
                        indexes[i] = 0;
                } catch (final NumberFormatException e) {
                    indexes[i] = 0;
                    LOGGER.error("Error in retriving index" + e.getMessage());
                }

        /*
         * we have filled indexes[] with index map between client and server let us output it now in the JS dc.grids['gridname'] =
         * [ [ 'firstrow first element', '.....] ...];
         */
        final StringBuffer sbf = new StringBuffer();
        sbf.append(dcName);
        sbf.append(".grids['");
        sbf.append(name);
        sbf.append("'] = [\n"); // ready to dump a row per line
        /*
         * header row with names
         */
        sbf.append("['");
        sbf.append(columnMaps[0].name); // first one output outside the loop
        for (int i = 1; i < clientCols; i++) { // note that the loop starts with 1 but not 0
            sbf.append("','");
            sbf.append(columnMaps[i].name);
        }
        sbf.append("']\n");

        /*
         * Now, data rows...
         */
        final int start = containsNames ? 1 : 0;
        for (int i = start; i < grid.length; i++) {
            sbf.append(",['");
            sbf.append(grid[i][indexes[0]]);
            for (int j = 1; j < clientCols; j++) {
                sbf.append("','");
                sbf.append(grid[i][indexes[j]]);
            }
            sbf.append("']\n");
        }
        // close the array
        sbf.append("];\n");
        return sbf.toString();
    }

    /*
     * Adds this grid to the DataCollection based on the fields available in Request. This is a data extractor that extracts data
     * from a specific table in a form to DC
     */
    public void addGrid(final DataCollection dc, final HttpServletRequest req) {
        final String nam = serverFieldName == null ? name : serverFieldName;
        if (columnMaps == null || columnMaps.length == 0) {
            dc.addGrid(nam, new String[0][0]);
            return;
        }

        FieldMap fm = columnMaps[0];
        final int ncols = columnMaps.length;
        String[] col = req.getParameterValues(fm.name);
        if (col == null) {
            dc.addGrid(nam, new String[0][0]);
            return;
        }
        final int nrows = col.length;

        final String[][] grid = new String[nrows][ncols];
        // populate the grid
        for (int i = 0; i < ncols; i++) { // note that we are going by column first...
            fm = columnMaps[i];
            col = req.getParameterValues(fm.name);
            if (col == null || col.length != nrows) { // some error in form design
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Error in form Design " + name
                            + " is not defined or has different number of values than the rest in the form");
            } else
                for (int k = 0; k < nrows; k++)
                    grid[k][i] = col[k];
        }
        dc.addGrid(nam, grid);

    }
    /*
     * public static void main(String[] args) { DataCollection dc = new DataCollection(); String[][] arr1 = {{"name1", "name2",
     * "name3", "name4"}, {"val11", "val12", "val13", "val14"}, {"val21", "val22", "val23", "val24"}, {"val31", "val32", "val33",
     * "val34"}, {"val41", "val42", "val43", "val44"}, }; String[][] arr2 = { {"value11", "value12", "value13", "value14"},
     * {"value21", "value22", "value23", "value24"}, {"value31", "value32", "value33", "value34"}, }; dc.addGrid("firstGrid",
     * arr1); dc.addGrid("secondGrid", arr2); GridMap gm = new GridMap(); gm.containsNames = false; gm.name = "clientName";
     * gm.serverFieldName = "secondGrid"; gm.columnMaps = new FieldMap[3]; FieldMap fm; fm = new FieldMap(); fm.name =
     * "clientName1"; fm.serverFieldName = "0"; gm.columnMaps[0] = fm; fm = new FieldMap(); fm.name = "clientName2";
     * fm.serverFieldName = "3"; gm.columnMaps[1] = fm; fm = new FieldMap(); fm.name = "clientName3"; fm.serverFieldName = "1";
     * gm.columnMaps[2] = fm; if(LOGGER.isDebugEnabled()) LOGGER.debug(gm.toJavaScript(dc, "ddcc")); }
     */
}
