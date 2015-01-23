package org.egov.pgr.repository;

import org.egov.pgr.entity.FileAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//@Repository
public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long> {
	
	List<FileAttachment> findByReferenceId(String referenceId);
	
	FileAttachment findOneByFilenameAndReferenceId(String filename, String referenceId);
}
