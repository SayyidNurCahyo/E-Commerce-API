package com.enigma.wmbapi.service;

import com.enigma.wmbapi.constant.UserRole;
import com.enigma.wmbapi.entity.Role;

public interface RoleService {
    Role getOrSave(UserRole role);
}
