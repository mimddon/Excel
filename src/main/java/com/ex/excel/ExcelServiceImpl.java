package com.ex.excel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    ExcelDAO dao;

    @Override
    public void insertStudents(List<Student> students) {
        dao.insertStudents(students);
    }

    @Override
    public List<Student> getAllStudents() {
        return dao.getAllStudents();
    }

    @Override
    public List<Student> checkForDuplicates(List<Student> students) {
        List<Integer> studentIds = new ArrayList<>();
        List<Student> duplicates = new ArrayList<>();


        for (Student student : students) {
            int studentId = student.getStudentId();
            if (studentIds.contains(studentId)) {
                duplicates.add(student);
            } else {
                studentIds.add(studentId);
            }
        }

        return duplicates;
    }

    @Override
    public List<Student> getDuplicates(List<Student> students) {
        List<Student> existingStudents = getAllStudents();
        List<Student> duplicates = new ArrayList<>();

        for (Student student : students) {
            for (Student existingStudent : existingStudents) {
                if (student.getStudentId() == existingStudent.getStudentId()) {
                    duplicates.add(student);
                    break;
                }
            }
        }

        return duplicates;
    }



}
