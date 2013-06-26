package ThreadEx;

public class DemonThread {

	public static void main(String ar[]){
		DemonThread daemon = new DemonThread();
		System.out.println("main 종료");
		
	}
	
	
	public DemonThread() {
	
		Thread t = new Thread(r);
		//daemon true | false
		t.setDaemon(true);
		t.start();
	}
	
	
	 Runnable r =  new Runnable() {
		public void run() {
			try{
				Thread.sleep(3000);
				System.out.println("Thread 종료");
			}catch(Exception e){
				
			}
		}
	};
	
	
}


