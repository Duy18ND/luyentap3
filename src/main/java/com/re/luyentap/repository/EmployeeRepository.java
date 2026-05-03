package com.re.luyentap.repository;

import com.re.luyentap.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT e FROM Employee e WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:deptId IS NULL OR e.department.id = :deptId) AND " +
            "(:minAge IS NULL OR e.age >= :minAge) AND " +
            "(:maxAge IS NULL OR e.age <= :maxAge)")
    Page<Employee> searchDynamic(@Param("keyword") String keyword,
                                 @Param("deptId") Long deptId,
                                 @Param("minAge") Integer minAge,
                                 @Param("maxAge") Integer maxAge,
                                 Pageable pageable);
    List<Employee> findByDepartmentId(Long departmentId);
}