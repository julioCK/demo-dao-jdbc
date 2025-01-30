package model.dao;

import model.impl.SellerDaoJDBC;

public class DaoFactory {

    public static SellerDaoJDBC createSellerDao() {
        return new SellerDaoJDBC();
    }
}
