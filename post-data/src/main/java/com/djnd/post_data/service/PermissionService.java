package com.djnd.post_data.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.djnd.post_data.domain.entity.Permission;
import com.djnd.post_data.repository.PermissionRepository;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<Permission> fetchListPermissionByIdIn(List<Long> ids) {
        return this.permissionRepository.findByIdIn(ids) != null ? this.permissionRepository.findByIdIn(ids) : null;
    }
}
