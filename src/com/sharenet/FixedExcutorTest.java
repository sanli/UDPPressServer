package com.sharenet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FixedExcutorTest {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		System.out.println("mainThread:" + Thread.currentThread().getName());
		
		ExecutorService pool = Executors.newFixedThreadPool(16);
		
		for(int i = 0 ; i< 50000 ; i++){
			final int idx = i;
			long start = System.currentTimeMillis();
			pool.execute(new Runnable(){

				@Override
				public void run() {
					System.out.println("task" + idx + " thread:" + Thread.currentThread().getName());
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("task" + idx + " thread:" + Thread.currentThread().getName());
				}
				
			});
			System.out.println("start task"+ i +  " using time:" + (System.currentTimeMillis() - start));
		}
		pool.shutdown();
		pool.awaitTermination(10000,TimeUnit.MILLISECONDS);
	}

}
