package com.mugo.mugocompany.servicemanager;

import com.mugo.mugocompany.DbClientInfo;
import com.mugo.mugocompany.FormatterHelper;
import com.mugo.mugocompany.Results;
import com.mugo.mugocompany.entity.ClientDetails;
import com.mugo.mugocompany.repository.ClientDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientDetailsServiceImpl implements ClientDetailsService{

    int pageNo = 1;
    int pageSize = 10;

    String sortField = "createdAt";
    String sortDirection = "DESC";

    private FormatterHelper formatterHelper = new FormatterHelper();

    @Autowired
    private ClientDetailsRepository clientDetailsRepository;

    @Override
    public Results addClientList(List<DbClientInfo> dbClientInfoList) {

        try {
            saveClientList(dbClientInfoList);
            return new Results(200, "The data has been received and its been processed");
        }catch (Exception e){
            return new Results(400, "There was an issue with the provided data");
        }

    }

    @Override
    public Results addClient(DbClientInfo dbClientInfo) {

        ClientDetails clientDetails = saveClientDetails(dbClientInfo);
        return new Results(200, clientDetails);
    }

    @Override
    public Results getClientList(int pageNo, int pageSize, String sortField, String sortDirection) {

        List<ClientDetails> clientDetailsList = getClientListData(pageNo, pageSize, sortField, sortDirection);
        return new Results(200,clientDetailsList);
    }

    @Override
    public Results getClientInfo(String clientId) {

        ClientDetails clientDetails = getClientData(clientId);
        if (clientDetails != null){
            return new Results(200, clientDetails);

        }else {
            return new Results(400, "The client cold not be found.");
        }
    }

    private ClientDetails saveClientDetails(DbClientInfo dbClientInfo){

        String clientName = dbClientInfo.getClientName();
        String regNo = dbClientInfo.getRegNumber();
        String status = dbClientInfo.getStatus();
        String risk = dbClientInfo.getRisk();

        ClientDetails clientDetails = new ClientDetails(clientName, regNo, status, risk);
        return clientDetailsRepository.save(clientDetails);
    }
    private void saveClientList(List<DbClientInfo> dbClientInfoList){

        List<ClientDetails> clientDetailsList = new ArrayList<>();
        for (int i = 0; i < dbClientInfoList.size(); i++){

            String clientName = dbClientInfoList.get(i).getClientName();
            String regNo = dbClientInfoList.get(i).getRegNumber();
            String status = dbClientInfoList.get(i).getStatus();
            String risk = dbClientInfoList.get(i).getRisk();

            ClientDetails clientDetails = new ClientDetails(clientName, regNo, status, risk);
            clientDetailsList.add(clientDetails);
        }

        formatterHelper.saveClientDataList(clientDetailsList,clientDetailsRepository);

    }
    public List<ClientDetails> getClientListData(int pageNo, int pageSize, String sortField, String sortDirection){

        String sortPageField = "";
        String sortPageDirection = "";

        if (sortField.equals("")){sortPageField = "clientName"; }else {sortPageField = sortField;}
        if (sortDirection.equals("")){sortPageDirection = "ASC"; }else {sortPageDirection = sortField;}

        Sort sort = sortPageDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortPageField).ascending() : Sort.by(sortPageField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<ClientDetails> contactsData = clientDetailsRepository.findAll(pageable);

        return contactsData.getContent();

    }
    private ClientDetails getClientData(String clientId){

        Optional<ClientDetails> optionalClientDetails = clientDetailsRepository.findById(clientId);
        return optionalClientDetails.orElse(null);

    }
}
