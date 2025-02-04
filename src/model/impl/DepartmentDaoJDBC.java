package model.impl;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {
    Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department department) {

        PreparedStatement prepSt = null;

        try {
            conn.setAutoCommit(false);

            prepSt = conn.prepareStatement("INSERT INTO department (Name) VALUE (?)", Statement.RETURN_GENERATED_KEYS);
            prepSt.setString(1, department.getName());

            int rowsAffected = prepSt.executeUpdate();
            if(rowsAffected > 0) {
                ResultSet rs = prepSt.getGeneratedKeys();
                if(rs.next()) {
                    int idInserted = rs.getInt(1);
                    department.setId(idInserted);
                }
                DB.closeResultSet(rs);
            }
            conn.commit();
        }
        catch(SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(prepSt);
        }
    }

    @Override
    public Integer update(Department department) {
        PreparedStatement prepSt = null;

        try {
            conn.setAutoCommit(false);

            prepSt = conn.prepareStatement("UPDATE department Name = ? WHERE Id = ?");
            prepSt.setString(1, department.getName());
            prepSt.setInt(2, department.getId());

            int rowsAffected = prepSt.executeUpdate();
            conn.commit();
            return rowsAffected;
        }
        catch(SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(prepSt);
        }
    }

    @Override
    public void deleteById(Department department) {
        PreparedStatement prepSt = null;

        try {
            conn.setAutoCommit(false);
            prepSt = conn.prepareStatement("DELETE FROM department WHERE Id = ?");
            prepSt.setInt(1, department.getId());

            int rowsAffected = prepSt.executeUpdate();
            if(rowsAffected == 0) {
                throw new IllegalArgumentException("Invalid Id! No rows were affected.");
            }
            conn.commit();
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(prepSt);
        }
    }

    @Override
    public Department findById(Integer id) {
        return null;
    }

    @Override
    public List<Department> findAll() {
        return List.of();
    }
}
