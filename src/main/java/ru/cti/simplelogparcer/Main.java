package ru.cti.simplelogparcer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple log parser
 * parser scans log files in specified directory.
 * In each scanned file if parser sees specified string, then all messages of that call with
 * specified regexp identifier will be parsed into found log file
 *
 * @author Eugeny
 */
public class Main {
    private static final Logger logger = LogManager.getRootLogger();
    // directory path with log files that will be parsed
    private String logDirPath;
    // file extension of log files. All files with this extension will be parsed in specified directory
    private String logFileExtension;
    // path of output parsed information
    private String foundLogPath;
    // regular expression of call identifier. Used for extracting call messages from log file
    private String callIdRegexp;
    // if parser sees that string, then all messages of that call with aforementioned regexp identifier will be parsed into found log file
    private String soughtError;

    public Main(String logDirPath, String logFileExtension, String foundLogPath, String callIdRegexp, String soughtError) {
        this.logDirPath = logDirPath;
        this.logFileExtension = logFileExtension;
        this.foundLogPath = foundLogPath;
        this.callIdRegexp = callIdRegexp;
        this.soughtError = soughtError;
    }

    /** Method parses log files*/
    public void parse() throws IOException {
        File dir = new File(logDirPath);
        File foundLog = new File(foundLogPath);
        // create or delete found log file if exists
        if (!foundLog.exists()) {
            foundLog.createNewFile();
        } else {
            foundLog.delete();
        }
        FileWriter foundLogWriter = new FileWriter(foundLog, true);
        // create an array with log files in specified directory
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(logFileExtension)) {
                    return true;
                }
                return false;
            }
        });
        Pattern pattern = Pattern.compile(callIdRegexp);
        // handle and parse each file in an array
        for (File file : files) {
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                // write file name in output log file
                foundLogWriter.append("\r\n");
                foundLogWriter.append("\r\n");
                foundLogWriter.append("\r\n" + file.getName());
                foundLogWriter.append("\n==========================================================================");
                String buffer;
                // seek for error string and then parse call identifier in this line
                while ((buffer = bufferedReader.readLine()) != null) {
                    if (buffer.contains(soughtError)) {
                        String foundString;
                        String foundTime;
                        Matcher matcher = pattern.matcher(buffer);
                        // if parser finds call identifier in error string, it observes file again and writes call messages with the call id to found log file
                        if (matcher.find()) {
                            foundString = matcher.group();
                            foundTime = buffer.substring(0, 5);
                            foundLogWriter.append("\r\n");
                            foundLogWriter.append("\r\n" + foundString);
                            FileReader foundFileReader = new FileReader(file);
                            BufferedReader findBufferedReader = new BufferedReader(foundFileReader);
                            String findBuffer;
                            while ((findBuffer = findBufferedReader.readLine()) != null) {
                                if (findBuffer.contains(foundString) && findBuffer.startsWith(foundTime)) {
                                    foundLogWriter.append("\r\n" + findBuffer);
                                }

                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.catching(e);
            } finally {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.catching(e);
                }
            }
        }
        foundLogWriter.close();
    }


    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ru.cti.simplelogparcer.Configuration.class);
        Main main = (Main) context.getBean("main");
        try {
            main.parse();
        } catch (IOException e) {
            e.printStackTrace();
            logger.catching(e);
        }
    }
}
