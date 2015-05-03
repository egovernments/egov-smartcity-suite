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
 * Created on Oct 5, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.master;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.eGov.src.domain.EgAssetPO;
import com.exilant.eGov.src.domain.WorksDetail;
import com.exilant.eGov.src.domain.worksmis;
import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
/**
 * @author Lakshmi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
*/
/*
 * modified by iliyaraja---Changed the "worksDetail_code" param name in jsp file--->dc.getValue("worksDetail_codeId") Instead of ---->dc.getValue("worksDetail_code")
*/

public class ProcmtOrder extends AbstractTask {
	private static final Logger LOGGER=Logger.getLogger(ProcmtOrder.class);
	private static final String WORKSDETAILCODEID = "worksDetail_codeId";
	private static final String WORKSDETAILID = "worksDetail_ID";
    private DataCollection dc;
    private Connection con=null;
    private Statement st=null;
    private ResultSet rs=null;
    private int workOrderId=0;
    private int workmisOrderId=0;
    public void execute (String nameTask,
            String dataTask,
            DataCollection dataCollection,
            Connection conn,
            boolean errorOnNoData,
            boolean gridHasColumnHeading, String prefix) throws TaskFailedException {
        dc=dataCollection;
        con=conn;
//      Checking whether new data has to be Inserted  -------SL
        if(dc.getValue("modeOfExec").equalsIgnoreCase("new"))
        {
            createVoucher(dc,con);
            return;
        }
//      Checking If data has to be Edited  -------SL
        if(dc.getValue("modeOfExec").equalsIgnoreCase("edit"))
        {
            editVoucher(dc,con);
            return;
        }
    }
    private void createVoucher(DataCollection dc,Connection con)throws TaskFailedException
    {
        /************* validation part Begins************/
        try
        {
            /**          Validation Begins            **/
            //modified by iliyaraja
            verifyUniqueness(con,dc.getValue(WORKSDETAILCODEID));

           //  verifyUniqueness(con,dc.getValue("worksDetail_code"));
            /**       Posting Values into Tables: WorksDetail & egf_Asset_PO     **/
            workOrderId=postInWorksDetail(con,dc);
            if(LOGGER.isInfoEnabled())     LOGGER.info("CREATED WORKS DETAIL ID IS ***************************"+workOrderId);
            //added by iliyaraja
            dc.addValue("wDetail_createId",workOrderId);
            postInWorksmis(con,dc);

            postInEgfAssetPO(con,dc);

			/*
            if(dc.getValue("consup").equalsIgnoreCase("contractor"))
             postInEgw_Works_Deductions(con,dc);
             */

             //modified by iliyaraja
           dc.addMessage("userSuccess","Procurement Order of code: "+dc.getValue("WORKSDETAILCODEID").toString(),"Created Successfully");
          //  dc.addMessage("userSuccess","Procurement Order of code: "+dc.getValue("worksDetail_code").toString(),"Created Successfully");
        }
        catch(Exception ex)
        {
            dc.addMessage("userFailure","Procurement Order Creation Failure"+ex.getMessage());
            throw new TaskFailedException(ex);
        }
    }
    private int postInWorksDetail(Connection con,DataCollection dc)throws TaskFailedException
    {
        if(LOGGER.isInfoEnabled())     LOGGER.info("Inside postInWorksDetail Method");
        WorksDetail wd=new WorksDetail();

        int result=0;
        String orderDate=dc.getValue("worksDetail_orderDate1");
        String sanctionDate=dc.getValue("worksDetail_sanctionDate1");

        String orderdate1="";
        try
        {
            SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            Date dt=new Date();
            dt = sdf.parse( orderDate );
            orderdate1 = formatter.format(dt);
            if(LOGGER.isInfoEnabled())     LOGGER.info("oorderdate1: "+orderdate1);
            if(sanctionDate!=null && sanctionDate.trim().length()>0)
            {
                dt = sdf.parse( sanctionDate );
                sanctionDate = formatter.format(dt);
            }
            if(LOGGER.isInfoEnabled())     LOGGER.info("sanctionDate: "+sanctionDate);
        }
        catch(Exception e){
            //dc.addMessage("userFailure","Procurement Order Creation Failure");
            throw new TaskFailedException();
        }
        wd.setRelationId(dc.getValue("worksDetail_relationId"));
        if(dc.getValue(WORKSDETAILCODEID)!=null && dc.getValue(WORKSDETAILCODEID)!="")
        	wd.setCode(dc.getValue(WORKSDETAILCODEID));
       // wd.setCode(dc.getValue("worksDetail_code"));
        wd.setOrderDate(orderdate1);
        wd.setTotalValue(dc.getValue("worksDetail_totalValue"));
        if(dc.getValue("worksDetail_advancePayable")!=null && dc.getValue("worksDetail_advancePayable")!="")
            wd.setAdvancePayable(dc.getValue("worksDetail_advancePayable"));
        wd.setIsActive(dc.getValue("worksDetail_isActive"));
        wd.setModifiedBy(dc.getValue("egUser_id"));
        wd.setName(dc.getValue("worksDetail_name"));
        wd.setAuthorizedBy(dc.getValue("worksDetail_authorizedBy"));
        if(dc.getValue("worksDetail_levelOfWork")!=null && dc.getValue("worksDetail_levelOfWork")!="")
        	wd.setLevelOfWork(dc.getValue("worksDetail_levelOfWork"));
        if(LOGGER.isInfoEnabled())     LOGGER.info("Wrd info"+dc.getValue("worksDetail_wardId"));
        //if(dc.getValue("worksDetail_wardId")!=null && dc.getValue("worksDetail_wardId")!="")
        String wrdId=dc.getValue("worksDetail_wardId");
        if(wrdId.equals(""))
			{
        	wrdId=null;
			}
        wd.setWardId(wrdId);
        wd.setSecurityDeposit(dc.getValue("worksDetail_securityDeposit"));
        if(dc.getValue("worksDetail_retention")!=null && dc.getValue("worksDetail_retention")!="")
            wd.setRetention(dc.getValue("worksDetail_retention"));
        if(dc.getValue("worksDetail_bankGuarantee")!=null && dc.getValue("worksDetail_bankGuarantee")!="")
            wd.setBankGuarantee(dc.getValue("worksDetail_bankGuarantee"));
        wd.setType(dc.getValue("worksDetail_type"));
        wd.setFundid(dc.getValue("worksDetail_fundId"));
        if(dc.getValue("worksDetail_fundSourceId")!=null && dc.getValue("worksDetail_fundSourceId")!="")
            wd.setFundSourceId(dc.getValue("worksDetail_fundSourceId"));
        wd.setSanctionNo(dc.getValue("worksDetail_sanctionno"));
        if(dc.getValue("worksDetail_workstds")!=null && dc.getValue("worksDetail_workstds")!="")
            wd.setWorksTds(dc.getValue("worksDetail_workstds"));
        if(dc.getValue("worksDetail_remarks")!=null && dc.getValue("worksDetail_remarks")!="")
            wd.setRemarks(dc.getValue("worksDetail_remarks"));
        	wd.setWorkCategory(dc.getValue("worksDetail_workCategory"));
            wd.setSubCategory(dc.getValue("worksDetail_subCategory"));
            
        wd.setSanctionDate(sanctionDate);
        if (null != dc.getValue("department_id")
				&& dc.getValue("department_id").trim().length() != 0) {
			wd.setExecdeptid(dc.getValue("department_id"));
		}
        
        try{
            if(dc.getValue("modeOfExec").equalsIgnoreCase("new"))
            {
                wd.insert(con);
                result=wd.getId();
            }
            else
            {
                if(LOGGER.isInfoEnabled())     LOGGER.info("Update");
                result=Integer.parseInt( dc.getValue(WORKSDETAILID));
                wd.setId(dc.getValue(WORKSDETAILID));
                wd.update(con);
            }
            return result;
        }
        catch(Exception ex){
            //dc.addMessage("userFailure","Procurement Order Creation Failure");

            throw new TaskFailedException(ex);
        }

    }

