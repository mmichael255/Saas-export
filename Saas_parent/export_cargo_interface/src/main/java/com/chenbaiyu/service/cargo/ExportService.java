package com.chenbaiyu.service.cargo;

import com.chenbaiyu.domain.cargo.Export;
import com.chenbaiyu.domain.cargo.ExportExample;
import com.chenbaiyu.vo.ExportResult;
import com.github.pagehelper.PageInfo;

public interface ExportService {
    Export findById(String id);

    void save(Export export);

    void update(Export export);

    void delete(String id);

    PageInfo<Export> findByPage(ExportExample example,
                                int pageNum, int pageSize);

    void updateResult(ExportResult exportResult);
}
