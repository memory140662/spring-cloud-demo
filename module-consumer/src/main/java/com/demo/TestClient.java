package com.demo;

import com.demo.entities.Module;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import java.io.IOException;
import java.util.List;

public class TestClient implements Runnable {

    private int count = 0;

    public TestClient(int count) {
        this.count = count;
    }

    private static final Integer LIMIT = 200;
    private static final String BASE_URL = "http://localhost:8800";
    private static final String RESULT_URL = "http://10.200.20.158:8866/zipkin/api/v2/traces?lookback=10800000&serviceName=module-consumer&spanName=get%20%2F&limit" + LIMIT;

    public static void main(String[] args) {
        System.out.println("Start!");
        for (int i = 0; i < LIMIT; i++) {
            try {
                new Thread(new TestClient(i)).start();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Finished!");
    }

    @Override
    public void run() {
        try {
            Service service = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(Service.class);
            Response<List<List<Module>>> response = service.getAll().execute();
            System.out.print("Count: " + count);
            if (response.body().size() == 0) {
                System.out.println(", No data.");
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static interface Service {
        @GET("/")
        Call<List<List<Module>>> getAll();
    }

}
