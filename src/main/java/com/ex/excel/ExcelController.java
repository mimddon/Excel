package com.ex.excel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

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
