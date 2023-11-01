package ru.esstu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentListDatabase implements StudentList {
    private Connection connection;

    public StudentListDatabase(String jdbcUrl, String username, String password) {
        this.connection = createConnection(jdbcUrl, username, password);
    }

    private Connection createConnection(String jdbcUrl, String username, String password) {
        try {
            return DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удалось установить соединение с БД");
        }
    }

    @Override
    public List<Student> getAll() {
        List<Student> students = new ArrayList<>();
        try {
            String query = "SELECT * FROM student";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String partonymicName = resultSet.getString("patronymic_name");
                String group = resultSet.getString("grouup");

                Student student = new Student(id, firstName, lastName, partonymicName, group);
                students.add(student);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    @Override
    public void add(Student student) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO student (id, first_name, last_name, patronymic_name, grouup) VALUES (?, ?, ?, ?, ?)")) {

            statement.setString(1, student.getId());
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getLastName());
            statement.setString(4, student.getPartonymicName());
            statement.setString(5, student.getGroup());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Student getById(String id) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM student WHERE id = ?")) {

            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String partonymicName = resultSet.getString("patronymic_name");
                String group = resultSet.getString("grouup");

                return new Student(id, firstName, lastName, partonymicName, group);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(String id) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM student WHERE id = ?")) {

            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Student student) {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE student SET first_name = ?, last_name = ?, patronymic_name = ?, grouup = ? WHERE id = ?")) {
            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.setString(3, student.getPartonymicName());
            statement.setString(4, student.getGroup());
            statement.setString(5, student.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}