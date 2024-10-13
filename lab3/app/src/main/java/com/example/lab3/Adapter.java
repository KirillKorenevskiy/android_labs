package com.example.lab3;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.CarViewHolder> {

    private List<Car> carList;

    public Adapter(List<Car> carList) {
        this.carList = carList;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view_item, parent, false);
        return new CarViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = carList.get(position);
        holder.textViewMark.setText("Mark: " + car.getCarMark());
        holder.textViewModel.setText("Model: " + car.getCarModel());
        holder.textViewYear.setText("Year: " + car.getCarYear());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailedCarActivity.class);
            intent.putExtra("carMark", car.getCarMark());
            intent.putExtra("carModel", car.getCarModel());
            intent.putExtra("carYear", car.getCarYear());
            intent.putExtra("carRun", car.getCarRun());
            intent.putExtra("dtpCity", car.getDtpCity());
            intent.putExtra("dtpCharacter", car.getDtpCharacter());
            intent.putExtra("horsePower", car.getHorsePower());
            intent.putExtra("engineMark", car.getEngineMark());
            intent.putExtra("numOfEngine", car.getNumOfEngine());
            intent.putExtra("transmissionType", car.getTransmissionType());
            intent.putExtra("phoneNumber", car.getPhoneNumber());
            intent.putExtra("email", car.getEmail());
            intent.putExtra("instLink", car.getInstLink());
            intent.putExtra("imagePath", car.getImagePath());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewMark;
        public TextView textViewModel;
        public TextView textViewYear;

        public CarViewHolder(View itemView) {
            super(itemView);
            textViewMark = itemView.findViewById(R.id.textViewMark);
            textViewModel = itemView.findViewById(R.id.textViewModel);
            textViewYear = itemView.findViewById(R.id.textViewYear);
        }
    }
}