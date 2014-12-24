package org.egov.ptis.domain.dao.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.infstr.utils.EGovConfig;
import org.egov.ptis.notice.PtNotice;
import org.egov.ptis.notice.SearchNoticeForm;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NoticeDAOTest {
	private EgovHibernateTest egovHibernateTest;
	private NoticeDAO noticeDao;
	private ModuleDao moduleDao;
	Module module;
	private boolean notice;
	private InputStream istream;
	static String noticeNo;

	@Before
	public void setUp() throws Exception {
		egovHibernateTest = new EgovHibernateTest();
		noticeDao = new NoticeDAO();
		moduleDao = GenericDaoFactory.getDAOFactory().getModuleDao();
		module = moduleDao.getModuleByName(EGovConfig.getProperty("ptis_egov_config.xml",
				"MODULE_NAME", "", "PT"));
		egovHibernateTest.setUp();
	}

	@After
	public void tearDown() throws Exception {
		moduleDao = null;
		module = null;
		noticeDao = null;
		egovHibernateTest.tearDown();
	}

	// @Test
	public void saveNoticeDetails() throws FileNotFoundException {
		noticeNo = Integer.valueOf((int) (Math.random() * 9999) + 1000).toString();
		String tempNoticeNo = "JUNITNOTICE/09-10/" + noticeNo;
		PtNotice noticeForm = getNotice(tempNoticeNo);
		File file = new File(getFilePath() + "//NoticeTestDoc.html");
		InputStream iStream = new FileInputStream(file);
		notice = noticeDao.saveNoticeDetails(noticeForm, iStream);
		assertEquals(true, notice);
	}

	private String getFilePath() {
		String filePath = "";
		String classPath = System.getProperty("java.class.path");
		boolean checkForColSemi = StringUtils.contains(classPath, ':');
		boolean temp = StringUtils.contains(classPath, ';');
		if (temp) {
			checkForColSemi = false;
		}
		String classPathArray[];
		if (checkForColSemi) {
			classPathArray = classPath.split(":");
		} else {
			classPathArray = classPath.split(";");
		}

		List<String> classPathList = Arrays.asList(classPathArray);
		for (String str : classPathList) {
			if (checkForColSemi) {
				if (StringUtils.contains(str, "PTJAVA/build/classes")) {
					filePath = str;
				}
			} else {
				if (StringUtils.contains(str, "PTJAVA\\build\\classes")) {
					filePath = str;
				}
			}
		}
		filePath = StringUtils.substringBefore(filePath, "classes");
		filePath = filePath + "classes";
		return filePath;
	}

	private PtNotice getNotice(String noticeNo) {
		PtNotice noticeForm = new PtNotice();
		noticeForm.setModuleId(module.getId());
		noticeForm.setNoticeType("JUNITNOTICE");
		noticeForm.setNoticeNo(noticeNo);
		/*noticeForm.setObjectNo("08-119-0000-000");
		noticeForm.setAddressTo("Junit Notice");
		noticeForm.setAddress("Junit address");*/
		noticeForm.setUserId(1);
		return noticeForm;
	}

	@Test
	public void getNoticeDocumentInputNullNotice() {
		istream = noticeDao.getNoticeDocument(null, null);
		assertNull(istream);
	}

	@Test
	public void getNoticeDocumentInputEmptyNotice() {
		istream = noticeDao.getNoticeDocument("", "");
		assertNull(istream);
	}

	// @Test
	public void getNoticeDocument() throws FileNotFoundException {
		String tempNoticeNo = "JUNITNOTICE/09-10/" + noticeNo;
		PtNotice noticeForm = getNotice(tempNoticeNo);
		File file = new File(getFilePath() + "//NoticeTestDoc.html");
		InputStream iStream = new FileInputStream(file);
		noticeDao.saveNoticeDetails(noticeForm, iStream);
		istream = noticeDao.getNoticeDocument(tempNoticeNo, "08-119-0000-000");
		assertNotNull(istream);
	}

	@Test
	public void saveNoticeDetailsInputNull() {
		notice = noticeDao.saveNoticeDetails(null, null);
		assertEquals(false, notice);
	}

	@Test
	public void saveNoticeDetailsInputNullDocument() throws FileNotFoundException {
		String tempNoticeNo = "JUNITNOTICE/09-10/" + noticeNo;
		PtNotice noticeForm = getNotice(tempNoticeNo);

		notice = noticeDao.saveNoticeDetails(noticeForm, null);
		assertEquals(false, notice);
	}

	@Test
	public void saveNoticeDetailsInputNullNoticeForm() throws FileNotFoundException {
		File file = new File(getFilePath() + "//NoticeTestDoc.html");
		InputStream iStream = new FileInputStream(file);
		notice = noticeDao.saveNoticeDetails(null, iStream);
		assertEquals(false, notice);
	}

	@Test
	public void getNoticeDetailsNull() {
		List<PtNotice> noticeFormList;
		noticeFormList = noticeDao.getNoticeDetails(null);
		assertEquals(0, noticeFormList.size());
	}

	// @Test
	public void getNoticeDetailsByAll() throws FileNotFoundException {
		List<PtNotice> noticeFormList;
		String tempNoticeNo = "JUNITNOTICE/09-10/" + noticeNo;
		PtNotice noticeForm = getNotice(tempNoticeNo);
		File file = new File(getFilePath() + "//NoticeTestDoc.html");
		InputStream iStream = new FileInputStream(file);
		noticeDao.saveNoticeDetails(noticeForm, iStream);

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SearchNoticeForm searchnoticeForm = new SearchNoticeForm();
		searchnoticeForm.setNoticeNumber("JUNITNOTICE/09-10/" + noticeNo);
		searchnoticeForm.setBillNumber("08-119-0000-000");
		searchnoticeForm.setFromDate(sdf.format(new Date()));
		searchnoticeForm.setToDate(sdf.format(new Date()));
		searchnoticeForm.setNoticeType("JUNITNOTICE");

		noticeFormList = noticeDao.getNoticeDetails(searchnoticeForm);
		assertEquals(1, noticeFormList.size());
	}

	// @Test
	public void getNoticeDetailsByNoticeNo() throws FileNotFoundException {
		List<PtNotice> noticeFormList;
		String tempNoticeNo = "JUNITNOTICE/09-10/" + noticeNo;
		PtNotice noticeForm = getNotice(tempNoticeNo);
		File file = new File(getFilePath() + "//NoticeTestDoc.html");
		InputStream iStream = new FileInputStream(file);
		noticeDao.saveNoticeDetails(noticeForm, iStream);

		SearchNoticeForm searchnoticeForm = new SearchNoticeForm();
		searchnoticeForm.setNoticeNumber("JUNITNOTICE/09-10/" + noticeNo);
		searchnoticeForm.setNoticeType("JUNITNOTICE");

		noticeFormList = noticeDao.getNoticeDetails(searchnoticeForm);
		assertEquals(1, noticeFormList.size());
	}

	// @Test
	public void getNoticeDetailsByBillNo() throws FileNotFoundException {
		List<PtNotice> noticeFormList;
		String tempNoticeNo = "JUNITNOTICE/09-10/" + noticeNo;
		PtNotice notice = getNotice(tempNoticeNo);
		File file = new File(getFilePath() + "//NoticeTestDoc.html");
		InputStream iStream = new FileInputStream(file);
		noticeDao.saveNoticeDetails(notice, iStream);

		SearchNoticeForm searchnoticeForm = new SearchNoticeForm();
		searchnoticeForm.setBillNumber("08-119-0000-000");
		searchnoticeForm.setNoticeType("JUNITNOTICE");

		noticeFormList = noticeDao.getNoticeDetails(searchnoticeForm);
		assertEquals(1, noticeFormList.size());
	}

	public void getNoticeDetailsByDate() throws FileNotFoundException {
		List<PtNotice> noticeFormList;
		String tempNoticeNo = "JUNITNOTICE/09-10/" + noticeNo;
		PtNotice noticeForm = getNotice(tempNoticeNo);
		File file = new File(getFilePath() + "//NoticeTestDoc.html");
		InputStream iStream = new FileInputStream(file);
		noticeDao.saveNoticeDetails(noticeForm, iStream);

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SearchNoticeForm searchnoticeForm = new SearchNoticeForm();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(
				Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE) - 1);
		searchnoticeForm.setFromDate(sdf.format(calendar.getTimeInMillis()));
		searchnoticeForm.setToDate(sdf.format(new Date()));
		searchnoticeForm.setNoticeType("JUNITNOTICE");

		noticeFormList = noticeDao.getNoticeDetails(searchnoticeForm);
		assertEquals(1, noticeFormList.size());
	}

}
