package Application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {
    public static void main(String[] args) {

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

//        System.out.println("=== TEST 1: Department insert ===");
//        Department dept = new Department(null, "Infrastructure");
//        departmentDao.insert(dept);
//        System.out.println("Department ID: " + dept.getId() + " inserted successfully!");

        System.out.println("=== TEST 1: Department DeleteById ===");
        Department dept = new Department(11, "Infrastructure");
        departmentDao.deleteById(dept);
        System.out.println("Department successfully excluded.");
    }
}
