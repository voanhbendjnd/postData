package com.djnd.post_data.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.djnd.post_data.domain.entity.Permission;
import com.djnd.post_data.domain.entity.Role;
import com.djnd.post_data.domain.entity.User;
import com.djnd.post_data.repository.PermissionRepository;
import com.djnd.post_data.repository.RoleRepository;
import com.djnd.post_data.repository.UserRepository;
import com.djnd.post_data.utils.constant.GenderEnum;

@Service
public class DatabaseInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(UserRepository userRepository, PermissionRepository permissionRepository,
            RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> Start init database<<<");
        Long cntPermissions = this.permissionRepository.count();
        Long cntRoles = this.roleRepository.count();
        Long cntUsers = this.userRepository.count();

        if (cntPermissions == 0) {
            List<Permission> list = new ArrayList<>();
            list.add(new Permission("Create a permission", "/api/v1/permissions", "POST", "PERMISSIONS"));
            list.add(new Permission("Update a permission", "/api/v1/permissions", "PUT", "PERMISSIONS"));
            list.add(new Permission("Delete a permission", "/api/v1/permissions/{id}", "DELETE", "PERMISSIONS"));
            list.add(new Permission("Fetch permission with id", "/api/v1/permissions/{id}", "GET", "PERMISSIONS"));
            list.add(new Permission("Fetch permission with pagination", "/api/v1/permissions", "GET", "PERMISSIONS"));

            list.add(new Permission("Create a role", "/api/v1/roles", "POST", "ROLES"));
            list.add(new Permission("Update a role", "/api/v1/roles", "PUT", "ROLES"));
            list.add(new Permission("Delete a role", "/api/v1/roles/{id}", "DELETE", "ROLES"));
            list.add(new Permission("Fetch role with id", "/api/v1/roles/{id}", "GET", "ROLES"));
            list.add(new Permission("Fetch role with pagination", "/api/v1/roles", "GET", "ROLES"));

            list.add(new Permission("Create a user", "/api/v1/users", "POST", "USERS"));
            list.add(new Permission("Update a user", "/api/v1/users", "PUT", "USERS"));
            list.add(new Permission("Delete a user", "/api/v1/users/{id}", "DELETE", "USERS"));
            list.add(new Permission("Fetch user with id", "/api/v1/users/{id}", "GET", "USERS"));
            list.add(new Permission("Fetch user with pagination", "/api/v1/users", "GET", "USERS"));

            this.permissionRepository.saveAll(list);
        }
        if (cntRoles == 0) {
            List<Permission> allPermissions = this.permissionRepository.findAll();
            Role adminRole = new Role();
            adminRole.setName("SUPER_ADMIN");
            adminRole.setDescription("Admin has full permissions");
            adminRole.setActive(true);
            adminRole.setPermissions(allPermissions);
            this.roleRepository.save(adminRole);
        }
        if (cntUsers == 0) {
            User adminUser = new User();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setAddress("Can Tho");
            adminUser.setAge(18);
            adminUser.setGender(GenderEnum.MALE);
            adminUser.setName("I'm super admin");
            adminUser.setPassword(this.passwordEncoder.encode("123456"));
            Role adminRole2 = this.roleRepository.findByName("SUPER_ADMIN");
            if (adminRole2 != null) {
                adminUser.setRole(adminRole2);
            }
            this.userRepository.save(adminUser);
        }
        if (cntPermissions != 0 & cntRoles != 0 && cntUsers != 0) {
            System.out.println(">>> SKIP <<<");
        } else {
            System.out.println(">>> END INIT DATABASE");
        }
    }

}
