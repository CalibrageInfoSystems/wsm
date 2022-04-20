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
import in.calibrage.wsm.model.ResDivision;

public class DivisionAdapter extends ArrayAdapter<ResDivision> {
    private Context context;
    private List<ResDivision> mDists;


    public DivisionAdapter(@NonNull Context context, int resource, List<ResDivision> mDists) {
        super(context, resource);
        this.context = context;
        this.mDists  = mDists;
    }

    @Override
    public int getCount() {
        return mDists.size();
    }


    public List<ResDivision> getmDists() {
        return mDists;
    }

    @Nullable
    @Override
    public ResDivision getItem(int position) {
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
