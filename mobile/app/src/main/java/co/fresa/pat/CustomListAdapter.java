package co.fresa.pat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import co.fresa.pat.R;
import co.fresa.pat.mundo.MisionesClanLogic;
import co.fresa.pat.mundo.Mission;

public class CustomListAdapter  extends BaseAdapter {

    private List<Mission> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    private HashMap<String, Drawable> criteriaToIcon;

    Drawable customIcon;
    Drawable darkIcon;
    Drawable shakeIcon;
    Drawable micIcon;

    public CustomListAdapter(Context aContext,  List<Mission> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);

        criteriaToIcon = new HashMap<String, Drawable>();

        criteriaToIcon.put("CUSTOM",context.getResources().getDrawable(R.mipmap.custom_icon));
        criteriaToIcon.put("DARK",context.getResources().getDrawable(R.mipmap.dark_icon));
        criteriaToIcon.put("SHAKE",context.getResources().getDrawable(R.mipmap.shake_icon));
        criteriaToIcon.put("SCREAM",context.getResources().getDrawable(R.mipmap.mic_icon));
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_layout, null);
            holder = new ViewHolder();
            holder.flagView = (ImageView) convertView.findViewById(R.id.imageView_flag);
            holder.countryNameView = (TextView) convertView.findViewById(R.id.textView_countryName);
            holder.populationView = (TextView) convertView.findViewById(R.id.textView_population);
            holder.communityView = (ImageView) convertView.findViewById(R.id.imageView_community);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Mission mission = this.listData.get(position);
        holder.countryNameView.setText(mission.getName());
        holder.populationView.setText(""+mission.getReward());
        holder.flagView.setImageDrawable(criteriaToIcon.get(mission.getCriteria()));

        if (MisionesClanLogic.getInstance().hasMissionId(mission.getId())) {
            holder.communityView.setVisibility(View.VISIBLE);
        }
        else holder.communityView.setVisibility(View.INVISIBLE);

        return convertView;
    }

    static class ViewHolder {
        ImageView flagView;
        ImageView communityView;
        TextView countryNameView;
        TextView populationView;
    }

}