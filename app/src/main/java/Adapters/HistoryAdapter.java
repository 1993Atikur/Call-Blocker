package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import Data.UserData;
import Database.RejectedCalls;
import spark.loop.callblocker.R;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {

    ArrayList<UserData>data;
    Context context;
    View view;
    int p;
    LayoutInflater inflater;
    RejectedCalls rejectedCalls;
    public HistoryAdapter(Context context) {
        this.context=context;
        inflater=LayoutInflater.from(context);
        rejectedCalls=new RejectedCalls(context);

    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view=inflater.inflate(R.layout.historysingleblock,viewGroup,false);
        HistoryHolder holder=new HistoryHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryHolder historyHolder, final int i) {

        historyHolder.NameNumber.setText(data.get(i).getName()+"\n"+data.get(i).getNumber());
        historyHolder.HistoryDate.setText(data.get(i).getHistorydate());
        historyHolder.DeleteHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    rejectedCalls.Delete_Single_Number(data.get(i).getName(),data.get(i).getNumber(),data.get(i).getHistorydate());
                    data.remove(i);
                    notifyDataSetChanged();

                }catch (Exception e){}
            }
        });

    }

    public void setData(ArrayList<UserData>data){
        this.data=data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class HistoryHolder extends RecyclerView.ViewHolder{

            TextView NameNumber,HistoryDate;
            Button DeleteHistory;

        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            NameNumber=itemView.findViewById(R.id.historyName);
            HistoryDate=itemView.findViewById(R.id.historyDate);
            DeleteHistory=itemView.findViewById(R.id.historydelete);


        }


    }

}
