package com.sanvalero.examenpspmayo.service;

import com.sanvalero.examenpspmayo.domain.Job;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

import java.util.List;

import static com.sanvalero.examenpspmayo.util.Constants.URL;

/**
 * Creado por @ author: Pedro Orós
 * el 14/05/2021
 */

public class JobService {

    private JobApiService api;

    public JobService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        api = retrofit.create(JobApiService.class);
    }

    public Observable<List<Job>> getAllJobs() {
        return api.getAllJobs();
    }

    public Observable<List<Job>> getJobsByLocation(String location) {
        return api.getJobsByLocation(location);
    }

    public Observable<List<Job>> getJobsByDescription(String description) {
        return api.getJobsByDescription(description);
    }

    public Observable<List<Job>> getJobsByType(String type) {
        return api.getJobsByType(type);
    }
}
