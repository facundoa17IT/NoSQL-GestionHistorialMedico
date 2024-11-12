package utu.nosql.tarea2.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import utu.nosql.tarea2.entity.Paciente;

public interface PacienteRepository extends MongoRepository<Paciente, String> {
}