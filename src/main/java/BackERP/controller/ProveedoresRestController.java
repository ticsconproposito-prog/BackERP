package BackERP.controller;


import BackERP.helper.erpProveedoresSpecs;
import BackERP.models.erpProveedores;
import BackERP.repository.RepositoryProveedores;
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
public class ProveedoresRestController {

    @Autowired
    private RepositoryProveedores repprove;

    @GetMapping("proveedores")
    public Page<erpProveedores> getProveedores(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String nombreDeContacto,
            @RequestParam(required = false) Integer idProveedorContains,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "idProveedor,asc") String sort
    ) {

        String[] sortParts = sort.split(",", 2);

        Sort.Direction dir = (sortParts.length > 1) ? Sort.Direction.fromString(sortParts[1]) : Sort.Direction.ASC;

        Sort s = Sort.by(dir, sortParts[0]);

        Pageable pageable = PageRequest.of(page, size, s);

        Specification<erpProveedores> spec = Specification
                .where(erpProveedoresSpecs.estadoEquals(1))
               .and(erpProveedoresSpecs.nombreContains(nombre))
                .and(erpProveedoresSpecs.idProveedorContains(idProveedorContains))
                .and(erpProveedoresSpecs.nombreDeContacto1Contains(nombreDeContacto));

        return repprove.findAll(spec, pageable);
    }

    @PostMapping("grabarProveedor")
    public String grabarProveedores(@RequestBody erpProveedores proveedores){

        // valor por defecto
        proveedores.setFechaModificacion(LocalDate.now());
        proveedores.setHoraModificacion(LocalTime.now());
        proveedores.setEstado(1);
        repprove.save(proveedores);

        return "Grabado";
    }
    @PutMapping("editarProveedor/{idProveedor}")
    public String editarProducto(@PathVariable long idProveedor, @RequestBody erpProveedores proveedores){


        erpProveedores updateProveedores = repprove.findById(idProveedor).get();
        updateProveedores.setNombre(proveedores.getNombre());
        updateProveedores.setDireccionFisica(proveedores.getDireccionFisica());
        updateProveedores.setCorreoElectronico(proveedores.getCorreoElectronico());
        updateProveedores.setNombreDeContacto1(proveedores.getNombreDeContacto1());
        updateProveedores.setNombreDeContacto2(proveedores.getNombreDeContacto2());
        updateProveedores.setTelefono1(proveedores.getTelefono1());
        updateProveedores.setTelefono2(proveedores.getTelefono2());
        updateProveedores.setCreditoAutorizado(proveedores.getCreditoAutorizado());
        updateProveedores.setDeudaActual(proveedores.getDeudaActual());
        updateProveedores.setFechaModificacion(LocalDate.now());
        updateProveedores.setHoraModificacion(LocalTime.now());
        updateProveedores.setIdUsuarioModificacion(proveedores.getIdUsuarioModificacion());
        repprove.save(updateProveedores);

        return "Editado";
    }

    @DeleteMapping("eliminarProveedor/{idProveedor}")
    public String eliminarProveedores(@PathVariable long idProveedor, @RequestBody erpProveedores proveedores){
        System.out.println("eliminar");
        erpProveedores updateProveedores = repprove.findById(idProveedor).get();
        updateProveedores.setFechaModificacion(LocalDate.now());
        updateProveedores.setHoraModificacion(LocalTime.now());
        updateProveedores.setIdUsuarioModificacion(proveedores.getIdUsuarioModificacion());
        updateProveedores.setEstado(0);
        repprove.save(updateProveedores);
        return "Eliminado";
    }

}
