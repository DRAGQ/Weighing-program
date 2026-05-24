package org.example.ivoprojekt.service;

import org.example.ivoprojekt.api.warning.NotFoundException;
import org.example.ivoprojekt.api.warning.ValidationException;
import org.example.ivoprojekt.domain.Material;
import org.example.ivoprojekt.repository.MaterialRepository;

import java.util.List;

public class MaterialService {
    private final MaterialRepository repository;

    public MaterialService(MaterialRepository repository) {
        this.repository = repository;
    }

    public List<Material> getAllMaterials() {
        List<Material> materials = this.repository.getAllMaterials();
        if (materials == null) {
            throw new NotFoundException("Žiadny materiál nebol nájdený");
        }
        return materials;
    }

    public Material getMaterialById(Integer id) {
        if (id == null) {
            throw new ValidationException("Id materálu je null");
        }
        Material material = this.repository.findById(id);
        if (material == null) {
            throw new NotFoundException("Materiál s id: " + id + " nebol nájdený");
        }
        return material;
    }

    public void saveMaterial(Material material) {
        if (material == null) {
            throw new ValidationException("Materíal je null");
        }
        this.repository.save(material);
    }

    public void updateMaterial(Material material){
        if (material == null) {
            throw new ValidationException("Materíal je null");
        }
        this.repository.update(material);
    }

    public void deleteMaterial(Integer id) {
        if (id == null) {
            throw new ValidationException("Id je null");
        }
        this.repository.delete(id);
    }
}
