package com.example.vivek.newzapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewzAdapter extends ArrayAdapter<Newz> {

    public NewzAdapter(@NonNull Context context, ArrayList<Newz> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_item, parent, false);
        }

        Newz currentNews = getItem(position);

        // FInd and display extra information
        TextView textView = listItemView.findViewById(R.id.trailText);
        textView.setText(currentNews.getmTrailText());

        // Find and display author
        String newsAuthor = currentNews.getmAuthor() + " ";
        TextView authorView = listItemView.findViewById(R.id.authorName);
        authorView.setText(newsAuthor);

        // Find and display title of news
        TextView title = listItemView.findViewById(R.id.webTitle);
        title.setText(currentNews.getmTopic());

        // Find and display the article's Date
        TextView dateView = listItemView.findViewById(R.id.date);
        // Format the date string (i.e. "Mar 3, 1984")
        String formattedDate = formatDate(currentNews.getmDate());
        // Display the date of the current earthquake in that TextView
        dateView.setText(formattedDate);

        // Find and display category of news
        TextView genre = listItemView.findViewById(R.id.category);
        genre.setText(currentNews.getmCategory());

        return listItemView;
    }

    private String formatDate(String date) {
        final SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());

        Date date_out = null;
        try {
            date_out = inputParser.parse(date);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        final SimpleDateFormat outputFormatter = new SimpleDateFormat("MMM dd ''yy", Locale.US);
        return outputFormatter.format(date_out);
    }
}
