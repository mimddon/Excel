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
        List<Student> existingStudents = dao.getAllStudents(); // 데이터베이스에서 모든 학생 데이터 가져오기

        for (Student student : students) {
            for (Student existingStudent : existingStudents) {
                if (student.getStudentId() == existingStudent.getStudentId()) {
                    // studentId가 중복된 경우 db 에서 구 데이터 삭제
                    dao.deleteStudent(student.getStudentId());
                    break; // 중복된 경우를 찾았으므로 더 이상 비교하지 않음
                }
            }
        }
        dao.insertStudents(students); // 데이터베이스에 새로운 데이터 삽입
    }


    @Override
    public void skipDuplicate(List<Student> students) {
        List<Student> nonDuplicateStudents = dao.getAllStudents(); // 데이터베이스에서 모든 학생 데이터 가져오기
        List<Student> studentsToRemove = new ArrayList<>(); // 삭제할 학생 목록

        // 중복 학생을 찾아서 studentsToRemove에 추가
        for (Student student : students) {
            for (Student nonDuplicateStudent : nonDuplicateStudents) {
                if (student.getStudentId() == nonDuplicateStudent.getStudentId()) {
                    studentsToRemove.add(student);
                    break;
                }
            }
        }

        // 중복 학생을 students에서 제거
        students.removeAll(studentsToRemove);

        // 중복을 제거한 데이터를 데이터베이스에 추가
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
