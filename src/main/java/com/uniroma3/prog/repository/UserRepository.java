package com.uniroma3.prog.repository;

import com.uniroma3.prog.model.Credentials;
import org.springframework.data.repository.CrudRepository;

import com.uniroma3.prog.model.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    public Optional<User> findByUsername(String username);

}
