package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.servise.UserService;


import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping
//    public String listUsers(Model model) {
//        model.addAttribute("users", userService.getAllUser());
//        return "admin-list";
//    }

    @GetMapping
    public String showAddForm(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Optional.of(SecurityContextHolder.getContext()).stream()
                .map(SecurityContext::getAuthentication)
                .filter(Objects::nonNull)
                .map(Authentication::getAuthorities)
                .flatMap(Collection::stream)
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        model.addAttribute("users", userService.getAllUser());
        return "user-form";
    }

    @PostMapping("/add")
    public String addUser(@RequestParam(value = "name") String name,
                          @RequestParam(value = "lastName") String lastName,
                          @RequestParam(value = "age") Integer age,
                          @RequestParam(value = "password") String password,
                          @RequestParam(value = "roles") Set<String> roles) {
        User user = new User(name, lastName, age, password);
        userService.addUser(user, roles);
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam(value = "id", required = false) Long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("roles", new HashSet<Role>());
        return "user-form";
    }

    @PostMapping("/edit")
    public String editUser(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "name") String name, @RequestParam(value = "lastName")
    String lastName, @RequestParam(value = "age") Integer age, @RequestParam(value = "password") String password) {
        User user = new User(name, lastName, age, password);
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam(value = "id", required = false) Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";

    }
}
