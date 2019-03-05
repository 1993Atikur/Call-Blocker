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
import android.widget.TextView;
import java.util.ArrayList;
import Data.UserData;
import spark.loop.callblocker.R;


public class CustomRecyclerView extends RecyclerView.Adapter<CustomRecyclerView.MyViewHolder> {
    Context context;
    LayoutInflater inflater;
    View view;
    public SparseBooleanArray booleanArray;
    ArrayList<UserData>data;
    public CustomRecyclerView(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        booleanArray = new SparseBooleanArray();
        data=new ArrayList<>();
    }

    @NonNull
    @Override
    public CustomRecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = inflater.inflate(R.layout.recyclerblock, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomRecyclerView.MyViewHolder myViewHolder, int i) {

        myViewHolder.Name.setText(data.get(i).getName());
        myViewHolder.Number.setText(data.get(i).getNumber());
        myViewHolder.keep(i);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void getUser(ArrayList<UserData>data){
        this.data=data;
        notifyDataSetChanged();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView Name, Number;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.textviewname);
            Number = itemView.findViewById(R.id.textviewnumber);
            checkBox = itemView.findViewById(R.id.recheckbox);
            itemView.setOnClickListener(this);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position=getAdapterPosition();
                    if (isChecked){
                        buttonView.setChecked(true);
                        booleanArray.put(position, true);

                    }else{
                        buttonView.setChecked(false);
                        booleanArray.put(position,false);

                    }
                }
            });
        }

        public void keep(int position) {

            if (!booleanArray.get(position, false)) {
                checkBox.setChecked(false);
            } else {
                checkBox.setChecked(true);

            }

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
    }






}
