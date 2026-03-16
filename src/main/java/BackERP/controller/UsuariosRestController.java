package BackERP.controller;

import BackERP.helper.segUsuariosSpecs;
import BackERP.models.segUsuarios;
import BackERP.repository.RepositoryUsuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UsuariosRestController {


    @Autowired
    private RepositoryUsuarios reusr;

    @GetMapping("usuarios")
    public List<segUsuarios> getUsuarios(
            @RequestParam(required = false) String usuario,
            @RequestParam(required = false) Integer idEmpleado,
            @RequestParam(required = false) Integer idUsuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "idUsuario,asc") String sort
    ){

        String[] sortParts = sort.split(",", 2);

        Sort.Direction dir = (sortParts.length > 1) ? Sort.Direction.fromString(sortParts[1]) : Sort.Direction.ASC;

        Sort s = Sort.by(dir, sortParts[0]);

        Pageable pageable = PageRequest.of(page, size, s);

        Specification<segUsuarios> spec = Specification
                .where(segUsuariosSpecs.estadoEquals(1))
                .and(segUsuariosSpecs.idUsuarioContains(idUsuario))
                .and(segUsuariosSpecs.idEmpleadoContains(idEmpleado))
                .and(segUsuariosSpecs.usuarioContains(usuario));

        return reusr.findAll(spec, pageable).getContent();
    }

    @PostMapping("grabarUsuario")
    public String grabarUsuario(@RequestBody segUsuarios Usuario){


        Usuario.setFechaModificacion(LocalDate.now());
        Usuario.setHoraModificacion(LocalTime.now());
        Usuario.setEstado(1);

        reusr.save(Usuario);

        return "Grabado";
    }
    @PutMapping("editarUsuario/{idUsuario}")
    public String editarUsuario(@PathVariable long idUsuario, @RequestBody segUsuarios Usuario){
        segUsuarios updateUsuario = reusr.findById(idUsuario).get();
        updateUsuario.setIdEmpleado(Usuario.getIdEmpleado());
        updateUsuario.setUsuario(Usuario.getUsuario());
        updateUsuario.setComentario(Usuario.getComentario());

        updateUsuario.setContrasena(Usuario.getContrasena());
        updateUsuario.setFechaModificacion(LocalDate.now());
        updateUsuario.setHoraModificacion(LocalTime.now());
        updateUsuario.setIdUsuarioModificacion(Usuario.getIdUsuarioModificacion());
        reusr.save(updateUsuario);

        return "Editado";
    }

    @DeleteMapping("eliminarUsuario/{idUsuario}")
    public String eliminarUsuario(@PathVariable long idUsuario, @RequestBody segUsuarios Usuario){

        segUsuarios updateUsuario = reusr.findById(idUsuario).get();
        updateUsuario.setFechaModificacion(LocalDate.now());
        updateUsuario.setHoraModificacion(LocalTime.now());
        updateUsuario.setIdUsuarioModificacion(Usuario.getIdUsuarioModificacion());
        updateUsuario.setEstado(0);
        reusr.save(updateUsuario);
        return "Eliminado";
    }


}
