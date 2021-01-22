package org.kodluyoruz;

import java.util.HashMap;
import java.util.Map;

public class Chef extends Employee implements Runnable {

    Thread thread;
    HashMap<Customer,String> orders;
    Bussiness bussiness;

    public Chef(String name, Bussiness bussiness) {
        this.name = name;
        orders = new HashMap<>();
        this.bussiness = bussiness;
        start();
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
            Map.Entry<Customer,String> curOrder = null;//Hazırlamak için aldığım sipariş

            synchronized (bussiness.getOrderLog()){//Siparişlere aynı anda tek bir kişinin bakmasını sağlıyorum çünkü aynı siparişi iki farklı kişi hazırlamak için almasın
                for(Map.Entry<Customer,String> order: bussiness.getOrderLog().entrySet()){
                    if(!bussiness.getCookLog().containsKey(order.getKey())){
                        curOrder = order;
                        bussiness.getCookLog().put(curOrder.getKey(),curOrder.getValue());
                        break;//Yemek hazırlama işlemi bu blokta yapılmıyor çünkü bu blok diğer şefleri ve garsonların orderLog'a erişmesini engelliyor.
                    }
                }
            }

            //Alınan bir sipariş varsa pişirilir.
            if(curOrder != null){
                //thread'i bir süreliğine beklet, 2-4 sn arası olabilir, yemek hazırlanıyormuş gibi.
                try {
                    Thread.sleep(2200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                 System.out.println(name + ": " + curOrder.getKey().getName() + " siparişi (" + curOrder.getValue() + ") hazırlandı.");
                 bussiness.getReadyToServeOrders().put(curOrder.getKey(),curOrder.getValue());
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
