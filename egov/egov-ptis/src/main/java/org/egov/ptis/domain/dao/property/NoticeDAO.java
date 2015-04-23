package org.egov.ptis.domain.dao.property;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.ptis.notice.PtNotice;
import org.egov.ptis.notice.SearchNoticeForm;
import org.hibernate.Query;

public class NoticeDAO {
	public final static Logger LOGGER = Logger.getLogger(NoticeDAO.class);

	public boolean saveNoticeDetails(PtNotice notice, InputStream noticeDocFis) {
		boolean isNoticeSaved = false;
		try {
			Query query = HibernateUtil
					.getCurrentSession()
					.createSQLQuery(
							"insert into EGPT_NOTICE (ID_NOTICE,ID_MODULE,NOTICETYPE,NOTICENO,NOTICEDATE,ID_USER) "
									+ "values (SEQ_NOTICE_ID.nextval,?,?,?,sysdate,?")
					.setParameter(0, notice.getModuleId())
					.setParameter(1, notice.getNoticeType())
					.setParameter(2, notice.getNoticeNo())
					.setParameter(3, notice.getUserId());
			query.executeUpdate();
			isNoticeSaved = true;
		} catch (Exception e) {
			LOGGER.info("Exception in saveNoticeDetails()--- NoticeDao--"
					+ e.getMessage());
		}
		return isNoticeSaved;
	}

	/**
	 * 
	 * @param noticeNo
	 * @param objectNo
	 * @return
	 */
	public InputStream getNoticeDocument(String noticeNo, String objectNo) {
		Connection conn = null;
		InputStream istream = null;
		String isBlob = null;
		try {
			List results = HibernateUtil
					.getCurrentSession()
					.createSQLQuery(
							"select DOCUMENT,IS_BLOB,DOCUMENT1 from notice where NOTICENO = ? and OBJECTNO = ?")
					.setParameter(0, noticeNo).setParameter(1, objectNo).list();
			for (Object result : results) {
				Object[] objects = (Object[]) result;
				isBlob = (String) objects[1];
				if (isBlob != null && isBlob.equals("Y")) {
					istream = (InputStream) objects[2];
				} else if ((isBlob == null)
						|| (isBlob != null && isBlob.equals("N"))) {
					istream = (InputStream) objects[0];
				}
			}
		} catch (Exception e) {
			LOGGER.info("Exception in getNoticeDocument()--- NoticeDao--"
					+ e.getMessage());
		}
		return istream;
	}

	@SuppressWarnings("unchecked")
	public List<PtNotice> getNoticeDetails(SearchNoticeForm searchNoticeForm) {
		StringBuffer queryStr = new StringBuffer(500);
		List params = new ArrayList();
		ResultSet resultSet = null;
		PtNotice notice = null;
		List<PtNotice> searchNoticeList = new ArrayList<PtNotice>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		try {

			if (searchNoticeForm != null) {
				queryStr.append("select * from EGPT_NOTICE where NOTICETYPE = ? and NOTICENO = ? ");
				params.add(searchNoticeForm.getNoticeType());
				params.add(searchNoticeForm.getNoticeNumber());
				if (searchNoticeForm.getFromDate() != null
						&& !searchNoticeForm.getFromDate().equals("")
						&& searchNoticeForm.getToDate() != null
						&& !searchNoticeForm.getToDate().equals("")) {
					Date toDate = sdf.parse(searchNoticeForm.getToDate());
					int day = toDate.getDate();
					toDate.setDate(day + 1);
					queryStr.append(" and NOTICEDATE between to_date(?,'dd/MM/yyyy') and to_date(?,'dd/MM/yyyy')");
					params.add(searchNoticeForm.getFromDate());
					params.add(sdf.format(toDate));
				}
				queryStr.append(" order by NOTICEDATE desc ");
				Query query = HibernateUtil.getCurrentSession().createSQLQuery(
						queryStr.toString());
				int i = 0;
				for (Object param : params) {
					query.setParameter(i, param);
					i++;
				}
				List results = query.list();
				for (Object object : results) {
					Object[] result = (Object[]) object;
					notice = new PtNotice();
					notice.setNoticeNo((String) result[3]);
					notice.setNoticeDate((Date) result[4]);
					searchNoticeList.add(notice);
				}
			}
		} catch (Exception e) {
			LOGGER.info("Exception in getNoticeDetails()--- NoticeDao--"
					+ e.getMessage());
		}
		return searchNoticeList;
	}
}
