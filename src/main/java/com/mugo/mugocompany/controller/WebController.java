package com.mugo.mugocompany.controller;

import com.mugo.mugocompany.Results;
import com.mugo.mugocompany.entity.ClientDetails;
import com.mugo.mugocompany.servicemanager.ClientDetailsServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WebController {

    int pageNo = 1;
    int pageSize = 10;

    String sortField = "createdAt";
    String sortDirection = "DESC";

    String userSortField = "clientName";
    String userSortDirection = "DESC";

    @Autowired
    private ClientDetailsServiceImpl clientDetailsService;


    @RequestMapping(value = "/")
    public ModelAndView getHome(){

        return getModelAndView();
    }
    @RequestMapping(value = "/master")
    public ModelAndView getMaster(){
        return getModelAndView();
    }

    @NotNull
    private ModelAndView getModelAndView() {
        ModelAndView modelAndView = new ModelAndView("master");
        List<ClientDetails> clientDetailsList = clientDetailsService.getClientListData(pageNo, pageSize, userSortField, userSortDirection);

        modelAndView.addObject("clientList", clientDetailsList);
        modelAndView.addObject("pageNo", pageNo);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("total", clientDetailsList.size());


        return modelAndView;
    }

    @RequestMapping(value = "/sanlam")
    public String getSanlam(){
        return "sanlam";
    }
    @RequestMapping(value = "/finance")
    public String getFinance(){
        return "finance";
    }
    @RequestMapping(value = "/recipients")
    public String getRecipients(){
        return "recipients";
    }
    @RequestMapping(value = "/sanlam_details")
    public String getSanlamDetails(){
        return "sanlam_details";
    }
    @RequestMapping(value = "/finance_details")
    public String getFinanceDetails(){
        return "finance_details";
    }


//    @RequestMapping(value = "/error")
//    public String getError(){
//        return "error";
//    }


}
