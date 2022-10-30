package com.chenbaiyu.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chenbaiyu.dao.cargo.*;
import com.chenbaiyu.domain.cargo.*;
import com.chenbaiyu.service.cargo.ExportService;
import com.chenbaiyu.vo.ExportProductResult;
import com.chenbaiyu.vo.ExportResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service
public class ExportServiceImpl implements ExportService {
    @Autowired
    ExportDao exportDao;
    @Autowired
    ContractDao contractDao;
    @Autowired
    ContractProductDao contractProductDao;
    @Autowired
    ExportProductDao exportProductDao;
    @Autowired
    ExtCproductDao extCproductDao;
    @Autowired
    ExtEproductDao extEproductDao;

    @Override
    public Export findById(String id) {
        return exportDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Export export) {
        export.setId(UUID.randomUUID().toString());
        export.setCreateTime(new Date());
        String[] contractIds = export.getContractIds().split(",");
        String contractNos = "";
        int proNum = 0;
        int extNum = 0;
        for (String contractId : contractIds) {
            Contract contract = contractDao.selectByPrimaryKey(contractId);
            contractNos += contract.getContractNo()+" ";
            proNum += contract.getProNum();
            extNum += contract.getExtNum();

            contract.setState(2);
            contractDao.updateByPrimaryKeySelective(contract);
        }
       export.setCustomerContract(contractNos.trim());
        export.setProNum(proNum);
        export.setExtNum(extNum);
        export.setInputDate(new Date());
        export.setState(0);
        exportDao.insertSelective(export);

        ContractProductExample contractProductExample = new ContractProductExample();
        ContractProductExample.Criteria cpCriteria = contractProductExample.createCriteria();
        cpCriteria.andContractIdIn(Arrays.asList(contractIds));

        List<ContractProduct> contractProducts = contractProductDao.selectByExample(contractProductExample);
        HashMap<String, String> map = new HashMap<>();
        if (contractProducts != null && contractProducts.size() > 0) {
            for (ContractProduct contractProduct : contractProducts) {
                ExportProduct exportProduct = new ExportProduct();
                BeanUtils.copyProperties(contractProduct, exportProduct);
                exportProduct.setId(UUID.randomUUID().toString());
                exportProduct.setExportId(export.getId());
                map.put(export.getId(), exportProduct.getId());
                exportProductDao.insertSelective(exportProduct);
            }
        }

        ExtCproductExample extCproductExample = new ExtCproductExample();
        ExtCproductExample.Criteria epcriteria = extCproductExample.createCriteria();
        epcriteria.andContractIdIn(Arrays.asList(contractIds));

        List<ExtCproduct> extCproducts = extCproductDao.selectByExample(extCproductExample);
        if (extCproducts != null && extCproducts.size() > 0) {
            for (ExtCproduct extCproduct : extCproducts) {
                ExtEproduct extEproduct = new ExtEproduct();
                BeanUtils.copyProperties(extCproduct, extEproduct);
                extEproduct.setId(UUID.randomUUID().toString());
                extEproduct.setExportId(export.getId());
                extEproduct.setExportProductId(map.get(export.getId()));
                extEproductDao.insertSelective(extEproduct);
            }
        }

    }

    @Override
    public void update(Export export) {
        export.setUpdateTime(new Date());
        exportDao.updateByPrimaryKeySelective(export);

        List<ExportProduct> exportProducts = export.getExportProducts();
        if (exportProducts != null){
            for (ExportProduct exportProduct : exportProducts) {
                exportProductDao.updateByPrimaryKeySelective(exportProduct);
            }
        }
    }

    @Override
    public void delete(String id) {
        exportDao.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<Export> findByPage(ExportExample example, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);

        return new PageInfo<>(exportDao.selectByExample(example));
    }

    @Override
    public void updateResult(ExportResult exportResult) {
        Export export = new Export();
        export.setId(exportResult.getExportId());
        export.setState(exportResult.getState());
        exportDao.updateByPrimaryKeySelective(export);


        Set<ExportProductResult> products = exportResult.getProducts();
        for (ExportProductResult product : products) {
            ExportProduct exportProduct = new ExportProduct();
            exportProduct.setTax(product.getTax());
            exportProduct.setId(product.getExportProductId());
            exportProductDao.updateByPrimaryKeySelective(exportProduct);
        }
    }
}