    /** Method to insert the values into the egw_works_mis table
     *
     * @param con
     * @param dc
     * @throws TaskFailedException
     */

    private void postInWorksmis(Connection con,DataCollection dc)throws TaskFailedException
    {
        if(LOGGER.isInfoEnabled())     LOGGER.info("Inside postInWorksmis Method");
        worksmis wm =new worksmis();
        String strworkOrderId = Integer.toString(workOrderId);

        wm.setWorksdetailid(strworkOrderId);
        wm.setFieldid(dc.getValue("field_name"));
        wm.setIsFixedAsset(dc.getValue("isFixedAsset"));
        String sid=dc.getValue("scheme");
        String ssid=dc.getValue("subscheme");
        if(LOGGER.isDebugEnabled())     LOGGER.debug(sid+"sub scheme"+ssid);
        EGovernCommon cm=new EGovernCommon();
        if(sid !=null && sid.length()>0)
        cm.validateScheme(dc.getValue("worksDetail_orderDate1"),dc.getValue("scheme"));
        if( ssid!=null && ssid.length()>0)
        cm.validatesubScheme(dc.getValue("worksDetail_orderDate1"),dc.getValue("subscheme"));
        wm.setSchemeid(dc.getValue("scheme"));
        wm.setSubschemeid(dc.getValue("subscheme"));
        if(LOGGER.isInfoEnabled())     LOGGER.info("Inside postInWorksmis Method var : "+strworkOrderId);
        try{
            if(dc.getValue("modeOfExec").equalsIgnoreCase("new"))
            { if(LOGGER.isInfoEnabled())     LOGGER.info("new");
                wm.insert(con);

            }
            else
            {
                if(LOGGER.isInfoEnabled())     LOGGER.info("Update");
                //wm.setWorksdetailid(dc.getValue(WORKSDETAILID));
                wm.update(con);
            }

        }
        catch(Exception ex){
            //dc.addMessage("userFailure","Procurement Order Creation Failure");
          throw new TaskFailedException(ex);
        }

    }


