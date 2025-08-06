//package com.example.schoolmanagement.config;
//
//import com.example.schoolmanagement.model.Role;
//import com.example.schoolmanagement.model.RoleName;
//import com.example.schoolmanagement.repository.RoleRepository;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DataInitializer {
//
//    private final RoleRepository roleRepository;
//
//    public DataInitializer(RoleRepository roleRepository) {
//        this.roleRepository = roleRepository;
//    }
//
//    @PostConstruct
//    public void init() {
//        for (RoleName roleName : RoleName.values()) {
//            roleRepository.findByName(roleName).orElseGet(() -> {
//                Role role = new Role();
//                role.setName(roleName);
//                return roleRepository.save(role);
//            });
//        }
//    }
//}
