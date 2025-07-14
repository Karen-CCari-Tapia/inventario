package com.example.inventario.service;



import com.example.inventario.entity.Producto;
import com.example.inventario.entity.Salida;
import com.example.inventario.repository.ProductoRepository;
import com.example.inventario.dto.ProductoDTO;
import com.example.inventario.repository.SalidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public Producto ingresarProducto(ProductoDTO dto) {

        Producto producto = productoRepository.findByCodigo(dto.getCodigo())
                .orElseGet(() -> {

                    Producto nuevo = new Producto();
                    nuevo.setCodigo(dto.getCodigo());
                    nuevo.setDescripcion(dto.getDescripcion());
                    nuevo.setPrecioCompra(dto.getPrecioCompra());
                    nuevo.setCantidad(0);
                    return nuevo;
                });


        producto.setCantidad(producto.getCantidad() + 1);

        return productoRepository.save(producto);
    }
    @Autowired
    private SalidaRepository salidaRepository;

    public String registrarSalidaDesdeQr(String qr, double precioVenta) {
        String[] partes = qr.split(",");
        if (partes.length < 1) throw new RuntimeException("QR no valido");

        String codigo = partes[0].trim();
        Producto producto = productoRepository.findByCodigo(codigo).orElseThrow(() ->
                new RuntimeException("Producto no encontrado"));

        if (producto.getCantidad() <= 0) {
            throw new RuntimeException("No hay stock suficiente");
        }


        Salida salida = new Salida();
        salida.setProducto(producto);
        salida.setPrecioVenta(precioVenta);
        salida.setFechaSalida(LocalDateTime.now());
        salidaRepository.save(salida);

        producto.setCantidad(producto.getCantidad() - 1);
        productoRepository.save(producto);

        return "Salida registrada correctamente";
    }
}