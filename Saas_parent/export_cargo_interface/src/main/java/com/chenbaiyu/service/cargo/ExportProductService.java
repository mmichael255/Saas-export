package com.chenbaiyu.service.cargo;

import com.chenbaiyu.domain.cargo.*;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ExportProductService {

    ExportProduct findById(String id);

    void save(ExportProduct exportProduct);

    void update(ExportProduct exportProduct);

    void delete(String id);

    List<ExportProduct> findAll(ExportProductExample exportProductExample);

    PageInfo<ExportProduct> findByPage(ExportProductExample exportProductExample,
                                       int pageNum, int pageSize);
}
