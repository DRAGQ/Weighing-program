package org.example.ivoprojekt.repository;

import javafx.scene.control.Alert;
import org.example.ivoprojekt.api.response.DialPartnerResponse;
import org.example.ivoprojekt.api.warning.DatabaseException;
import org.example.ivoprojekt.api.warning.WarningAlert;
import org.example.ivoprojekt.dao.PartnerDao;
import org.example.ivoprojekt.domain.Partner;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.JdbiException;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import java.util.List;

public class PartnerRepository {
    private final PartnerDao partnerDao;

    public PartnerRepository(Jdbi jdbi) {
        this.partnerDao = jdbi.onDemand(PartnerDao.class);
    }

    public List<DialPartnerResponse> getAllPartners() {
        try {
        return partnerDao.findAll();
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to fetch all partners", e);
        }
    }

    public Partner getPartnerById(Integer id) {
        try {
            return partnerDao.findById(id);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to fetch partner by id", e);
        }
    }

    public Integer save(Partner partner) {
        try {
            return partnerDao.save(partner);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to save partner", e);
        }
    }

    public void update(Partner partner) {
        try {
            partnerDao.updatePartner(partner);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to update partner", e);
        }
    }

    public void delete(Integer id) {
        try {
            partnerDao.deletePartner(id);
        } catch (JdbiException e) {
                throw new DatabaseException("Failed to delete partner", e);
            }
    }

}
