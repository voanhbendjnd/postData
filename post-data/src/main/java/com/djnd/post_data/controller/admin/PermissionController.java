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

import com.djnd.post_data.domain.entity.Permission;
import com.djnd.post_data.domain.response.ResultPaginationDTO;
import com.djnd.post_data.domain.response.crud.ResPermissionCreateDTO;
import com.djnd.post_data.domain.response.crud.ResPermissionUpdateDTO;
import com.djnd.post_data.service.PermissionService;
import com.djnd.post_data.utils.annotation.ApiMessage;
import com.djnd.post_data.utils.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("Create new permission")
    public ResponseEntity<ResPermissionCreateDTO> create(@Valid @RequestBody Permission permission)
            throws IdInvalidException {
        if (this.permissionService.existsByName(permission.getName())) {
            throw new IdInvalidException(">>> Name permission '" + permission.getName() + "' already exists! <<<");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.createNewPermission(permission));
    }

    @PutMapping("/permissions")
    @ApiMessage("Update permission")
    public ResponseEntity<ResPermissionUpdateDTO> update(@RequestBody Permission permission)
            throws IdInvalidException {
        if (!this.permissionService.existsById(permission.getId())) {
            throw new IdInvalidException(">>> Id already exist! <<<");
        }
        return ResponseEntity.ok(this.permissionService.updatePermission(permission));
    }

    @GetMapping("/permissions/{id}")
    @ApiMessage("Fetch by id")
    public ResponseEntity<ResPermissionCreateDTO> fetchById(@PathVariable("id") Long id) throws IdInvalidException {
        if (this.permissionService.existsById(id)) {
            return ResponseEntity.ok(this.permissionService.fetchById(id));
        }
        throw new IdInvalidException(">>> Id permission not exist! <<<");
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("Delete by id")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) throws IdInvalidException {
        if (this.permissionService.existsById(id)) {
            this.permissionService.deleteById(id);
            return ResponseEntity.ok(null);
        }
        throw new IdInvalidException(">>> Id permission not exist! <<<");
    }

    @GetMapping("/permissions")
    @ApiMessage("Fetch all")
    public ResponseEntity<ResultPaginationDTO> fetchAll(@Filter Specification<Permission> spec, Pageable page) {
        return ResponseEntity.ok(this.permissionService.fetchAll(page, spec));
    }
}
