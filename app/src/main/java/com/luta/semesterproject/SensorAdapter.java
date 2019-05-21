package com.luta.semesterproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.SensorViewHolder>{

    private Context mCtx;
    private List<Sensor> sensorList;
    private String[] statuses = {"Active", "Malfunctioned", "Disabled"};

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
        if(sensor.getStatus().equals("Active"))
            productViewHolder.textViewStatus.setTextColor(Color.GREEN);
        else if (sensor.getStatus().equals( "Malfunctioned"))
            productViewHolder.textViewStatus.setTextColor(Color.RED);
        else if (sensor.getStatus().equals("Disabled"))
            productViewHolder.textViewStatus.setTextColor(Color.DKGRAY);
        else
            Log.i("SENSORS","Wrong sensor status - " + sensor.getStatus());

        productViewHolder.textViewStatus.setText(sensor.getStatus());
        productViewHolder.textViewName.setText("Name: " + sensor.getName());
        productViewHolder.textViewType.setText("Type: " + sensor.getType());
        productViewHolder.textViewAmperage.setText("Amperage: " +sensor.getAmperage()+ "A");
        productViewHolder.textViewPower.setText("Power: " + sensor.getPower()+ "kW");
        productViewHolder.textViewVoltage.setText("Voltage: " + sensor.getVoltage()+ "V");
        productViewHolder.textViewFloor.setText("Floor: "+ sensor.getFloor());
    }

    @Override
    public int getItemCount() {
        return sensorList.size();
    }


    class SensorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewFloor, textViewName, textViewType, textViewStatus, textViewAmperage, textViewPower, textViewVoltage;

        public SensorViewHolder(View itemView) {
            super(itemView);

            textViewFloor = itemView.findViewById(R.id.sensor_textview_floor);
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
            final Sensor sensor = sensorList.get(getAdapterPosition());
            new AlertDialog.Builder(mCtx)
                    .setTitle(R.string.change_status_dialog)
                    .setItems(statuses, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SensorsActivity.updateStatus(getAdapterPosition(), statuses[which]);
                            Log.i("SENSORS", "onClick: salala");
                        }
                    })

                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            /*Intent intent = new Intent(mCtx, UpdateProductActivity.class);
            intent.putExtra("sensor", sensor);
            mCtx.startActivity(intent);*/
        }
    }
}
