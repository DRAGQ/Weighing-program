package org.example.ivoprojekt.service;

import javafx.scene.control.Alert;
import org.example.ivoprojekt.api.warning.DatabaseException;
import org.example.ivoprojekt.api.warning.NotFoundException;
import org.example.ivoprojekt.api.warning.ValidationException;
import org.example.ivoprojekt.repository.PartnerRepository;
import org.example.ivoprojekt.api.response.DialPartnerResponse;
import org.example.ivoprojekt.api.warning.WarningAlert;
import org.example.ivoprojekt.domain.Partner;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import java.util.List;

public class PartnerService {
    private final PartnerRepository repository;

    public PartnerService(PartnerRepository repository) {
        this.repository = repository;
    }

    public List<DialPartnerResponse> getAllPartners() {
        List<DialPartnerResponse> partners = repository.getAllPartners();
        if (partners == null) {
            throw new NotFoundException("Žiadny partner nebol nájdený");
        }
        return partners;
    }

    public Partner getPartnerById(Integer id) {
        if  (id == null) {
            throw new ValidationException("Id partnera je null");
        }
        Partner partner = repository.getPartnerById(id);
        if (partner == null) {
            throw new NotFoundException("Partner s id: " + id + " nebol nájdený");
        }
        return partner;
    }

    public Integer savePartner(Partner partner) {
        if (partner == null) {
            throw new ValidationException("Partner je null");
        }

        return repository.save(partner);
    }

    public void updatePartner(Partner partner) {
        if (partner == null) {
            throw new ValidationException("Partner je null");
        }
        repository.update(partner);
    }

    public void deletePartner(Integer id) {
        if (id == null) {
            throw new ValidationException("Id je null");
        }
        repository.delete(id);
    }

}
