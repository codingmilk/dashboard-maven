import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.time.temporal.ChronoUnit;
import java.text.DecimalFormat;

@RestController
@EnableAutoConfiguration
public class Service {
    // values
    static ArrayList<Integer> pulsometer = new ArrayList();
    static ArrayList<Integer> r = new ArrayList();
    static ArrayList<Integer> g = new ArrayList();
    static ArrayList<Integer> b = new ArrayList();
    static ArrayList<Float> engineEff = new ArrayList();

    @CrossOrigin
    @RequestMapping("/data")
    public Map<String, Object> home(Service s) {
        // REST API Service Method here
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime midnight = now.truncatedTo(ChronoUnit.DAYS);
        Duration duration = Duration.between(midnight,now);
        int secondsPassed = (int) duration.getSeconds();

        HashMap<String, Object> values = new HashMap<>();

        values.put("pulsometer", pulsometer.get(secondsPassed));
        values.put("r", r.get(secondsPassed));
        values.put("g", g.get(secondsPassed));
        values.put("b", b.get(secondsPassed));

        String pattern = "###.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        values.put("EngineEff",decimalFormat.format(engineEff.get(secondsPassed)));

        return values;
    }

    public static void loadData(String fileName){
        File file = new File(fileName);
        String[] values;
        boolean skipHeader = true;

        try{
            Scanner inputStream = new Scanner(file);
            while(inputStream.hasNext()){
                String data = inputStream.next();
                if ( skipHeader ) { skipHeader = false; continue;}                
                values = data.split(",");
                pulsometer.add(Integer.parseInt(values[1]));
                engineEff.add(Float.parseFloat(values[2]));
                r.add(Integer.parseInt(values[3]));
                g.add(Integer.parseInt(values[4]));
                b.add(Integer.parseInt(values[5]));
            }
            inputStream.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String fileName = "./dashBoardData.csv";
        loadData(fileName);
        SpringApplication.run(Service.class, args);
    }

}