package info.androidhive.cardview;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import Adapters.AlbumsAdapter;
import Entities.Album;

public class MainActivity extends AppCompatActivity {

    static String USER_ID = "145733563%40N08";
    static String API_KEY = "1330cbb40909a4ae993cc5d9ca62ac4b";
    static String ID = "id";
    static String PRIMARY = "primary";
    static String SECRET = "secret";
    static String SERVER = "server";
    static String FARM = "farm";
    static String TITLE = "title";
    private String urlPhotosets;
    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    ArrayList<Album> arrayList;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ImageView optionSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDirectoy();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initCollapsingToolbar();

        optionSettings = (ImageView) findViewById(R.id.options_settings);
        optionSettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showPopupMenu(optionSettings);
            }
        });

        if(isNetDisponible()){chargeView();}
        else{Toast.makeText(this,"SIN CONEXION", Toast.LENGTH_SHORT).show();}
        try {
            Glide.with(MainActivity.this).load(R.drawable.cover).into((ImageView)findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        JSONSetAlbum();
    }

    private void JSONSetAlbum() {

        urlPhotosets = "https://api.flickr.com/services/rest/?method=flickr.photosets.getList&api_key="+API_KEY+"&user_id="+ USER_ID +"&format=json&nojsoncallback=1";
        //-------------
        // Retrieve JSON Objects from the given URL address
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("succes", "Success Response: " + response.toString());
                try {
                    jsonObject = response.getJSONObject("photosets");
                    jsonArray = jsonObject.getJSONArray("photoset");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        String url;
                        jsonObject = jsonArray.getJSONObject(i);
                        Album album = new Album();
                        // Retrive JSON Objects
                        String primary = jsonObject.getString(PRIMARY);
                        String secret = jsonObject.getString(SECRET);
                        String server = jsonObject.getString(SERVER);
                        String farm = jsonObject.getString(FARM);
                        String id = jsonObject.getString(ID);
                        //build image url
                        url = "https://farm" + farm + ".staticflickr.com/" + server +
                                "/" + primary + "_" + secret + ".jpg";
                        JSONObject title = jsonObject.getJSONObject(TITLE);
                        album.setTitle(title.getString("_content"));
                        album.setUrl(url);
                        album.setId(id);
                        album.setPrimary(primary);
                        // Set the JSON Objects into the array
                        arrayList.add(album);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    Log.d("Error", "Error Response code: " + error.networkResponse.statusCode);

                }
            }
        };

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                urlPhotosets, null,
                listener,
                errorListener);
        requestQueue.add(request);
        //-------------
    }
    private boolean isNetDisponible() {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }

    public void chargeView(){
        arrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new AlbumsAdapter(MainActivity.this, arrayList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
    private void createDirectoy(){

        File file = new File(Environment.getExternalStorageDirectory(), "/Photolix");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating Image folder");

            }
        }
       }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_settings:
                    Toast toast = Toast.makeText(MainActivity.this, "FUNCIONA", Toast.LENGTH_LONG);
                    toast.show();
                    return true;
                case R.id.action_play_next:

                    return true;
                default:
            }
            return false;
        }
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}


