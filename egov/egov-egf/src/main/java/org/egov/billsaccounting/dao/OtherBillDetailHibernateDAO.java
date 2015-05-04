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
/**
 * 
 */
package org.egov.billsaccounting.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.model.OtherBillDetail;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Fundsource;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author sapna     
 *
 */
@Transactional(readOnly=true)
public class OtherBillDetailHibernateDAO extends GenericHibernateDAO
{

	/**
	 *   
	 */
	public OtherBillDetailHibernateDAO(Class persistentClass, Session session)
	{ 
		super(persistentClass, session);
	}
	   private static final Logger LOGGER = Logger.getLogger(OtherBillDetailHibernateDAO.class);
	   
   public List<OtherBillDetail> getCBillFilterBy(Integer fundId, Fundsource fundSource, Date vhFromDate, Date vhToDate, Date fromBillDate, Date toBillDate, final String billVhNoFrom, final String billVhNoTo,final Department dept,String functionary)
   {
    	Query qry;
        StringBuffer qryStr = new StringBuffer();
        List<OtherBillDetail> otherBillList=null;
        qryStr.append(" from OtherBillDetail ob where ob.voucherheaderByVoucherheaderid.status=0 and ob.egBillregister.billstatus <> 'PAYMENT APPROVED'" +
        		" and ob.egBillregister.expendituretype='Contingency' and ob.voucherheaderByVoucherheaderid.type='Journal Voucher' and ob.voucherheaderByVoucherheaderid.name='Contingency Journal'");
										       
        if(fundId!=null)  
        {
            qryStr.append(" and ob.voucherheaderByVoucherheaderid.fundId=:fundId");           
        } 	
        
        if(vhFromDate!=null)
        {
            qryStr.append(" and ob.voucherheaderByVoucherheaderid.voucherDate >=:vhFromDate");
        }
        if(vhToDate!=null)
        {
            qryStr.append(" and ob.voucherheaderByVoucherheaderid.voucherDate <=:vhToDate");
        }
        if(fromBillDate!=null)
        {
            qryStr.append(" and ob.egBillregister.billdate >=:fromBillDate");
        }
        if(toBillDate!=null)
        {
            qryStr.append(" and ob.egBillregister.billdate <=:toBillDate");
        }
        if(billVhNoFrom!=null && billVhNoTo!=null)
        {
            qryStr.append(" and ob.voucherheaderByVoucherheaderid.voucherNumber between :billVhNoFrom and :billVhNoTo");
        } 
        else if(billVhNoFrom!=null)
        {
            qryStr.append(" and ob.voucherheaderByVoucherheaderid.voucherNumber>=:billVhNoFrom");
        }
    	else if(billVhNoTo!=null)
    	{
            qryStr.append(" and ob.voucherheaderByVoucherheaderid.voucherNumber<=:billVhNoTo");
        }
    	if(fundSource!=null)
        {
            qryStr.append(" and ob.egBillregister.egBillregistermis.fundsource=:fundSource");           
        } 
    	if(dept!=null)
    	{
    		qryStr.append(" and ob.egBillregister.egBillregistermis.egDepartment=:dept");
    	}
    	if(functionary!=null)
    	{
    		qryStr.append(" and ob.egBillregister.egBillregistermis.functionaryid=:functionary");
    	}
    		
        qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
        if(fundId!=null)
        	qry.setInteger("fundId",fundId);       
        if(vhFromDate!=null)
        	qry.setDate("vhFromDate", vhFromDate);
        if(vhToDate!=null)
        	qry.setDate("vhToDate", vhToDate);
        if(fromBillDate!=null)
        	qry.setDate("fromBillDate", fromBillDate);
        if(toBillDate!=null)
        	qry.setDate("toBillDate", toBillDate);
        if(fundSource!=null)
        	qry.setEntity("fundSource",fundSource);
        if(billVhNoFrom!=null && billVhNoTo!=null)
        {
        	qry.setString("billVhNoFrom", billVhNoFrom);
        	qry.setString("billVhNoTo", billVhNoTo);
        }
    	else if(billVhNoFrom!=null)
    		qry.setString("billVhNoFrom", billVhNoFrom);
    	else if(billVhNoTo!=null)
    		qry.setString("billVhNoTo", billVhNoTo);
        if(dept!=null)
    	{
    		qry.setEntity("dept", dept);
    	}
    	if(functionary!=null)
    	{
    		qry.setBigDecimal("functionary", new BigDecimal(functionary));
    	}
        otherBillList=qry.list();
        return otherBillList;
    }
   
   public List<OtherBillDetail> getOtherBillDetailByPymntVoucherheader(CVoucherHeader pymntVoucherheader)
   {
	 final  Query qry =HibernateUtil.getCurrentSession().createQuery(" from OtherBillDetail obd where obd.voucherheaderByPayvhid =:pymntVoucherheader");
	   qry.setEntity("pymntVoucherheader", pymntVoucherheader);
	   return qry.list();
   }

}
