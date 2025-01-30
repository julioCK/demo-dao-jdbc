package Application;

import model.dao.DaoFactory;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;

public class Program {
    public static void main(String[] args) {

        Seller seller1 = DaoFactory.createSellerDao().findById(3);
        System.out.println(seller1);
    }
}
