package org.example.ivoprojekt.dao;

import org.example.ivoprojekt.api.request.WeighingOverviewRequest;
import org.example.ivoprojekt.api.response.WeighingPrintResponse;
import org.example.ivoprojekt.api.response.WeighingTableOverviewResponse;
import org.example.ivoprojekt.api.response.WeighingTableResponse;
import org.example.ivoprojekt.api.response.WeighingUpdateResponse;
import org.example.ivoprojekt.domain.Weighing;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(Weighing.class)
@RegisterBeanMapper(WeighingTableResponse.class)
@RegisterBeanMapper(WeighingUpdateResponse.class)
@RegisterBeanMapper(WeighingPrintResponse.class)
@RegisterBeanMapper(WeighingTableOverviewResponse.class)
public interface WeighingDao {

    @SqlQuery("""
            SELECT number, (local_date || ' ' || local_time_entry) AS dateTime, gross, weighing.tara, nett, weighing.description,
            (partner.name || ', ' || partner.township) AS partnerName, vehicle.identification_number AS vehicleIdentificationNumber, user.name AS issuedName, material.name AS material,
                CASE
                    WHEN weighing.type = 1 THEN 'Príjemka'
                    ELSE 'Výdajka'
                END AS type
            FROM weighing
            INNER JOIN partner ON partner.id = weighing.partner_id
            INNER JOIN vehicle ON vehicle.id = weighing.vehicle_id
            INNER JOIN user ON user.id = weighing.user_id
            INNER JOIN material ON material.id = weighing.material_id
            WHERE local_date >= ? AND local_date < ?
            """)
    List<WeighingTableResponse> findAllForTable(String chosenTimePeriodStart, String chosenTimePeriodEnd);

    @SqlQuery("""
            SELECT number, local_date AS localDate, local_time_entry AS localTimeEntry, local_time_departure AS localTimeDeparture,
            gross, weighing.tara, nett, weighing.description, (partner.name || ', ' || partner.township) AS partnerName, vehicle.identification_number AS vehicleIdentificationNumber,
            user.name AS issuedName, material.name AS material
            FROM weighing
            INNER JOIN partner ON partner.id = weighing.partner_id
            INNER JOIN vehicle ON vehicle.id = weighing.vehicle_id
            INNER JOIN user ON user.id = weighing.user_id
            INNER JOIN material ON material.id = weighing.material_id
            WHERE number = ?
            """)
    WeighingUpdateResponse getWeighingByNumber(int number);

    @SqlQuery("""
            SELECT userInfo.name AS userName, userInfo.street AS userStreet, userInfo.postcode AS userPostcode, userInfo.township AS userTownship,
            partner.name AS partnerName, partner.street AS partnerStreet, partner.postcode AS partnerPostcode,
            partner.township AS partnerTownship, local_date AS localDate, local_time_entry AS localTimeEntry,
            local_time_departure AS localTimeDeparture, vehicle.identification_number AS identificationNumber, material.name AS material,
            number, gross, weighing.tara, nett, weighing.description, weighing.type
            FROM weighing
            INNER JOIN partner ON partner.id = weighing.partner_id
            INNER JOIN user ON user.id = weighing.user_id
            INNER JOIN partner AS userInfo ON userInfo.id = user.partner_id
            INNER JOIN vehicle ON vehicle.id = weighing.vehicle_id
            INNER JOIN material ON material.id = weighing.material_id
            WHERE number = ?
            """)
    WeighingPrintResponse getWeighingForPrintByNumber(int number);

    // public WeighingTableOverviewResponse(String dateStart, String dateEnd, String type, String partner, String vehicle, String issued, String material, String description) {
    //  PODLA TOHOTO TO UROBIM NA ZAKLADE TYCH PODMIENOK TO DAM ESTE ZISTIT CI TO BUDE FUNGOVAT KED TAM BUDE NULL ALEBO PRAZCNY STRING


    @SqlQuery("""
            SELECT local_date AS dateTime, local_time_entry AS entry, local_time_departure AS departure, number,
                CASE
                    WHEN weighing.type = 1 THEN 'Príjem'
                    ELSE 'Výdaj'
                END AS type,
                (partner.name || ', ' || partner.township) AS partner, vehicle.identification_number AS vehicle, material.name AS material, gross, weighing.tara, nett
            FROM weighing
            INNER JOIN partner ON partner.id = weighing.partner_id
            INNER JOIN vehicle ON vehicle.id = weighing.vehicle_id
            INNER JOIN material ON material.id = weighing.material_id
            INNER JOIN user ON user.id = weighing.user_id
            WHERE (local_date || ' ' || local_time_departure) >= (:dateStart || ' ' || :timeStart) AND (local_date || ' ' || local_time_entry) <= (:dateEnd || ' ' || :timeEnd) AND
                        (:type IS NULL OR weighing.type = :type) AND (:partnerName IS NULL OR (partner.name || ', ' || partner.township) = :partnerName) AND
                        (:vehicleName IS NULL OR vehicle.identification_number = :vehicleName) AND (:userName IS NULL OR user.name = :userName) AND (:materialName IS NULL OR material.name = :materialName)
            """)
            List<WeighingTableOverviewResponse> getWeighingTableRows(@BindBean WeighingOverviewRequest request);

    @SqlQuery("SELECT COUNT(*) FROM weighing WHERE number LIKE ?")
    Integer getNumberOfRowsByDate(String formattedDate);

    @SqlUpdate("""
            INSERT INTO weighing (
                number, type, local_date, local_time_entry, local_time_departure, gross, tara, nett, description, user_id, partner_id, vehicle_id, material_id) VALUES
                (:number, :type, :localDate, :localTimeEntry, :localTimeDeparture, :gross, :tara, :nett, :description, :userId, :partnerId, :vehicleId, :materialId)
            """)
    @GetGeneratedKeys
    Integer save(@BindBean Weighing weighing);

    @SqlUpdate("""
            UPDATE weighing SET
                local_time_entry = :localTimeEntry, local_time_departure = :localTimeDeparture, gross = :gross,
                tara = :tara, nett = :nett, description = :description, user_id = weighing.user_id, partner_id = COALESCE(:partnerId, partner_id),
                vehicle_id = COALESCE(:vehicleId, vehicle_id), material_id = COALESCE(:materialId, material_id)
                WHERE number = :number
            """
    )
    int update(@BindBean Weighing weighing);

    @SqlUpdate("DELETE FROM weighing WHERE number = ?")
    void delete(int number);
}