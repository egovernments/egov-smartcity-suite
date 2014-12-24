/**
 * 
 */
package org.egov.services.deduction;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.utils.EntityType;
import org.egov.dao.voucher.VoucherHibernateDAO;
import org.egov.infstr.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.deduction.RemittanceBean;
import org.egov.utils.Constants;
import org.egov.utils.VoucherHelper;
import org.egov.web.actions.deduction.RemitRecoveryAction;
import org.hibernate.SQLQuery;
/**
 * @author manoranjan
 *
 */
public class RemitRecoveryService {

	private PersistenceService persistenceService;
	
	private static final Logger	LOGGER	= Logger.getLogger(RemitRecoveryAction.class);
	private static final SimpleDateFormat DDMMYYYY = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
	private static final SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
	private VoucherHibernateDAO voucherHibDAO;
	public List<RemittanceBean> getPendingRecoveryDetails(RemittanceBean remittanceBean ,CVoucherHeader voucherHeader,Integer detailKeyId) throws ValidationException{
		List<RemittanceBean> listRemitBean = new ArrayList<RemittanceBean>();
		StringBuffer query = new StringBuffer(200);
		query.append("select vh.name,vh.voucherNumber ,vh.voucherDate,egr.gldtlamt,gld.detailTypeId,gld.detailKeyId,egr.id ");
		query.append(" from CVoucherHeader vh ,Vouchermis mis , CGeneralLedger gl ,CGeneralLedgerDetail gld , EgRemittanceGldtl egr , Recovery rec  where ").
		append("  rec.chartofaccounts.id = gl.glcodeId.id and gld.id = egr.generalledgerdetail.id and  gl.id = gld.generalLedgerId and vh.id = gl.voucherHeaderId.id ").
		append(" and mis.voucherheaderid.id = vh.id  and vh.status=0 and vh.fundId.id=? and  egr.gldtlamt - decode(egr.remittedamt,null,0,egr.remittedamt) != 0 and rec.id =").
		append(remittanceBean.getRecoveryId()).append(" and ( egr.recovery.id =").append(remittanceBean.getRecoveryId())
		.append( " OR egr.recovery.id is null )")
		.append(" and vh.voucherDate <='").
		append(Constants.DDMMYYYYFORMAT1.format(voucherHeader.getVoucherDate())).append("'");
		if(detailKeyId!=null && detailKeyId!=-1)
			query.append(" and egr.generalledgerdetail.detailkeyid="+detailKeyId);
		query.append(VoucherHelper.getMisQuery(voucherHeader)).append(" order by vh.voucherNumber,vh.voucherDate");
		populateDetails(voucherHeader, listRemitBean, query);
		return listRemitBean;
	}
	
	public List<RemittanceBean> getRecoveryDetails(RemittanceBean remittanceBean , CVoucherHeader voucherHeader) throws ValidationException{
		LOGGER.debug("RemitRecoveryService | getRecoveryDetails | Start");
		List<RemittanceBean> listRemitBean = new ArrayList<RemittanceBean>();
		StringBuffer query = new StringBuffer(200);
	query=query.append("" +
	" SELECT vh.NAME     AS col_0_0_,  vh.VOUCHERNUMBER AS col_1_0_,  vh.VOUCHERDATE   AS col_2_0_,"+
	" egr.GLDTLAMT      AS col_3_0_,  gld.DETAILTYPEID  AS col_4_0_,  gld.DETAILKEYID   AS col_5_0_,"+
	" egr.ID            AS col_6_0_"+
	"  FROM VOUCHERHEADER vh,  VOUCHERMIS mis,  GENERALLEDGER gl,  GENERALLEDGERDETAIL gld,  EG_REMITTANCE_GLDTL egr,  TDS recovery5_"+
" WHERE recovery5_.GLCODEID  =gl.GLCODEID AND gld.ID =egr.GLDTLID AND gl.ID =gld.GENERALLEDGERID AND vh.ID =gl.VOUCHERHEADERID"+
" AND mis.VOUCHERHEADERID  =vh.ID AND vh.STATUS    =0 AND vh.FUNDID    =").append(voucherHeader.getFundId().getId())
.append(" AND egr.GLDTLAMT-"+
" DECODE((select egr1.REMITTEDAMT from EG_REMITTANCE_GLDTL egr1,eg_remittance_detail egd,eg_remittance  eg,voucherheader vh"+ 
" where vh.status!=4 and  eg.PAYMENTVHID=vh.id and egd.remittanceid=eg.id and egr1.id=egd.remittancegldtlid and egr1.id=egr.id),"+
" null,0,egr.REMITTEDAMT)<>0 AND recovery5_.ID          = ")
.append(remittanceBean.getRecoveryId()).append(" AND (egr.TDSID    = ")
.append(remittanceBean.getRecoveryId())
.append(" OR egr.TDSID     IS NULL) AND vh.VOUCHERDATE <= '")
.append(Constants.DDMMYYYYFORMAT1.format(voucherHeader.getVoucherDate()))
.append("'   "+getMisSQlQuery(voucherHeader)) 
.append(" ORDER BY vh.VOUCHERNUMBER,  vh.VOUCHERDATE");
				
LOGGER.debug("RemitRecoveryService | getRecoveryDetails | query := "+ query.toString());
	
		
		populateDetailsBySQL(voucherHeader, listRemitBean, query);
		LOGGER.debug("RemitRecoveryService | listRemitBean size : "+ listRemitBean.size());
		LOGGER.debug("RemitRecoveryService | getRecoveryDetails | End");
		return listRemitBean;
	}
	/**
	 * @param voucherHeader
	 * @return
	 */
	private Object getMisSQlQuery(CVoucherHeader voucherHeader) {
		StringBuffer misQuery = new StringBuffer();
		if(null != voucherHeader && null != voucherHeader.getVouchermis()){
			if(null != voucherHeader.getVouchermis().getDepartmentid() ){
				misQuery.append("and  mis.departmentid=");
				misQuery.append(voucherHeader.getVouchermis().getDepartmentid().getId());
			}
			if(null != voucherHeader.getVouchermis().getFunctionary()){
				misQuery.append(" and mis.functionary=");
				misQuery.append(voucherHeader.getVouchermis().getFunctionary().getId());
			}if(null != voucherHeader.getVouchermis().getSchemeid()){
				misQuery.append(" and mis.schemeid=");
				misQuery.append(voucherHeader.getVouchermis().getSchemeid().getId());
			}
			if (null != voucherHeader.getVouchermis().getSubschemeid()) {
				misQuery.append(" and mis.subschemeid=");
				misQuery.append(voucherHeader.getVouchermis().getSubschemeid().getId());
			}
			if (null != voucherHeader.getVouchermis().getFundsource()) {
				misQuery.append(" and mis.fundsourceid=");
				misQuery.append(voucherHeader.getVouchermis().getFundsource().getId());
			}
			if (null != voucherHeader.getVouchermis().getDivisionid()) {
				misQuery.append(" and mis.divisionid=");
				misQuery.append(voucherHeader.getVouchermis().getDivisionid().getId());
			}
		}
		return misQuery.toString();
	}

