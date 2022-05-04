package com.mugo.mugocompany.servicemanager.service;

import com.mugo.mugocompany.DbClientInfo;
import com.mugo.mugocompany.Results;
import com.mugo.mugocompany.entity.ClientDetails;

import java.util.List;

public interface ClientDetailsService {

    Results addClientList(List<DbClientInfo> dbClientInfoList);
    Results addClient(DbClientInfo dbClientInfo);
    Results getClientList(int pageNo, int pageSize, String sortField, String sortDirection);
    Results getClientInfo(String clientId);
    ClientDetails getClientByRegNo(String regNo);

}
