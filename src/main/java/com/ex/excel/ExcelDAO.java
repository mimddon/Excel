package com.ex.excel;

import java.util.List;

public interface ExcelDAO {

    void insertStudents(List<Student> students);

    List<Student> getAllStudents();

    void deleteStudent(int studentId);

    void deleteStudents(List<Integer> duplicatesId);
}