	private void populateDetailsBySQL(CVoucherHeader voucherHeader,List<RemittanceBean> listRemitBean, StringBuffer query) {
		RemittanceBean remitBean;
		SQLQuery searchSQLQuery = persistenceService.getSession().createSQLQuery(query.toString());
		final List<Object[]> list=searchSQLQuery.list();
		for (Object[] element : list) {
			remitBean = new RemittanceBean();
			remitBean.setVoucherName(element[0].toString());
			remitBean.setVoucherNumber(element[1].toString());
			try {
				remitBean.setVoucherDate(DDMMYYYY.format(YYYYMMDD.parse(element[2].toString())));
			} catch (ParseException e) {
				LOGGER.error("Exception Occured while Parsing instrument date" + e.getMessage());
			}
			remitBean.setAmount(BigDecimal.valueOf(Double.parseDouble(element[3].toString())));
			EntityType entity =  voucherHibDAO.getEntityInfo(Integer.valueOf(element[5].toString()), Integer.valueOf(element[4].toString()));
			remitBean.setPartyCode(entity.getCode());
			remitBean.setPartyName(entity.getName());
			remitBean.setPanNo(entity.getPanno());
			remitBean.setDetailTypeId(Integer.valueOf(element[4].toString()));
			remitBean.setDetailKeyid(Integer.valueOf(element[5].toString()));
			remitBean.setRemittance_gl_dtlId(Integer.valueOf(element[6].toString()));
			listRemitBean.add(remitBean);
		}
	}
	
	private void populateDetails(CVoucherHeader voucherHeader,List<RemittanceBean> listRemitBean, StringBuffer query) {
		RemittanceBean remitBean;
		final List<Object[]> list = persistenceService.findAllBy(query.toString(), voucherHeader.getFundId().getId());
		for (Object[] element : list) {
			remitBean = new RemittanceBean();
			remitBean.setVoucherName(element[0].toString());
			remitBean.setVoucherNumber(element[1].toString());
			try {
				remitBean.setVoucherDate(DDMMYYYY.format(YYYYMMDD.parse(element[2].toString())));
			} catch (ParseException e) {
				LOGGER.error("Exception Occured while Parsing instrument date" + e.getMessage());
			}
			remitBean.setAmount(BigDecimal.valueOf(Double.parseDouble(element[3].toString())));
			EntityType entity =  voucherHibDAO.getEntityInfo(Integer.valueOf(element[5].toString()), Integer.valueOf(element[4].toString()));
			remitBean.setPartyCode(entity.getCode());
			remitBean.setPartyName(entity.getName());
			remitBean.setPanNo(entity.getPanno());
			remitBean.setDetailTypeId(Integer.valueOf(element[4].toString()));
			remitBean.setDetailKeyid(Integer.valueOf(element[5].toString()));
			remitBean.setRemittance_gl_dtlId(Integer.valueOf(element[6].toString()));
			listRemitBean.add(remitBean);
		}
	}
	
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public void setVoucherHibDAO(VoucherHibernateDAO voucherHibDAO) {
		this.voucherHibDAO = voucherHibDAO;
	}
}
