package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import Entities.Comment;
import info.androidhive.cardview.R;


/**
 * Created by Luigi on 19-Nov-16.
 */

public class CommentListAdapter extends BaseAdapter {
    // Declare Variables
    Context context;

    ArrayList<Comment> comments;
    //ImageLoader imageLoader;
    Comment result;

    //new

    public CommentListAdapter(Context context, ArrayList<Comment> arrayList) {
        this.context = context;
        comments = arrayList;
    }


    @Override
    public int getCount() {
        return comments.size();
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

        TextView author;
        TextView content;

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.comment_item, parent, false);

        // Get the position
        result = comments.get(position);

        author = (TextView) itemView.findViewById(R.id.author);
        author.setText(result.getRealName()+" ("+result.getAuthor()+")");

        content = (TextView) itemView.findViewById(R.id.content);
        content.setText(result.getContent());

        return itemView;
    }
}
