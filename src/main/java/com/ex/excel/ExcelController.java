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
import java.util.ArrayList;
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
                String filePath = uploadDirectory + "/" + fileName;
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

                // 중복이 없는 경우 데이터베이스에 저장
                service.insertStudents(students);

                return "redirect:/";
            } catch (DuplicateKeyException e) {
                // 중복 예외 처리
                List<Student> duplicates = service.getDuplicates(students);

                model.addAttribute("duplicates", duplicates);

                System.out.println("중복된 학생 : " + duplicates);

                return "duplicate"; // 중복 처리 페이지의 뷰 이름
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

    @PostMapping("/duplicate")
    public String handleDuplicatesPage(@RequestParam("action") String action, Model model) {
        if (action.equals("skip")) {
            // 중복 데이터 건너뛰기 로직 구현


            return "redirect:/"; // 처리 완료 후 리다이렉트
        } else if (action.equals("overwrite")) {
            // 중복 데이터 덮어쓰기 로직 구현
            // 중복 처리 로직
            return "redirect:/"; // 처리 완료 후 리다이렉트
        } else {
            // 잘못된 액션 값이 넘어온 경우 처리할 로직 구현
            return "error"; // 에러 페이지로 이동 또는 다른 처리 방식 선택
        }
    }




    @GetMapping("/list")
    public String getAllStudents(Model model) {
        List<Student> students = service.getAllStudents();
        model.addAttribute("students", students);
        return "redirect: /list";
    }


}