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
package org.egov.ptis.domain.dao.property;

import org.apache.log4j.Logger;
import org.egov.ptis.notice.PtNotice;
import org.egov.ptis.notice.SearchNoticeForm;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Repository(value = "noticeDAO")
@Transactional(readOnly = true)
public class NoticeDAO {

	public final static Logger LOGGER = Logger.getLogger(NoticeDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	private Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	public boolean saveNoticeDetails(PtNotice notice, InputStream noticeDocFis) {
		boolean isNoticeSaved = false;
		try {
			Query query = getCurrentSession()
					.createSQLQuery(
							"insert into EGPT_NOTICE (ID,ID_MODULE,NOTICETYPE,NOTICENO,NOTICEDATE,ID_USER) "
									+ "values (SEQ_EGPT_NOTICE.nextval,?,?,?,sysdate,?")
					.setParameter(0, notice.getModuleId()).setParameter(1, notice.getNoticeType())
					.setParameter(2, notice.getNoticeNo()).setParameter(3, notice.getUserId());
			query.executeUpdate();
			isNoticeSaved = true;
		} catch (Exception e) {
			LOGGER.info("Exception in saveNoticeDetails()--- NoticeDao--" + e.getMessage());
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
			List results = getCurrentSession()
					.createSQLQuery(
							"select DOCUMENT,IS_BLOB,DOCUMENT1 from notice where NOTICENO = ? and OBJECTNO = ?")
					.setParameter(0, noticeNo).setParameter(1, objectNo).list();
			for (Object result : results) {
				Object[] objects = (Object[]) result;
				isBlob = (String) objects[1];
				if (isBlob != null && isBlob.equals("Y")) {
					istream = (InputStream) objects[2];
				} else if ((isBlob == null) || (isBlob != null && isBlob.equals("N"))) {
					istream = (InputStream) objects[0];
				}
			}
		} catch (Exception e) {
			LOGGER.info("Exception in getNoticeDocument()--- NoticeDao--" + e.getMessage());
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
				Query query = getCurrentSession().createSQLQuery(queryStr.toString());
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
			LOGGER.info("Exception in getNoticeDetails()--- NoticeDao--" + e.getMessage());
		}
		return searchNoticeList;
	}
}
