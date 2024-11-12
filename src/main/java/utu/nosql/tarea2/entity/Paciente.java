package utu.nosql.tarea2.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "pacientes")
public class Paciente {
    @Id
    private String ci;
    private String nombre;
    private String apellido;
    private String fechaNacimiento;
    private String sexo;

    // Getters y setters
}
