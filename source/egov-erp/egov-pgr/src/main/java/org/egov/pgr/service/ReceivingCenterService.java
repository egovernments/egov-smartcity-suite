package org.egov.pgr.service;

import java.util.List;

import org.egov.pgr.entity.ReceivingCenter;
import org.egov.pgr.repository.ReceivingCenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReceivingCenterService {

    private final ReceivingCenterRepository receivingCenterRepository;

    @Autowired
    public ReceivingCenterService(final ReceivingCenterRepository receivingCenterRepository) {
        this.receivingCenterRepository = receivingCenterRepository;
    }

    public ReceivingCenter findBy(final Long receivingCenterId) {
        return receivingCenterRepository.get(receivingCenterId);
    }

    public List<ReceivingCenter> findAll() {
        return receivingCenterRepository.findAll();
    }

    public ReceivingCenter load(final Long receivingCenterId) {
        return receivingCenterRepository.load(receivingCenterId);
    }
}
