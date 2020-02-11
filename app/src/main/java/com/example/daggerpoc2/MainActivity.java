package com.example.daggerpoc2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    //For field injection member mustn't be private or final
    @Inject
    public Car car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CarComponent component = DaggerCarComponent.create();
        //field injection is meant for framework types, that the android system instantiates
        component.inject(this );

        car = component.getCar();
        car.drive();
    }
}
