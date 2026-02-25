package BackERP.controller;

import BackERP.helper.erpEmpleadosSpecs;
import BackERP.models.erpempleados;
import BackERP.repository.RepositoryEmpleados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class EmpleadosRestController {

    @Autowired
    private RepositoryEmpleados reemp;

    @GetMapping("empleados")
    public Page<erpempleados> getEmpleados(
            @RequestParam(required = false) String nombreEmpleado,
            @RequestParam(required = false) Integer idEmpleado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "idEmpleado,asc") String sort
    ){
        String[] sortParts = sort.split(",", 2);

        Sort.Direction dir = (sortParts.length > 1) ? Sort.Direction.fromString(sortParts[1]) : Sort.Direction.ASC;

        Sort s = Sort.by(dir, sortParts[0]);

        Pageable pageable = PageRequest.of(page, size, s);

        Specification<erpempleados> spec = Specification
                .where(erpEmpleadosSpecs.estadoEquals(1))
                .and(erpEmpleadosSpecs.idEmpleadoContains(idEmpleado))
                .and(erpEmpleadosSpecs.nombreEmpleadoContains(nombreEmpleado));

        return reemp.findAll(spec, pageable);
    }

    @PostMapping("grabarEmpleado")
    public String grabarEmpleado(@RequestBody erpempleados empleado){

        empleado.setFechaModificacion(LocalDate.now());
        empleado.setHoraModificacion(LocalTime.now());
        empleado.setEstado(1);
        reemp.save(empleado);

        return "Grabado";
    }
    @PutMapping("editarEmpleado/{id_empleado}")
    public String editarEmpleado(@PathVariable long id_empleado, @RequestBody erpempleados empleado){


        erpempleados updateEmpleado = reemp.findById(id_empleado).get();
        updateEmpleado.setNombre(empleado.getNombre());
        updateEmpleado.setApellido(empleado.getApellido());
        updateEmpleado.setEmail(empleado.getEmail());
        updateEmpleado.setTelefono(empleado.getTelefono());
        updateEmpleado.setIdCargo(empleado.getIdCargo());
        updateEmpleado.setIdUbicacion(empleado.getIdUbicacion());
        updateEmpleado.setDireccionResidencia(empleado.getDireccionResidencia());
        updateEmpleado.setFechaNacimiento(empleado.getFechaNacimiento());
        updateEmpleado.setFechaIngresoLaboral(empleado.getFechaIngresoLaboral());
        updateEmpleado.setFechaModificacion(LocalDate.now());
        updateEmpleado.setHoraModificacion(LocalTime.now());
        updateEmpleado.setIdUsuarioModificacion(empleado.getIdUsuarioModificacion());
        reemp.save(updateEmpleado);

        return "Editado";
    }

    @DeleteMapping("eliminarEmpleado/{id_empleado}")
    public String eliminarEmpleado(@PathVariable long id_empleado){
        System.out.println("eliminar");
        erpempleados updateEmpleado = reemp.findById(id_empleado).get();
        updateEmpleado.setEstado(0);
        reemp.save(updateEmpleado);
        return "Eliminado";
    }
}
