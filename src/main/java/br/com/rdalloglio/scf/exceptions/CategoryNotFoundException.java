package br.com.rdalloglio.scf.exceptions;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("Categoria com ID " + id + " não encontrada.");
    }
}