package com.example.hobbie.service.impl;


import com.example.hobbie.model.entities.BusinessOwner;
import com.example.hobbie.model.entities.Hobby;
import com.example.hobbie.model.entities.UserRoleEntity;
import com.example.hobbie.model.entities.enums.UserRoleEnum;
import com.example.hobbie.model.repostiory.HobbyRepository;
import com.example.hobbie.model.service.HobbyServiceModel;
import com.example.hobbie.model.service.UpdateHobbyServiceModel;
import com.example.hobbie.service.CategoryService;
import com.example.hobbie.service.HobbyService;
import com.example.hobbie.service.UserService;
import com.example.hobbie.util.FileUploadUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@Service
public class HobbyServiceImpl implements HobbyService {
    private final ModelMapper modelMapper;
    private final HobbyRepository hobbyRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    @Autowired
    public HobbyServiceImpl(ModelMapper modelMapper, HobbyRepository hobbyRepository, CategoryService categoryService, UserService userService) {
        this.modelMapper = modelMapper;
        this.hobbyRepository = hobbyRepository;
        this.categoryService = categoryService;
        this.userService = userService;
    }


    @Override
    public Long createHobby(HobbyServiceModel hobbyServiceModel,  String fileName) {
                    //todo check why hobbie ids keep incrementing

                                Hobby hobby = this.modelMapper.map(hobbyServiceModel, Hobby.class);
                    hobby.setCategory(this.categoryService.findByName(hobbyServiceModel.getCategory()));
                    hobby.setPhotos(fileName);
                    hobby.setBusinessOwner(this.userService.findCurrentUserBusinessOwner());
                    Hobby savedHobby = this.hobbyRepository.save(hobby);
                    return savedHobby.getId();

    }

    @Override
    public List<Hobby> getAllHobbyOffers() {
        BusinessOwner currentUserBusinessOwner = this.userService.findCurrentUserBusinessOwner();
      return   this.hobbyRepository.findAllByBusinessOwnerBusinessName(currentUserBusinessOwner.getBusinessName());
    }

    @Override
    public Hobby findHobbieById(Long id) {
        Optional<Hobby> hobby = this.hobbyRepository.findById(id);
        if(hobby.isPresent()){
            return hobby.get();
        }
        else {
            throw new NullPointerException();
        }
    }

    @Override
    public void saveUpdatedHobby(UpdateHobbyServiceModel updateHobbyServiceModel, String fileName) {

        Hobby hobby = this.modelMapper.map(updateHobbyServiceModel, Hobby.class);
        hobby.setCategory(this.categoryService.findByName(updateHobbyServiceModel.getCategory()));
        hobby.setPhotos(fileName);
        hobby.setBusinessOwner(this.userService.findCurrentUserBusinessOwner());
       this.hobbyRepository.save(hobby);

    }

}
