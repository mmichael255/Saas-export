package com.chenbaiyu.web.controller.cargo;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.chenbaiyu.domain.cargo.ExtCproduct;
import com.chenbaiyu.domain.cargo.ExtCproductExample;
import com.chenbaiyu.domain.cargo.Factory;
import com.chenbaiyu.domain.cargo.FactoryExample;
import com.chenbaiyu.service.cargo.ExtCproductService;
import com.chenbaiyu.service.cargo.FactoryService;
import com.chenbaiyu.web.controller.BaseController;

import com.chenbaiyu.web.utils.FileUploadUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/cargo/extCproduct")
public class ExtCproductController extends BaseController {
    @Reference(timeout = 100000,retries = 0)
    ExtCproductService extCproductService;
    @Reference(timeout = 10000000,retries = 0)
    FactoryService factoryService;
    @Autowired
    FileUploadUtil fileUploadUtil;

    @RequestMapping("/list")
    public String list(String contractId, String contractProductId, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize){
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria criteria = factoryExample.createCriteria();
        criteria.andCtypeEqualTo("附件");
        List<Factory> factories = factoryService.findAll(factoryExample);

        ExtCproductExample extCproductExample = new ExtCproductExample();
        ExtCproductExample.Criteria extCproductExampleCriteria = extCproductExample.createCriteria();
        extCproductExampleCriteria.andContractProductIdEqualTo(contractProductId);
        PageInfo<ExtCproduct> extCproductServiceByPage = extCproductService.findByPage(extCproductExample, pageNum, pageSize);
        req.setAttribute("factoryList",factories);
        req.setAttribute("PageInfo",extCproductServiceByPage);
        req.setAttribute("contractProductId",contractProductId);
        req.setAttribute("contractId",contractId);
        return "/cargo/extc/extc-list";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(String id,String contractId,String contractProductId){
        ExtCproductExample extCproductExample = new ExtCproductExample();
        ExtCproductExample.Criteria criteria = extCproductExample.createCriteria();
        ExtCproduct byId = extCproductService.findById(id);
        req.setAttribute("extCproduct",byId);
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria factoryExampleCriteria = factoryExample.createCriteria();
        factoryExampleCriteria.andCtypeEqualTo("附件");
        List<Factory> factories = factoryService.findAll(factoryExample);
        req.setAttribute("factoryList",factories);
        req.setAttribute("contractId",contractId);
        req.setAttribute("contractProductId",contractProductId);
        return "/cargo/extc/extc-update";
    }

    @RequestMapping("/edit")
    public String edit(ExtCproduct extCproduct, String contractId, String contractProductId, MultipartFile productPhoto){
        try {
            String imgURL = fileUploadUtil.upload(productPhoto);
            extCproduct.setProductImage("http://"+imgURL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        extCproduct.setCompanyId(getLogInCompanyId());
        extCproduct.setCompanyName(getLogInCompanyName());
        extCproduct.setContractId(contractId);
        extCproduct.setContractProductId(contractProductId);

        if (StringUtils.isEmpty(extCproduct.getId())){
            extCproduct.setCreateBy(getLoginUser().getId());
            extCproduct.setCreateDept(getLoginUser().getDeptId());
            extCproductService.save(extCproduct);
        }else {
            extCproduct.setUpdateBy(getLoginUser().getUserName());
            extCproductService.update(extCproduct);
        }
        return "redirect:/cargo/extCproduct/list.do?contractId="+contractId+"&contractProductId="+contractProductId;
    }

    @RequestMapping("/delete")
    public String delete(String id,String contractId,String contractProductId){
        extCproductService.delete(id);
        return "redirect:/cargo/extCproduct/list.do?contractId="+contractId+"&contractProductId="+contractProductId;
    }

}