    private void postInEgfAssetPO(Connection con,DataCollection dc)throws TaskFailedException
    {
        EgAssetPO egPo=new EgAssetPO();
        int len=0;
       // String assetId=dc.getValue("ftService_id");
        String assetList[]=dc.getValueList("selected_Asset");
        if(assetList==null){
            assetList=new String[1];
            assetList[0]=dc.getValue("selected_Asset");
        }
        len=assetList.length;
        if(LOGGER.isInfoEnabled())     LOGGER.info("len:="+len);
        for(int i=0;i<len;i++)
        {
            if(assetList[i]=="")continue;
            egPo.setAssetId(assetList[i]);
            egPo.setWorkOrderId(String.valueOf(workOrderId));
            try
            {
                egPo.insert();
            }
            catch(Exception ex){
                throw new TaskFailedException(ex);
            }
        }
    }

	/*
    // added by iliyaraja for insert tds detail for Work Order in Egw_Works_Deductions table
    private void postInEgw_Works_Deductions(Connection con,DataCollection dc)throws SQLException,TaskFailedException
	    {
	        if(LOGGER.isInfoEnabled())     LOGGER.info("Inside postInEgw_Works_Deductions Method");
	        Egw_Works_Deductions egwDed =new Egw_Works_Deductions();
	        boolean booleanDelA=true;
	        boolean booleanDelB=true;


	        String strworkOrderId = Integer.toString(workOrderId);
	        String tdsList[]=dc.getValueList("tds_List");
	         st=con.createStatement();
	         int rcount=0;
	         if(LOGGER.isInfoEnabled())     LOGGER.info("Inside postInEgw_Works_Deductions modeOfExec is--->"+dc.getValue("modeOfExec"));



	        if(tdsList==null)
	        {
	        	if(LOGGER.isInfoEnabled())     LOGGER.info("<-------------Inside tdsList NULL--------------------->");
	        	tdsList=new String[1];
	        	tdsList[0]=dc.getValue("tds_List");
	        }



	        if(LOGGER.isInfoEnabled())     LOGGER.info("tdsList length is------>"+tdsList.length);
	         if(LOGGER.isInfoEnabled())     LOGGER.info("tdsList[0] value is------>"+tdsList[0]);


	        for(int i=0;i<tdsList.length;i++)
	        {
	        	//if(LOGGER.isInfoEnabled())     LOGGER.info("<-------Inside tdsList NOT NULL  tdsList[i] value is-->"+tdsList[i]);

	        	if(tdsList[i]=="" || tdsList[i]==null)
	        	{
					if(dc.getValue("modeOfExec").equalsIgnoreCase("edit"))
					{
						if(booleanDelA)
						{
								if(LOGGER.isInfoEnabled())     LOGGER.info("Inside Modify Mode 11111111");
								if(LOGGER.isInfoEnabled())     LOGGER.info("If No TDS for that particular Work Order then delete that old record from Egw_Works_Deductions");
								if(LOGGER.isInfoEnabled())     LOGGER.info("workOrderId"+workOrderId);


								// st=con.createStatement();
								 rcount=st.executeUpdate("delete Egw_Works_Deductions where worksdetailid="+workOrderId);
								 if(rcount>0)
								if(LOGGER.isInfoEnabled())     LOGGER.info("deleted "+rcount+" row from table: Egw_Works_Deductions");

								booleanDelA=false;
						}
					}

				}//if no tds selected

	        	if(tdsList[i]!="" && tdsList[i]!=null)
	        	{
	        	egwDed.setWorksdetailid(strworkOrderId);
	        	egwDed.setDedType("S");
	        	egwDed.setTdsId(tdsList[i]);
	               	try{
		            if(dc.getValue("modeOfExec").equalsIgnoreCase("new"))
		            { if(LOGGER.isInfoEnabled())     LOGGER.info("INSERT ROW IN Egw_Works_Deductions");
		            egwDed.insert(con);

		            }
		            else
		            {
		                if(LOGGER.isInfoEnabled())     LOGGER.info("Inside Modify Mode 2222222222222");

		              	if(booleanDelB)
		             	{
		             	 	if(LOGGER.isInfoEnabled())     LOGGER.info("Modify Mode Delete ROW IN Egw_Works_Deductions");
		             	 	if(LOGGER.isInfoEnabled())     LOGGER.info("workOrderId"+workOrderId);


		             	 	// st=con.createStatement();
							 rcount=st.executeUpdate("delete Egw_Works_Deductions where worksdetailid="+workOrderId);
							 if(rcount>0)
							if(LOGGER.isInfoEnabled())     LOGGER.info("deleted "+rcount+" row from table: Egw_Works_Deductions");

		              		booleanDelB=false;
						}

		                egwDed.insert(con);
		               	if(LOGGER.isInfoEnabled())     LOGGER.info("Modify Mode Insert ROW IN Egw_Works_Deductions");
		            }

		        }
		        catch(Exception ex){
		           	          throw new TaskFailedException(ex);
		    	    }
				}// main if

	        } //main for


    }//postInEgw_Works_Deductions
    */
    private void editVoucher(DataCollection dc,Connection con)throws TaskFailedException
    {
        /************* validation part Begins************/
        Statement st=null;
        ResultSet rset=null;
        try
        {
            /**          Validation Begins            **/
            if((dc.getValue("worksDetail_advancePayable").trim().length()>0) &&( !dc.getValue("worksDetail_advancePayable").trim().equals("0")))
            	validateAdvanceAmt(con,dc.getValue("worksDetail_advancePayable"),dc.getValue(WORKSDETAILID));
           // verifyUniqueness(con,dc.getValue(WORKSDETAILCODEID));
           // Modified by iliyaraja
             verifyUniquenessEdit(con,dc.getValue(WORKSDETAILCODEID));

            /**       Posting Values into Tables: WorksDetail & egf_Asset_PO     **/
            deleteEgfAssetPO(con);
            workOrderId=postInWorksDetail(con,dc);
            postInWorksmis(con,dc);

            postInEgfAssetPO(con,dc);

           /*
           if(dc.getValue("consup").equalsIgnoreCase("contractor"))
            postInEgw_Works_Deductions(con,dc);
            */

            dc.addMessage("userSuccess","Procurement Order of code: "+dc.getValue(WORKSDETAILCODEID).toString(),"Updated Successfully");
           // dc.addMessage("userSuccess","Procurement Order of code: "+dc.getValue("worksDetail_code").toString(),"Updated Successfully");
        }
        catch(Exception ex)
        {
           if(LOGGER.isInfoEnabled())     LOGGER.info(ex);
        	dc.addMessage("userFailure","Procurement Order Updation Failure"+ex.getMessage());
            throw new TaskFailedException(ex);
        }
    }
    private void deleteEgfAssetPO(Connection con)throws TaskFailedException
    {
        Statement st=null;
        int rs;
        String wrkid=dc.getValue(WORKSDETAILID);
        try
        {
            st=con.createStatement();
            rs=st.executeUpdate("delete eg_asset_po where workorderid="+wrkid);
            if(rs>0)
                if(LOGGER.isInfoEnabled())     LOGGER.info("deleted "+rs+" from table: eg_asset_po");
        }
        catch(Exception e){
            //dc.addMessage("userFailure","Procurement Order Updation Failure");
            throw new TaskFailedException(e);
        }
    }
    public void verifyUniqueness(Connection con,String field1)throws TaskFailedException,SQLException
    {
        if(LOGGER.isInfoEnabled())     LOGGER.info("verifyUniqueness:");
        Statement st=con.createStatement();
        ResultSet rs=null;
        String codeCreated="";
        String code="";
        codeCreated=field1.toLowerCase();
        rs=st.executeQuery("select code from worksdetail where lower(code)='"+codeCreated+"'");
        if(rs.next())
        {
            code=rs.getString(1);
            if(LOGGER.isInfoEnabled())     LOGGER.info("codeExisting:"+code);
            if(code.equalsIgnoreCase(field1))
            {
                dc.addMessage("exilError",": Duplicate Code :"+field1);
                throw new TaskFailedException();
            }
        }
    }
    public void verifyUniquenessEdit(Connection con,String field1)throws TaskFailedException,SQLException
	    {
	        if(LOGGER.isInfoEnabled())     LOGGER.info("verifyUniqueness:");
	        Statement st=con.createStatement();
	        ResultSet rs=null;
	        String codeCreated="";
	        String code="";
	        codeCreated=field1.toLowerCase();
	        rs=st.executeQuery("select wd.code from worksdetail wd where wd.id !="+dc.getValue(WORKSDETAILID)+" and lower(wd.code)='"+codeCreated+"'");
	        if(rs.next())
	        {
	            code=rs.getString(1);
	            if(LOGGER.isInfoEnabled())     LOGGER.info("codeExisting:"+code);
	            if(code.equalsIgnoreCase(field1))
	            {
	                dc.addMessage("exilError",": Duplicate Code :"+field1);
	                throw new TaskFailedException();
	            }
	        }
    }
    public void validateAdvanceAmt(Connection con,String field1,String field2)throws TaskFailedException,SQLException
    {
        if(LOGGER.isInfoEnabled())     LOGGER.info("validateAdvanceAmt:");
        Statement st=con.createStatement();
        ResultSet rs=null;
        double advAmt=0,advAmtPay=0;
        advAmtPay=Double.parseDouble(field1);
        rs=st.executeQuery("select Advanceamount from worksdetail where id='"+field2+"'");
        if(rs.next())
        {
            advAmt=rs.getDouble(1);
            if(LOGGER.isInfoEnabled())     LOGGER.info("advAmt:"+advAmt+" advAmtPay: "+advAmtPay);
            if(advAmtPay<advAmt)
            {
                dc.addMessage("exilError",": Advance Amount Payable :"+field1+" Should not be Less than Advance amount Paid: "+advAmt);
                throw new TaskFailedException();
            }
        }
    }
}
