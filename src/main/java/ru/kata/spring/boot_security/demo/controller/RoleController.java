package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import ru.kata.spring.boot_security.demo.servise.UserService;


import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class RoleController {

    UserService userService;
    @Autowired
    public RoleController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUser());
        return "admin-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", new HashSet<Role>());
        return "user-form";
    }

    @PostMapping("/add")
    public String addUser (@RequestParam(value = "name") String name, @RequestParam(value = "lastName")
    String lastName, @RequestParam(value = "age") Integer age,@RequestParam(value = "password") String password, @RequestParam Set<String> roles) {
        User user = new User(name, lastName, age,password);
        userService.addUser(user,roles);
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam(value = "id",required = false) Long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "user-edit-form";
    }

    @PostMapping("/edit")
    public String editUser (@RequestParam(value = "id") Long id, @RequestParam(value = "name") String name, @RequestParam(value = "lastName")
    String lastName, @RequestParam(value = "age") Integer age,@RequestParam(value = "password") String password ) {
        User user = new User(name, lastName, age,password);
        userService.updateUser(id,user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser (@RequestParam(value = "id",required = false) Long id) {
        userService.deleteUser (id);
        return "redirect:/admin";

    }
}
