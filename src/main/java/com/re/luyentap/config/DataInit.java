package com.re.luyentap.config;

import com.re.luyentap.model.Department;
import com.re.luyentap.model.Employee;
import com.re.luyentap.repository.DepartmentRepository;
import com.re.luyentap.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) throws Exception {
        if (departmentRepository.count() == 0 && employeeRepository.count() == 0) {
            Department it = new Department(null, "Phong IT", "Tang 5", null);
            Department hr = new Department(null, "Phong Hanh chinh", "Tang 2", null);
            departmentRepository.saveAll(Arrays.asList(it, hr));

            Employee e1 = new Employee(null, "Nguyen Van Code", 25, "avatar1.png", "Dang lam", it);
            Employee e2 = new Employee(null, "Tran Thi Bug", 24, "avatar2.png", "Dang lam", it);
            Employee e3 = new Employee(null, "Le Hanh Chinh", 30, "avatar3.png", "Nghi phep", hr);

            employeeRepository.saveAll(Arrays.asList(e1, e2, e3));
            System.out.println(">>> Đã khởi tạo dữ liệu mẫu thành công!");
        }
    }
}