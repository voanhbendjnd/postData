package com.djnd.post_data.utils.convert;

import java.util.List;
import java.util.stream.Collectors;

import com.djnd.post_data.domain.entity.Role;
import com.djnd.post_data.domain.response.crud.ResRoleCreateDTO;
import com.djnd.post_data.domain.response.crud.ResRoleUpdateDTO;

public class ConvertModuleRole {
    public static ResRoleCreateDTO createTran(Role role) {
        ResRoleCreateDTO res = new ResRoleCreateDTO();
        res.setId(role.getId());
        res.setActive(role.getActive());
        res.setDescription(role.getDescription());
        res.setName(role.getName());
        res.setCreatedAt(role.getCreatedAt());
        res.setCreatedBy(role.getCreatedBy());
        if (role.getPermissions() != null) {
            List<ResRoleCreateDTO.permission> perList = role.getPermissions().stream()
                    .map(x -> new ResRoleCreateDTO.permission(x.getId(), x.getName()))
                    .collect(Collectors.toList());
            res.setPermission(perList);
        }
        return res;
    }

    public static ResRoleUpdateDTO updateTran(Role role) {
        ResRoleUpdateDTO res = new ResRoleUpdateDTO();
        res.setId(role.getId());
        res.setActive(role.getActive());
        res.setDescription(role.getDescription());
        res.setName(role.getName());
        res.setUpdatedAt(role.getUpdatedAt());
        res.setUpdatedBy(role.getUpdatedBy());
        if (role.getPermissions() != null) {
            List<ResRoleUpdateDTO.permission> perList = role.getPermissions().stream()
                    .map(x -> new ResRoleUpdateDTO.permission(x.getId(), x.getName()))
                    .collect(Collectors.toList());
            res.setPermission(perList);
        }
        return res;
    }
}
