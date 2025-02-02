package Application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;


import java.util.Date;
import java.util.List;

public class Program {
    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=== TEST 1: Seller findById ===");
        Seller seller1 = sellerDao.findById(3);
        System.out.println(seller1);

        System.out.println("\n=== TEST 2: Seller findByDepartment ===");
        List<Seller> sellerList = sellerDao.findByDepartment(new Department(2, null));
        for(Seller seller : sellerList) {
            System.out.println(seller.toString());
        }

        System.out.println("\n=== TEST 3: Seller findAll ===");
        sellerList = sellerDao.findAll();
        for(Seller seller : sellerList) {
            System.out.println(seller.toString());
        }

        System.out.println("\n=== TEST 4: Seller insert ===");
        Seller newSeller = new Seller(null, "Juca", "juca@gmail.com", new Date(), 3000.0, new Department(3, "Fashion"));
        sellerDao.insert(newSeller);
        System.out.printf("New seller Id: %d inserted!\n", newSeller.getId());

        System.out.println("\n=== TEST 5: Seller update ===");
        newSeller = sellerDao.findById(1);
        newSeller.setEmail("zebolacha@gmail.com");
        sellerDao.update(newSeller);
        System.out.println("Seller ID: " + newSeller.getId() + ", name: " + newSeller.getName() + ", data was updated");
    }
}
