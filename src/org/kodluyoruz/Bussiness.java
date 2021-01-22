package org.kodluyoruz;

import java.util.*;

public class Bussiness implements Runnable {

    private ArrayList<Chef> chefs;
    private ArrayList<Waiter> waiters;
    private HashMap<Customer,Long> customersWithEnterTime;
    private HashMap<Customer,String> orderLog;//sipariş listesi
    private HashMap<Customer,String> cookLog;//Pişirilme sırasına alınan veya pişirilen
    private HashMap<Customer,String> readyToServeOrders;//pişirilen, servis edilmeye hazır
    private int chefCount = 2;
    private int waiterCount = 3;
    private int tableCount = 5;
    private int customerCouter = 0;
    private long maxTimeForSitting = 10000L;
    private Calendar calendar ;
    private Thread thread;

    public Bussiness( int chefCount, int waiterCount, int tableCount, long maxTimeForSitting) {
        this.chefs = new ArrayList<>();
        this.waiters = new ArrayList<>();
        this.customersWithEnterTime = new HashMap<>();
        readyToServeOrders = new HashMap<>();
        cookLog = new HashMap<>();
        calendar = new GregorianCalendar();
        orderLog = new HashMap<>();
        this.chefCount = chefCount;
        this.waiterCount = waiterCount;
        this.tableCount = tableCount;
        this.maxTimeForSitting = maxTimeForSitting;
    }

    public Bussiness() {
        orderLog = new HashMap<>();
        chefs = new ArrayList<>();
        waiters = new ArrayList<>();
        calendar = new GregorianCalendar();
        customersWithEnterTime = new HashMap<>();
        cookLog = new HashMap<>();
        readyToServeOrders = new HashMap<>();
    }

    private boolean isGoodForNewCustomer() {//Random iki sayı üretilir ve birinin diğerinden büyük olup olmadığı kontrol edilir.
        Random rand = new Random();
        return rand.nextBoolean();
    }

    public void start () {
        if (thread == null) {
            thread = new Thread (this, "Bussiness");
        }
        thread.start();
    }


    @Override
    public void run() {

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        System.out.println("Dükkan açıldı.");

        for(int i = 0; i < chefCount; i++){
            chefs.add(new Chef("chef-" + (i+1),this));
        }


        for(int i = 0; i < waiterCount; i++){
            waiters.add(new Waiter("waiter-" + (i+1),this));
        }



        while(true){

            //Yeterli müşteri değilse müşteri kabul edilir.
            if(customersWithEnterTime.size() < tableCount){//müşteri ekle
                if(isGoodForNewCustomer()){//
                    Customer newCus = new Customer("customer-" + (customerCouter++));
                    newCus.start();
                    customersWithEnterTime.put(newCus, calendar.getTimeInMillis());
                }
            }

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private boolean isTimeToGo(Long entryTime) {

        return (calendar.getTimeInMillis()-entryTime) > maxTimeForSitting;

    }

    public ArrayList<Chef> getChefs() {
        return chefs;
    }

    public void setChefs(ArrayList<Chef> chefs) {
        this.chefs = chefs;
    }

    public ArrayList<Waiter> getWaiters() {
        return waiters;
    }

    public void setWaiters(ArrayList<Waiter> waiters) {
        this.waiters = waiters;
    }

    public HashMap<Customer, Long> getCustomersWithEnterTime() {
        return customersWithEnterTime;
    }

    public void setCustomersWithEnterTime(HashMap<Customer, Long> customersWithEnterTime) {
        this.customersWithEnterTime = customersWithEnterTime;
    }

    public int getChefCount() {
        return chefCount;
    }

    public void setChefCount(int chefCount) {
        this.chefCount = chefCount;
    }

    public int getWaiterCount() {
        return waiterCount;
    }

    public void setWaiterCount(int waiterCount) {
        this.waiterCount = waiterCount;
    }

    public int getTableCount() {
        return tableCount;
    }

    public void setTableCount(int tableCount) {
        this.tableCount = tableCount;
    }

    public long getMaxTimeForSitting() {
        return maxTimeForSitting;
    }

    public void setMaxTimeForSitting(long maxTimeForSitting) {
        this.maxTimeForSitting = maxTimeForSitting;
    }

    public HashMap<Customer, String> getOrderLog() {
        return orderLog;
    }

    public HashMap<Customer, String> getReadyToServeOrders() {
        return readyToServeOrders;
    }

    public HashMap<Customer, String> getCookLog() {
        return cookLog;
    }
}
