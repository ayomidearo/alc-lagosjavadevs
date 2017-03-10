package me.ayomidearo.lagosjavadevs.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.ayomidearo.lagosjavadevs.MainApplication;
import me.ayomidearo.lagosjavadevs.R;
import me.ayomidearo.lagosjavadevs.adapter.DevListAdapter;
import me.ayomidearo.lagosjavadevs.constant.Constant;
import me.ayomidearo.lagosjavadevs.constant.NetworkManager;
import me.ayomidearo.lagosjavadevs.model.Devs;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView mListView;
    DevListAdapter mDevListAdapter;
    ArrayList<Devs> devs = null;

    String username;
    String profileImage;
    String profileUrl;

    int currentFirstVisibleItem = 0;
    int currentVisibleItemCount = 0;
    int totalItemCount = 0;
    int currentScrollState = 0;
    boolean loadingMore = false;
    Long startIndex = 0L;
    Long offset = 10L;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mListView = (ListView) findViewById(R.id.dev_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDevList();
            }
        });



        getJavaDevs();

    }


    private void refreshDevList() {
        getJavaDevs();
    }


    void getJavaDevs(){
        if(NetworkManager.isNetworkAvailable(getApplicationContext())){
            showProgress();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constant.SEARCH_URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(mSwipeRefreshLayout.isRefreshing()){
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    Log.d(TAG, "Response from github"+String.valueOf(response));
                    if (response != null) {
                        parseJsonFeed(response);
                    }else showEmpty();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mSwipeRefreshLayout.isRefreshing()){
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    showError();

                }
            });

            MainApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        }else {
            showOffline();
            if(mSwipeRefreshLayout.isRefreshing()){
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }

    }

    private void parseJsonFeed(JSONObject response) {
        try {
            Boolean incompleteResult = response.getBoolean("incomplete_results");
            if(incompleteResult) showEmpty();
            else {
                showContent();
                devs = new ArrayList<>();

                JSONArray devArray = response.getJSONArray("items");
                int totalCount = response.getInt("total_count");

                Log.d(TAG, String.valueOf(devArray.length()));

                for(int i = 0;i<devArray.length();i++){
                    JSONObject userObject = (JSONObject) devArray.get(i);
                    username = userObject.getString("login");
                    profileImage = userObject.getString("avatar_url");
                    profileUrl = userObject.getString("html_url");
                    devs.add(new Devs(username,profileImage,profileUrl));
                }
                mDevListAdapter = new DevListAdapter(this, devs);
                mListView.setAdapter(mDevListAdapter);
                mDevListAdapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showContent() {
        // show content container
        ViewGroup containerContent = (ViewGroup) findViewById(R.id.swiperefresh);
        ViewGroup listcontainerContent = (ViewGroup) findViewById(R.id.dev_list);
        ViewGroup containerProgress = (ViewGroup) findViewById(R.id.container_progress);
        ViewGroup containerOffline = (ViewGroup) findViewById(R.id.container_offline);
        ViewGroup containerEmpty = (ViewGroup) findViewById(R.id.container_empty);
        containerContent.setVisibility(View.VISIBLE);
        listcontainerContent.setVisibility(View.VISIBLE);
        containerProgress.setVisibility(View.GONE);
        containerOffline.setVisibility(View.GONE);
        containerEmpty.setVisibility(View.GONE);

    }


    private void showProgress() {
        // show progress container
        ViewGroup containerContent = (ViewGroup) findViewById(R.id.swiperefresh);
        ViewGroup listcontainerContent = (ViewGroup) findViewById(R.id.dev_list);
        ViewGroup containerProgress = (ViewGroup) findViewById(R.id.container_progress);
        ViewGroup containerOffline = (ViewGroup) findViewById(R.id.container_offline);
        ViewGroup containerEmpty = (ViewGroup) findViewById(R.id.container_empty);
        containerContent.setVisibility(View.GONE);
        listcontainerContent.setVisibility(View.GONE);
        containerProgress.setVisibility(View.VISIBLE);
        containerOffline.setVisibility(View.GONE);
        containerEmpty.setVisibility(View.GONE);

    }


    private void showOffline() {
        // show offline container
        ViewGroup containerContent = (ViewGroup) findViewById(R.id.swiperefresh);
        ViewGroup listcontainerContent = (ViewGroup) findViewById(R.id.dev_list);
        ViewGroup containerProgress = (ViewGroup) findViewById(R.id.container_progress);
        ViewGroup containerOffline = (ViewGroup) findViewById(R.id.container_offline);
        ViewGroup containerEmpty = (ViewGroup) findViewById(R.id.container_empty);
        containerContent.setVisibility(View.GONE);
        listcontainerContent.setVisibility(View.GONE);
        containerProgress.setVisibility(View.GONE);
        containerOffline.setVisibility(View.VISIBLE);
        containerEmpty.setVisibility(View.GONE);


    }


    private void showEmpty() {
        // show empty container
        ViewGroup containerContent = (ViewGroup) findViewById(R.id.swiperefresh);
        ViewGroup listcontainerContent = (ViewGroup) findViewById(R.id.dev_list);
        ViewGroup containerProgress = (ViewGroup) findViewById(R.id.container_progress);
        ViewGroup containerOffline = (ViewGroup) findViewById(R.id.container_offline);
        ViewGroup containerEmpty = (ViewGroup) findViewById(R.id.container_empty);
        containerContent.setVisibility(View.GONE);
        listcontainerContent.setVisibility(View.GONE);
        containerProgress.setVisibility(View.GONE);
        containerOffline.setVisibility(View.GONE);
        containerEmpty.setVisibility(View.VISIBLE);

    }
    private void showError() {
        // show empty container
        ViewGroup containerContent = (ViewGroup) findViewById(R.id.swiperefresh);
        ViewGroup listcontainerContent = (ViewGroup) findViewById(R.id.dev_list);
        ViewGroup containerProgress = (ViewGroup) findViewById(R.id.container_progress);
        ViewGroup containerOffline = (ViewGroup) findViewById(R.id.container_offline);
        ViewGroup containerEmpty = (ViewGroup) findViewById(R.id.container_empty);
        containerContent.setVisibility(View.GONE);
        listcontainerContent.setVisibility(View.GONE);
        containerProgress.setVisibility(View.GONE);
        containerOffline.setVisibility(View.GONE);
        containerEmpty.setVisibility(View.VISIBLE);

    }



}
