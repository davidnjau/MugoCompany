package com.mugo.mugocompany.controller;

import com.mugo.mugocompany.*;
import com.mugo.mugocompany.servicemanager.impl.ClientDetailsServiceImpl;
import com.mugo.mugocompany.servicemanager.impl.SanlamServiceImpl;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MugoController {

    @Autowired
    private ClientDetailsServiceImpl clientDetailsService;

    @Autowired
    private SanlamServiceImpl sanlamService;

    @RequestMapping(value = "/api/v1/files/import-client", method = RequestMethod.POST)
    public ResponseEntity importClientFile(@RequestParam("file") MultipartFile files) throws IOException {

        try{
            List<DbClientInfo> excelUsersList = new ArrayList<>();

            XSSFWorkbook workbook = new XSSFWorkbook(files.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);

            for (int index = 0; index < worksheet.getPhysicalNumberOfRows(); index++) {
                if (index > 0) {

                    XSSFRow row = worksheet.getRow(index);
                    String clientName = row.getCell(1).getStringCellValue();
                    String regNo = row.getCell(2).getStringCellValue();
                    String risk = row.getCell(3).getStringCellValue();
                    String status = row.getCell(4).getStringCellValue();

                    if (!clientName.equals("") &&
                            !regNo.equals("") &&
                            !risk.equals("") &&
                            !status.equals("")){

                        DbClientInfo dbClientInfo = new DbClientInfo(clientName, regNo, risk, status);
                        excelUsersList.add(dbClientInfo);


                    }

                }
            }

            Results results = clientDetailsService.addClientList(excelUsersList);
            return getResponse(results);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ResponseMessage("There was an error with the excel sheet. Please consult the developer and try again."));

        }

    }

    private ResponseEntity<?> getResponse(Results results) {

        int statusCode = results.getCode();
        if (statusCode == 200){
            return new ResponseEntity(new ResponseMessage("Data is been processed. Refresh to update."), HttpStatus.OK);
        }else {
            return ResponseEntity.badRequest().body(new ResponseMessage(results.getDetails()));

        }

    }

    @RequestMapping(value = "/api/v1/files/import-sanlam", method = RequestMethod.POST)
    public ResponseEntity importSanlamFile(@RequestParam("file") MultipartFile files) throws IOException {

        try{
            List<DbSanlamData> dbSanlamDataList = new ArrayList<>();

            XSSFWorkbook workbook = new XSSFWorkbook(files.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);

            for (int index = 0; index < worksheet.getPhysicalNumberOfRows(); index++) {
                if (index > 0) {

                    XSSFRow row = worksheet.getRow(index);
                    String claimNumber = row.getCell(1).getStringCellValue();
                    String amount = String.valueOf((int) row.getCell(2).getNumericCellValue());
                    String narration = row.getCell(3).getStringCellValue();

                    if (!claimNumber.equals("") &&
                            !amount.equals("") &&
                            !narration.equals("")){

                        DbSanlamData dbSanlamData = new DbSanlamData(claimNumber, amount, narration, "");
                        dbSanlamDataList.add(dbSanlamData);

                    }

                }
            }

            Results results = sanlamService.addSanlamList(dbSanlamDataList);
            return getResponse(results);

        }catch (Exception e){
            System.out.println("+++++++"+e);
            return ResponseEntity.badRequest().body(new ResponseMessage("There was an error with the excel sheet. Please consult the developer and try again."));

        }

    }

    @RequestMapping(value = "/api/v1/extract/{id}", method = RequestMethod.GET)
    public ResponseEntity extractValues(@PathVariable String id){

        Results results = sanlamService.extractValues(id);
        return getResponse(results);
    }

}
