package info.androidhive.cardview;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

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
import java.io.FileOutputStream;
import java.util.ArrayList;

import Adapters.PhotoListAdapter;
import Entities.Album;
import Entities.Photo;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoListFragment extends android.support.v4.app.Fragment {

    GridView gridView;
    ArrayList<Photo> photos;
    PhotoListAdapter adapter;
    String url;
    Bitmap theBitmap;
    ProgressDialog mProgressDialog;

    public static PhotoListFragment newInstance(Bundle arguments) {
        PhotoListFragment fragment = new PhotoListFragment();
        if (arguments != null) {
            fragment.setArguments(arguments);
        }
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_photo_list, container, false);

        photos = new ArrayList<>();
        /*urlList.add("https://farm6.staticflickr.com/5578/30902247605_1e353319d0.jpg");
        urlList.add("https://farm6.staticflickr.com/5649/30814273391_0f845b302d.jpg");*/
        gridView = (GridView) rootView.findViewById(R.id.grid_view);
        adapter = new PhotoListAdapter(rootView.getContext(), photos);
        gridView.setAdapter(adapter);
        createDirectoy();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(getArguments().getString("albumId") != null)
            JSONSetPhotos();
        else
            internalSetPhotos();
    }

    private void internalSetPhotos() {
        File file = Environment.getExternalStoragePublicDirectory("/Photolix/"+getArguments().getString("albumTitle"));
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            Photo photo = new Photo();
            photo.setPath(files[i].getAbsolutePath());
            photos.add(photo);
        }
        adapter.notifyDataSetChanged();
    }

    private void JSONSetPhotos() {
        url =   "https://api.flickr.com/services/rest/?method=flickr.photosets.getPhotos&api_key="+
                MainActivity.API_KEY+"&photoset_id="+this.getArguments().getString("albumId")+
                "&user_id="+MainActivity.USER_ID+"&format=json&nojsoncallback=1";

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET,
                        url,
                        null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject jsonObject;
                        JSONArray jsonArray;
                        try {
                            jsonObject = response.getJSONObject("photoset");
                            jsonArray = jsonObject.getJSONArray("photo");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                Photo photo = new Photo();
                                photo.setId(jsonArray.getJSONObject(i).getString("id"));
                                photo.setSecret(jsonArray.getJSONObject(i).getString("secret"));
                                photo.setServer(jsonArray.getJSONObject(i).getString("server"));
                                photo.setFarm(jsonArray.getJSONObject(i).getInt("farm"));
                                photo.setUrl();
                                photo.setAlbumTitle(getArguments().getString("albumTitle"));
                                photos.add(photo);
                            }
                            adapter.notifyDataSetChanged();
                            new ChargeInternalStorage().execute();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        int a = 0;
                    }
                }
                );

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(jsonObjectRequest);
    }

    private void createDirectoy(){

        File file = new File(Environment.getExternalStorageDirectory(), "/Photolix/"+getArguments().getString("albumTitle"));
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating Image folder");

            }
        }
    }
    // DownloadJSON AsyncTask
    private class ChargeInternalStorage extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            for (int i = 0; i < photos.size(); i++) {
                try {
                    theBitmap = Glide.
                            with(getActivity()).
                            load(photos.get(i).getUrl()).
                            asBitmap().
                            into(100, 100). // Width and height
                            get();
                    String t = photos.get(i).getUrl();
                    saveImage(theBitmap, photos.get(i).getId()+".jpeg", photos.get(i).getAlbumTitle());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

        }
    }
    private void saveImage(Bitmap imageToSave, String fileName, String albumTitle){
        File file = new File(Environment.getExternalStoragePublicDirectory("/Photolix/"+albumTitle), fileName);
        if (!file.exists()) {
            try {

                FileOutputStream out = new FileOutputStream(file);
                imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
