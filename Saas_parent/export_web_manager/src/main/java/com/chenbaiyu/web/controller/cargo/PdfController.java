package com.chenbaiyu.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chenbaiyu.domain.User;
import com.chenbaiyu.domain.cargo.Export;
import com.chenbaiyu.domain.cargo.ExportProduct;
import com.chenbaiyu.domain.cargo.ExportProductExample;
import com.chenbaiyu.service.cargo.ExportProductService;
import com.chenbaiyu.service.cargo.ExportService;
import com.chenbaiyu.web.controller.BaseController;
import com.chenbaiyu.web.utils.BeanMapUtils;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRHibernateListDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cargo/export")
public class PdfController extends BaseController {
    @Reference(timeout = 1000000,retries = 0)
    private ExportService exportService;
    @Reference(timeout = 1000000,retries = 0)
    private ExportProductService exportProductService;


//    @RequestMapping("/exportPdf")
//    public void exportPdf(String id) throws JRException, IOException {
//        InputStream in = session.getServletContext().getResourceAsStream("/jasper/test01.jasper");
//        JasperPrint jasperPrint = JasperFillManager.fillReport(in, new HashMap<>(), new JREmptyDataSource());
//
//        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
//    }

//    @RequestMapping("/exportPdf")
//    public void exportPdf(String id) throws JRException, IOException {
//        InputStream in = session.getServletContext().getResourceAsStream("/jasper/test02.jasper");
//
//        Map map = new HashMap<>();
//        map.put("username","大波萌妹");
//        map.put("email","123@ex.com");
//        map.put("companyname","pornhub");
//        map.put("deptname","攝影");
//
//        JasperPrint jasperPrint = JasperFillManager.fillReport(in, map, new JREmptyDataSource());
//
//        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
//    }

//    @RequestMapping("/exportPdf")
//    public void exportPdf(String id) throws JRException, IOException {
//        InputStream in = session.getServletContext().getResourceAsStream("/jasper/test03.jasper");
//
//        List<User> userList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            User user = new User();
//            user.setId(i+"");
//            user.setUserName("大波萌妹"+i);
//            user.setEmail("123"+i+"@ex.com");
//            user.setDeptName(i+"攝影部");
//            user.setCompanyName(i+"pornhub");
//            userList.add(user);
//        }
//
//
//        JasperPrint jasperPrint = JasperFillManager.fillReport(in, new HashMap<>(), new JRBeanCollectionDataSource(userList));
//
//        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
//    }

    @RequestMapping("/exportPdf")
    @ResponseBody
    public void exportPdf(String id) throws JRException, IOException {
        InputStream in = session.getServletContext().getResourceAsStream("/jasper/export.jasper");

        Export export = exportService.findById(id);

        Map<String, Object> exportMap = BeanMapUtils.beanToMap(export);

        ExportProductExample exportProductExample = new ExportProductExample();
        ExportProductExample.Criteria epCriteria = exportProductExample.createCriteria();
        epCriteria.andExportIdEqualTo(id);
        List<ExportProduct> epList = exportProductService.findAll(exportProductExample);


        JasperPrint jasperPrint = JasperFillManager.fillReport(in, exportMap, new JRBeanCollectionDataSource(epList));
        response.setHeader("Content-Disposition","attachment;filename=export.pdf");
        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());

    }
}
