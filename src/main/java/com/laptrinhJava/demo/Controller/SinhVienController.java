package com.laptrinhJava.demo.Controller;

import com.laptrinhJava.demo.Model.SinhVien;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class SinhVienController {

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/sinhvien")
    public String showForm(Model model) {
        model.addAttribute("sinhVien", new SinhVien());
        return "sinhvien/form-sinhvien";
    }

    @PostMapping("/sinhvien")
    public String submitForm(@RequestParam("avatar") MultipartFile avatarFile, @Valid SinhVien sinhVien,
                             BindingResult bindingResult, Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            return "sinhvien/form-sinhvien";
        }



        // Lưu ảnh vào thư mục static/avatar
        if (!avatarFile.isEmpty()) {
            byte[] bytes = avatarFile.getBytes();
            Path path = Paths.get(uploadPath + File.separator + "avatar" + File.separator + avatarFile.getOriginalFilename());
            Files.write(path, bytes);
            sinhVien.setAvatarFileName(avatarFile.getOriginalFilename());
        }

        model.addAttribute("message", "Sinh viên đã được thêm thành công!");
        return "sinhvien/result-sinhvien";
    }
}
