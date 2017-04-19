package com.example.chie.bulletinboard_0_5_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by chie on 2017/04/01.
 */


public class BoardAdapter extends RealmBaseAdapter<Board> implements ListAdapter {

    private Context context;

    public static class ViewHolder {
        TextView textViewTitle;
        TextView textViewPost;
    }

    public BoardAdapter(Context context, OrderedRealmCollection<Board> boards){
        super(context, boards);
        this.context = context;
    }

    @Override
    public long getItemId(int position){
        return getItem(position).getRealmID();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_view_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textViewTitle = (TextView) convertView.findViewById(R.id.text_view_title);
            viewHolder.textViewPost = (TextView) convertView.findViewById(R.id.text_view_post);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textViewTitle.setText(getItem(position).getRealmTitle());
        viewHolder.textViewPost.setText(getItem(position).getRealmPost());

        return convertView;
    }

}
