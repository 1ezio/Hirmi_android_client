package com.hirmiproject.hirmi.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hirmiproject.hirmi.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment3 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment3_inspector_layout, container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://hirmi-393b4-default-rtdb.firebaseio.com/");
        DatabaseReference items = database.getReference("item");
        final ArrayList<String> arrayList = new ArrayList<String>();

        items.addValueEventListener(new ValueEventListener() {
        ArrayAdapter<String> arrayAdapter  ;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String drawing = child.getKey();
                    arrayList.add(drawing);
                    // MyListAdapter adapter= new MyListAdapter(getContext(), drawing, "Approve");
                    ListView lv = (ListView) view.findViewById(R.id.list_id);
                    //arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,arrayList);
                    lv.setAdapter(new MyListAdaper(getActivity(), R.layout.inspector_drawings, arrayList));


                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            inspector_dialog dialog = new inspector_dialog();
                            dialog.showDialog(getActivity(), arrayList.get(i));

                        }
                    });
                }






            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        return view;
    }
    private class MyListAdaper extends ArrayAdapter<String> {
        private int layout;
        private List<String> mObjects;
        private MyListAdaper(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();

                viewHolder.title = (TextView) convertView.findViewById(R.id.drawing_id);
                viewHolder.button = (Button) convertView.findViewById(R.id.ok_id);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            final ViewHolder finalMainViewholder = mainViewholder;
            mainViewholder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalMainViewholder.button.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "Acknowledge Sent", Toast.LENGTH_LONG).show();
                }
            });
            mainViewholder.title.setText(getItem(position));

            return convertView;
        }
    }
    public class ViewHolder {


        TextView title;
        Button button;
    }

}
