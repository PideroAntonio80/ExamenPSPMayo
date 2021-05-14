package com.sanvalero.examenpspmayo.service;

import com.sanvalero.examenpspmayo.domain.Job;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

import java.util.List;

/**
 * Creado por @ author: Pedro Orós
 * el 14/05/2021
 */

public interface JobApiService {
    @GET("positions.json")
    Observable<List<Job>> getAllJobs();

    @GET("positions.json")
    Observable<List<Job>> getJobsByLocation(@Query("location") String location);

    @GET("positions.json")
    Observable<List<Job>> getJobsByDescription(@Query("description") String description);

    @GET("positions.json")
    Observable<List<Job>> getJobsByType(@Query("type") String type);
}
