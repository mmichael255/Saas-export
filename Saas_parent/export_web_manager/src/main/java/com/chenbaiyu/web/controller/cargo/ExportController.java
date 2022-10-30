package com.chenbaiyu.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chenbaiyu.domain.cargo.*;
import com.chenbaiyu.service.cargo.ContractService;
import com.chenbaiyu.service.cargo.ExportProductService;
import com.chenbaiyu.service.cargo.ExportService;

import com.chenbaiyu.vo.ExportProductVo;
import com.chenbaiyu.vo.ExportResult;
import com.chenbaiyu.vo.ExportVo;
import com.chenbaiyu.web.controller.BaseController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/cargo/export")
public class ExportController extends BaseController {

    @Reference(timeout = 1000000,retries = 0)
    private ContractService contractService;

    @Reference(timeout = 1000000,retries = 0)
    private ExportService exportService;

    @Reference(timeout = 1000000,retries = 0)
    private ExportProductService exportProductService;


    @RequestMapping("/contractList")
    public String contractList(@RequestParam(defaultValue = "1") int pageNum,
                               @RequestParam(defaultValue = "5") int pageSize){
        ContractExample contractExample = new ContractExample();
        ContractExample.Criteria criteria = contractExample.createCriteria();
        criteria.andStateEqualTo(1);

        contractExample.setOrderByClause("create_time desc");

        PageInfo<Contract> byPage = contractService.findByPage(contractExample, pageNum, pageSize);
        req.setAttribute("PageInfo",byPage);

        return "/cargo/export/export-contractList";
    }

    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "5") int pageSize){
        ExportExample exportExample = new ExportExample();
        ExportExample.Criteria criteria = exportExample.createCriteria();
        exportExample.setOrderByClause("create_time desc");
        criteria.andCompanyIdEqualTo(getLogInCompanyId());

        PageInfo<Export> byPage = exportService.findByPage(exportExample, pageNum, pageSize);
        req.setAttribute("PageInfo",byPage);
        return "/cargo/export/export-list";
    }

    @RequestMapping("/toExport")
    public String toExport(String id){
        req.setAttribute("id",id);
        return "/cargo/export/export-toExport";
    }

    @RequestMapping("/edit")
    public String edit(Export export){
        export.setCompanyId(getLogInCompanyId());
        export.setCompanyName(getLogInCompanyName());
        if (StringUtils.isEmpty(export.getId())){
            export.setCreateBy(getLoginUser().getId());
            export.setCreateDept(getLoginUser().getDeptId());
            exportService.save(export);
        }else {
            export.setUpdateBy(getLoginUser().getId());
            exportService.update(export);
        }
        return "redirect:/cargo/export/list.do";
    }

    @RequestMapping("toUpdate")
    public String toUpdate(String id){
        Export byId = exportService.findById(id);
        ExportProductExample exportProductExample = new ExportProductExample();
        ExportProductExample.Criteria criteria = exportProductExample.createCriteria();
        criteria.andExportIdEqualTo(id);
        List<ExportProduct> exportProductServiceAll = exportProductService.findAll(exportProductExample);
        req.setAttribute("export",byId);
        req.setAttribute("eps",exportProductServiceAll);
        return "/cargo/export/export-update";
    }

    @RequestMapping("/submit")
    public String submit(String id){
        Export export = new Export();
        export.setId(id);
        export.setState(1);
        exportService.update(export);
        return "redirect:/cargo/export/list.do";
    }

    @RequestMapping("/cancel")
    public String cancel(String id){
        Export export = new Export();
        export.setId(id);
        export.setState(0);
        exportService.update(export);
        return "redirect:/cargo/export/list.do";
    }

    @RequestMapping("/toView")
    public String toView(String id){
        Export byId = exportService.findById(id);
        req.setAttribute("export",byId);
        return "/cargo/export/export-view";
    }

    @RequestMapping("/exportE")
    public String exportE(String id) {
        ExportVo exportVo = new ExportVo();
        Export byId = exportService.findById(id);
        BeanUtils.copyProperties(byId, exportVo);
        exportVo.setExportId(id);

        ExportProductExample exportProductExample = new ExportProductExample();
        ExportProductExample.Criteria epCriteria = exportProductExample.createCriteria();
        epCriteria.andExportIdEqualTo(id);
        List<ExportProduct> exportProducts = exportProductService.findAll(exportProductExample);
        if (exportProducts != null && exportProducts.size() > 0) {
            for (ExportProduct exportProduct : exportProducts) {
                ExportProductVo exportProductVo = new ExportProductVo();
                BeanUtils.copyProperties(exportProduct, exportProductVo);
                exportProductVo.setExportId(id);
                exportProductVo.setExportProductId(exportProduct.getId());
                exportVo.getProducts().add(exportProductVo);
            }
        }

        WebClient.create("http://localhost:9091/ws/export/user")
                .post(exportVo);

        ExportResult exportResult = WebClient.create("http://localhost:9091/ws/export/user/" + id)
                .get(ExportResult.class);

        exportService.updateResult(exportResult);

        return "redirect:/cargo/export/list.do";
    }
}
