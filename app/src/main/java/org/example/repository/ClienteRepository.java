package org.example.repository;

import org.example.entity.Clientes;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Clientes, Long> {

    // Buscar por data específica
    List<Clientes> findByDataCriacao(LocalDateTime dataCriacao);

    @Query("SELECT c FROM Clientes c WHERE c.dataCriacao BETWEEN :inicio AND :fim")
    List<Clientes> buscarPorDataCriacao(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    List<Clientes> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT c FROM Clientes c WHERE c.nome LIKE %:nome% AND c.dataCriacao BETWEEN :inicio AND :fim")
    List<Clientes> buscarPorNomeEData(@Param("nome") String nome, @Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    boolean existsByEmail(String email); // 🔹 Agora permite verificar se o email já existe

    long countByDataCriacaoBetween(LocalDateTime inicio, LocalDateTime fim);
    long countByDataAtualizacaoBetween(LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT c FROM Clientes c WHERE c.dataCriacao >= :inicio AND c.dataAtualizacao IS NOT NULL")
    List<Clientes> buscarCriadosEAtualizados(@Param("inicio") LocalDateTime inicio);

    @Query("SELECT c FROM Clientes c WHERE c.dataAtualizacao BETWEEN :inicio AND :fim")
    List<Clientes> buscarPorAtualizacao(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT c FROM Clientes c WHERE c.dataAtualizacao IS NOT NULL")
    List<Clientes> buscarSomenteAtualizados();

}

