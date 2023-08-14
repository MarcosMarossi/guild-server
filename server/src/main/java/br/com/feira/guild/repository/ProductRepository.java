package br.com.feira.guild.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.com.feira.guild.to.Product;

@Service
public interface ProductRepository extends JpaRepository<Product, Integer> {

}