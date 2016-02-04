package com.demo.magicrecipe;

import com.demo.magicrecipe.model.Recipes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
	@GET("api/")
	Call<Recipes> listRecipes(@Query("i") String ingredients,@Query("p") String page);
		}