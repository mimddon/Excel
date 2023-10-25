package com.ex.excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ExcelController {

    @Value("${upload.directory}")
    private String uploadDirectory;

    @Autowired
    ExcelService service;

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        // 업로드된 파일 처리 로직 구현
        if (!file.isEmpty()) {
            List<Student> students = null;
            try {
                String fileName = file.getOriginalFilename();
                String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
                String newFileName = timestamp + "_" + fileName; // 현재 시간과 파일 이름을 결합
                String filePath = uploadDirectory + "/" + newFileName;
                file.transferTo(new File(filePath));

                // 엑셀 데이터 처리 로직 추가
                FileInputStream fis = new FileInputStream(filePath);
                Workbook workbook = new XSSFWorkbook(fis);
                Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트 가져오기

                students = new ArrayList<>();

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

                    System.out.println("학번 : " + student.getStudentId());
                    System.out.println("이름 : " + student.getName());
                    System.out.println("성별 : " + student.getGender());
                    System.out.println("이메일 : " + student.getEmail());

                }

                fis.close();

                // 데이터베이스에 저장
                service.insertStudents(students);

                return "redirect:/";
            } catch (DuplicateKeyException e) {
                // 중복 예외 처리
                service.deleteDuplicates(students);

                service.insertStudents(students);

                return "redirect:/list";
            } catch (Exception e) {
                // 예외 처리
                e.printStackTrace(); // 예외 메시지 출력
                return "redirect:/error";
            }
        } else {
            // 파일이 비어있을 경우 예외 처리
            return "redirect:/error";
        }
    }

    @GetMapping("/list")
    public String getAllStudents(Model model) {
        List<Student> students = service.getAllStudents();
        model.addAttribute("students", students);
        return "/list";
    }


}