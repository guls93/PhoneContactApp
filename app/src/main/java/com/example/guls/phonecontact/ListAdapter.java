package com.example.guls.phonecontact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;



/**
 * Created by GÜLSEREN on 26.3.2016.
 */
public class ListAdapter extends ArrayAdapter<Person> {

    public ListAdapter(Context context, int resource, List<Person> contacts) {
        super(context, resource,contacts);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.line_layout, null);
        }

        Person contact = getItem(position);

        if (contact != null) {
            ImageView personImage = (ImageView)v.findViewById(R.id.imageView);
            TextView Name = (TextView)v.findViewById(R.id.textView);
            TextView numbers = (TextView)v.findViewById(R.id.textView2);

            if(personImage != null && Name != null && numbers != null){
                Name.setText(contact.getName());
                numbers.setText((CharSequence) contact.getNumbers());
                personImage.setImageResource(contact.getPictureResourceID());
            }
        }



        return v;

    }

}

