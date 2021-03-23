package com.example.hobbie.service;

import com.example.hobbie.model.entities.AppClient;
import com.example.hobbie.model.entities.BusinessOwner;
import com.example.hobbie.model.entities.UserEntity;
import com.example.hobbie.model.service.RegisterBusinessServiceModel;
import com.example.hobbie.model.service.SignUpServiceModel;

public interface UserService {
    void seedUsersAndUserRoles();

    boolean register(SignUpServiceModel signUpServiceModel);
    BusinessOwner findCurrentUserBusinessOwner();
    AppClient findCurrentUserAppClient();
    String findCurrentUsername();
    boolean registerBusiness(RegisterBusinessServiceModel map);

    void saveUpdatedUser(BusinessOwner businessOwner);

    void saveUpdatedUserClient(AppClient appClient);

    UserEntity findUserById(Long userId);

    void deleteUser(Long id);

    BusinessOwner findBusinessOwnerById(long i);
}


