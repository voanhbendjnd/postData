package com.djnd.post_data.utils.convert;

import java.util.List;
import java.util.stream.Collectors;

import com.djnd.post_data.domain.entity.Permission;
import com.djnd.post_data.domain.response.crud.ResPermissionCreateDTO;
import com.djnd.post_data.domain.response.crud.ResPermissionUpdateDTO;

public class ConvertModulePermission {
    public static ResPermissionCreateDTO createTran(Permission permission) {
        ResPermissionCreateDTO res = new ResPermissionCreateDTO();
        res.setApiPath(permission.getApiPath());
        res.setCreatedAt(permission.getCreatedAt());
        res.setCreatedBy(permission.getCreatedBy());
        res.setId(permission.getId());
        res.setMethod(permission.getMethod());
        res.setModule(permission.getModule());
        res.setName(permission.getName());
        if (permission.getRoles() != null) {
            List<ResPermissionCreateDTO.Role> roleLists = permission.getRoles().stream()
                    .map(x -> new ResPermissionCreateDTO.Role(x.getId(), x.getName()))
                    .collect(Collectors.toList());
            res.setRoles(roleLists);
        }
        return res;
    }

    public static ResPermissionUpdateDTO updateTran(Permission permission) {
        ResPermissionUpdateDTO res = new ResPermissionUpdateDTO();
        res.setApiPath(permission.getApiPath());
        res.setUpdatedAt(permission.getUpdatedAt());
        res.setUpdatedBy(permission.getUpdatedBy());
        res.setId(permission.getId());
        res.setMethod(permission.getMethod());
        res.setModule(permission.getModule());
        res.setName(permission.getName());
        if (permission.getRoles() != null) {
            List<ResPermissionUpdateDTO.Role> roleLists = permission.getRoles().stream()
                    .map(x -> new ResPermissionUpdateDTO.Role(x.getId(), x.getName()))
                    .collect(Collectors.toList());
            res.setRoles(roleLists);
        }
        return res;
    }
}
