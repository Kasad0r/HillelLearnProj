package org.kasad0r;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.kasad0r.entity.Skill;
import org.kasad0r.entity.Tool;
import org.kasad0r.entity.User;

import java.sql.*;
import java.util.ArrayList;

public class BasicJDBCDemo {
    public static Connection connection() throws SQLException, ClassNotFoundException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/userDB");
        dataSource.setUser("kasad0r");
        dataSource.setPassword("2431082dima");
        Class.forName("com.mysql.jdbc.Driver");

        Connection connection = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUser(), "2431082dima");
        connection.setAutoCommit(true);
        return connection;
    }

    private static void insertUser(User user) throws SQLException {
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = connection();
            stmt = conn.prepareStatement("INSERT INTO Users values(?,?,?,?)");
            stmt.setInt(1, user.getId());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhone());
            stmt.executeUpdate();
            stmt.close();
            for (Tool tool : user.getTools()) {
                stmt = conn.prepareStatement("INSERT INTO Users_Tools values (?,?)");
                stmt.setInt(1, user.getId());
                stmt.setInt(2, tool.getId());
                stmt.executeUpdate();
                stmt.close();
            }
            for (Skill skill : user.getSkills()) {
                stmt = conn.prepareStatement("INSERT INTO Users_Skills values (?,?)");
                stmt.setInt(1, user.getId());
                stmt.setInt(2, skill.getId());
                stmt.executeUpdate();
                stmt.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    private static User getUser(int id) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection connection = connection()) {
            preparedStatement = connection.prepareStatement("SELECT Users.id,Users.name, Users.email, Users.phone from  Users where Users.id=?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            resultSet.next();

            User user = new User();
            user.setId(resultSet.getInt(1));
            user.setName(resultSet.getString(2));
            user.setEmail(resultSet.getString(3));
            user.setPhone(resultSet.getString(4));

            resultSet.close();
            preparedStatement.close();

            user.setTools(new ArrayList<>());
            user.setSkills(new ArrayList<>());
            preparedStatement = connection.prepareStatement("SELECT Tools.id, Tools.name FROM Tools, Users_Tools where Users_Tools.userId=? and Users_Tools.toolId = Tools.id");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            while (resultSet.next()) {
                Tool tool = new Tool();
                tool.setId(resultSet.getInt(1));
                tool.setName(resultSet.getString(2));
                user.getTools().add(tool);
            }
            resultSet.close();
            preparedStatement.close();

            preparedStatement = connection.prepareStatement("SELECT  Skills.id, Skills.name from Skills, Users_Skills where Users_Skills.userId=? and Users_Skills.skillId=Skills.id");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Skill skill = new Skill();
                skill.setId(resultSet.getInt(1));
                skill.setName(resultSet.getString(2));
                user.getSkills().add(skill);
            }
            return user;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }finally {
            if(resultSet!=null){
                resultSet.close();
            }
            if(preparedStatement!=null){
                preparedStatement.close();
            }
        }
    }

    private void initDB() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connection();
            connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement("CREATE table Users(id INT PRIMARY KEY " +
                    ", name VARCHAR (255),email VARCHAR (255),phone VARCHAR (255))");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = connection.prepareStatement("create table Tools(id Int primary key , name varchar(255))");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = connection.prepareStatement("create table Skills(id Int primary key , name varchar(255))");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = connection.prepareStatement("CREATE table Users_Tools(userId int, toolId int, primary key (userId,toolId))");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            preparedStatement = connection.prepareStatement("CREATE table Users_Skills(userId int, skillId int, primary key (userId,skillId))");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.commit();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            connection.rollback();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}
