package org.example.ivoprojekt.dao;

import org.example.ivoprojekt.domain.Vehicle;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(Vehicle.class)
public interface VehicleDao {

    @SqlQuery("SELECT * FROM vehicle")
    List<Vehicle> findAll();

    @SqlQuery("SELECT tara FROM vehicle WHERE id = ?")
    Double getTaraById(Integer selectedId);

    @SqlUpdate(
            "INSERT INTO vehicle (identification_number, description, tara) VALUES " +
                    "(:identificationNumber, :description, :tara)")
    @GetGeneratedKeys
    Integer save(@BindBean Vehicle vehicle);

    @SqlUpdate("UPDATE vehicle SET identification_number = ?, description = ?, tara = ? WHERE id = ?")
    void updateVehicle(String number, String text, Double tara, Integer id);

    @SqlUpdate("UPDATE vehicle SET tara = ? WHERE id = ?")
    void updateTara(Double tara, Integer id);

    @SqlUpdate("DELETE FROM vehicle WHERE id = ?")
    void deleteVehicle(Integer id);
}
