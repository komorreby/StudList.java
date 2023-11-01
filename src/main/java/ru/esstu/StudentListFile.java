package ru.esstu;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudentListFile implements StudentList {
    private String filename;
    private List<Student> students;

    public StudentListFile(String filename) {
        this.filename = filename;
        students = new ArrayList<>();
        loadFromFile();
    }

    @Override
    public List<Student> getAll() {
        return students;
    }

    @Override
    public void add(Student student) {
        students.add(student);
        saveToFile();
    }

    @Override
    public Student getById(String id) {
        for (Student student : students) {
            if (student.getId().equals(id)) {
                return student;
            }
        }
        return null;
    }

    @Override
    public void delete(String id) {
        Student studentToDelete = null;
        for (Student student : students) {
            if (student.getId().equals(id)) {
                studentToDelete = student;
                break;
            }
        }
        if (studentToDelete != null) {
            students.remove(studentToDelete);
            saveToFile();
        }
    }

    @Override
    public void update(Student student) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId().equals(student.getId())) {
                students.set(i, student);
                saveToFile();
                break;
            }
        }
    }

    private void loadFromFile() {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Файл с данными не был найден. Создание нового файла.");
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 5) {
                        Student student = new Student(parts[0], parts[1], parts[2], parts[3], parts[4]);
                        students.add(student);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Student student : students) {
                writer.write(student.getId() + "," + student.getFirstName() + "," + student.getLastName() + "," + student.getPartonymicName() + "," + student.getGroup());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}