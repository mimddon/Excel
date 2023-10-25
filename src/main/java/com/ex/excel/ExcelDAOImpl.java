package com.ex.excel;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExcelDAOImpl implements ExcelDAO {

    @Autowired
    SqlSession sql;

    @Override
    public void insertStudents(List<Student> students) {
        sql.insert("Excel.insertStudents", students);
    }

    @Override
    public List<Student> getAllStudents() {
        return sql.selectList("Excel.getAllStudents");
    }

    @Override
    public void deleteStudent(int studentId) {
        sql.delete("Excel.deleteStudent", studentId);
    }

    @Override
    public void deleteStudents(List<Integer> duplicatesId) {
        sql.delete("Excel.deleteStudents", duplicatesId);
    }
}
