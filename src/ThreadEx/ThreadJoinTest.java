package ThreadEx;

public class ThreadJoinTest {

	public static void main(String ar[]){
	
		ThreadJoinTest join1 = new ThreadJoinTest();
		System.out.println("main 종료");
		
	}
	
	public ThreadJoinTest() {
		Thread t = new Thread(r);
		t.start();
		try{
			t.join();
		}catch(InterruptedException e){
			
		}
		
		
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
