package com.example.campus360.ui.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.campus360.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ImageListAdapter extends ArrayAdapter<ListImage> {
    private Context mContext;
    int mResource;

    private int lastPosition = -1;

    static class ViewHolder {
        TextView tvFilename;
        TextView tvLocation;
        TextView tvDate;
        TextView tvTime;
        ImageView imageView;

    }

    public ImageListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ListImage> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String filename = getItem(position).getFilename();
        String location = getItem(position).getLocation();
        String date = getItem(position).getDate();
        String time = getItem(position).getTime();
        String type  = getItem(position).getType();
        String imgURL  = getItem(position).getImgURL();

        final View result;
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.list_image);
            holder.tvDate = (TextView) convertView.findViewById(R.id.dateTextView);
            holder.tvTime = (TextView) convertView.findViewById(R.id.timeTextView);
            holder.tvFilename = (TextView) convertView.findViewById(R.id.filenameTextView);
            holder.tvLocation = (TextView) convertView.findViewById(R.id.locationTextView);

            result = convertView;
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.loading_down_anim : R.anim.loading_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        File f = new File(imgURL, filename + ".jpg");
        Bitmap b = null;
        try { b = BitmapFactory.decodeStream(new FileInputStream(f)); }
        catch (FileNotFoundException e) { e.printStackTrace(); }

        holder.imageView.setImageBitmap(b);

        holder.tvFilename.setText(filename);
        holder.tvLocation.setText("Location: " + location);
        holder.tvDate.setText("Date: " + date);
        holder.tvTime.setText("Time: " + time);
        return convertView;
    }
}
