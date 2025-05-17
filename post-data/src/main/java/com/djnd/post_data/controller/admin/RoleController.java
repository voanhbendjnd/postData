package com.djnd.post_data.controller.admin;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.djnd.post_data.domain.entity.Role;
import com.djnd.post_data.domain.request.role.RequestUpdateRoleDTO;
import com.djnd.post_data.domain.response.ResultPaginationDTO;
import com.djnd.post_data.domain.response.crud.ResRoleCreateDTO;
import com.djnd.post_data.domain.response.crud.ResRoleUpdateDTO;
import com.djnd.post_data.service.RoleService;
import com.djnd.post_data.utils.annotation.ApiMessage;
import com.djnd.post_data.utils.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import jakarta.validation.executable.ValidateOnExecution;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("Create new a role")
    public ResponseEntity<ResRoleCreateDTO> create(@Valid @RequestBody Role role) throws IdInvalidException {
        if (this.roleService.existsByName(role.getName())) {
            throw new IdInvalidException(">>> Name role already exists <<<");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.create(role));
    }

    @PutMapping("/roles")
    @ApiMessage("Update role")
    public ResponseEntity<ResRoleUpdateDTO> update(@Valid @RequestBody RequestUpdateRoleDTO role)
            throws IdInvalidException {
        if (this.roleService.existsById(role.getId())) {
            return ResponseEntity.ok(this.roleService.updateNew(role));
        }
        throw new IdInvalidException(">>> Id not exist <<<");
    }

    @GetMapping("/roles/{id}")
    @ApiMessage("Fetch role by id")
    public ResponseEntity<ResRoleCreateDTO> fetchById(@Valid @PathVariable("id") Long id) throws IdInvalidException {
        if (this.roleService.existsById(id)) {
            return ResponseEntity.ok(this.roleService.fetchById(id));
        }
        throw new IdInvalidException(">>> Id not exist! <<< ");
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Delete role by id")
    public ResponseEntity<Void> deleteById(@Valid @PathVariable("id") Long id) throws IdInvalidException {
        if (this.roleService.existsById(id)) {
            this.roleService.deleteById(id);
            return ResponseEntity.ok(null);
        }
        throw new IdInvalidException(">>> Id not exist! <<<");
    }

    @GetMapping("/roles")
    @ApiMessage("Fetch all role")
    public ResponseEntity<ResultPaginationDTO> fetchAll(@Filter Specification<Role> spec, Pageable pageable) {
        return ResponseEntity.ok(this.roleService.fetchAll(pageable, spec));
    }
}