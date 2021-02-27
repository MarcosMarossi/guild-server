package br.com.suafeira.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.com.suafeira.to.ProductTO;

@Service
public interface ProductRepository extends JpaRepository<ProductTO, Integer> {

}