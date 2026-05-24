package org.example.ivoprojekt.repository;

import org.example.ivoprojekt.api.request.WeighingOverviewRequest;
import org.example.ivoprojekt.api.response.WeighingPrintResponse;
import org.example.ivoprojekt.api.response.WeighingTableOverviewResponse;
import org.example.ivoprojekt.api.response.WeighingTableResponse;
import org.example.ivoprojekt.api.response.WeighingUpdateResponse;
import org.example.ivoprojekt.api.warning.DatabaseException;
import org.example.ivoprojekt.dao.WeighingDao;
import org.example.ivoprojekt.domain.Weighing;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.JdbiException;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class WeighingRepository {
    WeighingDao weighingDao;

    private static final DateTimeFormatter DB_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter UI_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public WeighingRepository(Jdbi jdbi) {
        this.weighingDao = jdbi.onDemand(WeighingDao.class);
    }

    public List<WeighingTableResponse> getAllWeighingForTable(String chosenTimePeriodStart, String chosenTimePeriodEnd) {
        try {
            List<WeighingTableResponse> weighing = weighingDao.findAllForTable(chosenTimePeriodStart, chosenTimePeriodEnd);
            return weighing;
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to fetch all weighings", e);
        }
    }

    public WeighingUpdateResponse getWeighingByNumber(int number) {
        try {
            return weighingDao.getWeighingByNumber(number);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to fetch weighing by number: " + number, e);
        }
    }

    public WeighingPrintResponse getWeighingPrintResponse(int number) {
        try {
            return weighingDao.getWeighingForPrintByNumber(number);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to fetch weighing print response by number: " + number, e);
        }
    }

    public List<WeighingTableOverviewResponse> getWeighingTableRows(WeighingOverviewRequest request) {
        try {
            return weighingDao.getWeighingTableRows(request);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to fetch weighings for table rows", e);
        }
    }

    public int generateWeighingNumber(String date) {
        try {
            return this.weighingDao.getNumberOfRowsByDate(date);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to generate weighing number", e);
        }
    }

    /*public void save(Weighing weighing) {
        try {
            this.weighingDao.save(weighing);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to save weighing", e);
        }
    }*/

    public void save(Weighing weighing) {
        try {
            this.weighingDao.save(weighing);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to save weighing", e);
        }
    }

    public void update(Weighing weighing) {
        try {
            this.weighingDao.update(weighing);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to update weighing", e);
        }
    }

    public void delete(int number) {
        try {
            this.weighingDao.delete(number);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to delete weighing", e);
        }
    }

}
