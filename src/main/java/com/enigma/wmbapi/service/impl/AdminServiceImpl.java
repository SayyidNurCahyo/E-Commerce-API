package com.enigma.wmbapi.service.impl;

import com.enigma.wmbapi.dto.request.SearchAdminRequest;
import com.enigma.wmbapi.dto.request.SearchAdminRequest;
import com.enigma.wmbapi.dto.request.UpdateAdminRequest;
import com.enigma.wmbapi.dto.request.UpdateAdminRequest;
import com.enigma.wmbapi.dto.response.AdminResponse;
import com.enigma.wmbapi.dto.response.AdminResponse;
import com.enigma.wmbapi.entity.Admin;
import com.enigma.wmbapi.entity.Admin;
import com.enigma.wmbapi.repository.AdminRepository;
import com.enigma.wmbapi.repository.UserAccountRepository;
import com.enigma.wmbapi.service.AdminService;
import com.enigma.wmbapi.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addAdmin(Admin admin) {
        adminRepository.saveAndFlush(admin);
    }

    @Transactional(readOnly = true)
    @Override
    public AdminResponse getAdminById(String id) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Admin Not Found"));
        return convertToAdminResponse(admin);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AdminResponse> getAllAdmin(SearchAdminRequest request) {
        if (request.getPage()<1) request.setPage(1);
        if (request.getSize()<1) request.setSize(1);
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        if(request.getName()!=null || request.getPhone()!=null){
            Page<Admin> admins = adminRepository.findAdmin(request.getName(), request.getPhone(), page);
            return convertToPageAdminResponse(admins);
        }else {
            return convertToPageAdminResponse(adminRepository.findAll(page));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AdminResponse updateAdmin(UpdateAdminRequest request) {
        validationUtil.validate(request);
        Admin admin = adminRepository.findById(request.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Admin Not Found"));
        admin.getUserAccount().setUsername(request.getUsername());
        Admin adminNew = Admin.builder().id(admin.getId()).name(admin.getName())
                .phone(admin.getPhone()).userAccount(admin.getUserAccount()).build();
        adminRepository.saveAndFlush(adminNew);
        return convertToAdminResponse(adminNew);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AdminResponse deleteById(String id) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Admin Not Found"));
        adminRepository.delete(admin);
        return convertToAdminResponse(admin);
    }

    private AdminResponse convertToAdminResponse(Admin admin){
        return AdminResponse.builder()
                .adminId(admin.getId())
                .adminName(admin.getName())
                .adminPhone(admin.getPhone())
                .adminUsername(admin.getUserAccount().getUsername())
                .adminPassword(admin.getUserAccount().getPassword())
                .adminRole(admin.getUserAccount().getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
    }

    private Page<AdminResponse> convertToPageAdminResponse(Page<Admin> admins){
        return admins.map(this::convertToAdminResponse);
    }
}
