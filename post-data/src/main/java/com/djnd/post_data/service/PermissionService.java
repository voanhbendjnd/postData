package com.djnd.post_data.service;

import org.springframework.stereotype.Service;

import com.djnd.post_data.repository.PermissionRepository;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }
}
