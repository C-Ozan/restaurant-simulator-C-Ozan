package org.kodluyoruz;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Customer extends Employee implements Runnable{

    private String order = null;
    private List<String> foods = Arrays.asList("Dürüm, Ayran","Pizza, Kola","Kokoreç, Şalgam");
    private boolean isTimeToGo = false;
    private boolean isMyMealServed = false;
    private Thread thread;

    public Customer(String name) {
        this.name = name;
    }

    public void start () {
        if (thread == null) {
            thread = new Thread (this, name);
        }
        thread.start();
    }

    public String getName(){ return name; }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setIsMyMealServed(boolean isServed) {
        this.isMyMealServed = isServed;
    }

    public void setIsTimeToGo(Boolean isTimeToGo) {
        this.isTimeToGo = isTimeToGo;
    }

    public boolean getIsTimeToGo(){ return isTimeToGo; }

    public String getOrder() {
        return order;
    }

    public String toOrder(){//Sipariş vermek

        if(order == null){
            Random rand = new Random();
            int randIndex = rand.nextInt(foods.size());
            order = foods.get(randIndex);
        }

        return order;

    }

    @Override
    public void run() {
        System.out.println(name + " giriş yaptı.");

        while(!isTimeToGo){

            if(isMyMealServed){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(name + ": Yemek çok güzeldi.");
                isMyMealServed = false;//Yemek yeme işlemi tekrar edilsin istemiyorum.

                //yemek yedikten sonra kalkacak mı? Ya kendisi kalmayı seçer yada garson kalkması gerektiğini bildirir.
                if(isEnoughToSit()){
                    isTimeToGo = true;
                }
            }

        }

        System.out.println(name + " çıkış yaptı.");

    }

    private boolean isEnoughToSit() {
        Random rand = new Random();
        return rand.nextBoolean();
    }


}
