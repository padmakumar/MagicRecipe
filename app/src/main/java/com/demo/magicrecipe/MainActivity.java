package com.demo.magicrecipe;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.demo.magicrecipe.adapter.RecipeAdapter;
import com.demo.magicrecipe.model.Recip;
import com.demo.magicrecipe.model.Recipes;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends ActionBarActivity implements Callback<Recipes>{

    private ListView mListView;
    private String mRecipe;
    private int mRecipePage=1;
    private ArrayAdapter adapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListView=(ListView)findViewById(R.id.list_view);
        adapter = (ArrayAdapter) mListView.getAdapter();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                                             @Override
                                             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                 Recip item = (Recip) adapterView.getItemAtPosition(i);
                                                 Intent intent = new Intent(MainActivity.this,RecipeDetailsView.class);
                                                 intent.putExtra(Constant.RECIPE_URL, item.href);
                                                 startActivity(intent);

                                             }

        });

        mListView.setOnScrollListener(new  EndlessScrollListener() {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // TODO Auto-generated method stub
                 mRecipePage=page;
                System.out.println(mRecipePage+"~~page~~~total items~"+totalItemsCount);
                callRecipe(mRecipe);

            }});

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String arg0) {
                adapter=null;
                mRecipe=arg0;
              //  mRecipePage=1;
                callRecipe(mRecipe);
                searchView.clearFocus(); // close the keyboard
                return true;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                return false;
            }
        });
        return true;
    }



    private void callRecipe(String ingredients){

        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient mHttpClient = new OkHttpClient.Builder().addInterceptor(logger).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL)
//	    .addConverterFactory(new ToStringConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(mHttpClient)
                .build();

        ApiService service = retrofit.create(ApiService.class);
        Call<Recipes> repos = service.listRecipes(ingredients,mRecipePage+"");
        repos.enqueue(this);

    }


    @Override
    public void onResponse(Response<Recipes> response) {
        System.out.println("~~onresp~~~~"+response.code());

        if(response.code()==200) {
            if (response.body().results == null) return;
            if (response.body().results.size() == 0) {
                Toast.makeText(MainActivity.this, "No Recipe Found", Toast.LENGTH_LONG).show();

            } else {

                if (adapter == null) {
                    adapter = new RecipeAdapter(MainActivity.this, (ArrayList<Recip>) response.body().results);
                    mListView.setAdapter(adapter);
                } else {
                    adapter.addAll((ArrayList<Recip>) response.body().results);
                    adapter.notifyDataSetChanged();
                }
                //mListView.setAdapter(new RecipeAdapter(MainActivity.this, (ArrayList<Recip>) response.body().results));
            }

            }else if(response.code()==500){
            //Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_LONG).show();
        }
}

    @Override
    public void onFailure(Throwable t) {
        System.out.println("~~~~err~~"+t.toString());
    }
}

