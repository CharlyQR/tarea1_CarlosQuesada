package com.project.demo.logic.entity.Producto;

import com.project.demo.logic.entity.Categoria.Categoria;
import jakarta.persistence.*;

public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String nombre;
    private String descripcion;
    private double precio;

    @Column(name = "cantidadStock")
    private Integer cantidadStock;

    @ManyToOne
    @JoinColumn(name = "categoria")
    private Categoria categoria;
}