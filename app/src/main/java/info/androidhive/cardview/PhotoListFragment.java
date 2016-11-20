package info.androidhive.cardview;


import android.app.Fragment;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
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
                                photos.add(photo);
                            }
                            adapter.notifyDataSetChanged();

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

}
