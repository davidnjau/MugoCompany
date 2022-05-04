package com.mugo.mugocompany.servicemanager;

import com.mugo.mugocompany.DbClientInfo;
import com.mugo.mugocompany.Results;

import java.util.List;

public interface ClientDetailsService {

    Results addClientList(List<DbClientInfo> dbClientInfoList);
    Results addClient(DbClientInfo dbClientInfo);
    Results getClientList(int pageNo, int pageSize, String sortField, String sortDirection);
    Results getClientInfo(String clientId);

}
