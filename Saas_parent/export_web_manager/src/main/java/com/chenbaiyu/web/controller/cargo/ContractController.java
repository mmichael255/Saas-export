package com.chenbaiyu.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chenbaiyu.domain.cargo.Contract;
import com.chenbaiyu.domain.cargo.ContractExample;
import com.chenbaiyu.vo.ContractProductVo;
import com.chenbaiyu.service.cargo.ContractProductVoService;
import com.chenbaiyu.service.cargo.ContractService;
import com.chenbaiyu.web.controller.BaseController;
import com.chenbaiyu.web.utils.DownloadUtil;
import com.github.pagehelper.PageInfo;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping("/cargo/contract")
public class ContractController extends BaseController {
    @Reference(timeout = 1000000,retries = 0)
    private ContractService contractService;

    @Reference(timeout = 10000000,retries = 0)
    private ContractProductVoService contractProductVoService;


    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize){

        ContractExample contractExample = new ContractExample();
        ContractExample.Criteria criteria = contractExample.createCriteria();
        criteria.andCompanyIdEqualTo(getLogInCompanyId());
        if (getLoginUser().getDegree() == 4){
            criteria.andCreateByEqualTo(getLoginUser().getId());
        }else if (getLoginUser().getDegree() == 3){
            criteria.andCreateDeptEqualTo(getLoginUser().getDeptId());
        }else if (getLoginUser().getDegree() == 2){
            PageInfo<Contract> pageByDeptId = contractService.findPageByDeptId(getLoginUser().getDeptId(), pageNum, pageSize);
            req.setAttribute("PageInfo",pageByDeptId);
            return "/cargo/contract/contract-list";
        }
        PageInfo<Contract> byPage = contractService.findByPage(contractExample, pageNum, pageSize);
        req.setAttribute("PageInfo",byPage);
        return "/cargo/contract/contract-list";
    }

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "/cargo/contract/contract-add";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        Contract byId = contractService.findById(id);
         req.setAttribute("contract",byId);
         return "/cargo/contract/contract-update";
    }

    @RequestMapping("/edit")
    public String edit(Contract contract){
        contract.setCompanyId(getLogInCompanyId());
        contract.setCompanyName(getLogInCompanyName());

        if (StringUtils.isEmpty(contract.getId())){
            contract.setCreateBy(getLoginUser().getId());
            contract.setCreateDept(getLoginUser().getDeptId());
            contractService.save(contract);
        }else {
            contract.setUpdateBy(getLoginUser().getId());
            contractService.update(contract);
        }
        return "redirect:/cargo/contract/list.do";
    }

    @RequestMapping("/delete")
    public String delete(String id){
        contractService.delete(id);
        return "redirect:/cargo/contract/list.do";
    }

    @RequestMapping("/toView")
    public String toView(String id){
        Contract byId = contractService.findById(id);
        req.setAttribute("contract",byId);
        return "/cargo/contract/contract-view";
    }

    @RequestMapping("/submit")
    public String submit(String id){
        Contract contract = new Contract();
        contract.setId(id);
        contract.setState(1);
        contractService.update(contract);
        return "redirect:/cargo/contract/list.do";
    }

    @RequestMapping("/cancel")
    public String cancel(String id){
        Contract contract = new Contract();
        contract.setId(id);
        contract.setState(0);
        contractService.update(contract);
        return "redirect:/cargo/contract/list.do";
    }

    @RequestMapping("/print")
    public String print(){
        return "/cargo/print/contract-print";
    }

    @RequestMapping("/printExcel")
    public String printExcel(String inputDate) throws IOException {
        InputStream inputStream = session.getServletContext().getResourceAsStream("/make/xlsrint/tOUTPRODUCT.xlsx");
        Workbook wb = new XSSFWorkbook(inputStream);
        Sheet sheet = wb.getSheetAt(0);
        Row row = sheet.getRow(0);
        Cell cell = row.getCell(1);
        String title = inputDate.replaceAll("-","年")+"月出货表";
        cell.setCellValue(title);
        CellStyle[] cellStyles = new CellStyle[8];
        row = sheet.getRow(2);
        for (int i = 1; i <= 8 ; i++) {
            cellStyles[i-1] = row.getCell(i).getCellStyle();
        }

        List<ContractProductVo> byShipTime = contractProductVoService.findByShipTime(inputDate, getLogInCompanyId());
        for (int i = 0; i < byShipTime.size(); i++) {
            Row sheetRow = sheet.createRow(i + 3);
            sheetRow.setHeightInPoints(24);
            ContractProductVo contractProductVo = byShipTime.get(i);

            cell = sheetRow.createCell(1);
            cell.setCellStyle(cellStyles[0]);
            cell.setCellValue(contractProductVo.getCustomName());

            cell = sheetRow.createCell(2);
            cell.setCellStyle(cellStyles[1]);
            cell.setCellValue(contractProductVo.getContractNo());

            cell = sheetRow.createCell(3);
            cell.setCellStyle(cellStyles[2]);
            cell.setCellValue(contractProductVo.getProductNo());

            cell = sheetRow.createCell(4);
            cell.setCellStyle(cellStyles[3]);
            cell.setCellValue(contractProductVo.getCnumber());

            cell = sheetRow.createCell(5);
            cell.setCellStyle(cellStyles[4]);
            cell.setCellValue(contractProductVo.getFactoryName());

            cell = sheetRow.createCell(6);
            cell.setCellStyle(cellStyles[5]);
            cell.setCellValue(contractProductVo.getDeliveryPeriod());

            cell = sheetRow.createCell(7);
            cell.setCellStyle(cellStyles[6]);
            cell.setCellValue(contractProductVo.getShipTime());

            cell = row.createCell(8);
            cell.setCellStyle(cellStyles[7]);
            cell.setCellValue(contractProductVo.getTradeTerms());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wb.write(out);
        new DownloadUtil().download(out,response,"出货表.xlsx");
        return "/cargo/print/contract-print";
    }
}
