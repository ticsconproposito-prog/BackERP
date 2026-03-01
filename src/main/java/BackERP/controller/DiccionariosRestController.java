package BackERP.controller;


import BackERP.helper.erpDiccionariosSpecs;
import BackERP.models.erpdiccionarios;
import BackERP.repository.RepositoryDiccionarios;
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
public class DiccionariosRestController {

    @Autowired
    private RepositoryDiccionarios redic;

    @GetMapping("diccionarios")
    public Page<erpdiccionarios> getDiccionarios(
            @RequestParam(required = false) String diccionario,
            @RequestParam(required = false) String indice,
            @RequestParam(required = false) String valor,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "idDiccionario,asc") String sort
    ) {

        String[] sortParts = sort.split(",", 2);

        Sort.Direction dir = (sortParts.length > 1) ? Sort.Direction.fromString(sortParts[1]) : Sort.Direction.ASC;

        Sort s = Sort.by(dir, sortParts[0]);

        Pageable pageable = PageRequest.of(page, size, s);

        Specification<erpdiccionarios> spec = Specification
                .where(erpDiccionariosSpecs.diccionarioContains(diccionario))
                .and(erpDiccionariosSpecs.indiceContiene(indice))
                .and(erpDiccionariosSpecs.valorContiene(valor))
                .and(erpDiccionariosSpecs.estadoEquals(1));

        return redic.findAll(spec, pageable);
    }

    @PostMapping("grabarDiccionario")
    public String grabarDiccionario(@RequestBody erpdiccionarios Diccionario){

        //  normalizar código
        Diccionario.setDiccionario(
                Diccionario.getDiccionario().toUpperCase().trim()
        );

        //  valor por defecto
        Diccionario.setFechaModificacion(LocalDate.now());
        Diccionario.setHoraModificacion(LocalTime.now());
        Diccionario.setEstado(1);

        redic.save(Diccionario);
        return "Grabado";
    }

    @PutMapping("editarDiccionario/{idDiccionario}")
    public String editarDiccionario(@PathVariable long idDiccionario, @RequestBody erpdiccionarios Diccionario){


        erpdiccionarios updateDiccionario = redic.findById(idDiccionario).get();
        updateDiccionario.setDiccionario(Diccionario.getDiccionario());
        updateDiccionario.setIndice(Diccionario.getIndice());
        updateDiccionario.setValor(Diccionario.getValor());
        updateDiccionario.setDescripcion(Diccionario.getDescripcion());
        Diccionario.setFechaModificacion(LocalDate.now());
        Diccionario.setHoraModificacion(LocalTime.now());
        updateDiccionario.setIdUsuarioModificacion(Diccionario.getIdUsuarioModificacion());
        redic.save(updateDiccionario);

        return "Editado";
    }

    @DeleteMapping("eliminarDiccionario/{idDiccionario}")
    public String eliminarDiccionario(@PathVariable long idDiccionario, @RequestBody erpdiccionarios Diccionario){
        System.out.println("eliminar");
        erpdiccionarios updateDiccionario = redic.findById(idDiccionario).get();
        updateDiccionario.setFechaModificacion(LocalDate.now());
        updateDiccionario.setHoraModificacion(LocalTime.now());
        updateDiccionario.setIdUsuarioModificacion(Diccionario.getIdUsuarioModificacion());
        updateDiccionario.setEstado(0);
        redic.save(updateDiccionario);
        return "Eliminado";
    }


}
