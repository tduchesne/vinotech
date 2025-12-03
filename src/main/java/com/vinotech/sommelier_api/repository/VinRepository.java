// Les JPA Repository permettent d'interagir avec la BD sans utiliser de commandes SQL brutes.
package com.vinotech.sommelier_api.repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.vinotech.sommelier_api.model.Vin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// JpaRepository hérite automatiquement des méthodes CRUD pour l'Entité Vin (ID de type Long)
public interface VinRepository extends JpaRepository<Vin, Long>, JpaSpecificationExecutor<Vin> {
}