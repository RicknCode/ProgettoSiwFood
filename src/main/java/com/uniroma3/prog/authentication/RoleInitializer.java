package com.uniroma3.prog.authentication;

import com.uniroma3.prog.model.Role;
import com.uniroma3.prog.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleInitializer {

    @Bean
    public CommandLineRunner initializeRoles(RoleRepository roleRepository) {
        return args -> {
            createRoleIfNotFound(roleRepository, "ROLE_COOK");
            createRoleIfNotFound(roleRepository, "ROLE_ADMIN");
        };
    }

    private void createRoleIfNotFound(RoleRepository roleRepository, String roleName) {
        if (!roleRepository.findByName(roleName).isPresent()) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }
}