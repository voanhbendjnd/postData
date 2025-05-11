package com.djnd.post_data.service;

import org.springframework.stereotype.Service;

import com.djnd.post_data.domain.entity.Role;
import com.djnd.post_data.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public boolean existsByName(String name) {
        return this.roleRepository.existsByName(name) ? true : false;
    }

    public Role findByName(String name) {
        Role role = this.roleRepository.findByName(name);
        if (role != null) {
            return role;
        }
        return null;
    }
}
