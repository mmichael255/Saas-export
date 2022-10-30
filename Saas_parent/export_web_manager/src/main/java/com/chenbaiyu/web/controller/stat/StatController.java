package com.chenbaiyu.web.controller.stat;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chenbaiyu.service.stat.StatService;
import com.chenbaiyu.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/stat")
public class StatController extends BaseController {
    @Reference
    private StatService statService;

    @RequestMapping("/toCharts")
    public String toChart(String chartsType){
        return "/stat/stat-"+chartsType;
    }

    @RequestMapping("/factoryData")
    @ResponseBody
    public List<Map<String, Object>> factoryData(){
        List<Map<String, Object>> sellStat = statService.findSellStat(getLogInCompanyId());
        return sellStat;
    }

    @RequestMapping("/productData")
    @ResponseBody
    public List<Map<String, Object>> productData(){
        List<Map<String, Object>> productSellStat = statService.findProductSellStat(getLogInCompanyId());
        return productSellStat;
    }

    @RequestMapping("/onlineData")
    @ResponseBody
    public List<Map<String, Object>> onlineData(){
        List<Map<String, Object>> onlineData = statService.findonlineData(getLogInCompanyId());
        return onlineData;
    }
}
