package model.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement prepSt = null;

        try {
            conn.setAutoCommit(false);
            prepSt = conn.prepareStatement("INSERT INTO seller "
                            + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                            + "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            prepSt.setString(1, seller.getName());
            prepSt.setString(2, seller.getEmail());
            prepSt.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            prepSt.setDouble(4, seller.getBaseSalary());
            prepSt.setInt(5, seller.getDepartment().getId());

            int rowsAffected = prepSt.executeUpdate();
            conn.commit();

            if(rowsAffected > 0) {
                ResultSet rs = prepSt.getGeneratedKeys();
                if(rs.next()) {
                    int generatedIdKey = rs.getInt(1);
                    seller.setId(generatedIdKey);
                }
                DB.closeResultSet(rs);
            }
            else {
                throw new DbException("Error inserting lines. No lines were inserted.");
            }

        }
        catch(SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(prepSt);
        }
    }

    @Override
    public void update(Seller seller) {
        PreparedStatement prepSt = null;

        try {
            conn.setAutoCommit(false);
            prepSt = conn.prepareStatement(
                        "UPDATE seller "
                            + "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                            + "WHERE Id = ?");
            prepSt.setString(1, seller.getName());
            prepSt.setString(2,seller.getEmail());
            prepSt.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            prepSt.setDouble(4, seller.getBaseSalary());
            prepSt.setInt(5, seller.getDepartment().getId());
            prepSt.setInt(6, seller.getId());

            prepSt.executeUpdate();
            conn.commit();
        }
        catch(SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {

        PreparedStatement prepSt = null;
        ResultSet resultSet = null;
        try {
            prepSt = conn.prepareStatement(
                    "SELECT seller.*, department.Name as DepName "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE seller.Id = ?;");
            prepSt.setInt(1, id);

            resultSet = prepSt.executeQuery(); //executeQuery() é usado para retornar um resultSet apenas para consultas SELECT.
            if(resultSet.next()) {

                //building the department object based on the query result, this object will be associated with the seller obj
                Department dept = instatiateDepartment(resultSet);

                //building the seller object based on the query result
                Seller seller = instantiateSeller(resultSet, dept);

                return seller;
            }
            return null;
        }
        catch(SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(prepSt);
            DB.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement prepSt = null;
        ResultSet rs = null;

        try {
            prepSt = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE DepartmentId = ? "
                    + "ORDER BY Name");
            prepSt.setInt(1, department.getId());
            rs = prepSt.executeQuery();

            Map<Integer, Department> deptMap = new HashMap<>();
            List<Seller> sellersList = new ArrayList<>();

            while(rs.next()) {
                int rsDeptKey = rs.getInt("DepartmentId");

                if(!deptMap.containsKey(rsDeptKey)) {
                   Department dept = instatiateDepartment(rs);
                   deptMap.put(dept.getId(), dept);
                   sellersList.add(instantiateSeller(rs, dept));
                }
                else {
                    sellersList.add(instantiateSeller(rs, deptMap.get(rsDeptKey)));
                }
            }
            return sellersList;
        }
        catch(SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(prepSt);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement prepSt = null;
        ResultSet rs = null;

        try {
            prepSt = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "ORDER BY Name");
            rs = prepSt.executeQuery();

            List<Seller> sellersList = new ArrayList<>();
            Map<Integer, Department> deptMap = new HashMap<>();

            while(rs.next()) {
                int rsDeptKey = rs.getInt("DepartmentID");

                if(!deptMap.containsKey(rsDeptKey)) {
                    Department dept = instatiateDepartment(rs);
                    deptMap.put(rsDeptKey, dept);
                    sellersList.add(instantiateSeller(rs, dept));
                }
                else {
                    sellersList.add(instantiateSeller(rs, deptMap.get(rsDeptKey)));
                }
            }
            return sellersList;
        }
        catch(SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(prepSt);
            DB.closeResultSet(rs);
        }
    }

    private Department instatiateDepartment(ResultSet rs) throws SQLException {
        Department dept = new Department();
        dept.setId(rs.getInt("DepartmentId"));
        dept.setName(rs.getString("DepName"));
        return dept;
    }

    private Seller instantiateSeller(ResultSet rs, Department dept) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        seller.setBirthDate(rs.getDate("BirthDate"));
        seller.setBaseSalary(rs.getDouble("BaseSalary"));
        seller.setDepartment(dept);

        return seller;
    }
}
