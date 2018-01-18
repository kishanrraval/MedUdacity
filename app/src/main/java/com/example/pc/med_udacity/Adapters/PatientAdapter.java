package com.example.pc.med_udacity.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.krish.medical_app.Java_classes.Patient;
import com.example.krish.medical_app.R;

import java.util.List;


public class PatientAdapter extends ArrayAdapter<Patient> //implements Filterable
{

    public List<Patient> objects;

    public PatientAdapter(Context context, List<Patient> patients)
    {
        super(context,0, patients);
        this.objects = patients;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Patient patient = getItem(position);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.my_patient_singleview, parent, false);

        TextView patient_gender = (TextView)rootView.findViewById(R.id.textView_my_patient_view_gender);
        TextView patient_age = (TextView)rootView.findViewById(R.id.textView_my_patient_view_age);
        TextView patient_name = (TextView)rootView.findViewById(R.id.textView_my_patient_view_name);
        ImageView gender = (ImageView) rootView.findViewById(R.id.imageView_my_patient_view_gender);

        patient_gender.setText(patient.getGender());
        patient_age.setText(patient.getAge()+" years");
        patient_name.setText(patient.getFirst_name() + " " +patient.getLast_name());

        if(patient.getGender().equals("Male"))
        {
           gender.setImageResource(R.drawable.male_gender);
        }
        else if(patient.getGender().equals("Female"))
        {
            gender.setImageResource(R.drawable.female_gender);
        }
        else if(patient.getGender().equals("Other"))
        {
            gender.setImageResource(R.drawable.other_gender);
        }



        return rootView;

    }

/*
    @NonNull
    @Override
    public Filter getFilter() {
        return myFilter;
    }

    Filter myFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            ArrayList<Patient> tempList=new ArrayList<>();
            //constraint is the result from text you want to filter against.
            //objects is your data set you will filter from
            if(constraint != null && objects!=null) {
                int length=objects.size();
                int i=0;
                while(i<length){
                    Patient item=objects.get(i);
                    //do whatever you wanna do here
                    //adding result set output array

                    tempList.add(item);

                    i++;
                }
                //following two lines is very important
                //as publish result can only take FilterResults objects
                filterResults.values = tempList;
                filterResults.count = tempList.size();
            }
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence contraint, FilterResults results) {
            objects = (ArrayList<Patient>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }


    };
*/
}
