package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.constant.UserRole;
import com.enigma.wmbapi.entity.Role;
import com.enigma.wmbapi.repository.RoleRepository;
import com.enigma.wmbapi.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Role getOrSave(UserRole role) {
        return roleRepository.findByRole(role).orElseGet(()->roleRepository.saveAndFlush(Role.builder().role(role).build()));
    }
}
