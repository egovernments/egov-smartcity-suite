package org.egov.infra.filestore.repository;

import org.egov.infra.filestore.entity.FileStoreMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileStoreMapperRepository extends JpaRepository<FileStoreMapper,Long>{
    FileStoreMapper findByFileStoreId(String fileStoreId);
}
