package com.re.luyentap.controller;

import com.re.luyentap.model.Employee;
import com.re.luyentap.repository.DepartmentRepository;
import com.re.luyentap.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class EmployeeController {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @GetMapping({"/", "/list"})
    public String showList(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        return "list";
    }


    // 1. Hiển thị Form thêm mới
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentRepository.findAll());
        return "form";
    }

    // 2. Xử lý lưu nhân viên và Upload file
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee employee,
                               @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                String uploadDir = "./uploads/";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                file.transferTo(new File(dir.getAbsolutePath() + File.separator + fileName));

                employee.setAvatar(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            employee.setAvatar("defazult-avatar.png");
        }

        employeeRepository.save(employee);
        return "redirect:/";
    }
}