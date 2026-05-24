package org.example.ivoprojekt.service;

import org.example.ivoprojekt.api.warning.NotFoundException;
import org.example.ivoprojekt.api.warning.ValidationException;
import org.example.ivoprojekt.domain.Vehicle;
import org.example.ivoprojekt.dao.VehicleDao;
import org.example.ivoprojekt.repository.VehicleRepository;
import org.jdbi.v3.core.Jdbi;
import java.util.List;

public class VehicleService {
    VehicleRepository repository;

    public VehicleService(VehicleRepository repository) {
        this.repository = repository;
    }

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles =  repository.getAllVehicles();
        if (vehicles == null) {
            throw new NotFoundException("Žiadne vozidlo nebolo nájdené");
        }
        return vehicles;
    }

    public Double getTaraById(Integer id) {
        if (id == null) {
            throw new ValidationException("Id vozidla je null");
        }
        Double tara = repository.getTaraById(id);
        if (tara == null) {
            throw new NotFoundException("Vozidlo s id: " + id + " nebol nájdený");
        }
        return tara;
    }

    public void saveVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new ValidationException("Vozidlo je null");
        }
        repository.save(vehicle);
    }

    public void updateVehicle(Integer id, String number, String text, Double tara) {
        if (id == null || number == null || text == null || tara == null) {
            throw new ValidationException("Id, číslo, text alebo tara vozidla je null");
        }
        repository.update(id, number, text, tara);
    }

    public void updateTara(Integer id, Double tara) {
        if (id == null || tara == null) {
            throw new ValidationException("Id alebo tara vozidla je null");
        }
        repository.updateTara(id, tara);
    }

    public void deleteVehicle(Integer id) {
        if (id == null) {
            throw new ValidationException("Id vozidla je null");
        }
        repository.delete(id);
    }

}
