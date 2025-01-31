package model.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import javax.swing.plaf.SliderUI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    }

    @Override
    public void update(Seller seller) {

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
        List<Seller> sellersList = new ArrayList<>();

        try {
            prepSt = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE DepartmentId = ? "
                    + "ORDER BY Name");
            prepSt.setInt(1, department.getId());
            rs = prepSt.executeQuery();

            Map<Integer, Department> deptMap = new HashMap<>();
            while(rs.next()) {
                Department rsLineDept = instatiateDepartment(rs); //instancia um departamento da proxima linha do resultSet
                deptMap.putIfAbsent(rsLineDept.getId(), rsLineDept); //adiciona o departamento instanciado acima ao mapa "deptMap" com o id do departamento como chave. Estruturas Map nao admitem duas chaves iguais, portanto nao haverá dois objetos departamento iguais no mapa

                //todos os sellers do mesmo departamento devem estar relacionados com o mesmo objeto Department. esse objeto department estará armazenado no mapa deptMap.
                Seller rsLineSeller = instantiateSeller(rs, deptMap.get(rsLineDept.getId()));
                sellersList.add(rsLineSeller);
            }
        }
        catch(SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(prepSt);
            DB.closeResultSet(rs);
        }
        return sellersList;
    }

    @Override
    public List<Seller> findAll() {
        return List.of();
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
