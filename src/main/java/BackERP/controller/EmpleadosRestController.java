package BackERP.controller;

import BackERP.models.erpempleados;
import BackERP.repository.RepositoryEmpleados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class EmpleadosRestController {

    @Autowired
    private RepositoryEmpleados reemp;

    @GetMapping("empleados")
    public List<erpempleados> getEmpleados(){

        return reemp.findAll();
    }

    @PostMapping("grabarEmpleado")
    public String grabarEmpleado(@RequestBody erpempleados empleado){

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
        updateEmpleado.setFechaModificacion(empleado.getFechaModificacion());
        updateEmpleado.setHoraModificacion(empleado.getHoraModificacion());
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
