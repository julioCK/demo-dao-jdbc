package model.dao;

import db.DB;
import model.entities.Department;
import model.impl.DepartmentDaoJDBC;
import model.impl.SellerDaoJDBC;

public class DaoFactory {

    public static SellerDaoJDBC createSellerDao() {
        return new SellerDaoJDBC(DB.getConnection());
    }
    public static DepartmentDaoJDBC createDepartmentDao() {
        return new DepartmentDaoJDBC(DB.getConnection());
    }
}
