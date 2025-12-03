package com.vinotech.sommelier_api.service;

import com.vinotech.sommelier_api.model.CouleurVin;
import com.vinotech.sommelier_api.model.Vin;
import com.vinotech.sommelier_api.repository.VinRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;

@Service // Indique à Spring que c'est une classe de logique métier
public class VinService {

    private final VinRepository vinRepository;

    // Injection de Dépendance : Spring fournit automatiquement l'implémentation du Repository
    public VinService(VinRepository vinRepository) {
        this.vinRepository = vinRepository;
    }

    /**
     * Enregistre ou met à jour un vin dans la base de données.
     */
    public Vin save(Vin vin) {
        // Le Repository traduit l'opération en SQL (INSERT ou UPDATE)
        return vinRepository.save(vin);
    }

    /**
     * Récupère tous les vins.
     */
    public List<Vin> findAll() {
        return vinRepository.findAll();
    }

    /**
     * Récupère un vin par son ID.
     */
    public Optional<Vin> findById(Long id) {
        return vinRepository.findById(id);
    }

    /**
     * Recherche avancée avec critères dynamiques.
     */
    public Page<Vin> searchVins(CouleurVin couleur, Double minPrix, Double maxPrix, String region, Pageable pageable) {
        org.springframework.data.jpa.domain.Specification<Vin> spec = (root, query, criteriaBuilder) -> {
            java.util.List<jakarta.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();

            if (couleur != null) {
                predicates.add(criteriaBuilder.equal(root.get("couleur"), couleur));
            }
            if (minPrix != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("prix"), minPrix));
            }
            if (maxPrix != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("prix"), maxPrix));
            }
            if (region != null && !region.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("region")), "%" + region.toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        // On passe l'objet pageable au repository qui gère ça nativement
        return vinRepository.findAll(spec, pageable);
    }
}