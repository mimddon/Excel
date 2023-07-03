package com.ex.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ExcelController {

    @Value("${upload.directory}")
    private String uploadDirectory;

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        // 업로드된 파일 처리 로직 구현
        if (!file.isEmpty()) {
            try {
                String fileName = file.getOriginalFilename();
                String filePath = uploadDirectory + "/" + fileName;
                file.transferTo(new File(filePath));

                // 엑셀 데이터 처리 로직 추가
                FileInputStream fis = new FileInputStream(filePath);
                Workbook workbook = new XSSFWorkbook(fis);
                Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트 가져오기

                List<Student> students = new ArrayList<>();

                // 첫 번째 행은 헤더이므로 건너뜀
                int startRowNum = 1;
                int lastRowNum = sheet.getLastRowNum();
                for (int rowNum = startRowNum; rowNum <= lastRowNum; rowNum++) {
                    Row row = sheet.getRow(rowNum);

                    Student student = new Student();
                    student.setStudentId((int) row.getCell(0).getNumericCellValue());
                    student.setName(row.getCell(1).getStringCellValue());

                    String genderString = row.getCell(2).getStringCellValue();
                    int gender = genderString.equalsIgnoreCase("남자") ? 0 : 1;
                    student.setGender(gender);

                    student.setGrade((int) row.getCell(3).getNumericCellValue());
                    student.setEmail(row.getCell(4).getStringCellValue());

                    students.add(student);
                }

                // students 리스트를 데이터베이스에 저장하는 로직 작성

                fis.close();

                return "redirect:/success";
            } catch (Exception e) {
                // 예외 처리
                return "redirect:/error";
            }
        } else {
            // 파일이 비어있을 경우 예외 처리
            return "redirect:/error";
        }
    }


}
