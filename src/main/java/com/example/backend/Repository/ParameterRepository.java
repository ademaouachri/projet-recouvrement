package com.example.backend.Repository;

import com.example.backend.Model.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, String> {
    Optional<Parameter> findByCodeParametreAndTypeParametre(String codeParametre, String typeParametre);
}
