package com.re.luyentap.controller;

import com.re.luyentap.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    @GetMapping({"/", "/list"})
    public String showList(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        return "list";
    }
}