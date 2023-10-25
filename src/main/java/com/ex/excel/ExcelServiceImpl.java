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
    public void deleteDuplicates(List<Student> students) {
        List<Student> existingStudents = getAllStudents();
        //List<Integer> duplicatesId = new ArrayList<>();

        for (Student student : students) {
            for (Student existingStudent : existingStudents) {
                if (student.getStudentId() == existingStudent.getStudentId()) {
                    //duplicatesId.add(student.getStudentId());
                    dao.deleteStudent(student.getStudentId());
                    break;
                }
            }
        }


    }

}
