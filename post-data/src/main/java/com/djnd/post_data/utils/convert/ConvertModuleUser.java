package com.djnd.post_data.utils.convert;

import com.djnd.post_data.domain.entity.User;
import com.djnd.post_data.domain.response.crud.ResUserCreateDTO;
import com.djnd.post_data.domain.response.crud.ResUserUpdateDTO;

public class ConvertModuleUser {
    public static ResUserCreateDTO createdTran(User user) {
        ResUserCreateDTO res = new ResUserCreateDTO();
        res.setId(user.getId());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setGender(user.getGender());
        res.setCreatedAt(user.getCreatedAt());
        res.setCreatedBy(user.getCreatedBy());
        return res;
    }

    public static ResUserUpdateDTO updatedTran(User user) {
        ResUserUpdateDTO res = new ResUserUpdateDTO();
        res.setId(user.getId());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setGender(user.getGender());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setUpdatedBy(user.getUpdatedBy());
        return res;
    }
}
