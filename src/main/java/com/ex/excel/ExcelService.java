package com.ex.excel;

import java.util.List;

public interface ExcelService {
    void insertStudents(List<Student> students);

    List<Student> getAllStudents();

    List<Student> checkForDuplicates(List<Student> students);

    List<Student> getDuplicates(List<Student> duplicates);

}
