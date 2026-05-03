package com.re.luyentap.service;

import com.re.luyentap.model.Department;
import com.re.luyentap.model.Employee;
import com.re.luyentap.repository.DepartmentRepository;
import com.re.luyentap.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional(rollbackFor = Exception.class)
    public int deleteDepartmentSafely(Long deptId) {
        Department dept = departmentRepository.findById(deptId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng ban với ID: " + deptId));
        List<Employee> employees = employeeRepository.findByDepartmentId(deptId);
        int affectedEmployees = employees.size();
        for (Employee emp : employees) {
            emp.setDepartment(null);
        }
        employeeRepository.saveAll(employees);
        departmentRepository.delete(dept);

        return affectedEmployees;
    }
}