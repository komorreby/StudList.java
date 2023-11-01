package org.demo;

import ru.esstu.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;


public class StudListDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Выберите тип реализации StudentList:");
        System.out.println("1 - В памяти (StudentListArrayList)");
        System.out.println("2 - БД (StudentListDatabase)");
        System.out.println("3 - В файле (StudentListFile)");
        int choice = scanner.nextInt();

        StudentList studentList = null;

        if (choice == 1) {
            studentList = new StudentListArrayList();
        } else if (choice == 2) {
            String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
            System.out.println("Введите имя пользователя:");
            String username = scanner.next();
            System.out.println("Введите пароль:");
            String password = scanner.next();
            studentList = new StudentListDatabase(jdbcUrl, username, password);
        } else if (choice == 3) {
            System.out.println("Введите имя файла (Без имени расширения):");
            String filename = scanner.next() + ".txt";
            studentList = new StudentListFile(filename);
        } else {
            System.out.println("Неверный выбор.");
            System.exit(1);
        }

        while (true) {
            System.out.println("Выберите действие:");
            System.out.println("1 - Добавить запись");
            System.out.println("2 - Удалить запись");
            System.out.println("3 - Редактировать запись");
            System.out.println("4 - Просмотреть все записи");
            System.out.println("0 - Завершить программу");

            int action = scanner.nextInt();

            if (action == 0) {
                break;
            }

            switch (action) {
                case 1:
                    addStudent(studentList, scanner);
                    break;
                case 2:
                    deleteStudent(studentList, scanner);
                    break;
                case 3:
                    updateStudent(studentList, scanner);
                    break;
                case 4:
                    viewAllStudents(studentList);
                    break;
                default:
                    System.out.println("Неверное действие.");
            }
        }
    }

    private static void addStudent(StudentList studentList, Scanner scanner) {
        System.out.println("Введите данные студента:");
        System.out.print("ID: ");
        String id = scanner.next();
        System.out.print("Имя: ");
        String firstName = scanner.next();
        System.out.print("Фамилия: ");
        String lastName = scanner.next();
        System.out.print("Отчество: ");
        String partonymicName = scanner.next();
        System.out.print("Группа: ");
        String group = scanner.next();

        Student student = new Student(id, firstName, lastName, partonymicName, group);
        studentList.add(student);
        System.out.println("Запись добавлена.");
    }

    private static void deleteStudent(StudentList studentList, Scanner scanner) {
        System.out.print("Введите ID студента для удаления: ");
        String id = scanner.next();

        Student studentToDelete = studentList.getById(id);

        if (studentToDelete != null) {
            studentList.delete(id);
            System.out.println("Запись удалена.");
        } else {
            System.out.println("Студент с таким ID не найден.");
        }
    }

    private static void updateStudent(StudentList studentList, Scanner scanner) {
        System.out.print("Введите ID студента для редактирования: ");
        String id = scanner.next();
        Student existingStudent = studentList.getById(id);
        if (existingStudent != null) {
            System.out.println("Введите новые данные студента:");
            System.out.print("Имя: ");
            String firstName = scanner.next();
            System.out.print("Фамилия: ");
            String lastName = scanner.next();
            System.out.print("Отчество: ");
            String partonymicName = scanner.next();
            System.out.print("Группа: ");
            String group = scanner.next();

            Student updatedStudent = new Student(id, firstName, lastName, partonymicName, group);
            studentList.update(updatedStudent);
            System.out.println("Запись обновлена.");
        } else {
            System.out.println("Студент с таким ID не найден.");
        }
    }

    private static void viewAllStudents(StudentList studentList) {
        List<Student> students = studentList.getAll();

        if (students.isEmpty()) {
            System.out.println("Список студентов пуст");
        } else {
            System.out.println("Список студентов:");
            for (Student student : students) {
                System.out.println("ID: " + student.getId());
                System.out.println("Имя: " + student.getFirstName());
                System.out.println("Фамилия: " + student.getLastName());
                System.out.println("Отчество: " + student.getPartonymicName());
                System.out.println("Группа: " + student.getGroup());
                System.out.println();
            }
        }
    }
}

