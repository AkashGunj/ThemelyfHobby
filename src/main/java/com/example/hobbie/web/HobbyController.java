package com.example.hobbie.web;


import com.example.hobbie.model.binding.HobbyBindingModel;
import com.example.hobbie.model.binding.UpdateHobbyBindingModel;
import com.example.hobbie.model.entities.Hobby;
import com.example.hobbie.model.service.HobbyServiceModel;
import com.example.hobbie.model.service.UpdateHobbyServiceModel;
import com.example.hobbie.service.HobbyService;
import com.example.hobbie.util.FileUploadUtil;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Controller
@RequestMapping("/hobbies")
public class HobbyController {

    private final ModelMapper modelMapper;
    private final HobbyService hobbyService;

    @Autowired
    public HobbyController(ModelMapper modelMapper, HobbyService hobbyService) {
        this.modelMapper = modelMapper;
        this.hobbyService = hobbyService;
    }


    @GetMapping("/create_offer")
    public ModelAndView showCreateOffer(Model model) {
        if(!model.containsAttribute("hobbyBindingModel")){
            model.addAttribute("hobbyBindingModel", new HobbyBindingModel());
            model.addAttribute("isExists",false);
            model.addAttribute("noImg",false);
        }
        ModelAndView mav= new ModelAndView("create_offer");
        return mav;

    }

    @PostMapping("/create_offer")
    public RedirectView saveHobby(@Valid HobbyBindingModel hobbyBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes,

                                  @RequestParam("image") MultipartFile multipartFile) throws IOException {

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        if (bindingResult.hasErrors() || fileName.isBlank()) {
            redirectAttributes.addFlashAttribute("hobbyBindingModel", hobbyBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.hobbyBindingModel", bindingResult);
            if(fileName.isBlank()){
                redirectAttributes.addFlashAttribute("noImg",true);
            }

            return new RedirectView("/hobbies/create_offer", true);
        } else {

            Long id = this.hobbyService.createHobby(this.modelMapper.map(hobbyBindingModel, HobbyServiceModel.class), fileName);

            String uploadDir = "hobby-photos/" + id;

            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }

        return new RedirectView("/business_owner", true);
    }
    @GetMapping("/hobbie-details/{id}")
    public String showHome(@PathVariable Long id, Model model){
        Hobby hobby = this.hobbyService.findHobbieById(id);
        model.addAttribute("hobbie", hobby);
        return "hobbie-details";
    }

    @GetMapping("/update-hobby/{id}")
    public String showUpdateHobbyForm(@PathVariable("id") long id, Model model) {
        Hobby hobbie = this.hobbyService.findHobbieById(id);
        UpdateHobbyBindingModel updateHobbyBindingModel = this.modelMapper.map(hobbie, UpdateHobbyBindingModel.class);
        model.addAttribute("updateHobbyBindingModel", updateHobbyBindingModel);
        model.addAttribute("noImg2",false);

        return "update-hobby";
    }

        @PostMapping("/update-hobby/{id}")
        public String updateHobby(@PathVariable("id") long id,@Valid UpdateHobbyBindingModel updateHobbyBindingModel , BindingResult bindingResult, RedirectAttributes redirectAttributes,

                                        @RequestParam("image") MultipartFile multipartFile) throws IOException {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

            if (bindingResult.hasErrors() || fileName.isBlank()) {
                redirectAttributes.addFlashAttribute("updateHobbyBindingModel", updateHobbyBindingModel);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.updateHobbyBindingModel", bindingResult);
                if(fileName.isBlank()){
                    redirectAttributes.addFlashAttribute("noImg2",true);
                }
                return "update-hobby";
            } else {
                updateHobbyBindingModel.setId(id);
                this.hobbyService.saveUpdatedHobby(this.modelMapper.map(updateHobbyBindingModel, UpdateHobbyServiceModel.class), fileName);

                String uploadDir = "hobby-photos/" + id;

                if(Files.exists(Path.of(uploadDir))) {
                    FileUtils.cleanDirectory(new File(uploadDir));
                }
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            }

            return "redirect:/hobbies/hobbie-details/{id}";
        }
}



