package BackERP.controller;


import BackERP.helper.erpUbicacionesSpecs;
import BackERP.models.erpUbicaciones;
import BackERP.repository.RepositoryUbicaciones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UbicacionesRestControlles {

    @Autowired
    private RepositoryUbicaciones remubi;

    @GetMapping("ubicaciones")
    public List<erpUbicaciones> getUbicaciones(@RequestParam(required = false) Integer idUbicaciones,
                                               @RequestParam(required = false) String nombreUbicacion){


        Specification<erpUbicaciones> spec = Specification.where(erpUbicacionesSpecs.estadoEquals(1))
                .and(erpUbicacionesSpecs.idUbicacionContains(idUbicaciones))
                .and(erpUbicacionesSpecs.nombreUbicacionContains(nombreUbicacion));

        return remubi.findAll(spec);
    }

    @PostMapping("grabarUbicacion")
    public String grabarUbicacion(@RequestBody erpUbicaciones Ubicaciones){
        // valor por defecto
        Ubicaciones.setFechaModificacion(LocalDate.now());
        Ubicaciones.setHoraModificacion(LocalTime.now());
        Ubicaciones.setEstado(1);
        remubi.save(Ubicaciones);

        return "Grabado";
    }

    @PutMapping("editarUbicacion/{idUbicacion}")
    public String editarUbicaciones(@PathVariable long idUbicacion, @RequestBody erpUbicaciones Ubicaciones){
        erpUbicaciones updateUbicaciones = remubi.findById(idUbicacion).get();
        updateUbicaciones.setNombreUbicacion(Ubicaciones.getNombreUbicacion());
        updateUbicaciones.setDireccionFisica(Ubicaciones.getDireccionFisica());
        updateUbicaciones.setComentario(Ubicaciones.getComentario());
        updateUbicaciones.setFechaModificacion(LocalDate.now());
        updateUbicaciones.setHoraModificacion(LocalTime.now());
        remubi.save(updateUbicaciones);

        return "Editado";
    }

    @DeleteMapping("eliminarUbicacion/{idUbicacion}")
    public String eliminarUbicacion(@PathVariable long idUbicacion){
        System.out.println("eliminar");
        erpUbicaciones updateerpUbicaciones = remubi.findById(idUbicacion).get();
        updateerpUbicaciones.setEstado(0);
        remubi.save(updateerpUbicaciones);
        return "Eliminado";
    }

}
