package org.example.ivoprojekt.repository;

import org.example.ivoprojekt.api.warning.DatabaseException;
import org.example.ivoprojekt.dao.VehicleDao;
import org.example.ivoprojekt.domain.Vehicle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.JdbiException;

import java.util.List;

public class VehicleRepository {
    VehicleDao vehicleDao;

    public VehicleRepository(Jdbi jdbi) {
        this.vehicleDao = jdbi.onDemand(VehicleDao.class);
    }

    public List<Vehicle> getAllVehicles() {
        try {
            return vehicleDao.findAll();
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to fetch all vehicles", e);
        }
    }

    public Double getTaraById(Integer id) {
        try {
            return vehicleDao.getTaraById(id);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to fetch vehicle by id: " + id, e);
        }
    }

    public void save(Vehicle vehicle) {
        try {
            vehicleDao.save(vehicle);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to save vehicle", e);
        }
    }

    public void update(Integer id, String number, String text, Double tara) {
        try {
            vehicleDao.updateVehicle(number, text, tara, id);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to update vehicle", e);
        }
    }

    public void updateTara(Integer id, Double tara) {
        try {
            vehicleDao.updateTara(tara, id);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to update tara", e);
        }
    }

    public void delete(Integer id) {
        try {
            vehicleDao.deleteVehicle(id);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to delete tara", e);
        }
    }

}
