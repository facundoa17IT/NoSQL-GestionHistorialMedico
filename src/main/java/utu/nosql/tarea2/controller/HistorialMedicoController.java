package utu.nosql.tarea2.controller;

import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<Object> agregarPaciente(@RequestBody Paciente paciente) {
        try {
            Paciente nuevoPaciente = historialService.agregarPaciente(paciente);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPaciente);
        } catch (ResponseStatusException ex) {
            return crearRespuestaError(ex, null);
        }
    }

    @PostMapping("/pacientes/{ci}/registros")
    public ResponseEntity<Object> agregarRegistro(@PathVariable String ci, @RequestBody RegistroMedico registro) {
        try {
            RegistroMedico nuevoRegistro = historialService.agregarRegistroMedico(ci, registro);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRegistro);
        } catch (ResponseStatusException ex) {
            return crearRespuestaError(ex, null);
        }
    }

    @GetMapping("/pacientes/{ci}/historial")
    public ResponseEntity<Object> consultarHistorial(
            @PathVariable String ci,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String diagnostico,
            @RequestParam(required = false) String medico,
            @RequestParam(required = false) String institucion,
            HttpServletRequest request) {

        try {
            if (page != null && size != null) {
                if (size <= 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tamaño de la página debe ser mayor que cero");
                }
                List<RegistroMedico> historial = historialService.consultarHistorial(ci, PageRequest.of(page, size));
                return ResponseEntity.ok(historial);
            } else {
                List<RegistroMedico> registros = historialService.obtenerRegistrosPorCriterio(ci, tipo, diagnostico, medico, institucion);
                return ResponseEntity.ok(registros);
            }
        } catch (ResponseStatusException ex) {
            return crearRespuestaError(ex, request);
        }
    }

    // Método para crear la respuesta de error
    private ResponseEntity<Object> crearRespuestaError(ResponseStatusException ex, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", ex.getStatusCode().value());
        response.put("error", ex.getReason());
        response.put("message", ex.getReason()); // Mensaje personalizado desde el servicio
        response.put("path", request != null ? request.getRequestURI() : "/api"); // Obtener la URI del request

        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }
}
