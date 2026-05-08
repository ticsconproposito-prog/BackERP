package BackERP.controller;

import BackERP.config.FelResult;
import BackERP.config.FelService;
import BackERP.models.felDteRequestDto;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("fel")
public class FelController {

  private final FelService felService;

  @Autowired
  public FelController(FelService felService) {
    this.felService = felService;
  }

  // Endpoint original síncrono (se mantiene como respaldo)
  @PostMapping("/dtes/sync")
  public ResponseEntity<?> generarSync(@Valid @RequestBody felDteRequestDto req) {
    FelResult result = felService.generarDte(req);
    if (result.isOk()) {
      Map<String, Object> body = new HashMap<>();
      body.put("fel", result);
      body.put("mensaje", "Encabezado actualizado correctamente");
      return ResponseEntity.ok(body);
    }
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(result);
  }

  // Endpoint async recomendado
  @PostMapping("/dtes")
  public ResponseEntity<?> generarAsync(@Valid @RequestBody felDteRequestDto req) {

    CompletableFuture<FelResult> futureResult = felService.generarDteAsync(req);

    try {
      FelResult result = futureResult.get(25, TimeUnit.SECONDS);

      if (result.isOk()) {
        Map<String, Object> body = new HashMap<>();
        body.put("fel", result);
        body.put("mensaje", "Documento generado exitosamente");
        return ResponseEntity.ok(body);
      }
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(result);

    } catch (java.util.concurrent.TimeoutException e) {
      futureResult.cancel(true);

      Map<String, Object> errorBody = new HashMap<>();
      errorBody.put("ok", false);
      errorBody.put("error", "El servicio FEL está tardando más de 25 segundos");
      errorBody.put("cancelled", true);
      return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(errorBody);

    } catch (java.util.concurrent.CancellationException e) {
      Map<String, Object> errorBody = new HashMap<>();
      errorBody.put("ok", false);
      errorBody.put("error", "La operación fue cancelada por timeout");
      return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(errorBody);

    } catch (Exception e) {
      Map<String, Object> errorBody = new HashMap<>();
      errorBody.put("ok", false);
      errorBody.put("error", "Error al procesar: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
    }
  }

  // Endpoint async para anulación
  @PostMapping("/anularFactura")
  public ResponseEntity<?> anularFacturaAsync(@RequestParam Long idEncabezadoFactura,
                                              @RequestParam String serie,
                                              @RequestParam String preimpreso,
                                              @RequestParam String nitComprador,
                                              @RequestParam String fechaAnulacion,
                                              @RequestParam String motivo) {

    CompletableFuture<FelResult> futureResult = felService.anularFacturaAsync(
      idEncabezadoFactura, serie, preimpreso, nitComprador, fechaAnulacion, motivo
    );

    try {
      FelResult result = futureResult.get(25, TimeUnit.SECONDS);

      if (result.isOk()) {
        Map<String, Object> body = new HashMap<>();
        body.put("fel", result);
        body.put("mensaje", "Factura anulada correctamente");
        return ResponseEntity.ok(body);
      }
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(result);

    } catch (java.util.concurrent.TimeoutException e) {
      futureResult.cancel(true);

      Map<String, Object> errorBody = new HashMap<>();
      errorBody.put("ok", false);
      errorBody.put("error", "La anulación está tardando más de 25 segundos");
      errorBody.put("cancelled", true);
      return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(errorBody);

    } catch (java.util.concurrent.CancellationException e) {
      Map<String, Object> errorBody = new HashMap<>();
      errorBody.put("ok", false);
      errorBody.put("error", "La operación de anulación fue cancelada por timeout");
      return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(errorBody);

    } catch (Exception e) {
      Map<String, Object> errorBody = new HashMap<>();
      errorBody.put("ok", false);
      errorBody.put("error", "Error al anular: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
    }
  }
}
