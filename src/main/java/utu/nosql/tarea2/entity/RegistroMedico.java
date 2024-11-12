package utu.nosql.tarea2.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "registros_medicos")
public class RegistroMedico {
    @Id
    private String id;
    private String ciPaciente;
    private String fecha;
    private String tipo;
    private String diagnostico;
    private String medico;
    private String institucion;
    private String descripcion;
    private String medicacion;

    // Getters y setters
}
