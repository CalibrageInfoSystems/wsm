package in.calibrage.wsm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import in.calibrage.wsm.model.ResMandal;

public class MandalAdapter extends ArrayAdapter<ResMandal> {
    private Context context;
    private List<ResMandal> mDists;


    public MandalAdapter(@NonNull Context context, int resource, List<ResMandal> mDists) {
        super(context, resource);
        this.context = context;
        this.mDists  = mDists;
    }

    @Override
    public int getCount() {
        return mDists.size();
    }

    public List<ResMandal> getmDists() {
        return mDists;
    }

    @Nullable
    @Override
    public ResMandal getItem(int position) {
        return mDists.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(mDists.get(position).getName());
        return label;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(mDists.get(position).getName());
        return label;

    }
}
