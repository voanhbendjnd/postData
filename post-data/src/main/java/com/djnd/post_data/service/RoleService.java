package com.djnd.post_data.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.djnd.post_data.domain.entity.Permission;
import com.djnd.post_data.domain.entity.Role;
import com.djnd.post_data.domain.request.role.RequestUpdateRoleDTO;
import com.djnd.post_data.domain.response.ResultPaginationDTO;
import com.djnd.post_data.domain.response.crud.ResRoleCreateDTO;
import com.djnd.post_data.domain.response.crud.ResRoleUpdateDTO;
import com.djnd.post_data.repository.RoleRepository;
import com.djnd.post_data.utils.SecurityUtils;
import com.djnd.post_data.utils.UpdateNotNull;
import com.djnd.post_data.utils.convert.ConvertModuleRole;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;

    public RoleService(RoleRepository roleRepository, PermissionService permissionService) {
        this.roleRepository = roleRepository;
        this.permissionService = permissionService;
    }

    public List<Role> findByIdIn(List<Long> ids) {
        return this.roleRepository.findByIdIn(ids);
    }

    public boolean existsById(Long id) {
        return this.roleRepository.existsById(id) ? true : false;
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

    public ResRoleCreateDTO create(Role role) {
        if (role.getPermissions() != null) {
            List<Long> listPermissionID = role.getPermissions().stream()
                    .map(Permission::getId)
                    .collect(Collectors.toList());
            List<Permission> listPermissionExist = this.permissionService.fetchListPermissionByIdIn(listPermissionID);
            if (listPermissionExist != null) {
                role.setPermissions(listPermissionExist);
            }
        }
        Role currentRole = this.roleRepository.save(role);
        return ConvertModuleRole.createTran(currentRole);
    }

    public ResRoleUpdateDTO update(Role role) {
        Role roleDB = this.roleRepository.findById(role.getId()).get();
        if (roleDB == null)
            return null;
        if (role.getPermissions() != null) {
            List<Long> listPermissionID = role.getPermissions().stream()
                    .map(Permission::getId)
                    .collect(Collectors.toList());
            List<Permission> perList = this.permissionService.fetchListPermissionByIdIn(listPermissionID);
            if (perList != null) {
                List<Permission> totalList = roleDB.getPermissions();
                totalList.addAll(perList);
                roleDB.setPermissions(totalList);
            }
        }
        UpdateNotNull.handle(role, roleDB);
        Role res = this.roleRepository.save(roleDB);
        return ConvertModuleRole.updateTran(res);

    }

    public String getEmail() {
        return SecurityUtils.getCurrentUserLogin().get();
    }

    public ResRoleUpdateDTO updateNew(RequestUpdateRoleDTO dto) {
        Role roleDB = this.roleRepository.findById(dto.getId()).get();
        if (roleDB == null) {
            return null;
        }
        Role role = new Role();
        role.setId(dto.getId());
        role.setActive(dto.getActive());
        role.setDescription(dto.getDescription());
        role.setName(dto.getName());
        role.setPermissions(dto.getPermissions());
        if (dto.getCondition().equals("add")) {
            if (dto.getPermissions() != null) {
                List<Long> listPermissionID = dto.getPermissions().stream()
                        .map(x -> x.getId())
                        .collect(Collectors.toList());
                List<Permission> perList = this.permissionService.fetchListPermissionByIdIn(listPermissionID);
                if (perList != null) {
                    List<Permission> totalList = roleDB.getPermissions();
                    totalList.addAll(perList);
                    role.setPermissions(totalList);
                }

            }
        } else if (dto.getCondition().equals("remove") && dto.getPermissions() != null) {
            List<Long> listPermissionID = dto.getPermissions().stream()
                    .map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Permission> perList = this.permissionService.fetchListPermissionByIdIn(listPermissionID);
            if (perList != null) {
                List<Permission> totalList = roleDB.getPermissions();
                boolean check = totalList.removeAll(perList);
                if (check || !perList.isEmpty()) {
                    role.setPermissions(totalList);
                }
            }
        }
        roleDB.setUpdatedAt(Instant.now());
        roleDB.setUpdatedBy(this.getEmail());
        UpdateNotNull.handle(role, roleDB);
        Role res = this.roleRepository.save(roleDB);
        return ConvertModuleRole.updateTran(res);
    }

    public ResRoleCreateDTO fetchById(Long id) {
        Role role = this.roleRepository.findById(id).get();
        return ConvertModuleRole.createTran(role);
    }

    public void deleteById(Long id) {
        this.roleRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAll(Pageable pageable, Specification<Role> spec) {
        Page<Role> page = this.roleRepository.findAll(pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(page.getTotalPages());
        mt.setTotal(page.getTotalElements());
        List<ResRoleCreateDTO> listRole = page.getContent().stream()
                .map(ConvertModuleRole::createTran)
                .collect(Collectors.toList());
        res.setMeta(mt);
        res.setResult(listRole);
        return res;
    }

}
