package BackERP.controller;

import BackERP.helper.segUsuariosPerfilesSpecs;
import BackERP.models.segUsuariosPerfiles;
import BackERP.repository.RepositoryUsuariosPerfiles;
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
public class UsuariosPerfilesRestController {

    @Autowired
    private RepositoryUsuariosPerfiles repo;

    @GetMapping("usuariosPerfiles")
    public List<segUsuariosPerfiles> getUsuariosPerfiles(
            @RequestParam(required = false) Integer idUsuario,
            @RequestParam(required = false) Integer idPerfil,
            @RequestParam(required = false) String permiso,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "idUsuarioPerfil,asc") String sort
    ) {
        String[] sortParts = sort.split(",", 2);
        Sort.Direction dir = (sortParts.length > 1) ? Sort.Direction.fromString(sortParts[1]) : Sort.Direction.ASC;
        Sort s = Sort.by(dir, sortParts[0]);
        Pageable pageable = PageRequest.of(page, size, s);

        Specification<segUsuariosPerfiles> spec = Specification
                .where(segUsuariosPerfilesSpecs.estadoEquals(1))
                .and(segUsuariosPerfilesSpecs.idUsuarioEquals(idUsuario))
                .and(segUsuariosPerfilesSpecs.idPerfilEquals(idPerfil))
                .and(segUsuariosPerfilesSpecs.permisoContains(permiso));

        return repo.findAll(spec, pageable).getContent();
    }

    @PostMapping("grabarUsuarioPerfil")
    public String grabarUsuarioPerfil(@RequestBody segUsuariosPerfiles usuarioPerfil) {
        usuarioPerfil.setFechaModificacion(LocalDate.now());
        usuarioPerfil.setHoraModificacion(LocalTime.now());
        usuarioPerfil.setEstado(1);
        repo.save(usuarioPerfil);
        return "Grabado";
    }

    @PutMapping("editarUsuarioPerfil/{idUsuarioPerfil}")
    public String editarUsuarioPerfil(@PathVariable int idUsuarioPerfil, @RequestBody segUsuariosPerfiles usuarioPerfil) {
        segUsuariosPerfiles update = repo.findById(idUsuarioPerfil).get();
        update.setIdUsuario(usuarioPerfil.getIdUsuario());
        update.setIdPerfil(usuarioPerfil.getIdPerfil());
        update.setPermiso(usuarioPerfil.getPermiso());
        update.setComentario(usuarioPerfil.getComentario());
        update.setIdUsuarioModificacion(usuarioPerfil.getIdUsuarioModificacion());
        update.setFechaModificacion(LocalDate.now());
        update.setHoraModificacion(LocalTime.now());
        repo.save(update);
        return "Editado";
    }

    @DeleteMapping("eliminarUsuarioPerfil/{idUsuarioPerfil}")
    public String eliminarUsuarioPerfil(@PathVariable int idUsuarioPerfil) {
        segUsuariosPerfiles update = repo.findById(idUsuarioPerfil).get();
        update.setEstado(0);
        repo.save(update);
        return "Eliminado";
    }
}
