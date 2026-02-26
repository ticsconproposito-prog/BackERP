package BackERP.controller;

import BackERP.helper.segPerfilesPaginasSpecs;
import BackERP.models.segPerfilesPaginas;
import BackERP.repository.RepositoryPerfilesPaginas;
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
public class PerfilesPaginasRestController {

    @Autowired
    private RepositoryPerfilesPaginas repo;

    @GetMapping("perfilesPaginas")
    public List<segPerfilesPaginas> getPerfilesPaginas(
            @RequestParam(required = false) Integer idPerfil,
            @RequestParam(required = false) Integer idPagina,
            @RequestParam(required = false) Integer permiso,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "idPerfilPagina,asc") String sort
    ) {
        String[] sortParts = sort.split(",", 2);
        Sort.Direction dir = (sortParts.length > 1) ? Sort.Direction.fromString(sortParts[1]) : Sort.Direction.ASC;
        Sort s = Sort.by(dir, sortParts[0]);
        Pageable pageable = PageRequest.of(page, size, s);

        Specification<segPerfilesPaginas> spec = Specification
                .where(segPerfilesPaginasSpecs.estadoEquals(1))
                .and(segPerfilesPaginasSpecs.idPerfilEquals(idPerfil))
                .and(segPerfilesPaginasSpecs.idPaginaEquals(idPagina))
                .and(segPerfilesPaginasSpecs.permisoEquals(permiso));

        return repo.findAll(spec, pageable).getContent();
    }

    @PostMapping("grabarPerfilPagina")
    public String grabarPerfilPagina(@RequestBody segPerfilesPaginas perfilPagina) {
        perfilPagina.setFechaModificacion(LocalDate.now());
        perfilPagina.setHoraModificacion(LocalTime.now());
        perfilPagina.setEstado(1);
        repo.save(perfilPagina);
        return "Grabado";
    }

    @PutMapping("editarPerfilPagina/{idPerfilPagina}")
    public String editarPerfilPagina(@PathVariable int idPerfilPagina, @RequestBody segPerfilesPaginas perfilPagina) {
        segPerfilesPaginas update = repo.findById(idPerfilPagina).get();
        update.setIdPerfil(perfilPagina.getIdPerfil());
        update.setIdPagina(perfilPagina.getIdPagina());
        update.setPermiso(perfilPagina.getPermiso());
        update.setIdUsuarioModificacion(perfilPagina.getIdUsuarioModificacion());
        update.setFechaModificacion(LocalDate.now());
        update.setHoraModificacion(LocalTime.now());
        repo.save(update);
        return "Editado";
    }

    @DeleteMapping("eliminarPerfilPagina/{idPerfilPagina}")
    public String eliminarPerfilPagina(@PathVariable int idPerfilPagina) {
        segPerfilesPaginas update = repo.findById(idPerfilPagina).get();
        update.setEstado(0);
        repo.save(update);
        return "Eliminado";
    }
}
