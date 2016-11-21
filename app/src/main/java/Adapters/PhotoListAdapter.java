package Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableResource;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import Entities.Photo;
import info.androidhive.cardview.CommentListFragment;
import info.androidhive.cardview.MainActivity;
import info.androidhive.cardview.PhotoFragment;
import info.androidhive.cardview.R;

/**
 * Created by Luigi on 18-Nov-16.
 */

public class PhotoListAdapter extends BaseAdapter {
    // Declare Variables
    Context context;
    Bitmap theBitmap;
    ArrayList<Photo> photos;
    //ImageLoader imageLoader;
    Photo result;

    //new

    public PhotoListAdapter(Context context, ArrayList<Photo> arrayList) {
        this.context = context;
        photos = arrayList;
    }


    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.photo_item, parent, false);

        // Get the position
        result = photos.get(position);

        final ImageView photo;
        photo = (ImageView) itemView.findViewById(R.id.photo);

        if (result.getUrl() != null)
            Glide
                    .with(context)
                    .load(result.getUrl())
                    .centerCrop()
                    //.placeholder(R.drawable.loading_spinner)
                    .crossFade()
                    .into(photo);
        else
            Glide
                    .with(context)
                    .load(result.getPath())
                    .centerCrop()
                    //.placeholder(R.drawable.loading_spinner)
                    .crossFade()
                    .into(photo);


        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                arguments.putString("photoId", photos.get(position).getId());
                arguments.putString("photoUrl", photos.get(position).getUrl());
                arguments.putString("photoPath",photos.get(position).getPath());
                PhotoFragment fragment = PhotoFragment.newInstance(arguments);
                ((MainActivity) context).getSupportFragmentManager().beginTransaction().
                        replace(R.id.main_content, fragment).addToBackStack("tag").commit();
            }
        });

        return itemView;
    }

}
