package hu.bme.aut.suchtowers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MapListAdapter extends ArrayAdapter<GameData> {

    private List<GameData> items;

    public MapListAdapter(Context context, int textViewResourceId, List<GameData> items) {
        super(context, textViewResourceId, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.row, null);
        }
        final GameData o = items.get(position);
        if (o != null) {
            TextView tt = (TextView) v.findViewById(R.id.toptext);
            TextView bt = (TextView) v.findViewById(R.id.bottomtext);
            if (tt != null) {
                tt.setText("Name: " + o.name);
            }
            if(bt != null){
                bt.setText("Id: " + o.map);
            }

            final Context cont = getContext().getApplicationContext();
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(cont, GameActivity.class);
                    it.putExtra("MAP_ID", o.map);
                    it.putExtra("MISSION_ID", o.mission);
                    getContext().startActivity(it);
                    Log.d("map started", "sad");
                }
            });

        }

        return v;
    }
}