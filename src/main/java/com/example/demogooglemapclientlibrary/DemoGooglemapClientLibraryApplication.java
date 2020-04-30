package com.example.demogooglemapclientlibrary;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.GeocodingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class DemoGooglemapClientLibraryApplication implements CommandLineRunner {
    GeoApiContext context = new GeoApiContext.Builder()
            .apiKey("AIzaSyDLymJfALn1A4Vq0DF8AHhAyl78j70HsCU")
            .build();
    private static Logger LOG = LoggerFactory
            .getLogger(DemoGooglemapClientLibraryApplication.class);

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");

        SpringApplication.run(DemoGooglemapClientLibraryApplication.class, args);
        LOG.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) throws Exception {

        //PREPARE DATA
        Driver driverA = new Driver("Driver A", new String[]{"Phạm Văn Hai, Tân Bình, Hồ Chí Minh, Việt Nam",
                "Cách Mạng Tháng Tám, Cư xá Bắc Hải, Phường 15, Quận 10, Hồ Chí Minh, Việt Nam"});
        Driver driverB = new Driver("Driver B", new String[]{"Trường Sa, Bình Thạnh, Hồ Chí Minh, Việt Nam",
                "Phan Xích Long, Bình Thạnh, Hồ Chí Minh, Việt Nam"});
        Driver driverC = new Driver("Driver C", new String[]{"Nguyễn Kiệm, Phú Nhuận, Hồ Chí Minh, Việt Nam",
                "Đ. Lê Văn Sỹ, Tân Bình, Hồ Chí Minh, Việt Nam"});
        List<Driver> driverList = new ArrayList<>();
        driverList.add(driverA);
        driverList.add(driverB);
        driverList.add(driverC);

        String thePoint = "Điện Biên Phủ, Vinhomes Tân Cảng, Phường 22, Bình Thạnh, Hồ Chí Minh, Việt Nam";
        //PROCESS
        Driver choosenDriver = driverA;
        long theDuration = 1000000;
        for (Driver driver : driverList) {
            long tmpDuration = routeDuration(driver, thePoint);
            if (tmpDuration < theDuration) {
                choosenDriver = driver;
                theDuration = tmpDuration;
            }
        }

        //RESULT
        System.out.println("NGƯỜI ĐƯỢC CHỌN LÀ: " + choosenDriver.name);
        System.out.println("THỜI GIAN LÀ: " + theDuration);




/*
        DirectionsApiRequest request = new DirectionsApiRequest(context);
        request.origin("Hồ Bá Kiện, Cư xá Bắc Hải, Phường 15, Quận 10, Hồ Chí Minh, Việt Nam")
                .destination("Hoàng Văn Thụ, Tân Bình, Hồ Chí Minh, Việt Nam")
                .waypoints("Cách Mạng Tháng Tám, Cư xá Bắc Hải, Phường 15, Quận 10, Hồ Chí Minh, Việt Nam",
                        "Phạm Văn Hai, Tân Bình, Hồ Chí Minh, Việt Nam")
                .optimizeWaypoints(true);
        DirectionsRoute route = request.await().routes[0];

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(route));
        System.out.println("---------------------------------------------");
        DirectionsLeg[] legs = route.legs;
        long totalDuration = 0;
        for (DirectionsLeg leg : legs) {
            totalDuration += leg.duration.inSeconds;
        }
        System.out.println("total duration: " + totalDuration/60);

 */
    }

    private long routeDuration(Driver driver, String thePoint) throws InterruptedException, ApiException, IOException {
        long totalDuration = 0;



        DirectionsApiRequest request = new DirectionsApiRequest(context);
        request.origin("Hồ Bá Kiện, Cư xá Bắc Hải, Phường 15, Quận 10, Hồ Chí Minh, Việt Nam")
                .destination("Phạm Văn Đồng, Hồ Chí Minh, Việt Nam")
                .waypoints(driver.points[0], driver.points[1], thePoint)
                .optimizeWaypoints(true);
        DirectionsRoute route = request.await().routes[0];
        DirectionsLeg[] legs = route.legs;
        for (DirectionsLeg leg : legs) {
            totalDuration += leg.duration.inSeconds;
        }

        System.out.println("******************************");
        System.out.println("TAI XE: " + driver.name);
        System.out.println("THOI GIAN MOI: " + totalDuration/60);
        System.out.println("******************************");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(route));
        System.out.println("******************************");

        return totalDuration/60;
    }
}
/*
SOURCE
Git: https://github.com/googlemaps/google-maps-services-java
Vi du: https://github.com/googlemaps/google-maps-services-java/blob/master/src/test/java/com/google/maps/DirectionsApiTest.java
Doc:https://developers.google.com/maps/documentation/directions/intro
    https://googlemaps.github.io/google-maps-services-java/v0.2.1/javadoc/index.html?com/google/maps/model/Distance.html
 */
