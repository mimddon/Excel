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
        List<Integer> duplicates = new ArrayList<>();

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

    @Override
    public void overwriteDuplicate(List<Student> students) {
        List<Student> existingStudents = dao.getAllStudents();

        for (Student student : students) {
            for (Student existingStudent : existingStudents) {
                if (student.getStudentId() == existingStudent.getStudentId()) {
                    dao.deleteStudent(student.getStudentId());
                    break;
                }
            }
        }
        dao.insertStudents(students);
    }


    @Override
    public void skipDuplicate(List<Student> students) {
        List<Student> nonDuplicateStudents = dao.getAllStudents();
        List<Student> studentsToRemove = new ArrayList<>();

        for (Student student : students) {
            for (Student nonDuplicateStudent : nonDuplicateStudents) {
                if (student.getStudentId() == nonDuplicateStudent.getStudentId()) {
                    studentsToRemove.add(student);
                    break;
                }
            }
        }
        students.removeAll(studentsToRemove);
        dao.insertStudents(students);
    }



    @Override
    public List<Student> getDuplicates(List<Student> students) {
        List<Student> databaseStudents = dao.getAllStudents();
        List<Student> duplicateStudents = new ArrayList<>();

        // 모든 학생 데이터를 순회하며 중복을 확인합니다.
        for (Student student : students) {
            for (Student dbStudent : databaseStudents) {
                if (student.getStudentId() == dbStudent.getStudentId()) {
                    duplicateStudents.add(student);
                    break; // 중복 확인이 끝났으므로 반복을 중지합니다.
                }
            }
        }

        return duplicateStudents;
    }


}
