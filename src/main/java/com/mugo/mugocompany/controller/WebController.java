package com.mugo.mugocompany.controller;

import com.mugo.mugocompany.entity.ClientDetails;
import com.mugo.mugocompany.entity.SanlamList;
import com.mugo.mugocompany.servicemanager.impl.ClientDetailsServiceImpl;
import com.mugo.mugocompany.servicemanager.impl.SanlamServiceImpl;
import com.opencsv.CSVReader;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class WebController {

    int pageNo = 1;
    int pageSize = 20;

    String sortField = "createdAt";
    String sortDirection = "ASC";

    String userSortField = "registrationNumber";
    String userSortDirection = "DESC";

    @Autowired
    private ClientDetailsServiceImpl clientDetailsService;

    @Autowired
    private SanlamServiceImpl sanlamService;


    @RequestMapping(value = "/")
    public ModelAndView getHome() {

        return getModelAndView();
    }

    @RequestMapping(value = "/master")
    public ModelAndView getMaster() {
        return getModelAndView();
    }

    @NotNull
    private ModelAndView getModelAndView() {
        ModelAndView modelAndView = new ModelAndView("master");
        List<ClientDetails> clientDetailsList = clientDetailsService
                .getClientListData(pageNo, pageSize, userSortField, userSortDirection);

        modelAndView.addObject("clientList", clientDetailsList);
        modelAndView.addObject("pageNo", pageNo);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("total", clientDetailsList.size());


        return modelAndView;
    }

    @RequestMapping(value = "/sanlam")
    public ModelAndView getSanlam() {
        ModelAndView modelAndView = new ModelAndView("sanlam");
        List<SanlamList> sanlamDataList = sanlamService
                .getAllSanlamData(pageNo, pageSize, "systemName", userSortDirection);

        modelAndView.addObject("sanlamList", sanlamDataList);
        modelAndView.addObject("pageNo", pageNo);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("total", sanlamDataList.size());


        return modelAndView;
    }

    @RequestMapping(value = "/finance")
    public String getFinance() {
        return "finance";
    }

    @RequestMapping(value = "/recipients")
    public String getRecipients() {
        return "recipients";
    }

    @RequestMapping(value = "/sanlam_details")
    public String getSanlamDetails() {
        return "sanlam_details";
    }

    @RequestMapping(value = "/finance_details")
    public String getFinanceDetails() {
        return "finance_details";
    }


//    @RequestMapping(value = "/error")
//    public String getError(){
//        return "error";
//    }


}
