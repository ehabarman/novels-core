package com.itsmite.novels.core.services.security;

import com.itsmite.novels.core.boot.SpringRunnerWithDataProvider;
import com.itsmite.novels.core.errors.exceptions.AlreadyUsedException;
import com.itsmite.novels.core.models.security.ERole;
import com.itsmite.novels.core.models.security.Role;
import com.itsmite.novels.core.repositories.security.RoleRepository;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import javax.ws.rs.InternalServerErrorException;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunnerWithDataProvider.class)
public class RoleServiceTest {

    @TestConfiguration
    static class RoleServiceTestConfiguration {
        @Bean
        public RoleService roleService() {
            return new RoleService();
        }
    }

    @Autowired
    private RoleService roleService;

    @MockBean
    private RoleRepository roleRepository;

    @Test
    public void createRoleTestSuccess() {
        ERole eRole = ERole.ADMIN;
        Role role = new Role(eRole);
        Mockito.when(roleRepository.existsByRole(eRole)).thenReturn(false);
        Mockito.when(roleRepository.save(any())).thenReturn(role);
        Role result = roleService.createRole(eRole);
        Assert.assertEquals(role, result);
    }

    @Test
    @UseDataProvider("createRoleDataProvider")
    public void createRoleTestFailure(ERole eRole, boolean exists, Exception internalException, Class expectedException) {
        Mockito.when(roleRepository.existsByRole(eRole)).thenReturn(exists);
        Mockito.when(roleRepository.save(any())).thenThrow(internalException);
        try {
            roleService.createRole(eRole);
            Assert.fail("Should have failed");
        } catch (Exception actualException) {
            Assert.assertEquals(expectedException, actualException.getClass());
        }
    }

    @DataProvider
    public static Object[][] createRoleDataProvider() {
        return new Object[][] {
            {ERole.ADMIN, true, new RuntimeException(), AlreadyUsedException.class},
            {ERole.BASIC, false, new InternalServerErrorException(), InternalServerErrorException.class}
        };
    }

}
