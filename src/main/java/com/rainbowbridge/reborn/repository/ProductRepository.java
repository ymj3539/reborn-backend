package com.rainbowbridge.reborn.repository;

import com.rainbowbridge.reborn.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
