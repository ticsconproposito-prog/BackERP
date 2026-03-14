package BackERP.controller;

import BackERP.helper.segPaginasSpecs;
import BackERP.models.segpaginas;
import BackERP.repository.RepositoryPaginas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
public class PaginasRestController {

    @Autowired
    private RepositoryPaginas repag;


    @GetMapping("ping")
    public String ping() {
        return "OK";
    }
    @GetMapping("paginas")
    public Page<segpaginas> getPaginas(@RequestParam(required = false) String nombrePagina,
                                       @RequestParam(required = false) Integer idPagina,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int size,
                                       @RequestParam(defaultValue = "idPagina,asc") String sort
    ){
        String[] sortParts = sort.split(",", 2);

        Sort.Direction dir = (sortParts.length > 1) ? Sort.Direction.fromString(sortParts[1]) : Sort.Direction.ASC;

        Sort s = Sort.by(dir, sortParts[0]);

        Pageable pageable = PageRequest.of(page, size, s);


        Specification<segpaginas> spec = Specification
                .where(segPaginasSpecs.estadoEquals(1))
                .and(segPaginasSpecs.idPaginaContains(idPagina))
                .and(segPaginasSpecs.nombrePaginaContains(nombrePagina));


        return repag.findAll(spec, pageable);
    }

    @PostMapping("grabarPagina")
    public String grabarPagina(@RequestBody segpaginas pagina){


        pagina.setFechaModificacion(LocalDate.now());
        pagina.setHoraModificacion(LocalTime.now());
        pagina.setEstado(1);
        repag.save(pagina);

        return "Grabado";
    }
    @PutMapping("editarPagina/{idPagina}")
    public String editarPagina(@PathVariable long idPagina, @RequestBody segpaginas pagina){
        segpaginas updatePagina = repag.findById(pagina.getIdPagina()).get();
        updatePagina.setNombrePagina(pagina.getNombrePagina());
        updatePagina.setURL(pagina.getURL());
        updatePagina.setFechaModificacion(LocalDate.now());
        updatePagina.setHoraModificacion(LocalTime.now());
        updatePagina.setIdUsuarioModificacion(pagina.getIdUsuarioModificacion());

        repag.save(updatePagina);

        return "Editado";
    }

    @DeleteMapping("eliminarPagina/{idPagina}")
    public String eliminarPagina(@PathVariable long idPagina, @RequestBody segpaginas pagina){
        System.out.println("eliminar");
        segpaginas updatePagina = repag.findById(idPagina).get();
        updatePagina.setFechaModificacion(LocalDate.now());
        updatePagina.setHoraModificacion(LocalTime.now());
        updatePagina.setIdUsuarioModificacion(pagina.getIdUsuarioModificacion());
        updatePagina.setEstado(0);
        repag.save(updatePagina);
        return "Eliminado";
    }

}
