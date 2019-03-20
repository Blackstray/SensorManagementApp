package com.luta.semesterproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.SensorViewHolder>{

    private Context mCtx;
    private List<Sensor> sensorList;

    public SensorAdapter(Context mCtx, List<Sensor> sensorList) {
        this.mCtx = mCtx;
        this.sensorList = sensorList;
    }

    @NonNull
    @Override
    public SensorAdapter.SensorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SensorViewHolder(
                LayoutInflater.from(mCtx).inflate(R.layout.layout_sensor_info, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SensorAdapter.SensorViewHolder productViewHolder, int i) {
        Sensor sensor = sensorList.get(i);

        productViewHolder.textViewName.setText("Name: " + sensor.getName());
        productViewHolder.textViewType.setText("Type: " + sensor.getType());
        productViewHolder.textViewStatus.setText("Status: " + sensor.getStatus());
        productViewHolder.textViewAmperage.setText("Amperage: " +sensor.getAmperage()+ "A");
        productViewHolder.textViewPower.setText("Power: " + sensor.getPower()+ "kW");
        productViewHolder.textViewVoltage.setText("Voltage: " + sensor.getVoltage()+ "V");
    }

    @Override
    public int getItemCount() {
        return sensorList.size();
    }

    class SensorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewName, textViewType, textViewStatus, textViewAmperage, textViewPower, textViewVoltage;

        public SensorViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.sensor_textview_name);
            textViewType = itemView.findViewById(R.id.sensor_textview_type);
            textViewStatus = itemView.findViewById(R.id.sensor_textview_status);
            textViewAmperage = itemView.findViewById(R.id.sensor_textview_amperage);
            textViewPower = itemView.findViewById(R.id.sensor_textview_power);
            textViewVoltage = itemView.findViewById(R.id.sensor_textview_voltage);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Sensor sensor = sensorList.get(getAdapterPosition());
            /*Intent intent = new Intent(mCtx, UpdateProductActivity.class);
            intent.putExtra("sensor", sensor);
            mCtx.startActivity(intent);*/
        }
    }
}
