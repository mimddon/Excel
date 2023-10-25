package com.ex.excel;

import java.util.List;

public interface ExcelService {
    void insertStudents(List<Student> students);

    List<Student> getAllStudents();

    void deleteDuplicates(List<Student> students);
}
