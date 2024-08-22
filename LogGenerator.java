import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.nio.file.attribute.UserPrincipalLookupService;



public class LogGenerator {

    private static final String DIRECTORY_PATH = "/var/log/multilog";
    private static final String USER = System.getProperty("user.name");
    private static final String TEMPLATES_PATH = System.getProperty("user.dir") + "/templates/";

    public static void main(String[] args) {
        createLogDirectory();
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

    private static void createLogDirectory() {
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

            Files.write(Paths.get(DIRECTORY_PATH + "/" + logFile), (content + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processDeviceError(String templateFile, String logFile, String dterrorid, String errorTime) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(TEMPLATES_PATH + templateFile)), StandardCharsets.UTF_8);
            content = content.replace("ERRORTIME", errorTime)
                             .replace("DTERRORID", dterrorid);
            Files.write(Paths.get(DIRECTORY_PATH + "/" + logFile), (content + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
        catch (IOException e) {
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
