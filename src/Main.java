import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static spark.Spark.port;

public class Main {

    static ArrayList<Person> people = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        System.out.println("Starting People Web... \nPlease open your browser!");

        loadPeopleFile();

        Spark.init();


        //<editor-fold desc="GET /">
        Spark.get("/", (request, response) -> {
                    HashMap m = new HashMap();
                    String offset = request.queryParams("offset");

                    int offsetNum = 0;
                    if (offset != null) {
                        offsetNum = Integer.parseInt(offset);
                    }
                    List<Person> p = people.subList(offsetNum, offsetNum + 20);

                    int prevOffset = offsetNum - 20;
                    if (prevOffset >= 0) {
                        m.put("prevOffset", prevOffset);
                    }
                    int nextOffset = offsetNum + 20;
                    if (nextOffset <= 980) {
                        m.put("nextOffset", nextOffset);
                    }

                    m.put("people", p);
                    return new ModelAndView(m, "people-list.html");
                },
                new MustacheTemplateEngine()
        );
        //</editor-fold>

        //<editor-fold desc="GET /person">
        Spark.get("/person", (request, response) -> {
                    String personId = request.queryParams("id");
                    String offset = request.queryParams("offset");
                    HashMap p = new HashMap();
                    ArrayList<Person> personDetails = new ArrayList<>();
                    for (Person p1 : people) {
                        if (p1.id.equals(personId)) {
                            personDetails.add(p1);
                        }
                    }
                    p.put("details", personDetails);
                    p.put("offset", offset);
                    return new ModelAndView(p, "person-details.html");
                },
                new MustacheTemplateEngine()
        );
        //</editor-fold>


    }


    static void loadPeopleFile() throws Exception {
        File f = new File("people.csv");
        Scanner scanner = new Scanner(f);

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] columns = line.split(",");
            Person personInfo = new Person(columns[0], columns[1], columns[2], columns[3], columns[4], columns[5]);
            if (!personInfo.id.equals("id")) {
                people.add(personInfo);
            }
        }
    }
}

