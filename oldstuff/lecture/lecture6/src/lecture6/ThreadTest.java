package lecture6;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTest {
	public static void main(String [] args){
		System.out.println("Before for loop");
		
		MyThread mt1 = new MyThread("a");
		MyThread mt2 = new MyThread("b");
		MyThread mt3 = new MyThread("c");
		mt3.setPriority(Thread.MAX_PRIORITY);
		mt2.setPriority(Thread.MIN_PRIORITY);
		mt1.setPriority(Thread.MIN_PRIORITY);

		mt1.start();
		mt2.start();
		mt3.start();
		/*
		ExecutorService executors = Executors.newCachedThreadPool();
		executors.execute(mt1);
		executors.execute(mt2);
		executors.execute(mt3);
		executors.shutdown();
		while(!executors.isTerminated()){
			Thread.yield();
		}
		
		try{
			mt1.join();
		}catch(InterruptedException ie){
			System.out.println("ie in main "+ ie);
		}*/
		System.out.println("Terminating main method");
		
	}
	
	
}

class MyThread extends Thread{
	private String name;
	public MyThread(String name){
		this.name=name;
	}
	public void run(){
		try{
			for(int i=0; i<20; i++){
				System.out.print(name + "-" +i + " ");
				if(name=="a") {
					//Thread.sleep(200);
					//Thread.yield();
				}
			}
			System.out.println();
		}finally{
			
		}
	}
	
}
