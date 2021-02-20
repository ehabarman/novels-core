package com.itsmite.novels.core.payload.security.request;

import com.itsmite.novels.core.models.security.ERole;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class NewRoleRequest {

    @NotNull
    private ERole role;
}
