package Application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;


import java.util.List;

public class Program {
    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=== TEST 1: findById ===");
        Seller seller1 = sellerDao.findById(3);
        System.out.println(seller1);

        System.out.println("\n=== TEST 2: findByDepartment ===");
        List<Seller> sellerList = sellerDao.findByDepartment(new Department(2, null));
        for(Seller seller : sellerList) {
            System.out.println(seller.toString());
        }
    }
}
