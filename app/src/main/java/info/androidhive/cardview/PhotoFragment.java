package info.androidhive.cardview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

/**
 * Created by David on 19/11/2016.
 */

public class PhotoFragment extends android.support.v4.app.Fragment {
    ImageView imageView;
    ImageView imageDots;

    public static PhotoFragment newInstance(Bundle arguments) {
        PhotoFragment fragment = new PhotoFragment();
        if (arguments != null) {
            fragment.setArguments(arguments);
        }
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_fragment, container, false);
        imageView = (ImageView) rootView.findViewById(R.id.single_photo_of_an_album);
        imageDots = (ImageView) rootView.findViewById(R.id.dots_fragment);
        imageDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(imageDots);
            }
        });
        if (getArguments().getString("photoUrl") != null)
            Glide.with(getActivity()).load(getArguments().getString("photoUrl")).into(imageView);
        else
            Glide.with(getActivity()).load(getArguments().getString("photoPath")).into(imageView);
        return rootView;
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(getActivity(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(getActivity(), "Favorites", Toast.LENGTH_SHORT).show();
                    Bundle arguments = new Bundle();
                    arguments.putString("photoId", getArguments().getString("photoId"));
                    CommentListFragment fragment = CommentListFragment.newInstance(arguments);
                    getActivity().getSupportFragmentManager().beginTransaction().
                            replace(R.id.main_content, fragment).addToBackStack("tag").commit();
                    return true;
                case R.id.action_play_next:
                    Intent browserIntent;
                    if (getArguments().getString("photoUrl") != null) {
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getArguments().getString("photoUrl")));
                        startActivity(browserIntent);
                    }
                    else{
                        Toast toast = Toast.makeText(getActivity(), "Cannot connect",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    return true;
                default:
            }
            return false;
        }
    }

}
