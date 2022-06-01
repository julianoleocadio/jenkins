package com.api.pessoas.service;

import com.api.pessoas.model.Pessoa;
import com.api.pessoas.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository repository;

    public List<Pessoa> findAll() {
        return repository.findAll();
    }

    public Optional<Pessoa> findById(String id) {
        return repository.findById(id);
    }

    public Pessoa insert(Pessoa pessoa) {
        return repository.insert(pessoa);
    }
}
