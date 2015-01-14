package org.egov.pgr.repository;

import java.util.List;

import org.egov.pgr.entity.FileAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long> {
	
	List<FileAttachment> findByReferenceId(String referenceId);
	
	FileAttachment findOneByFilenameAndReferenceId(String filename, String referenceId);
}
