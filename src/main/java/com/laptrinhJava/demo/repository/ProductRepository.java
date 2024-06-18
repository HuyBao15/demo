package com.laptrinhJava.demo.repository;

import com.laptrinhJava.demo.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% OR p.description LIKE %:keyword%")
    List<Product> findByKeyword(@Param("keyword") String keyword);
    @Query("SELECT p FROM Product p WHERE (p.name LIKE %:keyword% OR p.description LIKE %:keyword%) AND p.category.id = :categoryId")
    List<Product> findByKeywordAndCategory(@Param("keyword") String keyword, @Param("categoryId") Long categoryId);

    @Query("SELECT p FROM Product p WHERE (p.name LIKE %:keyword% OR p.description LIKE %:keyword%) AND p.category.name LIKE %:categoryName%")
    List<Product> findByKeywordAndCategoryName(@Param("keyword") String keyword, @Param("categoryName") String categoryName);
}