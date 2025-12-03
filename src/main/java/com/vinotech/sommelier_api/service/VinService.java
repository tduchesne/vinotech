package com.vinotech.sommelier_api.service;

import com.vinotech.sommelier_api.model.CouleurVin;
import com.vinotech.sommelier_api.model.Vin;
import com.vinotech.sommelier_api.repository.VinRepository;
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
    public List<Vin> searchVins(CouleurVin couleur, Double minPrix, Double maxPrix, String region) {
        // On construit la "Spécification" (la règle de filtrage)
        Specification<Vin> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Filtre Couleur (Exact)
            if (couleur != null) {
                predicates.add(criteriaBuilder.equal(root.get("couleur"), couleur));
            }

            // Vérification cohérence des prix
            if (minPrix != null && maxPrix != null && minPrix > maxPrix) {
                throw new IllegalArgumentException("minPrix cannot be greater than maxPrix");
            }
            // 2. Filtre Prix Min (Supérieur ou égal)
            if (minPrix != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("prix"), minPrix));
            }

            // 3. Filtre Prix Max (Inférieur ou égal)
            if (maxPrix != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("prix"), maxPrix));
            }

            // 4. Filtre Région (Partiel & Insensible à la casse, ex: "bourgogne" trouve "France (Bourgogne)")
            if (region != null && !region.isBlank()) {
                String regionPattern = "%" + region.toLowerCase(java.util.Locale.ROOT) + "%";
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("region")), regionPattern));
            }

            // On combine tous les critères avec "AND"
            // On combine tous les critères avec "AND", ou une conjonction "true" s'il n'y a aucun filtre
            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // On exécute la requête via le Repository
        return vinRepository.findAll(spec);
    }
}