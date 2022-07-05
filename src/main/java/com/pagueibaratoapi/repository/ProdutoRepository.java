package com.pagueibaratoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pagueibaratoapi.models.requests.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    
    public boolean existsById(Integer id);

}