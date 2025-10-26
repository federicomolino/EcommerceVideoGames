package com.videogames.videogames.Repository;

import com.videogames.videogames.Entity.Parametro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParametroRepository extends JpaRepository<Parametro, Integer> {

    @Query("SELECT p FROM Parametro p WHERE p.codiceValore = :codiceValore")
    Optional<Parametro> findParametroByCodiceValore(@Param("codiceValore") String codiceValore);

    @Query("SELECT p FROM Parametro p WHERE p.codiceValore IN :codiciValore")
    List<Parametro> findParametroByCodiciValore(@Param("codiciValore") List<String> codiceValore);

    List<Parametro> findBycodiceValoreContainingIgnoreCase(String parametro);
}
