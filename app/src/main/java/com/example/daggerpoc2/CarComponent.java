package com.example.daggerpoc2;

import dagger.Component;

@Component
public interface CarComponent {

    //Provision method - uses getters
    Car getCar();

    void inject(MainActivity mainActivity);
}
