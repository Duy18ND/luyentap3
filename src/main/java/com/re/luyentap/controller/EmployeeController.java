package com.re.luyentap.controller;

import com.re.luyentap.model.Employee;
import com.re.luyentap.repository.DepartmentRepository;
import com.re.luyentap.repository.EmployeeRepository;
import com.re.luyentap.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;

    @GetMapping({"/", "/list"})
    public String showList(Model model,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "id") String sortField,
                           @RequestParam(defaultValue = "desc") String sortDir,
                           @RequestParam(required = false) String keyword,
                           @RequestParam(required = false) Long deptId,
                           @RequestParam(required = false) Integer minAge,
                           @RequestParam(required = false) Integer maxAge) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        int pageSize = 5;
        int currentPage = page < 1 ? 1 : page;
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, sort);

        Page<Employee> pageResult = employeeRepository.searchDynamic(keyword, deptId, minAge, maxAge, pageable);

        if (currentPage > pageResult.getTotalPages() && pageResult.getTotalPages() > 0) {
            currentPage = pageResult.getTotalPages();
            pageable = PageRequest.of(currentPage - 1, pageSize, sort);
            pageResult = employeeRepository.searchDynamic(keyword, deptId, minAge, maxAge, pageable);
        }

        model.addAttribute("employees", pageResult.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("totalItems", pageResult.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);
        model.addAttribute("deptId", deptId);
        model.addAttribute("minAge", minAge);
        model.addAttribute("maxAge", maxAge);
        model.addAttribute("departments", departmentRepository.findAll());

        return "list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentRepository.findAll());
        return "form";
    }

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
            employee.setAvatar("default-avatar.png");
        }

        employeeRepository.save(employee);
        return "redirect:/list";
    }

    @GetMapping("/employees/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeRepository.deleteById(id);
        return "redirect:/list";
    }

    @GetMapping("/departments/delete/{id}")
    public String deleteDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            int affectedCount = departmentService.deleteDepartmentSafely(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa phòng ban và cập nhật trạng thái cho " + affectedCount + " nhân viên.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa phòng ban: " + e.getMessage());
        }
        return "redirect:/list";
    }
}