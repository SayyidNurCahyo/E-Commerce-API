package com.enigma.wmbapi.service;

import com.enigma.wmbapi.dto.request.SearchAdminRequest;
import com.enigma.wmbapi.dto.request.SearchCustomerRequest;
import com.enigma.wmbapi.dto.request.UpdateAdminRequest;
import com.enigma.wmbapi.dto.request.UpdateCustomerRequest;
import com.enigma.wmbapi.dto.response.AdminResponse;
import com.enigma.wmbapi.dto.response.CustomerResponse;
import com.enigma.wmbapi.entity.Admin;
import com.enigma.wmbapi.entity.Customer;
import org.springframework.data.domain.Page;

public interface AdminService {
    void addAdmin(Admin admin);
    AdminResponse getAdminById(String id);
    Page<AdminResponse> getAllAdmin(SearchAdminRequest request);
    AdminResponse updateAdmin(UpdateAdminRequest admin);
    AdminResponse deleteById(String id);
}
