package com.kazusa.test.easyexcel.demo.web;

import com.kazusa.test.easyexcel.contxext.ExcelCustomContext;
import com.kazusa.test.easyexcel.dto.ExcelCustomExportDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description
 * @Author kazusa
 * @Date 2023-12-04 13:24
 */
@RestController
@RequestMapping("web/test")
public class WebDemoController {

    @Resource
    private ExcelCustomContext context;

    @PostMapping
    public void exportTest(HttpServletResponse response, @RequestBody ExcelCustomExportDTO dto) {
        try {
            context.download(dto, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
