package com.example.amdaeus.controller;

import com.example.amdaeus.dto.BtrFormDto;
import com.example.amdaeus.dto.BussinessTripRequestsDTO;
import com.example.amdaeus.dto.RegisterUserDto;
import com.example.amdaeus.dto.UserDto;
import com.example.amdaeus.entity.User;
import com.example.amdaeus.mappers.BTRMapper;
import com.example.amdaeus.mappers.UserMapper;
import com.example.amdaeus.service.BussinessTripRequestService;
import com.example.amdaeus.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
import java.util.Optional;

@Controller
public class ViewController {

    private final UserService userService;
    private final BussinessTripRequestService btrService;
    private final BTRMapper btrMapper;

    public ViewController(UserService userService, BussinessTripRequestService btrService, BTRMapper btrMapper) {
        this.userService = userService;
        this.btrService = btrService;
        this.btrMapper = btrMapper;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // templates/login.html
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {

        if (authentication == null) {
            return "redirect:/login";
        }
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN_ROLE"));
        boolean isApprover = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_APPROVER_ROLE"));

        if (isAdmin) {
            return "admin-dashboard";
        } else if (isApprover) {
            return "approver-dashboard";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/btr/new")
    public String newBtrForm(Model model) {
        model.addAttribute("btr", new BtrFormDto());
        return "btr-form";
    }

    @GetMapping("/approver-dashboard")
    public String getApproverDashboard(Model model) {
        List<BussinessTripRequestsDTO> pendingBTRs = btrService.getPendingBTRsForApproval()
                .stream()
                .map(btrMapper::toDTO)
                .toList();
        model.addAttribute("requests", pendingBTRs); // Thymeleaf: ${requests}
        return "approver-dashboard";
    }

    @GetMapping("/admin/user/{id}")
    public String userDetails(@PathVariable("id") Long id, Model model) {
        Optional<User> optionalUser = userService.getUserById(id);

        if (optionalUser.isPresent()) {
            model.addAttribute("user", optionalUser.get()); // <- tu bierzemy obiekt User
            return "user-details";
        } else {
            return "redirect:/admin/users";
        }
    }

    @PostMapping("/btr/submit")
    public String submitBtrForm(@ModelAttribute BussinessTripRequestsDTO dto, Model model) {
        User user = userService.getCurrentAuthenticatedUser();
        btrService.createBTRWithValidation(dto, user);
        return "redirect:btr-success.html";
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/admin-dashboard")
    public String adminDashboard(Model model) {
        List<UserDto> users = userService.getAllUsers()
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();

        model.addAttribute("users", users);
        return "admin-dashboard";
    }

    @GetMapping("/approver/approve/{id}")
    public String approveBTR(@PathVariable("id") Long id) {
        btrService.approveBTR(id);
        return "redirect:/approver-dashboard";
    }

    @GetMapping("/approver/reject/{id}")
    public String rejectBTR(@PathVariable("id") Long id) {
        btrService.rejectBTR(id);
        return "redirect:/approver-dashboard";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerForm", new RegisterUserDto("", "", "", "", ""));
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegisterUserDto registerUserDto, Model model) {
        System.out.println("Rejestracja użytkownika: " + registerUserDto);
        model.addAttribute("registerForm", registerUserDto);
        model.addAttribute("successMessage", "Użytkownik zarejestrowany!");
        return "register";
    }

    @GetMapping("/threshold-page")
    public String thresholdPage() {
        return "threshold";
    }

    @GetMapping("/admin-btrs")
    public String adminBtrsPage(Authentication auth) {
        System.out.println("Authorities: " + auth.getAuthorities());
        return "admin-btrs";
    }
}
