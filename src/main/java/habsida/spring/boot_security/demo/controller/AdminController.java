package habsida.spring.boot_security.demo.controller;

import habsida.spring.boot_security.demo.dto.UserForm;
import habsida.spring.boot_security.demo.repository.RoleRepository;
import habsida.spring.boot_security.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;




@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService service;
    private final RoleRepository roleRepository;

    public AdminController(UserService service, RoleRepository roleRepository) {
        this.service = service;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String adminPage(Model model, Principal principal) {
        model.addAttribute("users", service.findAll());
        model.addAttribute("currentUsername", principal.getName());

        model.addAttribute("userForm", new UserForm());
        model.addAttribute("allRoles", roleRepository.findAll());

        return "admin";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("userForm", new UserForm());
        model.addAttribute("allRoles", roleRepository.findAll());
        return "create";
    }

//    @GetMapping("/edit")
//    public String editUser(@RequestParam Long id, Model model) {
//        model.addAttribute("userForm", service.toForm(id));
//        model.addAttribute("allRoles", roleRepository.findAll());
//        return "edit";
//    }

    @ModelAttribute("currentUsername")
    public String currentUsername(Principal principal) {
        return principal.getName();
    }

    @PostMapping("/save")
    public String saveUser(
            @Valid @ModelAttribute("userForm") UserForm userForm,
            BindingResult bindingResult,
            @RequestParam(required = false) String newPassword,
            Model model,
            Principal principal) {

        if(userForm.getAge() == null) {
            bindingResult.rejectValue("age", "age.empty", "Age is required");
        }

        if (userForm.getId() == null) {

            if (userForm.getPassword() == null || userForm.getPassword().isBlank()) {
                bindingResult.rejectValue("password", "password.empty", "Password is required");
            }

            if (userForm.getRoleIds() == null || userForm.getRoleIds().isEmpty()) {
                bindingResult.rejectValue("roleIds", "roleIds.empty", "Choose at least one role");
            }

        } else {

            if (userForm.getRoleIds() == null || userForm.getRoleIds().isEmpty()) {
                bindingResult.rejectValue("roleIds", "roleIds.empty", "Choose at least one role");
            }
        }


        System.out.println(bindingResult.getAllErrors());
        if (bindingResult.hasErrors()) {

            model.addAttribute("users", service.findAll());
            model.addAttribute("userForm", userForm);

            return "admin";
        }

        if (userForm.getId() == null) {
            service.save(userForm);
        } else {
            service.update(userForm, newPassword);
        }

        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long id) {
        service.delete(id);
        return "redirect:/admin";
    }


}

