package com.chenbaiyu.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chenbaiyu.domain.cargo.ContractProduct;
import com.chenbaiyu.domain.cargo.ContractProductExample;
import com.chenbaiyu.domain.cargo.Factory;
import com.chenbaiyu.domain.cargo.FactoryExample;
import com.chenbaiyu.service.cargo.ContractProductService;

import com.chenbaiyu.service.cargo.FactoryService;
import com.chenbaiyu.web.controller.BaseController;
import com.chenbaiyu.web.utils.FileUploadUtil;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/cargo/contractProduct")
public class ContractProductController extends BaseController {
    @Reference(timeout = 100000,retries = 0)
    ContractProductService contractProductService;

    @Reference(timeout = 1000000,retries = 0)
    FactoryService factoryService;

    @Autowired
    FileUploadUtil fileUploadUtil;

    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "5") int pageSize, String contractId){
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria factoryExampleCriteria = factoryExample.createCriteria();
        factoryExampleCriteria.andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);

        ContractProductExample contractProductExample = new ContractProductExample();
        ContractProductExample.Criteria criteria = contractProductExample.createCriteria();
        criteria.andContractIdEqualTo(contractId);
        PageInfo<ContractProduct> byPage = contractProductService.findByPage(contractProductExample, pageNum, pageSize);
        req.setAttribute("PageInfo",byPage);
        req.setAttribute("contractId",contractId);

        req.setAttribute("factoryList",factoryList);
        return "/cargo/product/product-list";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        ContractProduct byId = contractProductService.findById(id);
        req.setAttribute("contractProduct",byId);
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria factoryExampleCriteria = factoryExample.createCriteria();
        factoryExampleCriteria.andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        req.setAttribute("factoryList",factoryList);
        return "/cargo/product/product-update";
    }

    @RequestMapping("/edit")
    public String edit(ContractProduct contractProduct, MultipartFile productPhoto){
        contractProduct.setCompanyId(getLogInCompanyId());
        contractProduct.setCompanyName(getLogInCompanyName());
        if (productPhoto != null){
            try {
                String imgUrl = fileUploadUtil.upload(productPhoto);
                contractProduct.setProductImage("http://"+imgUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isEmpty(contractProduct.getId())){
            contractProductService.save(contractProduct);
        }else {
            contractProductService.update(contractProduct);
        }
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractProduct.getContractId();
    }

    @RequestMapping("/delete")
    public String delete(String id,String contractId){
        contractProductService.delete(id);
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractId;
    }

    @RequestMapping("/toImport")
    public String toImport(String contractId){
        req.setAttribute("contractId",contractId);
        return "/cargo/product/product-import";
    }

    @RequestMapping("/import")
    public String Import(String contractId,MultipartFile file) throws IOException {
       Workbook wb = new HSSFWorkbook(file.getInputStream());
        Sheet sheet = wb.getSheetAt(0);
        int rowsNum = sheet.getPhysicalNumberOfRows();

        for (int i = 1; i < rowsNum; i++) {
            ContractProduct contractProduct = new ContractProduct();
            Row row = sheet.getRow(i);
            if (row.getCell(1) != null){
                String setFactoryName = row.getCell(1).getStringCellValue();
                contractProduct.setFactoryName(setFactoryName);
            }
            if (row.getCell(2) != null){
                String ProductNo = row.getCell(2).getStringCellValue();
                contractProduct.setProductNo(ProductNo);
            }
            if (row.getCell(3) != null){
                double Cnumber = row.getCell(3).getNumericCellValue();
                contractProduct.setCnumber((int)Cnumber);
            }
            if (row.getCell(4) != null){
                double PackingUnit = row.getCell(5).getNumericCellValue();
                contractProduct.setPackingUnit(PackingUnit+"");
            }
            if (row.getCell(5) != null){
                double LoadingRate = row.getCell(6).getNumericCellValue();
                contractProduct.setLoadingRate(LoadingRate+"");
            }
            if (row.getCell(6) != null){
                double BoxNum = row.getCell(6).getNumericCellValue();
                contractProduct.setBoxNum((int) BoxNum);
            }
            if (row.getCell(7) != null){
                double Price = row.getCell(7).getNumericCellValue();
                contractProduct.setPrice(Price);
            }
            if (row.getCell(8) != null){
                String ProductDesc = row.getCell(8).getStringCellValue();
                contractProduct.setProductDesc(ProductDesc);
            }
            if (row.getCell(9) != null){
                String ProductRequest = row.getCell(9).getStringCellValue();
                contractProduct.setProductRequest(ProductRequest);
            }

            Factory byFactory = factoryService.findByFactory(contractProduct.getFactoryName());
            contractProduct.setFactoryId(byFactory.getId());
            if (contractProduct.getPrice() != null && contractProduct.getCnumber() != null){
                contractProduct.setAmount(contractProduct.getPrice()*contractProduct.getCnumber());
            }

            contractProduct.setContractId(contractId);
            contractProduct.setCompanyName(getLogInCompanyName());
            contractProduct.setCompanyId(getLogInCompanyId());
            contractProductService.save(contractProduct);
        }
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractId;
    }
}
