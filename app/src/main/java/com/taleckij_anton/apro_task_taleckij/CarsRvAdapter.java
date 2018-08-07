package com.taleckij_anton.apro_task_taleckij;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taleckij_anton.apro_task_taleckij.cars_db.DataModels.Car;
import com.taleckij_anton.apro_task_taleckij.cars_db.DataModels.Photo;

import java.util.ArrayList;

public class CarsRvAdapter extends RecyclerView.Adapter<CarsRvAdapter.CarViewHolder>{
    public class CarViewHolder extends RecyclerView.ViewHolder{
        public ImageView getCarPhotoView() {
            return itemView.findViewById(R.id.car_photo);
        }

        public TextView getCarPriceView() {
            return itemView.findViewById(R.id.car_price);
        }

        public TextView getCarBrandView() {
            return itemView.findViewById(R.id.car_brand);
        }

        public TextView getCarModelView() {
            return itemView.findViewById(R.id.car_model);
        }

        CarViewHolder(View itemView){
            super(itemView);

//            CardView cv = itemView.findViewById(R.id.cv);
            //carPrice = cv.findViewById(R.id.car_price);
            //carPhoto = cv.findViewById(R.id.car_photo);
            //carBrand = cv.findViewById(R.id.car_brand);
            //carModel = cv.findViewById(R.id.car_model);
            fetchUiItemsAsync();
        }
    }

    private Thread mThread;
    private final Handler mHandler = new Handler();
    private final ArrayList<Car> mCars;

    CarsRvAdapter(ArrayList<Car> cars){
        mCars = cars;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cars_rv_item_, parent,false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
//            holder.cv.setOnClickListener();
        final Car car = mCars.get(position);
        if(!car.getPhoto().isBitmapPhotoNull()) {
            holder.getCarPhotoView().setImageBitmap(car.getPhoto().getBitmapPhoto());
        }else{
            holder.getCarPhotoView().setImageDrawable(
                    holder.itemView.getContext().getDrawable(R.drawable.sample_car));
        }
        //TODO доллар в ресурсы
        holder.getCarPriceView().setText(String.valueOf(car.getPrice()) + "$");
        holder.getCarBrandView().setText(car.getBrand().getName());
//        Log.i("==================", car.getBrand().getName());
        holder.getCarModelView().setText(car.getModel().getName());
//        Log.i("==================", car.getModel().getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CarInfoActivity.class);
                intent = car.toIntent(intent);
                v.getContext().startActivity(intent);
            }
        });
    }

    private void fetchUiItemsAsync(){
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final int size = mCars.size();
                for (int i = 0; i < size; i++) {
                    final Car car = mCars.get(i);
                    if(car.getPhoto().isBitmapPhotoNull()) {
                        car.getPhoto().setBitmapPhoto(
                                Photo.getByteArrayAsBitmap(car.getPhoto().getBytesPhoto())
                        );
                    }
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        });
        mThread.start();
    }

    @Override
    public int getItemCount() {
        return mCars.size();
    }

}
