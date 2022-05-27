package com.mugo.mugocompany.controller;

import com.google.gson.Gson;
import com.mugo.mugocompany.*;
import com.mugo.mugocompany.entity.ClientDetails;
import com.mugo.mugocompany.servicemanager.impl.ClientDetailsServiceImpl;
import com.mugo.mugocompany.servicemanager.impl.SanlamServiceImpl;
import com.mugo.mugocompany.servicemanager.service.QueryService;
import com.opencsv.CSVReader;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class MugoController {

    @Autowired
    private ClientDetailsServiceImpl clientDetailsService;

    @Autowired
    private SanlamServiceImpl sanlamService;

    @Autowired
    private QueryService queryService;

    Logger logger = LoggerFactory.getLogger(MugoController.class);

    @RequestMapping(value = "/api/v1/files/import-client", method = RequestMethod.POST)
    public ResponseEntity importClientFile(@RequestParam("file") MultipartFile files) throws IOException {

        try {
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
                            !status.equals("")) {

                        DbClientInfo dbClientInfo = new DbClientInfo(clientName, regNo, risk, status);
                        excelUsersList.add(dbClientInfo);


                    }

                }
            }

            Results results = clientDetailsService.addClientList(excelUsersList);
            return getResponse(results);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("There was an error with the excel sheet. Please consult the developer and try again."));

        }

    }

    private ResponseEntity<?> getResponse(Results results) {

        int statusCode = results.getCode();
        if (statusCode == 200) {
            return new ResponseEntity(new ResponseMessage("Data is been processed. Refresh to update."), HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().body(new ResponseMessage(results.getDetails()));

        }

    }

    @RequestMapping(value = "/api/v1/files/import-sanlam", method = RequestMethod.POST)
    public ResponseEntity importSanlamFile(@RequestParam("file") MultipartFile files) throws IOException {

        try {
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
                            !narration.equals("")) {

                        DbSanlamData dbSanlamData = new DbSanlamData(claimNumber, amount, narration, "");
                        dbSanlamDataList.add(dbSanlamData);

                    }

                }
            }

            Results results = sanlamService.addSanlamList(dbSanlamDataList);
            return getResponse(results);

        } catch (Exception e) {
            System.out.println("+++++++" + e);
            return ResponseEntity.badRequest().body(new ResponseMessage("There was an error with the excel sheet. Please consult the developer and try again."));

        }

    }

    @RequestMapping(value = "/api/v1/extract/{id}", method = RequestMethod.GET)
    public ResponseEntity extractValues(@PathVariable String id) {

        Results results = sanlamService.extractValues(id);
        return getResponse(results);
    }


    @GetMapping("/fetch")
    public List<ClientDetails> getexisting(String theString) {
        List<ClientDetails> page = queryService.listMatchingEntries(theString);
        return page;
    }


    /*YOU CAN UPDATE THIS FUNCTION TO REQUEST THE SECOND CSV AS FILE FROM THYMELEAF AND PASS THE FILE
    TO NEW FILE READER BELOW*/
    @SneakyThrows
    @RequestMapping(value = "/uploadsheettwo")
    public String uploadsheettwo() {

        //Displaying current date and time in 12 hour format with AM/PM
        DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy hh.mm.ss aa");
        String dateString2 = dateFormat2.format(new Date()).toString();

        String filename = "D:\\mugo-result - " + dateString2 + ".csv";

        File thefile = new File(filename);
        thefile.createNewFile();

        FileWriter writer = new FileWriter(thefile);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        CSVPrinter csvPrinter = new CSVPrinter(bufferedWriter, CSVFormat.DEFAULT.withHeader("clientname", "Reg no.", "Narration", "Amount"));

        List<List<String>> records = new ArrayList<List<String>>();

        logger.trace("Here : -------------------------");

        /*PASS THE FILE HERE*/
        try (CSVReader csvReader = new CSVReader(new FileReader("book.csv"));) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
        }

        for (int i = 1; i < records.size(); i++) {

            List<String> element = records.get(i);

            String referenceNumber = element.get(3).toString();
            String amountCapture = element.get(2).toString();

            String registrationNumber = referenceNumber.substring(referenceNumber.lastIndexOf("-") + 1);

            List<ClientDetails> thelist = getexisting(registrationNumber);

            if (!thelist.isEmpty()) {

                Gson gson = new Gson();
                String json = gson.toJson(thelist);
                GenerateObject generated_Kotlin_Object = gson.fromJson(json, GenerateObject.class);

                for (GenerateObjectItem generateObjectItem : generated_Kotlin_Object) {

                    String id = generateObjectItem.getId();
                    String status = generateObjectItem.getStatus();

                    String risk = generateObjectItem.getRisk();
                    String clientname = generateObjectItem.getClientName();
                    String registrationnumber = generateObjectItem.getClientName();

                    String narration = risk;
                    String amount = amountCapture;

                    csvPrinter.printRecord(clientname, registrationnumber, narration, amount);
                    logger.trace("AVAILABLE : -------------------------->    " + generated_Kotlin_Object.toString());

                }

            } else {

                logger.trace("NOT AVAILABLE : -------------------------->    ");

            }


        }

        csvPrinter.flush();
        csvPrinter.close();

        /*YOU CAN OPEN THE FILE DIRECTLY USING THE VARIABLE {filename} HERE INSTEAD OF JUST SAVING FILES*/
        /*YOU CAN ALSO SAVE FILE TO DB FOR RECORD KEEPING*/
        return "new ArrayList<>()";
    }

}
