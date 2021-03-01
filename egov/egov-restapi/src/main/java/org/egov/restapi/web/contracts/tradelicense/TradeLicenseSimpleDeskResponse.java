package org.egov.restapi.web.contracts.tradelicense;

import static java.io.File.separator;
import static org.apache.commons.io.FileUtils.getUserDirectoryPath;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.egov.infra.config.core.ApplicationThreadLocals.getCityCode;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.tl.entity.LicenseDocument;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.utils.Constants;
import org.springframework.util.FileCopyUtils;

public class TradeLicenseSimpleDeskResponse {

	private Long licenseId;
	private String tin;
	private String applicationNumber;
	private String applicationStatus;
	private String applicationDate;
	private String applicantName;
	private String fatherOrSpouseName;
	private String mobileNumber;
	private String aadharNumber;
	private String emailId;
	private String tradeTitle;
	private String ownershipType;
	private String assessmentNo;
	private String commencementDate;
	private BigDecimal tradeMeasure;
	private String boundary;
	private String parentBoundary;
	private String natureOfBusiness;
	private String subCategory;
	private String category;
	private String tradeAddress;
	private String licenseeAddress;
	private String remarks;
	private Date agreementDate;
	private String agreementDocNo;
	private String source;
	private String classificationType;
	private String employersType;
	private String mandalName;
	private String doorNo;
	private Long directWorkerMale;
	private Long directWorkerFemale;
	private Long contractWorkerMale;
	private Long contractWorkerFemale;
	private Long dailyWagesMale;
	private Long dailyWagesFemale;
	private Long totalWorkers;
	private String isPaymentSucess;
	private List<byte[]> licenseDocuments;
	private String fileStoreBaseDir;
	private String digiSignedCertFilestoreId;
	private String uid;

	public TradeLicenseSimpleDeskResponse(TradeLicense license, String fileStoreBaseDir) throws IOException {
		this.licenseId = license.getId();
		this.tin = license.getLicenseNumber();
		this.applicationNumber = license.getApplicationNumber();
		this.applicationStatus = license.getStatus().getName();
		this.applicationDate = license.getApplicationDate().toString();
		this.applicantName = license.getLicensee().getApplicantName();
		this.fatherOrSpouseName = license.getLicensee().getFatherOrSpouseName();
		this.mobileNumber = license.getLicensee().getMobilePhoneNumber();

		this.emailId = license.getLicensee().getEmailId();
		this.tradeTitle = license.getNameOfEstablishment();
		this.ownershipType = license.getOwnershipType().toString();
		this.assessmentNo = isBlank(license.getAssessmentNo()) ? EMPTY : license.getAssessmentNo();
		this.commencementDate = license.getCommencementDate().toString();
		this.tradeMeasure = license.getTradeArea_weight();
		this.boundary = license.getBoundary().getName();
		this.parentBoundary = license.getParentBoundary().getName();
		this.natureOfBusiness = license.getNatureOfBusiness().getName();
		this.subCategory = license.getTradeName().getName();
		this.category = license.getCategory().getName();
		this.tradeAddress = license.getAddress();
		this.licenseeAddress = license.getLicensee().getAddress();
		this.remarks = license.getRemarks();
		this.agreementDate = license.getAgreementDate();
		this.agreementDocNo = license.getAgreementDocNo();
		this.source = license.getApplicationSource();
		this.classificationType = license.getClassificationType().getName();
		this.employersType = license.getEmployersType().getName();
		this.mandalName = license.getMandalName();
		this.doorNo = license.getDoorNo();
		this.directWorkerMale = license.getDirectWorkerMale();
		this.directWorkerFemale = license.getDirectWorkerFemale();
		this.contractWorkerMale = license.getContractWorkerMale();
		this.contractWorkerFemale = license.getContractWorkerFemale();
		this.dailyWagesMale = license.getDailyWagesMale();
		this.dailyWagesFemale = license.getDailyWagesFemale();
		this.totalWorkers = license.getTotalWorkers();
		if (license.getDemand().getAmtCollected().compareTo(license.getDemand().getBaseDemand()) == 0
				|| license.getDemand().getAmtCollected().compareTo(license.getDemand().getBaseDemand()) == 1) {
			this.isPaymentSucess = "YES";
		} else {
			this.isPaymentSucess = "NO";
		}
		
		this.licenseDocuments = getSupportDocuments(license,fileStoreBaseDir);
		this.digiSignedCertFilestoreId = license.getDigiSignedCertFileStoreId();
		this.uid=license.getUid();
	}

	
	public Long getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(Long licenseId) {
		this.licenseId = licenseId;
	}

