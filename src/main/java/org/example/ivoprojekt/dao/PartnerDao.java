package org.example.ivoprojekt.dao;

import org.example.ivoprojekt.api.response.DialPartnerResponse;
import org.example.ivoprojekt.domain.Partner;
import org.example.ivoprojekt.domain.User;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(Partner.class)
@RegisterBeanMapper(DialPartnerResponse.class)
@RegisterBeanMapper(User.class)
public interface PartnerDao {

//    @SqlQuery("""
//            SELECT p.id AS id, (p.name || ', ' || p.township) AS name,
//            (p.street || ', ' || p.postcode || ' ' || p.township) AS address
//            FROM partner p
//            WHERE p.id NOT IN (
//            SELECT u.partner_id
//            FROM user u
//            )
//            """)
//    List<DialPartnerResponse> findAll();

    @SqlQuery("""
        SELECT p.id AS id, (p.name || ', ' || p.township) AS name,
        (p.street || ', ' || p.postcode || ' ' || p.township) AS address
        FROM partner p
        LEFT JOIN user u ON u.partner_id = p.id
        WHERE u.partner_id IS NULL
        """)
    List<DialPartnerResponse> findAll();

    @SqlQuery("SELECT * FROM partner WHERE id = ?")
    Partner findById(Integer id);

    @SqlQuery("SELECT * FROM partner WHERE name = ?")
    Partner findByName(String name);

    @SqlUpdate("""
            INSERT INTO partner (name, type, street, township, postcode, business_id, tax_id, vat) VALUES
            (:name, :type, :street, :township, :postcode, :businessId, :taxId, :vat)
            """)
    @GetGeneratedKeys
    Integer save(@BindBean Partner partner);

    @SqlUpdate(
            """
            UPDATE partner SET
            name = :name, type = :type, street = :street, township = :township, postcode = :postcode, business_id = :businessId, tax_id = :taxId, vat = :vat
            WHERE id = :id
            """)
    void updatePartner(@BindBean Partner partner);

    @SqlUpdate("DELETE FROM partner WHERE id = ?")
    void deletePartner(Integer id);

}
