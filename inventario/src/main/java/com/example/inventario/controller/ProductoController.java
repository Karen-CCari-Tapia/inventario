package com.example.inventario.controller;
import com.example.inventario.dto.ProductoDTO;
import com.example.inventario.dto.QrRequestDTO;
import com.example.inventario.dto.SalidaDTO;
import com.example.inventario.entity.Producto;
import com.example.inventario.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping("/ingreso")
    public Producto ingresoProducto(@RequestBody ProductoDTO dto) {
        return productoService.ingresarProducto(dto);
    }
    @PostMapping("/ingresoqr")
    public ResponseEntity<?> ingresoDesdeQr(@RequestBody QrRequestDTO qrDto) {
        try {
            String[] partes = qrDto.getQr().split(",");
            if (partes.length != 3) {
                return ResponseEntity.badRequest().body("EL formtato del qr no es valido ->se espera codigo, descripcion , precio");
            }

            String codigo = partes[0].trim();
            String descripcion = partes[1].trim();
            double precioCompra = Double.parseDouble(partes[2].trim());

            ProductoDTO dto = new ProductoDTO();
            dto.setCodigo(codigo);
            dto.setDescripcion(descripcion);
            dto.setPrecioCompra(precioCompra);

            productoService.ingresarProducto(dto);
            return ResponseEntity.ok("Producto ingresado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el qr" + e.getMessage());
        }
    }
    @PostMapping("/salidaqr")
    public ResponseEntity<?> salidaDesdeQr(@RequestBody SalidaDTO dto) {
        try {
            String resultado = productoService.registrarSalidaDesdeQr(dto.getQr(), dto.getPrecioVenta());
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error " + e.getMessage());
        }
    }
}