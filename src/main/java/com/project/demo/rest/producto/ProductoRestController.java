package com.project.demo.rest.producto;

import com.project.demo.logic.entity.producto.Producto;
import com.project.demo.logic.entity.producto.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoRestController {
    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','SUPER_ADMIN')")
    public List<Producto> getAllProducts() {
        return productoRepository.findAll();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public Producto updateProduct(@PathVariable Long id, @RequestBody Producto producto) {
        return productoRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setNombre(producto.getNombre());
                    existingProduct.setDescripcion(producto.getDescripcion());
                    existingProduct.setPrecio(producto.getPrecio());
                    existingProduct.setCantidadStock(producto.getCantidadStock());
                    existingProduct.setCategoria(producto.getCategoria());
                    return productoRepository.save(existingProduct);
                })
                .orElseGet(() -> {
                    producto.setId(id);
                    return productoRepository.save(producto);
                });
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public Producto addProduct(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productoRepository.deleteById(id);
    }
}
