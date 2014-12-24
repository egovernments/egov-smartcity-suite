package org.egov.billsaccounting.dao;
/*
 * Created on Jan 31, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */



import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.model.Supplierbilldetail;
import org.egov.commons.Fundsource;
import org.egov.commons.Relation;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.lib.rjbac.dept.DepartmentImpl;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author Tilak
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SupplierBillHibernateDAO extends GenericHibernateDAO
{

	public SupplierBillHibernateDAO(Class persistentClass, Session session)
	{ 
			super(persistentClass, session);
	}
    
    private static final Logger LOGGER = Logger.getLogger(SupplierBillHibernateDAO.class); 
    
    /**
	 * This function will get the Supplierbill voucher number associated with a Material Receipt Note.
	 * @param mrnno
	 * @param con
	 * @return
	 * @throws Exception 
	 */
	public HashMap getMrnBillInfo(final String mrnid,Connection con) throws Exception
	{
		HashMap billInfo=new HashMap();
		final String getBillInfo="Select a.billnumber,a.billdate from supplierbilldetail a "+
			"where a.mrnid=(select b.id from egf_mrnheader b where b.id="+mrnid+" and b.BILLId!=null)";
		LOGGER.info("updateQuery   "+getBillInfo);
		Statement statement = con.createStatement();
		ResultSet rs=statement.executeQuery(getBillInfo);
		if(rs.next())
		{
			billInfo.put("billNo",rs.getString(1));
			billInfo.put("billDate", rs.getString(2));
		}
		statement.close();
		rs.close();
		LOGGER.info("Bill Informatioms for "+mrnid+" is:"+billInfo);
		return billInfo;

	}
	
	public List<Supplierbilldetail> getSupplierBillDetailFilterBy(Integer fundId, Fundsource fundSource, Date vhFromDate, Date vhToDate, Relation relation, String vhNoFrom, String vhNoTo,final DepartmentImpl dept,String functionary)
    {
    	Query qry;
        StringBuffer qryStr = new StringBuffer();
        List<Supplierbilldetail> supBillList=null;
        qryStr.append(" select distinct sb from Supplierbilldetail sb, Worksdetail wd where sb.voucherheader.status=0 "
        		+" AND sb.passedamount>((sb.paidamount)+(sb.tdsamount)+(sb.advadjamt)+(sb.otherrecoveries))");
										       
        if(fundId!=null && !fundId.equals(""))
        {
            qryStr.append(" and sb.voucherheader.fundId=:fundId");           
        } 	
        
        if(vhFromDate!=null)
        {
            qryStr.append(" and sb.voucherheader.voucherDate >=:vhFromDate");
        }
        if(vhToDate!=null)
        {
            qryStr.append(" and sb.voucherheader.voucherDate <=:vhToDate");
        }
        if(vhNoFrom!=null && vhNoTo!=null)
        {
            qryStr.append(" and sb.voucherheader.voucherNumber between :vhNoFrom and :vhNoTo");
        } 
        else if(vhNoFrom!=null)
        {
            qryStr.append(" and sb.voucherheader.voucherNumber >=:vhNoFrom");
        }
    	else if(vhNoTo!=null)
    	{
            qryStr.append(" and sb.voucherheader.voucherNumber <=:vhNoTo");
        }
    	if(fundSource!=null)
        {
            qryStr.append(" and sb.worksdetailid= wd.id and wd.fundsource=:fundSource");           
        } 
    	if(relation!=null)
        { 
    		if(relation.getBankname().equals("RTGS")) // for RTGS bills
            {
    			qryStr.append(" and trim(sb.relation.bankname) is not null and trim(sb.relation.bankaccount) is not null and trim(sb.relation.tinno) is not null and trim(sb.relation.ifsccode) is not null ");
            }
            else
    		qryStr.append(" and sb.relation=:relation");
        }
    		
    	if(dept!=null)
    	{
    		qryStr.append(" and sb.egBillregister.egBillregistermis.egDepartment=:dept");
    	}
    	if(functionary!=null)
    	{
    		qryStr.append(" and sb.egBillregister.egBillregistermis.functionaryid=:functionary");
    	}
        qry = getSession().createQuery(qryStr.toString()) ;
        if(fundId!=null)
        	qry.setInteger("fundId",fundId);       
        if(vhFromDate!=null)
        	qry.setDate("vhFromDate", vhFromDate);
        if(vhToDate!=null)
        	qry.setDate("vhToDate", vhToDate);
        if(fundSource!=null)
        	qry.setEntity("fundSource",fundSource);
        if(relation!=null)
        {
        	if(!relation.getBankname().equals("RTGS"))
        		qry.setEntity("relation",relation);
        }
        if(vhNoFrom!=null && vhNoTo!=null)
        {
        	qry.setString("vhNoFrom", vhNoFrom);
        	qry.setString("vhNoTo", vhNoTo);
        }
    	else if(vhNoFrom!=null)
    		qry.setString("vhNoFrom", vhNoFrom);
    	else if(vhNoTo!=null)
    		qry.setString("vhNoTo", vhNoTo);
        if(dept!=null)
     	{
     		qry.setEntity("dept", dept);
     	}
     	if(functionary!=null)
     	{
     		qry.setBigDecimal("functionary", new BigDecimal(functionary));
     	}
        supBillList=qry.list();
        return supBillList;
    }
	
}