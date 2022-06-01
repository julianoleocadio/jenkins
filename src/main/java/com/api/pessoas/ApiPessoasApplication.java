package com.api.pessoas;

import com.api.pessoas.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class ApiPessoasApplication {

	@Autowired
	private PessoaRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(ApiPessoasApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//
//	}

}
