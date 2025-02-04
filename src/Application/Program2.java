package Application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.util.List;

public class Program2 {
    public static void main(String[] args) {

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

//        System.out.println("=== TEST 1: Department insert ===");
//        Department dept = new Department(null, "Infrastructure");
//        departmentDao.insert(dept);
//        System.out.println("Department ID: " + dept.getId() + " inserted successfully!");

//        System.out.println("=== TEST 1: Department insert ===");
//        Department dept = new Department(10, "Environment");
//        int rowsAffected = departmentDao.update(dept);
//        System.out.println("Rows affected: " + rowsAffected);

//        System.out.println("=== TEST 3: Department DeleteById ===");
//        Department dept = new Department(11, "Infrastructure");
//        departmentDao.deleteById(dept);
//        System.out.println("Department successfully excluded.");

//        System.out.println("=== TEST 4: Department findById ===");
//        Department dept = departmentDao.findById(8);
//        System.out.println(dept.toString());

        System.out.println("=== TEST 5: Department findAll ===");
        List<Department> deptList = departmentDao.findAll();
        for(Department dept : deptList) {
            System.out.println(dept.toString());
        }
    }
}
