package com.itsmite.novels.core.controllers.security;

import com.itsmite.novels.core.annotations.JsonRequestMapping;
import com.itsmite.novels.core.payload.security.request.NewRoleRequest;
import com.itsmite.novels.core.services.security.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.itsmite.novels.core.constants.EndpointConstants.API_ROLES_V1_ENDPOINT;

@RestController
@RequestMapping(value = API_ROLES_V1_ENDPOINT)
public class RolesController {

    private RoleService roleService;

    @Autowired
    public void autowireBeans(RoleService roleService) {
        this.roleService = roleService;
    }

    @JsonRequestMapping(path = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createRole(@Valid @RequestBody NewRoleRequest newRoleRequest) {
        roleService.createRole(newRoleRequest.getRole());
    }
}
