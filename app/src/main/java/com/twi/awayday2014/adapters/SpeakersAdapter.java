package com.twi.awayday2014.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Presenter;
import com.twi.awayday2014.utils.Fonts;

import java.util.Collections;
import java.util.List;

import static com.twi.awayday2014.models.Presenter.PresenterComparator.*;

public class SpeakersAdapter extends BaseAdapter{

    private Context context;
    private List<Presenter> presenters;
    private LayoutInflater inflater;

    public SpeakersAdapter(Context context, List<Presenter> speakers) {
        this.context = context;
        this.presenters = speakers;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return presenters.size();
    }

    @Override
    public Object getItem(int i) {
        return presenters.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewSource viewSource;
        if (view == null) {
            view = inflater.inflate(R.layout.view_speaker_listitem, viewGroup, false);
            viewSource = new ViewSource();
            viewSource.speakerName = (TextView) view.findViewById(R.id.speakerName);
            viewSource.speakerName.setTypeface(Fonts.openSansRegular(context));
            viewSource.speakerImage = (ImageView) view.findViewById(R.id.profile_image);
            viewSource.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.placeholder);
            view.setTag(viewSource);
        }

        Presenter presenter = presenters.get(i);
        viewSource = (ViewSource) view.getTag();
        viewSource.speakerName.setText(presenter.getName());
        viewSource.speakerImage.setImageBitmap(viewSource.bitmap);
        if (presenter.getImageUrl() != null) {
            Picasso.with(context)
                    .load(presenter.getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(viewSource.speakerImage);
            viewSource.speakerImage.setTag(presenter.getImageUrl());
        }
        return view;
    }

    public void onDataChange(List<Presenter> presenters) {
        this.presenters = presenters;
        Collections.sort(this.presenters, getComparator(SORT_BY_PRIORITY_ORDER, NAME_SORT));
        notifyDataSetChanged();
    }

    private class ViewSource {
        TextView speakerName;
        ImageView speakerImage;
        Bitmap bitmap;
    }

}