	public BigDecimal getTradeMeasure() {
		return tradeMeasure;
	}

	public void setTradeMeasure(BigDecimal tradeMeasure) {
		this.tradeMeasure = tradeMeasure;
	}

	public String getTin() {
		return tin;
	}

	public String getAgreementDocNo() {
		return agreementDocNo;
	}

	public void setAgreementDocNo(String agreementDocNo) {
		this.agreementDocNo = agreementDocNo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getOwnershipType() {
		return ownershipType;
	}

	public void setOwnershipType(String ownershipType) {
		this.ownershipType = ownershipType;
	}

	public void setTin(String tin) {
		this.tin = tin;
	}

	public String getAssessmentNo() {
		return assessmentNo;
	}

	public void setAssessmentNo(String assessmentNo) {
		this.assessmentNo = assessmentNo;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	public String getTradeAddress() {
		return tradeAddress;
	}

	public void setTradeAddress(String tradeAddress) {
		this.tradeAddress = tradeAddress;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getAgreementDate() {
		return agreementDate;
	}

	public void setAgreementDate(Date agreementDate) {
		this.agreementDate = agreementDate;
	}

	public String getCommencementDate() {
		return commencementDate;
	}

	public void setCommencementDate(String commencementDate) {
		this.commencementDate = commencementDate;
	}

	public String getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(String applicationDate) {
		this.applicationDate = applicationDate;
	}

	public String getBoundary() {
		return boundary;
	}

	public void setBoundary(String boundary) {
		this.boundary = boundary;
	}

	public String getParentBoundary() {
		return parentBoundary;
	}

	public void setParentBoundary(String parentBoundary) {
		this.parentBoundary = parentBoundary;
	}

	public String getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	public String getFatherOrSpouseName() {
		return fatherOrSpouseName;
	}

	public void setFatherOrSpouseName(String fatherOrSpouseName) {
		this.fatherOrSpouseName = fatherOrSpouseName;
	}

	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getLicenseeAddress() {
		return licenseeAddress;
	}

	public void setLicenseeAddress(String licenseeAddress) {
		this.licenseeAddress = licenseeAddress;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public String getTradeTitle() {
		return tradeTitle;
	}

	public void setTradeTitle(String tradeTitle) {
		this.tradeTitle = tradeTitle;
	}

	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public String getNatureOfBusiness() {
		return natureOfBusiness;
	}

	public void setNatureOfBusiness(String natureOfBusiness) {
		this.natureOfBusiness = natureOfBusiness;
	}

	public String getClassificationType() {
		return classificationType;
	}

	public void setClassificationType(String classificationType) {
		this.classificationType = classificationType;
	}

	public String getEmployersType() {
		return employersType;
	}

	public void setEmployersType(String employersType) {
		this.employersType = employersType;
	}

	public String getMandalName() {
		return mandalName;
	}

	public void setMandalName(String mandalName) {
		this.mandalName = mandalName;
	}

	public String getDoorNo() {
		return doorNo;
	}

	public void setDoorNo(String doorNo) {
		this.doorNo = doorNo;
	}

	public Long getDirectWorkerMale() {
		return directWorkerMale;
	}

	public void setDirectWorkerMale(Long directWorkerMale) {
		this.directWorkerMale = directWorkerMale;
	}

	public Long getDirectWorkerFemale() {
		return directWorkerFemale;
	}

	public void setDirectWorkerFemale(Long directWorkerFemale) {
		this.directWorkerFemale = directWorkerFemale;
	}

	public Long getContractWorkerMale() {
		return contractWorkerMale;
	}

	public void setContractWorkerMale(Long contractWorkerMale) {
		this.contractWorkerMale = contractWorkerMale;
	}

	public Long getContractWorkerFemale() {
		return contractWorkerFemale;
	}

	public void setContractWorkerFemale(Long contractWorkerFemale) {
		this.contractWorkerFemale = contractWorkerFemale;
	}

	public Long getDailyWagesMale() {
		return dailyWagesMale;
	}

	public void setDailyWagesMale(Long dailyWagesMale) {
		this.dailyWagesMale = dailyWagesMale;
	}

	public Long getDailyWagesFemale() {
		return dailyWagesFemale;
	}

	public void setDailyWagesFemale(Long dailyWagesFemale) {
		this.dailyWagesFemale = dailyWagesFemale;
	}

	public Long getTotalWorkers() {
		return totalWorkers;
	}

	public void setTotalWorkers(Long totalWorkers) {
		this.totalWorkers = totalWorkers;
	}

	public String getIsPaymentSucess() {
		return isPaymentSucess;
	}

	public void setIsPaymentSucess(String isPaymentSucess) {
		this.isPaymentSucess = isPaymentSucess;
	}

	public List<byte[]> getLicenseDocuments() {
		return licenseDocuments;
	}

	public void setLicenseDocuments(List<byte[]> licenseDocuments) {
		this.licenseDocuments = licenseDocuments;
	}
	
	public String getFileStoreBaseDir() {
		return fileStoreBaseDir;
	}

	public void setFileStoreBaseDir(String fileStoreBaseDir) {
		this.fileStoreBaseDir = fileStoreBaseDir;
	}

	public String getDigiSignedCertFilestoreId() {
		return digiSignedCertFilestoreId;
	}

	public void setDigiSignedCertFilestoreId(String digiSignedCertFilestoreId) {
		this.digiSignedCertFilestoreId = digiSignedCertFilestoreId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	private List<byte[]> getSupportDocuments(TradeLicense license, String fileStoreBaseDirConfig) throws IOException {
		List<LicenseDocument> licensedocs = license.getDocuments();
		Set<FileStoreMapper> fileStoreList = null;
		List<byte[]> licenseFileList = new ArrayList<>();
		String fileStoreBaseDir = null;
		 if(fileStoreBaseDirConfig.isEmpty()) {
			 fileStoreBaseDir = getUserDirectoryPath() + separator + "egovfilestore";
		 }else {
			 fileStoreBaseDir = fileStoreBaseDirConfig;
		 } 
		for (LicenseDocument licenseDocument : licensedocs) {
			fileStoreList = licenseDocument.getFiles();
			for (FileStoreMapper fileStoreMapper : fileStoreList) {
				final File file = this.fetch(fileStoreMapper.getFileStoreId(), Constants.TL_FILE_STORE_DIR, fileStoreBaseDir);
				licenseFileList.add(FileCopyUtils.copyToByteArray(file));
			}
		}
		return licenseFileList;
	}

	private File fetch(String fileStoreId, String moduleName, String fileStoreBaseDir) {
		return this.fetchAsPath(fileStoreId, moduleName, fileStoreBaseDir).toFile();
	}

	private Path fetchAsPath(String fileStoreId, String moduleName, String fileStoreBaseDir) {
		Path fileDirPath = this.getFileDirectoryPath(moduleName, fileStoreBaseDir);
		if (!fileDirPath.toFile().exists())
			throw new ApplicationRuntimeException(String.format("File Store does not exist at Path : %s/%s/%s",
					fileStoreBaseDir, getCityCode(), moduleName));
		return this.getFilePath(fileDirPath, fileStoreId);
	}

	private Path getFilePath(Path fileDirPath, String fileStoreId) {
		return Paths.get(fileDirPath + separator + fileStoreId);
	}

	private Path getFileDirectoryPath(String moduleName, String fileStoreBaseDir) {
		return Paths.get(new StringBuilder().append(fileStoreBaseDir).append(separator).append(getCityCode())
				.append(separator).append(moduleName).toString());
	}

}
