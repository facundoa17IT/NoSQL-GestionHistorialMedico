package utu.nosql.tarea2.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.mongodb.core.query.Query;
import utu.nosql.tarea2.entity.Paciente;
import utu.nosql.tarea2.entity.RegistroMedico;
import utu.nosql.tarea2.repository.PacienteRepository;
import utu.nosql.tarea2.repository.RegistroMedicoRepository;

@Service
public class HistorialMedicoService {

    private final PacienteRepository pacienteRepository;
    private final RegistroMedicoRepository registroMedicoRepository;
    private final MongoTemplate mongoTemplate;


    public HistorialMedicoService(PacienteRepository pacienteRepo, RegistroMedicoRepository registroMedicoRepo, MongoTemplate mongoTemplate) {
        this.pacienteRepository = pacienteRepo;
        this.registroMedicoRepository = registroMedicoRepo;
        this.mongoTemplate = mongoTemplate;
    }

    public Paciente agregarPaciente(Paciente paciente) {
        if (pacienteRepository.existsById(paciente.getCi())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "El paciente ya existe");
        }
        return pacienteRepository.save(paciente);
    }

    public RegistroMedico agregarRegistroMedico(String ci, RegistroMedico registro) {
        if (!pacienteRepository.existsById(ci)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un paciente con esa cédula");
        }
        registro.setCiPaciente(ci);
        return registroMedicoRepository.save(registro);
    }

    public List<RegistroMedico> consultarHistorial(String ciPaciente, Pageable pageable) {
        if (!pacienteRepository.existsById(ciPaciente)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un paciente con esa cédula");
        }
        if (pageable.getPageSize() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tamaño de la página debe ser mayor que cero");
        }
        return registroMedicoRepository.findByCiPacienteOrderByFechaDesc(ciPaciente, pageable);
    }

      public List<RegistroMedico> obtenerRegistrosPorCriterio(String ciPaciente, String tipo, String diagnostico, String medico, String institucion) {
        if (!pacienteRepository.existsById(ciPaciente)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un paciente con esa cédula");
        }

        Query query = new Query(Criteria.where("ciPaciente").is(ciPaciente));

        if (tipo != null) query.addCriteria(Criteria.where("tipo").is(tipo));
        if (diagnostico != null) query.addCriteria(Criteria.where("diagnostico").is(diagnostico));
        if (medico != null) query.addCriteria(Criteria.where("medico").is(medico));
        if (institucion != null) query.addCriteria(Criteria.where("institucion").is(institucion));

        return mongoTemplate.find(query, RegistroMedico.class);
    }
}
