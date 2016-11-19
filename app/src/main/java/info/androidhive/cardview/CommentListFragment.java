package info.androidhive.cardview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapters.CommentListAdapter;
import Entities.Comment;

/**
 * Created by Luigi on 19-Nov-16.
 */

public class CommentListFragment extends Fragment {

    ListView listView;
    ArrayList<Comment> comments;
    CommentListAdapter adapter;
    String url;

    public static CommentListFragment newInstance(Bundle arguments) {
        CommentListFragment fragment = new CommentListFragment();
        if (arguments != null) {
            fragment.setArguments(arguments);
        }
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_comment_list, container, false);

        comments = new ArrayList<>();

        listView = (ListView) rootView.findViewById(R.id.list_view);

        adapter = new CommentListAdapter(rootView.getContext(), comments);
        listView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        url =   "https://api.flickr.com/services/rest/?method=flickr.photos.comments.getList&api_key=f7d55bffd8b0ca3e7df8269a986d2d1d&photo_id="+this.getArguments().getString("photoId")+"+&format=json&nojsoncallback=1";

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET,
                        url,
                        null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject jsonObject;
                        JSONArray jsonArray;
                        try {
                            jsonObject = response.getJSONObject("comments");
                            jsonArray = jsonObject.getJSONArray("comment");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                Comment comment = new Comment();
                                comment.setAuthor(jsonArray.getJSONObject(i).getString("authorname"));
                                comment.setRealName(jsonArray.getJSONObject(i).getString("realname"));
                                comment.setContent(jsonArray.getJSONObject(i).getString("_content"));

                                comments.add(comment);
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
