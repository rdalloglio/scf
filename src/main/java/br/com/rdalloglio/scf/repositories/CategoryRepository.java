package br.com.rdalloglio.scf.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.rdalloglio.scf.entities.Category;
import br.com.rdalloglio.scf.enums.CategoryType;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByType(CategoryType type);
}
