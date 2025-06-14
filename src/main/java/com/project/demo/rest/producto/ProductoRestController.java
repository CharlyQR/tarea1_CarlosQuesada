package com.project.demo.rest.producto;

import com.project.demo.logic.entity.categoria.CategoriaRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import com.project.demo.logic.entity.producto.Producto;
import com.project.demo.logic.entity.producto.ProductoRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/productos")
public class ProductoRestController {
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public ResponseEntity<?> getAllProducts(HttpServletRequest request) {
        return new GlobalResponseHandler().handleResponse(
                "Productos Guardados: ",
                productoRepository.findAll(),
                HttpStatus.OK, request
        );
    }

    @PutMapping("/{productoId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateProducto(@PathVariable Long productoId, @RequestBody Producto producto, HttpServletRequest request) {
        Optional<Producto> foundProduct = productoRepository.findById(productoId);
        if (foundProduct.isPresent()) {
            producto.setId(foundProduct.get().getId());
            productoRepository.save(producto);
            return new GlobalResponseHandler().handleResponse("Producto actualizado correctamente",
                    producto, HttpStatus.OK, request);
        } else {
            return new GlobalResponseHandler().handleResponse("El producto con id: " + productoId + ", no existe",
                    HttpStatus.NOT_FOUND, request);
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
   public ResponseEntity<?>  createProduct(@RequestBody Producto producto, HttpServletRequest request) {
        Producto savedProduct =  productoRepository.save(producto);
        return new GlobalResponseHandler().handleResponse(
                "Producto guardado correctamente",
                savedProduct, HttpStatus.CREATED, request
        );
    }

    @DeleteMapping("/{productoId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productoId, HttpServletRequest request) {
        Optional<Producto> foundProduct = productoRepository.findById(productoId);
        if (foundProduct.isPresent()) {
            productoRepository.deleteById(productoId);
            return new GlobalResponseHandler().handleResponse("Producto eliminado correctamente.",
                    foundProduct, HttpStatus.OK, request
            );
        } else {
            return new GlobalResponseHandler().handleResponse("El producto con id: " + productoId + ", no fue encontrado." ,
                    HttpStatus.NOT_FOUND, request);
        }
    }
}