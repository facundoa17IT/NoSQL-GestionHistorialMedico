package utu.nosql.tarea2.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import utu.nosql.tarea2.entity.RegistroMedico;

public interface RegistroMedicoRepository extends MongoRepository<RegistroMedico, String> {
    List<RegistroMedico> findByCiPacienteOrderByFechaDesc(String ciPaciente, Pageable pageable);
}
