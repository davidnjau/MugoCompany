package com.mugo.mugocompany.servicemanager.impl;

import com.mugo.mugocompany.DbSanlamData;
import com.mugo.mugocompany.FormatterHelper;
import com.mugo.mugocompany.Results;
import com.mugo.mugocompany.entity.ClientDetails;
import com.mugo.mugocompany.entity.SanlamData;
import com.mugo.mugocompany.entity.SanlamList;
import com.mugo.mugocompany.repository.SanlamDataRepository;
import com.mugo.mugocompany.repository.SanlamListRepository;
import com.mugo.mugocompany.servicemanager.service.SanlamService;
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
public class SanlamServiceImpl implements SanlamService {

    int pageNo = 1;
    int pageSize = 10;

    String sortField = "createdAt";
    String sortDirection = "DESC";

    @Autowired
    private SanlamDataRepository sanlamDataRepository;

    @Autowired
    private SanlamListRepository sanlamListRepository;

    private FormatterHelper formatterHelper = new FormatterHelper();

    @Override
    public Results addSanlamList(List<DbSanlamData> dbSanlamDataList) {

        Results results = new Results(200,
                "The data is being processed and will be updated ASAP.");
        addSanlamDataList(dbSanlamDataList);
        return results;
    }

    @Override
    public Results getSanlamList(int pageNo, int pageSize, String sortField, String sortDirection) {

        List<SanlamData> sanlamDataList = getSanlamListData(pageNo, pageSize, sortField, sortDirection);
        return new Results(200, sanlamDataList);
    }

    @Override
    public Results getSanlamInfo(String sanlamId) {

        SanlamData sanlamData = getSanlamData(sanlamId);
        if (sanlamData != null){
            return new Results(200, sanlamData);
        }else {
            return new Results(400, "The resource could not be found.");
        }

    }

    @Override
    public Results createSanlamData() {

        return null;
    }

    private SanlamList saveSanlamData(int totalAmount){

        String sanlamDataName = formatterHelper.getSanlamDataName();
        SanlamList sanlamList = new SanlamList(sanlamDataName, totalAmount, false);
        return sanlamListRepository.save(sanlamList);

    }

    @Override
    public Results getSanlamDataList(int pageNo, int pageSize, String sortField, String sortDirection) {

        List<SanlamList> sanlamList = getAllSanlamData(pageNo, pageSize, sortField, sortDirection);
        return new Results(200, sanlamList);

    }

    @Override
    public Results getSanlamDataInfo(String id) {

        SanlamList sanlamList = getAllSanlamData(id);
        if (sanlamList != null){
            return new Results(200, sanlamList);
        }else {
            return new Results(400, "Resource could not be found");
        }

    }

    private SanlamList getAllSanlamData(String sanlamId){

        Optional<SanlamList> optionalSanlamData = sanlamListRepository.findById(sanlamId);
        return optionalSanlamData.orElse(null);

    }

    public List<SanlamList> getAllSanlamData(int pageNo, int pageSize, String sortField, String sortDirection){

        String sortPageField = "";
        String sortPageDirection = "";

        if (sortField.equals("")){sortPageField = "amount"; }else {sortPageField = sortField;}
        if (sortDirection.equals("")){sortPageDirection = "ASC"; }else {sortPageDirection = sortField;}

        Sort sort = sortPageDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortPageField).ascending() : Sort.by(sortPageField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<SanlamList> data = sanlamListRepository.findAll(pageable);

        return data.getContent();

    }


    public List<SanlamData> getSanlamListData(int pageNo, int pageSize, String sortField, String sortDirection){

        String sortPageField = "";
        String sortPageDirection = "";

        if (sortField.equals("")){sortPageField = "amount"; }else {sortPageField = sortField;}
        if (sortDirection.equals("")){sortPageDirection = "ASC"; }else {sortPageDirection = sortField;}

        Sort sort = sortPageDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortPageField).ascending() : Sort.by(sortPageField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<SanlamData> data = sanlamDataRepository.findAll(pageable);

        return data.getContent();

    }

    private void addSanlamDataList(List<DbSanlamData> dbSanlamDataList){

        int totalAmount = 0;

        List<SanlamData> sanlamDataList = new ArrayList<>();

        for (int i = 0; i < dbSanlamDataList.size(); i++){

            String claimNumber = dbSanlamDataList.get(i).getClaimNumber();
            String amount = dbSanlamDataList.get(i).getAmount();
            String narration = dbSanlamDataList.get(i).getNarration();

            totalAmount = totalAmount + Integer.parseInt(amount);

            SanlamData sanlamData = new SanlamData(claimNumber, amount, narration, "");
            sanlamDataList.add(sanlamData);
        }

        saveSanlamData(totalAmount);

        formatterHelper.saveSanlamDataList(sanlamDataList, sanlamDataRepository);

    }

    private SanlamData getSanlamData(String sanlamId){

        Optional<SanlamData> optionalSanlamData = sanlamDataRepository.findById(sanlamId);
        return optionalSanlamData.orElse(null);

    }
}
