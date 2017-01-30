package com.practicleexam.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.practicleexam.Comman_class.RoundedTransformation;
import com.practicleexam.ModelClass.Userdata_class;
import com.practicleexam.R;

import java.util.List;

/**
 * Created by zwtiphone on 05/09/16.
 */
public class UserList_Adapter extends RecyclerView.Adapter<UserList_Adapter.ViewHolder> {

    private List<Userdata_class> array_personaldata;
    Activity activity;


    public UserList_Adapter(Activity activity, List<Userdata_class> array_personaldata) {
        this.array_personaldata = array_personaldata;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_userlist, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {


        Userdata_class personaldata = array_personaldata.get(position);


        Glide.with(activity).load(personaldata.avatar_url)
                .crossFade().bitmapTransform(new RoundedTransformation(activity))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.img_user);
        viewHolder.txt_username.setText(personaldata.login);
        viewHolder.txt_repourl.setText(personaldata.repos_url);
        viewHolder.txt_contribution.setText(activity.getResources().getString(R.string.contributions)+" :"+ personaldata.contributions);

    }

    @Override
    public int getItemCount() {
        return array_personaldata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_user;
        TextView txt_username,txt_repourl,txt_contribution;

        public ViewHolder(View convertView) {
            super(convertView);
            img_user = (ImageView) convertView.findViewById(R.id.img_user);
            txt_username=(TextView)convertView.findViewById(R.id.txt_username);
            txt_repourl=(TextView)convertView.findViewById(R.id.txt_repourl);
            txt_contribution=(TextView)convertView.findViewById(R.id.txt_contribution);

        }

    }


}
