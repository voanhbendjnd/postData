package com.djnd.post_data.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.djnd.post_data.domain.entity.Permission;
import com.djnd.post_data.domain.entity.Role;
import com.djnd.post_data.domain.response.ResultPaginationDTO;
import com.djnd.post_data.domain.response.crud.ResPermissionCreateDTO;
import com.djnd.post_data.domain.response.crud.ResPermissionUpdateDTO;
import com.djnd.post_data.repository.PermissionRepository;
import com.djnd.post_data.repository.RoleRepository;
import com.djnd.post_data.utils.UpdateNotNull;
import com.djnd.post_data.utils.convert.ConvertModulePermission;

@Service
public class PermissionService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository, RoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }

    public List<Permission> fetchListPermissionByIdIn(List<Long> ids) {
        return this.permissionRepository.findByIdIn(ids) != null ? this.permissionRepository.findByIdIn(ids) : null;
    }

    public ResPermissionCreateDTO createNewPermission(Permission permission) {
        if (permission.getRoles() != null) {
            List<Long> listIdRole = permission.getRoles().stream()
                    .map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Role> roleLists = this.roleRepository.findByIdIn(listIdRole);
            if (roleLists != null) {
                permission.setRoles(roleLists);
            }
        }
        Permission per = this.permissionRepository.save(permission);
        return ConvertModulePermission.createTran(per);
    }

    public boolean existsByName(String name) {
        return this.permissionRepository.existsByName(name) ? true : false;
    }

    public ResPermissionUpdateDTO updatePermission(Permission permission) {
        Permission per = this.permissionRepository.findById(permission.getId()).get();
        UpdateNotNull.handle(permission, per);
        Permission res = this.permissionRepository.save(per);
        return ConvertModulePermission.updateTran(res);
    }

    public boolean existsById(Long id) {
        return this.permissionRepository.existsById(id) ? true : false;
    }

    public ResPermissionCreateDTO fetchById(Long id) {
        Permission per = this.permissionRepository.findById(id).get();
        return ConvertModulePermission.createTran(per);
    }

    public void deleteById(Long id) {
        this.permissionRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAll(Pageable pageable, Specification<Permission> spec) {
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        Page<Permission> page = this.permissionRepository.findAll(pageable);
        mt.setPages(page.getTotalPages());
        mt.setTotal(page.getTotalElements());
        List<ResPermissionCreateDTO> perList = page.getContent().stream()
                .map(ConvertModulePermission::createTran)
                .collect(Collectors.toList());
        res.setMeta(mt);
        res.setResult(perList);
        return res;
    }

}
