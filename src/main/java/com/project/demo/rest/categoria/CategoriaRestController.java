package com.project.demo.rest.categoria;

import com.project.demo.logic.entity.categoria.Categoria;
import com.project.demo.logic.entity.categoria.CategoriaRepository;
import com.project.demo.logic.entity.http.GlobalResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/categorias")
public class CategoriaRestController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'USER')")
    public ResponseEntity<?> getAllCategories(HttpServletRequest request) {
        return new GlobalResponseHandler().handleResponse("Categorías guardadas: ",
                categoriaRepository.findAll(), HttpStatus.OK, request
        );
    }

    @PutMapping("/{categoriaId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateCategoria(@PathVariable Long categoriaId, @RequestBody Categoria categoria,  HttpServletRequest request) {
        Optional<Categoria> foundCategoria = categoriaRepository.findById(categoriaId);
        if (foundCategoria.isPresent()) {
            categoria.setId(foundCategoria.get().getId());
            categoriaRepository.save(categoria);
            return new GlobalResponseHandler().handleResponse("Categoría actualizada correctamente.",
                    categoria, HttpStatus.OK, request
            );
        } else {
            return new GlobalResponseHandler().handleResponse("La categoría con id: " + categoriaId + ", no existe",
                    HttpStatus.NOT_FOUND, request
            );
        }
    }

    @PatchMapping("/{categoriaId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> patchCategoria(@PathVariable Long categoriaId, @RequestBody Categoria categoria, HttpServletRequest request) {
        Optional<Categoria> foundCategoria = categoriaRepository.findById(categoriaId);
        if (foundCategoria.isPresent()) {
            if (categoria.getNombre() != null) foundCategoria.get().setNombre(categoria.getNombre());
            if (categoria.getDescripcion() != null) foundCategoria.get().setDescripcion(categoria.getDescripcion());
            categoriaRepository.save(foundCategoria.get());
            return new GlobalResponseHandler().handleResponse("Categoría actualizada correctamente.",
                    foundCategoria.get(), HttpStatus.OK, request
            );
        } else {
            return new GlobalResponseHandler().handleResponse("La categoría con el id: " + categoriaId + ", no existe en la Base de Datos.",
                    HttpStatus.NOT_FOUND, request
            );
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> createCategoria(@RequestBody Categoria categoria, HttpServletRequest request) {
        Categoria savedCategoria = categoriaRepository.save(categoria);
        return new GlobalResponseHandler().handleResponse("Categoría guardada correctamente.",
                savedCategoria, HttpStatus.CREATED, request
        );
    }

    @DeleteMapping("/{categoriaId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteCategoria(@PathVariable Long categoriaId, HttpServletRequest request) {
        Optional<Categoria> foundCategoria = categoriaRepository.findById(categoriaId);
        if (foundCategoria.isPresent()) {
            categoriaRepository.delete(foundCategoria.get());
            return new GlobalResponseHandler().handleResponse("Producto eliminado correctamente.",
                    foundCategoria, HttpStatus.OK, request
            );
        } else {
            return new GlobalResponseHandler().handleResponse("La categoría con id: " + categoriaId + ", no fue encontrada.",
                    HttpStatus.NOT_FOUND, request
            );
        }
    }
}