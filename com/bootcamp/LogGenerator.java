package com.bootcamp;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
//import java.nio.file.attribute.UserPrincipalLookupService;



public class LogGenerator {
    private static String DT_URL = "DT_URL";
    private static String DT_LOG_INGEST_TOKEN = "DT_LOG_INGEST_TOKEN";
    //private static final String DIRECTORY_PATH = "logs";
    //private static final String USER = System.getProperty("user.name");
    private static final String TEMPLATES_PATH = System.getProperty("user.dir") + "/templates/";
    private static String dtUrl, logToken;
    private static HttpClient client;
    private static String dtUrlEndPoint;
    private static String authorization;

    public static void main(String[] args) {
        //createLogDirectory();
        dtUrl = System.getenv(DT_URL);
        dtUrlEndPoint = dtUrl + "/api/v2/logs/ingest";
        logToken = System.getenv(DT_LOG_INGEST_TOKEN);
        authorization = "Api-Token " + logToken;
        client = HttpClient.newHttpClient();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            generateData();
        };

        // Schedule the task to run every minute
        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);
    }

    private static void generateData() {
        generateInfoLogs();
        generateErrorLogs();
    }

    /*private static void createLogDirectory() {
        Path directory = Paths.get(DIRECTORY_PATH);
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
                UserPrincipalLookupService lookupService = FileSystems.getDefault().getUserPrincipalLookupService();

                Files.setOwner(directory, lookupService.lookupPrincipalByName(USER));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    */
    private static void generateInfoLogs() {
        Random random = new Random();
        int infologs = random.nextInt(10);
        
        for (int i = 0; i < infologs; i++) {
            String dttransid = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "DYNATRANSACTION" + (random.nextInt(100000) * random.nextInt(100000));
            String pmdwid = generateRandomString(13);
            String revenue = random.nextInt(100) + "." + random.nextInt(100);
            String inittime = getCurrentTimestamp();

            processDeviceLog("01-DEVICE01.xml", "processor-01.log", dttransid, pmdwid, revenue, inittime);
            sleepRandom(1000);

            String dynatime = getCurrentTimestamp();
            processDeviceLog("02-DEVICE02.json", "processor-02.log", dttransid, pmdwid, revenue, inittime, dynatime);
            sleepRandom(100);

            dynatime = getCurrentTimestamp();
            processDeviceLog("03-DEVICE03.txt", "processor-03.log", dttransid, pmdwid, revenue, inittime, dynatime);
            sleepRandom(100);

            dynatime = getCurrentTimestamp();
            processDeviceLog("04-DEVICE04.txt", "processor-04.log", dttransid, pmdwid, revenue, inittime, dynatime);
            sleepRandom(1000);

            dynatime = getCurrentTimestamp();
            processDeviceLog("05-DEVICE05.xml", "processor-05.log", dttransid, pmdwid, revenue, inittime, dynatime);
            sleepRandom(100);

            dynatime = getCurrentTimestamp();
            processDeviceLog("06-DEVICE06.json", "processor-06.log", dttransid, pmdwid, revenue, inittime, dynatime);
        }
    }

    private static void generateErrorLogs() {
        Random random = new Random();
        int errorgen = random.nextInt(100);
        int errorlogs = random.nextInt(10);

        if (errorgen % 10 == 3 || errorgen % 10 == 7) {
            for (int i = 0; i < errorlogs; i++) {
                String errortime = getCurrentTimestamp();
                String dterrorid = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "DYNATRANSACTION" + (random.nextInt(100000) * random.nextInt(100000));
                processDeviceError("ERROR-DEVICE01.xml", "processor-01.log", dterrorid, errortime);
            }
        }
    }

    private static void processDeviceLog(String templateFile, String logFile, String dttransid, String pmdwid, String revenue, String inittime, String... dynatime) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(TEMPLATES_PATH + templateFile)), StandardCharsets.UTF_8);
            content = content.replace("DYNATRANSID", dttransid)
                             .replace("PMDWID", pmdwid)
                             .replace("REVENUE", revenue)
                             .replace("INITTIME", inittime);

            if (dynatime.length > 0) {
                content = content.replace("DYNATIME", dynatime[0]);
            }
            int lastDotIndex = templateFile.lastIndexOf('.');
            String type = templateFile.substring(lastDotIndex + 1);
            System.out.println(content);

            send2DT(content, type, templateFile);
            //Files.write(Paths.get(DIRECTORY_PATH + "/" + logFile), (content + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processDeviceError(String templateFile, String logFile, String dterrorid, String errorTime) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(TEMPLATES_PATH + templateFile)), StandardCharsets.UTF_8);
            content = content.replace("ERRORTIME", errorTime)
                             .replace("DTERRORID", dterrorid);
            
            int lastDotIndex = templateFile.lastIndexOf('.');
            String type = templateFile.substring(lastDotIndex + 1);
            System.out.println(content);
            send2DT(content, type, templateFile);
            //System.out.println(content);
            //Files.write(Paths.get(DIRECTORY_PATH + "/" + logFile), (content + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void send2DT(String content, String type, String templateFile) {
        String contentType = "text/plain; charset=utf-8";
        if ( type == "json") {
            contentType = "application/json; charset=utf-8";
        }
        //String acceptHeader = contentType + "; charset=utf-8";
        String acceptHeader = "application/json; charset=utf-8";
        System.out.println(acceptHeader);
        //                 .header("accept", "application/json; charset=utf-8")
        System.out.println(contentType);
        System.out.println(dtUrlEndPoint);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(dtUrlEndPoint))
                .header("Accept", acceptHeader)
                .header("Authorization", authorization)
                .header("Content-Type", contentType)
                .POST(BodyPublishers.ofString(content))
                .build();
        
        System.out.println(request.toString());

        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            // Handle the response
            if (response.statusCode() == 200 || response.statusCode() == 204)  {
                System.out.println("Sent " + contentType + " for file " + templateFile + ". Response Code=" + response.statusCode() );
            } else {
                System.err.println("Failed to send logs: " + response.body());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String getCurrentTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }

    private static void sleepRandom(int maxMillis) {
        try {
            Thread.sleep(new Random().nextInt(maxMillis));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
