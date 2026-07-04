package br.duma.demo.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.duma.demo.domain.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

    User findByEmail(String email);
    
}
