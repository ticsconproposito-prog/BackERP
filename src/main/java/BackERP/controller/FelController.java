
package BackERP.controller;

import BackERP.config.FelResult;
import BackERP.config.FelService;
import BackERP.models.DteRequestDto;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("fel")
public class FelController {

    private final FelService felService;

    @Autowired
    public FelController(FelService felService) {
        this.felService = felService;
    }

    @PostMapping("/dtes")
    public ResponseEntity<?> generar(@Valid @RequestBody DteRequestDto req) {
        FelResult result = felService.generarDte(req);
        if (result.isOk()) {
            Map<String, Object> body = new HashMap<>();
            body.put("fel", result);
            body.put("mensaje", "Encabezado actualizado correctamente");
            return ResponseEntity.ok(body);
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(result);
    }

}
