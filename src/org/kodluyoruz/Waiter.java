package org.kodluyoruz;

import java.util.HashMap;
import java.util.Map;

public class Waiter extends Employee implements Runnable{

    private Bussiness bussiness;
    private HashMap<Customer,String> orders;
    private boolean isTimeToGo = false;
    private Thread thread;

    public Waiter(String name, Bussiness bussiness) {
        this.name = name;
        orders = new HashMap<>();
        this.bussiness = bussiness;
        start();
    }


    public HashMap<Customer, String> getOrders() {
        return orders;
    }

    public boolean getIsTimeToGo() {
        return isTimeToGo;
    }

    public void setIsTimeToGo(boolean timeToGo) {
        isTimeToGo = timeToGo;
    }

    public void setPriority(int newPro){
        Thread.currentThread().setPriority(newPro);
    }

    public void start () {
        if (thread == null) {
            thread = new Thread (this, name);
        }
        thread.start();
    }

    @Override
    public void run() {

        System.out.println(name + " is ready!");

        while(true){


            //Siparişi alınmamış kullanıcılardan sipariş alma
            Customer curCustomerForOrder = null;
            synchronized (bussiness.getCustomersWithEnterTime()){
                for(Customer customer: bussiness.getCustomersWithEnterTime().keySet()){
                    if(!bussiness.getOrderLog().containsKey(customer)){
                        curCustomerForOrder = customer;
                        bussiness.getOrderLog().put(curCustomerForOrder,"");
                        break;

                    }
                }
            }
            if(curCustomerForOrder != null){
                //Siparişi alınmamış müşterinin siparişi alınır.
                synchronized (bussiness.getOrderLog().get(curCustomerForOrder)){//İlgili müşterinin siparişine erişilmesini engelliyorum
                    //belli bir süre sipariş almaya giderken uyur.
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    bussiness.getOrderLog().put(curCustomerForOrder,curCustomerForOrder.toOrder());
                    System.out.println(name + ": " + curCustomerForOrder.getName() + "'in siparişini alındı.");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }



            Map.Entry<Customer,String> curOrderForServe = null;
            //Hazırlanan siparişleri teslim etme
            synchronized (bussiness.getReadyToServeOrders()){//Aynı siparişi iki farklı garson teslim etmesin diye
                for(Map.Entry<Customer,String> order: bussiness.getReadyToServeOrders().entrySet()){
                    curOrderForServe = order;
                    bussiness.getReadyToServeOrders().remove(order.getKey());
                    break;
                }
            }

            if(curOrderForServe != null){
                //Bir süre uyut, siparişi teslim etmeye gidiyor anlamında.
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                curOrderForServe.getKey().setIsMyMealServed(true);//yemeyi teslim edildi.
                System.out.println(name + ": " + curOrderForServe.getKey().getName() + "'in siparişi servis edildi.");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (curOrderForServe.getKey()){//Şimdiye kadar kalkmamışsa kalmasını engelliyorum çünkü ben göndericem.
                    if(!curOrderForServe.getKey().getIsTimeToGo()){
                        curOrderForServe.getKey().setIsTimeToGo(true);
                        System.out.println(name + ": " + curOrderForServe.getKey().getName() + " e gitmesi gerektiği bildirildi.");
                    }
                }
                bussiness.getCustomersWithEnterTime().remove(curOrderForServe.getKey());//Müşteri çıkış yaptı

            }

            //Dinlenme
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
