package org.example.ivoprojekt.dao;

import org.example.ivoprojekt.domain.Material;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(Material.class)
public interface MaterialDao {

    @SqlQuery("SELECT * FROM material")
    List<Material> findAll();

    @SqlQuery("SELECT * FROM material WHERE id = ?")
    Material findById(Integer id);

    @SqlUpdate(
            "INSERT INTO material (name, humidity, coefficient) VALUES " +
                    "(:name, :humidity, :coefficient)")
    Integer save(@BindBean Material material);

    @SqlUpdate("UPDATE material SET name = :name, humidity = :humidity, coefficient = :coefficient WHERE id = :id")
    void update(@BindBean Material material);

    @SqlUpdate("DELETE FROM material WHERE id = ?")
    int deleteById(Integer id);
}
