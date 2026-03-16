package BackERP.controller;

import BackERP.helper.segPerfilesSpecs;
import BackERP.models.segPerfiles;
import BackERP.repository.RepositoryPerfiles;
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
public class PerfilesRestController {

    @Autowired
    private RepositoryPerfiles reper;

    @GetMapping("perfiles")
    public Page<segPerfiles> getPerfiles(@RequestParam(required = false) String nombrePerfil,
                                         @RequestParam(required = false) Integer idPerfil,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size,
                                         @RequestParam(defaultValue = "idPerfil,asc") String sort
    ){

        String[] sortParts = sort.split(",", 2);

        Sort.Direction dir = (sortParts.length > 1) ? Sort.Direction.fromString(sortParts[1]) : Sort.Direction.ASC;

        Sort s = Sort.by(dir, sortParts[0]);

        Pageable pageable = PageRequest.of(page, size, s);

        Specification<segPerfiles> spec = Specification
                .where(segPerfilesSpecs.estadoEquals(1))
                .and(segPerfilesSpecs.nombrePerfilContains(nombrePerfil))
                .and(segPerfilesSpecs.idPaginaContains(idPerfil));

        return reper.findAll(spec, pageable);
    }

    @PostMapping("grabarPerfil")
    public String grabarPerfil(@RequestBody segPerfiles Perfil){

        Perfil.setFechaModificacion(LocalDate.now());
        Perfil.setHoraModificacion(LocalTime.now());
        Perfil.setEstado(1);
        reper.save(Perfil);

        return "Grabado";
    }
    @PutMapping("editarPerfil/{idPerfil}")
    public String editarPerfil(@PathVariable long idPerfil, @RequestBody segPerfiles Perfil){
        segPerfiles updatePerfil = reper.findById(idPerfil).get();
        updatePerfil.setNombrePerfil(Perfil.getNombrePerfil());
        updatePerfil.setFechaModificacion(LocalDate.now());
        updatePerfil.setHoraModificacion(LocalTime.now());
        updatePerfil.setIdUsuarioModificacion(Perfil.getIdUsuarioModificacion());
        reper.save(updatePerfil);

        return "Editado";
    }

    @DeleteMapping("eliminarPerfil/{idPerfil}")
    public String eliminarPerfil(@PathVariable long idPerfil, @RequestBody segPerfiles Perfil){
        System.out.println("eliminar");
        segPerfiles updatePerfil = reper.findById(idPerfil).get();
        updatePerfil.setFechaModificacion(LocalDate.now());
        updatePerfil.setHoraModificacion(LocalTime.now());
        updatePerfil.setIdUsuarioModificacion(Perfil.getIdUsuarioModificacion());
        updatePerfil.setEstado(0);
        reper.save(updatePerfil);
        return "Eliminado";
    }
}
