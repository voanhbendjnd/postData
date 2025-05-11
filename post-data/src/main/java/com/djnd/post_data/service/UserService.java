package com.djnd.post_data.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.djnd.post_data.domain.entity.Permission;
import com.djnd.post_data.domain.entity.Role;
import com.djnd.post_data.domain.entity.User;
import com.djnd.post_data.domain.request.user.RequestCreateDTO;
import com.djnd.post_data.domain.response.ResultPaginationDTO;
import com.djnd.post_data.domain.response.crud.ResUserCreateDTO;
import com.djnd.post_data.domain.response.crud.ResUserUpdateDTO;
import com.djnd.post_data.repository.UserRepository;
import com.djnd.post_data.utils.UpdateNotNull;
import com.djnd.post_data.utils.convert.ConvertModuleUser;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PermissionService permissionService;

    public UserService(UserRepository userRepository,
            RoleService roleService,
            PermissionService permissionService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    public User handleGetUserByUsername(String email) {
        return this.userRepository.findByEmail(email);
    }

    public void updateUserToken(String token, String email) {
        User user = this.userRepository.findByEmail(email);
        if (user != null) {
            user.setRefreshToken(token);
            this.userRepository.save(user);
        }
    }

    public User fetchUserByRefreshTokenAndEmail(String re, String email) {
        Optional<User> userOp = Optional.ofNullable(this.userRepository.findByRefreshTokenAndEmail(re, email));
        return userOp.isPresent() ? userOp.get() : null;
    }

    public void update(User user) {
        Optional<User> userOptional = this.userRepository.findById(user.getId());
        if (userOptional.isPresent()) {
            UpdateNotNull.handle(user, userOptional.get());
            this.userRepository.save(userOptional.get());
        }
    }

    public void deleteUserById(Long id) {
        this.userRepository.deleteById(id);
    }

    public ResUserCreateDTO createNewUser(RequestCreateDTO user) {
        User userNew = new User();
        userNew.setAddress(user.getAddress());
        userNew.setAge(user.getAge());
        userNew.setPassword(user.getPassword());
        userNew.setEmail(user.getEmail());
        userNew.setGender(user.getGender());
        userNew.setName(user.getName());
        Role role = this.roleService.findByName(user.getRole());
        if (role != null) {
            userNew.setRole(role);
        }
        User currentUser = this.userRepository.save(userNew);
        return ConvertModuleUser.createdTran(currentUser);
    }

    public ResUserUpdateDTO updateUser(RequestCreateDTO user) {
        Optional<User> userOptional = userRepository.findById(user.getId());
        if (userOptional.isPresent()) {
            User dbUser = userOptional.get();
            Role role = this.roleService.findByName(user.getRole());
            if (role != null) {
                dbUser.setRole(role);
            }
            UpdateNotNull.handle(user, dbUser);
            User resUser = this.userRepository.save(dbUser);
            return ConvertModuleUser.updatedTran(resUser);
        }
        return null;
    }

    public boolean existsById(Long id) {
        return this.userRepository.existsById(id) ? true : false;
    }

    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email) ? true : false;
    }

    public ResUserCreateDTO fetchUserById(Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return ConvertModuleUser.createdTran(userOptional.get());
        }
        return null;
    }

    public ResultPaginationDTO fetchAll(Specification<User> spec, Pageable pageable) {
        Page<User> page = this.userRepository.findAll(pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(page.getTotalPages());
        mt.setTotal(page.getTotalElements());
        res.setMeta(mt);
        List<ResUserCreateDTO> listUserRes = page.getContent().stream()
                .map(ConvertModuleUser::createdTran)
                .collect(Collectors.toList());
        res.setResult(listUserRes);
        return res;
    }
}
