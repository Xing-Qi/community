package com.nowcoder.community.controller;

import com.nowcoder.community.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @author Oliver
 * @create 2023-02-07 10:40
 */
@Controller
@RequestMapping("/data")
public class DataController {
    @Autowired
    private DataService dataService;

    /**
     * 获取页面
     *
     * @return
     */
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String getDataPage() {
        return "/site/admin/data";
    }

    /**
     * 统计uv
     * @param start
     * @param end
     * @param model
     * @return
     */
    @PostMapping("/uv")
    public String getUV(
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
            Model model
    ) {
        long uv = dataService.calculateUV(start, end);
        model.addAttribute("uvResult",uv);
        model.addAttribute("uvStartDate",start);
        model.addAttribute("uvEndDate",end);
        return "forward:/data";
    }

    /**
     * 统计dau
     * @param start
     * @param end
     * @param model
     * @return
     */
    @PostMapping("/dau")
    public String getDAU(
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
            Model model
    ) {
        long dau = dataService.calculateDAU(start, end);
        model.addAttribute("dauResult",dau);
        model.addAttribute("dauStartDate",start);
        model.addAttribute("dauEndDate",end);
        return "forward:/data";
    }
}
