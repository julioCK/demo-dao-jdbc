package model.dao;

import model.entities.Department;

import java.util.List;

public interface DepartmentDao {

    void insert(Department department);
    void update(Department department);
    void deleteById(Department department);
    Department findById(Integer id);
    List<Department> findAll();
}
