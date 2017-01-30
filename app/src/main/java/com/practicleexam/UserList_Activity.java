package com.practicleexam;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.practicleexam.Adapter.UserList_Adapter;
import com.practicleexam.Comman_class.Global_class;
import com.practicleexam.ModelClass.Userdata_class;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class UserList_Activity extends AppCompatActivity {

    public String str_url = "https://api.github.com/repos/square/retrofit/contributors";


    String tag_json_arry = "json_array_req";

    ProgressDialog pDialog;


    RecyclerView recyclerView;
    LinearLayout ll_filterbutton;
    SwipeRefreshLayout swipeRefreshLayout;

    List<Userdata_class> array_personaldata;
    UserList_Adapter adapter;


    Global_class global_class;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);
        global_class=(Global_class)getApplicationContext();

        init();
        clickEvents();

        if(!global_class.checkInternetConnection()){

            Toast.makeText(UserList_Activity.this,getResources().getString(R.string.interneterror),Toast.LENGTH_LONG).show();

        }else{
            getResponceApi();
        }



    }

    public void init() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        ll_filterbutton = (LinearLayout) findViewById(R.id.ll_filterbutton);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
    }

    public void clickEvents() {
        ll_filterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Collections.sort(array_personaldata, new Comparator<Userdata_class>() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public int compare(Userdata_class lhs, Userdata_class rhs) {
                        return Integer.compare(Integer.parseInt(lhs.contributions),Integer.parseInt(rhs.contributions));
                    }
                });

                adapter.notifyDataSetChanged();


            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                if(!global_class.checkInternetConnection()){

                    Toast.makeText(UserList_Activity.this,getResources().getString(R.string.interneterror),Toast.LENGTH_LONG).show();

                }else{
                    getResponceApi();
                }


            }
        });

    }

    public void getResponceApi() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        JsonArrayRequest req = new JsonArrayRequest(str_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("responce", "responce " + response.toString());
                        pDialog.hide();
                        swipeRefreshLayout.setRefreshing(false);
                        try {
                            array_personaldata = new ArrayList<>();

                            JSONArray jsonArray = new JSONArray(response.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Userdata_class personaldata = new Userdata_class();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);


                                personaldata.login = jsonObject.getString("login");
                                personaldata.id = jsonObject.getString("id");
                                personaldata.avatar_url = jsonObject.getString("avatar_url");
                                personaldata.gravatar_id = jsonObject.getString("gravatar_id");
                                personaldata.url = jsonObject.getString("url");


                                personaldata.html_url = jsonObject.getString("html_url");
                                personaldata.followers_url = jsonObject.getString("followers_url");
                                personaldata.following_url = jsonObject.getString("following_url");
                                personaldata.gists_url = jsonObject.getString("gists_url");
                                personaldata.starred_url = jsonObject.getString("starred_url");
                                personaldata.subscriptions_url = jsonObject.getString("subscriptions_url");


                                personaldata.organizations_url = jsonObject.getString("organizations_url");
                                personaldata.repos_url = jsonObject.getString("repos_url");
                                personaldata.events_url = jsonObject.getString("events_url");
                                personaldata.received_events_url = jsonObject.getString("received_events_url");
                                personaldata.type = jsonObject.getString("type");


                                personaldata.site_admin = jsonObject.getString("site_admin");
                                personaldata.contributions = jsonObject.getString("contributions");

                                array_personaldata.add(personaldata);



                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adapter = new UserList_Adapter(UserList_Activity.this, array_personaldata);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(UserList_Activity.this);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(adapter);



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", "Error " + error.getMessage());
                pDialog.hide();
            }
        });

        Global_class.getInstance().addToRequestQueue(req, tag_json_arry);
    }



}

