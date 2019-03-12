package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Data.UserData;
import spark.loop.callblocker.R;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.CallLogHolder> {
    Context context;
    LayoutInflater inflater;
    View view;
    public SparseBooleanArray booleanArray;
    ArrayList<UserData> data;

    public LogAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        booleanArray = new SparseBooleanArray();
        data = new ArrayList<>();

    }

    @NonNull
    @Override
    public CallLogHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = inflater.inflate(R.layout.recyclercalllog, viewGroup, false);
        CallLogHolder logHolder = new CallLogHolder(view);
        return logHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CallLogHolder callLogHolder, int i) {

        callLogHolder.Name.setText(data.get(i).getName()+"\n"+data.get(i).getNumber());
        callLogHolder.CallDate.setText(data.get(i).getDate());
        callLogHolder.keep(i);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void getUser(ArrayList<UserData> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public class CallLogHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView Name, Number, CallDate,Type;
        CheckBox checkBox;

        public CallLogHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.callName);
            CallDate= itemView.findViewById(R.id.callDate);
            checkBox = itemView.findViewById(R.id.callcheckBox);
            itemView.setOnClickListener(this);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = getAdapterPosition();
                    if (isChecked) {
                        buttonView.setChecked(true);
                        booleanArray.put(position, true);

                    } else {
                        buttonView.setChecked(false);
                        booleanArray.put(position, false);

                    }
                }
            });
        }

        @Override
        public void onClick(View v) {

            int adaptePosition = getAdapterPosition();
            if (!booleanArray.get(adaptePosition, false)) {
                checkBox.setChecked(true);
                booleanArray.put(adaptePosition, true);
            } else {
                checkBox.setChecked(false);
                booleanArray.put(adaptePosition, false);
            }

        }

        public void keep(int position) {

            if (!booleanArray.get(position, false)) {
                checkBox.setChecked(false);
            } else {
                checkBox.setChecked(true);

            }

        }
    }


}
