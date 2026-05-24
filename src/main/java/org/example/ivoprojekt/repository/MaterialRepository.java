package org.example.ivoprojekt.repository;

import org.example.ivoprojekt.api.warning.DatabaseException;
import org.example.ivoprojekt.dao.MaterialDao;
import org.example.ivoprojekt.domain.Material;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.JdbiException;

import java.util.List;

public class MaterialRepository {
    private final MaterialDao materialRepository;

    public MaterialRepository(Jdbi jdbi) {
        this.materialRepository = jdbi.onDemand(MaterialDao.class);
    }

    public List<Material> getAllMaterials() {
        try {
            return materialRepository.findAll();
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to fetch materials", e);
        }
    }

    public Material findById(Integer id) {
        try {
        return materialRepository.findById(id);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to fetch material by id", e);
        }
    }

    public void save(Material material) {
        try {
            materialRepository.save(material);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to save material", e);
        }
    }

    public void update(Material material) {
        try {
        materialRepository.update(material);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to update material", e);
        }
    }

    public void delete(Integer id) {
        try {
        materialRepository.deleteById(id);
        } catch (JdbiException e) {
            throw new DatabaseException("Failed to delete material", e);
        }
    }
}
