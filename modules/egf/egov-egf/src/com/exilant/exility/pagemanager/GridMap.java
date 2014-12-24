package com.exilant.exility.pagemanager;

import com.exilant.exility.common.DataCollection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * @author raghu.bhandi, Exilant Consulting
 */
public class GridMap {
	private static final Logger LOGGER = Logger.getLogger(GridMap.class);
	public String name;
	public String serverFieldName; //defaults to name if omitted
	public boolean containsNames; //whether the first row of the grid contains name.
	//if field map is not provided, entire grid is assumed to be required
	//if first row contians field names, then the field map is used to map names to cleint
	//columns are sent in the order in which the names are specified
	public FieldMap[] columnMaps; //if first row contains name, it is used, else columns are mapped on that order
	
	public GridMap() {
		super();
	}

	public String toJavaScript(DataCollection dc, String dcName){
		String[][] grid; 
		String nam = (this.serverFieldName == null) ? this.name : this.serverFieldName;
		grid = dc.getGrid(nam);
		//ensure  that there is some data, and some mapping to do before going furter
		if (grid == null || grid.length == 0 || grid[0].length == 0 || columnMaps == null || columnMaps.length == 0) return "";
		
		int serverCols = grid[0].length;
		int clientCols = columnMaps.length;
		
		//maps client column number to server column
		int[] indexes = new int[clientCols];

		if (this.containsNames){
			for (int i=0; i<clientCols; i++){
				indexes[i] = 0;
				for (int j=0; j<serverCols; j++){
					String n = (columnMaps[i].serverFieldName == null)? columnMaps[i].name : columnMaps[i].serverFieldName;
			   		if (grid[0][j].equalsIgnoreCase(n)) {
			   			indexes[i] = j;
			   			break;
			   		}
				}
			}
		}else{
			for (int i=0; i<clientCols; i++){
				try{
					indexes[i] = Integer.parseInt(this.columnMaps[i].serverFieldName);
					if (indexes[i] >= serverCols)indexes[i] = 0;
				}catch(NumberFormatException e){
					indexes[i] = 0;
				}
			}
		}

		/*
		 * we have filled indexes[] with index map between client and server
		 * let us output it now in the JS
		 * dc.grids['gridname'] = [ [ 'firstrow first element', '.....] ...];
		 */
		StringBuffer sbf = new StringBuffer();
		sbf.append(dcName);
		sbf.append(".grids['");
		sbf.append(this.name);
		sbf.append("'] = [\n"); //ready to dump a row per line
		/*
		 * header row with names
		 */
		sbf.append("['");
		sbf.append(this.columnMaps[0].name); //first one output outside the loop
		for (int i=1; i<clientCols; i++){ //note that the loop starts with 1 but not 0
			sbf.append("','");		 	
			sbf.append(this.columnMaps[i].name);
		}
		sbf.append("']\n");
		
		/*
		 * Now, data rows...
		 */		 	
		int start = (this.containsNames)? 1 :0;
		for(int i=start; i<grid.length; i++){
			sbf.append(",['");
			sbf.append(grid[i][indexes[0]]);
			for (int j=1; j<clientCols; j++){
				sbf.append("','");
				sbf.append(grid[i][indexes[j]]);
			}
			sbf.append("']\n");
		}
		//close the array
		sbf.append("];\n");
		return sbf.toString();
	}
	/*
	 * Adds this grid to the DataCollection based on the fields available in Request.
	 * This is a data extractor that extracts data from a specific table in a form to DC 
	 */
	public void addGrid(DataCollection dc, HttpServletRequest req){
		String nam = (this.serverFieldName == null) ? this.name :this.serverFieldName ;
		if (this.columnMaps == null ||this.columnMaps.length == 0 ){
			dc.addGrid(nam, new String[0][0]);
			return;
		}
				
		FieldMap fm = this.columnMaps[0];
		int ncols = this.columnMaps.length;
		String[] col = req.getParameterValues(fm.name);
		if (col == null ){
			dc.addGrid(nam, new String[0][0]);
			return;
		}
		int nrows = col.length;
				
		String[][] grid = new String[nrows][ncols];
		//populate the grid
		for (int i=0; i<ncols; i++){ //note that we are going by column first...
			fm = this.columnMaps[i];
			col = req.getParameterValues(fm.name);
			if (col == null || col.length != nrows){ //some error in form design
				LOGGER.debug("Error in form Design " + name +" is not defined or has different number of values than the rest in the form");
			}else{
				for (int k=0; k<nrows; k++)grid[k][i]=col[k];
			}
		}
		dc.addGrid(nam, grid);

	}
/*
	public static void main(String[] args) {
		DataCollection dc = new DataCollection();
		String[][] arr1 = {{"name1", "name2", "name3", "name4"},
			{"val11", "val12", "val13", "val14"},
			{"val21", "val22", "val23", "val24"},
			{"val31", "val32", "val33", "val34"},
			{"val41", "val42", "val43", "val44"},
			};
		String[][] arr2 = {	{"value11", "value12", "value13", "value14"},
			{"value21", "value22", "value23", "value24"},
			{"value31", "value32", "value33", "value34"},
			};
		dc.addGrid("firstGrid", arr1);
		dc.addGrid("secondGrid", arr2);
		
		GridMap gm = new GridMap();
		gm.containsNames = false;
		gm.name = "clientName";
		gm.serverFieldName = "secondGrid";
		gm.columnMaps = new FieldMap[3];
		FieldMap fm;

		fm = new FieldMap();
		fm.name = "clientName1";
		fm.serverFieldName = "0";
		gm.columnMaps[0] = fm;

		fm = new FieldMap();
		fm.name = "clientName2";
		fm.serverFieldName = "3";
		gm.columnMaps[1] = fm;

		fm = new FieldMap();
		fm.name = "clientName3";
		fm.serverFieldName = "1";
		gm.columnMaps[2] = fm;

		LOGGER.debug(gm.toJavaScript(dc, "ddcc"));	
	}
*/
}
