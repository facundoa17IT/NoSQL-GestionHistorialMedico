package utu.nosql.tarea2.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import utu.nosql.tarea2.entity.Paciente;
import utu.nosql.tarea2.entity.RegistroMedico;
import utu.nosql.tarea2.service.HistorialMedicoService;

@RestController
@RequestMapping("/api")
public class HistorialMedicoController {

    private final HistorialMedicoService historialService;

    public HistorialMedicoController(HistorialMedicoService historialService) {
        this.historialService = historialService;
    }

    @PostMapping("/pacientes")
    public Paciente agregarPaciente(@RequestBody Paciente paciente) {
        return historialService.agregarPaciente(paciente);
    }

    @PostMapping("/pacientes/{ci}/registros")
    public RegistroMedico agregarRegistro(@PathVariable String ci, @RequestBody RegistroMedico registro) {
        return historialService.agregarRegistroMedico(ci, registro);
    }

    @GetMapping("/pacientes/{ci}/historial")
    public List<RegistroMedico> consultarHistorial(
            @PathVariable String ci,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String diagnostico,
            @RequestParam(required = false) String medico,
            @RequestParam(required = false) String institucion) {

        if (page != null && size != null) {
            if (size <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tamaño de la página debe ser mayor que cero");
            }
            return historialService.consultarHistorial(ci, PageRequest.of(page, size));
        } else {
            return historialService.obtenerRegistrosPorCriterio(ci, tipo, diagnostico, medico, institucion);
        }
    }
}
