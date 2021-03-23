package com.example.hobbie.service.impl;


import com.example.hobbie.model.entities.BusinessOwner;
import com.example.hobbie.model.entities.Hobby;
import com.example.hobbie.model.entities.UserRoleEntity;
import com.example.hobbie.model.entities.enums.CategoryNameEnum;
import com.example.hobbie.model.entities.enums.UserRoleEnum;
import com.example.hobbie.model.repostiory.HobbyRepository;
import com.example.hobbie.model.service.HobbyServiceModel;
import com.example.hobbie.model.service.UpdateHobbyServiceModel;
import com.example.hobbie.service.CategoryService;
import com.example.hobbie.service.HobbyService;
import com.example.hobbie.service.UserService;
import com.example.hobbie.util.FileUploadUtil;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
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

    @Override
    public void deleteHobby(long id) {
            this.hobbyRepository.deleteById(id);
    }

    @Override
    public void initHobbyOffers() {
        if(hobbyRepository.count() == 0) {
            //1
            Hobby climbing = new Hobby();
            climbing.setName("Climbing");
            climbing.setDescription("Rock climbing is a sport in which participants climb up, down or across natural rock formations or artificial rock walls. The goal is to reach the summit of a formation or the endpoint of a usually pre-defined route without falling. Rock climbing is a physically and mentally demanding sport, one that often tests a climber's strength, endurance, agility and balance along with mental control. Knowledge of proper climbing techniques and the use of specialized climbing equipment is crucial for the safe completion of routes.\n" +
                    "\n" +
                    "Because of the wide range and variety of rock formations around the world, rock climbing has been separated into several different styles and sub-disciplines, such as scrambling, another activity involving the scaling of hills and similar formations, differentiated by rock climbing's sustained use of hands to support the climber's weight as well as to provide balance.");

            climbing.setCategory(this.categoryService.findByName(CategoryNameEnum.ACTIVE));
            climbing.setBusinessOwner(this.userService.findBusinessOwnerById(3));
            climbing.setPrice(new BigDecimal("73"));
            climbing.setProfilePhoto("1.jpg");
            this.hobbyRepository.save(climbing);
            //2
            Hobby dancing = new Hobby();
            dancing.setName("Dancing");
            dancing.setDescription("Dance is a performing art form consisting of purposefully selected sequences of human movement. This movement has aesthetic and symbolic value, and is acknowledged as dance by performers and observers within a particular culture. Dance can be categorized and described by its choreography, by its repertoire of movements, or by its historical period or place of origin.\n" +
                    "\n" +
                    "An important distinction is to be drawn between the contexts of theatrical and participatory dance, although these two categories are not always completely separate; both may have special functions, whether social, ceremonial, competitive, erotic, martial, or sacred/liturgical. Other forms of human movement are sometimes said to have a dance-like quality, including martial arts, gymnastics, cheerleading, figure skating, synchronised swimming, marching bands, and many other forms of athletics.");

            dancing.setCategory(this.categoryService.findByName(CategoryNameEnum.FUN));
            dancing.setBusinessOwner(this.userService.findBusinessOwnerById(3));
            dancing.setPrice(new BigDecimal("62.40"));
            dancing.setProfilePhoto("2.jpg");
            this.hobbyRepository.save(dancing);
            //3
            Hobby horseRiding = new Hobby();
            horseRiding.setName("Horse riding");
            horseRiding.setDescription("What is Equestrian Tourism?\n" +
                    "The Equestrian Tourism is an activity that combines the passion for horse riding with the interest to visit different regions, provinces and countries, which allows to discover different cultures, other people and typical gastronomy.\n" +
                    "\n" +
                    "Is it difficult to ride a horse?\n" +
                    "The practice of horse riding is known as equitation and it is relatively simple to start with, at least to be able to go on rides in the countryside. If your interest grows further, you will want to improve your riding skills and acquire more knowledge about horses.\n" +
                    "\n" +
                    "How fast your skills improve depend on every individual and on your purpose, you can ride for pleasure in the nature in all types of terrain or choose an equestrian sport and train professionally.\n" +
                    "\n" );
            horseRiding.setCategory(this.categoryService.findByName(CategoryNameEnum.ACTIVE));
            horseRiding.setBusinessOwner(this.userService.findBusinessOwnerById(3));
            horseRiding.setPrice(new BigDecimal("162.20"));
            horseRiding.setProfilePhoto("3.jpg");
            this.hobbyRepository.save(horseRiding);

            //4
            Hobby yoga = new Hobby();
            yoga.setName("Yoga");
            yoga.setDescription("The origins of yoga are shrouded in the mists of time. The ancient wisdom known as \"the supreme science of life\" is believed to have been revealed to the great sages of India several thousand years ago.\n" +
                    "\n" +
                    "Yoga is an ancient system of physical and mental practices that originated during the Indus Valley civilization in South Asia. The fundamental purpose of yoga is to foster harmony in the body, mind, and environment.\n" +
                    "\n" +
                    "Yoga professes a complete system of physical, mental, social, and spiritual development. For generations, this philosophy was passed on from the master teacher to the student. The first written records of the practice of yoga appeared around 200 BC in Yogasutra of Patanjali. The system consisted of the eightfold path or Asthangayoga.");
            yoga.setCategory(this.categoryService.findByName(CategoryNameEnum.RELAX));
            yoga.setBusinessOwner(this.userService.findBusinessOwnerById(3));
            yoga.setPrice(new BigDecimal("52.40"));
            yoga.setProfilePhoto("4.jpg");
            this.hobbyRepository.save(yoga);

            //5
            Hobby painting = new Hobby();
            painting.setName("Painting");
            painting.setDescription("There are ten people per class, beginner or advanced. Students can bring their own personal projects. Here, nothing is compulsory, everything depends on the will of each person and you are free to choose your favorite material (oil, acrylic, watercolour, pastel, pencils, etc.).\n" +
                    "\n" +
                    "The teacher will be happy to advise you and teach you the different techniques necessary to achieve your project. The teacher will help to develop the students perception and feeling in order to create with ones personality and originality. For beginners, she will guide you through the different basic drawing or painting techniques and will help you find a project that you like. A small library is available to inspire you.\n" +
                    "\n" +
                    "Life of Art is a place where all senses are stimulated; natural daylight, incense and music to help you relax. The goal is to create an atmosphere where your mind feels at ease and is not blocking your sensitivity and your imagination. Over time, students refine their technique and creativity.");
            painting.setCategory(this.categoryService.findByName(CategoryNameEnum.CREATIVE));
            painting.setBusinessOwner(this.userService.findBusinessOwnerById(3));
            painting.setPrice(new BigDecimal("40"));
            painting.setProfilePhoto("5.jpg");
            this.hobbyRepository.save(painting);
        }

    }

}
