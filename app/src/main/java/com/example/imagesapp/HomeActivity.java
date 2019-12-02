package com.example.imagesapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.example.imagesapp.Adapters.ImagelistAdapter;
import com.example.imagesapp.Models.ImageModel;
import com.example.imagesapp.Models.ImagetableClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    Context context;
    RecyclerView imageRV;
    private ImagelistAdapter imagelistAdapter;
    private List<ImageModel> imageList;
    private Menu menu;
    AutoCompleteTextView searchFieldAT;
    ImagetableDatabase imgdb;
    boolean loading = true;
    int passingflagcount = 1;
    int passingdbcount = 0;
    int limit = 20;
    ProgressBar progressBar;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager fr;
    int Pages = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        context = HomeActivity.this;

        imgdb = ImagetableDatabase.getInstance(context);
        progressBar = findViewById(R.id.progressbar);
        imageRV = findViewById(R.id.activity_main_recyclerview);
        imageRV.setLayoutManager(new GridLayoutManager(context, 2));
        searchFieldAT = findViewById(R.id.layout_custom_toolbar_searchtext);


        fr = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);

//        imageRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//
//                if (loading) {
//
//
//                    if (dy > 0) //check for scroll down
//                    {
//
//                        visibleItemCount = imageRV.getChildCount();
//                        totalItemCount = fr.getItemCount();
//                        pastVisiblesItems = fr.findFirstVisibleItemPosition();
//
//
//                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
//                            System.out.println("@@@@ Reacheddd visibleItemCount"+visibleItemCount);
//                            System.out.println("@@@@ Reacheddd pastVisiblesItems"+pastVisiblesItems);
//                            System.out.println("@@@@ Reacheddd totalItemCount"+totalItemCount);
//
//                            loading = false;
//                            Log.v("Reacheddd", " Reached Last Item");
//                            int passvaluecheck = passingflagcount + 1;
//
//                            System.out.println("Reacheddd"+counter);
//                            System.out.println("Reacheddd"+passvaluecheck);
//                            if (counter > passvaluecheck) {
//
//                                passingflagcount = passingflagcount + 1;
//
//
//                                if (CheckInternetNetwork.isInternetAvailable(context)) //returns true if internet available
//                                {
//
//                                    progressBar.setVisibility(View.VISIBLE);
//                                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//
//
//                                    getImagesdata();
//
//
//                                } else {
//
//                                    progressBar.setVisibility(View.INVISIBLE);
//                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        });
        if (CheckInternetNetwork.isInternetAvailable(context)) {
            System.out.println("hello i am getting server data");
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            getImagesdata();

        } else {
            System.out.println("hello i am getting room data");

            if (imgdb.imagetableDao().getimagesList().size() >0) {
                getImagesdatafromRoom();
            } else {
                Toast.makeText(getApplicationContext(), "No recent history found.", Toast.LENGTH_LONG).show();
            }


        }


        searchFieldAT.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {


                    filter(s.toString());


                //you can use runnable postDelayed like 500 ms to delay search text
            }

            void filter(String text) {
                List<ImageModel> temp = new ArrayList();

                if ((imageList != null) && !imageList.isEmpty())

                {
                    for (ImageModel d : imageList) {
                        //or use .equal(text) with you want equal match
                        //use .toLowerCase() for better matches
                        if (d.getImgtitle().contains(text)) {
                            temp.add(d);
                        }
                    }
                    //update recyclerview

                    if (temp.size() == 0) {

                    } else {
                        imagelistAdapter.updateList(temp);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "No recent history found to search.", Toast.LENGTH_LONG).show();
                }

            }

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.two) {
            imageRV.setLayoutManager(new GridLayoutManager(context, 2));
        } else if (item.getItemId() == R.id.three) {
            imageRV.setLayoutManager(new GridLayoutManager(context, 3));
        } else if (item.getItemId() == R.id.four) {
            imageRV.setLayoutManager(new GridLayoutManager(context, 4));
        }
        return true;
    }

    private void getImagesdatafromRoom() {

        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


        System.out.println("limit is " + limit + "passingdbcount  " + passingdbcount + "    hello i am getting local db data :" + imgdb.imagetableDao().getimagespageList(limit, passingdbcount).size());


        if (passingdbcount == 0) {
            imageList = new ArrayList<>();
        }
//
        for (int i = 0; i < imgdb.imagetableDao().getimagespageList(limit, passingdbcount).size(); i++) {

            final int pageid = imgdb.imagetableDao().getimagespageList(limit, passingdbcount).get(i).getPageid();
            final String imgtitle = imgdb.imagetableDao().getimagespageList(limit, passingdbcount).get(i).getTitle();
            String imagepath = String.valueOf(imgdb.imagetableDao().getimagespageList(limit, passingdbcount).get(i).getImage());

            System.out.println("all images room pageid" + pageid);
            System.out.println("all images room imgtitle" + imgtitle);
            System.out.println("all images room imagepath" + imagepath);

            ImageModel il = new ImageModel(pageid, imagepath, imgtitle);
            imageList.add(il);

        }

        if (passingdbcount == 0) {
            imagelistAdapter = new ImagelistAdapter(context, imageList);
            imageRV.setAdapter(imagelistAdapter);
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            imagelistAdapter.setOnBottomReachedListener(new OnBottomReachedListener() {
                @Override
                public void onBottomReached(int position) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.VISIBLE);
                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            System.out.println("position is if" + position);

                            passingdbcount = passingdbcount + limit;

                            getImagesdatafromRoom();
                        }
                    });


                }
            });

            imageRV.setItemAnimator(new DefaultItemAnimator());

        } else {
            System.out.println("imageList e l s e : " + imageList.size());

            imagelistAdapter.ImagelistAdapter1(imageList);
            Handler handler = new Handler();

            final Runnable r = new Runnable() {
                public void run() {

                    imagelistAdapter.notifyDataSetChanged();
                }
            };

            handler.post(r);

            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            imagelistAdapter.setOnBottomReachedListener(new OnBottomReachedListener() {
                @Override
                public void onBottomReached(int position) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("position is else " + position);
                            System.out.println(imgdb.imagetableDao().getimagesList().size() + "====position is else=====" + imageList.size());
                            if (imgdb.imagetableDao().getimagesList().size() == imageList.size()) {

                            } else {
                                progressBar.setVisibility(View.VISIBLE);
                                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                passingdbcount = passingdbcount + limit;
                                getImagesdatafromRoom();
                            }


                        }
                    });
                }
            });
        }


    }

    private void getImagesdata() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("", "");


        CustomRequest jsObjRequest = new CustomRequest(Request.Method.GET, Urlinfo.BASE_URL + passingflagcount, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    if (passingflagcount == 1) {
                        imageList = new ArrayList<>();
                    }
                    int page = Integer.parseInt(response.getString("page"));
                    String total_results = response.getString("total_results");
                    String total_pages = response.getString("total_pages");


                    JSONArray dataarray = response.getJSONArray("results");
                    if (dataarray.length() > 0) {
                        for (int i = 0; i < dataarray.length(); i++) {
                            JSONObject dataobject = dataarray.getJSONObject(i);

                            String posterimg = dataobject.getString("poster_path");
                            String poster_path = Urlinfo.BASE_POSTER_URL + posterimg;
                            String title = dataobject.getString("title");

                            ImageModel il = new ImageModel(page, poster_path, title);
                            imageList.add(il);


                            AsyncTask.execute(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void run() {

                                    int abc = imgdb.imagetableDao().getpageidPresent(page).size();

                                    if (abc == 20) {
                                        runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                            }
                                        });
                                    }else {
                                        URL url = null;
                                        try {
                                            url = new URL(poster_path);
                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }
                                        Bitmap bmp = null;
                                        try {
                                            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                        byte[] byteArray = stream.toByteArray();
                                        String imgString = Base64.encodeToString(getBytesFromBitmap(bmp),
                                                Base64.NO_WRAP);

                                        ImagetableClass imgtable = new ImagetableClass(page, title, imgString);
                                        imgdb.imagetableDao().insertImagesData(imgtable);

                                        runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                            }
                                        });

                                    }
                                }
                            });
                        }


                        if (passingflagcount == 1) {
                            imagelistAdapter = new ImagelistAdapter(context, imageList);
                            imageRV.setAdapter(imagelistAdapter);
                            imagelistAdapter.setOnBottomReachedListener(new OnBottomReachedListener() {
                                @Override
                                public void onBottomReached(int position) {
                                    passingflagcount = passingflagcount + 1;
                                    progressBar.setVisibility(View.VISIBLE);
                                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    getImagesdata();
                                }
                            });

                            imageRV.setItemAnimator(new DefaultItemAnimator());

                        } else {
                            imagelistAdapter.ImagelistAdapter1(imageList);
                            Handler handler = new Handler();

                            final Runnable r = new Runnable() {
                                public void run() {

                                    imagelistAdapter.notifyDataSetChanged();
                                }
                            };

                            handler.post(r);


                            imagelistAdapter.setOnBottomReachedListener(new OnBottomReachedListener() {
                                @Override
                                public void onBottomReached(int position) {
                                    passingflagcount = passingflagcount + 1;

                                    if (Pages >= passingflagcount) {
                                        progressBar.setVisibility(View.VISIBLE);
                                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        getImagesdata();
                                    }
                                }
                            });
                        }

                        loading = true;

                    } else {

                    }


                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error is ", "Error: " + error.getMessage());
            }
        });

        jsObjRequest.setRetryPolicy(new

                DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().

                addToRequestQueue(jsObjRequest);

    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }
}
