package com.mugo.mugocompany.servicemanager.service;

import com.mugo.mugocompany.DbClientInfo;
import com.mugo.mugocompany.DbSanlamData;
import com.mugo.mugocompany.Results;

import java.util.List;

public interface SanlamService {

    Results addSanlamList(List<DbSanlamData> dbSanlamDataList);
    Results getSanlamList(int pageNo, int pageSize, String sortField, String sortDirection);
    Results getSanlamInfo(String sanlamId);

    Results createSanlamData();
    Results getSanlamDataList(int pageNo, int pageSize, String sortField, String sortDirection);
    Results getSanlamDataInfo(String id);

    Results extractValues(String sanlamId);

}
